package com.aranjan5694.assignment2;

import com.aranjan5694.assignment2.model.bTreeNode.AbstractBNode;
import com.aranjan5694.assignment2.model.bTreeNode.BNode;

import java.util.*;
import java.util.function.Consumer;

/**
 *  The aim for this TreeSet to be able to replace java.util.TreeSet. Our TreeSet uses B-Tree for implementation instead of
 *  Red-Black Tree that java.util.TreeSet uses.
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
    private BTreeNode<E> root = null;
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
        int counter = 0;
        for(E element: this){
            if(counter == k) {
                return element;
            }
            counter++;
        }
        throw new IndexOutOfBoundsException("k is out of bound!");
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

        BNode<E> node = root;
        node = node.getNodeToInsert(node, e);

        boolean duplicate = false;
        long countDuplicate = node.getKeys()
                .stream()
                .filter(s -> s.compareTo(e) == 0)
                .count();
        if(countDuplicate >= 1){
            duplicate = true;
        }
        if(duplicate){
            return false;
        }

        node.addKey(e);
        if(node.getKeysSize() > maxNumberOfKeys) {
            split(node);
        }

        size++;
        return true;
    }

    /**
     * Create root the first time add key called
     * @param key Key to be added to newly created root
     */
    private void initializeRoot(E key){
        root = new BTreeNode<>(null);
        root.addKey(key);
    }

    /**
     * splits the node. called when keys size is greater than maximum keys allowed in node
     * @param node Node to split
     */
    private void split(BNode<E> node) {

        BTreeNode<E> left = createLeftNode(node);
        BTreeNode<E> right = createRightNode(node);

        if (node.getParent() == null) {
            createNewRoot(node, left, right);
        }
        else {
            BNode<E> parent = node.getParent();
            parent.addKey(node.getKey(node.getKeysSize() / 2));
            parent.removeChild(node);
            parent.addChildNode(left);
            parent.addChildNode(right);

            if (parent.getKeysSize() > maxNumberOfKeys){
                split(parent);
            }
        }
    }

    /**
     * create new root, height of tree to be increased.
     * Add median key in the new root.
     * @param node Node to be split
     * @param left add left node to the child of node
     * @param right right node to be added as child of node
     */
    private void createNewRoot(BNode<E> node, BNode<E> left, BNode<E> right) {
        BTreeNode<E> newRoot = new BTreeNode<>(null);
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
    private BTreeNode<E> createRightNode(BNode<E> node) {
        BTreeNode<E> right = new BTreeNode<>(null);
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
    private BTreeNode<E> createLeftNode(BNode<E> node) {
        BTreeNode<E> left = new BTreeNode<>(null);
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
     *
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

        private Stack<BNode<E>> nodeStack;
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

            BNode<E> node = nodeStack.peek();
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

        private void pushLeftChild(BNode<E> node) {
            while (true) {
                nodeStack.push(node);
                indexStack.push(0);
                if (node.getChildrenSize() == 0)
                    break;
                node = node.getChild(0);
            }
        }
    }

    private class BTreeNode<E>
            extends AbstractBNode<E> {

        public BTreeNode(BNode<E> parent) {
            super(parent);
        }

        /**
         * get next candidate node
         * @param node Node to compare key with
         * @param keyToAdd key to be added in tree
         * @return candidate Node where key can be inserted
         */
        @Override
        public BNode<E> getNodeToInsert(BNode<E> node, E keyToAdd) {
            if(node.getChildrenSize() == 0) {
                return node;
            }

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
        if(node.getChildrenSize() == 0) {
            TreeSet.BTreeNodeLeaves bTreeNodeLeaves = (TreeSet.BTreeNodeLeaves)node;
            bTreeNodeLeaves.getNodeToInsert(node, keyToAdd);
        }

            return node.getNodeToInsert(node, keyToAdd);
        }
        /**
         * Compares this object with the specified object for order.  Returns a
         * negative integer, zero, or a positive integer as this object is less
         * than, equal to, or greater than the specified object.
         *
         * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
         * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
         * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
         * <tt>y.compareTo(x)</tt> throws an exception.)
         *
         * <p>The implementor must also ensure that the relation is transitive:
         * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
         * <tt>x.compareTo(z)&gt;0</tt>.
         *
         * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
         * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
         * all <tt>z</tt>.
         *
         * <p>It is strongly recommended, but <i>not</i> strictly required that
         * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
         * class that implements the <tt>Comparable</tt> interface and violates
         * this condition should clearly indicate this fact.  The recommended
         * language is "Note: this class has a natural ordering that is
         * inconsistent with equals."
         *
         * <p>In the foregoing description, the notation
         * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
         * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
         * <tt>0</tt>, or <tt>1</tt> according to whether the value of
         * <i>expression</i> is negative, zero or positive.
         *
         * @param o the object to be compared.
         * @return a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         * @throws NullPointerException if the specified object is null
         * @throws ClassCastException   if the specified object's type prevents it
         *                              from being compared to this object.
         */
//        @Override
//        public int compareTo(BTreeNode<E> o) {
//            return compare(this.keys.get(0), o.getKey(0));
//        }
    }

    private class BTreeNodeLeaves<E>
            extends AbstractBNode<E> {

        public BTreeNodeLeaves(BNode<E> parent) {
            super(parent);
        }

        @Override
        public BNode<E> getNodeToInsert(BNode<E> node, E keyToAdd) {
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
    }
}
