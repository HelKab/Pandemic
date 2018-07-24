import java.io.File;
import java.io.IOException;
import java.util.*;
import java.lang.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Prototype {

	private final Modele m;
	private final Vue v;
    private String chemin;
    private String villeDepart;
    private Joueur j1;
    private Joueur j2;
    private Curseur explosion;
    private Curseur tauxInfection;
    private Pioche cartesInfection;
    private Pioche cartesJoueur;
    // Clé = Couleur / Valeur = Vaccin
    private HashMap<Couleur,Vaccin> vaccins;
    
	public Prototype(Modele m, String villeDepart) {
		this.m = m;
        this.chemin = String.valueOf(Paths.get("").toAbsolutePath().getParent());
		this.v = new Vue(this.chemin+"/data/images/pandemicExemple.jpg",m.getL(),m.getH());
        this.villeDepart = villeDepart;
        this.j1 = new Joueur(m.getVille(villeDepart));
        this.j2 = new Joueur(m.getVille(villeDepart));
        this.explosion = new Curseur(0);
        this.tauxInfection = new Curseur(9);
        this.cartesInfection = new Pioche();
        this.cartesJoueur = new Pioche();
        this.vaccins = new HashMap<>();
        this.vaccins.put(Couleur.Bleue, new Vaccin(Couleur.Bleue));
        this.vaccins.put(Couleur.Rouge, new Vaccin(Couleur.Rouge));
        this.vaccins.put(Couleur.Jaune, new Vaccin(Couleur.Jaune));
        this.vaccins.put(Couleur.Noire, new Vaccin(Couleur.Noire));
	}

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

    // Paramètre Difficulté de la partie:
    public int difficultePartie() {
        int r=0;
        System.out.println(" 1: Partie facile");
        System.out.println(" 2: Partie difficile");
        r = entrerClavier(2);
        return r;
    }

    public void initMaladie() {

        Random r = new Random();
        int alea, s = 0;

        for(int i=0; i < 9; i++) {
            if (i%3==0) { s++; } // 3 villes pour chaque stade (1,2,3)

            alea = r.nextInt(cartesInfection.size() + 1);
            String nomVille = cartesInfection.get(alea);
            Ville ville = m.getVille(nomVille);
            Maladie maladie = ville.getMaladies().get(ville.getCouleur());
            maladie.upStade(s);
            ville.setImage(v,chemin);
            cartesInfection.remove(alea);
        }
    }

    public void initialisationJeu() {

        // Création des cases sur la Vue:
        m.initCasesModele(v); 
        
        System.out.println(" - Paramètres du jeu -");

        // Difficulté du jeu = Nombre de cartes épidemie:
        int d = difficultePartie();
        int nbCartesEpidemie;
        if (d == 1) { nbCartesEpidemie = 4; }
        else { nbCartesEpidemie = 6; }

        // Création des deux pioches:
        cartesJoueur.creationPioche(m,nbCartesEpidemie);
        cartesInfection.creationPioche(m,0);

        // Choix du rôle des joueurs:
        j1.choixRole();
        j2.choixRole();

        // Placement des pions:
        j2.setPion(v,chemin);
        j1.setPion(v,chemin);

        // Placement des fioles:
        Iterator it = vaccins.keySet().iterator();
        while (it.hasNext()) {
            Vaccin vaccin = (Vaccin)vaccins.get(it.next());
            vaccin.setImage(v,chemin);
        }

        // Placement des curseurs:
        tauxInfection.setImage(v,chemin);
        explosion.setImage(v,chemin);

        // Initialisation des maladies:
        initMaladie();

        // Joueurs piochent:
        piocher(j1,2);
        piocher(j2,2);
    }

    public static int askAction() {
        int r=0;
        System.out.println("  1: soin");
        System.out.println("  2: Déplacement simple");
        System.out.println("  3: Vol direct");
        System.out.println("  4: Vol charter");
        System.out.println("  5: créer un vaccin");
        System.out.println("  6: Échanger une carte");
        System.out.println("  7: Déplacer le joueur allié");
        System.out.println("  8: Passer le tour");
        r = entrerClavier(8);
        return r;
    }

    public void tourJoueur(Joueur j, Joueur ja) {
        System.out.println("\nTour: "+j.toString());
        System.out.println(j.afficheMain(m));
        int r=0;
        //Tant que le joueur peut effectuer une action :
        while (j.getAction() != 0) {

            System.out.println("\nActions restantes: "+j.getAction());
            j.afficheMain(m);
            r = this.askAction();
            if (r == 1) { j.soin(v,chemin); } 
            else if (r == 2) { j.deplacement(m,v); }
            else if (r == 3) { j.volDirect(m,v); }
            else if (r == 4) { j.volCharter(m,v); }
            else if (r == 5) { j.creerVaccin(m,v,chemin,cartesInfection,vaccins); }
            else if (r == 6) { j.echangerUneCarte(m,ja); }
            else if (r == 7) { j.deplacerJoueurAllie(m,v,ja); }
            else { j.passerLeTour(); }
        }
        j.reinitAction();
    }

    public void piocher(Joueur j, int nbCartesAPiocher) {
        Random r = new Random();
        int alea;

        for (int i = 0; i < nbCartesAPiocher; i++) {
            alea = r.nextInt(cartesJoueur.size());
            String carteTiree = cartesJoueur.get(alea);
            // Si Carte Épidémie tirée:
            if (carteTiree == "Épidémie" ) {
                System.out.println("\n - Carte ÉPIDEMIE tirée ! -");
                //Étape 1: Augmentation du taux de propagation des maladies
                tauxInfection.deplaceCurseur(v);

                //Étape 2: Infection et Explosion de la maladie d'une ville
                alea = r.nextInt(cartesInfection.size() + 1);
                String nomVille = cartesInfection.get(alea);
                Ville ville = m.getVille(nomVille);
                Maladie maladie = ville.getMaladies().get(ville.getCouleur());
                while (maladie.getStade() < 3) {
                    maladie.upStade(1);
                }
                ville.setImage(v,chemin);
                explosion(maladie,ville);

                //Étape 3: Pioche réinitialisée
                cartesInfection.clear();
                cartesInfection.creationPioche(m,0);
            // Sinon la carte est mise dans la main du joueur:
            } else {
                j.getMainJoueur().add(carteTiree);
            }
            cartesJoueur.remove(alea);
        }
        System.out.println(j.afficheMain(m));

        // Défausse de cartes si la main du joueur contient plus de 7 cartes:
        while (j.getSizeMainJoueur() > 7) {
            System.out.println("Veuillez défausser une carte:");
            Iterator<String> it = j.getMainJoueur().iterator();
            int n=0, rep=0;
            while (it.hasNext()) {
                n++;
                Ville ville = m.getVille(it.next());
                System.out.println("  "+n+": ("+ville.getCouleur()+") "+ville.getNom());
            }
            rep = entrerClavier(n);
            j.getMainJoueur().remove(rep-1);
        }
    }

    public boolean explosion(Maladie maladie, Ville ville) {
        if (maladie.getStade() == 3) {
            Iterator<Integer> it = ville.getVoisins().iterator();
            System.out.println("\n - EXPLOSION -  \nLes habitants de "+ville.getNom()+
                    " ont transmis leur maladie aux habitants des villes voisines!");
            explosion.deplaceCurseur(v);
            // La maladie se propage dans les villes voisines:
            while (it.hasNext()) {
                Ville voisine = m.getVille(it.next());
                Maladie propMaladie = voisine.getMaladies().get(ville.getCouleur());
                // Ceci permet d'éviter les explosions en chaîne
                // Et de limiter le nb de stade d'une maladie à 3
                if (propMaladie.getStade() < 3) {
                    propMaladie.upStade(1);
                    voisine.setImage(v,chemin);
                    System.out.println(voisine.toString());
                }
            }
            return true;
        }
        return false;
    }

    public void apparitionMaladie() {

        int taux = 2;
        if (tauxInfection.getNb() >= 12 && tauxInfection.getNb() < 14) { taux = 3;}
        else if (tauxInfection.getNb() >= 14) { taux = 4;}

        for(int i = 0; i < taux; i ++) {
            // Tirage d'une carte dans la pioche carteInfection:
            Random r = new Random();
            Integer alea = r.nextInt(cartesInfection.size() + 1);
            String nomVille = cartesInfection.get(alea);
            Ville ville = m.getVille(nomVille);
            Maladie maladie = ville.getMaladies().get(ville.getCouleur());

            //Vaccin trouvé, la maladie n'apparaît pas sur la map du jeu:
            if (vaccins.get(maladie.getCouleur()).getExiste()) {
                System.out.println("\n - Pas d'épidémie, le vaccin a été trouvé -  ");
                System.out.println(ville.toString());

            } else {
                if (maladie.getStade() < 3) {
                    maladie.upStade(1);
                    ville.setImage(v,chemin);
                    if (maladie.getStade() == 1) { System.out.println("\n - Nouvelle apparition -  "); } 
                    else { System.out.println("\n - Nouvelle propagation -  "); }
                    System.out.println(ville.toString());
                }
                explosion(maladie, ville);
            }
            cartesInfection.remove(alea);
        }
    }

    public boolean finJeu() {
        // Pioche Joueur est vide:
        if (this.cartesJoueur.isEmpty()) {
            System.out.println("\n - Plus de cartes joueurs ! -\n - Vous avez perdu la partie. -\n");
            return true;
        }
        // Curseur explosion au dernier cran:
        if (explosion.getNb() >= 8) {
            System.out.println("\n - Trop de maladies ont explosé ! -\n - Vous avez perdu la partie. -\n");
            return true;
        }
        int nbVaccins = 0;
        Iterator it = vaccins.keySet().iterator();
        while (it.hasNext()) {
            Vaccin vaccin = (Vaccin)vaccins.get(it.next());
            if (vaccin.getExiste() == true) {
                nbVaccins ++;
            }
        }
        // Tous les vaccins sont trouvés:
        if (nbVaccins == 4) {
            System.out.println("\n - Tout les vaccins ont été trouvé ! -\n - Vous avez gagné la partie -\n");
            return true;
        }
        return false;
    } 

	public void deroulementJeu() {

        initialisationJeu();

		int n = 0;
        System.out.println("--------------------------------------");
        while (this.finJeu() == false) {

            if (n%2 == 0) { 
                tourJoueur(j1,j2);
                piocher(j1,2);
                System.out.println("--------------------------------------");
                tourJoueur(j2,j1);
                piocher(j2,2);
                System.out.println("--------------------------------------");

            } else {
                System.out.println("\nTour de l'infecteur :");
                apparitionMaladie();
                System.out.println("\n--------------------------------------");
            }
            n ++;
        }
	}
}