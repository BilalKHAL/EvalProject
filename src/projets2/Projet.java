/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.io.File;
import java.io.FileNotFoundException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yonah/Bilal
 */
public class Projet implements Comparable {

    private static int nbProjet = 0;
    private int id, nbClasseErreur = 0, nbClasseWarnings = 0, nbClasseValide = 0, nbClasseTotal = 0;
    private File m_myFolder;
    private Package m_racinePackage;
    private ArrayList<TestUnitaire> m_mesTest;
    private Benchmark m_mesScores;
    private boolean m_strucutre = true;
    private HTMLEtudiant monHtml;
    private HashMap<Classe, Boolean> strucutre;

    
    /**
 * Construire un projet avec le dossier decompressé associé
 */
    public Projet(File myFolder) {

        m_myFolder = myFolder;
        m_racinePackage = this.buildPackage(myFolder, new Package(""));
        m_mesTest = new ArrayList<TestUnitaire>();
        this.strucutre = new HashMap<Classe, Boolean>();
        monHtml = new HTMLEtudiant(this.getNom());
        id = nbProjet;
        nbProjet++;

    }
/**
 *r Renvoie les tests unitaires passés par le projet
 */
    public ArrayList<TestUnitaire> getTest() {
        return this.m_mesTest;

    }
/**
 * renvoie le Benchmark passé par le projet
 */    
    public Benchmark getBench(){
        return this.m_mesScores ; 
    
    
    }
/**
 * Permet de vérifier la strucutre du projet en fonction de la structure passée en argument 
 */
    public void verifierStrucure(Package strucutre ,String parent) {

        for (Classe a : strucutre.getClasse()) {

            if (!Files.exists(Paths.get(m_myFolder.getAbsolutePath() + File.separator +parent+File.separator + a.toString()))) {
                this.strucutre.put(a, false);
                this.m_strucutre = false;
            } else {
                this.strucutre.put(a, true);

            }
        }

        ArrayList<Package> racine = strucutre.getPack();
        for (int i = 0; i < racine.size(); i++) {
            Package actuel = racine.get(i);

            this.verifierStrucure(actuel,parent.concat(strucutre.getNom()) + File.separator);

        }
    }
/**
 * Permet d'initialiser le nombre de classe en erreur/warnings/valide 
 */
    public void getClasseErreur(Package strucutre) {

        for (Classe a : strucutre.getClasse()) {
            if (a.isError()) {
                this.nbClasseErreur++;
                this.nbClasseTotal++;

            } else if (a.isWarnings()) {
                this.nbClasseWarnings++;
                this.nbClasseTotal++;

            } else {
                this.nbClasseValide++;
                this.nbClasseTotal++;

            }

        }

        ArrayList<Package> racine = strucutre.getPack();
        for (int i = 0; i < racine.size(); i++) {
            Package actuel = racine.get(i);

            this.getClasseErreur(actuel);

        }
    }
/**
 * Permet de construire les packages/classes du projet 
 */
    private Package buildPackage(File unFile, Package monPackage) {

        if (unFile.isDirectory()) {
            Package nouveau = new Package(unFile.getAbsolutePath());
            monPackage.ajouterPackage(nouveau);
            monPackage = nouveau;
            File[] enfant = unFile.listFiles();
            for (int i = 0; i < enfant.length; i++) {
                File enfantActu = enfant[i];

                this.buildPackage(enfantActu, monPackage);
            }

        } else {
            if (unFile.getName().contains(".java") && !(unFile.getName().contains("_"))) {
                monPackage.ajouterClasse(new Classe(unFile.getName()));

            }

        }
        return monPackage;
    }
    
    /**
 * Renvoie le package racine d'un projet
 */

    public Package getPackage() {
        return this.m_racinePackage;

    }
/**
 * Renvoie le dossier du projet
 */
    public File getFolder() {
        return this.m_myFolder;
    }
/**
 * Constitue une chaine de caractere avec les chemins de tous les packages du projet
 */
    public String allPackage(String Package, Package unPackage) {

        if (Package.length() == 0) {
            Package = Package.concat(unPackage.getNom());

        } else {
            Package = Package.concat(":" + unPackage.getNom());

        }

        ArrayList<Package> mesEnfants = unPackage.getPack();

        for (int i = 0; i < mesEnfants.size(); i++) {
            Package enfant = mesEnfants.get(i);
            Package = this.allPackage(Package, enfant);

        }
        return Package;

    }
/**
 * Ajoute un test unitaire au projet 
 */
    public void addTestUnitaire(String nom) {
        TestUnitaire unTest = new TestUnitaire(nom, this.allPackage("", m_racinePackage));

        Thread th1 = new Thread(unTest);
       // try {
            th1.start();
           // th1.join(120000000);

       // } catch (InterruptedException ex) {
       //     unTest.setNoRun(false);
      //  }

        m_mesTest.add(unTest);
    }
/**
 * Ajoute un Benchmark au projet
 */
    public void addBenchmark(String nom) {
        Benchmark unTest = new Benchmark(nom, this.allPackage("", m_racinePackage));
        Thread th1 = new Thread(unTest);
        try {
            th1.start();
            th1.join(120000000);
       } catch (InterruptedException ex) {
            unTest.setNoRun(false);
       }
        this.m_mesScores = unTest;

    }
/**
 * renvoie le nom du projet
 */
    public String getNom() {
        return this.m_myFolder.toString().split("/")[1];

    }
/**
 * Renvoie la page HTML du projet
 */
    public HTMLEtudiant getHtml() {
        return this.monHtml;
    }
/**
 * Renvoie l'identifiant d'un projet
 */
    public int getId() {
        return this.id;
    }
/**
 * Renvoie le score du projet
 */
    public float getScore() {

        if (this.m_mesScores != null) {

            return Float.parseFloat(this.m_mesScores.getScore());

        } else {
            return 0;
        }

    }
/**
 * Permet de comparer un projet a un autre en fonction du score
 */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Projet) {

            return (this.getScore() < ((Projet) o).getScore()) ? 1 : (this.getScore() == ((Projet) o).getScore()) ? 0 : -1;

        } else {
            return -1;
        }

    }
/**
 * Ajoute un Benchmark au projet
 */
    public int getNbMethodeDoc(Package unPackage, int nbMethodeDoc) {
        for (Classe a : unPackage.getClasse()) {
            nbMethodeDoc += a.getNbMethodeDoc();

        }

        ArrayList<Package> racine = unPackage.getPack();
        for (int i = 0; i < racine.size(); i++) {
            Package actuel = racine.get(i);

            nbMethodeDoc = this.getNbMethodeDoc(actuel, nbMethodeDoc);

        }
        return nbMethodeDoc;

    }
/**
 * Nombre de methode total du projet
 */
    public int getNbMethodeTotal(Package unPackage, int nbMethodeDoc) {
        for (Classe a : unPackage.getClasse()) {
            nbMethodeDoc += a.getNbMethode();

        }

        ArrayList<Package> racine = unPackage.getPack();
        for (int i = 0; i < racine.size(); i++) {
            Package actuel = racine.get(i);

            nbMethodeDoc = this.getNbMethodeTotal(actuel, nbMethodeDoc);

        }
        return nbMethodeDoc;

    }
/**
 * Renvoie les classes de la structure ainsi qu'une indication sur leur presence
 */
    public HashMap<Classe, Boolean> getStruct() {
        return this.strucutre;

    }
/**
 * renvoie le nombre de classe en erreur 
 */
    public int getClasseErreur() {
        return this.nbClasseErreur;

    }
/**
 * renvoie le nombre de classe en warnings
 */
    public int getClasseWarnings() {
        return this.nbClasseWarnings;

    }
/**
 * renvoie le nombre de classe valide 
 */
    public int getClasseValide() {
        return this.nbClasseValide;

    }
/**
 * renvoie le nombre de classe total 
 */
    public int getClasseTotal() {
        return this.nbClasseTotal;

    }
/**
 * Indique si la structure a été respectée
 */
    public boolean structureIsValide() {
        return this.m_strucutre;
    }

}
