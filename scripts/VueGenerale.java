
public interface VueGenerale {

	public void setCase(String nom, int x, int y);

	public void setImage(String nom, ImageSimple p);

	public void removeImage(ImageSimple p);
	
    public void deplace(ImageSimple p, String caseDepart, String caseArrivee);
}
