/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Yonah/Bilal
 */
public class Classe {

    private String m_nom;
    private String logError;
    private boolean isValide;
    private boolean isWarning;
    private boolean isError;
    private Javadoc maJavaDoc;
    private HashMap<Methodes, Boolean> mesMethodes;

    /**
     *
     * Constructeur qui prend en argument le nom de la classe
     */
    public Classe(String nom) {
        if ((nom.contains(".java"))) {
            this.m_nom = nom;

        } else {
            this.m_nom = nom + ".java";

        }
        mesMethodes = new HashMap<Methodes, Boolean>();

    }

    /**
     *
     * Redefinition qui permet de comparer deux classes par leur nom
     */
    @Override
    public boolean equals(Object unObjet) {

        if (unObjet instanceof Classe) {
            if (((Classe) unObjet).toString().equals(this.m_nom)) {

                return true;

            } else {
                return false;

            }

        } else {
            return false;

        }

    }

    /**
     *
     * Methode qui renvoie le nom de la classe
     */
    @Override
    public String toString() {
        return this.m_nom;

    }

    /**
     *
     * Methode qui nous permet d'ajouter une erreur
     */
    public void addError(String error) {
        this.logError = error;
    }

    /**
     *
     * Methode qui permet de recuperer l'erreur
     */
    public String getError() {
        return this.logError;
    }

    /**
     *
     * Methode qui initialise la javadoc
     */
    public void initJavaDoc(String chemin) throws IOException, ClassNotFoundException {
        maJavaDoc = new Javadoc(chemin);
        maJavaDoc.isGenerated(true);

    }

    /**
     *
     * Methode qui initialise les methodes
     */
    public void initMethod(String monPackage, String allPack) throws MalformedURLException, ClassNotFoundException {
        
        
        if(maJavaDoc != null){
                 ArrayList<Methodes> Methodes = maJavaDoc.getListemeth();

        for (Methodes e : Methodes) {

            if (e.isCommented()) {
                mesMethodes.put(e, true);
            } else {
                mesMethodes.put(e, false);

            }

        }
        
        }

   

        /* String[] tabPack = allPack.split(":");
        URL[] urls = new URL[tabPack.length];

        for (int i = 0; i < tabPack.length; i++) {
            File file = new File(tabPack[i]);
            URL url = file.toURI().toURL();
            urls[i] = url;
        }

        ClassLoader cl = new URLClassLoader(urls);

        Class cls = cl.loadClass(m_nom.replace(".java", ""));
        Method[] methode = cls.getMethods();
        int i = 0;
        while (i < methode.length) {
            Methodes uneMethode = this.getMethodeFromString(methode[i].toString());

            if (uneMethode != null) {
                if (maJavaDoc.getListemeth().contains(uneMethode) && maJavaDoc.getListemeth().get(maJavaDoc.getListemeth().indexOf(uneMethode)).isCommented()) {
                    mesMethodes.put(maJavaDoc.getListemeth().get(maJavaDoc.getListemeth().indexOf(uneMethode)), true);

                } else {
                    if (maJavaDoc.getListemeth().contains(uneMethode)) {
                        mesMethodes.put(maJavaDoc.getListemeth().get(maJavaDoc.getListemeth().indexOf(uneMethode)), false);

                    }

                }

            }
            i = i + 1;
        }*/
    }

    /**
     *
     * Methode qui renvoie a partir de la javadoc le nom de la methode (avec
     * Package)
     */
    private Methodes getMethodeFromString(String methode) {

        if (methode.contains("throws")) { //On ne recupere pas les exceptions levée
            int indexFin = methode.indexOf("throws");
            methode = methode.substring(0, indexFin);

        }

        String[] tab = methode.split(" ");
        int index = tab.length - 1;

        String nom = tab[index].replace(m_nom.replace(".java", "") + ".", "");
        int indexFin = nom.indexOf("(");
        Methodes maMethode = new Methodes(nom.substring(0, indexFin));
        nom = nom.substring(indexFin);
        if (nom.contains(",")) {
            for (int i = 0; i < nom.split(",").length; i++) {
                maMethode.addArgument(nom.split(",")[i].replaceAll("\\(", "").replaceAll("\\)", ""));

            }
            return maMethode;

        } else {
            maMethode.addArgument(nom.replaceAll("\\(", "").replaceAll("\\)", ""));
            return maMethode;

        }

    }

    /**
     *
     * Renvoie le nombre de methodes documentées
     */
    public int getNbMethodeDoc() {
        Collection<Boolean> e = this.mesMethodes.values();
        int nbretour = 0;
        for (Boolean un : e) {

            if (un.booleanValue()) {
                nbretour++;
            }

        }
        return nbretour;

    }

    /**
     *
     * renvoie le nombre de methodes
     */
    public int getNbMethode() {
        return mesMethodes.size();

    }

    /**
     * Permet de definir si la classe est en warning
     */
    public void setWarning(boolean value) {
        this.isWarning = value;
    }

    /**
     *
     * Permet de definir si la classe est en erreur
     */
    public void setError(boolean value) {
        this.isError = value;

    }

    /**
     *
     * Permet de definir si la classe est valide
     */
    public void setValide(boolean value) {

        this.isValide = value;
    }

    /**
     *
     * Permet de savoir si la javadoc a ete generee
     */
    public boolean isJavadoc() {
        if (this.maJavaDoc != null) {
            return true;
        }
        return false;

    }

    /**
     * Permet de savoir si la classe est en erreur
     */
    public boolean isError() {
        return this.isError;
    }

    /**
     *
     * Permet de savoir si la classe est valide
     */
    public boolean isValide() {
        return this.isValide;
    }

    /**
     *
     * Permet de savoir si la classe est en warning
     */
    public boolean isWarnings() {
        return this.isWarning;
    }

    /**
     * Renvoie les methodes de la classe ainsi que leur état (documentée ou non)
     */
    public HashMap<Methodes, Boolean> getMethode() {
        return this.mesMethodes;

    }

}
