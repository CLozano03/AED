package aed.individual5;

import java.util.Iterator;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;
import es.upm.aedlib.map.*;

public class Utils {

  public static boolean eqNull(Object o1, Object o2){
    return (o1 == o2) || (o1 != null && o1.equals(o2));
  }
  
  public static <E> PositionList<E> deleteRepeated(PositionList<E> l) {
    PositionList<E> result = new NodePositionList<E>();
    Position<E> cursor; 
    boolean estaContenido;

    for(E element : l) {
      estaContenido = false;
      cursor = result.first();
      while(cursor != null && !estaContenido){
        estaContenido = eqNull(element, cursor.element());
        cursor = result.next(cursor);
      }
      if(!estaContenido){
        result.addLast(element);
      }
    }
    return result;
  }
  
  public static <E> PositionList<E> compactar (Iterable<E> lista) {
    if(lista == null) {throw new IllegalArgumentException("El iterable es null");}
    
    PositionList<E> result = new NodePositionList<E>();
    Iterator<E> iterator = lista.iterator();

    E element;
    E elementoPrevio = null;

    while(iterator.hasNext()) {
      element = iterator.next();

      //añado el elemento si no es igual a su anterior
      if(!eqNull(element, elementoPrevio) || result.size() == 0){
        result.addLast(element);
        elementoPrevio = element;
      }
    } 
    return result;
  }
  
  public static Map<String,Integer> maxTemperatures(TempData[] tempData) {
    Map<String,Integer> maxTemp = new HashTableMap<String, Integer>();

    for(TempData ciudad: tempData){

      //Compruebo primero si esta la ciudad metida
      if(!maxTemp.containsKey(ciudad.getLocation())){
        maxTemp.put(ciudad.getLocation(), ciudad.getTemperature());
        
        //Si esta, compruebo cual de las temperaturas es mayor 
      } else if(ciudad.getTemperature() > maxTemp.get(ciudad.getLocation())){
        maxTemp.put(ciudad.getLocation(), ciudad.getTemperature());
      }
    }
    return maxTemp;
  }
  
}


