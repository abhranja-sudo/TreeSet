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

//    /**
//     * get next candidate node
//     * @param node Node to compare key with
//     * @param keyToAdd key to be added in tree
//     * @return candidate Node where key can be inserted
//     */
//    @Override
//    public BNode<E> getNodeToInsert(BNode<E> node, E keyToAdd) {
//        if(node.getChildrenSize() != 0) {
//            if(compare(keyToAdd, node.getKey(node.getKeysSize() - 1)) > 0) {
//                return node.getChild(node.getKeysSize());
//            }
//
//            if (compare(keyToAdd, node.getKey(0)) <= 0) {
//                return node.getChild(0);
//            }
//
//            for (int i = 1; i < node.getKeysSize(); i++) {
//
//                if (compare(keyToAdd, node.getKey(i)) <= 0 && compare(keyToAdd, node.getKey(i - 1)) > 0) {
//                    return node.getChild(i);
//                }
//            }
//        }
//        return node;
//    }

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
    @Override
    public int compareTo(AbstractBNode<E> o) {
        return compare(this.keys.get(0), o.getKey(0));
    }
}
