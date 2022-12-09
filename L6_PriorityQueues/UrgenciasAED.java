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

        /*  intento de corrgeir el ultimo error que no funciona aun
            if (cola.first().getValue().getPrioridad() == prioridad && (cola.first().getValue().getTiempoAdmision() < hora)) {
            // me lo guardo
            try {
                Paciente pacGuardar = salirPaciente(cola.first().getValue().getDNI(), hora);
            
                lista.put(DNI, cola.enqueue(prioridad, paciente));
                // lo vuelvo a meter
                lista.put(pacGuardar.getDNI(), cola.enqueue(pacGuardar.getPrioridad(), pacGuardar));
            } catch (PacienteNoExisteException e) {}
        } else {
            lista.put(DNI, cola.enqueue(prioridad, paciente));
        } */
    
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
            if(hora - paciente.getTiempoAdmision() > maxTiempoEspera && paciente.getPrioridad() != 0){
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
            insertarRec(paciente, listaOrdenada, (int)(listaOrdenada.size()/2));
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


    /*  */
    /* private static void insertar(Paciente e, IndexedList<Paciente> l) {
        int pos = (int) (l.size() / 2);
        boolean stop = false;
        if (l.size() == 0) {
          l.add(0, e);
          return;
        }
        while (!stop) {
            if ((pos != 0 && l.get(pos - 1).compareTo(e) <= 0)
                && l.get(pos).compareTo(e) >= 0) {
                //annado el elemento
                stop = true;
                l.add(pos, e);
            } else if (l.get(pos).compareTo(e) > 0) {
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
    } */

    private static void insertarRec(Paciente e, IndexedList<Paciente> l, int pos){
        if(l.isEmpty()){
            l.add(0, e);
        } else if(pos == 0 && e.compareTo(l.get(pos)) < 0) {
            l.add(pos, e);
        } else if(pos == 0 && e.compareTo(l.get(pos)) > 0){
            l.add(pos + 1, e);
        } else if(pos == l.size() - 1 && e.compareTo(l.get(pos)) < 0){
            l.add(pos, e);
        } else if(pos == l.size() - 1){
            l.add(pos + 1, e);
        } else if(e.compareTo(l.get(pos)) <= 0 && e.compareTo(l.get(pos - 1)) >= 0){
            l.add(pos,e);
        } else if(e.compareTo(l.get(pos)) < 0){
            insertarRec(e, l, (int)(pos/2));
        } else if(e.compareTo(l.get(pos)) > 0){
            insertarRec(e, l, (int)(l.size() - (l.size() - pos)/2));
        }
    }

}