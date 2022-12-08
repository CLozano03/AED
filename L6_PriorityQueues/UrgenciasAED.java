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
    private HashTableMap<String, Paciente> lista; 

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
        cola.enqueue(prioridad, paciente); // Insertamos el nuevo nodo en la ultima posicion libre
        // ¿violamos la heap order property?--> cada padre tiene mayor prioridad que su hijos
        /* Iterator<Entry<Integer, Paciente>> it = cola.iterator();
        while(it.hasNext()) {
            Entry<Integer, Paciente> entry = it.next();
            Paciente pac = entry.getValue();
            if (pac.compareTo(paciente) > 0) { // es mas urgente atender a paciente 
                //Los voy a intercambiar
                Integer clavePac = entry.getKey();
                Paciente pacientePac = entry.getValue();
                // Pongo en su lugar a paciente
                cola.replaceKey(entry, prioridad);
                cola.replaceValue(entry, paciente);
                paciente = pac;
            } else if (pac.compareTo(paciente) < 0) {
                cola.replaceKey(entry, pac.getPrioridad());
                cola.replaceValue(entry, pac);
            }
        } */
        lista.put(DNI, paciente);
        return paciente;
    }

    @Override
    /* Paciente sale de las urgencias (sin ser atendido). 
        El paciente se borra de la estructuras de datos de las urgencias */
    public Paciente salirPaciente(String DNI, int hora) throws PacienteNoExisteException {
        // Compruebo que Paciente existe en la cola
        /* Paciente pacBus = new Paciente(DNI, 0, hora, hora);
        Iterator<Entry<Integer, Paciente>> it = cola.iterator();
        boolean found = false;
        Entry<Integer, Paciente> entry;
        while(it.hasNext() && !found) {
            entry = it.next();
            Paciente pac = entry.getValue();
            if (pac.equals(pacBus)) {
                found = true;
                pacBus = new Paciente(pac.getDNI(), pac.getPrioridad(), pac.getTiempoAdmision(), pac.getTiempoAdmisionEnPrioridad());
            }
        }
        if (!found) {
            throw new PacienteNoExisteException();
        }
        Entry<Integer, Paciente> entryPac = new EntryImpl<Integer,Paciente>(pacBus.getPrioridad(), pacBus);
        cola.remove(entryPac);
        */
        Paciente paciente = lista.get(DNI);
        if (paciente == null) {
            throw new PacienteNoExisteException();
        }
        cola.remove(new EntryImpl<Integer, Paciente>(paciente.getPrioridad(), paciente));
        /* Lo borro del hashMap y returneo */
        return lista.remove(DNI);
    }

    @Override
    public Paciente cambiarPrioridad(String DNI, int nuevaPrioridad, int hora) throws PacienteNoExisteException {
        //Paciente buscoPac = new Paciente(DNI, 0,0,0);
        
        // Compruebo que el paciente existe
        Paciente buscoPac = getPaciente(DNI);
        if (buscoPac == null) {
            throw new PacienteNoExisteException();
        }
        /* Iterator<Entry<Integer, Paciente>> it = cola.iterator();
        boolean found = false;
        while(it.hasNext() && !found) {
            Entry<Integer, Paciente> entry = it.next();
            if (buscoPac.equals(entry.getValue())) {
                found = true;
                lista.remove(buscoPac.getDNI());
                buscoPac = entry.getValue();
                buscoPac.setPrioridad(nuevaPrioridad);
                buscoPac.setTiempoAdmisionEnPrioridad(hora);
                lista.put(buscoPac.getDNI(), buscoPac); //lo annado de nuevo a la lista actualizado
            }
        } */
        Entry<Integer, Paciente> entryQuitar = new EntryImpl<Integer,Paciente>(buscoPac.getPrioridad(), buscoPac);
        cola.remove(entryQuitar);
        lista.remove(DNI); //lo quito de la lista porque va a cambiar
        buscoPac.setPrioridad(nuevaPrioridad);
        buscoPac.setTiempoAdmisionEnPrioridad(hora);
        cola.enqueue(buscoPac.getPrioridad(), buscoPac);
    
         //lo annado de nuevo a la lista actualizado
        return lista.put(DNI, buscoPac);
    }

    @Override
    public Paciente atenderPaciente(int hora) {
        if (cola.isEmpty()) return null;
        pacientesAtendidos++;
        Paciente pacienteAtendido = cola.dequeue().getValue();
        sumaTiemposAdmision += hora - pacienteAtendido.getTiempoAdmision(); 
        return lista.remove(pacienteAtendido.getDNI()); 
    }

    @Override
    public void aumentaPrioridad(int maxTiempoEspera, int hora) {
        for(Entry<Integer, Paciente> entry: cola){

        }
    }

    @Override
    public Iterable<Paciente> pacientesEsperando() {
        Paciente paciente;
        IndexedList<Paciente> listaOrdenada = new ArrayIndexedList<>();
        for(Entry<String,Paciente> entry: lista){
            paciente = entry.getValue();
            insertar(paciente, listaOrdenada);
        }
        return listaOrdenada;
    }

    @Override
    public Paciente getPaciente(String DNI) {
        return lista.get(DNI);
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