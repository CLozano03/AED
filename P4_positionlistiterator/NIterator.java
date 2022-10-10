package aed.positionlistiterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.PositionList;


public class NIterator<E> implements Iterator<E> {
  
  private PositionList<E> list;
  private int n;
  private Position<E> cursor;
  boolean haDevuelto;

  public NIterator(PositionList<E> list, int n) {
    this.list = list;
    this.n = n;
    haDevuelto = false;
    cursor = list.first();
  }

  @Override
  public boolean hasNext() {
    if (!haDevuelto && cursor != null) {
      return true;
    }else if(cursor == null){
      return false;
    } else {

      boolean hasNext = true; 
      Position<E> cursor1 = cursor;
 
      int i = 0;
      while(hasNext && i < n){
        cursor1 = list.next(cursor1);
        hasNext = cursor1 != null;
        i++;
      }
  
      return hasNext;
    }
  }

  @Override
  public E next() {
    
    if(!hasNext()) {
      throw new NoSuchElementException();
    } else if(cursor.element() == null){
      cursor = list.next(cursor);
      return next();
    } if(!haDevuelto){
      //Es el primero que devuelve
      haDevuelto = true;
      return cursor.element();
    } else {
      
      //Muevo el cursor segun n 
      int i = 0;
      while( cursor != null && (cursor.element() == null || i < n)) {
        cursor = list.next(cursor);
        i++;
      }
        E e = cursor.element();
        haDevuelto = true;
        return e;
    }
  }
}          