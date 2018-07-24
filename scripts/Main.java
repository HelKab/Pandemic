import java.io.File;
import java.io.IOException;
import java.util.*;
import java.lang.*;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("\t---------------");
        System.out.println("\tP A N D E M I C");
        System.out.println("\t---------------");

        File file = new File("../data/datasGame.txt");
        Modele m = new Modele(file);
        m.constructionModele();        

        Prototype p = new Prototype(m,"Paris");
        p.deroulementJeu();
    }
}