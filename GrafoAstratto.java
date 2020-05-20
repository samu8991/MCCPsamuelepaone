package grafiColorati;

import java.util.Iterator;

public abstract class GrafoAstratto<N> implements Grafo<N> {
	public int numNodi() {
		int c = 0;
		for (N u: this) c++;
		return c;
	} // numNodi
	public int numArchi() {
		int c = 0;
		for (N u: this) {
			Iterator<? extends Arco<N>> it = adiacenti(u);
			while (it.hasNext()) { it.next(); c++; }
		}
		return c;
	} // numArchi
	public boolean esisteNodo(N u) {
		for (N v: this) if (v.equals(u)) return true;
		return false;
	} // esisteNodo
	public boolean esisteArco(Arco<N> a) {
		N u = a.getOrigine();
		if (esisteNodo(u)) {
			Iterator<? extends Arco<N>> it = adiacenti(u);
			while (it.hasNext()) if (it.next().getDestinazione().equals(a.getDestinazione())) return true;
		}
		return false;
	} // esisteArco
	public boolean esisteArco(N u, N v) {
		return esisteArco(new Arco<N>(u, v));
	} // esisteArco
	public void insArco(N u, N v) {
		insArco(new Arco<N>(u, v));
	} // insArco
	public void rimuoviNodo(N u) {
		Iterator<N> it = iterator();
		while (it.hasNext()) {
			if (it.next().equals(u)) {
				it.remove(); break;
			}
		}
	} // rimuoviNodo
	public void rimuoviArco(Arco<N> a) {
		N u = a.getOrigine(), v = a.getDestinazione();
		if (esisteNodo(u)) {
			Iterator<? extends Arco<N>> it = adiacenti(u);
			while (it.hasNext())
				if (it.next().getDestinazione().equals(v)) {
					it.remove(); break;
				}
		}
	} // rimuoviArco
	public void rimuoviArco(N u, N v) {
		rimuoviArco(new Arco<N>(u , v));
	} // rimuoviArco
	public void clear() {
		Iterator<N> it = iterator();
		while (it.hasNext()) {
			it.next(); it.remove();
		}
	} // clear
	public Grafo<N> copia() {
		Grafo<N> copia = factory();
		for (N u: this) { copia.insNodo(u); }
		for (N u: this) {
			Iterator<? extends Arco<N>> it = adiacenti(u);
			while (it.hasNext()) copia.insArco(it.next());
		}
		return copia;
	} // copia
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (!(o instanceof Grafo)) return false;
		if (o == this) return true;
		Grafo<N> g = (Grafo)o;
		if (numNodi() != g.numNodi() || numArchi() != g.numArchi()) return false;
		for (N u: this) {
			if (!g.esisteNodo(u)) return false;
			for (N v: this) {
				Arco<N> a = new Arco<N>(u, v);
				if (esisteArco(a) && !g.esisteArco(a) ||
					!esisteArco(a) && g.esisteArco(a)) return false;
			}
		}
		return true;
	} // equals
	public int hashCode() {
		final int MOLT = 41; int h = 0;
		for (N u: this) {
			h = h * MOLT + u.hashCode();
			for (N v: this) {
				Arco<N> a = new Arco<N>(u, v);
				if (esisteArco(a)) h = h * MOLT + a.hashCode();
			}
		}
		return h;
	} // hashCode
	public String toString() {
		StringBuilder sb = new StringBuilder(500);
		for (N u: this) {
			sb.append(u); sb.append(": ");
			Iterator<? extends Arco<N>> it = adiacenti(u);
			while (it.hasNext()) sb.append(it.next() + " ");
			sb.append('\n');
		}
		return sb.toString();
	} // toString

	public abstract void insNodo(N u);
	public abstract void insArco(Arco<N> a);
	public abstract Iterator<? extends Arco<N>> adiacenti(N u);
	public abstract Iterator<N> iterator();
	public abstract Grafo<N> factory();
} // GrafoAstratto
