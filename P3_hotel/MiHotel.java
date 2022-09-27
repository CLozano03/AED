package aed.hotel;

import es.upm.aedlib.indexedlist.*;
import java.util.Comparator;

import javax.lang.model.util.ElementScanner14;

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
    CompES cmpES = new CompES();
    
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
    
    // Comparadores usados
    CompES cmpES = new CompES();
    CompPrecio cmpPrecio = new CompPrecio();

    IndexedList<Habitacion> habitacionesLibres = new ArrayIndexedList<Habitacion>();

    for (int i = 0; i < habitaciones.size(); i++) {

      Habitacion punteroHabitacion = habitaciones.get(i);

      if(punteroHabitacion.getReservas().size() != 0){
        
        for (int j = 0; j < punteroHabitacion.getReservas().size(); j++) {
  
          if (cmpES.compare(punteroHabitacion.getReservas().get(j), posibleReserva) >= 0 || cmpES.compare(posibleReserva, punteroHabitacion.getReservas().get(j)) >= 0) {
            try{
              insertar(punteroHabitacion, habitacionesLibres, cmpPrecio);
            } catch (IllegalArgumentException e) {}
          }
        }
      } else {
        insertar(punteroHabitacion, habitacionesLibres, cmpPrecio);
      }

    }
    return habitacionesLibres;
  }

  @Override
  public IndexedList<Reserva> reservasPorCliente(String dniPasaporte) {
    IndexedList<Reserva> reservas = new ArrayIndexedList<Reserva>();

    for (int i = 0; i < habitaciones.size(); i++) {
      for (int j = 0; i < habitaciones.get(i).getReservas().size(); j++) {
        //Reserva punteroReserva = habitaciones.get(i).getReservas().get(j);
        if (habitaciones.get(i).getReservas().get(j).getDniPasaporte().hashCode() == dniPasaporte.hashCode()) {
          insertar(habitaciones.get(i).getReservas().get(j), reservas, new CompFEntrada());
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

    // Comparadores a usar
    CompNHabitacion cmpNHabitacion = new CompNHabitacion();
    CompFEntrada cmpFEntrada = new CompFEntrada();
    CompFSalida cmpFSalida = new CompFSalida();

    for (int i = 0; i < habitaciones.size(); i++) {
      Habitacion punteroHabitacion = habitaciones.get(i);
      for (int j = 0; j < punteroHabitacion.getReservas().size(); j++) {
        Reserva punteroReserva = punteroHabitacion.getReservas().get(j);

        // entrada antes que hoyDia y salida despues o igual a hoyDia.
        if (cmpFEntrada.compare(punteroReserva, hoyDiaReserva) < 0
            && cmpFSalida.compare(punteroReserva, hoyDiaReserva) >= 0) {
          insertar(punteroHabitacion, aLimpiar, cmpNHabitacion);
        }
      }
    }
    return aLimpiar;
  }

  @Override
  public Reserva reservaDeHabitacion(String nombreHabitacion, String dia) throws IllegalArgumentException {

    Habitacion habitacion = new Habitacion(nombreHabitacion, 0);

    // Compruebo que la habitacion existe en MiHotel
    if (busquedaBinaria(habitacion, habitaciones, new CompNHabitacion()) == -1) {

      throw new IllegalArgumentException("Habitacion inexistente");

    } else {

      Reserva reserva = new Reserva(nombreHabitacion, "", dia, "");
      boolean encontrado = false;

      for (int i = 0; i < habitacion.getReservas().size() && !encontrado; i++) {
        if (habitacion.getReservas().get(i).compareTo(reserva) <= 0) {
          reserva = habitacion.getReservas().get(i);
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
  // otro buscar
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
  /*
   * Dentro de la clase MiHotel creamos una clase static comparator que nos va a
   * ayudar a ordenar las listas
   * Vamos a crear tantas clases static como necesitemos para ordenar las listas
   */

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

  // Mini tester para insertar
  public static void main(String[] args){

    MiHotel h = new   MiHotel();
    
    //Crear habitaciones
    Habitacion h1 = new Habitacion("0", 1);
    Habitacion h2 = new Habitacion("1", 2);
    Habitacion h3 = new Habitacion("2", 3);
    Habitacion h4 = new Habitacion("3", 4);
    Habitacion h5 = new Habitacion("4", 5);
    Habitacion h6 = new Habitacion("5", 6);
    Habitacion h7 = new Habitacion("6", 7);
    Habitacion h8 = new Habitacion("7", 8);
    Habitacion h9 = new Habitacion("8", 9);
    Habitacion h10 = new Habitacion("9", 10);
    
    //Crear fechas
    String fecha1 = "2022-01-11";
    String fecha2 = "2022-01-04";
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
    Reserva r3 = new Reserva("1", "2", "2", "2");
    Reserva r4 = new Reserva("2", "3", "3", "3");

    
    h.anadirHabitacion(h1);
    h.anadirHabitacion(h2);
    h.anadirHabitacion(h3);
    h.anadirHabitacion(h4);
    h.anadirHabitacion(h5);
    h.anadirHabitacion(h6);
    h.anadirHabitacion(h7);
    h.anadirHabitacion(h8);
    h.anadirHabitacion(h9);
    h.anadirHabitacion(h10);

    h.getHabitaciones().get(0).getReservas().add(0, r1);
    h.getHabitaciones().get(0).getReservas().add(1, r2);
    h.getHabitaciones().get(0).getReservas().add(2, r5);

  IndexedList<Habitacion> j = h.disponibilidadHabitaciones(fecha7,fecha8);
   for(int i = 0; i < j.size(); i++){
    System.out.println(j.get(i).getNombre());
  }

  System.out.println(h.habitacionDisponible(h1, fecha9,fecha10)); //true
  System.out.println(h.habitacionDisponible(h1, fecha1,fecha2)); //false

  }
}
