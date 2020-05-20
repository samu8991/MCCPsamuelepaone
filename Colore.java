

public class Colore {
	private int colore;
	
	public Colore() {
		colore = 0;
	}//Costruttore di default
	
	public Colore(int colore) {
		if(colore < 0)throw new IllegalArgumentException("Un colore è identificato da un intero positivo");
		this.colore = colore;
	}
	
	public int getColore() {
		return colore;
	}//getColore
	
	public void setColore(int colore) {
		this.colore = colore;
	}//setColore
	
	public boolean equals(Object o) {
		if(this == o)return true;
		if(!(o instanceof Colore))return false;
		Colore c = (Colore) o;
		return c.getColore() == colore;
	}//equal
	
	public int hashCode() {
		return Integer.hashCode(colore);
	}//hashCode;
	
	public String toString() {
		
		return Integer.toString(colore);
	}//toString
	
	
	
}//Colore 
