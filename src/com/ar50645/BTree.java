package com.ar50645;

import com.ar50645.assignment1.model.Node;

public class BTree<T extends Comparable<T>> {

    // Order here determines the minimum and maximum no. of keys any node can hold
    private int order;
    private Node<T> root = null;
    private int minNumberOfKeys;
    private int maxNumberOfKeys;
    private int minNumberOfChild;
    private int maxNumberOfChild;

    //Defaults to 2-3 BTree
    public BTree() {
        initializeProperties(1);
    }

    public BTree(int order) {
        this.order = order;
        initializeProperties(order);
    }

    private void initializeProperties(int order){
        minNumberOfKeys = order;
        maxNumberOfKeys = minNumberOfKeys * 2;
        minNumberOfChild = minNumberOfKeys + 1;
        maxNumberOfChild = maxNumberOfKeys + 1;
    }

    public boolean addKey(T keyToAdd){
        if(root == null){
            initializeRoot(keyToAdd);
            return true;
        }
        Node<T> node = root;
        while (node != null) {
            if(node.getChildrenSize() == 0){
                node.addKey(keyToAdd);
                if(node.getKeysSize() <= maxNumberOfKeys) {
                    break;
                }
                // keys size is greater than maximum Keys allowed, split the node
                split(node);
            }
            node = findNextNode(node, keyToAdd);
        }

        return true;
    }

    private Node<T> findNextNode(Node<T> node, T keyToAdd) {
        //Navigate
        //Lessor or equal
        T lesser = node.getKey(0);
        if (keyToAdd.compareTo(lesser) <= 0) {
            return node.getChild(0);
        }

        // Greater
        int numberOfKeys = node.getKeysSize();
        T greater = node.getKey(numberOfKeys - 1);
        if(keyToAdd.compareTo(greater) > 0) {
            return node.getChild(numberOfKeys);
        }

        // Search internal nodes
        for (int i = 1; i < node.getKeysSize(); i++) {
            T prev = node.getKey(i - 1);
            T next = node.getKey(i);
            if (keyToAdd.compareTo(prev) > 0 && keyToAdd.compareTo(next) <= 0) {
                node = node.getChild(i);
                break;
            }
        }
        return node;
    }

    private void initializeRoot(T key){
        root = new Node<>(null);
        root.addKey(key);
    }

    private void split(Node<T> node) {

        int medianIndex = node.getKeysSize() / 2;
        T medianValue = node.getKey(medianIndex);

        Node<T> left = createLeftNode(node);
        Node<T> right = createRightNode(node);

        // new root, height of tree is increased
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
        parent.addChild(left);
        parent.addChild(right);

        if (parent.getKeysSize() > maxNumberOfKeys){
            split(parent);
        }
    }

    private void createNewRoot(Node<T> node, Node<T> left, Node<T> right) {
        int medianIndex = node.getKeysSize() / 2;
        T medianValue = node.getKey(medianIndex);
        Node<T> newRoot = new Node<>(null);
        newRoot.addKey(medianValue);
        node.setParent(newRoot);
        root = newRoot;
        node = root;
        node.addChild(left);
        node.addChild(right);
    }

    private Node<T> createRightNode(Node<T> node) {
        int medianIndex = node.getKeysSize() / 2;
        int numberOfKeys = node.getKeysSize();
        Node<T> right = new Node<>(null);
        for (int i = medianIndex + 1; i < numberOfKeys; i++) {
            right.addKey(node.getKey(i));
        }
        if (node.getChildrenSize() > 0) {
            for (int j = medianIndex + 1; j < node.getChildrenSize(); j++) {
                Node<T> c = node.getChild(j);
                right.addChild(c);
            }
        }
        return right;
    }

    private Node<T> createLeftNode(Node<T> node) {
        int medianIndex = node.getKeysSize() / 2;
        Node<T> left = new Node<>(null);
        for (int i = 0; i < medianIndex; i++) {
            left.addKey(node.getKey(i));
        }
        if (node.getChildrenSize() > 0) {
            for (int j = 0; j <= medianIndex; j++) {
                Node<T> c = node.getChild(j);
                left.addChild(c);
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
