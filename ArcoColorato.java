

public class ArcoColorato<N> extends Arco<N> {
	private Colore colore;
	
	public ArcoColorato(N u, N v) {
		super(u, v);
		// TODO Auto-generated constructor stub
	}
	
	public ArcoColorato(N u, N v, Colore colore) {
		super(u,v);
		this.colore =colore;
	}
	public Colore getColore() { return colore; }
	public void setColore(Colore c) { colore = c; }
	public String toString() {
		return "<" + super.toString() + ", " + colore + ">";
	} // toString
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean equals(Object o) {
		if (!(o instanceof ArcoColorato)) return false;
		if (o == this) return true;
		ArcoColorato<N> a = (ArcoColorato)o;
		return super.equals(a) && colore.equals(a.colore);
	} // equals
	public int hashCode() {
		final int MOLT = 811;
		return super.hashCode() * MOLT + colore.hashCode();
	} // hashCode
}
