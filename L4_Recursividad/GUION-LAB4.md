# L4: Recursividad

Se pide implementar los metodos de las clases `Utils` y `Explorador`:
- **multiply**
- **findBottom**
- **joinMultiSets**
- **explora**

Es obligatorio usar recursión en la implementación de esta práctica

###### Normas:
1. En la clase `Utils`: está prohibido usar bucles for, for-each, while, do-while, o iteradores.
2. En la clase `Explorador`: sólo está permitido el uso de bucles para explorar los caminos que salen de un Lugar (ver después).
3. NO está permitido añadir nuevos atributos a clases.
4. NO está permitido crear nuevas estructuras de datos como pilas, colas, etc. Unicamente est´a permido crear nuevas NodePositionList para los resultados a devolver por el método joinMultiSets y/o sus métodos auxiliares.
---
## Multiply

Implementa *recursivamente* el siguiente algoritmo para **multiplicar** dos enteros `a` y `b`:

- sign := −1 si a < 0 y 1 si no
- sum := 0
- while a 6= 0
    - sum := sum + b si a mod 2 6= 0
    - a := a/2
    - b := b ∗ 2
- return sign ∗ sum

en el método:
`public static int multiply(int a, int b)`

--- 

## FindBottom

Implementar el método:
`public <E extends<Comparable>> static int findBottom(IndexedList<E> list)` que devuelve un indice en list que corresponde a un ***hoyo*** .

Un *hoyo* es un elemento de `list` que no es mayor que sus vecinos. Si i es el índice de un hoyo y tiene dos vecinos se cumple:
`list.get(i-1) >= list.get(i) <= list.get(i+1)`
- Por ejemplo:
    - En la lista `[8,6,3,4]` el elemento 3 es el único hoyo.
    - En la lista `[1,4,5,3,3]` hay tres hoyos: 1 (que solo tiene un vecino), y los dos elementos 3.

- Si hay múltiples hoyos, puede devolver el índice de cualquiera de ellos. Si no hay un hoyo, el método debe devolver -1.
- La lista `list` nunca contiene elementos `null`.
- Para obtener la máxima puntuación se debe implementar con un algoritmo de **O(log n)**. El algoritmo de **Búsqueda binaria** es capaz de alcanzar esta complejidad: [Wikipedia-Divide y venceras](https://es.wikipedia.org/wiki/Algoritmo_divide_y_vencer%C3%A1s)
- Este algoritmo se debe implementar de forma **recursiva**.

---

## Join MultiSets

- Un *multiset* se comporta como un set, excepto que los multisets admiten **elementos repetidos**.
- Vamos a usar una representación más eficiente que un array usual: una lista de pares: `Pair(Element,Count)`. `Count` indica cuantas occurencias de `Element` hay en el *multiset*.


Implementar el método:
`public static <E extends Comparable<E>> NodePositionList<Pair<E,Integer>> joinMultisets(NodePositionList<Pair<E,Integer>> l1, NodePositionList<Pair<E,Integer>> l2)`
- El método recibe dos listas `l1` y `l2`. Cada lista representa multisets mediante pares `Pair(Element, Count)`. Count indica el número de elementos Element en el multiset.
- **Importante:** las listas `l1` y `l2` están ordenadas. Si `Element1 < Element2, entonces Pair(Element1,Count1)` aparece
antes de `Pair(Element2, Count2)`.
- El método `joinMultisets` deber devolver una lista **nueva** que representa la unión de los multisets l1 y l2.
- **Obligatorio:** la lista devuelta por `joinMultisets` también tiene que estar ordenada en orden creciente.

##### A tener en cuenta:
- Las listas nunca contendrán elementos `null`.
- Para comparar dos elementos de tipo `E` se puede usar el metodo
`compareTo` ya que `E` implementa el interfaz `Comparable`.

---
## Explora

**Objetivo:** desarrollar un método `Pair<Object, PositionList<Lugar>> explora(Lugar inicialLugar) { ... }` en la clase `Explorador.java`
que recorra un laberinto sistemáticamente y encuentre un “tesoro”.
- El laberinto esta compuesto por “lugares”, implementados como
objetos de la clase Lugar
- En un lugar:
    - Puede haber un **tesoro** (un objeto, no nulo).
    - Cada lugar puede estar marcado con tiza o no (para detectar que ya ha sido visitado).
    - Cada lugar puede tener caminos hacia otros lugares.
- El método debe devolver un par con el tesoro encontrado y un camino `PositionList<Lugar>` que lleva al tesoro:
    - Si no hay ningún tesoro alcanzable, el método devolverá `null`.
    - En otro caso, el camino debe contener al menos el lugar desde el que arranca la búsqueda.

##### Clase lugar
~~~
public class Lugar {
    public boolean tieneTesoro()            // Devuelve true si el lugar tiene un tesoro
    public Object getTesoro()               // Devuelve el tesoro (un objeto) o null
    public Iterable<Lugar> caminos()        // Devuelve los lugares conectados con el lugar (del objeto)
    public void marcaSueloConTiza()         // Permite marcar el ‘‘suelo’’ en el lugar con ‘‘tiza’’
    public boolean sueloMarcadoConTiza()    // Esta marcado el suelo con tiza?
    public String toString()                // Para imprimir el lugar
    public void printLaberinto()            // Imprime todo el laberinto
}
~~~


 

