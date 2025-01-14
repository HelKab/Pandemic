
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Vue implements VueGenerale{

	private final JFrame fen; // La fenetre
	private final JPanel pan; // l'image de fond
	
	private final HashMap<String,Integer[]> caseCoord; // l'association no Case, coordonnees
	
    // constructeur de la fenetre, avec une image de fond et la dimension de la fenetre
    public Vue(String bgroundPic, int x, int y){    	
		fen = new JFrame ("Pandemic");
                pan =new ImageSimple(bgroundPic, x,y);
		fen.setContentPane(pan); 
		fen.setSize(x,y);
		fen.setLocationRelativeTo(null);
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen.setLayout(null);
		fen.setVisible(true);

		// petit outil qui aide à trouver les coordonnees des cases 
		// à supprimer ensuite
				pan.addMouseListener(new MouseAdapter() {
                                @Override
				public void mousePressed(MouseEvent e) {
					System.out.println("{ "+e.getX() + "," + e.getY()+" },");
				}
				});
		// fin de l'outil 

		caseCoord = new HashMap<>();
    } 
	
	// methode qui associe une case a des coordonnees
	@Override
	public void setCase(String nom, int x, int y) {
		Integer t[] = new Integer[2];
		t[0] = x;
		t[1] = y;
		caseCoord.put(nom, t);
	}

	@Override
	public void setImage(String nom, ImageSimple p) {
		Integer[] rep = caseCoord.get(nom);
		if (rep == null){
			System.out.println ("La case "+ nom +" n'a pas encore ete declaree avec setCase, on considere qu'elle est en 0,0");
			p.setBounds(0, 0, p.largeur, p.hauteur);
		} else {
			p.setBounds(rep[0], rep[1], p.largeur, p.hauteur);
		}
		pan.add(p);
	}

	@Override
	public void removeImage(ImageSimple p) {
		pan.remove(p);
	}
	
	@Override
	public void deplace(ImageSimple p, String caseDepart, String caseArrivee) {
		Integer[] dep = caseCoord.get(caseDepart);
		if (dep == null){
			System.out.println ("La case  "+caseDepart+" n'a pas encore ete declaree avec setCase, on considere qu'elle est en 0,0");
			dep = new Integer[2];
			dep[0] = 0;
			dep[1] = 0;
		}
		Integer[] arr = caseCoord.get(caseArrivee);
		if (arr == null){
			System.out.println ("La case "+caseArrivee+" n'a pas encore ete declaree avec setCase, on considere qu'elle est en 0,0");
			arr=new Integer[2];
			arr[0] = 0;
			arr[1] = 0;
		}

		int x0 = dep[0],y0 = dep[1];
		int x1 = arr[0], y1 = arr[1];
		int nbStep = 100;
	
		try {
			for (int i=0; i<nbStep; i++){
				Thread.sleep(10);
				int x = x0+(x1-x0)*i/nbStep;
				int y = y0+(y1-y0)*i/nbStep;
				p.setBounds(x,y,50,50);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}