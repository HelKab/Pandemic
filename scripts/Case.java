import java.util.Iterator;

public class Case {
	protected final String nom;
	protected final int x, y;

	public Case(String nom, int x, int y) {
		this.nom = nom;
		this.x = x;
		this.y = y;
	}

	public String getNom() { return this.nom; }

	public int getX() { return this.x; }

	public int getY() { return this.y; }
}