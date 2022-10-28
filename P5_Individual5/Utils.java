package aed.individual5;

import java.util.Iterator;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;
import es.upm.aedlib.map.*;

public class Utils {
  
  public static <E> PositionList<E> deleteRepeated(PositionList<E> l) {
    PositionList<E> result = new NodePositionList<E>();
    Position<E> cursor; 
    boolean estaContenido;

    for(E element : l) {
      estaContenido = false;
      cursor = result.first();
      while(cursor != null && !estaContenido){
        if(element == null){
          estaContenido = element == cursor.element();
        } else {
          estaContenido = element.equals(cursor.element());
        }
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

      //Distingo comparaciones en funcion de si el elemento es null
      if(element == null) {
        if(element != elementoPrevio || result.size() == 0) {
          result.addLast(element);
          elementoPrevio = element;
        }
      } else if(!element.equals(elementoPrevio)) {
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


