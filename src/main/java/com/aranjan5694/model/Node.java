package com.aranjan5694.model;

import java.util.List;

public interface Node<E> {

    void setChildren(List<Node<E>> children);

    void setParent(Node<E> parent);

    int getChildrenSize();

    List<E> getKeys();

    void addKey(E element);

    boolean removeChild(Node<E> child);

    int getKeysSize();

    E getKey(int index);

    List<Node<E>> getChildren();

    Node<E> getParent();

    /**
     * Add the child to the node
     * @param child Node to be added as child
     * @return true if child is successfully added
     */
    boolean addChildNode(Node<E> child);

    /**
     * get the child at any particular index
     * @param index Index of the child need to get
     * @return Node at a particular index
     */
    Node<E> getChild(int index);

    Node<E> getNodeToInsert(Node<E> node, E keyToAdd);

    boolean haveKeys();
}
