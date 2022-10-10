package aed.filter;

import es.upm.aedlib.indexedlist.*;
import java.util.Iterator;
import java.util.function.Predicate;


public class Utils {

  public static <E> Iterable<E> filter(Iterable<E> d, Predicate<E> pred) {
    //Creamos una indexedList para almacenar la informacion del nuevo iterable
    IndexedList<E> result = new ArrayIndexedList<E>();
    Iterator<E> iterator = d.iterator();
    
    while (iterator.hasNext()) {
      //Guardo mi elemeto
      E e = iterator.next();
      
      //Aplico los filtros y aniado en caso de que los pase
      if(e != null && pred.test(e)){
        result.add(result.size(), e);
      }
    }
    return (Iterable<E>)result;
  }
}
