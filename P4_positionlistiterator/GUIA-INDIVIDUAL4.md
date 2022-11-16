# Individual4

## Tarea: Implementar un iterador sobre una PositionList


- Se pide terminar la implementación de la siguiente clase con los métodos `E next()` y `boolean hasNext()`
~~~~
public class NIterator<E> implements Iterator<E> {
  // Constructor
  public NIterator(PositionList<E> list, int n) {
  ...
  }
}
~~~~

- La clase `NIterator<E>` implementa un iterador sobre
elementos de tipo `<E>`

- Tiene un constructor que recibe como argumento una `PositionList`, y un entero n > 0.
- No está permitido modificar la lista parámetro al constructor.
- No es necesario implementar el metodo remove del iterador.
- Está permitido y es necesario añadir atributos dentro la clase.
  
#### Métodos next() y hasNext()

- El primer elemento devuelto es el primer elemento de la lista,
excepto si es null. En este caso el iterador busca el primer
elemento != null en la continuación de la lista, y devuelve este
elemento.
- Después de haber devuelto un elemento, el siguiente elemento
devuelto por el iterador debe ser el elemento n posiciones
adelante en la lista. Si este elemento es null, se
busca y devuelve el primer elemento != null en la continuación
de la lista.
