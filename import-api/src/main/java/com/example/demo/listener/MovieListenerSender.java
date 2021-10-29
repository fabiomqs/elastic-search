package com.example.demo.listener;

import com.example.demo.event.MovieEvent;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.StringWriter;

@Slf4j
@Component
public class MovieListenerSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app-config.rabbit.exchange.movie}")
    private String movieTopicExchange;

    @Value("${app-config.rabbit.routingKey.movie}")
    private String movieKey;

    public MovieListenerSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listenMovieEventAndSendNotification(MovieEvent movieEvent) {
        try {
            JsonFactory factory = new JsonFactory();
            StringWriter jsonObjectWriter = new StringWriter();
            JsonGenerator generator = factory.createGenerator(jsonObjectWriter);
            generator.useDefaultPrettyPrinter(); // pretty print JSON
            generator.writeStartObject();
            generator.writeFieldName("id_movie");
            generator.writeNumber(movieEvent.getId());
            generator.writeFieldName("message");
            generator.writeString("Movie Change");
            String message = jsonObjectWriter.toString();
            log.info("Sending message: {}", new ObjectMapper().writeValueAsString(message));
            rabbitTemplate.convertAndSend(movieTopicExchange, movieKey, message);
            log.info("Message was sent successfully!");
        } catch (Exception ex) {
            log.error("Error while trying to send message: ", ex);
        }
    }
}
