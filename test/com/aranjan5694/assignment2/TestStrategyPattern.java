package com.aranjan5694.assignment2;

import com.aranjan5694.assignment2.model.Student;
import com.aranjan5694.assignment2.model.strategy.AscendingGPAOrderingStrategy;
import com.aranjan5694.assignment2.model.strategy.AscendingNameOrderingStrategy;
import com.aranjan5694.assignment2.model.strategy.DescendingGPAOrderingStrategy;
import com.aranjan5694.assignment2.model.strategy.DescendingNameOrderingStrategy;
import com.aranjan5694.assignment2.model.strategy.OrderingContext;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStrategyPattern {

    private static final int ORDER = 3;

    private static List<Student> testData = Arrays.asList(
            new Student(101,"Abhishek",4.0),
            new Student(102,"Anuj",4.1),
            new Student(104,"Kshitij",3.8),
            new Student(102,"Hari",4.099),
            new Student(113,"Aasit", 2.2),
            new Student(114,"Sanatan",3.438),
            new Student(115,"Gaurav",3.439),
            new Student(116,"Saurav",2.8),
            new Student(117,"Saurabh",3.43),

            new Student(101,"Abhishek1",4.012),
            new Student(102,"Anuj1",4.01),
            new Student(104,"Kshiti1j",3.81),
            new Student(102,"Hari1",4.013),
            new Student(113,"Aasit1", 2.21),
            new Student(114,"Sanatan1",3.4315),
            new Student(115,"Gaurav1",3.4319),
            new Student(116,"Saurav1",2.81),
            new Student(117,"Saurabh1",3.431)
    );


    @Test
    void testAscendingNameOrderingStrategy(){

        TreeSet<Student> treeSet = new TreeSet<>(ORDER, new OrderingContext(new AscendingNameOrderingStrategy())
                .getOrderingStrategy());

        treeSet.addAll(testData);

        testData.sort(Comparator.comparing(Student::getName));
        List<Student> expectedOrder = testData;

        List<Student> actualOrder = new ArrayList<>(treeSet);

        assertEquals(expectedOrder, actualOrder);

    }

    @Test
    void testDescendingNameOrderingStrategy(){

        TreeSet<Student> treeSet = new TreeSet<>(ORDER, new OrderingContext(new DescendingNameOrderingStrategy())
                .getOrderingStrategy());

        treeSet.addAll(testData);

        testData.sort(Comparator.comparing(Student::getName).reversed());
        List<Student> expectedOrder = testData;

        List<Student> actualOrder = new ArrayList<>(treeSet);

        assertEquals(expectedOrder, actualOrder);

    }

    @Test
    void testDescendingGPAOrderingStrategy(){

        TreeSet<Student> treeSet = new TreeSet<>(ORDER, new OrderingContext(new DescendingGPAOrderingStrategy())
                .getOrderingStrategy());

        treeSet.addAll(testData);

        testData.sort(Comparator.comparing(Student::getGpa).reversed());
        List<Student> expectedOrder = testData;

        List<Student> actualOrder = new ArrayList<>(treeSet);

        assertEquals(expectedOrder, actualOrder);

    }

    @Test
    void testAscendingGPAOrderingStrategy(){

        TreeSet<Student> treeSet = new TreeSet<>(ORDER, new OrderingContext(new AscendingGPAOrderingStrategy())
                .getOrderingStrategy());

        treeSet.addAll(testData);

        testData.sort(Comparator.comparing(Student::getGpa));
        List<Student> expectedOrder = testData;

        List<Student> actualOrder = new ArrayList<>(treeSet);

        assertEquals(expectedOrder, actualOrder);

    }

}
