package grafiColorati;

import java.util.Iterator;

public abstract class GrafoNonOrientatoColoratoAstratto<N> extends GrafoNonOrientatoAstratto<N> implements GrafoColorato<N>{
	public void insArco(Arco<N> a) { insArco(new ArcoColorato<N>(a.getOrigine(), a.getDestinazione())); }
	public void insArco(Arco<N> a, Colore c) { insArco(new ArcoColorato<N>(a.getOrigine(), a.getDestinazione(), c)); }
	public void insArco(N u, N v, Colore c) { insArco(new ArcoColorato<N>(u, v, c)); }
	public void rimuoviArco(Arco<N> a) {
		if (!(a instanceof ArcoColorato)) throw new IllegalArgumentException();
		rimuoviArco((ArcoColorato<N>)a);
	} // rimuoviArco
	public void modArco(ArcoColorato<N> ap, Colore c) {
		if (!esisteNodo(ap.getOrigine()) || !esisteNodo(ap.getDestinazione())) return;
		Iterator<ArcoColorato<N>> it = adiacenti(ap.getOrigine());
		while (it.hasNext()) {
			ArcoColorato<N> a = it.next();
			if (a.getDestinazione().equals(ap.getDestinazione())) {
				a.setColore(c);
				ArcoColorato<N> api = new ArcoColorato<N>(ap.getDestinazione(), ap.getOrigine());
				it = adiacenti(api.getOrigine());
				while (it.hasNext()) {
					ArcoColorato<N> ai = it.next();
					if (ai.getDestinazione().equals(api.getDestinazione())) {
						ai.setColore(c); return;
					}
				}
			}
		}
		insArco(new ArcoColorato<N>(ap.getOrigine(), ap.getDestinazione(), c));
	} // modArco
	public Colore colore(N u, N v) {
		Iterator<ArcoColorato<N>> it = adiacenti(u);
		while (it.hasNext()) {
			ArcoColorato<N> a = it.next();
			if (a.getDestinazione().equals(v)) return a.getColore();
		}
		return null;
	} // peso

	public abstract void insArco(ArcoColorato<N> ap);
	public abstract void rimuoviArco(ArcoColorato<N> ap);
	public abstract Iterator<ArcoColorato<N>> adiacenti(N u);
}
