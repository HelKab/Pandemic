public class Vaccin {
	private final Couleur couleur;
	private boolean existe;
	private ImageSimple image;

	public Vaccin(Couleur couleur) {
		this.couleur = couleur;
		this.existe = false;
		this.image = null;
	}

	public Couleur getCouleur() { return this.couleur; } 

	public boolean getExiste() { return this.existe; }

	public void setImage(Vue v, String chemin) {
		this.image = new ImageSimple(chemin+"/data/images/"+String.valueOf(this.couleur)+"0.png",41,41);
		v.setImage(String.valueOf(this.couleur), this.image);
		v.deplace(this.image, String.valueOf(this.couleur), String.valueOf(this.couleur));
	}

	public void trouver(Vue v, String chemin) { 
		this.existe = true;
		v.removeImage(this.image);
		this.image = new ImageSimple(chemin+"/data/images/"+String.valueOf(this.couleur)+"1.png",41,41);
		v.setImage(String.valueOf(this.couleur), this.image);
		v.deplace(this.image, String.valueOf(this.couleur), String.valueOf(this.couleur));
	}
}