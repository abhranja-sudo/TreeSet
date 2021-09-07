package com.aranjan5694.assignment1.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Node class to be used in the B-Tree
 * @author Abhishek Ranjan <aranjan5694@sdsu.edu>
 */
public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {

    private List<T> keys;
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

    public T getKey(int index) {
        return keys.get(index);
    }

    public void addKey(T element){
        keys.add(element);
        Collections.sort(keys);
    }
    public int getKeysSize() {
        return keys.size();
    }

    public List<T> getKeys() {
        return keys;
    }

    public void setKeys(List<T> keys) {
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
        return this.keys.get(0).compareTo(o.getKey(0));
    }

}
