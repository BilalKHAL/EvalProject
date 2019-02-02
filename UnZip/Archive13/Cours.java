

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/** 
 * Classe qui repr�sente un cours dans notre projet
 * @author Bilal
 * @author Yonah
 */

public class Cours {

/** 
 * Constructeur qui permet d'instancier un objet cours
 * @param le nom du cours en question
 */
	
	public Cours(String nom) {
		
		m_nom = nom ; 
		m_listeEtudiants = new HashMap<Integer,Student>() ; 
		m_idCours = nbCours ; 
		if (this instanceof Td ) {
			occupation = 0.1 ; 
			
		}else {
			occupation = 1 ; 
			
			
		}
		
		nbCours++ ; 
		m_tdCours = new HashMap<Integer,Td>() ; 
		
		
	}
  /**
   * Permet de modifier le cr�neau d'un cours 
   */
  
  	public void setCreneau(Creneau unCreneau) {
		this.monCreneau = unCreneau ; 
	}
  	
  	public void afficherCours() {
  		monCreneau.afficherCours();
  		
  	}
  
  /**
   * Permet d'ajouter un td � un cours 
   * @param le td � ajouter
   * @return vrai si lajout a bien �t� effectu� 
   * false sinon
   */
   
  	public boolean ajouterTd(Td unTd) {
			
		if(!(m_tdCours.containsKey(unTd.getId()))) {
			m_tdCours.put(unTd.getId(), unTd) ;
			return true ; 
			
		}else {
			
			
			return false ; 
		}
	
	}
	
  
  
    
/** 
 * Methode permettant de modifier le nombre de groupe de TD du cours
 * @param le nombre d'�l�ves 
 */		
		
	
	
	public void setNbGroupeTd(int nb) {
		m_nbGroupesTd = nb ;
	}
	
	
	public Collection<Td> getTd() {
		
		
		ArrayList<Td> mesTd = new ArrayList<Td>(m_tdCours.values());
		
		Collections.shuffle(mesTd);
		
		return mesTd; 
		 
		
	}
  

	
/** 
 * Methode permettant d'ajouter un �tudiant � un cours
 * @param un objet de type �l�ve
 * @return true si l'�tudiant a bien �t� ajout� 
 * faux sinon
 */	
 
	public boolean ajouterEtudiant(Student etudiant) {
		
            
            if(this.monCreneau != null){
                
                for(Cours e : etudiant.getCours()){
                	
                    
                    if (e.getCreneau() != null){
                    	
                    	
                        
                        if(this.monCreneau.equals(e.getCreneau())){
                           return false ; 
                           
                        }
                         } 
                            
   }
                
                
for(Td e : etudiant.getTd()){
                	
                    
                    if (e.getCreneau() != null){
                    	
                    	
                        
                        if(this.monCreneau.equals(e.getCreneau())){
                           return false ; 
                           }
                         } 
                            
   }
                    
                    
  }
                
                
                
                
            
            
            
            
            
            
    
            
            
            
		
            
            
            
            if(!(m_listeEtudiants.containsKey(etudiant.getIdentifiant()))) {
			m_listeEtudiants.put(etudiant.getIdentifiant(), etudiant) ;
			return true ; 
			
		}else {
			
			return false ; 
		}
	
	}
  
/** 
 * Methode permettant de nous renvoyer l'identifiant du cours
 * @return l'identifiant du cours
 */	  
  
	public int getId() {
		return m_idCours ;
		
	}
  
 /** 
 * Methode qui permet de retourner le nom du cours
 * @return le nom du cours
 */	 
	
	public String toString() {
		return m_nom ; 
	}
  
   /** 
 * Methode qui permet de retourner la liste des �tudiants qui souhaitent participer � ce cours
 * @return la liste
 */	 
 
  public Map<Integer,Student> getlisteEtudiants(){
  return m_listeEtudiants;
  }
  
  /**
   * Methode permettant d'afficher les �tudiants d'un cours 
   */
  
  	public void afficherEtudiant() {
		Collection<Student> e = m_listeEtudiants.values() ; 
		for (Student t : e) {
			System.out.println(t);
		}
		
		
		
	}
  /** M�thode qui permet de retourner l'occupation d'un cours
   *@return l'occupation 
   */
	
	public double getOccupation() {
		return occupation ; 
		
	}
	
	/** 
	 * Variable static qui compte le nombre de cours de notre gestion de groupe et permet d'assigner un identifiant unique a chaque groupe
	*/
	
	public void afficherTd() {
		Collection<Td> e = m_tdCours.values() ; 
		for (Td t : e) {
			System.out.println(t);
		}
		
		
		
	}
	
	
	public Creneau getCreneau() {
		return this.monCreneau ; 
		
		
	}
        
        
        public boolean equals(Object unCours) {
		
		if (this instanceof Cours) {
                    if (this.m_nom.equals(unCours.toString())){
                        
                        
                        return true ; 
                    
                    }else {
                    return false ; 
                    }
                }
                else {
                    return false ; 
                
                }
			
                
                }
        
        
        public boolean isPresent(Student a) {
        	
        	if(this.m_listeEtudiants.containsKey(a.getIdentifiant())) {
        		
        		return true ;
        	}else {
        		
        		return false ; 
        	}
        	
        	
        }
	
	private static int nbCours = 1 ; 
	/** 
	 * Identifiant du cours
	*/
	private int m_idCours ; 
	/** 
	 * nom du cours
	*/
	private String m_nom ; 
	/** 
	 *Nombre de groupes de TD du cours
	*/
	int m_nbGroupesTd ; 
	/** 
	 *Liste des �tudiant qui souhaitent participer a ce cours
	*/
	protected Map<Integer,Student> m_listeEtudiants ; //Map pour �viter les doublons
	 /**
	  *Occupation d'un cours
	  */
		private final double occupation  ; 
    
    /**
     * Map des td d'un cours
     */
	
	private Map<Integer,Td> m_tdCours ;  
	 /**
	  *le cr�neau du cours 
	  */
	private Creneau monCreneau ;

}
