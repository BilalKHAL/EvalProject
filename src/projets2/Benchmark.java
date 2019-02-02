/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yonah/Bilal
 */
public class Benchmark implements Runnable {

    private String nomTest;
    private boolean isExecute;
    private String pack;
    private String m_score;
    private String sortie;
    private boolean thread = false;

    /**
     *
     * Constructeur qui prend en argument le nom du test ainsi que les packages
     * necessaire a la compilation
     */
    public Benchmark(String nom_Test, String p) {
        this.nomTest = nom_Test;
        this.isExecute = false;
        this.pack = p;
        this.sortie = new String();
    }

    /**
     *
     * Traitement lors de l'execution d'un Benchmark
     */
    @Override
    public void run() {

        ProcessBuilder terminal = new ProcessBuilder();

        terminal.command("javac", "-cp", pack, nomTest);
        try {
            Process test = terminal.start();
            test.waitFor();
            InputStream i = test.getErrorStream();
            int nbOctet;

            if (i.available() != 0) {//Si erreur compilation

                byte[] buffer = new byte[2048];
                String erreur = new String();
                while ((nbOctet = i.read(buffer, 0, 2048)) > 0) {
                    sortie = sortie.concat(new String(buffer, 0, nbOctet));
                }
                this.isExecute = false;
            } else {

                terminal.directory(new File(nomDossier.testScore.toString()));
                int indexNom = nomTest.split(File.separator).length;

                terminal.command("java", "-cp", ".:" + pack, nomTest.split(File.separator)[indexNom - 1].replace(".java", ""));
                Process test2 = terminal.start();
                test2.waitFor();
                InputStream i2 = test2.getInputStream();
                int nbOctet2;

                if (i2.available() != 0) {
                    this.isExecute = true;

                    byte[] buffer = new byte[2048];
                    String resultat = new String();
                    while ((nbOctet2 = i2.read(buffer, 0, 2048)) > 0) {
                        resultat = resultat.concat(new String(buffer, 0, nbOctet2));
                    }
                    String[] resultByLigne = resultat.split("\n");

                    int debScore = resultByLigne[resultByLigne.length - 1].indexOf("PT") + 2; //recuperation de l'index du debut du score
                    int finScore = resultByLigne[resultByLigne.length - 1].indexOf("S");
                    this.m_score = resultByLigne[resultByLigne.length - 1].substring(debScore, finScore);
                } else {
                    InputStream i3 = test2.getErrorStream();
                    byte[] buffer = new byte[2048];
                    String resultat = new String();
                    while ((nbOctet2 = i2.read(buffer, 0, 2048)) > 0) {
                        resultat = resultat.concat(new String(buffer, 0, nbOctet2));
                    }
                    this.sortie = resultat;

                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            thread = true;
        } catch (InterruptedException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            thread = true;
        }
        thread = true;

    }

    /**
     *
     * Méthode qui renvoie le score du benchmark
     */
    public String getScore() {

        if (this.m_score != null) {
            return this.m_score;

        } else {
            return "120";

        }

    }

    /**
     *
     * Methode qui permet de definir l'execution ou non d'un Benchmark
     */
    public void setNoRun(boolean value) {
        this.isExecute = value;

    }

    /**
     *
     * Attribut qui nous permet de savoir si nous avons executé notre methode
     * run de l'interface Runnable
     */

    public boolean getThread() {
        return this.thread;
    }

}
