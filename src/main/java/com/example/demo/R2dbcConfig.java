package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import com.example.demo.DemoApplication.JsonNodeReadingConverter;
import com.example.demo.DemoApplication.JsonNodeWritingConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class R2dbcConfig {

    @Bean
    R2dbcCustomConversions r2dbcCustomConversions() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new JsonNodeWritingConverter(objectMapper));
        converters.add(new JsonNodeReadingConverter(objectMapper));

        var storeConversions = CustomConversions.StoreConversions.NONE;
        return new R2dbcCustomConversions(storeConversions, converters);
    }
   
}



 
