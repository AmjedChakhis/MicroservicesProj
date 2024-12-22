package com.garage.notification_service.service;

import com.garage.notification_service.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String VEHICLE_SERVICE_URL = "http://localhost:8082/api/vehicles/";
    private static final String CLIENT_SERVICE_URL = "http://localhost:8081/api/clients/";

    // Listen to messages on the queue
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(Map<String, Object> message) {
        logger.info("Received raw message: {}", message); // Log raw message for debugging

        Long vehicleId = (Long) message.get("vehicleId");
        if (vehicleId == null) {
            logger.error("Invalid message, vehicleId not found");
            return;
        }
        logger.info("Received message for vehicleId: {}", vehicleId);

        // Fetch vehicle owner ID from the Vehicle Service
        fetchVehicleOwnerId(vehicleId)
                .flatMap(ownerId -> {
                    logger.info("Fetched ownerId for vehicleId {}: {}", vehicleId, ownerId);
                    return fetchClientEmail(ownerId);  // Fetch client's email using owner ID
                })
                .timeout(Duration.ofSeconds(10))  // Timeout after 10 seconds
                .doOnTerminate(() -> logger.info("Finished processing message for vehicleId: {}", vehicleId))
                .subscribe(
                        email -> {
                            logger.info("Fetched email for ownerId: {}: {}", vehicleId, email);
                            sendEmailNotification(email, vehicleId);
                        },
                        error -> {
                            logger.error("Error processing message for vehicleId: {}", vehicleId, error);
                        }
                );
    }

    // Fetch vehicle owner's ID from Vehicle Service
    private Mono<Long> fetchVehicleOwnerId(Long vehicleId) {
        String url = VEHICLE_SERVICE_URL + vehicleId;

        logger.info("Requesting vehicle owner ID from Vehicle Service for vehicleId: {}", vehicleId);

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Long.class)
                .timeout(Duration.ofSeconds(5))  // Timeout after 5 seconds
                .doOnError(e -> logger.error("Error fetching vehicle owner ID for vehicleId: {}", vehicleId, e))
                .onErrorResume(e -> Mono.error(new RuntimeException("Error fetching vehicle owner ID")));
    }

    // Fetch client's email using the owner's ID from Client Service
    private Mono<String> fetchClientEmail(Long ownerId) {
        String url = CLIENT_SERVICE_URL + ownerId;

        logger.info("Requesting client email from Client Service for ownerId: {}", ownerId);

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))  // Timeout after 5 seconds
                .doOnError(e -> logger.error("Error fetching client email for ownerId: {}", ownerId, e))
                .onErrorResume(e -> Mono.error(new RuntimeException("Error fetching client email")));
    }

    // Send email to the vehicle owner
    private void sendEmailNotification(String email, Long vehicleId) {
        logger.info("Sending email to owner with email: {} for vehicleId: {}", email, vehicleId);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Task Scheduled Notification");
        message.setText("Dear Vehicle Owner, your vehicle with ID: " + vehicleId + " has been scheduled for maintenance.");

        emailSender.send(message);
        logger.info("Email sent to: {}", email);
    }
}
