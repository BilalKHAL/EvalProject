
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	
	
	public static void main(String[] argv) throws IOException {
		GestionGroupe test = new GestionGroupe() ; 
		
		Scanner monUtilisateur = new Scanner(System.in) ; 
		
		while (true) {
                    
                    
			 int choix = test.afficherMenu() ;

			switch (choix) {
			
			
			
			case 1 :
                            if(test.lireFichier()){
                                test.afficherEtudiantPb();
                            
                            }
                            break ;
				
                                
				
			case 2 :
                            System.out.println("Saisir chemin emploi du temps :") ;
                           String cheminEdt = monUtilisateur.nextLine() ; 
                           System.out.println("Saisir chemin Etudiant :") ;
                            String cheminEtu = monUtilisateur.nextLine() ;
                            if(test.lireFichier(cheminEdt,cheminEtu)){
                                test.afficherEtudiantPb();
                            
                            }
                            break ;
                           
				
				
			case 3 :
                            if(test.affecter()){
                                
                                
                                test.afficherTdEtudiant();
                            }else{
                                System.out.println("Affectation echoue(Si une affectation a deja etait attribuer refaire lecture fichier) !\n ou Augmenter les bornes de Td ou vérifier fichier emploi du temps en ajoutant des creneaux pour les Td");
                            }
                            break ;
			case 4 : 
				test.changerBorne();
			
				break ;
                        case 5 :
                             System.out.println("Saisir Borne:") ;
                             try  {
                                 int num =monUtilisateur.nextInt();
                                 Td.m_borneMax = num ; 
                                 
                                 if(num>0){
                                     System.out.println("Entrez entier positif");
                                 
                                 }
                             }catch(NumberFormatException e){
                                 System.out.println("Entrez uniquement un chiffre !");
                                 monUtilisateur.next() ; 
                                 break ;
                             
                             }
                             break ;
                            
                        case 6 :
                            try{
                                test.genererNouveauPb();
                                System.out.println("Fichier de sortie generer verifier le dossier ou se trouve l'application : etuGene.csv , edtGene.csv");
                                
                            
                            
                            }catch(FileNotFoundException e){
                               System.out.println("Impossible d'ecrire les fichier de sortie vérifier l'emplacement ou se trouve l'application possible que le fichier soit deja ouvert ");
                            
                            }
                            
                            
			default : 
				break ; 
			
			}
		}
		
		
		
		
		
		
		
	}
	


}
