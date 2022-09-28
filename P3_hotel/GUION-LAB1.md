# Laboratorio1

### Objetivo

Se pide implementar la interfaz **Hotel** en la clase **MiHotel**:

~~~~
interface Hotel {

void anadirHabitacion(Habitacion habitacion);

IndexedList<Habitacion> disponibilidadHabitaciones(String diaEntrada, String diaSalida);

boolean reservaHabitacion(Reserva reserva);

boolean cancelarReserva(Reserva reserva);

Reserva reservaDeHabitacion(String nombreHabitacion, String dia);

IndexedList<Reserva> reservasPorCliente(String dniPasaporte);

IndexedList<Habitacion> habitacionesParaLimpiar(String hoyDia);

}
~~~~
---
## Clases

#### MiHotel 
- Atributo habitaciones de tipo IndexedList<Habitacion>
- La clase dispone de un único constructor sin parámetros para inicializar el atributo habitaciones, que debe almacenar las habitaciones del hotel.

#### Habitación
  
  Una habitación tiene asociado un nombre, un precio para cada noche y una lista de reservas.
  
  ~~~~
  public class Habitacion implements Comparable<Habitacion> {
    private String nombre; // un nombre unico
    private int precio; // precio al noche
    private IndexedList<Reserva> reservas; // reservas
    ... // getters, equals, toString
  }
  ~~~~ 



#### Reserva
  
  Almacena los datos de una reserva de una habitación
  
  ~~~~
  public class Reserva implements Comparable<Reserva> {
    private String habitacion; // habitacion reservada
    private String dniPasaporte; // dni o pasaporte del cliente
    private String diaEntrada; // Dia entrada
    private String diaSalida; // Dia salida
  }
  ~~~~
  
  - Las fechas están representadas como instancias de `String` en la forma "yyyy-mm-dd" (por ejemplo "2022-09-20")
  - Dos fechas fecha1 y fecha2 son comparables usando el método compareTo. Por ejemplo: `fecha1.compareTo(fecha2)` devuelve
  un entero < 0 si fecha1 es anterior a fecha2
---
## A tener en cuenta
  - **Dos reservas no estan en conflicto** --> Cuando la fecha de salida de salida de r1 es menor o igual que la fecha de entrada de r2, o viceversa (la fecha de salida de r2 es menor o igual que la fecha de entrada de r1).  
  
> En construccion
