## Individual1

Se pide implementar el método `public static int[] arrayCheckSum(int[] arr, int n)`
dentro la clase **ArrayCheckSumUtils** (en el paquete invididual1) que recibe un array de enteros arr y un int n.

- El método devuelve un array nuevo, que contiene los mismos datos que arr, pero que después de cada n elementos del array original añade un elemento *checksum*.

- El valor de ese *checksum* es la suma de los n elementos anteriores. Si el número de elementos de arr no es un múltiplo de n, se añade
un checksum al final del array nuevo sumando los números que hay después del último checksum.

##### Ejemplos
arrayCheckSum([1,4,3],3) --> [1,4,3,8] // checksum en posicion 3 (8 == 1+4+3) en el array nuevo devuelto

arrayCheckSum([1,2,7,4],2) --> [1,2,3,7,4,11] // checksums en posiciones 2 (3 == 1+2) y 5 (11 == 7+4)

arrayCheckSum([1,2,7],2) --> [1,2,3,7,7] // checksums en posiciones 2 (3 == 1+2) y 4 (7 == 7)

arrayCheckSum([1,2,3],1) --> [1,1,2,2,3,3]

arrayCheckSum([],1) --> [] // array vacio

arrayCheckSum([1],88) --> [1,1]

<p></p>
