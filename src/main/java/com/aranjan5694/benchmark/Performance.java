package com.aranjan5694.benchmark;

import com.aranjan5694.TreeSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Performance {
    private static final int ORDER = 1000000;

    public static void main(String[] args) {
        List<Integer> randomNumber = generateUniqueRandomNumber();

        System.out.println("Total execution time: " + testBTreeSet(randomNumber) + "ms");

        System.out.println("Total execution time: " + testBTree(randomNumber) + "ms");

//        testBTree();
    }
    private static long testBTree(List<Integer> randomNumber) {
        long startTime = System.currentTimeMillis();
        TreeSet<Integer> treeSet = new TreeSet<>(ORDER);
        for(int i: randomNumber) {
            treeSet.add(i);
        }

        treeSet.contains(randomNumber.get(randomNumber.size()/2));
        long endTime = System.currentTimeMillis();
        return (endTime-startTime) ;
    }

    private static long testBTreeSet(List<Integer> randomNumber) {
        long startTime = System.currentTimeMillis();
        java.util.TreeSet<Integer> treeSet = new java.util.TreeSet<>();
        for(int i: randomNumber) {
            treeSet.add(i);
        }

        treeSet.contains(randomNumber.get(randomNumber.size() / 2));
        //iteration in treeset
        for(int i: treeSet) {

        }
        long endTime = System.currentTimeMillis();
        return (endTime-startTime) ;
    }

    private static List<Integer> generateUniqueRandomNumber() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=1; i < 1000000; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        List<Integer> randomNumber = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            randomNumber.add(list.get(i));
        }
        return randomNumber;
    }

}
