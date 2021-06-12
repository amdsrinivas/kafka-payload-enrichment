package com.assignment.kafkalistener.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class City {
    private String city;
    private String country ;
    private int population ;

    @JsonCreator
    public City(@JsonProperty("city") String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + city + '\'' +
                ", country='" + country + '\'' +
                ", population=" + population +
                '}';
    }
}
