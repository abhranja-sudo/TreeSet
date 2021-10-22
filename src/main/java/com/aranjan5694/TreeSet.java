package com.aranjan5694;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The aim for this TreeSet to be able to replace java.util.TreeSet. Our TreeSet uses B-Tree for implementation instead of
 * Red-Black Tree that java.util.TreeSet uses.
 * A B-tree is a tree data structure that keeps data sorted and allows searches, insertions, and deletions in
 * logarithmic amortized time. Unlike self-balancing binary search trees, it is optimized for systems that read and
 * write large blocks of data. It is most commonly used in database and file systems.
 *
 * @author Abhishek Ranjan <aranjan5694@sdsu.edu>
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
    private com.aranjan5694.model.Node<E> root;
    private int minNumberOfKeys;
    private int maxNumberOfKeys;
    private int minNumberOfChild;
    private int maxNumberOfChild;
    private int size;
    private static final int DEFAULT_BTREE_ORDER = 2;

    /**
     * Initializes BTree with given getOrderingStrategy
     *
     * @param order denotes maximum number of child node can have
     */
    public TreeSet(int order) {
        this.order = order;
        comparator = null;
        initializeProperties(order);
    }

    public TreeSet(int order, Comparator<? super E> comparator) {
        this.order = order;
        this.comparator = comparator;
        initializeProperties(order);
    }

    /**
     * Default constructor. Initialize Btree with getOrderingStrategy 2
     */
    public TreeSet() {
        this.comparator = null;
        initializeProperties(DEFAULT_BTREE_ORDER);
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
    private void initializeProperties(int order) {
        root = new NullNode<>();
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
        return comparator == null ? ((Comparable<? super E>) e1).compareTo((E) e2)
                : comparator.compare((E) e1, (E) e2);
    }

    /**
     * @param k Index of the element to get
     *          0-Indexed
     * @return kth element in B-Tree
     */
    public E getElement(int k) {
        int counter = 0;
        for (E element : this) {
            if (counter == k) {
                return element;
            }
            counter++;
        }
        throw new IndexOutOfBoundsException("k is out of bound!");
    }

    /**
     * * Adds the specified element to this set if it is not already present.
     * * More formally, adds the specified element {@code e} to this set if
     * * the set contains no element {@code e2} such that
     * * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>.
     * * If this set already contains the element, the call leaves the set
     * * unchanged and returns {@code false}.
     * *
     * * @param e element to be added to this set
     * * @return {@code true} if this set did not already contain the specified
     */
    @Override
    public boolean add(E e) {

        if (isDuplicate(e)) {
            return false;
        }

        com.aranjan5694.model.Node<E> node = root.getNodeToInsert(root, e);
        node.addKey(e);

        if (node.getKeysSize() > maxNumberOfKeys) {
            split(node);
        }

        size++;
        return true;
    }

    /**
     * Traverse the tree to check for duplicates
     */
    private boolean isDuplicate(E e) {
        boolean duplicate = false;
        long countDuplicate = this.stream()
                .filter(e1 -> compare(e, e1) == 0)
                .count();

        if (countDuplicate > 0) {
            duplicate = true;
        }

        return duplicate;
    }

    /**
     * splits the node. called when keys size is greater than maximum keys allowed in node
     *
     * @param node Node to split
     */
    private void split(com.aranjan5694.model.Node<E> node) {

        com.aranjan5694.model.Node<E> left = createLeft(node);
        com.aranjan5694.model.Node<E> right = createRight(node);

        if (node.getParent() == null) {
            createNewRoot(node, left, right);
        } else {
            com.aranjan5694.model.Node<E> parent = node.getParent();
            parent.addKey(node.getKey(node.getKeysSize() / 2));
            parent.removeChild(node);
            parent.addChildNode(left);
            parent.addChildNode(right);

            if (parent.getKeysSize() > maxNumberOfKeys) {
                split(parent);
            }
        }
    }

    /**
     * create new root, height of tree to be increased.
     * Add median key in the new root.
     *
     * @param node  Node to be split
     * @param left  add left node to the child of node
     * @param right right node to be added as child of node
     */
    private void createNewRoot(com.aranjan5694.model.Node<E> node, com.aranjan5694.model.Node<E> left, com.aranjan5694.model.Node<E> right) {
        com.aranjan5694.model.Node<E> newRoot = new Node(null);
        newRoot.addKey(node.getKey(node.getKeysSize() / 2));
        node.setParent(newRoot);
        root = newRoot;
        node = root;
        node.setChildren(new ArrayList<>());
        node.addChildNode(left);
        node.addChildNode(right);
    }

    /**
     * Create right node, add key to the new right node from splitting node from mid to end and
     * make all the right children of splitting node, if any, the children of new right node
     *
     * @param node Node that is splitting up
     * @return right node
     */
    private com.aranjan5694.model.Node<E> createRight(com.aranjan5694.model.Node<E> node) {
        com.aranjan5694.model.Node<E> right = new Node(null);
        for (int i = node.getKeysSize() / 2 + 1; i < node.getKeysSize(); i++) {
            right.addKey(node.getKey(i));
        }
        if (node.getChild(0).haveKeys()) {
            right.setChildren(new ArrayList<>());
            for (int i = node.getKeysSize() / 2 + 1; i < node.getChildrenSize(); i++) {
                right.addChildNode(node.getChild(i));
            }
        }
        return right;
    }

    /**
     * Create left node, add key to the new left node from splitting node upto mid and
     * make all the left children of splitting node, if any, the children of the new left node
     *
     * @param node Node that is splitting up
     * @return left node
     */
    private com.aranjan5694.model.Node<E> createLeft(com.aranjan5694.model.Node<E> node) {
        com.aranjan5694.model.Node<E> left = new Node(null);
        for (int i = 0; i < node.getKeysSize() / 2; i++) {
            left.addKey(node.getKey(i));
        }
        if (node.getChild(0).haveKeys()) {
            left.setChildren(new ArrayList<>());
            for (int i = 0; i <= node.getKeysSize() / 2; i++) {
                left.addChildNode(node.getChild(i));
            }
        }
        return left;
    }


    /**
     * Internal Iterator
     * <p>
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the reverse order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
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

    @Override
    public Object[] toArray() {
        Object[] elementArray = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < elementArray.length; i++) {
            elementArray[i] = it.next();
        }
        return elementArray;
    }

    @Override
    public String toString() {
        //Level-Order Traversal
        Queue<com.aranjan5694.model.Node<E>> queue = new LinkedList<>();
        queue.add(root);

        StringBuilder sb = new StringBuilder();

        while (!queue.isEmpty()) {
            com.aranjan5694.model.Node<E> node = queue.remove();
            sb.append(node);
            queue.addAll(node.getChildren());
        }
        return sb.toString();
    }


    private final class TreeIterator implements Iterator<E> {

        private Stack<com.aranjan5694.model.Node<E>> nodeStack;
        private Stack<Integer> indexStack;

        public TreeIterator() {
            nodeStack = new Stack<>();
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

            com.aranjan5694.model.Node<E> node = nodeStack.peek();
            int index = indexStack.pop();
            E result = node.getKey(index);
            index++;
            if (index < node.getKeysSize())
                indexStack.push(index);
            else
                nodeStack.pop();
            if (node.getChild(0).haveKeys())
                pushLeftChild(node.getChild(index));
            return result;
        }

        private void pushLeftChild(com.aranjan5694.model.Node<E> node) {
            while (true) {
                nodeStack.push(node);
                indexStack.push(0);
                if (!node.getChild(0).haveKeys())
                    break;
                node = node.getChild(0);
            }
        }
    }

    class NullNode<E> implements com.aranjan5694.model.Node<E> {

        @Override
        public com.aranjan5694.model.Node<E> getNodeToInsert(com.aranjan5694.model.Node<E> node, E elementToAdd) {
            if (!root.haveKeys()) {
                root = new TreeSet.Node(null);
                return (com.aranjan5694.model.Node<E>) root;
            }
            return node;
        }

        @Override
        public void setChildren(List<com.aranjan5694.model.Node<E>> children) {

        }

        @Override
        public void setParent(com.aranjan5694.model.Node<E> parent) {

        }

        @Override
        public int getChildrenSize() {
            return 0;
        }

        @Override
        public List<E> getKeys() {
            return null;
        }

        @Override
        public void addKey(E element) {

        }

        @Override
        public boolean removeChild(com.aranjan5694.model.Node<E> child) {
            return false;
        }

        @Override
        public int getKeysSize() {
            return 0;
        }

        @Override
        public E getKey(int index) {
            return null;
        }

        @Override
        public List<com.aranjan5694.model.Node<E>> getChildren() {
            return new ArrayList<>();
        }

        @Override
        public com.aranjan5694.model.Node<E> getParent() {
            return null;
        }

        /**
         * Add the child to the node
         *
         * @param child Node to be added as child
         * @return true if child is successfully added
         */
        @Override
        public boolean addChildNode(com.aranjan5694.model.Node<E> child) {
            return false;
        }

        /**
         * get the child at any particular index
         *
         * @param index Index of the child need to get
         * @return Node at a particular index
         */
        @Override
        public com.aranjan5694.model.Node<E> getChild(int index) {
            return null;
        }

        @Override
        public boolean haveKeys() {
            return false;
        }

        @Override
        public String toString() {
            return " ";
        }
    }

    private class Node<E> implements Comparable<Node>, com.aranjan5694.model.Node<E> {
        private List<E> keys;
        private int keysSize;
        private List<com.aranjan5694.model.Node<E>> children;
        private int childrenSize;
        private com.aranjan5694.model.Node<E> parent;

        public Node(com.aranjan5694.model.Node<E> parent) {
            this.keys = new ArrayList<>();
            keysSize = 0;
            this.children = new ArrayList<>();
            childrenSize = 0;
            this.parent = parent;
            children = new ArrayList<>(order);
            initChildrenWithNullNode(children);
        }

        private void initChildrenWithNullNode(List<com.aranjan5694.model.Node<E>> children) {
            IntStream.range(0, order)
                    .<com.aranjan5694.model.Node<E>>mapToObj(i -> new NullNode<>())
                    .forEach(children::add);
        }

        /**
         * Remove child node
         *
         * @param child Node to removed
         * @return true if successfully removed
         */
        public boolean removeChild(com.aranjan5694.model.Node<E> child) {

            for (int i = 0; i < this.getChildrenSize(); i++) {
                if (children.get(i).equals(child)) {
                    shiftLeft(i + 1, children);
                    children.remove(children.size() - 1);
                    childrenSize--;
                    return true;
                }
            }
            return false;
        }

        /**
         * Function to shift node left starting with index
         *
         * @param index from where shifting is to be done
         */
        void shiftLeft(int index, List<com.aranjan5694.model.Node<E>> list) {
            while (index < list.size()) {
                list.set(index - 1, list.get(index));
                index++;
            }
        }

        /**
         * get next candidate node where
         *
         * @param node         Node to compare key with
         * @param elementToAdd key to be added in tree
         * @return candidate Node where key can be inserted
         */
        public com.aranjan5694.model.Node<E> getNodeToInsert(com.aranjan5694.model.Node<E> node, E elementToAdd) {

            //get NULL node and use getNodeToInsert() on it
            if (!node.getChild(0).haveKeys()) {
                return this.getChild(0).getNodeToInsert(node, elementToAdd);
            }

            if (compare(elementToAdd, node.getKey(node.getKeysSize() - 1)) > 0) {
                node = getChild(node.getKeysSize());
                return node.getNodeToInsert(node, elementToAdd);
            }

            if (compare(elementToAdd, node.getKey(0)) <= 0) {
                node = node.getChild(0);
                return node.getNodeToInsert(node, elementToAdd);
            }

            for (int i = 1; i < node.getKeysSize(); i++) {

                if (compare(elementToAdd, node.getKey(i)) <= 0 && compare(elementToAdd, node.getKey(i - 1)) > 0) {
                    node = node.getChild(i);
                    return node.getNodeToInsert(node, elementToAdd);
                }
            }
            return null;
        }

        @Override
        public boolean haveKeys() {
            return true;
        }

        /**
         * Add the child to the node
         *
         * @param child Node to be added as child
         * @return true if child is successfully added
         */
        public boolean addChildNode(com.aranjan5694.model.Node<E> child) {
            child.setParent(this);
            children.add(childrenSize, child);
            childrenSize++;
            Collections.sort((List<Node>) (List<?>) children);
            return true;
        }

        /**
         * get the child at any particular index
         *
         * @param index Index of the child need to get
         * @return Node at a particular index
         */
        public com.aranjan5694.model.Node<E> getChild(int index) {
            if (index >= children.size()) {
                return null;
            }
            return children.get(index);
        }

        public E getKey(int index) {
            return keys.get(index);
        }

        public void addKey(E element) {
            keys.add(keysSize, element);
            keysSize++;
            keys.sort((Comparator<? super E>) comparator);
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

        public List<com.aranjan5694.model.Node<E>> getChildren() {
            return children;
        }

        @Override
        public void setChildren(List<com.aranjan5694.model.Node<E>> children) {
            this.children = children;
        }

        @Override
        public void setParent(com.aranjan5694.model.Node<E> parent) {
            this.parent = parent;
        }

        @Override
        public com.aranjan5694.model.Node<E> getParent() {
            return parent;
        }

        @Override
        public int compareTo(Node o) {
            return compare(this.keys.get(0), o.keys.get(0));
        }

        @Override
        public String toString() {
            return getKeys().stream().map(Object::toString)
                    .collect(Collectors.joining(", "));
        }
    }
}
