# Individual2

### Tarea 1: 
Se pide implementar el método
`public static IndexedList<Integer> indexedListCheckSum(IndexedList<Integer> list, int n)`
dentro la clase `IndexedListCheckSumUtils` que recibe una lista indexada de enteros `list` y
un `int n`.

- El método devuelve una lista indexada nueva, que contiene los mismos datos que list, pero que después de cada *n* elementos de la lista original añade un elemento *checksum*.

- El valor de ese *checksum* es la suma de los *n* elementos anteriores.

- Si el número de elementos de list no es un múltiplo de *n*, se añade un checksum al final  de la lista nueva sumando los números que hay después del último *checksum*.


indexedListCheckSum([1,4,3],3) --> [1,4,3,8] // checksum en posicion 3 (8 == 1+4+3) en la lista nueva devuelta

indexedListCheckSum([1,2,7,4],2) --> [1,2,3,7,4,11] // checksums en posiciones 2 (3 == 1+2) y 5 (11 == 7+4)

indexedListCheckSum([1,2,7],2) --> [1,2,3,7,7] // checksums en posiciones 2 (3 == 1+2) y 4 (7 == 7)

indexedListCheckSum([1,2,3],1) --> [1,1,2,2,3,3]

indexedListCheckSum([],1) --> [] // array vacio

indexedListCheckSum([1],88) --> [1,1]

### Tarea 2: Comprobar que una lista tiene *checksums*  


Se pide implementar el método `public static boolean checkIndexedListCheckSum(IndexedList<Integer> list,int n)`
dentro la clase **IndexedListCheckSumUtils** que recibe una lista indexada de enteros `list` y un `int n`. 



checkIndexedListCheckSum([1,4,3,8],3) --> **true** // checksum BUENA en posicion 3 (8 == 1+4+3)

checkIndexedListCheckSum([1,4,3,9],3) --> **false** // checksum MALO en posicion 3 (9 != 1+4+3)

checkIndexedListCheckSum([1,4,3],3) --> **false** // falta un checksum despues del elemento 3

checkIndexedListCheckSum([1,2,3,8,9,17,10,10],2) --> **true** // checksums BUENAS en posiciones 2 (3 == 1+2), 5 (17 == 8+9) y 7

checkIndexedListCheckSum([],5) --> **true** 

checkIndexedListCheckSum([8,8],5) --> **true** // checksum BUENA en posicion 1 (8 == 8)

checkIndexedListCheckSum([1,1,2,2,3,3],1) --> **true** // checksums BUENAS en posiciones 1, 3, 5
