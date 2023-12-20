package com.example.r2EncTest;

import java.util.Arrays;
import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import reactor.core.publisher.Flux;

@SpringBootApplication
public class R2EncTestApplication {
/*
	   @Autowired
	    private EncryptionUtil encryptionUtil;
*/
	
	    public static void main(String[] args) {
	        SpringApplication.run(R2EncTestApplication.class, args);
	    }
	    private String encryptionKey= "pass12345";
	    @Bean
	    CommandLineRunner init(PersonRepository repository, EncryptionUtil encryptionUtil) {
	        return args -> {
	        	List<Person> persons = Arrays.asList(
	                    new Person("Jan", "jan@gmail.com"),
	                    new Person("Mo", "mo@gmail.com")
	                );

	                Flux.fromIterable(persons)
	                    .flatMap(person -> {
	                        try {
	                            String encryptedEmail = EncryptionUtil.encrypt(person.getEmail(), encryptionKey);
	                            System.out.println("Encrypting email for: " + person.getName() + " - " + encryptedEmail);
	                            person.setEmail(encryptedEmail);
	                            return repository.save(person);
	                        } catch (Exception e) {
	                            return Flux.error(e);
	                        }
	                    })
	            .thenMany(repository.findAll())
	            .flatMap(person -> {
	                try {
	                    String decryptedEmail = EncryptionUtil.decrypt(person.getEmail(),encryptionKey);
	                    System.out.println("Decrypting email for: " + person.getName() + " - " + decryptedEmail);
	                    person.setEmail(decryptedEmail);
	                    return Flux.just(person);
	                } catch (Exception e) {
	                    return Flux.error(e);
	                }
	            })
	            .subscribe();
	            //.subscribe(person -> System.out.println("Ergebnise: " + person.getName() + person.getEmail()));
	        };
	    }
	    
	   /* @Bean
	    CommandLineRunner run() {
	        return args -> {
	            String originalData = "Hello";
	            String password = "12345pass"; 
	            try {
	                String encryptedData = EncryptionUtil.encrypt(originalData, password);
	                System.out.println("Encrypted Data: " + encryptedData);

	                String decryptedData = EncryptionUtil.decrypt(encryptedData, password);
	                System.out.println("Decrypted Data: " + decryptedData);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        };
	    }*/
	}