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
    } else if (end- start == 1){
      return l.get(start).compareTo(l.get(end)) > 0? end : start;
    } else {
      int indice = (start + end)/2;
      if(l.get(indice).compareTo(l.get(indice - 1)) <= 0 && l.get(indice).compareTo(l.get(indice + 1)) <= 0){
        return indice;
      } else {
        return findBottomAux(l, start, indice);
      }
    }
  }
	
  public static <E extends Comparable<E>> NodePositionList<Pair<E,Integer>>
    joinMultiSets(NodePositionList<Pair<E,Integer>> l1,
		  NodePositionList<Pair<E,Integer>> l2) {
    return null;
  }
}
