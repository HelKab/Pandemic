import java.util.*;

public class Joueur {
	private static int incr;
	private int num;
	private ArrayList<String> mainJoueur;
	private Ville placement;
	private ImageSimple pion;
	private int nbActions;
	private Role role;

	public Joueur(Ville placement) {
		this.num = ++incr;
		this.mainJoueur = new ArrayList<String>();
		this.placement = placement;
		this.nbActions = 4;
		this.pion = null;
	}

	public int getNum() { return this.num; }

	public ArrayList<String> getMainJoueur() { return mainJoueur; }

	public int getSizeMainJoueur() { return mainJoueur.size(); }

	public Ville getPlacement() { return this.placement; }

	public void newPlacement(Ville ville) { this.placement = ville; }

	public void setPion(Vue v, String chemin) {
		this.pion = new ImageSimple(chemin+"/data/images/pin"+this.num+".png",25,25);
        v.setImage(this.placement.getNom(),this.pion);
        v.deplace(this.pion,this.placement.getNom(),this.placement.getNom());
	}

	public ImageSimple getPion() { return this.pion; }

	public void setRole(Role role) { this.role = role;}

	public Role getRole() { return this.role; }

	public int getAction() { return this.nbActions; }

	public void reinitAction() { this.nbActions = 4; }

	public void downAction() { this.nbActions --; }

	// Demande d'une valeur au clavier et
    // vérification que cette valeur est bien
    // un entier compris entre 1 et n
	public static int entrerClavier(int n) {
        Scanner cl = new Scanner(System.in);
        int r=0;
        try {
            r = cl.nextInt();
            if (r<1 || r>n) {
                System.out.println(" |!| Entrez un entier entre 1 et "+n);
                return entrerClavier(n);
            } else {
                return r;
            }
        } catch (Exception e) {
            System.out.println(" |!| Entrez un entier entre 1 et "+n);
            return entrerClavier(n);
        }
    }

    // Paramètre choix du rôle par le joueur:
    public void choixRole() {
        int r=0;
        System.out.println("\nChoisir le rôle du joueur "+this.getNum());
        System.out.println(" 1: Le médecin");
        System.out.println(" 2: Le scientifique");
        System.out.println(" 3: L'expéditeur");
        r = entrerClavier(3);
        
        if (r == 1) {
            this.setRole(Role.Medecin);
        } else if( r == 2) {
            this.setRole(Role.Scientifique);
        } else {
            this.setRole(Role.Expediteur);
        }
    }

    public String afficheMain(Modele m) {
    	Iterator<String> it = mainJoueur.iterator();
    	String s = "\nMain du joueur "+this.getNum()+":\n";
    	while (it.hasNext()) {
    		Ville ville = m.getVille(it.next());
			s += "  - ("+ville.getCouleur()+") "+ville.getNom()+"\n";
		}
    	return s;
    }

    // ACTIONS DES JOUEURS :

    public boolean soinMaladie(Vue v, String chemin, Ville ville, Maladie maladie) {
    	if (maladie.getStade() > 0) {
    		// Le médecin guérit tous les stades d'une maladie:
    		if (this.role.equals(Role.Medecin)) {
    			while (maladie.getStade() != 0) { 
	    			maladie.downStade(1);
	    			ville.setImage(v,chemin);
	    		}
	    	// Si autre rôle, un seul stade est retiré
    		} else {
	            maladie.downStade(1);
	        }
	        this.downAction();
            ville.setImage(v,chemin);

            // Maladie redescendue à un stade de 0 dans cette ville
            if (maladie.getStade() == 0) {
                System.out.println("- Bravo! Vous avez soigné tous les habitants de "
                		+ville.getNom()+" de la maladie "+ maladie.getCouleur()+" -");
            }
            return true;
        }
        return false;
    }

    public boolean soin(Vue v, String chemin) {
        Ville ville = this.getPlacement();
        Maladie maladieZonale = ville.getMaladies().get(ville.getCouleur());
        // Soin de la maladie Zonale (correspondant à l'étiquette de la ville)
		if (this.soinMaladie(v,chemin,ville,maladieZonale)) { 
			return true;
		// Soin des autres maladies (Non Zonale)
		// On choisit la maladie avec le stade le plus élevé
        } else {
			Maladie maladieNonZonale = null;
			Iterator it = ville.getMaladies().keySet().iterator();

			while (it.hasNext()) {
				Maladie maladie = (Maladie)ville.getMaladies().get(it.next());
				if (maladieNonZonale == null || maladie.getStade() > maladieNonZonale.getStade()) {
			    	maladieNonZonale = maladie;
			    }
			}
			// Pas de malades à soigner
			if (maladieNonZonale.getStade() == 0) {
				System.out.println(" |!| Il n'y a pas d'habitants à soigner dans cette ville!");
        		return false;

			} else {
				this.soinMaladie(v,chemin,ville,maladieNonZonale);
				return true;
        	}
        }
    }

    public boolean deplacement(Modele m, Vue v) {
        Scanner cl = new Scanner(System.in);
        int n=0, r=0;

        Iterator<Integer> it = this.placement.getVoisins().iterator();
        ArrayList<Ville> voisines = new ArrayList<Ville>();

        System.out.println("\nChoisissez une ville voisine:");
        while (it.hasNext()) {
            Ville ville = m.getVille(it.next());
            n++;
            System.out.println("  "+n+": "+ville.getNom());
            voisines.add(ville);
        }

        r = entrerClavier(n);
        this.downAction();
        v.deplace(this.pion,this.placement.getNom(),voisines.get(r-1).getNom());
        this.newPlacement(voisines.get(r-1));
        return true;
    }

    public boolean volDirect(Modele m, Vue v) {
    	Scanner cl = new Scanner(System.in);
        int r=0, n=0;

        if (this.mainJoueur.isEmpty()) { 
        	System.out.println(" |!| Main vide - Pas de carte à jouer!");
        	return false;
        }
        // Choix d'une carte:
	    System.out.println("\nQuelle carte de votre main jouez-vous ?");
	   	Iterator<String> it = this.mainJoueur.iterator();

        while (it.hasNext()) {
            Ville ville = m.getVille(it.next());
            n++;
            System.out.println("  "+n+": "+ville.getNom());
        }

        r = entrerClavier(n);
        // Déplacer le joueur dans cette ville:
    	Ville ville = m.getVille(mainJoueur.get(r-1));
    	this.mainJoueur.remove(r-1);
        this.downAction();
        v.deplace(this.pion,this.placement.getNom(),ville.getNom());
        this.newPlacement(ville);
        return true;
    }

    public boolean volCharter(Modele m, Vue v) {
    	Scanner cl = new Scanner(System.in);
        int n=0, r=0;

        Iterator<String> it = this.mainJoueur.iterator();
        while (it.hasNext()) {
	        Ville carte = m.getVille(it.next());
        	if (carte.equals(this.placement)) {

        		// Choix d'une ville parmis (sauf celle de l'emplacement du joueur):
				Iterator<Case> itCases = m.getCases().iterator();
				ArrayList<Ville> villes = new ArrayList<Ville>();

			    while (itCases.hasNext()) {
			    	Case c = itCases.next();
			    	if (c instanceof Ville) {
			    		Ville ville = (Ville)c;
			    		if (ville.equals(carte)) { continue; }
			    		n++;
			    		System.out.println("  "+n+": "+ville.getNom());
			    		villes.add(ville);
	            	}
	            }

	            r = entrerClavier(n);
	            // Déplacer le joueur dans cette ville:
            	Ville ville = m.getVille(villes.get(r-1).getNom());
            	this.mainJoueur.remove(r-1);
                this.downAction();
                v.deplace(this.pion,this.placement.getNom(),ville.getNom());
                this.newPlacement(ville);
                return true;
            }
        }
        System.out.println(" |!| Vous ne pouvez pas faire de vol charter!");
        return false;
    }

    public void retirerCartesMain(Modele m, Couleur couleur) {
    	Iterator<String> itMain = mainJoueur.iterator();
    	Set<String> cartes = new HashSet<String>();
		while(itMain.hasNext()) {
		    Ville ville = m.getVille(itMain.next());
		    if (ville.getCouleur() == couleur) {
		    	cartes.add(ville.getNom());
    		}
    	}
    	mainJoueur.removeAll(cartes);
    }

    public boolean creerVaccin(Modele m, Vue v, String chemin, Pioche cartesInfection, HashMap<Couleur,Vaccin> vaccins) {
    	int nbCartes=5;
    	// Le scientifique peut créer un vaccin avec 4 cartes
    	if (this.role.equals(Role.Scientifique)) { nbCartes = 4;}

    	// On parcours les clés des vaccins (Bleue, Rouge, Jaune, Noire)
		Iterator it = vaccins.keySet().iterator();
		int cartes = 0;	
		while (it.hasNext()) {
		    Vaccin vaccin = (Vaccin)vaccins.get(it.next());
		    // Si le vaccin existe déjà, on passe à la clé suivante:
		    if (vaccin.getExiste()) { continue; } 

		    // Compter le nombre de cartes de la même couleur que la clé
		    Iterator<String> itMain = mainJoueur.iterator();
		    while(itMain.hasNext()) {
		    	Ville ville = m.getVille(itMain.next());
		    	if (ville.getCouleur() == vaccin.getCouleur()) {
		    		cartes ++;
		    	}
		    	// Si le nombre de carte est suffisant alors on peut créer un vaccin
		    	if (cartes >= nbCartes) {
			    	vaccin.trouver(v,chemin);
			    	System.out.println(" - Le vaccin de la maladie "+vaccin.getCouleur()+" vient d'être trouvé! -");
			    	retirerCartesMain(m,vaccin.getCouleur());
			    	return true;
			    }
		    }
			cartes = 0;
		}
		System.out.println(" |!| Impossible de créer un vaccin pour le moment.");
		return false;
    }

    // Action possible seulement par l'Éxpéditeur:
	public boolean deplacerJoueurAllie(Modele m, Vue v, Joueur j) {
		if (!this.role.equals(Role.Expediteur)) {
    		System.out.println(" |!| Vous ne pouvez pas effectuer cette action!");
    		return false;

    	} else {
	    	Scanner cl = new Scanner(System.in);
	        int n=0, r=0;

			Iterator<Case> itCases = m.getCases().iterator();
			ArrayList<Ville> villes = new ArrayList<Ville>();

			// Choix de la ville parmi toutes les villes de la cartes:
		    while (itCases.hasNext()) {
		    	Case c = itCases.next();
		    	if (c instanceof Ville) {
		    		Ville ville = (Ville)c;
		    		if (ville.equals(j.getPlacement())) { continue; }
		    		n++;
		    		System.out.println("  "+n+": "+ville.getNom());
		    		villes.add(ville);
	        	}
	        }

	        r = entrerClavier(n);
	        // Déplacement du joueur allié dans cette ville:
	    	Ville ville = m.getVille(villes.get(r-1).getNom());
	        this.downAction();
	        v.deplace(j.pion,j.getPlacement().getNom(),ville.getNom());
	        j.newPlacement(ville);
	        return true;
	    }
	}

    public boolean echangerUneCarte(Modele m, Joueur j) {
    	int n1=0, n2=0, r1=0, r2=0;

    	if (this.placement.equals(j.getPlacement())) {

    		if (this.mainJoueur.isEmpty() || j.mainJoueur.isEmpty()) { 
        		System.out.println(" |!| Main vide - Échange impossible!");
        		return false;
        	}

    		//Choix de la carte à donner:
	    	System.out.println("Quelle carte souhaitez-vous échanger ?");
	    	System.out.println("\nMain du joueur "+this.getNum()+":");
			Iterator<String> it1 = mainJoueur.iterator();
	    	while (it1.hasNext()) {
	    		Ville ville = m.getVille(it1.next());
	    		n1 ++;
				System.out.println("  "+n1+": ("+ville.getCouleur()+") "+ville.getNom());
			}
			r1 = entrerClavier(n1);

			// Choix de la carte à prendre:
			Iterator<String> it2 = j.mainJoueur.iterator();
			System.out.println("Quelle carte souhaitez-vous échanger ?");
			System.out.println("\nMain du joueur "+j.getNum()+":\n");
			while (it2.hasNext()) {
	    		Ville ville = m.getVille(it2.next());
	    		n2 ++;
				System.out.println("  "+n2+": ("+ville.getCouleur()+") "+ville.getNom());
			}
			r2 = entrerClavier(n2);

			// Échange de cartes:
    		this.mainJoueur.add(j.mainJoueur.get(r2-1));
    		j.mainJoueur.add(mainJoueur.get(r1-1));
    		j.mainJoueur.remove(r2-1);
    		this.mainJoueur.remove(r1-1);
	    	this.downAction();
	    	return true;

	    } else {
	    	System.out.println(" |!| Échange impossible. Les deux joueurs doivent être dans la même ville!");
	    	return false;
	    }
    }

    public void passerLeTour() {
    	while( this.nbActions != 0) {
    		this.downAction();
    	}
    }

	public String toString() {
		this.placement.toString();
		return "Joueur "+this.num+" ("+this.role+")";
	}
}