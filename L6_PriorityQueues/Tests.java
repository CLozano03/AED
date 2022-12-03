package aed.urgencias;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {

  @Test
  public void testAdmitir() throws PacienteExisteException
  {
    Urgencias u = new UrgenciasAED();
    u.admitirPaciente("111", 5, 1);
    Paciente p = u.atenderPaciente(10);

    // Check expected DNI ("111") == observed DNI (p.getDNI())
    assertEquals("111", p.getDNI());
  }
}

