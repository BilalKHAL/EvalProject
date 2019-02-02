/* Cette classe permet de créer une équipe de 4 participants. 
 * Elle est utilisée dans la question 9.
*/

public class Quatuor {
	private Binome nom;
	private Binome nom2;
	
	/* Constructeur
	 */
	public Quatuor (Binome nom, Binome nom2) {
		this.nom=nom;
		this.nom2=nom2;
	}
	
	public Binome getb1() {
		return (this.nom);
	}
	
	public Binome getb2() {
		return (this.nom2);
	}
	
}
