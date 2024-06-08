package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	  private final CustomerRepository customerRepository;

	    public CustomerController(CustomerRepository customerRepository) {
	        this.customerRepository = customerRepository;
	    }
	    @PostMapping
	    public Mono<Customer> createPerson(@RequestBody Customer person) {
	        return customerRepository.save(person);
	    }
	    @GetMapping
	    public Flux<Customer> getAllCustomers() {
	        return customerRepository.findAll();
	    }
	}


