
import java.util.ArrayList;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Creneau {
	
	private Map<Cours,Integer> element ;  
	private Jour monJour ; 
	
	private static int nbCreneau = 1 ; 
	private int m_idCreneau ; 
	
	
	public Creneau() {
		element = new HashMap<Cours,Integer>() ; 
		m_idCreneau = nbCreneau ; 
		nbCreneau++ ; 
		}
        
   
	
	public void setJout(Jour unJour) {
		monJour = unJour ; 
		
	}
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
	
	
	public Jour getJour() {
		
		return this.monJour ; 
	}
	
	public int getId() {
		
		return this.m_idCreneau ; 
	}
	
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
	public static void setCreneau(int id) {
		nbCreneau = id ; 
		
	}
	public String toString() {
		return "Creneau numero: " + m_idCreneau ; 
		
	}
	
	public void afficherCours() {
            Collection<Cours> e = this.element.keySet() ; 
		for(Cours unCours : e) {
			System.out.println(unCours);
			
		}
		
		
	}
        
        public int getNumCours() {
            int nb = 0 ; 
            for(Cours e : this.element.keySet()){
                
                if(e instanceof Cours){
                    
                    nb++ ; 
                
                }
            
            
            
            }
            return nb ; 
        
        
        
        }
	
	 public int getNumTd() {
            int nb = 0 ; 
            for(Cours e : this.element.keySet()){
                
                if(e instanceof Td){
                    
                    nb++ ; 
                
                }
            
            
            
            }
            return nb ; 
        
        
        
        }
	
	
	
	
	


}

