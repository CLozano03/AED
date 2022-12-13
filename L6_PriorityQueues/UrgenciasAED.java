package aed.urgencias;

import es.upm.aedlib.Entry;
import es.upm.aedlib.Pair;
import es.upm.aedlib.indexedlist.ArrayIndexedList;
import es.upm.aedlib.indexedlist.IndexedList;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.priorityqueue.EmptyPriorityQueueException;
import es.upm.aedlib.priorityqueue.HeapPriorityQueue;

public class UrgenciasAED implements Urgencias {
    
    private HeapPriorityQueue<Paciente, Paciente> cola;
    private HashTableMap<String, Entry<Paciente, Paciente>> lista; 

    private int pacientesAtendidos;
    private int sumaTiemposAdmision;
    
    public UrgenciasAED(){
        this.cola = new HeapPriorityQueue<Paciente, Paciente>();
        this.lista = new HashTableMap<String, Entry<Paciente, Paciente>>();
        pacientesAtendidos = 0;
        sumaTiemposAdmision = 0;
    }


    @Override
    public Paciente admitirPaciente(String DNI, int prioridad, int hora) throws PacienteExisteException {
        Paciente paciente = new Paciente(DNI, prioridad, hora, hora);
        // compruebo si esta ya el paciente
        if (getPaciente(DNI) != null) {
            throw new PacienteExisteException();
        }
    
        lista.put(DNI, cola.enqueue(paciente, paciente));
        return lista.get(DNI).getValue();
    }

    @Override
    /* Paciente sale de las urgencias (sin ser atendido). 
        El paciente se borra de la estructuras de datos de las urgencias */
    public Paciente salirPaciente(String DNI, int hora) throws PacienteNoExisteException {
        if (lista.get(DNI) == null) {
            throw new PacienteNoExisteException();
        }
        cola.remove(lista.get(DNI));
        /* Lo borro del hashMap y returneo*/
        return lista.remove(DNI).getValue();
    }

    @Override
    public Paciente cambiarPrioridad(String DNI, int nuevaPrioridad, int hora) throws PacienteNoExisteException {
        Paciente buscoPac = getPaciente(DNI);
        if (buscoPac == null) {
            throw new PacienteNoExisteException();
        }
        if(buscoPac.getPrioridad() == nuevaPrioridad){
            return buscoPac;
        }
        cola.remove(lista.get(DNI));
        lista.remove(DNI); //lo quito de la lista porque va a cambiar
        buscoPac.setPrioridad(nuevaPrioridad);
        buscoPac.setTiempoAdmisionEnPrioridad(hora);
         //lo annado de nuevo a la lista actualizado
         lista.put(DNI, cola.enqueue(buscoPac, buscoPac));
        return lista.get(DNI).getValue();
    }

    @Override
    public Paciente atenderPaciente(int hora) {
        if(cola.isEmpty()){
            return null;
        }
        pacientesAtendidos++;

        Paciente pacienteAtendido = cola.first().getValue();        
        sumaTiemposAdmision += hora - pacienteAtendido.getTiempoAdmision(); 
        return lista.remove(cola.dequeue().getValue().getDNI()).getValue(); 
    }

    @Override
    public void aumentaPrioridad(int maxTiempoEspera, int hora) {
        for(Entry<String, Entry<Paciente, Paciente>> entry: lista){
            Paciente paciente = entry.getValue().getValue();
            if(hora - paciente.getTiempoAdmisionEnPrioridad() > maxTiempoEspera && paciente.getPrioridad() != 0){
                try {
                    cambiarPrioridad(entry.getKey(), paciente.getPrioridad() -1, hora);
                } catch (PacienteNoExisteException e) {}
            }
        }
    }

    @Override
    public Iterable<Paciente> pacientesEsperando() {
        Paciente paciente;
        IndexedList<Paciente> listaOrdenada = new ArrayIndexedList<>();
        for(Entry<String, Entry<Paciente, Paciente>> entry: lista){
            paciente = entry.getValue().getValue();
            addBinary(paciente, listaOrdenada);
        }
        return listaOrdenada;
    }

    @Override
    public Paciente getPaciente(String DNI) {
        Entry<Paciente, Paciente> entry = lista.get(DNI);
        if(entry == null){
            return null;
        }
        return entry.getValue();
    }

    @Override
    public Pair<Integer, Integer> informacionEspera() {
        return new Pair<Integer, Integer>(sumaTiemposAdmision, pacientesAtendidos);
    }


    private static <E> void addBinary(Paciente e, IndexedList<Paciente> lista) {

	    int inicio = 0;
	    int fin = lista.size() - 1;
	    int mitad = 0;
	    boolean encontrado = false;

	    while(inicio < fin && !encontrado) {
	      mitad = (inicio + fin) / 2;
	      if (e.compareTo(lista.get(mitad)) == 0) {
	        lista.add(mitad,e);
	        encontrado = true;
	      }
	      else if (e.compareTo(lista.get(mitad)) > 0) {
	        inicio = mitad + 1;
	      }
	      else {
	        fin = mitad - 1;
	      }
	    }

	    if (!encontrado && (lista.isEmpty() || e.compareTo(lista.get(inicio)) <= 0)) {
	      lista.add(inicio, e);
	    }
			else if (!encontrado) {
				lista.add(inicio + 1, e);
	    }
	  }

}