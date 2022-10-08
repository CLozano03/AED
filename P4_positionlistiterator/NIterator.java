package aed.positionlistiterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.PositionList;


public class NIterator<E> implements Iterator<E> {
  
  private PositionList<E> list;
  private int n;
  private Position<E> cursor;

  public NIterator(PositionList<E> list, int n) {
    this.list = list;
    this.n = n;
    cursor = list.first();
  }

  @Override
  public boolean hasNext() {
    boolean hasNext = true; 
    Position<E> cursor1 = cursor;

    for(int i = 0 ; i< n && hasNext; i++){
      cursor1 = list.next(cursor1);
      hasNext = cursor1 != null;
    }

    return hasNext;
  }

  @Override
  public E next() {
    
    if(!hasNext() || cursor == null){
      throw new NoSuchElementException();
    } else if(cursor.element() == null){
      int i = 0;
      while(cursor.element() == null && i < n){
        cursor = list.next(cursor);
        i++;
      }
      return next();
    } else {
      E e = cursor.element();
  
      //Muevo el cursor segun n 
      int i = 0;
      while(cursor.element() == null && i < n){
        cursor = list.next(cursor);
        i++;
      }
  
      return e;

    }

  }


}
//  [1,2,null,3,4,null,null] //n=2

//   ^         