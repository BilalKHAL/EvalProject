
import java.util.ArrayList;
/** 
 * Classe qui repr�sente un jour dans notre projet
 * @author Bilal
 * @author Yonah
 */



public class Jour {
/**
* Liste des cr�neau d'un jour
*/	
	private ArrayList<Creneau> m_creneauJour ;
 /**
 * Identifiant d'un jour
*/	 
 
	private int m_idJour ; 
  
/**
* Nom du jour
*/	   
	private String m_nomJour ;
/**
* param�tre permettant d'avoir un identifiant du jour unique
*/  

	private static int nbJour = 0 ; 
/** 
 * Constructeur qui permet d'instancier un objet jour
 * @param le nom du jour en question
 */	
	
	public Jour(String nom) {
		m_creneauJour = new ArrayList<Creneau>() ;
		m_nomJour = nom ; 
		m_idJour = nbJour ;
		nbJour++ ; 
		Creneau.setCreneau(1);
		
	}
	
	
	@Override
	public boolean equals(Object unJour) {
		
		if (unJour instanceof Jour) {
			
			if (this.m_idJour == ((Jour)unJour).getId()) {
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
	 * Met a zero le nombre de jour
	 */
	
	public static void resetNbJour(){
        Jour.nbJour = 0 ;
        }
	
	public int getId() {
		
		return this.m_idJour ; 
	}
  
 /**
* Methode permettant de retourner un cr�neau 
* @param l'index du cr�neau du jours en question
* @return le cr�neau du jours recherch�
*/
	
	public Creneau getCreneau(int index) {
		return m_creneauJour.get(index) ; 
		
	}
/**
* /**
* Permet d�obtenir la repr�sentation d�un jour en cha�ne de caract�res
* @return le repr�sentation d'un jour sous forme de chaine
*/	
	
	
	public String toString() {
  String chaine="Jour de la semaine : " + m_nomJour+" \n";
		
		return chaine ; 
	}
	
/**
* Permet d'ajouter un cr�neau � un jour
* @param le cr�neau � ajouter
*/		
	public void ajouterCreneau(Creneau unCreneau) {
		
		m_creneauJour.add(unCreneau) ; 
	}
  
  /**
* Permet de retourner le nom du jour
* @return nom du jour
*/	
	
	public String getNom() {
		return m_nomJour ; 
	}
/**
* Permet de retourner l'identifiant du jour
* @return identifiant
*/		
	public int getIdJour() {
		return m_idJour ; 
		}
    
/**
* Permet d'afficher l'emploi du temps d'un jour
*/	
	
        
        
	public void afficherCreneau() {
		System.out.println(m_nomJour);
		for (Creneau unCreneau : m_creneauJour) {
			
			System.out.print(unCreneau);
			unCreneau.afficherCours();
			
		}
		
	}
        
        /** 
	 * Renvoire tous les creneaux d'une journée
	 */
        public ArrayList<Creneau> getAllCreneau(){
        return this.m_creneauJour ; 
        }


}
