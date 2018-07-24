import java.util.*;
import java.io.File;
import java.io.IOException;
import java.lang.*;

public class Pioche {

	private ArrayList<String> pioche;

	public Pioche() {
		this.pioche = new ArrayList<String>();
	}

    public boolean isEmpty() { return this.pioche.isEmpty();}

	public ArrayList<String> getPioche() { return this.pioche; }

	public int size() { return pioche.size(); }

	public String get(int i) { return pioche.get(i); }

	public void remove(int i) { pioche.remove(i); }

    public void clear() { pioche.clear(); }

    public void creationPioche(Modele m, int nbCartesEpidemie) {
        for (int i = 1; i < m.getNbNoeuds()+1; i++) {
            this.pioche.add(m.getVille(i).getNom());
        }
        for (int i = 0; i < nbCartesEpidemie; i++) { this.pioche.add("Épidémie"); }
    }

    public static Set<Integer> tiragesAleatoire(int nbCartesTotales, int nbCartesAPiocher) {
        Random r = new Random();
        Set<Integer>  nbAlea = new HashSet<Integer>();

        while ( nbAlea.size() < nbCartesAPiocher) {
            Integer next = r.nextInt(nbCartesTotales);
            nbAlea.add(next);
        }
        return nbAlea;
    }
}