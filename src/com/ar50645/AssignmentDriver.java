package com.ar50645;

import com.ar50645.assignment1.model.Student;

import java.util.LinkedList;
import java.util.stream.Stream;

public class AssignmentDriver {

    public static void main(String[] args) {

        //Implement a B-tree with order 3
        BTree<Student> bTree = new BTree<>(3);

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

        // Add element in BTree
        bTree.addElement(abhishek);
        bTree.addElement(anuj);
        bTree.addElement(kshitij);
        bTree.addElement(hari);
        bTree.addElement(aashit);
        bTree.addElement(sanatan);
        bTree.addElement(gaurav);
        bTree.addElement(saurav);
        bTree.addElement(saurabh);

        System.out.println(bTree);

        //Given a k,your code returns the kth element in the B-tree in lexicographical order. If k is out-of-bounds throw an exception.
        bTree.getElement(5);
        bTree.getElement(9); // gives out of bounds exception

        System.out.println("Print out the RedIds of the students that are on probation(GPA less than 2.85) that in the\n" +
                "list from the front to the back of the list.");

        printStudentOnProbation(bTree);

        System.out.println("Print out the names of the students with GPA of 4.0 in the list from the back to the front of\n" +
                "the list");

        printStudentWithGpaInReverseOrder(4.0, bTree);
    }

    public static void printStudentOnProbation(BTree<Student> bTree){
        bTree.traverse()
                .stream()
                .filter(student -> student.getGpa() < 2.85)
                .map(student -> student.getName())
                .forEach(System.out::println);
    }

    public static void printStudentWithGpaInReverseOrder(double GPA, BTree<Student> bTree){
        reverse(bTree
                .traverse()
                .stream())
                .filter(student -> student.getGpa() == GPA)
                .map(student -> student.getName())
                .forEach(System.out::println);
    }

    public static <T> Stream<T> reverse(Stream<T> stream)
    {
        LinkedList<T> stack = new LinkedList<>();
        stream.forEach(stack::push);
        return stack.stream();
    }
}