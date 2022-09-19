package aed.individual2;

import es.upm.aedlib.indexedlist.*;

public class IndexedListCheckSumUtils {

  // a no es null, podria tener tamanno 0, n>0
  public static ArrayIndexedList<Integer> indexedListCheckSum(ArrayIndexedList<Integer> list, int n) {

    ArrayIndexedList<Integer> resultado = new ArrayIndexedList<Integer>();

    int contadorRes = 0;
    int checkSum = 0;

    for (int i = 0; i < list.size(); i++) {
      // Primero agrego el elemento de list a la lista
      resultado.add(contadorRes, list.get(i));
      contadorRes++;
      checkSum += list.get(i);

      // Compruebo si se annade el checkSum ya sea porque se ha llegado al final del
      // array o porque se ha llegado a un multiplo de n
      if ((i + 1) % n == 0 || i == list.size() - 1) {

        // annado mi variable checkSum a resultado en su posicion
        resultado.add(contadorRes, checkSum);
        contadorRes++;
        // reseteo variables
        checkSum = 0;
      }

    }

    return resultado;
  }

  // list no es null, podria tener tamanno 0, n>0

  public static boolean checkIndexedListCheckSum(ArrayIndexedList<Integer> list, int n) {
    boolean todosCumplen = true;
    int checkSum = 0;

    for (int i = 0; i < list.size() && todosCumplen; i++) {
      if (i %(n+1) == n || i == list.size() - 1) {
        todosCumplen = checkSum == list.get(i);
        //Reseteeo checkSum
        checkSum = 0;
      } else {
        checkSum += list.get(i);
      }
    }
    return todosCumplen;

  }
}
