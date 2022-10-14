## Individual3

Se pide implementar la funcion `static <E> Iterable<E> filter(Iterable<E> l, Predicate<E> pred)` en la clase **Utils**

Este método recibe un `Iterable<E> l` y devuelve la misma estructura, solo que aplica los siguientes filtros
 - r no podrá contener elementos null
 - I r no contendrá los elementos filtrados por `Predicate<E> pred`

#### Predicados

Definen cuándo un objeto cumple una determinada condición

~~~~
public interface Predicate<T> {
  boolean test(T t); // Evaluates the predicate on its argument t
}
~~~~


#### Predicados

El Tester usa la clase GreaterThan que implementa el interfaz Predicate. La clase tiene un constructor `GreaterThan(E e)` y su método `test(arg)` 
devuelve true si “arg > e” y false si no lo es

---
##### Trabajar con iteradores
##### No está permitido el for-eac
