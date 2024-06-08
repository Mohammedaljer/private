package com.example.demo;

import java.io.IOException;
import java.time.Duration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    @Profile("h2")
    CommandLineRunner h2Runner(CustomerRepository repository, ObjectMapper objectMapper) {
        System.out.println("/////////////////////h2/////////////////////");
        return createRunner(repository, objectMapper);
    }

    @Bean
    @Profile("mariadb")
    CommandLineRunner mariadbRunner(CustomerRepository repository, ObjectMapper objectMapper) {
        System.out.println("/////////////////////MariaDB/////////////////////");
        return createRunner(repository, objectMapper);
    }

    @Bean
    @Profile("postgres")
    CommandLineRunner postgresRunner(CustomerRepository repository, ObjectMapper objectMapper) {
        System.out.println("/////////////////////PostgreSQL/////////////////////");
        return createRunner(repository, objectMapper);
    }
    MappingR2dbcConverter x;
    private CommandLineRunner createRunner(CustomerRepository repository, ObjectMapper objectMapper) {
        return args -> {
        	JsonNode jsonNodeJohn = createJsonNode(objectMapper, "john@gmail.com");
            Customer customerJohn = new Customer(null, "John", "Doe", jsonNodeJohn);

            JsonNode jsonNodeMo = createJsonNode(objectMapper, "mog@out.com");
            Customer customerMo = new Customer(null, "Mo", "J", jsonNodeMo);

            repository.save(customerJohn).block(Duration.ofSeconds(10));
            repository.save(customerMo).block(Duration.ofSeconds(10));
          /*  JsonNode jsonNode = createJsonNode(objectMapper, "John@gmail.com");
            List<Customer> newCustomers = Arrays.asList(new Customer(null, "John", "Doe", jsonNode));

            for (Customer newCustomer : newCustomers) {
                repository.save(newCustomer).block(Duration.ofSeconds(10));
            }*/

            log.info("Customers found with findAll() after update/insert:");
            log.info("-------------------------------");
            repository.findAll().doOnNext(customer -> log.info(customer.toString())).blockLast(Duration.ofSeconds(10));
            log.info("");
        };
    }
/*JsonNode rootNode = objectMapper.readTree(source);
String name = rootNode.get("name").asText(); */
    
    private JsonNode createJsonNode(ObjectMapper objectMapper, String email) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("Email", email);
        //node.put("lastName", lastName);
        return node;
    }

    @WritingConverter
    public static class JsonNodeWritingConverter implements Converter<JsonNode, String> {
        private final ObjectMapper objectMapper;

        public JsonNodeWritingConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public String convert(JsonNode source) {
            try {
                return objectMapper.writeValueAsString(source);
            } catch (IOException e) {
                throw new RuntimeException("Error writing JSON", e);
            }
        }
    }

    @ReadingConverter
    public static class JsonNodeReadingConverter implements Converter<String, JsonNode> {
        private final ObjectMapper objectMapper;

        public JsonNodeReadingConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public JsonNode convert(String source) {
            try {
                return objectMapper.readTree(source);
            } catch (IOException e) {
                throw new RuntimeException("Error reading JSON", e);
            }
        }
    }
}
/*JsonNode is a base class in the Jackson library that represents an immutable JSON element. 
It can hold any type of JSON data, such as objects, arrays, strings, numbers, booleans, or null.
ObjectNode is a concrete subclass of JsonNode that represents a JSON object, i.e., a set of key-value pairs where the keys are strings.
Usage:
It's used for constructing or modifying JSON objects.
Provides methods to add, update, or remove fields from the JSON object.
ObjectMapper : A class from the Jackson library used for converting between Java objects and JSON.*/