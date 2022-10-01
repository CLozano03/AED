package aed.polinomios;



import es.upm.aedlib.Position;
import es.upm.aedlib.positionlist.*;
//import es.upm.aedlib.Pair;
 
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
      if(!monomio.contains("x")){
        mon.setLeft(Integer.parseInt(monomio));
        mon.setRight(0);
      } else{
        par = monomio.split("x"); // "x".split("x") => []
        if(par.length == 0){
          mon.setLeft(1);
          mon.setRight(1);
        } else {
          mon.setLeft(par[0] == ""? 1 : Integer.parseInt(par[0]));
          mon.setRight(par.length == 1? 1: Integer.parseInt(par[1].split("\\^")[1]));
        }
      }
      if(mon.getCoeficiente() != 0){
        terms.addLast(mon); 
      }
    }
  }
  
  //Devuelve el coefciente de grado i del polinomio. Devuelve 0 si no existe
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

    Position<Monomio> cursor1 = p1.getTerms().first();
    
    while(cursor1 != null){
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
        m = new Monomio (cursor.element().getCoeficiente()* t.getCoeficiente(), cursor.element().getExponente() + t.getExponente()); 
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
    Polinomio p2 = new Polinomio();//  5x ^2 + 2x^1
    Polinomio p3 = new Polinomio();

    Monomio m1 = new Monomio(3,0); //3x^0
    Monomio m2 = new Monomio(2,1); //2x^1
    Monomio m3 = new Monomio(1,2); //1x^2
    Monomio m4 = new Monomio(5,2); //5x^2
    Monomio m5 = new Monomio(4,3); //4x^3

    p1.getTerms().addLast(m5);
    p1.getTerms().addLast(m3);
    p1.getTerms().addLast(m1);
    p2.getTerms().addLast(m4);
    p2.getTerms().addLast(m2);
    p3.getTerms().addLast(m4);
    p3.getTerms().addLast(m2);

    /* System.out.println(m1); */

    //To string
    /* System.out.println(p1.toString());
    System.out.println(p2.toString()); */
    //System.out.println(p3.toString());
  
    // Testeando funcion grado
   /*  System.out.println("Grado de p1: " + p1.grado());
    System.out.println("Grado de p2: " + p2.grado()); */

    // Testeando funcion evaluar
    //System.out.println("Evaluando p1 en -1: " + p1.evaluar(-1));
    //System.out.println("Evaluando p2 en 2: " + p2.evaluar(2));

    //Testeando equals
    /* System.out.println(p1.equals(p2));//false
    System.out.println(p2.equals(p3));//true
    System.out.println(p2.equals(m1));//false */

    /* System.out.println(suma(p1,p2).toString());
    System.out.println(resta(p1,p2).toString());
 */
    /* System.out.println(p1.coefDeGrado(p1,3));////)
    System.out.println(p1.coefDeGrado(p1,2)); 
    System.out.println(p1.coefDeGrado(p1,0));  */

    //Testeando constructor polinomio a partir de un string
    Polinomio p4 = new Polinomio("0");
    System.out.println(p4.toString());
    
    
    //System.out.println(multiplica(p2, p1).toString());


  }
}
