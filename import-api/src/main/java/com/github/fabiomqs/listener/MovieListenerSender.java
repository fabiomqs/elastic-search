package com.github.fabiomqs.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabiomqs.dto.MovieMessageDTO;
import com.github.fabiomqs.event.MovieEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
public class MovieListenerSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbit.exchange}")
    private String movieTopicExchange;

    @Value("${app.rabbit.routingKey}")
    private String movieKey;

    public MovieListenerSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listenMovieEventAndSendNotification(MovieEvent movieEvent) {
        try {
            MovieMessageDTO message = new MovieMessageDTO(movieEvent.getId() + "", "Movie Change");
            System.out.println("Sending message: {" +  new ObjectMapper().writeValueAsString(message) + "}");
            rabbitTemplate.convertAndSend(movieTopicExchange, movieKey, message);
            System.out.println("Message was sent successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
