package com.ar50645;

import com.ar50645.assignment1.model.Node;
import com.sun.xml.internal.fastinfoset.tools.StAX2SAXReader;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @param <T>
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
     * @param order denotes maximum number of child a node can have
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
     *
     * @param element to be added to node
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
                node.addKey(element);
                if(node.getKeysSize() <= maxNumberOfKeys) {
                    break;
                }
                // keys size is greater than maximum Keys allowed, split the node
                split(node);
            }
            node = navigateNextNode(node, element);
        }
        size++;
        return true;
    }

    /**
     *
     * @param k denotes the
     * @return element
     */
    public T getElement(int k){
        if(k >= size){
            throw new IndexOutOfBoundsException("k is out of bound!");
        }
        return (T)traverse().toArray()[k];
    }

    public void traverseInorderTillK(){
        traverseHelper(root);
    }
    private void traverseHelper(Node<T> node){
        if(node.getChildrenSize() == 0){
            System.out.println(node.getKeys().stream().map(Object::toString)
                    .collect(Collectors.joining("/n ")));
//            System.out.println(node.getKeys());
            return;
        }
        for(int i = 0;i <= node.getKeysSize(); i++){
            traverseHelper(node.getChild(i));
            if(i != node.getKeysSize()){
                System.out.println(node.getKey(i));
            }
//            traverseHelper(node.getChild(i + 1));
        }
//        System.out.println(node.getKeys());
    }

    public Set<T> traverse(){
        T lastValue;
        Node<T> lastNode = null;
        int index = 0;
        Deque<Node<T>> toVisit = new ArrayDeque<>();
        if (root!=null && root.getKeysSize() > 0) {
            toVisit.add(root);
        }
        Set<T> set = new TreeSet<>();

        while(((lastNode!=null && index<lastNode.getKeysSize())||(toVisit.size()>0))){
            if (lastNode!=null && (index < lastNode.getKeysSize())) {
                lastValue = lastNode.getKey(index++);
                set.add(lastValue);
                continue;
            }
            while (toVisit.size()>0) {
                // Go thru the current nodes
                Node<T> n = toVisit.pop();

                // Add non-null children
                for (int i=0; i<n.getChildrenSize(); i++) {
                    toVisit.add(n.getChild(i));
                }

                // Update last node (used in remove method)
                index = 0;
                lastNode = n;
                lastValue = lastNode.getKey(index++);
                set.add(lastValue);
                break;
            }
        }
        return set;
    }


    private Node<T> navigateNextNode(Node<T> node, T keyToAdd) {

        //return last child if keyToAdd is greater than the largest key in the node
        if(keyToAdd.compareTo(node.getKey(node.getKeysSize() - 1)) > 0) {
            return node.getChild(node.getKeysSize());
        }

        //return first child if the keyToAdd is smaller or equal than the smallest key in the node.
        if (keyToAdd.compareTo(node.getKey(0)) <= 0) {
            return node.getChild(0);
        }

        // Search internal nodes
        for (int i = 1; i < node.getKeysSize(); i++) {
            if (keyToAdd.compareTo(node.getKey(i)) <= 0 && keyToAdd.compareTo(node.getKey(i - 1)) > 0) {
                return node.getChild(i);
            }
        }
        return node;
    }

    private void initializeRoot(T key){
        root = new Node<>(null);
        root.addKey(key);
    }

    private void split(Node<T> node) {

        Node<T> left = createLeftNode(node);
        Node<T> right = createRightNode(node);

        // new root, height of tree to be increased
        if (node.getParent() == null) {
            createNewRoot(node, left, right);
        }
        // Move the median value up to the parent
        else {
            adjustMedianUpToParent(node, left, right);
        }
    }

    private void adjustMedianUpToParent(Node<T> node, Node<T> left, Node<T> right) {
        int medianIndex = node.getKeysSize() / 2;
        T medianValue = node.getKey(medianIndex);
        Node<T> parent = node.getParent();
        parent.addKey(medianValue);
        parent.removeChild(node);
        parent.addChildNode(left);
        parent.addChildNode(right);

        if (parent.getKeysSize() > maxNumberOfKeys){
            split(parent);
        }
    }

    private void createNewRoot(Node<T> node, Node<T> left, Node<T> right) {
        Node<T> newRoot = new Node<>(null);

        //Add median key in the new root
        newRoot.addKey(node.getKey(node.getKeysSize() / 2));
        node.setParent(newRoot);
        root = newRoot;
        node = root;
        node.addChildNode(left);
        node.addChildNode(right);
    }

    private Node<T> createRightNode(Node<T> node) {
        int midIndex = node.getKeysSize() / 2;
        Node<T> right = new Node<>(null);
        for (int i = midIndex + 1; i < node.getKeysSize(); i++) {
            right.addKey(node.getKey(i));
        }
        if (node.getChildrenSize() > 0) {
            for (int j = midIndex + 1; j < node.getChildrenSize(); j++) {
                Node<T> c = node.getChild(j);
                right.addChildNode(c);
            }
        }
        return right;
    }

    private Node<T> createLeftNode(Node<T> node) {
        int midIndex = node.getKeysSize() / 2;
        Node<T> left = new Node<>(null);
        for (int i = 0; i < midIndex; i++) {
            left.addKey(node.getKey(i));
        }
        if (node.getChildrenSize() > 0) {
            for (int j = 0; j <= midIndex; j++) {
                Node<T> c = node.getChild(j);
                left.addChildNode(c);
            }
        }
        return left;
    }

    @Override
    public String toString() {
        return TreePrinter.getString(this);
    }

    private static class TreePrinter {

        public static <T extends Comparable<T>> String getString(BTree<T> tree) {
            if (tree.root == null) return "Tree has no nodes.";
            return getString(tree.root, "", true);
        }

        private static <T extends Comparable<T>> String getString(Node<T> node, String prefix, boolean isTail) {
            StringBuilder builder = new StringBuilder();

            builder.append(prefix).append((isTail ? "└── " : "├── "));
            for (int i = 0; i < node.getKeysSize(); i++) {
                T value = node.getKey(i);
                builder.append(value);
                if (i < node.getKeysSize() - 1)
                    builder.append(", ");
            }
            builder.append("\n");

            if (node.getChildren() != null) {
                for (int i = 0; i < node.getChildrenSize() - 1; i++) {
                    Node<T> obj = node.getChild(i);
                    builder.append(getString(obj, prefix + (isTail ? "    " : "│   "), false));
                }
                if (node.getChildrenSize() >= 1) {
                    Node<T> obj = node.getChild(node.getChildrenSize() - 1);
                    builder.append(getString(obj, prefix + (isTail ? "    " : "│   "), true));
                }
            }

            return builder.toString();
        }
    }
}
