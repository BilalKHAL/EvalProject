

import java.util.Comparator;

/** 
 * Classe qui implemente l'interface Comparator 
 * @author Bilal
 * @author Yonah
 */
public class OrdreId implements Comparator<Student> {
/** 
 * Redefinition de la methode compare de l'interface Comparator qui va comparer les identifiants des étudiants 
 * @param un étudiant 
 * @param un deuxieme etudiant
 * @return -1 si l'identifiant de l'etudiant 1 est inferieur a celui de l'etudiant 2
 * 0 si l'identifiant de l'etudiant 1 est égale a celui de l'etudiant 2
 * 1 sinon
 */
	@Override
	public int compare(Student etu1, Student etu2) {
		
		return (etu1.getIdentifiant() < etu2.getIdentifiant()) ? -1 : (etu1.getIdentifiant() == etu2.getIdentifiant()) ? 0 : 1 ; 
		
	}

}
