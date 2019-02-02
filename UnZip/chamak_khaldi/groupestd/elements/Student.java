
import java.util.ArrayList;
/** 
 * Classe qui repr�sente un �tudiant dans notre projet
 * @author Bilal
 *@author Yonah
 */

public class Student implements Comparable {
	/** 
 * Constructeur qui permet d'instancier un objet étudiant
 * @param l'identifiant de l'étudiant en question
 */
	
	public Student(int id) {
		m_identifiant = id ; 
		m_listeCours = new ArrayList<Cours>() ; 
		m_listeTd = new ArrayList<Td>() ; 
		this.m_niveauContrainte = 0 ; 
		
	}
	      /**
  Methode permettant de comparer deux etudiants
  *@param l'etudiant a comparer 
  *@return true si les &tudiants possèdent le même identifiant
  *false sinon
  */
	
	@Override
	public boolean equals(Object o) {
		
		if(o instanceof Student) {
			
			if(this.m_identifiant == ((Student)o).getIdentifiant()) {
				
				return true ;
			}else {
				return false ; 
			}
			
			
		}else {
			
			return false ; 
		}
		
	}
        
           /**
   *Méthode qui permet d'ajouter un cours à un élève
   *@param le cours de l'étudiant souhaité
   */
	
	public void ajouterCours(Cours monCours) {
		m_listeCours.add(monCours) ; 
}
	 /**
   *Méthode qui permet d'ajouter un Td à un élève
   *@param le td de l'étudiant souhaité
   */
	public void ajouterTd(Td monCours) {
		m_listeTd.add(monCours) ; 
}
        /**
   *Méthode qui retourne la liste des cours que l'élève suit
   *@return la liste des cours 
   */
	
	public ArrayList<Cours> getCours(){
		return m_listeCours ; 
		
	}
	  /**
   *Méthode qui retourne la liste des TDs que l'élève suit
   *@return la liste des TDs 
   */
	public ArrayList<Td> getTd(){
		
		return this.m_listeTd ; 
		
		
	}
	
	
    /**
   *Méthode qui affiche tous les cours suivis par un étudiant 
   */
	
	public void afficherCours() {
		System.out.print("ETUDIANT ID : " + m_identifiant + "\n");
		for (Cours monCours : m_listeCours) {
			System.out.println(monCours);
		}
		
	}
        
        /**
     *Méthode qui permet de retourner la représentation d'un éléve sous forme de chaine de caractère 
     *@return la représentation d'un éléve sous forme de chaine
     */   
	
	public String toString() {
		
		return "Etudiant ID : " + m_identifiant ; 
	}
	
	/** 
	 * Identifiant unique de l'�tudiant
	 */
	private int m_identifiant ; 
	/** 
	 * Variable qui permet d'assigner un "niveau de contrainte" a un �tudiant par exemple avoir plusieurs TD sur un meme creneau incrementerai cette variable
	 */
	private int m_niveauContrainte ; 
	/** 
	 *Liste qui montre tous les cours suivi pas besoin de Map nous verifions dans la classe GestionGroupe l'unicit� des cours
	 */
	private ArrayList<Cours> m_listeCours ;
	private ArrayList<Td> m_listeTd ; 
	
	
  /**
     *Méthode qui permet de retourner l'identifiant d'un élève
     *@return l'identifiant
     */ 
	public int getIdentifiant() {
		return m_identifiant ; 
	}
        /**
     *Méthode qui permet d'incrementer la contrainte d'un élève
     *@return le niveau de contrainte
     */    

public void incrementContrainte() {
	this.m_niveauContrainte++ ; 
}

 /**
     *Méthode qui permet de retourner la contrainte d'un élève
     *@return la contrainte
     */ 
public int getContrainte() {
	
	return this.m_niveauContrainte ; 
	
}
/**
     *Méthode qui de l'interface Comparable pour comparer les niveaux de contrainte de deux étudiants
     *@return un entier 
     */ 
@Override
public int compareTo(Object etu) {
	
	if (etu instanceof Student) {
		
		return (this.m_niveauContrainte < ((Student)etu).getContrainte()) ? 1 : (this.m_niveauContrainte == ((Student)etu).getContrainte()) ? 0 : -1 ; 
		
		
		
	}
	return 0 ; 
	
}
}
