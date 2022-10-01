package aed.polinomios;

/**
 * @author Cesar Lozano Argueso
 * @author Victoria Fernandez Alegria
 */
 
import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;

/**
 * Operaciones sobre polinomios de una variable con coeficientes enteros.
 */
public class Polinomio {

  // Una lista de monomios
  PositionList<Monomio> terms; 

  /**
   * Crea el polinomio "0".
   */
  public Polinomio() {
    terms = new NodePositionList<>();
  }

  public PositionList<Monomio> getTerms() {
    return terms;
  }
  
  /**
   * Crea un polinomio definado por una lista con monomios.
   * @param terms una lista de monomios
   */
  public Polinomio(PositionList<Monomio> terms) {
    this.terms = terms;
  }

  /**
   * Crea un polinomio definado por un String.
   * La representación del polinomio es una secuencia de monomios separados
   * por '+' (y posiblemente con caracteres blancos).
   * Un monomio esta compuesto por tres partes,
   * el coefficiente (un entero), el caracter 'x' (el variable), y el exponente
   * compuesto por un un caracter '^' seguido por un entero.
   * Se puede omitir multiples partes de un monomio, 
   * ejemplos:
   * <pre>
   * {@code
   * new Polinomio("2x^3 + 9");
   * new Polinomio("2x^3 + -9");
   * new Polinomio("x");   // == 1x^1
   * new Polinomio("5");   // == 5x^0
   * new Polinomio("8x");  // == 8x^1
   * new Polinomio("0");   // == 0x^0
   * }
   * </pre>
   * @throws IllegalArgumentException si el argumento es malformado
   * @param polinomio - una secuencia de monomios separados por '+'
   */
  public Polinomio(String polinomio) {
    String[] monomios = polinomio.split("\\+"); // separa por monomios
    String[] par = new String[2];

    terms = new NodePositionList<>();
    
    for (String monomio : monomios) { 
      monomio = monomio.trim();
      Monomio mon = new Monomio(0, 0); 
      /*
       * Casos especiales que hay que cubrir:
       * "xA".split("x") => ["", "A"]
       * "Ax".split("x") => ["A"]
       * "x".split("x") => []
       * "abc".split("x") => ["abc"] 
      */
      if (!monomio.contains("x")){
        mon.setLeft(Integer.parseInt(monomio));
        mon.setRight(0);
      } else {
        par = monomio.split("x"); 
        if(par.length == 0){
          mon.setLeft(1);
          mon.setRight(1);
        } else {
          mon.setLeft(par[0].equals("")? 1 : Integer.parseInt(par[0]));
          mon.setRight(par.length == 1? 1: Integer.parseInt(par[1].split("\\^")[1]));
        }
      }
      if(mon.getCoeficiente() != 0){
        terms.addLast(mon); 
      }
    }
  }
  
  //Devuelve el coeficiente de grado i del polinomio p1. Devuelve 0 si no existe
  private static int coefDeGrado (Polinomio p1,int deGrado) {
    Position<Monomio> cursor = p1.getTerms().first();
  
    if (p1.grado() >= deGrado) {
      while(cursor != null) { //cond de curor apunta a null ?
        if (cursor.element().getExponente() == deGrado) {
          return cursor.element().getCoeficiente();
        }
        cursor = p1.getTerms().next(cursor);
      }
    }
    return 0;
  }

  // Devuelve si existe un monomio de grado deGrado en p1
  private static boolean existeExp (Polinomio p1,int deGrado) {
    return coefDeGrado(p1, deGrado) != 0;
  }

  /**
   * Suma dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return la suma de los polinomios.
   */
  public static Polinomio suma(Polinomio p1, Polinomio p2) {
    Polinomio res = new Polinomio();
    Monomio m1;
  
    // Recorro ambos polinomios (ordenados en orden decreciente de Grado)
    for(int i = 0; i <= Math.max(p1.grado(), p2.grado()); i++) {
      if(existeExp(p1, i) || existeExp(p2, i)) {
        m1 = new Monomio(coefDeGrado(p1, i) + coefDeGrado(p2, i), i);
        res.getTerms().addFirst(m1);
      }
    }
    return res;
  } 
    
/*   *
   * Substraccion de dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return la resta de los polinomios.
   */
  public static Polinomio resta(Polinomio p1, Polinomio p2) {
    Polinomio res = new Polinomio();
    Monomio m1;
    
    // Recorro ambos polinomios (ordenados en orden decreciente de Grado)
    for(int i = 0; i <= Math.max(p1.grado(), p2.grado()); i++) {
      if(existeExp(p1, i) || existeExp(p2, i)) {
        m1 = new Monomio(coefDeGrado(p1, i) - coefDeGrado(p2, i), i);
        if(m1.getCoeficiente() != 0) {
          res.getTerms().addFirst(m1);
        }
      }
    }
    return res;
  }
    
  /**
   * Calcula el producto de dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return el producto de los polinomios.
   */
  public static Polinomio multiplica(Polinomio p1, Polinomio p2) {
    Polinomio res = new Polinomio();
    Position<Monomio> cursor1 = p1.getTerms().first();
    
    while(cursor1 != null){
      // Multiplicamos los monomios de p1 por p2 y luego los sumamos para simplificar
      res = suma(res, multiplica(cursor1.element(), p2));
      cursor1 = p1.terms.next(cursor1);
    }
    return new Polinomio(res.toString());
  }  
  
  /**
   * Calcula el producto de un monomio y un polinomio.
   * @param m el monomio
   * @param p el polinomio
   * @return el producto del monomio y el polinomio
   */
  public static Polinomio multiplica(Monomio t, Polinomio p) {
    Polinomio res = new Polinomio();
    Monomio m;
    Position<Monomio> cursor = p.getTerms().first();
    while(cursor != null) {
        // Multiplicamos t por el monomio al que apunta cursor (los exponentes se suman)
        m = new Monomio (cursor.element().getCoeficiente()* t.getCoeficiente(), cursor.element().getExponente() + t.getExponente()); 
        // Cuando no es 0 lo annadimos a polinomio
        if(m.getCoeficiente() != 0 ){
          res.getTerms().addLast(m);
        }
        cursor = p.getTerms().next(cursor);
      }
    return res; 
  }     
    
  /**
   * Devuelve el valor del polinomio cuando su variable es sustiuida por un valor concreto.
   * Si el polinomio es vacio (la representacion del polinomio "0") entonces
   * el valor devuelto debe ser 0.
   * @param valor el valor asignado a la variable del polinomio
   * @return el valor del polinomio para ese valor de la variable.
   */
  public long evaluar(int valor) {
    int imagen  = 0;
    Position<Monomio> puntero = terms.first();
    Monomio aux;

    // Recorro la lista de monomios hasta que no haya mas
    while(puntero != null){
      aux = puntero.element();
      // Voy sumando al resultado el monomio sustituido por valor
      imagen += aux.getCoeficiente() * Math.pow(valor, aux.getExponente());
      puntero = terms.next(puntero);
    }
    return imagen;
  }

  /**
   * Devuelve el exponente (grado) del monomio con el mayor grado
   * dentro del polinomio
   * @return el grado del polinomio
   */
  public int grado() {
    return terms.isEmpty()? -1 : terms.first().element().getExponente();
  }

  @Override
  public String toString() {
    if (terms.isEmpty()) return "0";
    else {
      StringBuffer buf = new StringBuffer();
      Position<Monomio> cursor = terms.first();
      while (cursor != null) {
        Monomio p = cursor.element();
        int coef = p.getCoeficiente();
        int exp = p.getExponente();
        buf.append(coef);
        if (exp > 0) {
          buf.append("x");
          buf.append("^");
          buf.append(exp);
        }
        cursor = terms.next(cursor);
        if (cursor != null) buf.append(" + ");
      }
      return buf.toString();
    }
  }
  
  // Comprueba si dos polinomios son iguales
  public boolean equals(Object o) {
    if (!(o instanceof Polinomio)) return false;
    else {
      Polinomio pol = (Polinomio) o;

      Position<Monomio> cursor = terms.first();
      Position<Monomio> cursor2 = pol.getTerms().first();

      boolean iguales = true; 
      
      // Comprobamos el tamanno
      if (terms.size() == pol.getTerms().size()) {
        
        while(cursor != null && iguales) {
          // Comprobamos si los monomomios son iguales
          iguales = cursor.element().equals(cursor2.element());

          // Seguimos iterando por la lista
          cursor = terms.next(cursor);
          cursor2 = pol.getTerms().next(cursor2);
        }
      } else {
        iguales = false;
      }
      return iguales;      
    }
  }
}
