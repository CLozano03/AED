# L7: Grafos

**Objetivo:** Completar la clase `Delivery<V>`:
~~~
public class Delivery<V> {
    // Construct a graph from a series of vertices
    // with labels of type V and an adjacency matrix.
    public Delivery(V[] places, Integer[][] gmat) {...}
    // Return the constructed graph
    public DirectedGraph<V, Integer> getGraph() {...}
    // Return a path (a list of vertices) in the constructed
    // graph that contains every node in the graph
    // exactly once. Return null if there is no such path.
    public PositionList <Vertex<V>> tour() {...}
    // Return the length of a path in the graph.
    // The path has to have at least one node.
    public Integer length(PositionList<Vertex<V>> path) {...}
}
~~~

#### Datos del constructor:

- **`V[] places`** es un array que contiene los vertices del grafo.
- **`int[][] gmat`** es la matriz de adyacecencia del grafo dirigido, donde las filas y columnas representan los vertices, segun aparecen ordenados en `V[] places`. Cuando un elemento de la matriz sea `null`, significará que no existe una arista que conecte los dos vertices implicados.

#### A tener en cuenta:

- Hay muchas maneras de buscar los caminos que queremos. Es válido explorar todas las posibilidades hasta encontrar uno: **recursión + backtracking**.
- Puede ser computacionalmente muy costoso (p.e., O(n!)). Un código descuidado rebasaría tiempos de ejecución razonables. **No hay restricciones** en cuanto a variables internas, artibutos o métodos privados. Sin embargo, aparte de las estructuras de datos disponibles en la librería `aedlib`, está permitido utilizar `java.util.Iterator`, pero no otras clases de `java.util`.
- La clase `DirectedGraph<V>` implementa `toString()`. Puede usarse para examinar el grafo generado.
- Si hay varios caminos válidos, algunos serán mínimos. **No pedimos buscar caminos mínimos** sólo uno que pase una vez por cada nodo.