import java.time.Duration;

public class Participants {
	private String prenom;
	private String nom;
	private String nationalite;
	private String sexe;
	private String age;
	private Duration temps;
	
	/* Constructeur
	 */
	public Participants (String prenom, String nom, String nationalite, String sexe, String age, Duration temps) {
		this.prenom=prenom;
		this.nom=nom;
		this.nationalite=nationalite;
		this.sexe=sexe;
		this.age=age;
		this.temps=temps;
	}
	
	/* Les 6 méthodes suivantes permettre de retourner les attributs du participant
	 */
	public String getprenom() {
		return (this.prenom);
	}
	
	public String getnom() {
		return (this.nom);
	}
	
	public String getnationalite() {
		return (this.nationalite);
	}
	
	public String getsexe() {
		return (this.sexe);
	}
	
	public String getage() {
		return (this.age);
	}
	
	public Duration gettemps() {
		return (this.temps);
	}
	
	/* La méthode get prend un argument de type String en paramètre. Ce paramètre critère peut être: prénom/nom/nationalité/sexe/âge.
	 * Cette méthode retourne l'attribut du participant en fonction du critère choisi. 
	 * Cette méthode a été développée pour les méthodes Extraire et Supprimer de la classe Resultats.
	 */
	public String get(String critere) {
		String x = "";
		if (critere.equals("prenom")){
			x = this.prenom;
		}
		if (critere.equals("nom")){
			x = this.nom;
		}
		if (critere.equals("nationalite")){
			x = this.nationalite;
		}
		if (critere.equals("sexe")){
			x = this.sexe;
		}
		if (critere.equals("age")){
			x = this.age;
		}
		return x;
	}
	
	/* La méthode toString permet de retourner la description d'un participant sous la forme d'une chaîne de caractère.
	 */
	public String toString(){
		return " Prenom: "+prenom+" / Nom: " +nom+" / Nationalite: "+nationalite+ " / Catégorie: "+sexe+age;
	}
}
