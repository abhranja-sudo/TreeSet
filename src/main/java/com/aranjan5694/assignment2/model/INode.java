package com.aranjan5694.assignment2.model;

import java.util.List;

public interface INode<E> {

    void setChildren(List<INode<E>> children);

    void setParent(INode<E> parent);

    int getChildrenSize();

    List<E> getKeys();

    void addKey(E element);

    boolean removeChild(INode<E> child);

    int getKeysSize();

    E getKey(int index);

    List<INode<E>> getChildren();

    INode<E> getParent();

    /**
     * Add the child to the node
     * @param child Node to be added as child
     * @return true if child is successfully added
     */
    boolean addChildNode(INode<E> child);

    /**
     * get the child at any particular index
     * @param index Index of the child need to get
     * @return Node at a particular index
     */
    INode<E> getChild(int index);

    INode<E> getNodeToInsert(INode<E> node, E keyToAdd);

    boolean haveKeys();
}
