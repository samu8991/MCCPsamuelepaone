La classe MCCPalgorithm implementa una euristica in grado di trovare una soluzione al problema Minimum Colored Cut.
La condizione di stop dell'algoritmo è un tempo determinato empiricamente in base alla dimensione delle istanze che sono caratterizzate da una tripla di valori:
  - numero di nodi
  - densità = rami/nodi
  - numero di colori
Il tempo stabilito per ogni istanza si trova nella seguente tabella:
  Numero di nodi      Tempo
  50                  2
  100                 20
  200                 50
  400                 80
  500                 200
  1000                2800
  
Nota la dimensione delle istanze, prima di eseguire il programma, bisogna settare correttamente il tempo entro il quale l'algoritmo deve terminare; inoltre è già presente una procedura in grado di prelevare le istanze da un file di testo che abbia la seguente formattazione:

# nodi #rami #colori
/* riga vuota */
nodo_partenza nodo_arrivo colore
nodo_partenza nodo_arrivo colore
*
*
*
*
nodo_partenza nodo_arrivo colore


nodo_partenza,nodo_arrivo e colore sono considerati numeri interi positivi
