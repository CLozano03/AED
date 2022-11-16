package aed.recursion;

import es.upm.aedlib.Pair;
import es.upm.aedlib.Position;
import es.upm.aedlib.indexedlist.*;
import es.upm.aedlib.positionlist.*;


public class Utils {

  public static int multiply(int a, int b) {
    int sign = a <0? -1:1;
    if(a == 0){
      return 0;
    } else if(a%2 == 0) {
      return multiply(a/2, b*2);
    } else {
      return b * sign + multiply(a/2, b*2);
    }
  }

  public static <E extends Comparable<E>> int findBottom(IndexedList<E> l) {
    return findBottomAux(l,0, l.size()-1);
  }

  public static <E extends Comparable<E>> int findBottomAux(IndexedList<E> l, int start, int end) {
    if(start == end){
      return start; //ese elemento es un hoyo. Devuelvo el indice del hoyo
    } else if (end - start == 1){
      return l.get(start).compareTo(l.get(end)) > 0? end : start;
    } else {
      int indice = (start + end)/2;
      if(l.get(indice).compareTo(l.get(indice - 1)) < 0 && l.get(indice).compareTo(l.get(indice + 1)) < 0){
        return indice;
      } else if(l.get(indice).compareTo(l.get(indice - 1)) > 0){
        return findBottomAux(l, start, indice -1);
      } else if (l.get(indice).compareTo(l.get(indice + 1)) > 0) {
        return findBottomAux(l,indice +1, end);
      } else {
        return -1;
      }
    }
  }
	
  public static <E extends Comparable<E>> NodePositionList<Pair<E,Integer>>
    joinMultiSets(NodePositionList<Pair<E,Integer>> l1,
		  NodePositionList<Pair<E,Integer>> l2) {
        NodePositionList<Pair<E,Integer>> res = new NodePositionList<Pair<E,Integer>>();
      return joinMultiSetsAux(l1, l2, res, l1.first(), l2.first());
  }

  public static <E extends Comparable<E>> NodePositionList<Pair<E,Integer>>
    joinMultiSetsAux(NodePositionList<Pair<E,Integer>> l1,
		  NodePositionList<Pair<E,Integer>> l2, NodePositionList<Pair<E,Integer>> res, Position<Pair<E,Integer>> cursor1, Position<Pair<E,Integer>> cursor2) {
        if(cursor1 == null && cursor2 == null) {
          return res;
        } else if(cursor1 == null) {
          res.addLast(cursor2.element());
          return joinMultiSetsAux(l1,l2, res, cursor1, l2.next(cursor2));
        } else if (cursor2 == null){
          res.addLast(cursor1.element());
          return joinMultiSetsAux(l1, l2, res, l1.next(cursor1), cursor2);
        } else if(cursor1.element().getLeft().compareTo(cursor2.element().getLeft()) == 0){
          Pair<E,Integer> el1 = cursor1.element();
          Pair<E,Integer> el2 = cursor2.element();

          res.addLast(el1);
          res.last().element().setRight(el1.getRight() + el2.getRight());
          return joinMultiSetsAux(l1, l2, res, l1.next(cursor1), l2.next(cursor2));
        } else if(cursor1.element().getLeft().compareTo(cursor2.element().getLeft()) < 0){
          res.addLast(cursor1.element());
          return joinMultiSetsAux(l1, l2, res, l1.next(cursor1), cursor2);
        } else {
          res.addLast(cursor2.element());
          return joinMultiSetsAux(l1, l2, res, cursor1, l2.next(cursor2));
        }
    }
}
