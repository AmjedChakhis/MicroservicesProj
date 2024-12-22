package com.garage.invoice_service.service;

import com.garage.invoice_service.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
public class InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private static final String VEHICLE_SERVICE_URL = "http://localhost:8082/api/vehicles/";
    private static final String CLIENT_SERVICE_URL = "http://localhost:8081/api/clients/";

    @RabbitListener(queues = RabbitMQConfig.INVOICE_QUEUE_NAME)
    public void receiveInvoiceMessage(Map<String, Object> message) {
        logger.info("Received raw message for invoice: {}", message);

        Long vehicleId = (Long) message.get("vehicleId");
        if (vehicleId == null) {
            logger.error("Invalid message, vehicleId not found");
            return;
        }
        logger.info("Received message for vehicleId: {}", vehicleId);

        fetchVehicleOwnerId(vehicleId)
                .flatMap(ownerId -> {
                    logger.info("Fetched ownerId for vehicleId {}: {}", vehicleId, ownerId);
                    return fetchClientEmail(ownerId);
                })
                .timeout(Duration.ofSeconds(10))
                .subscribe(
                        email -> {
                            logger.info("Fetched email for ownerId: {}: {}", vehicleId, email);
                            sendInvoiceEmail(email, vehicleId);
                        },
                        error -> logger.error("Error processing message for vehicleId: {}", vehicleId, error)
                );
    }

    private Mono<Long> fetchVehicleOwnerId(Long vehicleId) {
        return webClientBuilder.build()
                .get()
                .uri(VEHICLE_SERVICE_URL + vehicleId)
                .retrieve()
                .bodyToMono(Long.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    logger.error("Error fetching vehicle owner ID for vehicleId: {}", vehicleId, e);
                    return Mono.error(new RuntimeException("Error fetching vehicle owner ID"));
                });
    }

    private Mono<String> fetchClientEmail(Long ownerId) {
        return webClientBuilder.build()
                .get()
                .uri(CLIENT_SERVICE_URL + ownerId)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    logger.error("Error fetching client email for ownerId: {}", ownerId, e);
                    return Mono.error(new RuntimeException("Error fetching client email"));
                });
    }

    private void sendInvoiceEmail(String email, Long vehicleId) {
        logger.info("Sending invoice email to: {}", email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Vehicle is Ready for Pickup");
        message.setText("Dear Vehicle Owner,\n\n" +
                "Your vehicle with ID: " + vehicleId + " has completed its maintenance.\n" +
                "Please find your invoice attached.\n\n" +
                "Thank you for choosing our service!");

        emailSender.send(message);
        logger.info("Invoice email sent to: {}", email);
    }
}
