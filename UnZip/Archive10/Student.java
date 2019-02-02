import java.util.ArrayList;
/** 
 * Classe qui repr�sente un �tudiant dans notre projet
 * @author Bilal/Yonnah
 *
 */

public class Student implements Comparable {
	
	
	public Student(int id) {
		m_identifiant = id ; 
		m_listeCours = new ArrayList<Cours>() ; 
		m_listeTd = new ArrayList<Td>() ; 
		this.m_niveauContrainte = 0 ; 
		
	}
	
	public void ajouterCours(Cours monCours) {
		m_listeCours.add(monCours) ; 
}
	
	public void ajouterTd(Td monCours) {
		m_listeTd.add(monCours) ; 
}
	
	public ArrayList<Cours> getCours(){
		return m_listeCours ; 
		
	}
	
	public ArrayList<Td> getTd(){
		
		return this.m_listeTd ; 
		
		
	}
	
	
	public void afficherCours() {
		System.out.print("ETUDIANT ID : " + m_identifiant + "\n");
		for (Cours monCours : m_listeCours) {
			System.out.println(monCours);
		}
		
	}
        
        
	
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
	
	
	public int getIdentifiant() {
		return m_identifiant ; 
	}

public void incrementContrainte() {
	this.m_niveauContrainte++ ; 
}

public int getContrainte() {
	
	return this.m_niveauContrainte ; 
	
}

@Override
public int compareTo(Object etu) {
	
	if (etu instanceof Student) {
		
		return (this.m_niveauContrainte < ((Student)etu).getContrainte()) ? 1 : (this.m_niveauContrainte == ((Student)etu).getContrainte()) ? 0 : -1 ; 
		
		
		
	}
	return 0 ; 
	
}
}
