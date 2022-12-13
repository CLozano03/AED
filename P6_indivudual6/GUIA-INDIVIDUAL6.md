## Individual de Grafos

Implementar el siguiente método:

`static <E> Map<Vertex<Integer>,Integer> sumVertices(DirectedGraph<Integer,E> g)`

Dado un grafo dirigido G, cuyos vértices contienen elementos de tipo `Integer`, el método devuelve un objeto de tipo `Map<Vertex<Integer>, Integer>` donde:
- Las claves son todos los vértices del grafo
- El valor asociado a cada clave (es decir, a cada vértice) **V** es la suma de los elementos(son Integer) de los vértices alcanzables ("reachable") desde v **sin pasar por un mismo vértice dos veces**

#### A tener en cuenta

El método no debe cambiar el grafo; se comprueba
que los elementos de de los vertices y los aristas no cambian.

No es permitido añadir nuevos atributos.
