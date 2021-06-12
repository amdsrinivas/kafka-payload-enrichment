package com.assignment.kafkalistener.service;

import com.assignment.kafkalistener.dal.CountryDAL;
import com.assignment.kafkalistener.model.City;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class EnrichmentService {

    private static Logger LOGGER = LoggerFactory.getLogger(EnrichmentService.class) ;
    private CountryDAL countryDAL ;
    private RestTemplate restTemplate ;

    @Value("${population.mockwire.endpoint}")
    private String populationEndpoint ;

    @Autowired
    public EnrichmentService(CountryDAL countryDAL, RestTemplateBuilder restTemplateBuilder) {
        this.countryDAL = countryDAL;
        this.restTemplate = restTemplateBuilder.build() ;
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
    }

    public City enrichCountry(City city){
        // make call to DB.
        city.setCountry(countryDAL.getCountry(city.getCity()));
        LOGGER.info("Payload after enriching country : " + city);
        return city ;
    }
    public City enrichPopulation(City city){
        // make call to REST API.
        ResponseEntity<City> response ;
        response = restTemplate.getForEntity(populationEndpoint + city.getCity().toLowerCase(), City.class) ;
        if(response.getStatusCode()== HttpStatus.OK){
            LOGGER.info("Response received : " + response.getBody());
            city.setPopulation(
                    response.getBody().getPopulation()
            );
        }
        else{
            LOGGER.warn("Population not received from the endpoint. Status Code : " + response.getStatusCode() );
            city.setPopulation(-1);
        }
        return city ;
    }
}
