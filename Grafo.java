package grafiColorati;

import java.util.Iterator;

public interface Grafo<N> extends Iterable<N> {
	int numNodi();
	int numArchi();
	boolean esisteNodo(N u);
	boolean esisteArco(Arco<N> a);
	boolean esisteArco(N u, N v);
	void insNodo(N u);
	void insArco(Arco<N> a);
	void insArco(N u, N v);
	void rimuoviNodo(N u);
	void rimuoviArco(Arco<N> a);
	void rimuoviArco(N u, N v);
	Iterator<? extends Arco<N>> adiacenti(N u);
	void clear();
	Grafo<N> copia();
} // Grafo
