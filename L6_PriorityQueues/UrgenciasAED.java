package aed.urgencias;

import java.util.Iterator;
import es.upm.aedlib.Entry;
import es.upm.aedlib.EntryImpl;
import es.upm.aedlib.Pair;
import es.upm.aedlib.indexedlist.ArrayIndexedList;
import es.upm.aedlib.indexedlist.IndexedList;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.priorityqueue.HeapPriorityQueue;

public class UrgenciasAED implements Urgencias {
    
    private HeapPriorityQueue<Integer, Paciente> cola;
    private HashTableMap<String, Entry<Integer, Paciente>> lista; 

    private int pacientesAtendidos;
    private int sumaTiemposAdmision;
    
    public UrgenciasAED(){
        this.cola = new HeapPriorityQueue<>();
        this.lista = new HashTableMap<>();
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
        lista.put(DNI, cola.enqueue(prioridad, paciente));
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
         lista.put(DNI, cola.enqueue(buscoPac.getPrioridad(), buscoPac));
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
        for(Entry<String, Entry<Integer, Paciente>> entry: lista){
            Paciente paciente = entry.getValue().getValue();
            if(hora - paciente.getTiempoAdmision() > maxTiempoEspera ){
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
        for(Entry<String, Entry<Integer, Paciente>> entry: lista){
            paciente = entry.getValue().getValue();
            insertar(paciente, listaOrdenada);
        }
        return listaOrdenada;
    }

    @Override
    public Paciente getPaciente(String DNI) {
        Entry<Integer, Paciente> entry = lista.get(DNI);
        if(entry == null){
            return null;
        }
        return entry.getValue();
    }

    @Override
    public Pair<Integer, Integer> informacionEspera() {
        return new Pair<Integer, Integer>(sumaTiemposAdmision, pacientesAtendidos);
    }


    /*  */
    private static void insertar(Paciente e, IndexedList<Paciente> l) {
        int pos = (int) ((l.size()) / 2);
        boolean stop = false;
        if (l.size() == 0) {
          l.add(0, e);
        }else {
          while (!stop) {
          
            if ((pos != 0 && e.compareTo(l.get(pos - 1)) <= 0)
                && e.compareTo(l.get(pos)) >= 0) {
              //annado el elemento
              stop = true;
              l.add(pos, e);
            } else if (e.compareTo(l.get(pos)) > 0) {
              //ajuste cuando hay que annadir en primera pos
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

}