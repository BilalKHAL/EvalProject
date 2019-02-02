/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author bilalkhaldi
 */
public class ProjetS2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException {
                Console test4 = new Console();

        while(true){
        int choix = ProjetS2.afficherMenu() ; 
        switch(choix){
            case 1 :
                test4.unzipProject();
                break ; 
            case 2 : 
                if(test4.getIsUnzip()){
                  test4.compilation();
                }else{
                    System.out.println("Décompresser avant !");
                }
                break ; 
            case 3 : 
                test4.supprimerDossierSoumission(new File("UnZip"), 0);
                break ; 
            case 4 : 
                if(test4.getIsExecute()){
                    FTPs monFtp;
                    monFtp = new FTPs("moonkbr_1","password","ftp-moonkbr.alwaysdata.net");
                    monFtp.deleteFolderRecursive("/www");
                    System.out.println("Upload des resultat");

                    monFtp.uploadRecursive("HTML", "/www");
                }else{
                    System.out.println("Compiler avant !");
                }
                break ;
            case 5 : 
                if(test4.getIsExecute()){
                    test4.ecrireLog();
                }else{
                    System.out.println("Compiler avant !");
                    
                
                }
                break ;
        }}
     

    }
     public static int afficherMenu() {
		int choix = -1;
                Scanner m_user = new Scanner(System.in) ; 
		System.out.print(
				"1.Extraire Projet\n2.Tester les projets\n3.Supprimer fichier generer\n4.Envoyer les fichier sur le serveur web\n5.generer log projet(classe erreur compilation)\nChoix :");
		try {
			choix = m_user.nextInt();
			if (choix < 1 || choix > 5) {
				System.out.println("Choix ne correspond a aucune action du menu !");

				ProjetS2.afficherMenu();
			}

		} catch (InputMismatchException e) {
			System.out.println("Veuillez saisir uniquement des chiffres !");
			m_user.next();
			ProjetS2.afficherMenu();

		}
		return choix;

	}

}
