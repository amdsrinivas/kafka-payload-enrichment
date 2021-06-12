package com.assignment.kafkalistener.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CountryDAL {

    private static Logger LOGGER = LoggerFactory.getLogger(CountryDAL.class) ;
    private JdbcTemplate jdbcTemplate ;
    private String countryQuery = "SELECT COUNTRY FROM COUNTRY_DETAILS WHERE UPPER(CITY) = UPPER('INPUT_CITY')" ;

    @Autowired
    public CountryDAL(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getCountry(String cityName){
        LOGGER.info("Querying the country for city : " + cityName);
        String result ;
        try{
            result = DataAccessUtils.singleResult(
                            jdbcTemplate.queryForList(
                                    countryQuery.replace("INPUT_CITY", cityName), String.class
                            )
                    ) ;

            if( null != result){
                LOGGER.info("Country queried : " + result);
                return result ;
            }
            else{
                LOGGER.warn("City not found in DB : " + cityName);
                return null ;
            }
        }
        catch (Exception e){
            LOGGER.error(e.getLocalizedMessage());
            return null ;
        }
    }
}
