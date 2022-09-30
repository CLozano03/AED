package aed.hotel;

/**
 * @author Victoria Fernandez Alegria
 * @author Cesar Lozano Argueso
 */

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
    //Si la habitacion ya existe, lanzar excepcion
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

    // Compruebo que la habitacion existe en MiHotel
    if (busquedaBinaria(habitacionAux, habitaciones, new CompNHabitacion()) == -1) {
      throw new IllegalArgumentException();
    } else {
      Habitacion habitacion = habitaciones.get(busquedaBinaria(habitacionAux, habitaciones, new CompNHabitacion()));

      // Antes de reservarla, compruebo que esta disponible en las fechas
      if (habitacionDisponible(habitacion, reserva.getDiaEntrada(), reserva.getDiaSalida())){
        insertar(reserva, habitacion.getReservas(), new CompFEntrada());
        reservada = true;
      }
    }

    return reservada;
  }

  private boolean habitacionDisponible(Habitacion hab, String diaEntrada, String diaSalida) {
    // Compruebo si la hab que quiero esta disponible en diaEntrada y diaSalida incluido
    IndexedList<Habitacion> listaHabDisponibles = disponibilidadHabitaciones(diaEntrada, diaSalida);

    boolean encontrada = false;
    //Busco en la lista mi habitacion, para ver si esta disponible entre esas fechas
    for (int i = 0; i < listaHabDisponibles.size() && !encontrada; i++) {
      if (listaHabDisponibles.get(i).getNombre().equals(hab.getNombre())) {
        encontrada = true;
      }
    }
    return encontrada;
  }

  @Override
  public boolean cancelarReserva(Reserva reserva) {

    Habitacion hab = new Habitacion(reserva.getHabitacion(), 0);
    int indice = busquedaBinaria(hab, habitaciones, new CompNHabitacion());

    boolean cancelable = false;
    // Compruebo que la habitacion existe en MiHotel
    if (indice == -1) {
      throw new IllegalArgumentException("La habitacion no existe");
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

      if (punteroHabitacion.getReservas().size() == 0) {

        insertar(punteroHabitacion, habitacionesLibres, cmpPrecio);
        libre = false; // Para que no la aniada dos veces al salir del bucle de reservas

      } else {

        for (int j = 0; j < punteroHabitacion.getReservas().size() && libre; j++) {
          // Si entran las fechas en conflicto con alguna reserva, no esta libre y se sale
          // del bucle
          if (!(cmpES.compare(punteroHabitacion.getReservas().get(j), posibleReserva) >= 0
              || cmpES.compare(posibleReserva, punteroHabitacion.getReservas().get(j)) >= 0)) {
            libre = false;
          }
        }
      }

      // La aniado en caso de que no haya entrado en conflicto
      if (libre) {
        insertar(punteroHabitacion, habitacionesLibres, cmpPrecio);
      }

    }
    return habitacionesLibres;
  }

  @Override
  public IndexedList<Reserva> reservasPorCliente(String dniPasaporte) {

    // Punteros
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

    // variable que para de recorrer la lista de reservas cuando encuentra una
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

    Habitacion habitacion = new Habitacion(nombreHabitacion, 0);
    int indice = busquedaBinaria(habitacion, habitaciones, new CompNHabitacion());

    Reserva reservaAux = new Reserva("", "", dia, "");

    //CmpES para ver que la fecha dada esta dentro de la reserva
    CompES cmpES = new CompES();

    // Compruebo que la habitacion existe en MiHotel
    if (indice == -1) {
      throw new IllegalArgumentException("Habitacion inexistente");
    } else {
      habitacion = habitaciones.get(indice);

      Reserva reserva = new Reserva("", "", "", "");
      boolean encontrado = false;

      // Es necesario recorrer la lista hasta el final para que devuelva la ultima reserva
      for (int i = 0; i < habitacion.getReservas().size() && !encontrado; i++) {
        reserva = habitacion.getReservas().get(i);

        if (habitacion.getReservas().get(i).compareTo(reservaAux) <= 0
            && cmpES.compare(reservaAux, habitacion.getReservas().get(i)) < 0) {
          encontrado = true;
        }
      }
      return encontrado ? reserva : null;
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
    } else if (busquedaBinaria(e, l, cmp) != -1) {
      throw new IllegalArgumentException("Ya existe este elemento");
    } else {
      while (!stop) {
      
        if ((pos != 0 && cmp.compare(l.get(pos - 1), e) <= 0)
            && cmp.compare(l.get(pos), e) >= 0) {
          //annado el elemento
          stop = true;
          l.add(pos, e);
        } else if (cmp.compare(l.get(pos), e) > 0) {
          //ajuste cuando hay que aniadir en primera pos
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

  private static <E> int busquedaBinaria(E e, IndexedList<E> l, Comparator<E> cmp) {
    int min = 0;
    int max = l.size() - 1;
    int medio;

    while (min <= max) {
      medio = (int) ((min + max) / 2);
      if (cmp.compare(e, l.get(medio)) == 0) {
        return medio;
      } else if (cmp.compare(l.get(medio), e) > 0) { // h > medio
        max = medio - 1;
      } else {                                       // h < medio  
        min = medio + 1;
      }
    }
    return -1;
  }

  // -----------------------------------------------------------------------------------------
  // ---------------------------------- COMPARATORS ------------------------------------------
  // -----------------------------------------------------------------------------------------

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
      return o1.compareTo(o2);
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
}