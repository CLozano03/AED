package aed.recursion;


import es.upm.aedlib.Pair;
import es.upm.aedlib.positionlist.*;


public class Explorador {
  
  public static Pair<Object,PositionList<Lugar>> explora(Lugar inicialLugar) {
    return exploraAux(inicialLugar, new NodePositionList<Lugar>());
  }

  public static Pair<Object,PositionList<Lugar>> exploraAux(Lugar inicialLugar, NodePositionList<Lugar> camino){
    if(inicialLugar == null) {
      return new Pair<Object,PositionList<Lugar>>(null, camino);
    } 
    
    if(inicialLugar.tieneTesoro()){
      camino.addLast(inicialLugar);
      return new Pair<Object,PositionList<Lugar>>(inicialLugar.getTesoro(), camino);
    } else {
      inicialLugar.marcaSueloConTiza();
      for(Lugar lugar: inicialLugar.caminos()){
        if(!lugar.sueloMarcadoConTiza()){
          camino.addLast(inicialLugar);
            return exploraAux(lugar, camino);
          }
      }
      return exploraAux(camino.remove(camino.last()), camino);
    }
  }
}