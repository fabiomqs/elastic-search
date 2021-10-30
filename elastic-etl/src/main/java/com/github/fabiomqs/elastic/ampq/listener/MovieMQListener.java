package com.github.fabiomqs.elastic.ampq.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fabiomqs.elastic.ampq.dto.MovieMessageDTO;
import com.github.fabiomqs.elastic.service.ElasticService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class MovieMQListener {

    private final ElasticService elasticService;

    public MovieMQListener(ElasticService elasticService) {
        this.elasticService = elasticService;
    }

    @RabbitListener(queues = "${app.rabbit.queue}")
    public void recieveMovieMessage(MovieMessageDTO message) throws Exception {
        System.out.println("Recieving message with data: {" +
                new ObjectMapper().writeValueAsString(message) + "}");
        elasticService.indexMovie(message.getLongIdMovie());
    }

}
