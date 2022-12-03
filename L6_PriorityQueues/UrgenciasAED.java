package aed.urgencias;

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
        Paciente paciente = new Paciente(DNI, prioridad, 0,0);
        return lista.put(DNI, paciente);
    }

    @Override
    public Paciente salirPaciente(String DNI, int hora) throws PacienteNoExisteException {
        // TODO Auto-generated method stub
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