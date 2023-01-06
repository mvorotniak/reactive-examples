package com.reactive.exmaples.command;

import com.reactive.exmaples.entity.Person;
import lombok.Data;

@Data
public class PersonCommand {

    public PersonCommand(final Person person) {
        this.firstName = person.getFirstName();
        this.surname = person.getSurname();
    }

    private String firstName;

    private String surname;

    public String getFullName() {
        return String.format("My name is %s %s.", firstName, surname);
    }

}
