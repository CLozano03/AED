package aed.individual6;

import es.upm.aedlib.graph.Edge;
import es.upm.aedlib.graph.Vertex;
import es.upm.aedlib.graph.DirectedGraph;
import es.upm.aedlib.map.Map;
import es.upm.aedlib.set.HashTableMapSet;
import es.upm.aedlib.map.HashTableMap;


public class Suma {
  public static <E> Map<Vertex<Integer>,Integer> sumVertices(DirectedGraph<Integer,E> g) {
    Map<Vertex<Integer>,Integer> mapa = new HashTableMap<Vertex<Integer>,Integer>();
    for(Vertex<Integer> verticeMapa: g.vertices()){
      mapa.put(verticeMapa, sumaV(verticeMapa, g, new HashTableMapSet<>()));
    }
    return mapa;
  }

  public static <E> int sumaV(Vertex<Integer> v, DirectedGraph<Integer,E> g, HashTableMapSet<Vertex<Integer>> visitados){
    int suma = v.element();
    visitados.add(v);
    for(Edge<E> vecino: g.outgoingEdges(v)){
      if(!visitados.contains(g.endVertex(vecino))){
        suma += sumaV(g.endVertex(vecino), g, visitados);
      }
    }
    return suma;
  }
}
