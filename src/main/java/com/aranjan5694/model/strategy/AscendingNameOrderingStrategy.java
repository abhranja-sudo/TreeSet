package com.aranjan5694.model.strategy;

import com.aranjan5694.model.Student;

import java.util.Comparator;

public class AscendingNameOrderingStrategy implements OrderingStrategy{
    @Override
    public Comparator<Student> order() {
        return Comparator.comparing(Student::getName);
    }
}
