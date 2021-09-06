package com.ar50645;

import com.ar50645.assignment1.model.Student;

import java.util.LinkedList;
import java.util.stream.Stream;

public class Assignment1 {

    public static void main(String[] args) {
	// write your code here
        BTree<Student> bTree = new BTree<>(3);

        bTree.addElement(new Student(101,"Abhishek",4.0));
        bTree.addElement(new Student(102,"Anuj",4.0));
        bTree.addElement(new Student(103,"Hari",4.0));
        bTree.addElement(new Student(104,"Kshitij",2.8));
        bTree.addElement(new Student(105,"Shreeraj1",2.8));
        bTree.addElement(new Student(106, "Adhishek", 3.8));
        bTree.addElement(new Student(107,"Anujaa",4.0));
        bTree.addElement(new Student(108,"Dheeraj", 3.9));
        bTree.addElement(new Student(109,"Shreeraj2", 4.0));
        bTree.addElement(new Student(110,"Ksitij1",3.998));
        bTree.addElement(new Student(111,"Ksitij2",3.998));
        bTree.addElement(new Student(112, "Dhanrj",2.85));
        bTree.addElement(new Student(113,"Aasit", 2.2));
        bTree.addElement(new Student(114,"Sanatan",3.43));
        bTree.addElement(new Student(115,"Gaurav",3.43));
        bTree.addElement(new Student(116,"Saurav",3.43));
        bTree.addElement(new Student(117,"Saurabh",3.43));
        bTree.addElement(new Student(118,"Patil",3.43));

        System.out.println(bTree);
//
        System.out.println("Print out the RedIds of the students that are on probation(GPAlessthan2.85)thatinthe\n" +
                "list from the front to the back of the list.");
        bTree.traverse()
                .stream()
                .filter(student -> student.getGpa() < 2.85)
                .map(student -> student.getName())
                .forEach(System.out::println);
//
        System.out.println("Print out the names of the students with GPA of 4.0 in the list from the back to the front of\n" +
                "the list");
        reverse(bTree
                .traverse()
                .stream())
            .filter(student -> student.getGpa() == 4.0)
            .map(student -> student.getName())
            .forEach(System.out::println);

        System.out.println(bTree.traverse());
        System.out.println(bTree.traverse().size());
        System.out.println(bTree.getElement(17));
    }
    public static <T> Stream<T> reverse(Stream<T> stream)
    {
        LinkedList<T> stack = new LinkedList<>();
        stream.forEach(stack::push);

        return stack.stream();
    }
}