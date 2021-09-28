package com.aranjan5694.assignment2;

import com.aranjan5694.assignment2.model.strategy.GPAOrderingStrategy;
import com.aranjan5694.assignment2.model.strategy.NameOrderingStrategy;
import com.aranjan5694.assignment2.model.strategy.OrderingContext;
import com.aranjan5694.assignment2.model.Student;

import java.util.*;
import java.util.stream.Stream;

/**
 * Driver class to test assignment
 */
public class AssignmentDriver {

    public static void main(String[] args) {

        //Implement a B-tree with getOrderingStrategy 3
        OrderingContext orderingByName = new OrderingContext(new NameOrderingStrategy());
        OrderingContext orderingByGPA = new OrderingContext(new GPAOrderingStrategy());

//        java.util.TreeSet utilSet = new java.util.TreeSet();
//        utilSet.forEach(System.out::println);

//        TreeSet<Student> bTree = new TreeSet<>(3, orderingByName.getOrderingStrategy());
        TreeSet<Student> bTree = new TreeSet<>(3, orderingByGPA.getOrderingStrategy());

        //Each element in the B-tree contains a Student object. A Student has a name, redid and GPA
        Student abhishek = new Student(101,"Abhishek",4.0);
        Student anuj = new Student(102,"Anuj",4.0);
        Student kshitij = new Student(104,"Kshitij",3.8);
        Student hari = new Student(102,"Hari",4.0);
        Student aashit = new Student(113,"Aasit", 2.2);
        Student sanatan = new Student(114,"Sanatan",3.43);
        Student gaurav = new Student(115,"Gaurav",3.43);
        Student saurav = new Student(116,"Saurav",2.8);
        Student saurabh = new Student(117,"Saurabh",3.43);
//        utilSet.add(abhishek);
//        utilSet.add(anuj);
//        utilSet.add(hari);
//        System.out.println(utilSet);

        // Add element in BTree
        bTree.add(abhishek);
        bTree.add(sanatan);
        bTree.add(anuj);
        bTree.add(kshitij);
        bTree.add(hari);
        bTree.add(aashit);
        bTree.add(sanatan);
        bTree.add(gaurav);
        bTree.add(saurav);
        bTree.add(saurabh);

        //Given a k,your code returns the kth element in the B-tree in lexicographical getOrderingStrategy.
        // If k is out-of-bounds throw an exception.
        System.out.println(bTree.getElement(5));

//        // Out of bounds exception
//        try{
//            bTree.getElement(9);
//        }catch (IndexOutOfBoundsException e){
//            e.printStackTrace();
//        }

        System.out.println("Print out the RedIds of the students that are on probation(GPA less than 2.85) that in the\n" +
                "list from the front to the back of the list.");

        printStudentOnProbation(bTree);

        System.out.println("Print out the names of the students with GPA of 4.0 in the list from the back to the front of\n" +
                "the list");

        printStudentWithGpaInReverseOrder(4.0, bTree);

        System.out.println("iterator running  ... ");
        for (Student student : bTree) {
                System.out.println(student);
        }

//        bTree.forEach(System.out::println);
    }

    public static void printStudentOnProbation(TreeSet<Student> bTree){
        bTree.stream()
                .filter(student -> student.getGpa() < 2.85)
                .map(Student::getName)
                .forEach(System.out::println);
    }

    public static void printStudentWithGpaInReverseOrder(double GPA, TreeSet<Student> bTree){
        reverse(bTree
                .stream())
                .filter(student -> student.getGpa() == GPA)
                .map(Student::getName)
                .forEach(System.out::println);
    }

    public static <T> Stream<T> reverse(Stream<T> stream)
    {
        LinkedList<T> stack = new LinkedList<>();
        stream.forEach(stack::push);
        return stack.stream();
    }
}