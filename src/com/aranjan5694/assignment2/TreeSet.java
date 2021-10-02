package com.aranjan5694.assignment2;

import com.aranjan5694.assignment2.model.BNode;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    private BNode<E> root = new NullNode<>();
    private int minNumberOfKeys;
    private int maxNumberOfKeys;
    private int minNumberOfChild;
    private int maxNumberOfChild;
    private int size;
    private static final int DEFAULT_BTREE_ORDER = 2;

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

        if(isDuplicate(e)){
            return false;
        }

        BNode<E> node =  root.getNodeToInsert(root, e);
        node.addKey(e);

        if(node.getKeysSize() > maxNumberOfKeys) {
            split(node);
        }

        size++;
        return true;
    }

    private boolean isDuplicate(E e){
        boolean duplicate = false;
        long countDuplicate = this.stream()
            .filter(e1 -> compare(e, e1) == 0)
            .count();

        if(countDuplicate >= 1){
            duplicate = true;
        }

        return duplicate;
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
    private void split(BNode<E> node) {

        BNode<E> left = createLeft(node);
        BNode<E> right = createRight(node);

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
        BNode<E> newRoot = new Node<>(null);
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
     * @param node Node that is splitting up
     * @return right node
     */
    private BNode<E> createRight(BNode<E> node) {
        BNode<E> right = new Node<>(null);
        for (int i = node.getKeysSize() / 2 + 1; i < node.getKeysSize(); i++) {
            right.addKey(node.getKey(i));
        }
        if (node.getChildrenSize() > 0 && node.getChild(0).canRelocate())  {
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
     * @param node  Node that is splitting up
     * @return left node
     */
    private BNode<E> createLeft(BNode<E> node) {
        BNode<E> left = new Node<>(null);
        for (int i = 0; i < node.getKeysSize() / 2; i++) {
            left.addKey(node.getKey(i));
        }
        if (node.getChildrenSize() > 0 && node.getChild(0).canRelocate()) {
            left.setChildren(new ArrayList<>());
            for (int i = 0; i <= node.getKeysSize() / 2; i++) {
                left.addChildNode(node.getChild(i));
            }
        }
        return left;
    }


    /**
     * Internal Iterator
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

    @Override
    public Object[] toArray() {
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < r.length; i++) {
            if (! it.hasNext()) // fewer elements than expected
                return Arrays.copyOf(r, i);
            r[i] = it.next();
        };
        return r;
    }

    @Override
    public String toString() {
        Queue<BNode<E>> queue = new LinkedList<>();
        queue.add(root);

        StringBuilder sb = new StringBuilder("");

        while (!queue.isEmpty()) {
            BNode<E> node = queue.remove();
            sb.append(node);
            queue.addAll(node.getChildren());
        }
        return sb.toString();
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
            if (node.getChild(0).canRelocate())
                pushLeftChild(node.getChild(index));
            return result;
        }

        private void pushLeftChild(BNode<E> node) {
            while (true) {
                nodeStack.push(node);
                indexStack.push(0);
                if (!node.getChild(0).canRelocate())
                    break;
                node = node.getChild(0);
            }
        }
    }

    //Singleton implementation for NullNode
     class NullNode<E> implements BNode<E> {

        private NullNode() {
        }

        @Override
        public BNode<E> getNodeToInsert(BNode<E> node, E keyToAdd) {
            if(!root.canRelocate()){
                root = new Node<>(null);
                return (BNode<E>) root;
            }
            return node;
        }

        @Override
        public void setChildren(List<BNode<E>> children) {

        }

        @Override
        public void setParent(BNode<E> parent) {

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
        public boolean removeChild(BNode<E> child) {
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
        public List<BNode<E>> getChildren() {
            return new ArrayList<>();
        }

        @Override
        public BNode<E> getParent() {
            return null;
        }

        /**
         * Add the child to the node
         *
         * @param child Node to be added as child
         * @return true if child is successfully added
         */
        @Override
        public boolean addChildNode(BNode<E> child) {
            return false;
        }

        /**
         * get the child at any particular index
         *
         * @param index Index of the child need to get
         * @return Node at a particular index
         */
        @Override
        public BNode<E> getChild(int index) {
            return null;
        }

        @Override
        public boolean canRelocate() {
            return false;
        }

        @Override
        public String toString() {
            return " ";
        }
    }

    private class Node<E> implements Comparable<Node<E>>, BNode<E> {
        private List<E> keys;
        private int keysSize = 0;
        private List<BNode<E>> children;
        private int childrenSize = 0;
        private BNode<E> parent;
        public Node(BNode<E> parent) {
            this.keys = new ArrayList<>();
            this.children = new ArrayList<>();
            this.parent = parent;
            children = new ArrayList<>(order);
            initChildrenWithNullNode(children);
        }

        private void initChildrenWithNullNode(List<BNode<E>> children) {
            for(int i = 0; i < order; i++) {
                children.add(new NullNode<>());
            }
        }

        /**
         * Remove child node
         * @param child Node to removed
         * @return true if successfully removed
         */
        public boolean removeChild(BNode<E> child) {
            if (this.getChildrenSize() == 0){
                return false;
            }
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
         *  Function to shift node left starting with index
         * @param index from where shifting is to be done
         */
        void shiftLeft(int index, List<BNode<E>> list) {
            while( index < list.size()){
                list.set( index - 1, list.get(index));
                index++;
            }
        }

        /**
         * get next candidate node
         * @param node Node to compare key with
         * @param keyToAdd key to be added in tree
         * @return candidate Node where key can be inserted
         */
         public BNode<E> getNodeToInsert(BNode<E> node, E keyToAdd) {

             //get NULL node and use navigateNextNode() on it
             if(node.getChildrenSize() == 0 || !node.getChild(0).canRelocate()) {
                 return this.getNextNode().getNodeToInsert(node, keyToAdd);
             }

            if(compare(keyToAdd, node.getKey(node.getKeysSize() - 1)) > 0) {
                node = getChild(node.getKeysSize());
                return node.getNodeToInsert(node, keyToAdd);
            }

            if (compare(keyToAdd, node.getKey(0)) <= 0){
                node = node.getChild(0);
                return node.getNodeToInsert(node, keyToAdd);
            }

            for (int i = 1; i < node.getKeysSize(); i++) {

                if (compare(keyToAdd, node.getKey(i)) <= 0 && compare(keyToAdd, node.getKey(i - 1)) > 0) {
                    node = node.getChild(i);
                    return node.getNodeToInsert(node, keyToAdd);
                }
            }
            return null;
        }

        @Override
        public boolean canRelocate() {
            return true;
        }

        /**
         * Add the child to the node
         * @param child Node to be added as child
         * @return true if child is successfully added
         */
        public boolean addChildNode(BNode<E> child) {
            child.setParent(this);
            children.add(childrenSize, child);
            childrenSize++;
            Collections.sort((List<Node<E>>) (List<?>)children);
            return true;
        }

        /**
         * get the child at any particular index
         * @param index Index of the child need to get
         * @return Node at a particular index
         */
        public BNode<E> getChild(int index) {
            if (index >= children.size()){
                return null;
            }
            return children.get(index);
        }

        public E getKey(int index) {
            return keys.get(index);
        }

        public void addKey(E element){
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

        public List<BNode<E>> getChildren() {
            return children;
        }

        @Override
        public void setChildren(List<BNode<E>> children) {
            this.children = children;
        }

        @Override
        public void setParent(BNode<E> parent) {
            this.parent = parent;
        }

        public BNode<E> getNextNode() {
            if(this.getChildrenSize() == 0){
                return new NullNode<>();
            }
            return this.getChild(0);
        }

        @Override
        public BNode<E> getParent() {
            return parent;
        }

        @Override
        public int compareTo(Node<E> o) {
            return compare(this.keys.get(0), o.keys.get(0));
        }

        @Override
        public String toString() {
            return getKeys().stream().map(Object::toString)
                    .collect(Collectors.joining(", "));
        }
    }
}
