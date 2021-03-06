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

    public class AVLIterator<T> implements Iterator<Position<T>> {

        private Iterator<Position<AVLInfo<T>>> it;

        public AVLIterator(Iterator<Position<AVLInfo<T>>> iterator) {
            this.it = iterator;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Position<T> next() {
            Position<AVLInfo<T>> aux = it.next();
            return aux.getElement();
        }

        @Override
        public void remove() {
            it.remove();
        }
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
        binTree.binTree = reestructurator;
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
        AVLInfo<E> elem = new AVLInfo<>(value);
        Position<AVLInfo<E>> pos = binTree.insert(elem);
        elem.setTreePosition(pos); //OJO!!!
        rebalance(pos);
        return elem;
    }

    private int calculateHeight(Position<AVLInfo<E>> pos){
        Position<AVLInfo<E>> left = binTree.binTree.left(pos);
        Position<AVLInfo<E>> right = binTree.binTree.right(pos);
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

    private Position<AVLInfo<E>> tallerChild(Position<AVLInfo<E>> pos){
        int altLeft = 0;
        int altRight = 0;
        Position<AVLInfo<E>> leftChild = binTree.binTree.left(pos);
        Position<AVLInfo<E>> rightChild = binTree.binTree.right(pos);

        if(leftChild != null ){
            altLeft = leftChild.getElement().height;
        }
        if(rightChild != null){
            altRight = rightChild.getElement().height;
        }

        if(altLeft > altRight){
            return leftChild;
        }else if(altLeft < altRight){
            return rightChild;
        }else {
            //Ahora suponemos que altLeft == altRight
            if (binTree.binTree.isRoot(pos)) {
                return leftChild;
            }

            if (binTree.binTree.left(binTree.binTree.parent(pos)) == pos) {
                return leftChild;
            }
            return rightChild;
        }
    }

    /**
     * Rebalance method called by insert and remove. Traverses the path from p
     * to the root. For each node encountered, we recompute its height and
     * perform a trinode restructuring if it's unbalanced.
     */

    private void rebalance(Position<AVLInfo<E>> zPos) {
        //Vamos de abajo a arriba hasta la raiz
        while(zPos != null){
            if(!isBalanced(zPos)){
                calculateHeight(zPos); //Actualiza la altura de zpos
                if(!(isBalanced(zPos))){
                    //Estamos en zPos. A reestruct le falta el xPos
                    Position<AVLInfo<E>> xPos = tallerChild(tallerChild(zPos)); // Cogemos el hijo mas alto para x e y
                    zPos = reestructurator.restructure(xPos, binTree.binTree); // Return medium
                    calculateHeight(binTree.binTree.left(zPos));
                    calculateHeight(binTree.binTree.right(zPos));
                    calculateHeight(zPos); //Cambiamos medium por la izda, derecha y la raiz
                }
                zPos = binTree.binTree.parent(zPos);
            }
        }

    }

    @Override
    public boolean isEmpty() {
        return binTree.isEmpty();
    }

    @Override
    public E remove(Position<E> pos) {
        AVLInfo<E> p = checkPosition(pos);
        E toReturn = pos.getElement();
        binTree.remove(p.getTreePosition());
        //TODO
        return null;
    }

    @Override
    public int size() {
        return binTree.size();
    }

    private AVLInfo<E> checkPosition(Position<E> p) throws RuntimeException {
        if (p == null) {
            throw new RuntimeException("The position of the AVL node is null");
        } else if (!(p instanceof AVLInfo)) {
            throw new RuntimeException("The position of the AVL node is not AVL");
        } else {
            AVLInfo<E> aux = (AVLInfo<E>) p;
            return aux;
        }
    }


    @Override
    public Iterable<Position<E>> findRange(E minValue, E maxValue) throws RuntimeException {
        ArrayList<Position<E>> list = new ArrayList<>();
        Iterator<Position<E>> iterator = iterator();
        Comparator<E> comparator = new DefaultComparator<>();
        if (comparator.compare(minValue,maxValue)>0){
            throw new RuntimeException("min>maxvalue");
        }
        while (iterator.hasNext()){
            Position<E> next = iterator.next();
            if((comparator.compare(next.getElement(), minValue)>0) && (comparator.compare(maxValue, next.getElement())>0)){
                list.add(next);
            }
        }
        return list;
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
        Iterator<Position<E>> it = iterator();
        ArrayList<Position<E>> resul = new ArrayList<>();
        Comparator<E> comparator = new DefaultComparator<>();
        if (!binTree.binTree.isEmpty()) {
            while (it.hasNext()) {
                Position<E> next = it.next();
                if (comparator.compare(next.getElement(), pos.getElement()) > 0) {
                    resul.add(next);
                }
            }
        }
        return resul;
    }

    @Override
    public Iterable<Position<E>> predecessors(Position<E> pos) {
        Iterator<Position<E>> it = iterator();
        ArrayList<Position<E>> resul = new ArrayList<>();
        Comparator<E> comparator = new DefaultComparator<>();
        if (!binTree.binTree.isEmpty()) {
            while (it.hasNext()) {
                Position<E> next = it.next();
                if (comparator.compare(next.getElement(), pos.getElement()) < 0) {
                    resul.add(next);
                }
            }
        }
        return resul;
    }

    public Iterable<? extends Position<E>> rangeIterator(E m, E M) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Position<E>> iterator() {
        Iterator<Position<AVLInfo<E>>> it = binTree.iterator();
        return new AVLIterator<E>(it);
    }

}

