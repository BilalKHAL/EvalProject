/* Cette classe permet de créer un binome de participants. 
 * Elle est utilisée dans la question 9.
 */

public class Bonjour {
	private Participants nom;
	private Participants nom2;
	private Long temps;
	
	/* Constructeur
	 */
	public Bonjour (Participants nom, Participants nom2, long temps) {
		this.nom=nom;
		this.nom2=nom2;
		this.temps=temps;
	}
	
	public Participants getp1() {
		return (this.nom);
	}
	
	public Participants getp2() {
		return (this.nom2);
	}
	
	public Long gettemps() {
		return (this.temps);
	}
	
}
