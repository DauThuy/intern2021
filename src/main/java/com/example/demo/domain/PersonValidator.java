package com.example.demo.domain;

import Utils.ValidatePerson;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

public class PersonValidator implements Validator<Person> {

    @Override
    public void validate(Person person) throws ValidationException {
        if (!ValidatePerson.validateEmail(person.getEmail())) {
            System.out.println("invalid email");
            throw new ValidationException("invalid email");
        }

        if (!ValidatePerson.validatePhone(person.getPhoneNumber())) {
            System.out.println("invalid phone]");
            throw new ValidationException("invalid phone");
        }
    }
}
