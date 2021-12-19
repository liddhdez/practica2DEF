package binarysearchtree;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import material.Position;

/**
 *
 * @author Lidia
 */
public class AVLTree<E> implements BinarySearchTree<E> {

    public AVLTree() {
        this(new DefaultComparator<>());
    }

    /**
     * Creates a BinarySearchTree with the given comparator.
     *
     * @param c the comparator used to sort the nodes in the tree
     */
    public AVLTree(Comparator<E> c) {
        Comparator<AVLInfo<E>> avlComparator = (o1, o2) -> c.compare(o1.getElement(), o2.getElement());
        binTree = new LinkedBinarySearchTree<>(avlComparator);
        reestructurator = new Reestructurator<>();
        binTree.binaryTree = reestructurator;
    }


    //Esta clase es necesaria para guardar el valor de la altura AVL en los nodos BTNodes
    private class AVLInfo<T> implements Comparable<AVLInfo<T>>, Position<T> {

        private int height;
        private Position<AVLInfo<T>> posInfo;
        private T elem;

        public AVLInfo(T elem) {
            this.elem = elem;
            this.height = 1; //OJO!! INICIALIZAMOS ALTURA A 1
            this.posInfo = null;
        }

        public void setTreePosition(Position<AVLInfo<T>> pos) {
            this.posInfo = pos;
        }

        public Position<AVLInfo<T>> getTreePosition() {
            return posInfo;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        @Override
        public T getElement() {
            return elem;
        }

        @Override
        public int compareTo(AVLInfo<T> o) {
            if(this.elem instanceof Comparable && o.elem instanceof Comparable){
                Comparable<T> c1 = (Comparable<T>) elem;
                return c1.compareTo(o.elem);
            }else{
                throw new ClassCastException("Elem is not comparable");
            }
        }

        @Override
        public String toString() {
            return this.elem.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return this.elem.equals(obj); //???
        }

    }

    private LinkedBinarySearchTree<AVLInfo<E>> binTree;
    private Reestructurator<AVLInfo<E>> reestructurator;

    @Override
    public Position<E> find(E value) {
        AVLInfo<E> valueInfo = new AVLInfo<>(value);
        Position<AVLInfo<E>> pos = binTree.find(valueInfo);
        if(pos == null){
            return null;
        }
        return pos.getElement();
    }

    @Override
    public Iterable<? extends Position<E>> findAll(E value) {
        AVLInfo<E> valueInfo = new AVLInfo<>(value);
        ArrayList<AVLInfo<E>> list = new ArrayList<>();
        for(Position<AVLInfo<E>> n : binTree.findAll(valueInfo)){
            list.add(n.getElement());
        }
        return list;
    }

    @Override
    public Position<E> insert(E value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int calculateHeight(Position<AVLInfo<E>> pos){
        Position<AVLInfo<E>> left = binTree.binaryTree.left(pos);
        Position<AVLInfo<E>> right = binTree.binaryTree.right(pos);
        int altLeft = 0;
        int altRight = 0;
        if(left != null){
            altLeft = left.getElement().height;
        }
        if(right != null){
            altRight = right.getElement().height;
        }
        int alt = 1 + Math.max(altLeft, altRight);
        pos.getElement().setHeight(alt);
        return alt;
    }

    private boolean isBalanced(Position<AVLInfo<E>> pos){
        int alt = calculateHeight(pos);
        return (alt > -2 && alt < 2);
    }

    private Position<AVLInfo<E>> tallerChild(Position<E> pos){
        Position<E> parent = binTree.binaryTree.parent(new AVLInfo<>(pos));
    }
    /**
     * Rebalance method called by insert and remove. Traverses the path from p
     * to the root. For each node encountered, we recompute its height and
     * perform a trinode restructuring if it's unbalanced.
     */
    private void rebalance(Position<AVLInfo<E>> zPos) {
        while(!(binTree.binaryTree.isRoot(zPos))){
            if(!isBalanced(zPos)){
                zPos =
                reestructurator.restructure(zPos, binTree.binaryTree);
            }
        }

    }

    @Override
    public boolean isEmpty() {
        return binTree.isEmpty();
    }

    @Override
    public E remove(Position<E> pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int size() {
        return binTree.size();
    }

    @Override
    public Iterable<Position<E>> findRange(E minValue, E maxValue) throws RuntimeException {
        return null;
    }

    @Override
    public Position<E> first() throws RuntimeException {
        return binTree.first().getElement();
    }

    @Override
    public Position<E> last() throws RuntimeException {
        return binTree.last().getElement();
    }

    @Override
    public Iterable<Position<E>> successors(Position<E> pos) {
        return null;
    }

    @Override
    public Iterable<Position<E>> predecessors(Position<E> pos) {
        return null;
    }

    public Iterable<? extends Position<E>> rangeIterator(E m, E M) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Position<E>> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

