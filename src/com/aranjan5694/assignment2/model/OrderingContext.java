package com.aranjan5694.assignment2.model;

import java.util.Comparator;

public class OrderingContext {
    private OrderingStrategy strategy;

    public OrderingContext(OrderingStrategy strategy) {
        this.strategy = strategy;
    }

    public Comparator<Student> getOrderingStrategy(){
        return strategy.order();
    }
}
