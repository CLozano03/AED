package aed.polinomios;

import java.util.Arrays;
import java.util.function.BiFunction;

import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;
 
//ctrl k --> z
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
    throw new RuntimeException("no esta implementado todavia");
  }

  // No se si voy a usarlas pero por si acaso
  // Funcion privada para saber si dos monomios tienen el mismo grado
  private boolean mismoGrado(Monomio m1, Monomio m2) {
    return m1.getExponente() == m2.getExponente();
  }
  
  //Devuelve el coefciente de grado i del polinomio. Devuelve 0 si no existe
  private static int coefDeGrado (Polinomio p1,int deGrado) {
    int expoPolinomio;
    Position<Monomio> cursor = p1.getTerms().first();
    int gradoMayorPol = p1.grado();
  
    if (gradoMayorPol >= deGrado) {
      for (int i = 0; i < p1.grado(); i++) { //cond de curor apunta a null ?
        expoPolinomio = cursor.element().getExponente();
        if (expoPolinomio == deGrado) {
          return cursor.element().getCoeficiente();
        }
        cursor = p1.getTerms().next(cursor);
      }
    }
    
    return 0;
  }

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

    Monomio m1 = p1.terms.first().element();
    
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

    Monomio m1 = p1.terms.first().element();
    
    // Recorro ambos polinomios (ordenados en orden decreciente de Grado)
    for(int i = 0; i <= Math.max(p1.grado(), p2.grado()); i++) {
      if(existeExp(p1, i) || existeExp(p2, i)) {
        m1 = new Monomio(coefDeGrado(p1, i) - coefDeGrado(p2, i), i);
        res.getTerms().addFirst(m1);
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
    Monomio cursor1 = p1.terms.first().element();
    while(cursor1 != null){
      /* res.addFirst(multiplicaMonomio(cursor1, p2));
      cursor1 = p1.terms.next(cursor1); */
    }
    // Multiplicamos coeficientes, sumamor exponentes y sumamos al final todo resultado
    /* for (int i = 0; i < p1.grado(); i++) {
      coef1 = p1.coefDeGrado(p1, i);

      for (int j = 0; j < p2.grado(); j++) {
        // Calculo coeficientes
        coef2 = p2.coefDeGrado(p2, j);
        if (coef1 != 0 && coef2 == 0) {
          coefRes = coef1;
          expoRes = p1.terms.first().element().getExponente();
        } else if (coef1 == 0 && coef2 != 0){
          coefRes = coef2;
          expoRes = p2.terms.first().element().getExponente();
        } else  {
          coefRes = coef1 * coef2;
          expoRes = p1.terms.first().element().getExponente() 
                  + p2.terms.first().element().getExponente();
          // Hacer tema next cursor!!!!
          
        }
        //res.getTerms().addLast(new Monomio (coefRes), indice)); //Lista position, no nodeposition
        indice++;
      } 
    } */
    return res;
  }  
  
  /**
   * Calcula el producto de un monomio y un polinomio.
   * @param m el monomio
   * @param p el polinomio
   * @return el producto del monomio y el polinomio
   */
  public static Polinomio multiplica(Monomio t, Polinomio p) { //copiado el de arriba, hay que modificar
    Polinomio res = new Polinomio();

    for (int i = 0; i < p.grado(); i++){
      if(existeExp(p, i)){ 
        res.getTerms().addFirst(new Monomio (coefDeGrado(p, i) * t.getExponente(), i + t.getExponente())); 
      }
    }
    return res; 
  }     
    
  /**
   * Devuelve el valor del polinomio cuando su variable es sustiuida por un valor concreto.
   * Si el polinomio es vacio (la representacion del polinomio "0") entonces
   * el valor devuelto debe ser -1.
   * @param valor el valor asignado a la variable del polinomio
   * @return el valor del polinomio para ese valor de la variable.
   */
  public long evaluar(int valor) {
    int imagen  = -1;
    Position<Monomio> puntero = terms.first();
    Monomio aux;

    // Recorro la lista de monomios hasta que no haya mas
    while(puntero != null) {
      aux = puntero.element();  // Monomio
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
        buf.append(new Integer(coef).toString());
        if (exp > 0) {
          buf.append("x");
          buf.append("^");
          buf.append(new Integer(exp)).toString();
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
      Position<Monomio> cursor = terms.first();

      Monomio aux1;
      Monomio aux2;
      Polinomio pol = (Polinomio) o;
      Position<Monomio> cursor2 = pol.getTerms().first();
      boolean iguales = true;
      if (terms.size() == pol.getTerms().size()) {
        while(cursor != null && iguales) {
          aux1 = cursor.element();
          aux2 = cursor2.element();
  
          cursor = terms.next(cursor);
          cursor2 = ((Polinomio)o).getTerms().next(cursor2);
  
          iguales = aux1.equals(aux2);
        } 
      } else {
          iguales = false;
      }
      return iguales;      
    }
  }

  
  public static void main(String[] args) {
    Polinomio p1 = new Polinomio();  // 4x^3 + x^2 + 3x^0  ---> evaluar 2 --> 4*2^3 + 1*2^2 + 3*2^1 = 39
    Polinomio p2 = new Polinomio();// 
    Polinomio p3 = new Polinomio();

    Monomio m1 = new Monomio(3,0);
    Monomio m2 = new Monomio(2,1);
    Monomio m3 = new Monomio(1,2);
    Monomio m4 = new Monomio(5,2);
    Monomio m5 = new Monomio(4,3);

    p1.getTerms().addLast(m5);
    p1.getTerms().addLast(m3);
    p1.getTerms().addLast(m1);
    p2.getTerms().addLast(m4);
    p2.getTerms().addLast(m2);
    p3.getTerms().addLast(m4);
    p3.getTerms().addLast(m2);

    /* System.out.println(m1); */

    //To string
    System.out.println(p1.toString());
    System.out.println(p2.toString());
    //System.out.println(p3.toString());
  
    // Testeando funcion grado
   /*  System.out.println("Grado de p1: " + p1.grado());
    System.out.println("Grado de p2: " + p2.grado()); */

    // Testeando funcion evaluar
    /* System.out.println("Evaluando p1 en 2: " + p1.evaluar(2)); */
    //System.out.println("Evaluando p2 en 2: " + p2.evaluar(2));

    //Testeando equals
    /* System.out.println(p1.equals(p2));//false
    System.out.println(p2.equals(p3));//true
    System.out.println(p2.equals(m1));//false */

    System.out.println(suma(p1,p2).toString());
    System.out.println(resta(p1,p2).toString());

    /* System.out.println(p1.coefDeGrado(p1,3));////)
    System.out.println(p1.coefDeGrado(p1,2)); 
    System.out.println(p1.coefDeGrado(p1,0));  */

  }
}
