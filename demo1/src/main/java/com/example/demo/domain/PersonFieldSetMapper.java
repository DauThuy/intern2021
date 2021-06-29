package com.example.demo.domain;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class PersonFieldSetMapper implements FieldSetMapper<Person> {

    @Override
    public Person mapFieldSet(FieldSet fieldSet) throws BindException {
        return new Person(
                fieldSet.readInt("id"),
                fieldSet.readString("name"),
                fieldSet.readString("email"),
                fieldSet.readDate("dateOfBirth","yyyy-MM-dd"),
                fieldSet.readString("phoneNumber"),
                fieldSet.readString("gender"));
    }
}
