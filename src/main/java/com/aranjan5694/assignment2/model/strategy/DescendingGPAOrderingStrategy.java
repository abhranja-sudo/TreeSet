package com.aranjan5694.assignment2.model.strategy;

import com.aranjan5694.assignment2.model.Student;
import java.util.Comparator;

public class DescendingGPAOrderingStrategy implements OrderingStrategy{
    @Override
    public Comparator<Student> order() {
        return Comparator.comparing(Student::getGpa).reversed();
    }
}
