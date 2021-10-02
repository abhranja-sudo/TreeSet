package com.aranjan5694.assignment2.model;

import java.util.List;

public interface BNode<E> {

    void setChildren(List<BNode<E>> children);

    void setParent(BNode<E> parent);

    int getChildrenSize();

    List<E> getKeys();

    void addKey(E element);

    boolean removeChild(BNode<E> child);

    int getKeysSize();

    E getKey(int index);

    List<BNode<E>> getChildren();

    BNode<E> getParent();

    /**
     * Add the child to the node
     * @param child Node to be added as child
     * @return true if child is successfully added
     */
    boolean addChildNode(BNode<E> child);

    /**
     * get the child at any particular index
     * @param index Index of the child need to get
     * @return Node at a particular index
     */
    BNode<E> getChild(int index);

    BNode<E> getNodeToInsert(BNode<E> node, E keyToAdd);

    boolean canRelocate();
}
