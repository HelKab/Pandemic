public class Curseur {
	private static int incr;
	private int num;
	private ImageSimple image;
	private int nb;

	public Curseur(int nb) {
		this.num = ++incr;
		this.image = null;
		this.nb = nb;
	}

	public ImageSimple getImage() { return this.image; }

	public int getNb() { return this.nb; }

	public void setImage(Vue v, String chemin) {
		this.image = new ImageSimple(chemin+"/data/images/curseur"+this.num+".png",48,48);
		v.setImage(String.valueOf(this.nb), this.image);
		v.deplace(this.image, String.valueOf(this.nb), String.valueOf(this.nb));
	}

	public void deplaceCurseur(Vue v) { 
		this.nb ++;
		v.deplace(this.image, String.valueOf(this.nb), String.valueOf(this.nb));
	}
}