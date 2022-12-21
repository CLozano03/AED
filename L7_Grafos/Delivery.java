package aed.delivery;

import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.graph.DirectedGraph;
import es.upm.aedlib.graph.DirectedAdjacencyListGraph;
import es.upm.aedlib.graph.Vertex;
import es.upm.aedlib.graph.Edge;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.map.Map;
import es.upm.aedlib.set.HashTableMapSet;
import es.upm.aedlib.set.Set;
import java.util.Iterator;

public class Delivery<V> {

  private DirectedGraph<V,Integer> g;
  private Map<Integer, Vertex<V>> mapa;

  // Construct a graph out of a series of vertices and an adjacency matrix.
  // There are 'len' vertices. A negative number means no connection. A non-negative
  // number represents distance between nodes.
  public Delivery(V[] places, Integer[][] gmat) {
    g = new DirectedAdjacencyListGraph<V, Integer>();
    mapa = new HashTableMap<Integer, Vertex<V>>();

    for(int i = 0; i < places.length; i++){
      mapa.put(i, g.insertVertex(places[i]));   // Añadimos Vertex al grafo y lo metemos en el mapa
    }
    for(int i = 0; i < gmat.length; i++){
      for(int j = 0; j < gmat.length; j++){
          if(gmat[i][j] != null){
            g.insertDirectedEdge(mapa.get(i), mapa.get(j), gmat[i][j]);
          }
      }
    }
  }

  // Just return the graph that was constructed
  public DirectedGraph<V, Integer> getGraph() {
    return g;
  }

  // Return a Hamiltonian path for the stored graph, or null if there is none.
  // The list containts a series of vertices, with no repetitions (even if the path
  // can be expanded to a cycle).
  public PositionList <Vertex<V>> tour() {
    PositionList <Vertex<V>> camino; 
    for(Vertex<V> vertice: g.vertices()){
      camino = tourAux(vertice, new NodePositionList<>(), new HashTableMapSet<>());
      if(camino.size() == g.numVertices()){
        return camino;
      }
    }
    return null;
  }

  private PositionList <Vertex<V>> tourAux(Vertex<V> nodoVisitando, PositionList<Vertex<V>> path, Set<Vertex<V>> visitados){
    return null;
  }

  public int length(PositionList<Vertex<V>> path) {
    int len = 0;
    Iterator<Vertex<V>> itCamino = path.iterator();
    Iterator<Edge<Integer>> itAristas;
    Vertex<V> aux = itCamino.next();
    Edge<Integer> arista;
    boolean found;

    while(itCamino.hasNext()){
      found = false;
      itAristas = g.outgoingEdges(aux).iterator();
      aux = itCamino.next();

      while(itAristas.hasNext() && !found){
        arista = itAristas.next();
        if(g.endVertex(arista).equals(aux)){
          found = true;
          len += arista.element();
        }
      }   
    }
    return len;
  }

  public String toString() {
    return g.toDot();
  }
}
