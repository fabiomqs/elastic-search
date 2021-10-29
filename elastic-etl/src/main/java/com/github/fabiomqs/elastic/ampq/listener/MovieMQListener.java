package com.github.fabiomqs.elastic.ampq.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabiomqs.elastic.ampq.dto.MovieMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MovieMQListener {

    @RabbitListener(queues = "${app-config.rabbit.queue.movie")
    public void recieveProductStockMessage(MovieMessageDTO message) throws JsonProcessingException {
        log.info("Recieving message with data: {}",
                new ObjectMapper().writeValueAsString(message));

    }



}
