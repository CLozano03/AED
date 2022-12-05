package aed.urgencias;

import java.util.Iterator;
import es.upm.aedlib.Entry;
import es.upm.aedlib.Pair;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.priorityqueue.HeapPriorityQueue;

public class UrgenciasAED implements Urgencias {
    
    private HeapPriorityQueue<Integer, Paciente> cola;
    private HashTableMap<String, Paciente> lista; 
    
    public UrgenciasAED(){
        this.cola = new HeapPriorityQueue<>();
        this.lista = new HashTableMap<>();
    }

    @Override
    public Paciente admitirPaciente(String DNI, int prioridad, int hora) throws PacienteExisteException {
        Paciente paciente = new Paciente(DNI, prioridad, hora, hora);
        cola.enqueue(prioridad, paciente); // Insertamos el nuevo nodo en la ultima posicion libre
        // ¿violamos la heap order property?--> cada padre tiene mayo prioridad que su hijos
        Iterator<Entry<Integer, Paciente>> it = cola.iterator();
        while(it.hasNext()) {
            Entry<Integer, Paciente> entry = it.next();
            Paciente pac = entry.getValue();
            if (pac.compareTo(paciente) > 0) { // es mas urgente atender a paciente 
                //Los voy a intercambiar
                Integer clavePac = entry.getKey();
                Paciente pacientePac = entry.getValue();
                // Pongo en su lugar a paciente
                replaceKey​(entry, prioridad);
                replaceValue​(entry, paciente);
                paciente = pac;
            } else if (pac.compareTo(paciente) < 0) {
                replaceKey​(entry,pac.getPrioridad());
                replaceValue​(entry, pac);
            }
        }
        return lista.put(DNI, paciente);
    }

    @Override
    public Paciente salirPaciente(String DNI, int hora) throws PacienteNoExisteException {
        
        return null;
    }

    @Override
    public Paciente cambiarPrioridad(String DNI, int nuevaPrioridad, int hora) throws PacienteNoExisteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Paciente atenderPaciente(int hora) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void aumentaPrioridad(int maxTiempoEspera, int hora) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Iterable<Paciente> pacientesEsperando() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Paciente getPaciente(String DNI) {
        return lista.get(DNI);
    }

    @Override
    public Pair<Integer, Integer> informacionEspera() {
        // TODO Auto-generate method stub
        return null;
    }
}