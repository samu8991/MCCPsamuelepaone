package grafiColorati;

public class Arco<N> {
	private N origine, destinazione;
	public Arco(N u, N v) {
		origine = u; destinazione = v;
	} // Costruttore
	public N getOrigine() { return origine; }
	public N getDestinazione() { return destinazione; }
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (!(o instanceof Arco)) return false;
		if (o == this) return true;
		@SuppressWarnings("rawtypes")
		Arco<N> a = (Arco)o;
		return (origine.equals(a.origine) && destinazione.equals(a.destinazione))||
				(origine.equals(a.destinazione) && destinazione.equals(a.origine));
	} // equals
	public int hashCode() {
		final int MOLT = 811;
		return origine.hashCode() * MOLT + destinazione.hashCode();
	} // hashCode
	public String toString() {
		return "<" + origine + ", " + destinazione + ">";
	} // toString
} // Arco
