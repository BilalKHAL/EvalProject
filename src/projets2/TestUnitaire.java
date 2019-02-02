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
import java.time.Instant;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yonah/Bilal
 */
public class TestUnitaire implements Runnable, Comparable {

    private String nomTest;
    private String nomPack;
    private boolean isCompil;
    private boolean isValide;
    private Duration m_tempsExecution;
    private String m_sortieExec;
    private String m_sortieErreur;
    private HashMap<String, String> m_testErreur;
    private int m_nbTestTotal;
    private int m_nbTestEchec;
    private static int nbReussiteTotal = 0;
    private static int nbEchecTotal = 0;
    private boolean thread = false ; 
/**
 * Construit un test unitaire avec les packages à ajouter 
 */
    public TestUnitaire(String nomtest, String nomPack) {
        this.nomTest = nomtest;
        this.nomPack = nomPack;
        this.isCompil = false;
        this.isValide = false;
        this.m_testErreur = new HashMap<String, String>();
    }
    
    
/**
 * définit ce qui va etre executé pour le TestUnitaire
 */
    @Override
    public void run() {
        ProcessBuilder terminal = new ProcessBuilder();

        terminal.command("javac", "-cp", nomPack + ":" + nomDossier.testU + "/*", nomTest);
        try {
            Process test = terminal.start();
            test.waitFor();
            InputStream i = test.getErrorStream();
            int nbOctet;

            if (i.available() != 0) {

                byte[] buffer = new byte[2048];
                String erreur = new String();
                while ((nbOctet = i.read(buffer, 0, 2048)) > 0) {
                    erreur = erreur.concat(new String(buffer, 0, nbOctet));
                }
                this.m_sortieErreur = erreur;

            } else {
                this.isCompil = true;

                terminal.directory(new File(nomDossier.testU.toString()));
                int indexNom = nomTest.split(File.separator).length;
                terminal.command("java", "-cp", ".:" + nomPack + ":" + "junit-4.10.jar", "org.junit.runner.JUnitCore", nomTest.split(File.separator)[indexNom - 1].replace(".java", ""));
                Process test2 = terminal.start();
                Instant start = Instant.now();
                test2.waitFor();
                Instant end = Instant.now();

                InputStream i2 = test2.getInputStream();
                int nbOctet2;

                if (i2.available() != 0) {

                    byte[] buffer = new byte[2048];
                    String sortie = new String();
                    while ((nbOctet2 = i2.read(buffer, 0, 2048)) > 0) {
                        sortie = sortie.concat(new String(buffer, 0, nbOctet2));
                    }
                    this.m_sortieExec = sortie;
                    this.getResult();
                    this.isValide = true;
                    this.m_tempsExecution = Duration.between(start, end);
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            thread = true ;
        } catch (InterruptedException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            thread = true ; 
        }
        thread = true ; 

    }
/**
 * Permet de vérifier si le Thread a été executé 
 */    
public boolean getThread(){
return this.thread ; 

}
/**
 * Permet de recuperer le resultat du projet
 */    
    public void getResult() {

        if (this.m_sortieExec != null) {
            String[] erreurByLigne = m_sortieExec.split("\n");
            String ligneRecap = erreurByLigne[erreurByLigne.length - 1];
            if (ligneRecap.contains("Failures")) {
                int nbTestTotal = Integer.parseInt(ligneRecap.substring(ligneRecap.indexOf("run:") + 5, ligneRecap.indexOf(",")));
                int nbTestEchec = Integer.parseInt(ligneRecap.substring(ligneRecap.indexOf("Failures:") + 10));
                this.m_nbTestEchec = nbTestEchec;
                this.m_nbTestTotal = nbTestTotal;

                for (int i = 1; i <= nbTestEchec; i++) {
                    for (int j = 0; j < erreurByLigne.length; j++) {
                        if (erreurByLigne[j].substring(0, 2).contains(String.valueOf(i) + ")")) {
                            String nomTestEchec = erreurByLigne[j].replace(String.valueOf(i) + ")", "").replaceAll("\n", "");
                            String nomErreur = erreurByLigne[j + 1];
                            this.m_testErreur.put(nomTestEchec, nomErreur);
                            break;
                        }
                    }
                }
            } else {
                this.m_nbTestEchec = 0;
                this.m_nbTestTotal = Integer.parseInt(this.m_sortieExec.substring(m_sortieExec.indexOf("(") + 1, m_sortieExec.indexOf("(") + 2));
            }
            TestUnitaire.nbEchecTotal += this.m_nbTestEchec;
            TestUnitaire.nbReussiteTotal += this.m_nbTestTotal - this.m_nbTestEchec;
        }

    }
    /**
 * Permet de specifier si un test a été executé ou non 
 */    

    public void setNoRun(boolean value) {
        this.isCompil = value;
        this.isValide = value;

    }
    /**
 * Permet de recuperer le nom d'un test unitaire
 */    

    public String getNom() {
        return this.nomTest.split(File.separator)[this.nomTest.split(File.separator).length - 1];

    }
    /**
 * Permet de recuperer le nombre de test
 */    

    public int getTestTotal() {
        return this.m_nbTestTotal;

    }
    /**
 * Permet de recuperer le nombre de test en echec
 */    

    public int getTestEchec() {
        return this.m_nbTestEchec;

    }
    /**
 * Permet de recuperer une liste détailée des tests avec leur erreur
 */    

    public HashMap<String, String> getTestEchecDet() {
        return this.m_testErreur;

    }
    
    /**
 * Permet de savoir si un test est valide et compilé 
 */    
    

    public boolean getIsValideAndIscompil() {
        return (this.isCompil && this.isValide);
    }
    /**
 * Renvoie le temps d'execution du test
 */    

    public String getTemps() {
        return Integer.toString(this.m_tempsExecution.getNano());
    }
/**
 * Permet de comparer des tests entre eux en fonction du nombre d'echec
 */    
    @Override
    public int compareTo(Object o) {
        if (o instanceof TestUnitaire) {

            return (this.getTestEchec() < ((TestUnitaire) o).getTestEchec()) ? 1 : (this.getTestEchec() == ((TestUnitaire) o).getTestEchec()) ? 0 : -1;

        } else {
            return -1;
        }

    }

}
