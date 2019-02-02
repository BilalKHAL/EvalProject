
import java.util.ArrayList;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Creneau {
	/** 
	 * Map des cours d'un creneau
	 */
	private Map<Cours,Integer> element ;  
        /** 
	 * Jour d'un creneau
	 */
	private Jour monJour ; 
	/** 
	 * nbCrenau qui permet d'attribuer un identifiant unique a un creneau pour un jour donner
	 */
	private static int nbCreneau = 1 ; 
        /** 
	 * Identifiant d'un creneau pour un jour donner
	 */
        
	private int m_idCreneau ; 
        
        /** 
	 * Constructeur d'un creneau 
	 */
	
	
	public Creneau() {
		element = new HashMap<Cours,Integer>() ; 
		m_idCreneau = nbCreneau ; 
		nbCreneau++ ; 
		}
        
   
	/** 
	 * Methode qui attribue un Jour a un creneau
	 */
	public void setJout(Jour unJour) {
		monJour = unJour ; 
		
	}
        
        /** 
	 * redefinition de la methode equals pour pouvoir comparer deux objet de type Jour
	 */
	@Override
	public boolean equals(Object unCreneau) {
		
		if (unCreneau instanceof Creneau) {
			
		
			if (this.monJour.equals(((Creneau)unCreneau).getJour()) && this.m_idCreneau == ((Creneau)unCreneau).getId()) {
				
				
				return true ;
			}else {
				
				return false ; 
			}
			}
		else {
			
			return false ; 
		}
		
		
		
		
	}
	
	/** 
	 * retourne le jour d'un Td
	 */
	public Jour getJour() {
		
		return this.monJour ; 
	}
	/** 
	 * Retourne l'identifiant  
	 */
	public int getId() {
		
		return this.m_idCreneau ; 
	}
        
        /** 
	 * Ajoute un cours a un Creneau evite un Td en meme temps qu'un cours sur un creneau
	 */
	
	public boolean ajouterCours(Cours unCours) {
            
           // element.put(unCours, unCours.getId()) ; 
           for(Cours c : element.keySet()){
               
               if(unCours instanceof Td){
                   
                   if(!(c instanceof Td)){
                       
                       if(c.toString().equals(unCours.toString().split("_")[0])){
                           return false ; //Eviter Un TD en meme temps qu'un cours 
                       }
                   }
                   
                   
                   
               
               }
           
           
           
           }
           
            
            if(!(element.containsKey(unCours))){
                
                element.put(unCours, unCours.getId()) ; 
                return true ; 
            }
            else {
            
            return false ; 
            }
            
    }
	
        /** 
	 * Attribue un identifiant a un creneau
	 */
        public static void setCreneau(int id) {
		nbCreneau = id ; 
		
	}
	public String toString() {
		return "Creneau numero: " + m_idCreneau ; 
		
	}
	/** 
	 * Affiche les cours d'un Creneau
	 */
	public void afficherCours() {
            Collection<Cours> e = this.element.keySet() ; 
		for(Cours unCours : e) {
			System.out.println(unCours);
			
		}
		
		
	}
        /** 
	 * Retourne le nombre de cours d'un creneau
	 */
        public int getNumCours() {
            int nb = 0 ; 
            for(Cours e : this.element.keySet()){
                
                if(e instanceof Cours){
                    
                    nb++ ; 
                
                }
            
            
            
            }
            return nb ; 
        
        
        
        }
        /** 
	 * Retourne le nombre de Td d'un creneau
	 */
	
	 public int getNumTd() {
            int nb = 0 ; 
            for(Cours e : this.element.keySet()){
                
                if(e instanceof Td){
                    
                    nb++ ; 
                
                }
            
            
            
            }
            return nb ; 
        
        
        
        }
	
	/** 
	 * Retourne les cours d'un creneau
	 */
	public ArrayList<Cours> getCours(){
		
		ArrayList<Cours> retour = new ArrayList(this.element.keySet()) ;
		
		
		return retour ; 
	}
	
	


}

