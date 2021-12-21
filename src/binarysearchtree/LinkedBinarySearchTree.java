package binarysearchtree;

import Iterators.InorderBinaryTreeIterator;
import binaryTree.LinkedBinaryTree;
import material.Position;

import java.util.*;


public class LinkedBinarySearchTree<E> implements BinarySearchTree<E> {

    protected LinkedBinaryTree<E> binTree;
    protected Comparator<E> comparator;
    protected int size = 0;

    public LinkedBinarySearchTree(){
        this(null);
    }

    public LinkedBinarySearchTree(Comparator<E> c){
        if(c == null){
            this.comparator = new DefaultComparator<>();
        }
        else{
            this.comparator = c;
        }
        this.binTree = new LinkedBinaryTree<>();
    }

    /**
     * Auxiliary method used by find, insert, and remove.
     *
     * @param value the value searched
     * @param pos the position to start the search
     * @return the position where value is stored
     */
    protected Position<E> treeSearch(E value, Position<E> pos) throws IllegalStateException, IndexOutOfBoundsException {
        E posValue = pos.getElement();
        int comp = comparator.compare(value, posValue);
        if ((comp < 0) && this.binTree.hasLeft(pos)) {
            return treeSearch(value, this.binTree.left(pos)); // search left
        } else if ((comp > 0) && this.binTree.hasRight(pos)) {
            return treeSearch(value, this.binTree.right(pos)); // search right
        }
        else {
            return pos;
        }
    }

    /**
     * Adds to L all entries in the subtree rooted at v having keys equal to k.
     */
    protected void addAll(List<Position<E>> l, Position<E> pos, E value) {
        if (this.binTree.isLeaf(pos)) {
            return;
        }
        Position<E> p = treeSearch(value, pos);
        if (!this.binTree.isLeaf(p)) { // we found an entry with key equal to k
            addAll(l, this.binTree.left(p), value);
            l.add(p); // add entries in inorder
            addAll(l, this.binTree.right(p), value);
        } // this recursive algorithm is simple, but it's not the fastest
    }

    @Override
    public Position<E> find(E value) {
        if(isEmpty()){
            return null;
        }
        Position<E> pos = treeSearch(value, this.binTree.root());
        if(pos.getElement() == value){
            return pos;
        }
        return null;
    }

    @Override
    public Iterable<? extends Position<E>> findAll(E value) {
        List<Position<E>> l = new LinkedList<>();
//        addAll(l, this.binTree.root(), value);
        Position<E> node = treeSearch(value, this.binTree.root());
        if(comparator.compare(node.getElement(), value) == 0) {
            l.add(node);
        }
        while(comparator.compare(successor(node).getElement(), node.getElement()) == 0){
            l.add(node);
            node = successor(node);
        }

        return l;
    }

    @Override
    public Position<E> insert(E value) {

        if(this.binTree.isEmpty()){
            size++;
            return this.binTree.addRoot(value);
        }
        else{
            Position<E> node = treeSearch(value, this.binTree.root());
            int c = this.comparator.compare(value, node.getElement());
            Position<E> returnPos;
            if(c == 0){
                this.binTree.addRight(node, value);
                size++;
                return this.binTree.right(node);
            }
            if(c < 0){
                returnPos = this.binTree.insertLeft(node, value);
            }
            else{ //c > 0
                returnPos = this.binTree.insertRight(node, value);
            }
            size++;
            return returnPos;
        }
    }



    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E remove(Position<E> pos) {
        E toReturn = pos.getElement();
        if(this.binTree.isLeaf(pos) || !this.binTree.hasRight(pos) || !this.binTree.hasLeft(pos)){
            this.binTree.remove(pos);
        }
        else{
            Position<E> succ = successor(pos);
            this.binTree.swap(succ, pos);
//            remove(pos);
            this.binTree.remove(pos);
        }
        size--;
        return toReturn;
    }

    public int removeReturn(Position<E> pos) throws IllegalStateException {
        E remReturn;
        if(this.binTree.isLeaf(pos) || !this.binTree.hasRight(pos) || !this.binTree.hasLeft(pos)){
            remReturn = this.binTree.remove(pos);
        }
        else{
            Position<E> succ = successor(pos);
            this.binTree.swap(succ, pos);
//            remove(pos);
            remReturn = this.binTree.remove(pos);
        }
        size--;
        return (int)remReturn;
    }

    @Override
    public int size() {
        return this.size;
    }


    public Iterable<? extends Position<E>> rangeIterator(E m, E M) {
        List<Position<E>> l = new LinkedList<>();
        Position<E> first = treeSearch(m, this.binTree.root());
        l.add(first);
        while(first.getElement() != M){
            first = successor(first);
            l.add(first);
        }
        return l;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new InorderBinaryTreeIterator<>(binTree);
    }

    public Position<E> successor(Position<E> pos) {
        if(this.binTree.hasRight(pos)){
            return this.binTree.right(pos);
        }
        else{
            Position<E> parent = this.binTree.parent(pos);
            while(comparator.compare(parent.getElement(), pos.getElement()) <= 0){
                if(this.binTree.isRoot(parent)){
                    return null;
                }
                parent = this.binTree.parent(parent);
            }
            return parent;
        }
    }

    public Position<E> predecessor(Position<E> pos) {
        if(this.binTree.hasLeft(pos)){
            return this.binTree.left(pos);
        }
        else{
            Position<E> parent = this.binTree.parent(pos);
            while(comparator.compare(parent.getElement(), pos.getElement()) > 0){
                if(this.binTree.isRoot(parent)){
                    return null;
                }
                parent = this.binTree.parent(parent);
            }
            return parent;
        }
    }

    public Iterable<Position<E>> successors(Position<E> pos) {
        List<Position<E>> l = new LinkedList<>();
        Iterator<Position<E>> it = this.iterator();
        while (it.hasNext()) {
            Position<E> next = it.next();
            if (this.comparator.compare(next.getElement(), pos.getElement()) >= 0) {
                l.add(next);
            }

        }
        return l;
    }

    public Iterable<Position<E>> predecessors(Position<E> pos) {
        List<Position<E>> l = new LinkedList<>();
        Iterator<Position<E>> it = this.iterator();
        while (it.hasNext()) {
            Position<E> next = it.next();
            if (this.comparator.compare(next.getElement(), pos.getElement()) <= 0) {
                l.add(next);
            }

        }
        Collections.reverse(l);
        return l;
    }

    public Position<E> first(){
        if(this.binTree.isEmpty()){
            throw new RuntimeException("No first element.");
        }
        Position<E> node = this.binTree.root();
        while(this.binTree.hasLeft(node)){
            node = this.binTree.left(node);
        }
        return node;
    }

    public Position<E> last(){
        if(this.binTree.isEmpty()){
            throw new RuntimeException("No last element.");
        }
        Position<E> node = this.binTree.root();
        while(this.binTree.hasRight(node)){
            node = this.binTree.right(node);
        }
        return node;
    }

    @Override
    public Iterable<Position<E>> findRange(E minValue, E maxValue) throws RuntimeException {
        if(isEmpty()){
            return null;
        }
        if(comparator.compare(minValue, maxValue) > 0){
            throw new RuntimeException("Invalid range. (min>max)");
        }
        List<Position<E>> l = new LinkedList<>();

        Position<E> nodeMin = treeSearch(minValue, this.binTree.root());
        Position<E> nodeMax = treeSearch(maxValue, this.binTree.root());

//        System.out.println("nodemin = " + nodeMin.getElement());
//        System.out.println("nodemax = " + nodeMax.getElement());

        if(comparator.compare(nodeMin.getElement(), maxValue) > 0 || comparator.compare(nodeMax.getElement(), minValue) < 0){
            return l;
        }
        if(comparator.compare(nodeMin.getElement(), nodeMax.getElement()) <= 0) {
            l.add(nodeMin);
        }
        while((successor(nodeMin) != null) && (comparator.compare(successor(nodeMin).getElement(), nodeMax.getElement()) <= 0)){
            nodeMin = successor(nodeMin);
            l.add(nodeMin);
        }

        return l;
    }

}