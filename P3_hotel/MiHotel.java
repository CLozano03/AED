package aed.hotel;

import es.upm.aedlib.indexedlist.*;
import java.util.Comparator;

public class MiHotel implements Hotel {
  
  private IndexedList<Habitacion> habitaciones;

  public IndexedList<Habitacion> getHabitaciones() {
    return this.habitaciones;
  }

  public void setHabitaciones(IndexedList<Habitacion> habitaciones) {
    this.habitaciones = habitaciones;
  }

  /**
   * Crea una instancia del hotel.
   */
  public MiHotel() {
    // No se debe cambiar este codigo
    this.habitaciones = new ArrayIndexedList<>();
  }

  @Override
  public void anadirHabitacion(Habitacion habitacion) throws IllegalArgumentException {
    if (busquedaBinaria(habitacion, habitaciones, new CompNHabitacion()) != -1) {
      throw new IllegalArgumentException("La habitacion existe");
    } else {
      insertar(habitacion, habitaciones, new CompNHabitacion());
    }
  }

  @Override
  public boolean reservaHabitacion(Reserva reserva) throws IllegalArgumentException {
    
    Habitacion habitacionAux = new Habitacion(reserva.getHabitacion(), 0);
    boolean reservada = false;

    //compES
    //CompES cmpES = new CompES();
    
    // Compruebo que la habitacion existe en MiHotel
    if (busquedaBinaria(habitacionAux, habitaciones, new CompNHabitacion()) == -1) {
      throw new IllegalArgumentException();
    } else {
      Habitacion habitacion = habitaciones.get(busquedaBinaria(habitacionAux, habitaciones, new CompNHabitacion()));
     
      // Antes de reservarla, compruebo que esta disponible en las fechas
      if (habitacionDisponible(habitacion, reserva.getDiaEntrada(), reserva.getDiaSalida())) { // funcion auxiliar que
                                                                                               // comprueba si esta
                                                                                               // disponible
        insertar(reserva, habitacion.getReservas(), new CompFEntrada());
        reservada = true;
      }

      //Comprobamos si no entran en conflicto con las reservas ya existentes
      /* for(int i = 0; i < habitacion.getReservas().size() && !reservada; i++) {
        if(cmpES.compare(habitacion.getReservas().get(i), reserva) >= 0 || cmpES.compare(reserva, habitacion.getReservas().get(i)) >= 0){
          insertar(reserva, habitacion.getReservas(), new CompFEntrada());
          reservada = true;
        }
      } */
    }

    return reservada;
  }

  private boolean habitacionDisponible(Habitacion hab, String diaEntrada, String diaSalida) {
    // Compruebo si la hab que quiero esta disponible en diaEntrada y diaSalida
    // incluido
    // buscar(hab,disponibilidadHabitaciones(diaEntrada, diaSalida),cmp);
    IndexedList<Habitacion> listaHabDisponibles = disponibilidadHabitaciones(diaEntrada, diaSalida);
   // listaHabDisponibles = disponibilidadHabitaciones(diaEntrada, diaSalida);
    boolean encontrada = false;
    // Busco en la lista mi habitacion, para ver si esta disponible entre esas
    // fechas inclusive
    for (int i = 0; i < listaHabDisponibles.size() && !encontrada; i++) {
      if (listaHabDisponibles.get(i).getNombre().equals(hab.getNombre())) {
        encontrada = true;
      }
    }
    return encontrada;
  }

  @Override
  public boolean cancelarReserva(Reserva reserva) {
	  
    Habitacion hab = new Habitacion(reserva.getHabitacion(),0);
	  int indice = busquedaBinaria(hab, habitaciones, new CompNHabitacion());
    
    boolean cancelable = false;

    // Compruebo que la habitacion existe en MiHotel
    if(indice == -1){
      throw new IllegalArgumentException("La habitacion no existe en el hotel");
    } else {
    	hab = habitaciones.get(indice);

      for (int i = 0; i < hab.getReservas().size() && !cancelable; i++) {
        cancelable = hab.getReservas().get(i).equals(reserva);
        if (cancelable) {
            // borro elemento
            hab.getReservas().removeElementAt(i);
          }
      }   
      
    }
    return cancelable;
  }

  @Override
  public IndexedList<Habitacion> disponibilidadHabitaciones(String diaEntrada, String diaSalida) {

    Reserva posibleReserva = new Reserva("", "", diaEntrada, diaSalida);

    boolean libre;
    
    // Comparadores usados
    CompES cmpES = new CompES();
    CompPrecio cmpPrecio = new CompPrecio();

    // Lista de habitaciones disponibles a devolver
    IndexedList<Habitacion> habitacionesLibres = new ArrayIndexedList<Habitacion>();

    for (int i = 0; i < habitaciones.size(); i++) {

      Habitacion punteroHabitacion = habitaciones.get(i);
      libre = true;

      if(punteroHabitacion.getReservas().size() == 0){
        
        insertar(punteroHabitacion, habitacionesLibres, cmpPrecio);
        libre = false; //Para que no la aniada dos veces al salir del bucle de reservas

      } else {
        
        for (int j = 0; j < punteroHabitacion.getReservas().size() && libre; j++) {
          //Si entran las fechas en conflicto con alguna reserva, no esta libre y se sale del bucle
          if (!(cmpES.compare(punteroHabitacion.getReservas().get(j), posibleReserva) >= 0 || cmpES.compare(posibleReserva, punteroHabitacion.getReservas().get(j)) >= 0)) {
            libre = false;
          } 
        }
      }

      //La aniado en caso de que no haya entrado en conflicto
      if(libre){
        insertar(punteroHabitacion, habitacionesLibres, cmpPrecio);
      }

    }
    return habitacionesLibres;
  }

  @Override
  public IndexedList<Reserva> reservasPorCliente(String dniPasaporte) {

    //Punteros
    Habitacion punteroHabitacion;
    Reserva punteroReserva;

    IndexedList<Reserva> reservas = new ArrayIndexedList<Reserva>();

    for (int i = 0; i < habitaciones.size(); i++) {
      punteroHabitacion = habitaciones.get(i);
      for (int j = 0; j < punteroHabitacion.getReservas().size(); j++) {
        punteroReserva = punteroHabitacion.getReservas().get(j);

        if (punteroReserva.getDniPasaporte().equals(dniPasaporte)) {
          insertar(punteroReserva, reservas, new CompFEntrada());
        }
      }
    }
    return reservas;
  }

  @Override
  public IndexedList<Habitacion> habitacionesParaLimpiar(String hoyDia) {
    // Creo instancia de reserva para usar con el comparator
    Reserva hoyDiaReserva = new Reserva("", "", hoyDia, hoyDia);

    // lista a devolver
    IndexedList<Habitacion> aLimpiar = new ArrayIndexedList<Habitacion>();

    //variable que para de recorrer la lista de reservas cuando encuentra una
    boolean encontrado;

    // Comparadores a usar
    CompNHabitacion cmpNHabitacion = new CompNHabitacion();
    CompFEntrada cmpFEntrada = new CompFEntrada();
    CompFSalida cmpFSalida = new CompFSalida();

    for (int i = 0; i < habitaciones.size(); i++) {
      Habitacion punteroHabitacion = habitaciones.get(i);
      encontrado = false;

      for (int j = 0; j < punteroHabitacion.getReservas().size() && !encontrado; j++) {

        Reserva punteroReserva = punteroHabitacion.getReservas().get(j);

        // entrada antes que hoyDia y salida despues o igual a hoyDia.
        if (cmpFEntrada.compare(punteroReserva, hoyDiaReserva) < 0
            && cmpFSalida.compare(punteroReserva, hoyDiaReserva) >= 0) {
          insertar(punteroHabitacion, aLimpiar, cmpNHabitacion);
          encontrado = true;

        }
      }
    }
    return aLimpiar;
  }

  @Override
  public Reserva reservaDeHabitacion(String nombreHabitacion, String dia) throws IllegalArgumentException {

    Habitacion habitacion = new Habitacion(nombreHabitacion,0);
    int indice = busquedaBinaria(habitacion, habitaciones, new CompNHabitacion());
    Reserva reservaAux = new Reserva("", "", dia, "");

    //CmpEs para ver que la fecha dada esta dentro de la reserva
    CompES cmpES = new CompES();

    // Compruebo que la habitacion existe en MiHotel
    if (indice == -1) {
      throw new IllegalArgumentException("Habitacion inexistente");
    } else {
      habitacion = habitaciones.get(indice);
      
      Reserva reserva  = new Reserva("","", "", "");
      boolean encontrado = false;

      //Es necesario recorrer la lista hasta el final para que devuelva la ultima reserva
      for (int i = 0; i < habitacion.getReservas().size() &&  !encontrado; i++) {
        reserva = habitacion.getReservas().get(i);

        if (habitacion.getReservas().get(i).compareTo(reservaAux) <= 0 && cmpES.compare(reservaAux, habitacion.getReservas().get(i)) < 0) {
          encontrado = true;
        }
      }
      if (encontrado) {
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
    int pos = (int) ((l.size()) / 2);
    boolean stop = false;
    if (l.size() == 0) {
      l.add(0, e);
    } else if(busquedaBinaria(e, l, cmp) != -1){
      throw new IllegalArgumentException("Ya existe esta habitacion");
    }else {
      while (!stop) {
        if ((pos != 0 /* && pos != l.size() */ && cmp.compare(l.get(pos - 1), e) <= 0)
            && cmp.compare(l.get(pos), e) >= 0) {
          // annado el elemento
          stop = true;
          l.add(pos, e);
        } else if (cmp.compare(l.get(pos), e) > 0) {
          // ajuste cuando hay que aniadir en primera pos
          if (pos == 0) {
            l.add(pos, e);
            stop = true;
          } else {
            pos = (int) (pos / 2);
          }
        } else {
          // ajuste cuando hay que aniadir en ultima
          if (pos == l.size() - 1) {
            l.add(pos + 1, e);
            stop = true;
          } else {
            pos = l.size() - (int) (l.size() - pos) / 2;
          }

        }
      }
    }

  }

  private static <E> int busquedaBinaria(E h, IndexedList<E> l, Comparator <E> cmp){
    int min = 0;
    int max = l.size() - 1;
    int medio;

    while (min <= max) {
      medio = (int) ((min + max) / 2);
      if (cmp.compare(h, l.get(medio)) == 0) {
        return medio;
      } else if (cmp.compare(l.get(medio), h) > 0){ // si nombreHab es mayor que nombreHabdePosicionMedio
        max = medio - 1;
      } else {
        min = medio + 1;
      }
    }
    return -1;
	}

  // --------------------------------------------------------------------------------------------
  // ---------------------------------- COMPARATORS ---------------------------------------------
  // --------------------------------------------------------------------------------------------

  static class CompFEntrada implements Comparator<Reserva> {

    public CompFEntrada() {
    }

    @Override
    public int compare(Reserva o1, Reserva o2) {
      return o1.getDiaEntrada().compareTo(o2.getDiaEntrada());
    }
  }

  static class CompFSalida implements Comparator<Reserva> {
    public CompFSalida() {
    }

    @Override
    public int compare(Reserva o1, Reserva o2) {
      return o1.getDiaSalida().compareTo(o2.getDiaSalida());
    }
  }

  static class CompES implements Comparator<Reserva> {
    public CompES() {
    }

    @Override
    public int compare(Reserva o1, Reserva o2) {
      return o1.getDiaEntrada().compareTo(o2.getDiaSalida());
    }
  }

  static class CompNHabitacion implements Comparator<Habitacion> {
    public CompNHabitacion() {
    }

    @Override
    public int compare(Habitacion o1, Habitacion o2) {
      if (Integer.parseInt(o1.getNombre()) > Integer.parseInt(o2.getNombre())) {
        return 1;
      } else if (Integer.parseInt(o1.getNombre()) < Integer.parseInt(o2.getNombre())) {
        return -1;
      } else {
        return 0;
      }
    }
  }

  static class CompPrecio implements Comparator<Habitacion> {
    public CompPrecio() {
    }

    @Override
    public int compare(Habitacion o1, Habitacion o2) {
      return o1.getPrecio() - o2.getPrecio();
    }
  }

  //TESTER
  public static void main(String[] args){

    MiHotel h = new   MiHotel();
    
    //Crear habitaciones
    Habitacion h1 = new Habitacion("0", 1);
    Habitacion h2 = new Habitacion("1", 2);
    
    //Crear fechas
    String fecha1 = "2022-01-04";
    String fecha2 = "2022-01-11";
    String fecha3 = "2022-01-21";
    String fecha4 = "2022-03-17";
    String fecha5 = "2022-04-22";
    String fecha6 = "2022-07-05";
    String fecha7 = "2022-08-13";
    String fecha8 = "2022-09-06";

    //Fechas para comparar conflictos
    String fecha9 = fecha6;
    String fecha10 = "2022-12-25";
    

    //Crear reservas
    Reserva r1 = new Reserva("0", "0", fecha5, fecha6);
    Reserva r2 = new Reserva("0", "1", fecha1, fecha2);
    Reserva r5 = new Reserva("0", "1", fecha3, fecha4);
    Reserva r3 = new Reserva("1", "2", fecha3, fecha4);
    Reserva r4 = new Reserva("1", "3", fecha1, fecha2);

    
    h.anadirHabitacion(h1);
    h.anadirHabitacion(h2);
    /* h.anadirHabitacion(h3);
    h.anadirHabitacion(h4);
    h.anadirHabitacion(h5);
    h.anadirHabitacion(h6);
    h.anadirHabitacion(h7);
    h.anadirHabitacion(h8);
    h.anadirHabitacion(h9);
    h.anadirHabitacion(h10); */

    insertar(r1, h.getHabitaciones().get(0).getReservas(), new CompFEntrada());
    insertar(r2, h.getHabitaciones().get(0).getReservas(), new CompFEntrada());
    insertar(r5, h.getHabitaciones().get(0).getReservas(), new CompFEntrada());

    
    //Printear reservas de la habitacion 0
    System.out.println("Reservas hab h1");

    System.out.println(h.getHabitaciones().get(0).getReservas().toString());

    /* IndexedList<Habitacion> j = h.disponibilidadHabitaciones(fecha1,fecha3);
    for(int i = 0; i < j.size(); i++){
      System.out.println(j.get(i).getNombre());
    } */

    /* System.out.println(h.habitacionDisponible(h1, fecha9,fecha10)); //true
    System.out.println(h.habitacionDisponible(h1, fecha1,fecha2)); //false */
    
    System.out.println(h.reservasPorCliente("1").toString());
    

  }
}
