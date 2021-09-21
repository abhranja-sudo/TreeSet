package com.aranjan5694.assignment2;

import com.aranjan5694.assignment2.model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  A B-tree is a tree data structure that keeps data sorted and allows searches, insertions, and deletions in
 *  logarithmic amortized time. Unlike self-balancing binary search trees, it is optimized for systems that read and
 *  write large blocks of data. It is most commonly used in database and file systems.
 *
 *  @author Abhishek Ranjan <aranjan5694@sdsu.edu>
 */
public class BTree<T extends Comparable<T>> {

    private int order;
    private Node<T> root = null;
    private int minNumberOfKeys;
    private int maxNumberOfKeys;
    private int minNumberOfChild;
    private int maxNumberOfChild;
    private int size;

    /**
     * Initializes BTree with given order
     * @param order denotes maximum number of child node can have
     */
    public BTree(int order) {
        this.order = order;
        initializeProperties(order);
    }

    /**
     * Default constructor. Initialize Btree with order 2
     */
    public BTree() {
        initializeProperties(2);
    }

    /**
     * Helper function called by constructor to initialize BTree
     * @param order denotes maximum number of child a node can have
     */
    private void initializeProperties(int order){
        maxNumberOfChild = order;
        maxNumberOfKeys = maxNumberOfChild - 1;
        minNumberOfKeys = 1;
        minNumberOfChild = minNumberOfKeys + 1;
    }

    /**
     * @param k Index of the element to get
     *          0-Indexed
     * @return kth element in B-Tree
     */
    public T getElement(int k){
        if(k >= size){
            throw new IndexOutOfBoundsException("k is out of bound!");
        }
        return traverse().get(k);
    }

    /**
     * Traverse the tree In-Order
     * @return List containing lexicographically sorted element
     */
    public List<T> traverse(){
        ArrayList<T> element = new ArrayList<>();
        traverseHelper(root, element);
        return element;
    }

    /**
     * Helper function called by traverse. Recursively traverse the tree In-Order
     * @param node the node where traversal starts
     * @param list accumulates lexicographically sorted element
     */
    private void traverseHelper(Node<T> node, ArrayList<T> list){
        if(node.getChildrenSize() == 0){
            node.getKeys()
                .stream()
                .collect(Collectors.toCollection(() -> list));
            return;
        }

        for(int i = 0;i <= node.getKeysSize(); i++){
            traverseHelper(node.getChild(i), list);
            if(i != node.getKeysSize()){
                list.add(node.getKey(i));
            }
        }
    }

    /**
     * Add element to the Btree
     * @param element Element to be added to the tree
     * @return true if element is successfully added
     */
    public boolean addElement(T element){
        if(root == null){
            initializeRoot(element);
            size++;
            return true;
        }

        Node<T> node = root;
        while (node != null) {
            if(node.getChildrenSize() == 0){
                //check for duplicates
                long count = node.getKeys().stream().filter(s -> s.compareTo(element) == 0).count();
                if(count >= 1){
                    return false;  // duplicate items
                }
                node.addKey(element);
                if(node.getKeysSize() <= maxNumberOfKeys) {
                    break;
                }
                split(node);
            }
            node = navigateNextNode(node, element);
        }
        size++;
        return true;
    }

    /**
     * Helper function called by addElement for next candidate node down the root for element to add
     * @param node Node to compare key with
     * @param keyToAdd key to be added in tree
     * @return candidate Node where key can be inserted
     */
    private Node<T> navigateNextNode(Node<T> node, T keyToAdd) {

        if(keyToAdd.compareTo(node.getKey(node.getKeysSize() - 1)) > 0) {
            return node.getChild(node.getKeysSize());
        }

        if (keyToAdd.compareTo(node.getKey(0)) <= 0) {
            return node.getChild(0);
        }

        for (int i = 1; i < node.getKeysSize(); i++) {
            if (keyToAdd.compareTo(node.getKey(i)) <= 0 && keyToAdd.compareTo(node.getKey(i - 1)) > 0) {
                return node.getChild(i);
            }
        }
        return node;
    }

    /**
     * Create root the first time add key called
     * @param key Key to be added to newly created root
     */
    private void initializeRoot(T key){
        root = new Node<>(null);
        root.addKey(key);
    }

    /**
     * splits the node. called when keys size is greater than maximum keys allowed in node
     * @param node Node to split
     */
    private void split(Node<T> node) {

        Node<T> left = createLeftNode(node);
        Node<T> right = createRightNode(node);

        if (node.getParent() == null) {
            createNewRoot(node, left, right);
        }
        else {
            adjustMedianUpToParent(node, left, right);
        }
    }

    /**
     * Helper function. Called by split() to move the median value up to the parent
     */
    private void adjustMedianUpToParent(Node<T> node, Node<T> left, Node<T> right) {
        Node<T> parent = node.getParent();
        parent.addKey(node.getKey(node.getKeysSize() / 2));
        parent.removeChild(node);
        parent.addChildNode(left);
        parent.addChildNode(right);

        if (parent.getKeysSize() > maxNumberOfKeys){
            split(parent);
        }
    }

    /**
     * create new root, height of tree to be increased.
     * Add median key in the new root.
     * @param node Node to be split
     * @param left add left node to the child of node
     * @param right right node to be added as child of node
     */
    private void createNewRoot(Node<T> node, Node<T> left, Node<T> right) {
        Node<T> newRoot = new Node<>(null);
        newRoot.addKey(node.getKey(node.getKeysSize() / 2));
        node.setParent(newRoot);
        root = newRoot;
        node = root;
        node.addChildNode(left);
        node.addChildNode(right);
    }

    /**
     * Create right node, add key to the new right node from splitting node from mid to end and
     * make all the right children of splitting node, if any, the children of new right node
     * @param node Node that is splitting up
     * @return right node
     */
    private Node<T> createRightNode(Node<T> node) {
        Node<T> right = new Node<>(null);
        for (int i = node.getKeysSize() / 2 + 1; i < node.getKeysSize(); i++) {
            right.addKey(node.getKey(i));
        }
        if (node.getChildrenSize() > 0) {
            for (int i = node.getKeysSize() / 2 + 1; i < node.getChildrenSize(); i++) {
                right.addChildNode(node.getChild(i));
            }
        }
        return right;
    }

    /**
     * Create left node, add key to the new left node from splitting node upto mid and
     * make all the left children of splitting node, if any, the children of the new left node
     * @param node  Node that is splitting up
     * @return left node
     */
    private Node<T> createLeftNode(Node<T> node) {
        Node<T> left = new Node<>(null);
        for (int i = 0; i < node.getKeysSize() / 2; i++) {
            left.addKey(node.getKey(i));
        }
        if (node.getChildrenSize() > 0) {
            for (int i = 0; i <= node.getKeysSize() / 2; i++) {
                left.addChildNode(node.getChild(i));
            }
        }
        return left;
    }
}
