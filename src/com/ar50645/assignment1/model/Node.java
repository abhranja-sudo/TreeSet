package com.ar50645.assignment1.model;

import java.util.*;

public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {

    private List<T> keys;
    private List<Node<T>> children;
    private Node<T> parent;

    public Node(Node<T> parent) {
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
        this.parent = parent;
    }

    public boolean removeChild(Node<T> child) {
        boolean found = false;
        if (this.getChildrenSize() == 0)
            return found;
        for (int i = 0; i < this.getChildrenSize(); i++) {
            if (children.get(i).equals(child)) {
                found = true;
            } else if (found) {
                // shift the rest of the keys down
                children.set(i - 1, children.get(i));
            }
        }
        if (found) {
            children.remove(children.size() - 1);
        }
        return found;
    }

    public boolean addChildNode(Node<T> child) {
        child.parent = this;
        children.add(child);
        Collections.sort(children);
        return true;
    }

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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("keys=[");
        for (int i = 0; i < getKeysSize(); i++) {
            T value = getKey(i);
            builder.append(value);
            if (i < getKeysSize() - 1)
                builder.append(", ");
        }
        builder.append("]\n");

        if (parent != null) {
            builder.append("parent=[");
            for (int i = 0; i < parent.getKeysSize(); i++) {
                T value = parent.getKey(i);
                builder.append(value);
                if (i < parent.getKeysSize() - 1)
                    builder.append(", ");
            }
            builder.append("]\n");
        }

        if (children != null) {
            builder.append("keySize=").append(getKeysSize()).append(" children=").append(getChildrenSize()).append("\n");
        }

        return builder.toString();
    }
}
