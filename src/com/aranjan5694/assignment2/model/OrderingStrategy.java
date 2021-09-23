package com.aranjan5694.assignment2.model;

import java.util.Comparator;

public interface OrderingStrategy {
    Comparator<Student> order();
}
