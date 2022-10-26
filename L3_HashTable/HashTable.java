package aed.hashtable;

import java.util.NoSuchElementException;
import java.util.Iterator;

import es.upm.aedlib.Entry;
import es.upm.aedlib.EntryImpl;
import es.upm.aedlib.map.*;
import es.upm.aedlib.InvalidKeyException;
import es.upm.aedlib.indexedlist.*;

/**
 * A hash table implementing using open addressing to handle key collisions.
 */
public class HashTable<K, V> implements Map<K, V> {
  Entry<K, V>[] buckets;
  int size;

  public HashTable(int initialSize) {
    this.buckets = createArray(initialSize);
    this.size = 0;
  }

  @Override
  public Iterator<Entry<K, V>> iterator() { // O(n)
    return entries().iterator();
  }

  @Override
  public boolean containsKey(Object arg0) throws InvalidKeyException { //O(n)
    if (arg0 == null) {
      throw new InvalidKeyException();
    } 
    boolean contiene = false; // arg0 => key
    for (int i = 0; i < buckets.length && !contiene; i++) {
      if (buckets[i] != null) {
        contiene = buckets[i].getKey().equals(arg0);
      }
    }
    return contiene;
  }

  @Override
  public Iterable<Entry<K, V>> entries() { // O(n)
    IndexedList<Entry<K, V>> entries = new ArrayIndexedList<Entry<K, V>>();

    for (Entry<K, V> bucket: buckets) {
      if (bucket != null) {
        entries.add(entries.size(), bucket);
      }
    }
    return entries;
  }

  @Override
  public V get(K arg0) throws InvalidKeyException { //O(2n)
    if (arg0 == null) {
      throw new InvalidKeyException();
    } else {
      V res = null;
      if (containsKey(arg0)) {
        int indiceLocalizado = search(arg0);
        res = buckets[indiceLocalizado].getValue();
      }
      return res;
    }
  }

  @Override
  public boolean isEmpty() { //O(1)
    return size() == 0;
  }

  @Override
  public Iterable<K> keys() { // O(n)
    IndexedList<K> claves = new ArrayIndexedList<K>();
    for (Entry<K, V> e : buckets) {
      if (e != null) {
        claves.add(claves.size(), e.getKey());
      }
    }
    return claves;
  }

  @Override
  public V put(K arg0, V arg1) throws InvalidKeyException { //O(n)
    if (arg0 == null) {
      throw new InvalidKeyException();
    }
    if (buckets.length == size()) {
      rehash();
    }
    Entry<K, V> nuevaEntrada = new EntryImpl<K, V>(arg0, arg1); // Entrada a insertar
    int indice = search(arg0);
    V value = null;
    
    if (buckets[indice] != null) {
      value = buckets[indice].getValue();
    } else size++;
    buckets[indice] = nuevaEntrada;
    return value;
  }

  @Override
  public V remove(K arg0) throws InvalidKeyException {
    if(arg0 == null) throw new InvalidKeyException();
    if (containsKey(arg0)) {
      int posK = index(arg0);
      
      //busco el elemento
      boolean encontrado = false;

      while(!encontrado){

        if((buckets[posK] != null) && arg0.equals(buckets[posK].getKey())){
          encontrado = true;
        } else {
          posK = (posK +1) % buckets.length;
        }
      }

      V res = buckets[posK].getValue();
      buckets[posK] = null;
      size--;
      // Una vez borrada, debemos colapsar huecos
      // Comprobamos si los siguientes elem posK podemos recolocarlo
      int contador = 0;
      while (contador < size() && (buckets[posK++] != null)) {
        int indicePreferido = index(buckets[posK++].getKey());
        if (indicePreferido == posK) {
          buckets[posK] = buckets[posK++];
          buckets[posK++] = null;
        }
        posK++;
      }
      return res;
    }
    return null;
  }

  @Override
  public int size() { //O(1)
    return size;
  }
  

  @SuppressWarnings("unchecked")
  private Entry<K, V>[] createArray(int size) { //O(1)
    Entry<K, V>[] buckets = (Entry<K, V>[]) new Entry[size];
    return buckets;
  }

  /* Returns the bucket index of an object */
  private int index(Object obj) { //O(1)
    return Math.abs(obj.hashCode()) % buckets.length;
  }

  /**
   *  Returns the index where an entry with the key is located 
   *  or if no such entry exists, the "next" bucket with no entry,
   *  or if all buckets stores an entry, -1 is returned. 
   */
  private int search(Object obj) { //O(n)
    int indicePreferido = index(obj);
    int contador = 0;

    int n = -1;
    while (contador < buckets.length) {
      if (buckets[indicePreferido] == null || buckets[indicePreferido].getKey().equals(obj)) {
        return indicePreferido;
      }
      indicePreferido = (indicePreferido + 1) % buckets.length;
      contador++;
    }
    return n;
  }

  /* Doubles the size of the bucket array, and inserts all entries present
  in the old bucket array into the new bucket array, in their correct
  places. Remember that the index of an entry will likely change in
  the new array, as the size of the array changes. 
  */
  private void rehash() { //O(n^2)
    Entry<K, V>[] newBuckets = createArray(buckets.length * 2);
    Entry<K, V>[] oldBuckets = buckets;
    buckets = newBuckets;

    for (Entry<K, V> bucket : oldBuckets) {
      if (bucket != null) {
        // Ahora hay espacio para insertar
        buckets[search(bucket.getKey())] = bucket;
      }
    }
  }
}
