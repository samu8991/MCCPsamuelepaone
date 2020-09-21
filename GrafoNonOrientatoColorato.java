import java.io.*;
import java.util.*;


public class GrafoNonOrientatoColorato<N> extends GrafoNonOrientatoColoratoAstratto<N> {
	private Map<N, LinkedList<ArcoColorato<N>>> grafo = new HashMap<N, LinkedList<ArcoColorato<N>>>();
	private HashSet<ArcoColorato<N>> listaArchi = new HashSet<>();
	public GrafoNonOrientatoColorato() {
		grafo = new HashMap<N, LinkedList<ArcoColorato<N>>>();
	}
	public GrafoNonOrientatoColorato(GrafoNonOrientatoColorato<N> G) {
		grafo = new HashMap<N, LinkedList<ArcoColorato<N>>>(G.grafo);
	}
	public int numNodi() { return grafo.size(); }
	public boolean esisteNodo(N u) { return grafo.containsKey(u); }
	public void insNodo(N u) {
		if (esisteNodo(u)) return;
		grafo.put(u, new LinkedList<ArcoColorato<N>>());
	} // insNodo
	public void insArco(ArcoColorato<N> ap) {
		if (!grafo.containsKey(ap.getOrigine()) || !grafo.containsKey(ap.getDestinazione()))
			throw new RuntimeException("Nodo/i non presente/i durante insArco");
		LinkedList<ArcoColorato<N>> l = grafo.get(ap.getOrigine());
		listaArchi.add(ap);
		Iterator<ArcoColorato<N>> it = l.iterator();
		while (it.hasNext())
			if (it.next().getDestinazione().equals(ap.getDestinazione())) it.remove();
		l.add(ap);
		ArcoColorato<N> inverso = new ArcoColorato<N>(ap.getDestinazione(), ap.getOrigine(),ap.getColore());
		l = grafo.get(inverso.getOrigine());
		it = l.iterator();
		while (it.hasNext())
			if (it.next().getDestinazione().equals(inverso.getDestinazione())) it.remove();
		l.add(inverso);
	} // insArco
	public void rimuoviNodo(N u) {
		grafo.remove(u);
		Iterator<N> it = grafo.keySet().iterator();
		while (it.hasNext()) {
			N v = it.next();
			Iterator<ArcoColorato<N>> itAr = grafo.get(v).iterator();
			while (itAr.hasNext())
				if (itAr.next().getDestinazione().equals(u)) {
					itAr.remove(); break;
				}
		}
	} // rimuoviNodo
	public void rimuoviArco(ArcoColorato<N> ap) {
		N u = ap.getOrigine(), v = ap.getDestinazione();
		if (!grafo.containsKey(u)) return;
		Iterator<ArcoColorato<N>> itAr = grafo.get(u).iterator();
		while (itAr.hasNext())
			if (itAr.next().getDestinazione().equals(v)) {
				itAr.remove();
				itAr = grafo.get(v).iterator();
				while (itAr.hasNext())
					if (itAr.next().getDestinazione().equals(u)) {
						itAr.remove(); break;
					}
				break;
			}
	} // rimuoviArco
	public void clear() { grafo.clear(); }
	
	public Iterator<ArcoColorato<N>> adiacenti(N u) {
		if (!esisteNodo(u)) throw new IllegalArgumentException();
		return new IteratoreAdiacenti(u);
	} // adiacenti
	
	private class IteratoreAdiacenti implements Iterator<ArcoColorato<N>> {
		private Iterator<ArcoColorato<N>> it;
		private ArcoColorato<N> a = null;
		public IteratoreAdiacenti(N u) { it = grafo.get(u).iterator(); }
		public boolean hasNext() { return it.hasNext(); }
		public ArcoColorato<N> next() { return a = it.next(); }
		public void remove() {
			it.remove();
			ArcoColorato<N> inverso = new ArcoColorato<N>(a.getDestinazione(), a.getOrigine());
			Iterator<ArcoColorato<N>> itAr = grafo.get(inverso.getOrigine()).iterator();
			while (itAr.hasNext())
				if (itAr.next().getDestinazione().equals(a.getOrigine())) {
					itAr.remove(); break;
				}
		} // remove
	} // IteratoreAdiacenti
	public Iterator<N> iterator() { return new IteratoreGrafo(); }
	private class IteratoreGrafo implements Iterator<N> {
		private Iterator<N> it = grafo.keySet().iterator();
		private N u = null;
		public boolean hasNext() { return it.hasNext(); }
		public N next() { return u = it.next(); }
		public void remove() {
			it.remove();
			Iterator<N> it2 = grafo.keySet().iterator();
			while (it2.hasNext()) {
				N v = it.next();
				Iterator<ArcoColorato<N>> itAr = grafo.get(v).iterator();
				while (itAr.hasNext())
					if (itAr.next().getDestinazione().equals(u)) {
						itAr.remove(); break;
					}
			}
		} // remove
	} // IteratoreGrafo
	

	public HashSet<Colore> insiemeColori() {
		
		HashSet<Colore> ret = new HashSet<>();
		Set<N> l = grafo.keySet();
		for(N u: l) 
			for(ArcoColorato<N> arco:grafo.get(u))
				ret.add(arco.getColore());
		return ret;
		
	}//numeroColori
	
	public int getComponenti() {
		int count = 1;
		HashSet<N> visitati = new HashSet<>();
		LinkedList<N> stack = new LinkedList<>();
		for(N u: grafo.keySet()) {
			if(!visitati.contains(u)) {
				stack.push(u);
				while(!stack.isEmpty()) {
					N nodoCorrente = stack.pop();
					if(!visitati.contains(nodoCorrente)) {
						visitati.add(nodoCorrente);
						for(ArcoColorato<N> ap: grafo.get(nodoCorrente))
							stack.push(ap.getDestinazione());
					}
				}
				if(visitati.size()!= grafo.keySet().size())count++;
				//stack.clear();
			}
		}//for
		return count;
	}//getComponenti
	public int componentiConnesse(HashSet<Colore> S) {
		GrafoNonOrientatoColorato<N> tmp = new GrafoNonOrientatoColorato<>();
		if(S.isEmpty()) return this.numNodi();
		for(ArcoColorato<N> ac : listaArchi) {
			tmp.insNodo(ac.getOrigine());
			tmp.insNodo(ac.getDestinazione());
			if(S.contains(ac.getColore())) 
				tmp.insArco(ac);
		}
		return tmp.getComponenti();
		}//componentiConnesse
	
	public List<ArcoColorato<N>> trovaTaglio(HashSet<Colore> BestS) {
		GrafoNonOrientatoColorato<N> tmp = new GrafoNonOrientatoColorato<>();
		for(ArcoColorato<N> ac : listaArchi) {
			tmp.insNodo((N) ac.getOrigine());
			tmp.insNodo((N) ac.getDestinazione());
			if(BestS.contains(ac.getColore()))
				tmp.insArco(ac);
		}
		List<ArcoColorato<N>> l = new ArrayList<ArcoColorato<N>>();
		HashSet<N> W = new HashSet<>();
		HashSet<N> Wsegnato = new HashSet<>();
		LinkedList<N> stack = new LinkedList<>();
		N [] lista = (N[]) grafo.keySet().toArray();
		stack.push(lista[0]);
		while (!stack.isEmpty()) {
			N nodoCorrente = stack.pop();
			if (!W.contains(nodoCorrente)) {
				W.add(nodoCorrente);
				Iterator<ArcoColorato<N>> it = tmp.adiacenti(nodoCorrente);
				while (it.hasNext()) {
					ArcoColorato<N> ap = it.next();
					stack.push(ap.getDestinazione());
				}
			}
		}
		for (int i = 0; i < numNodi(); i++){
			if (!W.contains(i))
				Wsegnato.add((N) new Integer(i));
		}
		for(ArcoColorato<N> arco:listaArchi) {
			if (W.contains(arco.getOrigine()) && Wsegnato.contains(arco.getDestinazione()) || (W.contains(arco.getDestinazione()) && Wsegnato.contains(arco.getOrigine())))
				l.add(arco);
		}

		return l;
		
	}
	public Grafo<N> factory() { return new GrafoNonOrientatoColorato<N>(); }

	public static GrafoNonOrientatoColorato<Integer> prelevaIstanze(File f){
		GrafoNonOrientatoColorato<Integer> grafo = new GrafoNonOrientatoColorato<>();
		try {
			InputStream is = new FileInputStream(f);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(is));
			boolean endloop = true;
			int i = 1;
			while(endloop) {
				String stringa = br.readLine();
				if(i<=2) {
					i++;
					continue;
				}
				else if(stringa == null)endloop = false;
				else {
					StringTokenizer st = new StringTokenizer(stringa);
					int nodoSorgente = Integer.parseInt(st.nextToken());
					int nodoDestinazione= Integer.parseInt(st.nextToken());
					Colore colore = new Colore(Integer.parseInt(st.nextToken()));
					grafo.insNodo(nodoSorgente);grafo.insNodo(nodoDestinazione);
					ArcoColorato<Integer> ac = new ArcoColorato<>(nodoSorgente,nodoDestinazione,colore);
					grafo.insArco(ac);
				}
			}
			br.close();
		}catch(IOException e ) {
			System.out.println("Errore nella lettura del file");
		}
		return grafo;
	}
	public static void main(String[]args) {
		File f = new File("/home/voidjocker/file");
		GrafoNonOrientatoColorato<Integer> grafo = prelevaIstanze(f);
		/*HashSet<Colore> BestS = new HashSet<Colore>();
		BestS.add(new Colore(2));
		BestS.add(new Colore(3));
		System.out.println(grafo.trovaTaglio(BestS));*/
		System.out.println(grafo.getComponenti());

	}
	
}
