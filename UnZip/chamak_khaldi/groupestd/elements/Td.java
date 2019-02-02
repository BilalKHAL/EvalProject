
/** 
 * Classe qui repr�sente un TD dans notre projet
 * @author Bilal
 * @author Yonah
 */
public class Td extends Cours {
    /** 
 * Constructeur qui permet d'instancier un objet Td
 * @param le nom du Td en question
 */
    public Td(String nom) {
		super(nom);
	}
/**Methode permettant de mofifier l'identifiant d'un td
 *@param l'identifiant d'un td
 */
  
 public void setId(int id) {
		this.m_idGroupe = id ; 
}
        
        
        

/**
 *Methode permettant de retourner l'identifiant d'un td
 *@return l'identifiant
 */

public int getId() {
		
		return m_idGroupe ; 
	}

/**
 *Methode permettant de mettre à jour le nombre maximum de la borne
 *@param le nombre maximum d'eleve que peut comporter un TD
 */

public void setBorne(int borne){
    this.m_borneMax = borne ; 
}

/**
 *Methode d'identifier si le td est remplit
 *@return true s'il est remplit
 *false sinon
 */
public boolean isFull(){
    
    if(this.m_listeEtudiants.size() < this.m_borneMax){
        return false ; 
    
    }else {
    return true ; 
    }


}
	


	/** 
	 * Identifiant du groupe
	 */
	private int m_idGroupe ;
	/** 
	 * Borne maximum d'�tudiant dans un groupe
	 */
	public static int m_borneMax = 40;
	/** 
	 * Identifiant du cours rattacher au groupes
	 */
	private int m_idCours ; 
	

	
	
	
}