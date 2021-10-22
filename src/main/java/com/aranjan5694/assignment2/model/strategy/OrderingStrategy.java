package com.aranjan5694.assignment2.model.strategy;

import com.aranjan5694.assignment2.model.Student;

import java.util.Comparator;

public interface OrderingStrategy {
    Comparator<Student> order();
}
