package com.assignment.kafkalistener.listener;

import com.assignment.kafkalistener.model.City;
import com.assignment.kafkalistener.producer.PayloadProducer;
import com.assignment.kafkalistener.service.EnrichmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CityListener {

    private static Logger LOGGER = LoggerFactory.getLogger(CityListener.class) ;
    private EnrichmentService enrichmentService ;
    private PayloadProducer payloadProducer ;
    @Autowired
    public CityListener(EnrichmentService enrichmentService, PayloadProducer payloadProducer) {
        this.enrichmentService = enrichmentService;
        this.payloadProducer = payloadProducer ;
    }
    @Value("${producer.topic}")
    private String producerTopic ;

    @KafkaListener(
            topics = "${listener.topic}",
            containerFactory = "greetingKafkaListenerContainerFactory",
            groupId = "city-listener-group"
    )
    public void consume(City city){

        LOGGER.info("Received : " + city);
        LOGGER.info("Calling enrichment service");
        try{
            city = enrichmentService.enrichCountry(city) ;
            city = enrichmentService.enrichPopulation(city) ;
            LOGGER.info("After enrichment : " + city);
            payloadProducer.sendMessage(city, producerTopic);
        }
        catch (Exception e){
            LOGGER.error(e.getLocalizedMessage());
        }
    }

}
