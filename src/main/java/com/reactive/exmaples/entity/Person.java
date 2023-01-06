package com.reactive.exmaples.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {

    private String firstName;

    private String surname;

    public String getFullName() {
        return String.format("My name is %s %s.", firstName, surname);
    }

}
