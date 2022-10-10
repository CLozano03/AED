package aed.filter;

import es.upm.aedlib.indexedlist.*;
import java.util.Iterator;
import java.util.function.Predicate;


public class Utils {

  public static <E> Iterable<E> filter(Iterable<E> d, Predicate<E> pred) {
    IndexedList<E> result = new ArrayIndexedList<E>();
    Iterator<E> iterator = d.iterator();
    
    while (iterator.hasNext()) {
      E e = iterator.next();
      if(e != null && pred.test(e)){
        result.add(result.size(), e);
      }
    }
    return result;
  }
}
