package aed.hotel;

import es.upm.aedlib.indexedlist.*;
import java.util.Comparator;

/**
 * Implementa el interfaz Hotel, para realisar y cancelar reservas en un hotel,
 * y para realisar preguntas sobre reservas en vigor.
 */
public class MiHotel implements Hotel {
  /**
   * Usa esta estructura para guardar las habitaciones creados.
   */
  private IndexedList<Habitacion> habitaciones;

  /**
   * Crea una instancia del hotel.
   */
  public MiHotel() {
    // No se debe cambiar este codigo
    this.habitaciones = new ArrayIndexedList<>();
  }

  @Override
  public void anadirHabitacion(Habitacion habitacion) throws IllegalArgumentException {
    if(habitaciones.get(Integer.parseInt(habitacion.getNombre())) != null){
      throw new IllegalArgumentException();
    } else {
      insertar(habitacion, habitaciones, new CompNHabitacion());
    }
  }

  @Override
  public boolean reservaHabitacion(Reserva reserva) {
    String nombreHabitacion = reserva.getHabitacion();
    Habitacion habitacion = habitaciones.get(Integer.parseInt(nombreHabitacion));
    boolean reservada = false;
    String entrada = reserva.getDiaEntrada();
    String salida = reserva.getDiaSalida();
    // Compruebo que la habitacion existe en MiHotel
    if (habitacion != null) {
      // Antes de reservarla, compruebo que esta disponible en las fechas
      if (habitacionDisponible(habitacion, entrada, salida)) { //funcion auxiliar que comprueba si esta disponible
       insertar(reserva,habitacion.getReservas(), new CompFEntrada()); // cmp no se que poner
       reservada = true;
      }
    } else {
      throw new IllegalArgumentException();
    }
    return reservada;
  }

  private boolean habitacionDisponible(Habitacion hab, String diaEntrada, String diaSalida) {
    //Compruebo si la hab que quiero esta disponible en diaEntrada y diaSalida incluido
    //buscar(hab,disponibilidadHabitaciones(diaEntrada, diaSalida),cmp);
    IndexedList<Habitacion> listaHabDisponibles = new ArrayIndexedList<Habitacion>();
    listaHabDisponibles = disponibilidadHabitaciones(diaEntrada, diaSalida);
    boolean encontrada = false;
    // Busco en la lista mi habitacion, para ver si esta disponible entre esas fechas inclusive
    for (int i = 0; i < listaHabDisponibles.size() && !encontrada; i++) {
      if (listaHabDisponibles.get(i).getNombre() == (hab.getNombre())) {
        encontrada = true;
      }
    }
    return encontrada;
    
    // habitacion.getReserva() que es una lista con reserva que es concreta
    //Tenemos que buscar en la lista de reservas la reserva con la fecha de E/S mas proxima a la de @param reserva
    
    
    /* if(reserva.compareTo(habitacion.getReservas(). get(i)) < 0){
      
    } */
  }



  @Override
  public boolean cancelarReserva(Reserva reserva) {
    int nombreHabitacion = Integer.parseInt(reserva.getHabitacion());
    Habitacion habitacion = habitaciones.get(nombreHabitacion);
    boolean cancelable = false;

    for (int i = 0; i < habitacion.getReservas().size() && !cancelable; i++) {
      cancelable = habitacion.getReservas().get(i).equals(reserva);

      if (cancelable) {
        // borro elemento
        habitaciones.get(nombreHabitacion).getReservas().set(i, null);

      }
    }

    return cancelable;
  }

  @Override
  public IndexedList<Habitacion> disponibilidadHabitaciones(String diaEntrada, String diaSalida) {
    
    Reserva reservaDuda = new Reserva("", "", diaEntrada, diaSalida);
    Reserva punteroReserva = new Reserva("", diaSalida, diaSalida, diaSalida);
    Habitacion punteroHabitacion = new Habitacion("", 0);
    
    //Comparadores usados
    CompES cmpES = new CompES();
    CompFSalida cmpSalida = new CompFSalida();
    CompPrecio cmpPrecio = new CompPrecio();

    IndexedList<Habitacion> habitacionesLibres = new ArrayIndexedList<Habitacion>();

    for(int i  = 0; i < habitaciones.size(); i++){
      
      punteroHabitacion = habitaciones.get(i);
      
      for(int j = 0; j < habitaciones.get(i).getReservas().size(); j++){
        
        punteroReserva = punteroHabitacion.getReservas().get(j);
        
        if(cmpES.compare(punteroReserva, reservaDuda) >= 0){
          insertar(punteroHabitacion, habitacionesLibres, cmpPrecio);
        }
          
      }
    }
    return habitacionesLibres;
  }

  @Override
  public IndexedList<Reserva> reservasPorCliente(String dniPasaporte) {
    IndexedList<Reserva> res = new ArrayIndexedList<Reserva>();

    for (int i = 0; i < habitaciones.size(); i++) {
      for (int j = 0; i < habitaciones.get(i).getReservas().size(); j++) {
        if (habitaciones.get(i).getReservas().get(j).getDniPasaporte().equals(dniPasaporte)) {
          res.add(0, habitaciones.get(i).getReservas().get(j));
        }
      }
    }
    // !!! Hay que ordenar por precios no reservas

    return null;
  }

  @Override
  public IndexedList<Habitacion> habitacionesParaLimpiar(String hoyDia) {
    // Ordenados por nombre de hab
    IndexedList<Habitacion> aLimpiar = new ArrayIndexedList<Habitacion>();
    
    for (int i = 0; i < habitaciones.size(); i++) {
      //Hay que implementar la 
      /* if(){
        insertar(habitaciones.get(i), aLimpiar, new CompNHabitacion());
      } */
    }

    return aLimpiar;
  }

  @Override
  public Reserva reservaDeHabitacion(String nombreHabitacion, String dia) throws IllegalArgumentException {

    if (habitaciones.get(Integer.parseInt(nombreHabitacion)) == null) {

      throw new IllegalArgumentException("Habitacion inexistente");

    } else {

      Reserva reserva = new Reserva(nombreHabitacion, "", dia, "");
      boolean encontrado = false;
      Habitacion habitacion = habitaciones.get(Integer.parseInt(nombreHabitacion));

      for (int i = 0; i < habitacion.getReservas().size() && !encontrado; i++) {
        if (habitacion.getReservas().get(i).compareTo(reserva) <= 0) {
          reserva = habitacion.getReservas().get(i);
          encontrado = true;
        }
      }
      if(encontrado){
        return reserva;
      } else {
        return null;
      }
    }
  }

  
  /**
   * Metodo auxiliar generico para insertar un elemento en una lista atendiendo al
   * orden especificado por @param cmp 
   */
  private static <E> void insertar(E e, IndexedList<E> l, Comparator<E> cmp) {
    int pos = (int)((l.size())/2);
    boolean stop = false;
      if(l.size() == 0){
        l.add(0, e);
      } else {
        while(!stop){
          if ((pos != 0 /* && pos != l.size() */ && cmp.compare(l.get(pos - 1), e) <= 0) && cmp.compare(l.get(pos), e) >= 0){
            //annado el elemento
            stop = true;
            l.add(pos, e);  
          } else if (cmp.compare(l.get(pos), e) > 0){
            //ajuste cuando hay que aniadir en primera pos
            if(pos == 0){
              l.add(pos, e);
              stop = true;
            } else {
              pos = (int)(pos/2);
            }
          } else {
            //ajuste cuando hay que aniadir en ultima
            if(pos == l.size()-1){
              l.add(pos+1, e);
              stop = true;
            } else{
              pos = l.size() - (int)(l.size() - pos)/2;
            }
            
          }
        }
      }
          
  }

  
  /* private static <E> int buscar(E e, IndexedList<E> l, Comparator<E> cmp){
    int pos = (int)((l.size())/2);
    boolean stop = false;
    
      if(l.size() == 0){
        stop = true;
      } else {
        while(!stop){
          if ((pos != 0 && cmp.compare(l.get(pos - 1), e) <= 0) && cmp.compare(l.get(pos), e) >= 0){
            //annado el elemento
            stop = true;
            l.add(pos, e);  
          } else if (cmp.compare(l.get(pos), e) > 0){
            //ajuste cuando hay que aniadir en primera pos
            if(pos == 0){
              stop = true;
            } else {
              pos = (int)(pos/2);
            }
          } else {
            //ajuste cuando hay que aniadir en ultima
            if(pos == l.size()-1){
              l.add(pos+1, e);
              stop = true;
            } else{
              pos = l.size() - (int)(l.size() - pos)/2;
            }
            
          }
        }
      }
    
    
    
    
    
    
    
    /* int pos = (int)l.size()/2;
    boolean encontrado = false;
    E el = null;
    
      while(!encontrado && (pos !=0 || pos != l.size())){
        if (cmp.compare(l.get(pos), e) == 0) {
          encontrado = true;
          el = l.get(pos);
        } else if (cmp.compare(l.get(pos), e) > 0){
          pos = (int)(pos/2);
        } else {
          pos = l.size() - (l.size() -pos)/2;
        }
      }
      return el; 
  } */
  // --------------------------------------------------------------------------------------------
  /*
   * Dentro de la clase MiHotel creamos una clase static comparator que nos va a
   * ayudar a ordenar las listas
   * Vamos a crear tantas clases static como necesitemos
   * 
   * !!(Para comparar fecha de salida tambieen, no podemos cambiar el archivo Reserva.java)
   * Borrar este comentario en un futuro no muy lejano
   */

  static class CompFEntrada implements Comparator<Reserva> {

    public CompFEntrada(){}
    
    @Override
    public int compare(Reserva o1, Reserva o2) {
      return o1.getDiaEntrada().compareTo(o2.getDiaEntrada());
    }
    
  }

  static class CompFSalida implements Comparator<Reserva> {
    public CompFSalida(){}
    @Override
    public int compare(Reserva o1, Reserva o2) {
      return o1.getDiaSalida().compareTo(o2.getDiaSalida());
    }

  }
  static class CompES implements Comparator<Reserva> {
    public CompES(){}
    @Override
    public int compare(Reserva o1, Reserva o2) {
      return o1.getDiaEntrada().compareTo(o2.getDiaSalida());
    }

  }

  static class CompNHabitacion implements Comparator<Habitacion> {
    public CompNHabitacion(){}

    @Override
    public int compare(Habitacion o1, Habitacion o2) {
        if(Integer.parseInt(o1.getNombre())> Integer.parseInt(o2.getNombre())){
          return 1;
        } else if (Integer.parseInt(o1.getNombre()) < Integer.parseInt(o2.getNombre())){
          return -1;
        } else {
          return 0;
        }
    }

  }
  static class CompPrecio implements Comparator<Habitacion> {
    public CompPrecio(){}

    @Override
    public int compare(Habitacion o1, Habitacion o2) {
      if(o1.getPrecio() > o2.getPrecio()){
        return 1;
      } else if (o1.getPrecio() < o2.getPrecio()){
        return -1;
      } else {
        return 0;
      }
    }

  }
  
  //Mini tester para insertar
  public static void main(String[] args){
    //Test insertar method
    IndexedList<Habitacion> l = new ArrayIndexedList<Habitacion>();
    l.add(0, new Habitacion("1", 5));
    l.add(1, new Habitacion("5", 5));  

    insertar(new Habitacion("0", 5), l, new CompNHabitacion());
    insertar(new Habitacion("6", 5), l, new CompNHabitacion());

    insertar(new Habitacion("5", 5), l, new CompNHabitacion());
    insertar(new Habitacion("7", 5), l, new CompNHabitacion());

    insertar(new Habitacion("4", 5), l, new CompNHabitacion());

    insertar(new Habitacion("78", 5), l, new CompNHabitacion());

    //print list
    for (int i = 0; i < l.size(); i++) {
      System.out.println(l.get(i).getNombre());
    }
  }
}