package com.aranjan5694.assignment2.model;

import java.util.Comparator;

public class NameOrderingStrategy implements OrderingStrategy{

    @Override
    public Comparator<Student> order() {
        return Comparator.comparing(Student::getName);
    }
}
