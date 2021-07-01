package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private Integer id;
    private String name;
    private String email;
    private String dateOfBirth;
    private String phoneNumber;
    private String gender;
    @Override
    public String toString() {
        return this.id+"-"+this.name+"-"+this.email+"-"+this.dateOfBirth+"-"+this.phoneNumber+"-"+this.gender;
    }
}
