package tesiSamuelePaone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import grafiColorati.ArcoColorato;
import grafiColorati.Colore;
import grafiColorati.GrafoNonOrientatoColorato;

public final class MCCPAlgorithm {
	
	private MCCPAlgorithm() {}
	
	public static int MCCP(GrafoNonOrientatoColorato<Integer> grafo) {
		//---Implementazione timer---//
		Calendar calendar = Calendar.getInstance();
		Calendar now;
		calendar.add(Calendar.SECOND, 1);
		boolean endloop = false;
		//---fine timer---//
		int k; //variabile di controllo del neighborhood 
		
		//---Inizio algoritmo---//
		HashSet<Colore> BestS = generateInitialSolution(grafo);
		HashSet<Colore> C = grafo.insiemeColori();// C = { c(e) | e in E}
		int sizeC = C.size();
		int MaxNeighborhood = sizeC - BestS.size();//intorno locale
		HashSet<Colore> S = new HashSet<>();		
		
		while(!endloop) {
			
			// creazione soluzione candidata S
			
			newSolution(S,BestS,C,grafo);
			
			//while1 interno
			while(S.size() > BestS.size()) {
				BestS = new HashSet<>(S);
				MaxNeighborhood = C.size() - BestS.size();
				newSolution(S,BestS,C,grafo);
			}//fine while1 
			
			k = 1;
			//while2 interno
			while(k < MaxNeighborhood) {
				HashSet<Colore> S_1 = new HashSet<>(S);
				shake(S_1,S,k,C);
				if(grafo.componentiConnesse(S_1) == 1)fix(S_1,grafo);
				localSearch(S_1,C,grafo);
				if(S_1.size() > S.size()) {
					S = new HashSet<>(S_1);
					k = 1;
				}
				else k++;
			}//fine while2
			
			if(S.size() > BestS.size()) {
				BestS = new HashSet<>(S);
				MaxNeighborhood = C.size() -BestS.size();
			}
			
			// --- Implementazione timer ---//
			now = Calendar.getInstance();
			if(calendar.getTimeInMillis()-now.getTimeInMillis() <= 0)endloop = true;
		
		}// while principale
		HashSet<Colore> diff = differenza(C,BestS);
		System.out.println(diff+" insieme colori ottimo");
		System.out.println(grafo.componentiConnesse(diff)+"Componenti connesse C - BestS");
		System.out.println(grafo.componentiConnesse(BestS)+"Componenti connesse BestS");
		System.out.println(grafo.trovaTaglio(BestS, diff));
		return C.size() - BestS.size(); //valore funzione obiettivo
		
	}//MCCP
	public static HashSet<Colore> generateInitialSolution(GrafoNonOrientatoColorato<Integer> grafo){
		HashSet<Colore> BestS = new HashSet<>();
		HashSet<Colore> tuttiColori = grafo.insiemeColori();
		
		boolean endloop = false;
		HashSet<Colore> complementS;
		while(!endloop) {
			
			//trovo il complemento di S
			complementS = complemento(tuttiColori,BestS);
			//trovo colore c che massimizza il numero di componenti connesse di BestS U c
	
			int[] v = massimizzaComponentiConnesse(BestS,complementS,grafo);
			int compC = v[0];
			Colore c = new Colore(v[1]);
			//controllo che la soluzione raggiunta non sia connessa ovvero sia ammissibile
			if(compC>1) BestS.add(c);
			else endloop = true;
			
		}//endloop
		return BestS;
	}//generaSoluzioneIniziale
	private static void newSolution(HashSet<Colore> S,HashSet<Colore> BestS,HashSet<Colore> C,GrafoNonOrientatoColorato<Integer> grafo){
		S.clear();
		HashSet<Colore> complementBestS = complemento(C,BestS); //non viene mai modificato
		HashSet<Colore> complementBestSMenoS = differenza(complementBestS,S);
		HashSet<Colore> BestSMenoS;
		int compC = grafo.numNodi();
		while(compC > 1 && !complementBestSMenoS.isEmpty()) {
			
			//trovo colore che massimizza il numero di componenti di complement(BestS)\S unito c
			int [] vet = massimizzaComponentiConnesse(S,complementBestSMenoS,grafo);
			compC = vet[0];
			Colore c = new Colore(vet[1]);
			if(compC>1) {
				S.add(c);
				complementBestSMenoS = differenza(complementBestS,S);
			}
			else break;
		
		}//while 1
		compC = grafo.componentiConnesse(S);
		while( compC > 1) {
		
			BestSMenoS = differenza(BestS,S);
			//trovo colore che massimizza il numero di componenti di BestS\S unito c
			
			int [] vet = massimizzaComponentiConnesse(S,BestSMenoS,grafo);
			compC = vet[0];
			Colore c = new Colore(vet[1]);
			if(compC > 1)S.add(c);
			else break;
			
		}//while2
	
	}//newSolution
	private static void shake (HashSet<Colore> S_1, HashSet<Colore> S, int k,HashSet<Colore> universo) {
		// S insieme soluzione, k dimensione del vicinato
		HashSet<Colore> intersezioneS_1S;
		HashSet<Colore> intersezioneComplementS_1S;
		HashSet<Colore> complementS_1;
		HashSet<Colore> complementS;
		Random rand = new Random();
		for (int i = 0; i < k; i++) {
			double delta = rand.nextDouble();
			if (delta < 0.5 && S_1.size() > 0) {
				intersezioneS_1S = MCCPAlgorithm.intersezione(S_1,S);
				if(!intersezioneS_1S.isEmpty()) {	
					Object[] coloriIntersezione = intersezioneS_1S.toArray();
					//rimozione casuale di un colore in S intersect S_1
					int random = rand.nextInt(coloriIntersezione.length);
					Colore c = (Colore)coloriIntersezione[random];
					S_1.remove(c);intersezioneS_1S.remove(c);
				}
			}
			else {
				complementS_1 = MCCPAlgorithm.complemento(universo,S_1);
				complementS = MCCPAlgorithm.complemento(universo, S);
				intersezioneComplementS_1S = MCCPAlgorithm.intersezione(complementS_1,complementS);
				if(!intersezioneComplementS_1S.isEmpty()) {
					Object[] coloriIntersezioneCompl = intersezioneComplementS_1S .toArray();
					//aggiunta casuale di un colore preso da complement(S) intersect complement(S_1) in S'
					int random = rand.nextInt(coloriIntersezioneCompl.length);
					Colore c = (Colore)coloriIntersezioneCompl[random];
					S_1.add(c);
				}
			}
		}//for esterno
	}//shake
	private static void fix(HashSet<Colore> S_1,GrafoNonOrientatoColorato<Integer> grafo) {
		Object[] coloriS_1=S_1.toArray();
		Random rand = new Random();
		while(grafo.componentiConnesse(S_1)==1) {
			int random = rand.nextInt(coloriS_1.length);
			Colore c = (Colore)coloriS_1[random];
			S_1.remove(c);
		}
	}//fix
	private static void localSearch(HashSet<Colore> S_1,HashSet<Colore> tuttiColori,GrafoNonOrientatoColorato<Integer> grafo) {
		int compCMax = grafo.componentiConnesse(S_1);
		while(compCMax>1) {
			HashSet<Colore> complementS_1 = complemento(tuttiColori,S_1);
			int [] vet = massimizzaComponentiConnesse(S_1,complementS_1,grafo);// stesso di sopra ma il numero di componenti connesse relativo
			compCMax = vet[0];
			Colore cMax = new Colore(vet[1]);//colore che massimizza il numero di componenti connesse di S_1 U color correntemente
			if(compCMax > 1)S_1.add(cMax);
			else break;
		}
		return;
	}//localSearch
	private static HashSet<Colore> differenza(HashSet<Colore> S1,HashSet<Colore> S2){
		HashSet<Colore> ret = new HashSet<Colore>(S1);
		ret.removeAll(S2);
		return ret;
	}//differenza
	private static HashSet<Colore> intersezione(Set<Colore> S, Set<Colore> S_1){
		HashSet<Colore> intersezione = new HashSet<>();
		for(Colore c: S) 
			if(S_1.contains(c))
				intersezione.add(c);
		return intersezione;
	}//intersezione
	private static HashSet<Colore> complemento(Set<Colore> universo,Set<Colore> S){
		Set<Colore> complemento = new HashSet<>(universo);
		complemento.removeAll(S);
		return (HashSet<Colore>) complemento;
	}//intersezione
	private static int[] massimizzaComponentiConnesse(HashSet<Colore> S,HashSet<Colore>complementS,GrafoNonOrientatoColorato<Integer> grafo) {
		int [] ret= new int[2];
		ret[0] = 1;//componenti connesse massime correntemente
		ret[1] = 0;//colore che genera il numero massimo di componenti connesse correntemente
		for(Colore color:complementS) {
			S.add(color);
			int betterColor = grafo.componentiConnesse(S);
			S.remove(color);
			if(betterColor > ret[0]) {
				ret[1] = color.getColore();
				ret[0] = betterColor;
			}	
		}//for
		return ret;
	}//massimizzaComponentiConnesse
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
		int ris = 0;
		int i = 1;
		while(i<=10) {
			File f = new File("/home/voidjocker/Documenti/Tesi/INSTANCES2/instance.n=50_d=0.5_l=12.5instNum="+i);
			GrafoNonOrientatoColorato<Integer> grafo = prelevaIstanze(f);
			ris += MCCP(grafo);
			i++;
		}
		System.out.println((double)ris/10);
		/*GrafoNonOrientatoColorato<Integer> grafo = new GrafoNonOrientatoColorato<>();
		for(int i = 1; i <=8;i++)grafo.insNodo(i);
		grafo.insArco(new ArcoColorato<Integer>(1,2,new Colore(1)));
		grafo.insArco(new ArcoColorato<Integer>(1,6,new Colore(3)));
		grafo.insArco(new ArcoColorato<Integer>(1,4,new Colore(2)));
		grafo.insArco(new ArcoColorato<Integer>(2,4,new Colore(3)));
		grafo.insArco(new ArcoColorato<Integer>(2,3,new Colore(2)));
		grafo.insArco(new ArcoColorato<Integer>(3,4,new Colore(1)));
		grafo.insArco(new ArcoColorato<Integer>(3,8,new Colore(3)));
		grafo.insArco(new ArcoColorato<Integer>(4,5,new Colore(2)));
		grafo.insArco(new ArcoColorato<Integer>(7,5,new Colore(3)));
		grafo.insArco(new ArcoColorato<Integer>(7,6,new Colore(1)));
		grafo.insArco(new ArcoColorato<Integer>(7,8,new Colore(2)));
		grafo.insArco(new ArcoColorato<Integer>(6,5,new Colore(2)));
		grafo.insArco(new ArcoColorato<Integer>(8,5,new Colore(1)));
		System.out.println(MCCP(grafo));*/
	}//main
}//MCCPAlgorithm
