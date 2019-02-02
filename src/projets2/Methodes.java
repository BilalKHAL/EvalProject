/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.util.ArrayList;

/**
 *
 * @author yonah/bilal
 */
public class Methodes {

    private String m_nom;
    private ArrayList<String> m_arguments;
    private String m_commentaire;

    public Methodes(String nom) {
        this.m_nom = nom;
        m_arguments = new ArrayList<String>();

    }
    /**
 * Permet d'ajouter un argument a une methode
 */

    public void addArgument(String argument) {
        m_arguments.add(argument);

    }
    
    /**
 * Permet de comparer deux methode en fonction de leur nom
 */

    @Override
    public boolean equals(Object Methode) {
        boolean test = false;

        if (Methode instanceof Methodes) {

            if (this.m_nom.equals((((Methodes) Methode).getNom()))) {

                if (m_arguments.isEmpty() && ((Methodes) Methode).getArguments().isEmpty()) {
                    return true;

                }

                for (String unArgument : m_arguments) {

                    for (String aComparer : ((Methodes) Methode).getArguments()) {

                        if (!aComparer.equals(unArgument)) {
                            test = false;

                        } else {
                            test = true;
                        }

                    }

                }
                return test;

            } else {
                return false;
            }

        } else {

            return false;

        }

    }
    /**
 * Renvoie le nom de la methode
 */

    public String getNom() {
        return this.m_nom;
    }

   /**
 * Renvoie l'ensemble des arguments d'une methode
 */
    public ArrayList<String> getArguments() {
        return this.m_arguments;
    }
    
    /**
 * Mise à jour du commentaire 
 */

    public void setCommentaire(String commentaire) {
        this.m_commentaire = commentaire;

    }
    /**
 * Permet de savoir si une methode est commentée
 */
    public boolean isCommented() {
        if (this.m_commentaire.length() > 20) {
            ArrayList<String> mesMots = new ArrayList<String>() ; 
            
            for(int i =0 ; i < this.m_commentaire.length() / 3 ; i++){
              String mots = this.m_commentaire.substring(3*i,3*(i+1)); 
              mesMots.add(mots) ;
            }
            String test = new String(); 
            
            for(String e : mesMots){
                if(e.equals(test)){
                    return false ; 
                }
                test = e ; 
            
            
            }
            
            String[] motsConsecutifs = this.m_commentaire.split(" ") ; 
            
            for(int i = 0 ; i < motsConsecutifs.length ; i++){
                
                if(i != motsConsecutifs.length - 1){
                  if(motsConsecutifs[i].equals(motsConsecutifs[i+1])){
                      return false ; 
                  }

                }
            
            }
            
            return true;

        } else {
            return false;

        }
    }

    /**
 *Renvoie la longueur d'un commentaire
 */
    public int lengthComm() {
        return this.m_commentaire.length();

    }

    /*  public  ArrayList<Method> returnMethode() throws ClassNotFoundException, MalformedURLException
                         
                 {
                     
                     File file = new File("/Users/bilalkhaldi/Desktop/ProjetS2");

                //convert the file to URL format
		URL url = file.toURI().toURL();
		URL[] urls = new URL[]{url};

                //load this folder into Class loader
		ClassLoader cl = new URLClassLoader(urls,getClass().getClassLoader());

                //load the Address class in 'c:\\other_classes\\'
		Class  cls = cl.loadClass("Participants");
                      
                
                
                ArrayList<Method> meth=new ArrayList<Method>();
                      
                      
                      Class b = new URLClassLoader(new URL[] {new File("Participants.java").toURI().toURL()}).loadClass("Participants") ; 
                     Class a = Class.forName("java.lang.String") ; 
        Method[] methode=b.getMethods() ; 
        int i=0;
        while (i<methode.length)
        {
         meth.add(methode[i]);
         System.out.println(methode[i]);
         i=i+1;
        }
        return null ; 
        
                 }
                     
     */
}
