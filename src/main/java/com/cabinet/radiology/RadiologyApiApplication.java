package com.cabinet.radiology;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RadiologyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RadiologyApiApplication.class, args);
    }
}
