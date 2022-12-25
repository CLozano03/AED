## L2: Polinomios

Se quiere desarrollar un librería para poder manipular y evaluar polinomios sobre una variable y con coeficientes y exponentes enteros.

Dentro la clase representamos un polinomio como una lista de monomios (de tipo `PositionList`) guardada en el atributo monomios dentro Polinomio:
`PositionList<Monomio> terms;`

### Condiciones a cumplir
- Los monomios dentro de un polinomio deberan de estar **ordenados** en orden decreciente de grado. Por ejemplo el polinomio: $x^4 + 3x ^3 - 2$ se almacenaría de la siguiente forma en `terms`:
`[Monomio(1,4), Monomio(3,3), Monomio(-2,0)]`
-  No puede haber varios monomios del **mismo grado** en la lista. Por ejemplo, la representación `[Monomio(1,3), Monomio(2,3)]` no es válida.
- No puede haber ningún monomio con **coeficiente 0** en la lista. El polinomio 0 (o polinomio nulo) se representa como la lista vacía.


Un monomio es un par de un coeficiente y un exponente:

~~~
public class Monomio extends Pair<Integer,Integer> {
public Monomio(int coeficiente, int exponente) {
super(coeficiente, exponente);
}
public int getCoeficiente() { return getLeft(); }
public int getExponente() { return getRight(); }
}
~~~

Se pide **implementar** los siguientes métodos:

~~~
// suma dos polinomios, subtraccion y multiplicacion
static Polinomio suma(Polinomio p1, Polinomio p2);
static Polinomio resta(Polinomio p1, Polinomio p2);
static Polinomio multiplica(Monomio m, Polinomio p);
static Polinomio multiplica(Polinomio p1, Polinomio p2);

// calcula el valor de un polinomio cuando x = valor
long evaluar(int valor);

// devuelve el grado del polinomio
int grado();

// comprueba si dos polinomios son iguales
boolean equals(Object obj);
~~~

---
### Opcional: Implementar constructor

Se pide implementar el siguiente **constructor**: 
`public Polinomio(String polinomio) { ... }` que inicializa el atributo `terms` con el polinomio representado por el `String` polinomio.

#### Pistas para la implementación 
- Pueden aparecer espacios ’ ’ entre las diferentes partes.
- Textualmente un polinomio esta representado por un suma de monomios separados por ’+’.
- `s.split(regexp)` devuelve un array de String donde cada elemento ha sido separado por regexp. Por ejemplo, si `s = "2x^3 + 1x + 2"` entonces `s.split("\ +")` devuelve el array `["2x^3 ", " 1x ", " 2"]`.
- Un “regexp” es una expresion regular. Para esta tarea solo hace falta reconocer caracteres individuales. Por ejemplo la expresion regular "x" reconoce el caracter ’x’. Sin embargo, algunas caracteres como ’+’ y ’^’ tiene signicado especial. Para reconocer estos caracteres tenemos que declarar que no nos interesa el significado especial. Usaremos "\\+" y "\\^" para eso.
