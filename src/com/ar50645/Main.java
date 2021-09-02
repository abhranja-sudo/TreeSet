package com.ar50645;

import com.ar50645.assignment1.model.Student;

public class Main {

    public static void main(String[] args) {
	// write your code here
        BTree<Student> bTree = new BTree<>();
        bTree.addKey(new Student(112,"Abhishek",2.9));
        bTree.addKey(new Student(113,"Anuj",2.9));
        bTree.addKey(new Student(114,"Kshitij",2.8));
        bTree.addKey(new Student(114,"Shreeraj",2.8));

        bTree.addKey(new Student(820, "Abhishek", 3.8));
        bTree.addKey(new Student(821,"Anuj",3.81));
        bTree.addKey(new Student(822,"Dheeraj", 3.9));
        bTree.addKey(new Student(823,"Shreeraj", 3.99));
        bTree.addKey(new Student(824,"Ksitij",3.998));
        bTree.addKey(new Student(825, "Dhanrj",3.2));
        bTree.addKey(new Student(825,"Aasit", 3.1));
        bTree.addKey(new Student(826,"Aaasjbybf",3.43));
        bTree.addKey(new Student(827,"Aaasjbybf",3.43));
        bTree.addKey(new Student(828,"Aaasjbybf",3.43));
        bTree.addKey(new Student(829,"Aaasjbybf",3.43));
        bTree.addKey(new Student(829,"Aaasjbybf",3.43));

        System.out.println(bTree);
    }
}
