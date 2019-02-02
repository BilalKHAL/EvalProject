/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.util.ArrayList;

/**
 *
 * @author Yonah/Bilal
 */
public class Package {

    private String m_nom;
    private ArrayList<Classe> m_classePack;
    private ArrayList<Package> m_sousPack;
/**
 * construire un Package a partir de son nom
 */
    public Package(String nom) {
        this.m_nom = nom;
        m_classePack = new ArrayList<Classe>();
        m_sousPack = new ArrayList<Package>();
    }
/**
 * Ajoute un sous package a ce package
 */
    public void ajouterPackage(Package unPackage) {
        m_sousPack.add(unPackage);

    }
/**
 * Ajoute une classe au package 
 */
    public void ajouterClasse(Classe uneClasse) {
        m_classePack.add(uneClasse);

    }
/**
 * Permet de récupérer les sous packages du package
 */
    public ArrayList<Package> getPack() {
        return this.m_sousPack;
    }
/**
 * Permet de récupérer les classes du package
 */
    public ArrayList<Classe> getClasse() {
        return this.m_classePack;
    }
    /**
 * renvoie le nom du Package
 */

    public String getNom() {
        return this.m_nom;

    }

    /* @Override
    public boolean equals(Object otherPackage){
        
        for (Package unPackage : m_sousPack){
            
            for(Package deuxieme : ((Package)otherPackage).getPack()){
                
                if(unPackage.getNom().equals(((Package)otherPackage).getNom())){
                    
                
                
                
                }
            
            
            }
        
        
        }
        
        
    
    
    }
    
    
    public ArrayList<Package> searchLevelPackage(Package unPackage , int level) {
        
        for(int i=0 ; i < unPackage.getPack().size() ; i++){
            this.searchLevelPackage(unPackage.getPack().get(i), level) ; 
        }
        return unPackage ; 
    }
    
    public boolean checkExistPackage(Package un , Package deux){
        boolean search = false ; 
        for (Package premier : un.getPack()){
            
            for(Package deuxieme : deux.getPack()){
                if(premier.getNom().equals(deuxieme.getNom())){
                search = true ; 
                }
            }}
        return search ; 
    
    
    
    }*/
}
