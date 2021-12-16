package binarysearchtree;

import binaryTree.LinkedBinaryTree;
import material.Position;

import java.util.*;


public class LinkedBinarySearchTree<E> implements BinarySearchTree<E> {



    // Clase iterador
    private class BSTIterator<T> implements Iterator<Position<T>>{

        private LinkedBinaryTree<T> binaryTree;
        private int notVisited;
        Iterator<Position<T>> it;

        public BSTIterator(LinkedBinaryTree<T> binaryTree) {
            this.binaryTree = binaryTree;
            this.notVisited = binaryTree.size();
            this.it = binaryTree.iterator();
        }

        @Override
        public boolean hasNext() {
            return notVisited>0;
        }

        @Override
        public Position<T> next() {
            if(notVisited == 0){
                throw new RuntimeException("This tree has no more elements");
            }
            Position<T> next = it.next();
            while(next == null){
                next = it.next();
            }
            notVisited--;
            return next;
        }
    }

    private LinkedBinaryTree<E> binaryTree;
    private Comparator<E> c;
    private int size;

    public LinkedBinarySearchTree(){
        this(null); //MIRAR
    }

    public LinkedBinarySearchTree(Comparator<E> comparator) {
        if(comparator == null) this.c = new DefaultComparator<>();
        else {
            this.binaryTree = binaryTree;
            this.c = comparator;
            this.size = size;
        }
    }

    @Override
    public Position<E> find(E value) {
        if(value == this.binaryTree.root().getElement()){
            return this.binaryTree.root();
        }else{
            return searchTree(this.binaryTree.root(), value);
        }
    }

    private Position<E> searchTree(Position<E> pos, E value){
        if(binaryTree.isLeaf(pos)){
            //Si es una hoja devolvemos un nodo externo
            return pos; //Creo q las hojas son nodos nulos que tienen al final
        }else{
            E current = pos.getElement();
            int dif = c.compare(current, value);
            //NEGATIVO -> SI 1º < 2º
            if(dif < 0){
                searchTree(this.binaryTree.left(pos), value);
            }else if(dif > 0){
                searchTree(this.binaryTree.right(pos), value);
            }
            return pos;
        }
    }

    private void addAll(E value, LinkedList<Position<E>> list, Position<E> aux){
        if(aux != null){
            Position<E> pos = searchTree(aux, value);
            if(!(this.binaryTree.isLeaf(pos))){ // si no es hoja
                //Ha encontrado una entrada con dicho valor...
                //Buscamos por la izda... insercion por orden
                addAll(value, list, this.binaryTree.left(pos));
                list.add(pos);
                addAll(value, list, this.binaryTree.right(pos));
            }
        }
    }

    @Override
    public Iterable<Position<E>> findAll(E value) {
        LinkedList<Position<E>> list = new LinkedList<>();
        addAll(value, list, binaryTree.root());
        return list;
    }

    @Override
    public Position<E> insert(E value) {
        Position<E> pos = searchTree(this.binaryTree.root(), value);
        //Vamos a considerar aquellos que pueden tener valores repetidos...
        while(! binaryTree.isLeaf(pos)){
            //Buscamos por la dcha
            searchTree(binaryTree.right(pos), value);
        }
        return insertAtLeaf(pos, value);
    }

    protected void expandLeaf(Position<E> aux, E v1, E v2){
        if(!binaryTree.isLeaf(aux)){
            throw new RuntimeException("This is not a external node");
        }
        binaryTree.insertLeft(aux, v1);
        binaryTree.insertRight(aux, v2);
    }

    private Position<E> insertAtLeaf(Position<E> pos, E value) {
        expandLeaf(pos, null, null);
        binaryTree.replace(pos, value);
        this.size++;
        return pos;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E remove(Position<E> pos) throws RuntimeException {
        E toReturn = pos.getElement();
        Position<E> r = getLeafToRemove(pos);
        removeLeaf(r);
        return toReturn;
    }

    private void removeLeaf(Position<E> remove){
        removeAboveLeaf(remove);
        this.size--;
    }

    private void removeAboveLeaf(Position<E> remove) {
        Position<E> parent = binaryTree.parent(remove);
        binaryTree.remove(remove);
        binaryTree.remove(parent); //Borramos el parent y la hoja a null que le hemos pasado
    }

    private Position<E> getLeafToRemove(Position<E> pos) {
        //Caso simple: es HOJA
        Position<E> resul = pos;
        if(binaryTree.isLeaf(binaryTree.left(pos))){
            resul = binaryTree.left(pos); // esto pq le estoy pasando la hoja a NULL
        }else if(binaryTree.isLeaf(binaryTree.right(pos))){
            resul = binaryTree.right(pos);
        }else{
            //Nodo interno
            Position<E> swap = resul; //Guardamos para intercambio el que queremos borrar
            //Buscamos el sucesor
            resul = binaryTree.right(resul);
            do{
                resul = binaryTree.left(resul);
            }while(binaryTree.isInternal(resul));
            binaryTree.swap(swap, binaryTree.parent(resul));
        }
        return resul;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterable<Position<E>> findRange(E minValue, E maxValue) throws RuntimeException {
        ArrayList<Position<E>> l = new ArrayList<>();
        if(c.compare(minValue, maxValue)>0){
            throw new RuntimeException("minvalue > maxvalue");
        }else {
            if (! binaryTree.isEmpty()) {
                Position<E> root = this.binaryTree.root();
                findRangeRec(l, root, minValue, maxValue);
            }
        }
        return l;
    }

    private void findRangeRec(ArrayList<Position<E>> l, Position<E> pos, E minValue, E maxValue) {
        if(!binaryTree.isLeaf(pos)) {
            //Fuera de rango
            if (c.compare(pos.getElement(), minValue) < 0) {
                findRangeRec(l, binaryTree.right(pos), minValue, maxValue);
            } else if (c.compare(pos.getElement(), maxValue) > 0) {
                findRangeRec(l, binaryTree.left(pos), minValue, maxValue);
            }
            //Dentro de rango
            else{
                findRangeRec(l, binaryTree.left(pos), minValue, maxValue);
                l.add(pos);
                findRangeRec(l, binaryTree.right(pos), minValue, maxValue);
            }
        }
    }

    @Override
    public Position<E> first() throws RuntimeException {
        if(binaryTree.isEmpty()){
            throw new RuntimeException("The tree is empty");
        }
        Position<E> f = binaryTree.root();
        while( this.binaryTree.hasLeft(f)){
            f = binaryTree.left(f);
        }
        return this.binaryTree.parent(f); //Los nodos hoja NO contienen datos
    }

    @Override
    public Position<E> last() throws RuntimeException {
        if(binaryTree.isEmpty()){
            throw new RuntimeException("The tree is empty");
        }
        Position<E> f = binaryTree.root();
        while( this.binaryTree.hasRight(f)){
            f = binaryTree.right(f);
        }
        return this.binaryTree.parent(f); //Los nodos hoja NO contienen datos
    }

    @Override
    public Iterable<Position<E>> successors(Position<E> pos) {
        return findRange(binaryTree.right(pos).getElement(), last().getElement());
    }

    @Override
    public Iterable<Position<E>> predecessors(Position<E> pos) {
        return findRange(first().getElement(), binaryTree.left(pos).getElement());
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new BSTIterator<>(binaryTree);
    }
}