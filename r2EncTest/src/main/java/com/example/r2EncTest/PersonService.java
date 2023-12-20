package com.example.r2EncTest;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private String encryptionKey= "pass12345";
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Mono<Person> savePerson(Person person) {
        try {
            
            String encryptedEmail = EncryptionUtil.encrypt(person.getEmail(), encryptionKey);
            person.setEmail(encryptedEmail);
            return personRepository.save(person);
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    public Mono<Person> getPerson(Long id) {
        return personRepository.findById(id)
                .flatMap(person -> {
                    try {
                        
                        String decryptedEmail = EncryptionUtil.decrypt(person.getEmail(), encryptionKey);
                        person.setEmail(decryptedEmail);
                        return Mono.just(person);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }
    public Flux<Person> getAllPersons() {
        return personRepository.findAll()
                .flatMap(person -> {
                    try {
                        String decryptedEmail = EncryptionUtil.decrypt(person.getEmail(), "pass12345");
                        person.setEmail(decryptedEmail);
                        return Mono.just(person);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
}
}

