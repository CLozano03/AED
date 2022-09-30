package aed.polinomios;

import java.util.Arrays;
import java.util.function.BiFunction;

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
    throw new RuntimeException("no esta implementado todavia");
  }

  // No se si voy a usarlas pero por si acaso
  // Funcion privada para saber si dos monomios tienen el mismo grado
  private boolean mismoGrado(Monomio m1, Monomio m2) {
    return m1.getExponente() == m2.getExponente();
  }

  private Monomio gradoMayor(Monomio m1, Monomio m2) {
    if (m1.getExponente() > m2.getExponente()) {
      return m1;
    } else {
      return m2;
    }
  }
  
  private Monomio gradoMenor(Monomio m1, Monomio m2) {
    if (m1.getExponente() < m2.getExponente()) {
      return m1;
    } else {
      return m2;
    }
  }
  //Devuelve el coefciente de grado i del polinomio
  private int coefDeGrado (Polinomio p1,int grado) {
    int res = 0;
    int coefPolinomio = terms.first().element().getCoeficiente();
    for (int i = 0; i < p1.grado() && res > 0; i++) {
      if (coefPolinomio == grado) res = grado;
    }
    return res;
  }
  /**
   * Suma dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return la suma de los polinomios.
   */
  public static Polinomio suma(Polinomio p1, Polinomio p2) {
    Polinomio res = new Polinomio();
    // La suma es el propio polinomio modificado o un nuevo Polinomio¿?

    // Tengo que saber cual tiene mayor grado
    if (p1.grado() >= p2.grado()) {
      // Recorro ambos polinomios (ordenados en orden decreciente de Grado)
      for (int i = 0; i < p1.grado(); i++) {
        res.getTerms().addLast(new Monomio (p1.coefDeGrado(p1, i) + p2.coefDeGrado(p2, i), i));
      }
    } else {
      for (int i = 0; i < p2.grado(); i++) {
        res.getTerms().addLast(new Monomio (p1.coefDeGrado(p1, i) + p2.coefDeGrado(p2, i), i));
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
    // El orden importa, primer polinomio - segundo polinomio

    // Tengo que saber cual tiene mayor grado
    if (p1.grado() >= p2.grado()) {
      // Recorro ambos polinomios (ordenados en orden decreciente de Grado)
      for (int i = 0; i < p1.grado(); i++) {
        res.getTerms().addLast(new Monomio (p1.coefDeGrado(p1, i) - p2.coefDeGrado(p2, i), i));
      }
    } else {
      for (int i = 0; i < p2.grado(); i++) {
        res.getTerms().addLast(new Monomio (p1.coefDeGrado(p1, i) - p2.coefDeGrado(p2, i), i));
      }
    }
    
    return res;
  }
    
  /**
   * Calcula el producto de un monomio y un polinomio.
   * @param m el monomio
   * @param p el polinomio
   * @return el producto del monomio y el polinomio
   */
  public static Polinomio multiplica(Polinomio p1, Polinomio p2) {
    Polinomio res = new Polinomio();
    int coef1 = 0;
    int coef2 = 0;
    int coefRes;
    int expoRes;
    int indice = 0;
    // Multiplicamos coeficientes, sumamor exponentes y sumamos al final todo resultado
    for (int i = 0; i < p1.grado(); i++) {
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
        res.getTerms().addLast(new Monomio (coefRes), indice)); //Lista position, no nodeposition
        indice++;
        }
      }
    }  
    // Sumar los que estan desagrupados!!!! 
    return res;
  }

  /**
   * Calcula el producto de dos polinomios.
   * @param p1 primer polinomio.
   * @param p2 segundo polinomio.
   * @return el producto de los polinomios.
   */
  public static Polinomio multiplica(Monomio t, Polinomio p) { //copiado el de arriba, hay que modificar
    Polinomio res = new Polinomio();
  
    int coefRes;
    int expoRes;
    int indice = 0;
    // Multiplicamos coeficientes, sumamor exponentes y sumamos al final todo resultado
    for (int i = 0; i < p.grado(); i++) {
      

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
        res.getTerms().addLast(new Monomio (coefRes), indice));
        indice++;
        }
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
    return 0;
  }

  /**
   * Devuelve el exponente (grado) del monomio con el mayor grado
   * dentro del polinomio
   * @return el grado del polinomio
   */
  public int grado() {
    return -1;
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

  }

  
  public static void main(String[] args) {
    Polinomio p1 = new Polinomio();
    Polinomio p2 = new Polinomio();

    Monomio m1 = new Monomio(3,1);
    Monomio m2 = new Monomio(2,1);
    Monomio m3 = new Monomio(1,2);
    Monomio m4 = new Monomio(5,2);
    Monomio m5 = new Monomio(7,5);

    p1.getTerms().addLast(m1);
    p1.getTerms().addLast(m3);
    p1.getTerms().addLast(m5);
    p2.getTerms().addLast(m2);
    p2.getTerms().addLast(m4);

    System.out.println(m1);

  }
}
