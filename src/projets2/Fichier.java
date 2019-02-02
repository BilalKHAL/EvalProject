/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author bilalkhaldi
 */
public class Fichier {
/**
 * Permet de lire un fichier 
 */
    public static String lireFichier(String chemin) throws FileNotFoundException, IOException {
        BufferedReader a = new BufferedReader(new FileReader(chemin));
        int nbOctet;
        char[] buffer = new char[2048];
        String retour = new String();
        while ((nbOctet = a.read(buffer)) != -1) {
            retour = retour.concat(new String(buffer));
        }
        return retour;

        //InputStream i = new InputStream(new File("/Users/bilalkhaldi/Desktop/ProjetS2/ProjetS2/SoumissionUnZip/30-seconds-of-java8-master2/30-seconds-of-java8-master/src/main/java/snippets/JAVA/snippets/Snippets.html"));
    }

}
