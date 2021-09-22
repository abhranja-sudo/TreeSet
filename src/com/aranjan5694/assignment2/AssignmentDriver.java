package com.aranjan5694.assignment2;

import apple.laf.JRSUIUtils;
import com.aranjan5694.assignment2.model.Student;

import java.util.*;
import java.util.stream.Stream;

/**
 * Driver class to test assignment
 */
public class AssignmentDriver {

    public static void main(String[] args) {

//        SortedSet<Map.Entry<String, Double>> sortedset = new java.util.TreeSet<>(
//                new Comparator<Map.Entry<String, Double>>() {
//                    @Override
//                    public int compare(Map.Entry<String, Double> e1,
//                                       Map.Entry<String, Double> e2) {
//                        return e1.getValue().compareTo(e2.getValue());
//                    }
//                });
//
//
//        java.util.TreeSet<Student> treeSet = new java.util.TreeSet<>(
//                Comparator.comparing(Student::getName)
//        );


        //Implement a B-tree with order 3
        TreeSet<Student> bTree = new TreeSet<>(3, Comparator.comparing(Student::getName));

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

        //Given a k,your code returns the kth element in the B-tree in lexicographical order.
        // If k is out-of-bounds throw an exception.
        bTree.getElement(5);

        // Out of bounds exception
        try{
            bTree.getElement(9);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        System.out.println("Print out the RedIds of the students that are on probation(GPA less than 2.85) that in the\n" +
                "list from the front to the back of the list.");

        printStudentOnProbation(bTree);

        System.out.println("Print out the names of the students with GPA of 4.0 in the list from the back to the front of\n" +
                "the list");

        printStudentWithGpaInReverseOrder(4.0, bTree);

        System.out.println("iterator running  ... ");
        Iterator itr = bTree.iterator();
        while (itr.hasNext()){
            System.out.println(itr.next());
        }
    }

    public static void printStudentOnProbation(TreeSet<Student> bTree){
        bTree.traverse()
                .stream()
                .filter(student -> student.getGpa() < 2.85)
                .map(student -> student.getName())
                .forEach(System.out::println);
    }

    public static void printStudentWithGpaInReverseOrder(double GPA, TreeSet<Student> bTree){
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