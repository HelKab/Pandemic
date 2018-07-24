public class Maladie {
    private final Couleur couleur;
    private int stade;

    public Maladie(Couleur couleur) {
        this.couleur = couleur;
        this.stade = 0;
    }

    public Couleur getCouleur() { return this.couleur; } 

    public int getStade() { return this.stade; }

    public void upStade(int i) { this.stade += i; }
    
    public void downStade(int i) { this.stade -= i; }

    public String toString() {
        String s = "";
        if (this.couleur.equals(Couleur.Bleue)) { s += "B:"; }
        else if (this.couleur.equals(Couleur.Rouge)) { s += "R:"; }
        else if (this.couleur.equals(Couleur.Jaune)) { s += "J:"; }
        else if (this.couleur.equals(Couleur.Noire)) { s += "N:"; }
        return s + this.getStade();
    }
}