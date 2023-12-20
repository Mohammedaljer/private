package com.example.r2EncTest;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("person")
public class Person {

    @Id
    private Long id;
    private String name;
    private String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
        
    }
}
