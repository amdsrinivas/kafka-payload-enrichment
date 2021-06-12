package com.assignment.kafkalistener.producer;

import com.assignment.kafkalistener.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PayloadProducer {
    private final KafkaTemplate kafkaTemplate ;

    @Autowired
    public PayloadProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(City city, String publishTopic){
        kafkaTemplate.send(publishTopic, city) ;
    }
}
