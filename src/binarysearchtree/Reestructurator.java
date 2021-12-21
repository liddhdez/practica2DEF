package binarysearchtree;

import binaryTree.LinkedBinaryTree;
import material.Position;

//Clase reestructuracion
public class Reestructurator<E> extends LinkedBinaryTree<E> {
    public Reestructurator() {
        this.addRoot(null); //Constructor vacio
    }

    /**
     * Performs a tri-node restructuring. Assumes the nodes are in one of
     * following configurations:
     *
     * <pre>
     *          z=c       z=c        z=a         z=a
     *         /  \      /  \       /  \        /  \
     *       y=b  t4   y=a  t4    t1  y=c     t1  y=b
     *      /  \      /  \           /  \         /  \
     *    x=a  t3    t1 x=b        x=b  t4       t2 x=c
     *   /  \          /  \       /  \             /  \
     *  t1  t2        t2  t3     t2  t3           t3  t4
     * </pre>
     *
     * @return the new root of the restructured subtree
     */
    public Position restructure(Position posNode, LinkedBinaryTree binTree) {
        BTNode<E> low, medium, high, t1, t2, t3, t4;
        //posNode = x
        Position<E> posx = posNode;
        Position<E> posy = binTree.parent(posNode); //Parent
        Position<E> posz = binTree.parent(posy); //Grandparent
        //Miramos si estan los deseq a la izda o a la derecha
        boolean childLeft = (binTree.left(posz) == posy);
        boolean grandChildLeft = (binTree.left(posy) == posx);
        BTNode<E> node = (BTNode<E>) posNode;
        BTNode<E> x = (BTNode<E>) posx;
        BTNode<E> y = (BTNode<E>) posy;
        BTNode<E> z = (BTNode<E>) posz;

        //Primer caso: deseq izda-izda
        if (childLeft && grandChildLeft) {
            low = x;
            medium = y;
            high = z;
            t1 = low.getLeft();
            t2 = low.getRight();
            t3 = medium.getRight();
            t4 = high.getRight();
        } else if (childLeft && !grandChildLeft) { //Izda - dcha
            low = y;
            medium = x;
            high = z;
            t1 = low.getLeft();
            t2 = medium.getLeft();
            t3 = medium.getRight();
            t4 = high.getRight();
        } else if (!childLeft && grandChildLeft) {
            low = z;
            medium = x;
            high = y;
            t1 = low.getLeft();
            t2 = medium.getLeft();
            t3 = medium.getRight();
            t4 = high.getRight();
        } else {
            low = z;
            medium = y;
            high = x;
            t1 = low.getLeft();
            t2 = medium.getLeft();
            t3 = high.getLeft();
            t4 = high.getRight();
        }
        if (binTree.isRoot(posz)) {
            binTree.addRoot(medium);
        } else {
            BTNode<E> aux = (BTNode<E>) binTree.parent(posz);
            if (binTree.left(aux) == posz) {
                aux.setLeft(medium);
            } else {
                aux.setRight(medium);
            }
            medium.setParent(aux);
        }
        //CAMBIAMOS HIJOS Y PADRES
        medium.setLeft(low);
        medium.setRight(high);
        low.setParent(medium);
        low.setLeft(t1);
        t1.setParent(low);
        low.setRight(t2);
        t2.setParent(low);
        high.setParent(medium);
        high.setLeft(t3);
        t3.setParent(high);
        high.setRight(t4);
        t4.setParent(t3);
        //RETORNAMOS LA "RAIZ" = MEDIUM
        return medium;
    }
}
