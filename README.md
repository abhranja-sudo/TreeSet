# TreeSet

The aim for this TreeSet to be able to replace java.util.TreeSet for systems that write large blocks of data. It is most commonly used in database and file systems.. 

Our TreeSet uses **B-Tree** for implementation instead of
Red-Black Tree that java.util.TreeSet uses.

A B-tree is a tree data structure that keeps data sorted and allows searches, insertions, and deletions in
logarithmic amortized time. 

 In particular, a **B-tree**:

* keeps keys in sorted order for sequential traversing
* uses a hierarchical index to minimize the number of disk reads
* uses partially full blocks to speed up insertions and deletions
* keeps the index balanced with a recursive algorithm
* In addition, a B-tree minimizes waste by making sure the interior nodes are at least half full. A B-tree can handle an arbitrary number of insertions and deletions.


The following **Design Patterns** used in implementation:

* **Iterator Pattern**  to implement external and internal Iterator
* **Null Object Pattern** to remove null check
* **Strategy Pattern**  to allow clients to control how B-Tree orders the elements

Demonstration for strategy pattern has been included
