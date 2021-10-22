package com.aranjan5694.model.strategy;

import com.aranjan5694.model.Student;

import java.util.Comparator;

public interface OrderingStrategy {
    Comparator<Student> order();
}
