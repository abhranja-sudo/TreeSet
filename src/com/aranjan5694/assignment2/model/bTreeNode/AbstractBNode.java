package com.aranjan5694.assignment2.model.bTreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractBNode<E> implements BNode<E>, Comparable<AbstractBNode<E>>{
    private List<E> keys;
    private List<BNode<E>> children;

    public void setChildren(List<BNode<E>> children) {
        this.children = children;
    }

    private BNode<E> parent;
    private Comparator<? super E> comparator;

    public void setParent(BNode<E> parent) {
        this.parent = parent;
    }

    public AbstractBNode(BNode<E> parent, Comparator<? super E> comparator) {
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
        this.parent = parent;
        this.comparator = comparator;
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
                return true;
            }
        }
        return false;
    }

    /**
     *  Function to shift node left starting with index
     * @param index from where shifting is to be done
     */
    public void shiftLeft(int index, List<BNode<E>> list) {
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
    public boolean addChildNode(BNode<E> child) {
        child.setParent(this);
        children.add(child);
        Collections.sort((List<AbstractBNode<E>>) (List<?>)children);
        return true;
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

    public List<BNode<E>> getChildren() {
        return children;
    }

    public BNode<E> getParent() {
        return parent;
    }

    public void setParent(AbstractBNode<E> parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(AbstractBNode<E> o) {
        return compare(this.keys.get(0), o.getKey(0));
    }
}
