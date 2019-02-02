/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

/**
 *
 * @author yonah/bilal
 */
public enum nomDossier {
    Soumis("Soumission"),testU("testUnitaire"),testScore("Benchmark") ;
    private String name = "" ; 
    
    nomDossier(String name){
        this.name = name ;
    }
    public String toString(){
    return this.name ; 
    }
    
}
