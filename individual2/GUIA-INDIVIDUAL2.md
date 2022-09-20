# Individual2

### Tarea 1: 


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
