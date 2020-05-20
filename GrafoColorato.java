package grafiColorati;

import java.util.Iterator;

public interface GrafoColorato<N> extends Grafo<N> {
	
	public void insArco(ArcoColorato<N> ap);
	public void insArco(Arco<N> a, Colore c);
	public void insArco(N u, N v, Colore c);
	public void modArco(ArcoColorato<N> a, Colore c);
	public void rimuoviArco(ArcoColorato<N> ap);
	public Iterator<ArcoColorato<N>> adiacenti(N u);
	public Colore colore(N u, N v);
	
} // GrafoColorato
