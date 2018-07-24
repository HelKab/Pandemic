import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.regex.*;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Modele {

	private final File file;
	private static int nbNoeuds, l, h;
	private Set<Case> cases;

	public Modele(File file) {
		this.file = file;
		this.cases = new LinkedHashSet<Case>();
	}

	public int getNbNoeuds() { return this.nbNoeuds; }

	public int getL() { return this.l; }

	public int getH() { return this.h; }

	public Set<Case> getCases() { return this.cases; }

	// Obtenir la ville du Modèle à partir de son nom:
	public Ville getVille(String nomVille) {

		Iterator<Case> it = this.cases.iterator();

	    while (it.hasNext()) {
	    	Case c = it.next();
	    	if (c instanceof Ville) {
	    		Ville v = (Ville)c;
	    		if (nomVille.compareTo(v.getNom())==0) {
	    			return v;
	    		}
	    	}
	    }
	    // La ville n'existe pas:
	    return new Ville("blablabla", Couleur.Noire, 0, 0);
	}

	// Obtenir la ville du Modèle à partir de son numéro:
	public Ville getVille(int numVille) {

		Iterator<Case> it = this.cases.iterator();

	    while (it.hasNext()) {
	    	Case c = it.next();
	    	if (c instanceof Ville) {
	    		Ville v = (Ville)c;
	    		if (numVille == v.getNum()) {
	    			return v;
	    		}
	    	}
	    }
	    // La ville n'existe pas:
	    return new Ville("blablabla", Couleur.Noire, 0, 0);
	}

	// Lecture du fichier et renvoie de son contenu en String:
	public static String readStream(File file) throws IOException {

		FileInputStream fd = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fd.read(data);
		String str = new String(data, "UTF-8");
		fd.close();
		return(str);
	}

	// Mise en place du modèle à partir du fichier donné en paramètre:
	public void constructionModele() throws IOException {

    	String contents = readStream(file);

    	// Parsing du fichier en utilisant des expressions régulières:
		Pattern line = Pattern.compile("\\n");
		Pattern blank = Pattern.compile("\\s");
		Pattern desc = Pattern.compile("([a-zA-Z]+(\\s|)[a-zA-Z]+(\\s|)[a-zA-Z]+)\\s{1,5}#\\s{1,5}([a-zA-Z]+)\\s{1,5}#\\s{1,5}\\{\\s([0-9]{2,4}),([0-9]{2,4})\\s\\}");
		Pattern interac = Pattern.compile("([a-zA-Z]+(\\s|)[a-zA-Z]+(\\s|)[a-zA-Z]+)\\s{1,5}##\\s{1,5}([a-zA-Z]+(\\s|)[a-zA-Z]+(\\s|)[a-zA-Z]+)");
		Pattern curseur = Pattern.compile("([0-9]+)\\s#\\s\\{\\s([0-9]{2,4}),([0-9]{2,4})\\s\\}");
		Pattern vaccin = Pattern.compile("([A-Za-z]{5})\\s#\\s\\{\\s([0-9]{2,4}),([0-9]{2,4})\\s\\}");

		String[] items = line.split(contents); // Tableau des lignes du fichier
		String lh[] = blank.split(items[1]);

		this.nbNoeuds = Integer.parseInt(items[0]); // Nombre de villes
		this.l = Integer.parseInt(lh[0]); // Longueur de la Vue
		this.h = Integer.parseInt(lh[1]); // Hauteur de la Vue

		for(int i=2; i<items.length; i++) {
			Matcher mD = desc.matcher(items[i]);
			Matcher mI = interac.matcher(items[i]);
			Matcher mC = curseur.matcher(items[i]);
			Matcher mV = vaccin.matcher(items[i]);

			// Cases ville:
			if (mD.find()) {
				Ville newVille = new Ville(mD.group(1),
							Couleur.valueOf(mD.group(4)),
							Integer.valueOf(mD.group(5)),
							Integer.valueOf(mD.group(6)));
	            this.cases.add(newVille);
			}

			// Ajout des voisins de chaque ville:
			if (mI.find()) {
				Ville ville1 = getVille(mI.group(1));
				Ville ville2 = getVille(mI.group(4));
				this.getVille(mI.group(1)).addVoisin(this.getVille(mI.group(4)));
	        }

	        // Cases emplacement curseurs:
	        if (mC.find()) {
				Case c = new Case(mC.group(1),
						Integer.valueOf(mC.group(2)),
						Integer.valueOf(mC.group(3)));
				this.cases.add(c);
	        }

	        // Cases emplacement fioles:
	       	if (mV.find()) {
				Case c = new Case(mV.group(1),
						Integer.valueOf(mV.group(2)),
						Integer.valueOf(mV.group(3)));
				this.cases.add(c);
	        } 

		}
	}

	// Initialisation de toutes les cases du Modèle sur la Vue:
	public void initCasesModele(Vue v) {
        Iterator<Case> it = this.cases.iterator();
        while (it.hasNext()) {
            Case c = it.next();
            v.setCase(c.getNom(),c.getX(),c.getY());
        }
    }

	public String toString() {
		Iterator<Case> it = this.cases.iterator();
		String s = "nbNoeuds : "+nbNoeuds+"\nVilles : \n";
		while (it.hasNext()) {
			s += it.next()+"\n";
		}
		return s;
    }
}