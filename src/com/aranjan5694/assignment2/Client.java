package com.aranjan5694.assignment2;

import com.aranjan5694.assignment2.model.strategy.*;
import com.aranjan5694.assignment2.model.Student;

import java.util.*;
import java.util.stream.Stream;

/**
 * Driver class to test assignment
 */
public class Client {

    public static void main(String[] args) {

        //Implement a B-tree with getOrderingStrategy 3
        OrderingContext orderingByName = new OrderingContext(new AscendingNameOrderingStrategy());
//        OrderingContext orderingByGPA = new OrderingContext(new AscendingGPAOrderingStrategy());

//        java.util.TreeSet utilSet = new java.util.TreeSet();
//        utilSet.forEach(System.out::println);

        TreeSet<Student> bTree = new TreeSet<>(3, orderingByName.getOrderingStrategy());
//        TreeSet<Student> bTree = new TreeSet<>(3);

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



        Student abhishek1 = new Student(101,"Abhishek1",4.0);
        Student anuj1 = new Student(102,"Anuj1",4.0);
        Student kshitij1 = new Student(104,"Kshiti1j",3.8);
        Student hari1 = new Student(102,"Hari1",4.0);
        Student aashit1 = new Student(113,"Aasit1", 2.2);
        Student sanatan1 = new Student(114,"Sanatan1",3.43);
        Student gaurav1 = new Student(115,"Gaurav1",3.43);
        Student saurav1 = new Student(116,"Saurav1",2.8);
        Student saurabh1 = new Student(117,"Saurabh1",3.43);
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

        bTree.add(abhishek1);
        bTree.add(sanatan1);
        bTree.add(anuj1);
        bTree.add(kshitij1);
        bTree.add(hari1);
        bTree.add(aashit1);
        bTree.add(sanatan1);
        bTree.add(gaurav1);
        bTree.add(saurav1);
        bTree.add(saurabh1);

        System.out.println("iterator running  ... ");
        for (Student student : bTree) {
            System.out.println(student);
        }

        int k = 7;
        System.out.println(" \n get element at k...." + k);
        //Given a k,your code returns the kth element in the B-tree in lexicographical getOrderingStrategy.
        // If k is out-of-bounds throw an exception.
        System.out.println(bTree.getElement(k));

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