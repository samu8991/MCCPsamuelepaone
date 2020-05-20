package grafiColorati;

import java.util.Iterator;

public abstract class GrafoNonOrientatoAstratto<N> extends GrafoAstratto<N> implements GrafoNonOrientato<N> {
	public int grado(N u) {
		int g = 0;
		if (esisteNodo(u)) {
			Iterator<? extends Arco<N>> it = adiacenti(u);
			while (it.hasNext()) { it.next(); g++; }
		}
		return g;
	} // grado
} // GrafoNonOrientatoAstratto
