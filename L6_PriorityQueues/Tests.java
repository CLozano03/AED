package aed.urgencias;

import org.junit.jupiter.api.Test;

import aed.urgencias.TesterLab6.CambiarPrioridad;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.text.PasswordView;

public class Tests {
  /* Comprueba que tras haber admitido a un paciente P1 y
despu ́es a un paciente P2, ambos con la misma prioridad, una
llamada a atenderPaciente() devuelve el paciente P1 */
  @Test
  public void test01() throws PacienteExisteException
  {
    Urgencias u = new UrgenciasAED();
    u.admitirPaciente("111", 5, 1);
    Paciente p = u.atenderPaciente(10);

    // Check expected DNI ("111") == observed DNI (p.getDNI())
    assertEquals("111", p.getDNI());
  }

  @Test
  public void test02() throws PacienteExisteException{
    Urgencias u = new UrgenciasAED();
    String p1DNI = "111";
    String p2DNI = "112";
    u.admitirPaciente(p1DNI, 2, 1);
    u.admitirPaciente(p2DNI, 2, 2);
    Paciente p1 = u.atenderPaciente(10);
    Paciente p2 = u.atenderPaciente(11);
    assertEquals(p1DNI, p1.getDNI());
    assertEquals(p2DNI, p2.getDNI());
  }

  @Test
  public void test03() throws PacienteExisteException {
    Urgencias u = new UrgenciasAED();
    String p1DNI = "111";
    String p2DNI = "112";
    u.admitirPaciente(p1DNI, 5, 1);
    u.admitirPaciente(p2DNI, 2, 2);

    Paciente pacienteAtendido = u.atenderPaciente(10);
    assertEquals(p2DNI, pacienteAtendido.getDNI());

  }

  @Test
  public void test04() throws PacienteExisteException, PacienteNoExisteException{
    Urgencias u = new UrgenciasAED();
    String p1DNI = "111";
    String p2DNI = "112";
    u.admitirPaciente(p1DNI, 3, 1);
    u.admitirPaciente(p2DNI, 3, 2);

    u.salirPaciente("111", 10);
    Paciente pacienteAtendido = u.atenderPaciente(10);
    assertEquals(p2DNI, pacienteAtendido.getDNI());
  }

  /* Comprueba que tas admitir a un paciente P1 y despu ́es a un
paciente P2, ambos con prioridad 5, y despu ́es haber llamado
al m ́etodo cambiarPrioridad() con el DNI de P2 y la nueva
prioridad a 1, una llamada al m ́etodo atenderPaciente()
devuelve el paciente P2 */

  @Test
  public void test05() throws PacienteExisteException, PacienteNoExisteException{
    Urgencias u = new UrgenciasAED();
    String p1DNI = "111";
    String p2DNI = "112";
    u.admitirPaciente(p1DNI, 5, 1);
    u.admitirPaciente(p2DNI, 5, 2);

    Paciente cambiaPrioridad = u.cambiarPrioridad(p2DNI, 1, 3);

    assertEquals(p2DNI, cambiaPrioridad.getDNI());

  }
}



