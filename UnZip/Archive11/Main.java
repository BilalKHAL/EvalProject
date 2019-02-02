import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	
	
	public static void main(String[] argv) throws IOException {
		GestionGroupe test = new GestionGroupe() ; 
		int choix = test.afficherMenu() ;
		Scanner testd = new Scanner(System.in) ; 
		
		switch (choix) {
		
		
		case 1 :
			test.lireFichier();
			break;
		case 2 :
			String ligne = testd.nextLine() ;
			String ligne2 = testd.nextLine();
			test.lireFichier(ligne, ligne2);
			break ; 
		}
		
		
		
		
		
	}
	


}
