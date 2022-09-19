package aed.individual1;

public class ArrayCheckSumUtils {
	// a no es null, podria tener tamanno 0, n>0
	public static int[] arrayCheckSum(int[] arr, int n) {
		/*
		 * El tamanno de mi nuevo array sera la longitud de arr mas las veces que pasa
		 * por n hasta terminar la iteracion. + 1 en caso de que la division no sea
		 * entera
		 */
		int tamannoResultado = arr.length + (int) (arr.length / n) + (arr.length % n == 0 ? 0 : 1);
		int resultado[] = new int[tamannoResultado];

		int contadorRes = 0;
		int checkSum = 0;

		for (int i = 0; i < arr.length; i++) {
			// Primero agrego el elemento de arr a la lista
			resultado[contadorRes++] = arr[i];
			checkSum += arr[i];

			/*
			 * Compruebo si se annade el checkSum ya sea porque se ha llegado al final del
			 * array o
			 * porque se ha llegado a un multiplo de n
			 */
			if ((i + 1) % n == 0 || i == arr.length - 1) {
				// annado mi variable checkSum a resultado en su posicion
				resultado[contadorRes++] = checkSum;
				// reseteo checkSum
				checkSum = 0;
			}
		}
		return resultado;
	}
}
