package com.fraktalio.courier;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CourierApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourierApplication.class, args);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer(){
        return builder ->
                builder.visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }
}
