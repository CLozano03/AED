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
public class HashTable<K,V> implements Map<K,V> {
  Entry<K,V>[] buckets;
  int size;
  
  public HashTable(int initialSize) {
    this.buckets = createArray(initialSize);
    this.size = 0;
  }
  
  /**
   * Add here the method necessary to implement the Map api, and
   * any auxilliary methods you deem convient.
   */

  public Iterator<Entry<K, V>> iterator() { //O(n)
    return entries().iterator();
  }
  
  @Override
  public boolean containsKey(Object arg0) throws InvalidKeyException {
    /* if(arg0 instanceof (K)) { //COMPROBAR SI ES DE TIPO CLAVE(K)
      throw new InvalidKeyException();
      return false;
    
    } */
    boolean contiene = false;
    Object actual = arg0; // arg0 siempre va a ser key, no otro objeto
    for (int i = 0; i < buckets.length && !contiene; i++) {
      if (buckets[i] != null) {
        contiene = buckets[i].getKey().equals(actual);
      }
    }
    return contiene;
  }
  
  @Override
  public Iterable<Entry<K, V>> entries() { //O(n)
    // recorremos con el iterator EN EL FUTURO
    IndexedList<Entry<K, V>> entries = new ArrayIndexedList<Entry<K, V>>(); 

    for(Entry<K,V> bucket: buckets){
      entries.add(entries.size(), bucket);
    }
    return entries;
  }
  
  @Override
  public V get(K arg0) throws InvalidKeyException {
    Entry<K,V> o = new EntryImpl<K,V>(arg0, null);
    if (arg0 == null || !containsKey(o)) throw new InvalidKeyException();
    else {
      V res = null;
      int i = 0;
      boolean done = false;
      while (i < buckets.length && !done) {
        if (buckets[i].getKey() == arg0) {
          res = buckets[i].getValue();
          done = true;
        }
      } 
      return res;
    }
  }
  
  @Override
  public boolean isEmpty() {
    return size() == 0;
  }
  
  @Override
  public Iterable<K> keys() { //O(n)
    IndexedList<K> claves = new ArrayIndexedList<K>(); 
    for(Entry<K, V> e: buckets) {
      claves.add(claves.size(), e.getKey());
    }
    return claves;
  }
  
  @Override
  public V put(K arg0, V arg1) throws InvalidKeyException {
    if(arg0 == null) {
      throw new InvalidKeyException();
    } else {
      V value = null;

      Entry<K,V> entrada = new EntryImpl<K,V>(arg0, arg1);
      int indice = index(entrada);

      if(buckets[indice] == null){
        buckets[indice] = entrada;
        size++;
      } else {
        value = buckets[indice].getValue(); 
        buckets[indice] = entrada; 
        size++;
      } 
      return value;
    }
  }
  
  @Override
  public V remove(K arg0) throws InvalidKeyException {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public int size() {
    return size;
  }
  /*
    Debe tener en cuenta que podr´ıa estar en lugar diferente de su bucket
  \preferido": podr´ıa haber habido colisiones en la inserci´on.
  Algoritmo:
  I Calcular index, el bucket preferido de la clave.
  I Asignar un variable i = index.
  I Repetir:
        F Si buckets[i] est´a vac´ıo, la clave no est´e en la tabla.
        F Si la clave de buckets[i] es la que buscamos, hemos encontrado la
        Entry que necesitamos. Devolver su valor.
        F Si no, ir al siguiente i (circularmente).
  Si i = index termina la b´usqueda sin ´exito.
  
  
  
  
  El procedimiento de colapsar huecos termina cuando encontramos un
  bucket vac´ıo no creado por el procedimiento mismo o cuando
  llegamos al principio (al bucket originalmente borrado).
   */
  
  // Examples of auxilliary methods: IT IS NOT REQUIRED TO IMPLEMENT THEM
  
  @SuppressWarnings("unchecked") 
  private Entry<K,V>[] createArray(int size) {
    Entry<K,V>[] buckets = (Entry<K,V>[]) new Entry[size];
    return buckets;
  }
  
  // Returns the bucket index of an object
  private int index(Object obj) { // COMPROBAR SI HAY QUE HACER EL HASHCODE DIRECTO
    if (!(obj instanceof Entry)) throw new IllegalArgumentException("No has metido un objeto de tipo Entry");
    
    Entry<K,V> e = (Entry<K, V>) obj;
    return Math.abs(e.getKey().hashCode()) % size();
}

  // Returns the index where an entry with the key is located,
  // or if no such entry exists, the "next" bucket with no entry,
  // or if all buckets stores an entry, -1 is returned.
  private int search(Object obj) {
    if (!(obj instanceof Entry)) return -1;
    if (size == 0) return -1;

    int indicePreferido = index(obj);
    int contador = 0; // variable que me va a comprobar si todos los Entry almacenan un valor

    while(buckets[indicePreferido] != null && contador < size) {
      indicePreferido = (indicePreferido + 1) % size();
      contador++;
    }

    return contador == size? -1: indicePreferido;
  }

  // Doubles the size of the bucket array, and inserts all entries present
  // in the old bucket array into the new bucket array, in their correct
  // places. Remember that the index of an entry will likely change in
  // the new array, as the size of the array changes.
  private void rehash() {
    Entry<K,V>[] newBuckets = createArray(buckets.length*2);
    Entry<K,V>[] oldBuckets = buckets;
    buckets = newBuckets;
    
    int indiceInsertar;
    for(Entry<K, V> bucket: oldBuckets) {
      if (bucket != null) {
        indiceInsertar = search(bucket);
        if(indiceInsertar != -1){
          buckets[indiceInsertar] = bucket;
        }  
      }
    }
  }

  /*
   * --------------------------------------------------
   *                      TESTS
   * --------------------------------------------------
   */
  public static void main(String[] args) {
    System.out.println(new HashTable<Integer, Integer>(5).iterator());
  }
}

