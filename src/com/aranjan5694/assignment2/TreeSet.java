package com.aranjan5694.assignment2;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *  A B-tree is a tree data structure that keeps data sorted and allows searches, insertions, and deletions in
 *  logarithmic amortized time. Unlike self-balancing binary search trees, it is optimized for systems that read and
 *  write large blocks of data. It is most commonly used in database and file systems.
 *
 *  @author Abhishek Ranjan <aranjan5694@sdsu.edu>
 */
public class TreeSet<E extends Comparable<E>> extends AbstractSet<E> {

    /**
     * The comparator used to maintain getOrderingStrategy in this tree map, or
     * null if it uses the natural ordering of its keys.
     *
     * @serial
     */
    private Comparator<? super E> comparator;
    private int order;
    private Node<E> root = null;
    private int minNumberOfKeys;
    private int maxNumberOfKeys;
    private int minNumberOfChild;
    private int maxNumberOfChild;
    private int size;

    /**
     * Initializes BTree with given getOrderingStrategy
     * @param order denotes maximum number of child node can have
     */
    public TreeSet(int order) {
        this.order = order;
        comparator = null;
        initializeProperties(order);
    }

    public TreeSet(int order, Comparator<? super E> comparator ) {
        this.order = order;
        this.comparator = comparator;
        initializeProperties(order);
    }

    /**
     * Default constructor. Initialize Btree with getOrderingStrategy 2
     */
    public TreeSet() {
        this.comparator = null;
        int order = 2;
        initializeProperties(order);
    }

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public Iterator<E> iterator() {
        return new TreeIterator();
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * @param order denotes maximum number of child a node can have
     */
    private void initializeProperties(int order){
        maxNumberOfChild = order;
        maxNumberOfKeys = maxNumberOfChild - 1;
        minNumberOfKeys = 1;
        minNumberOfChild = minNumberOfKeys + 1;
    }

    /**
     * Compares two keys using the correct comparison method for this TreeMap.
     */
    @SuppressWarnings("unchecked")
    final int compare(Object e1, Object e2) {
        return comparator==null ? ((Comparable<? super E>)e1).compareTo((E)e2)
                : comparator.compare((E)e1, (E)e2);
    }

    /**
     * @param k Index of the element to get
     *          0-Indexed
     * @return kth element in B-Tree
     */
    public E getElement(int k){
        if(k >= size){
            throw new IndexOutOfBoundsException("k is out of bound!");
        }
        return traverse().get(k);
    }

    /**
     * Traverse the tree In-Order
     * @return List containing lexicographically sorted element
     */
    public List<E> traverse(){
        ArrayList<E> element = new ArrayList<>();
        traverseHelper(root, element);
        return element;
    }

    /**
     * Helper function called by traverse. Recursively traverse the tree In-Order
     * @param node the node where traversal starts
     * @param list accumulates lexicographically sorted element
     */
    private void traverseHelper(Node<E> node, ArrayList<E> list){
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
     * * Adds the specified element to this set if it is not already present.
     *      * More formally, adds the specified element {@code e} to this set if
     *      * the set contains no element {@code e2} such that
     *      * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>.
     *      * If this set already contains the element, the call leaves the set
     *      * unchanged and returns {@code false}.
     *      *
     *      * @param e element to be added to this set
     *      * @return {@code true} if this set did not already contain the specified
     */
    public boolean add(E e){
        if(root == null){
            initializeRoot(e);
            size++;
            return true;
        }

        Node<E> node = root;
        while (node != null) {
            if(node.getChildrenSize() == 0){
                //check for duplicates
                long count = node.getKeys().stream().filter(s -> s.compareTo(e) == 0).count();
                if(count >= 1){
                    return false;  // duplicate items
                }
                node.addKey(e);
                if(node.getKeysSize() <= maxNumberOfKeys) {
                    break;
                }
                split(node);
            }
            node = navigateNextNode(node, e);
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
    private Node<E> navigateNextNode(Node<E> node, E keyToAdd) {

        if(compare(keyToAdd, node.getKey(node.getKeysSize() - 1)) > 0) {
            return node.getChild(node.getKeysSize());
        }

        if (compare(keyToAdd, node.getKey(0)) <= 0) {
            return node.getChild(0);
        }

        for (int i = 1; i < node.getKeysSize(); i++) {

            if (compare(keyToAdd, node.getKey(i)) <= 0 && compare(keyToAdd, node.getKey(i - 1)) > 0) {
                return node.getChild(i);
            }
        }
        return node;
    }

    /**
     * Create root the first time add key called
     * @param key Key to be added to newly created root
     */
    private void initializeRoot(E key){
        root = new Node<>(null);
        root.addKey(key);
    }

    /**
     * splits the node. called when keys size is greater than maximum keys allowed in node
     * @param node Node to split
     */
    private void split(Node<E> node) {

        Node<E> left = createLeftNode(node);
        Node<E> right = createRightNode(node);

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
    private void adjustMedianUpToParent(Node<E> node, Node<E> left, Node<E> right) {
        Node<E> parent = node.getParent();
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
    private void createNewRoot(Node<E> node, Node<E> left, Node<E> right) {
        Node<E> newRoot = new Node<>(null);
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
    private Node<E> createRightNode(Node<E> node) {
        Node<E> right = new Node<>(null);
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
    private Node<E> createLeftNode(Node<E> node) {
        Node<E> left = new Node<>(null);
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


    /**
     * Internal Iterator.
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @since 1.8
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        Stack<E> stack = new Stack<>();
        for (E e : this) {
            stack.push(e);
        }
        while (!stack.isEmpty()) {
            action.accept(stack.pop());
        }
    }

    private final class TreeIterator implements Iterator<E> {

        private Stack<Node<E>> nodeStack;
        private Stack<Integer> indexStack;

        public TreeIterator() {
            nodeStack  = new Stack<>();
            indexStack = new Stack<>();
            if (root.getKeysSize() > 0)
                pushLeftChild(root);
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return !nodeStack.isEmpty();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Node<E> node = nodeStack.peek();
            int index = indexStack.pop();
            E result = node.getKey(index);
            index++;
            if (index < node.getKeysSize())
                indexStack.push(index);
            else
                nodeStack.pop();
            if (!(node.getChildrenSize() == 0))
                pushLeftChild(node.getChild(index));
            return result;
        }

        private void pushLeftChild(Node<E> node) {
            while (true) {
                nodeStack.push(node);
                indexStack.push(0);
                if (node.getChildrenSize() == 0)
                    break;
                node = node.getChild(0);
            }
        }
    }

    private class Node<T extends Comparable<T>> implements Comparable<Node<T>>{
        private List<E> keys;
        private List<Node<T>> children;
        private Node<T> parent;

        public Node(Node<T> parent) {
            this.keys = new ArrayList<>();
            this.children = new ArrayList<>();
            this.parent = parent;
        }

        /**
         * Remove child node
         * @param child Node to removed
         * @return true if successfully removed
         */
        public boolean removeChild(Node<T> child) {
            if (this.getChildrenSize() == 0){
                return false;
            }
            for (int i = 0; i < this.getChildrenSize(); i++) {
                if (children.get(i).equals(child)) {
                    shiftLeft(i + 1, children);
                    children.remove(children.size() - 1);
                    return true;
                }
            }
            return false;
        }

        /**
         *  Function to shift node left starting with index
         * @param index from where shifting is to be done
         */
        public void shiftLeft(int index, List<Node<T>> list) {
            while( index < list.size()){
                list.set( index - 1, list.get(index));
                index++;
            }
        }

        /**
         * Add the child to the node
         * @param child Node to be added as child
         * @return true if child is successfully added
         */
        public boolean addChildNode(Node<T> child) {
            child.parent = this;
            children.add(child);
            Collections.sort(children);
            return true;
        }

        /**
         * get the child at any particular index
         * @param index Index of the child need to get
         * @return Node at a particular index
         */
        public Node<T> getChild(int index) {
            if (index >= children.size()){
                return null;
            }
            return children.get(index);
        }

        public E getKey(int index) {
            return keys.get(index);
        }

        public void addKey(E element){
            keys.add(element);
            keys.sort(comparator);
        }
        public int getKeysSize() {
            return keys.size();
        }

        public List<E> getKeys() {
            return keys;
        }

        public void setKeys(List<E> keys) {
            this.keys = keys;
        }

        public int getChildrenSize() {
            return children.size();
        }

        public List<Node<T>> getChildren() {
            return children;
        }

        public void setChildren(List<Node<T>> children) {
            this.children = children;
        }

        public Node<T> getParent() {
            return parent;
        }

        public void setParent(Node<T> parent) {
            this.parent = parent;
        }

        @Override
        public int compareTo(Node<T> o) {
            return compare(this.keys.get(0), o.getKey(0));
        }
    }
}
