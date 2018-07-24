import java.util.*;
import java.util.Iterator;

public class Ville extends Case {
	private static int incr;
	private final int num;
	private final Couleur couleur;
	private HashSet<Integer> voisins;
	// Clé = Couleur / Valeur = Maladie
	private HashMap<Couleur,Maladie> maladies; 
	private ImageSimple image;

	public Ville(String nom, Couleur couleur, int x, int y) {
		super(nom, x, y);
		this.num = ++incr;
		this.couleur = couleur;
		this.voisins = new HashSet<Integer>();
		this.maladies = new HashMap<>();
		this.maladies.put(Couleur.Jaune, new Maladie(Couleur.Jaune));
		this.maladies.put(Couleur.Rouge, new Maladie(Couleur.Rouge));
		this.maladies.put(Couleur.Bleue, new Maladie(Couleur.Bleue));
		this.maladies.put(Couleur.Noire, new Maladie(Couleur.Noire));
		this.image = null;
	}

	public int getNum() { return this.num; }

	public Couleur getCouleur() { return this.couleur; }

	public HashSet<Integer> getVoisins() { return this.voisins; }

	// Ajout des voisins d'une ville v
	// Ajout ville v dans les voisins de chaque voisin de v
	public boolean addVoisin(Object o) {
		if (o instanceof Ville) {
			Ville ville = (Ville)o;
			if (ville.num != this.num) {
				this.voisins.add(ville.num);
				ville.voisins.add(this.num);
				return true;
			}
		}
		return false;
	}

	public Map<Couleur,Maladie> getMaladies() { return this.maladies; }

	public ImageSimple getImage() { return this.image; }

	// L'image (chiffre) correspond à la somme des 
	// stades de toutes les maladies d'une ville 
    public void setImage(Vue v, String chemin) {
        if (this.image != null) {
            v.removeImage(this.image);
        }

        Integer sumStades = 0;
    	Iterator it = this.maladies.keySet().iterator();
		 
		while (it.hasNext()) {
		    Maladie maladie = (Maladie)this.maladies.get(it.next());
		    sumStades += maladie.getStade();
		}
        this.image = new ImageSimple(chemin+"/data/images/"+sumStades+".png",20,25);
        v.setImage(this.nom,this.image);
        v.deplace(this.image,this.nom,this.nom);
    }

	public String toString() {
		String s = "("+this.getCouleur()+") "+this.nom+" - Maladies: ";
		Iterator it = this.maladies.keySet().iterator();	
		while (it.hasNext()) {
		    Maladie maladie = (Maladie)this.maladies.get(it.next());
		    s += maladie.toString()+" ";
		}
		return s;
	}
}