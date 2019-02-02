
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class GestionGroupe {
	private Scanner m_user;
	private static final int m_essai = 10000;
	private String m_lastError;
	private String m_pathFichierEdt = "edt.csv";
	private String m_pathFichierStudent = "etu.csv";
	private FileInputStream fluxEdt;
	private FileInputStream fluxStudent;
	private Map<String, Cours> m_listeCoursFichier;
	private Map<Integer, Student> m_listeEtudiantFichier;
	private Map<Integer, Student> m_listeEtudiantPb;
	private Map<Integer, String> m_idColNomCours;
	private Map<String, Jour> m_listeJourFichier;
	private Map<Integer, String> m_idColNomJour;
	
	private boolean lecture  ;
        
        private static final String[] bonusCours = {"JAVA","IA","Sociologie","Math","SVT","Graphes" ,"Proba","C++","Unix","Logique","Anglais" , "Allemand"} ; 
        private static final String[] nomJour = {"Lundi","Mardi","Mercredi","Jeudi","Vendredi"} ; 
        private static final int nbCreneauJour = 8 ; 

	public GestionGroupe() throws IOException {
		
		m_user = new Scanner(System.in);
		lecture = false ;
                m_listeCoursFichier = new HashMap<String, Cours>();
		m_listeJourFichier = new HashMap<String, Jour>();
		m_idColNomCours = new HashMap<Integer, String>();
		m_idColNomJour = new HashMap<Integer, String>();
		m_listeEtudiantFichier = new HashMap<Integer, Student>();
		m_listeEtudiantPb = new HashMap<Integer, Student>();
                
  

	}
         /** 
 * Methode qui permet a partir d'une chaine du fichier de renvoyer le cours ou Td correspondant 
 * @param la chaine a determiner exemple : ("JAVA_1") 
 * @return ArrayList des objet correspondant a la chaine 
 */
	private ArrayList<Cours> determinerChaine(String chaine) {
		ArrayList<Cours> retour = new ArrayList<Cours>(); //ArrayList de retour 
		if (chaine.contains(" ")) { //Si la chaine contient un espace presence d'un cours et un Td en parralele ou deux Td
			String[] td = chaine.split(" "); //On recupere les deux elements 
			for (int i = 0; i < td.length; i++) {

				if (td[i].contains("_")) {//Si notre ellement contient un _ etant donné la norme du fichier ce sera forcement un Td
					Td unTd = new Td(td[i]); //On construit notre Td avec le nom correspondant 
					unTd.setId(Integer.parseInt(td[i].split("_")[1]));//On lui attribut son identifiant qui correspond a l'identifiant apres le underscore
					retour.add(unTd);//On l'ajoute as notre ArrayList de retour 

				} else {
					if (m_listeCoursFichier.containsKey(td[i])) {//On verifie la correspondance du cours avec ceux qu'on as lu dans le premier fichier
						retour.add(m_listeCoursFichier.get(td[i])); //Majgres que le cours existe deja on l'ajoute comme meme a notre arrayList de retour pour un traitement ulterieur(Methode creerTd)
					}

					
				}
			}
			return retour; //On peut renvoyer a partir d'ici comme on aura traiter tout les elements separer d'un espace

		} else if (chaine.contains("_")) { //Si on arrive ici c'est que notre element est seul dans le creneau et si il contient un _ c'est que forcement un TD
			Td unTd = new Td(chaine); //On cree notre Td
			unTd.setId(Integer.parseInt(chaine.split("_")[1])); //meme processus que plus haut 
			retour.add(unTd);
			return retour;

		} else {

			if (m_listeCoursFichier.containsKey(chaine)) {//Si ce n'est pas un Td alors c'est forcement un Cours meme processus que plus haut 
				retour.add(m_listeCoursFichier.get(chaine));
				return retour;

			} else {

				return null; //Si le cours n'appartient pas au fichier emploi du temps alors on ne renvoie rien aucun elemnt determiner
			}

		}

	}
        
        /** 
 * Methode qui permet de lire le fichier 
 * @param le fichier a lire
 * @return ArrayList du fichier en question 
 */
        
        public static ArrayList<String> lireFichier(FileInputStream fichier) throws IOException {
		BufferedReader flux_entree = new BufferedReader(new InputStreamReader(fichier, "UTF-8")); //On recupere un BufferedReader afin de lire le fichier avec un encodage UTF-8
		String ligne;
		ArrayList<String> retour = new ArrayList<String>(); //ArrayList qui represente chaque ligne de notre fichier
		flux_entree.mark(1);//On mark le premier caractere au cas de non presence du mask order byte utf-8 afin de se resituer dans le flus

		if (flux_entree.read() != 0XFEFF) { // Permet de v�rifier la presence du mask order byte en UTF-8 au debut du
											// fichier (generer par exemple par notepad sur windows)
			flux_entree.reset(); // Si on rentre dans le if pas de presence de mask order byte donc on se remet
									// au precedent mark avant la lecture
		}
		while ((ligne = flux_entree.readLine()) != null) { // Tant qu'on recupere une ligne on l'ajoute a notre ArrayList
			retour.add(ligne);
		}

		return retour;
	}
        
        
               /** 
 * Methode qui permet d'initialiser l'attrbut 
 * @param int 0 --> fichier emploi du temps  1 --> fichier etudiant 
 * 
 */
        
        
        private void ouvrirFlux(int fichier) {
		switch (fichier) {
		case 0:
			try {
				this.fluxEdt = new FileInputStream(this.m_pathFichierEdt); //On initialise un attribut en fonction du parametre de la methode
			} catch (FileNotFoundException e) {
				System.out.println("Fichier Emploi du temps introuvable !\nmodifier chemin:");
				m_pathFichierEdt = m_user.next();
				this.ouvrirFlux(0);//Au cas ou l'excepetion est levee on propose a l'utilisateur de resaisir un chemin valide
			}
		case 1:
			try {
				this.fluxStudent = new FileInputStream(this.m_pathFichierStudent);
			} catch (FileNotFoundException e) {
				System.out.println("Fichier Etudiant introuvable !\nmodifier chemin:");
				this.m_pathFichierStudent = m_user.next();
				this.ouvrirFlux(1);
			}
			break;
		default:
			throw new IllegalArgumentException();//Si ne rentre pas dans le case 1 ou 2 la methode a mal etait comprise donc on jette une Exception d'argument invalide

		}
	}
        
        
          /** 
 * Methode qui permet d'obtenir le nombre de colonne d'un fichier csv  
 * @return int nombreColonne
 * @param string une ligne de notre fichier 
 
 * 
 */
        private int getNbColonne(String ligne) {
		String[] nbColonne = ligne.split(";"); //
		return nbColonne.length;

	}
        
      /** 
 * Methode qui affiche le menu 
 * @return int qui correspond au choix de l'utilisateur 
 
 * 
 */   
        public int afficherMenu() {
		int choix = -1;
		System.out.print(
				"1. Lire les fichiers d'entr�e par d�faut\n2. Lire les fichier d'entr�e en entrant le nom de chaque fichier au clavier\n3. Affecter les �tudiants dans les groupes de TDs\n4. Changer le nombre maximal d'�tudiants par groupe de TD(Changer avant d'affecter etudiant sinon reproceder a une lecture des fichiers)\n5.Changer nombres maximal d'etudiant pours tous les Td(Changer avant d'affecter etudiant sinon reproceder a une lecture des fichiers)\n6.Generer Nouveau probleme\nChoix :");
		try {
			choix = m_user.nextInt();
			if (choix < 1 || choix > 6) {
				System.out.println("Choix ne correspond a aucune action du menu !");

				this.afficherMenu();
			}

		} catch (InputMismatchException e) {
			System.out.println("Veuillez saisir uniquement des chiffres !");
			m_user.next();
			this.afficherMenu();

		}
		return choix;

	}
        
            /** 
 * Methode qui cree les objet cours contenu dans le fichier 
 * @return boolean qui correspond a la reussite de la creation 
 * @param ArrayList qui correspond au ligne du fichier etudiant 
 
 * 
 */   
        private boolean creerCours(ArrayList<String> liste) {

		int nbCours = this.getNbColonne(liste.get(0)); //On recupere le nombre de cours contenu dans notre fichier 
		for (int i = 1; i < nbCours; i++) { //Pour chaque cours on part de 1 car le premier element correspond a Id
			Cours nouveauCours = new Cours(liste.get(0).split(";")[i].replace(" ", "")); //On cree notre cours avec en parametre le nomDuCours contenu dans le fichier (si presence d'un espace on le retire pour comparer chaine Td et Cours car dans Td aucun espace dans le nom  cours JAVA_1)

			if (!(this.m_listeCoursFichier.containsKey(nouveauCours.toString()))) { //On verifie que notre cours n'existe deja pas a l'aide de notre Map
				m_listeCoursFichier.put(nouveauCours.toString(), nouveauCours);//On Ajoute le cours
				m_idColNomCours.put(nouveauCours.getId(), nouveauCours.toString());//On recupere l'id de la colonne qui correspond a ce cours

			} else {

				return false; //Si il existe deja c'est que les cours sont deja creer 

			}

		}
		return true;

	}
        
              /** 
 * Methode qui cree les etudiant contenu dans le fichier 
 * @return boolean qui correspond a la reussite de la creation 
 * @param ArrayList qui correspond au ligne du fichier etudiant 
 
 * 
 */      
        private boolean creerEtudiant(ArrayList<String> liste) {
		int nbCours = this.getNbColonne(liste.get(0));//On recupere le nombre de cours de notre fichier 
		for (int i = 1; i < liste.size(); i++) {//Pour chaque etudiant 
			boolean etudiantValide = true;//Boolean qui correspond a une lecture totale d'un etudiant 
                        Student nouveauEtudiant = new Student(Integer.parseInt(liste.get(i).split(";")[0]));//On recupere l'identifiant de notre etudiant 
			for (int j = 1; j < nbCours; j++) {
				int nombre;
				try {
					nombre = Integer.parseInt(liste.get(i).split(";")[j]); //On recupere la valeur entrer a la ligne i qui correspond a l'eleve et la colonne j qui correspond au cours

				} catch (NumberFormatException e) {
					nombre = -1;

				}

				switch (nombre) {
				case 1:
					nouveauEtudiant.ajouterCours(m_listeCoursFichier.get(m_idColNomCours.get(j))); //Si notre etudiant est inscrit on ajoute son cours
					break;
				case -1:
					etudiantValide = false;//Erreur dans le fichier etudiant 
					break;
				}
			}

			if (etudiantValide) {
				if (!(this.m_listeEtudiantFichier.containsKey(nouveauEtudiant.getIdentifiant()))) { //Afin de verifier la presence de doublons dans le fichier 

					this.m_listeEtudiantFichier.put(nouveauEtudiant.getIdentifiant(), nouveauEtudiant); //On Ajoute l'etudiant 
					
				} 

			} else {

				if (!(this.m_listeEtudiantPb.containsKey(nouveauEtudiant.getIdentifiant()))) { //Etudiant qui n'a pas pu etre traiter du a une erreur dans le fichier etudiant
					m_listeEtudiantPb.put(nouveauEtudiant.getIdentifiant(), nouveauEtudiant);
				} 

			}

		}
		return true;

	}
        
   /** 
 * Methode qui cree les jour qui correspondent au fichier emploi du temp 
 *
 * @param ArrayList qui correspond au ligne du fichier emploi du temps 
 
 * 
 */             
        
        
        private void creerJour(ArrayList<String> liste) {

		for (int i = 0; i < liste.get(0).split(";").length; i++) {//Pour chaque jour 
			Jour nouveauJour = new Jour(liste.get(0).split(";")[i]);//On recupere le nom du jour contenu dans le fichier et on construit un objet jour
			m_listeJourFichier.put(nouveauJour.getNom(), nouveauJour);//On ajoute ce jour a notre liste de jour 
			m_idColNomJour.put(nouveauJour.getIdJour(), nouveauJour.getNom());//On ajoute l'index de la colonne qui correspond a ce jour dans le fichier 

			for (int j = 0; j < liste.size() - 1; j++) {//Pour chaque creneau contenu dans un jour -1 car on ne compte la ligne qui correspond au jour 
				Creneau nouveauCreneau = new Creneau(); //On ajoute un creneau vide 
				nouveauCreneau.setJout(nouveauJour);
				nouveauJour.ajouterCreneau(nouveauCreneau);
			}
		}

	}
        
        /** 
 * Methode qui permet d'afficher l'emploi du temps  
 *
 * 
 
 * 
 */    
        public void afficherEmploiDuTemp() {

		TreeMap<Integer, String> mapOrdo = new TreeMap<Integer, String>(m_idColNomJour); //On ordonne en fonction de l'ordre mit dans le fichier 
		Collection<String> c = mapOrdo.values();//On recupere les clef de listeJourFichier
		for (String e : c) {
			m_listeJourFichier.get(e).afficherCreneau(); //Pour chaque jour on affiche c creneau 
		}

	}
        
     
        /** 
 * Methode qui permet de creer les Td en fonction de l'emploi du temps et ajoute les creneau qui corresponde au cours dans l'emploi du temps   
 *
 * 
 
 * 
 */          
        private boolean creerTd(ArrayList<String> liste) {

		for (int i = 0; i < liste.get(0).split(";").length; i++) { //Pour chaque jour lecture verticale du fichier Emploi du temps
			Jour monJour = m_listeJourFichier.get(m_idColNomJour.get(i));//On recupere notre jour 
			for (int j = 1; j < liste.size(); j++) { //Pour chaque creneau 

				ArrayList<Cours> monCoursCreneau = this.determinerChaine(liste.get(j).split(";", -1)[i]); //On recupere l'element qui correspond a notre jour J ert a la colonne i de notre emploi du temps

				if (monCoursCreneau != null) { //Si on arrive a determiner quelque chose dans ce creneau 

					for (int x = 0; x < monCoursCreneau.size(); x++) { //Pour chaque element 
						if (monCoursCreneau.get(x) instanceof Td) { 

							Td unTd = (Td) monCoursCreneau.get(x);

							if (m_listeCoursFichier.containsKey(unTd.toString().split("_")[0])) {//On verifie qu'il existe bien un cours qui correspond a ce Td

								monJour.getCreneau(j - 1).ajouterCours(unTd);//On Ajoute notre Td a ce creneau 
								unTd.setCreneau(monJour.getCreneau(j - 1));//On Ajoute un creneau a ce Td
								m_listeCoursFichier.get(unTd.toString().split("_")[0]).ajouterTd((Td) unTd);//On Ajoute le Td au cours qui correspond

								
							} else {

								return false;

							}
						} else if (monCoursCreneau.get(x) instanceof Cours) {

							if (m_listeCoursFichier.containsKey(monCoursCreneau.get(x).toString())) {
								monJour.getCreneau(j - 1).ajouterCours(m_listeCoursFichier.get(monCoursCreneau.get(x).toString())) ; //On ajoute le TD a ve creneau 
								m_listeCoursFichier.get(monCoursCreneau.get(x).toString()).setCreneau(monJour.getCreneau(j - 1)); //On le creneau a ce TD
							} else {
								return false;

							}

						}

					}
				}
			}
		}
		return true;
	}
        
           /** 
 * Methode qui permet d'assigner les contrainte aux etudiantes afin de creer une hierarchisation 
 *
 * 
 
 * 
 */        
        private void setContrainteEtu() {
		Collection<Student> e = m_listeEtudiantFichier.values(); // On recupere une collection

		for (Student t : e) { //Pour chaque etudiant 
			ArrayList<Cours> r = t.getCours(); //On recupere les cours de cette etudiant 

			ArrayList<Creneau> creneauParra = new ArrayList(); //ArrayList qui va nous permetre de savoir les Creneau que cette etudiant ah en parralle

			for (Cours f : r) {

				Collection<Td> mesTd = f.getTd(); //On recupere les Td du cours ou l'etudiant est inscrit 
				Map<Creneau, Td> CreneauPossible = new HashMap<Creneau, Td>();//Map qui va nous permettre de savoir combien de creneau different pour les Td du cours
				for (Td z : mesTd) {

					if (!CreneauPossible.containsKey(z.getCreneau())) {
						CreneauPossible.put(z.getCreneau(), z); //Si un des Td du cours n'appartien pas au meme creneau c'est un nouveau creneau possibles 
					}
				} // Savoir si groupes de TD appartenant au meme cours sont dans le meme creneau

				Collection<Creneau> d = CreneauPossible.keySet(); //Variable qui va nous permettre d'ajouter tous les creneaux possible des Td
				for (Creneau y : d) {
					creneauParra.add(y); //On ajoute les creneaux possible des Td de ce cours a tous les autres 
				}

			}

			Map<Creneau, Integer> detectCreneauDouble = new HashMap<Creneau, Integer>();

			for (Creneau j : creneauParra) {

				if (!(detectCreneauDouble.containsKey(j))) { //On ajoute tous les creneaux qu'on as recuperer si on en as en double cela veut dire que l'etudiant des Td different en parralele au quelle il doit participer

					detectCreneauDouble.put(j, 0);

				} else {
					t.incrementContrainte();//On incremente donc la contrainte l'etudiant 
				}

			}

		}

	}
        /** 
 * Methode qui permet d'assigner les contrainte aux etudiantes afin de creer une hierarchisation afin de traiter en priorité les etudiants prioritaires
 *
 * 
 
 * 
 */           
        
        private boolean assignerEtudiant(ArrayList<Student> er) {


		

		Collections.sort(er);//On tri nos etudiant en fonction de leur contraintes 

		ArrayList<ArrayList<Student>> ter = new ArrayList<ArrayList<Student>>();//ArrayList qui va nous permettre de repartir nos etudiant en sous groupes de contraintes equivalentes
		ArrayList<Student> prio = new ArrayList<Student>();//ArrayList qui va nous permettre de constituer nos groupe d'etudiant 
		int max = er.get(0).getContrainte();//On recupere la premeire valeur qui vaut a la contrainte maximum etant donnee qu'on as trier note ArrayList
		for (Student erd : er) {
			int maxPre = erd.getContrainte();//Valeur Maximum actuel
			if (maxPre != max) {//Si la valeur n'est plus maximum on ajoute notre sous groupe a notre ArrayList final et on construit un nouveau groupe 
				ter.add(prio);
				prio = new ArrayList<Student>();
				prio.add(erd);
			} else {
				prio.add(erd);//Si la valeur est toujours maximum on ajoute l'etudiant au groupes
			}

		}

		ter.add(prio);//Ajout du dernier Groupe constituer
                
               
                    for (ArrayList<Student> monNiveauContrainte : ter) {//Pour chaque groupe constituer 

			Collections.shuffle(monNiveauContrainte);//On melange ce sous groupe afin en cal d'appel recursif essayer d'autres combinaison

			for (Student monEtudiant : monNiveauContrainte) {//Pour chaque etudiant de ce groupe

				ArrayList<Cours> tg = monEtudiant.getCours(); 
				boolean placer = false;

				for (Cours f : tg) {

					for (Td z : f.getTd()) {//Pour chaque Td du cours  de l'etudiant

						if ((!(z.isFull()))) { //Si il reste de la place dans le Td
							
							if ((z.ajouterEtudiant(monEtudiant))) {//Si on parvient a ajouter l'etudiant methode ajouter qui va verifier toutes les contraintes
								monEtudiant.ajouterTd(z);
								placer = true;
								break;
							}
						}
					}

					if (placer == false) {//Si on ne parvient pas as placer un etudiant notre tentatives de ^lacement as echouer donc on qui quitte afin de ressayer une autres combinaison
						return false ;

						

					}

				}

			}

		}
                    
                    
              return true ;
                
                
                
                
                
                
              

		

	}
        
   /** 
 * Methode qui permet de verifier les fichier afin de detecter une incoherence exemple cours existant dans l'emploi du temps mais inexistant dans le fichier etudiant 
 *
 * 
 
 * 
 */        
        private boolean verfierFichier(ArrayList<String> listeEdt, ArrayList<String> listeEtu) {

		int nbJour = listeEdt.get(0).split(";").length;
		listeEdt.remove(0);

		for (String s : listeEdt) {
			if (s.split(";", -1).length > nbJour) {

				this.m_lastError = "Verifier fichier emploi du temps colonne qui ne correspond a aucun jour : ";
				return false;
			}

			String[] st = s.split(";");

			for (int j = 0; j < st.length; j++) {
				if (st[j].contains(" ")) {
					String[] element = st[j].split(" ");
					for (int i = 0; i < element.length; i++) {
						if (element[i].contains("_")) {
							if ((!(this.m_listeCoursFichier.containsKey(element[i].split("_")[0])))
									&& (!(element[i].isEmpty()))) {
								this.m_lastError = "Verifier fichier emploi du temps TD :" + element[i].split("_")[0]
										+ "qui ne correspond a aucun cours dans le fichier etudiant";
								return false;
							}

						} else {

							if ((!(this.m_listeCoursFichier.containsKey(element[i]))) && (!(element[i].isEmpty()))) {
								this.m_lastError = "Verifier fichier emploi du temps cours :" + element[i]
										+ "qui ne correspond a aucun cours dans le fichier etudiant : ";
								return false;
							}

						}

					}
				} else {
					if (st[j].contains("_")) {
						if (!(this.m_listeCoursFichier.containsKey(st[j].split("_")[0])) && (!(st[j].isEmpty()))) {
							this.m_lastError = "Verifier fichier emploi du temps Td :" + st[j]
									+ "qui ne correspond a aucun cours dans le fichier etudiant : ";
							return false;
						}
					}

					else {
						if (!(this.m_listeCoursFichier.containsKey(st[j])) && (!(st[j].isEmpty()))) {

							this.m_lastError = "Verifier fichier emploi du temps cours :" + st[j]
									+ "qui ne correspond a aucun cours dans le fichier etudiant : ";
							return false;

						}

					}
				}

			}
		}

		return true;
	}
        
        
        
        /** 
 * Methode qui permet d'ajouter les etudiants au cours au quel ils sont inscrit 
 *
 * 
 
 * 
 */   
        private void inscrireEtudiant() {


		for (Student etu : this.m_listeEtudiantFichier.values()) {
			ArrayList<Cours> coursEtu = etu.getCours();
			for (Cours inscrit : coursEtu) {
				inscrit.ajouterEtudiant(etu);
			}

		}

	}
        
        /** 
 * Methode qui permet lire les fichier de notre projet emploi du temps 
 *
 * 
 
 * 
 */   
        public boolean lireFichier() throws IOException {
            Creneau.setCreneau(1);//On met le nombre de creneau de 1 
            Jour.resetNbJour();//On remet a 0 le nombre de Jour
            Cours.resetNbCours();//On remet a 0 le nombre de cours
            this.viderListe();//On vide chaque liste
            this.ouvrirFlux(0);//On recupere les flux de chaque fichier
            this.ouvrirFlux(1);
            ArrayList<String> liste2 = this.lireFichier(fluxEdt);//On recupere l'ArrayList du fichier emploi du temps
		ArrayList<String> liste = this.lireFichier(fluxStudent);//On recupere l'ArrayList du fichier etudiants
                fluxEdt.close();//On ferme les flux
                fluxStudent.close();
		this.creerCours(liste) ; //On cree les cours et tous les elements necessaire 
		this.creerJour(liste2);
		this.creerEtudiant(liste);
		this.creerTd(liste2);
                  this.inscrireEtudiant();
                if(!this.verfierFichier(liste2, liste)){
                    return false ;
                }
                
		
		this.lecture = true ;
                return true ; 
		} 
        
        /** 
 * Methode qui permet lire les fichier de notre projet avec des chemins specifique
 *
 * 
 
 * 
 */   
        public boolean lireFichier(String cheminEdt , String cheminEtu) throws IOException {
            this.viderListe();
            Creneau.setCreneau(1);
            Jour.resetNbJour();
            Cours.resetNbCours();
		
		this.m_pathFichierEdt = cheminEdt ;
		this.m_pathFichierStudent = cheminEtu ; 
		this.ouvrirFlux(0);
		this.ouvrirFlux(1);
		return this.lireFichier();
	
	
		
		
		
		
		
		
}
        
        
         /** 
 * Methode qui permet ge gener le fichier de sortie des etudiant repartie dans les differents Td
 *
 * 
 
 * 
 */    
        
        public void ecrireFichierSortie() throws IOException {
	  PrintWriter sortie = new PrintWriter("resultat.csv") ; //On cree notre fichier de sortie
		
		Collection<Student> etu = this.m_listeEtudiantFichier.values() ; //On recupere nos etudiant
		ArrayList<Student> monArray = new ArrayList(etu) ;
		OrdreId monOrdre = new OrdreId() ; 
		Collections.sort(monArray,monOrdre);//On tri les etudiant en fonction de leur Id afin d'avoir les identifiant dans un ordre croissant dans le fichier
		
		ArrayList<ArrayList<String>> monFichier = new ArrayList<ArrayList<String>>() ; //Fichier a la fin la premire ArrayList correspond a la ligne et la deuxieme a l'ensemble des lignes
		
		int numColonne =1; //Indice de la colonne de notre Td
		Map<Integer,Td> colTd = new HashMap<Integer,Td>() ; //Map qui va nous permettre de stocker nos Td en fonction de leur colonne
		
		Map<Td,Integer> TdCol = new HashMap<Td,Integer>() ;  //Map qui va en fonction d'un Td attribuer sa colonne
		ArrayList<String> premeireLigne = new ArrayList<String>() ; //ArrayList qui represente la premiere ligne du fichier 
		premeireLigne.add("Id") ;

		for(Cours monCours : this.m_listeCoursFichier.values()) {//Pour chaque cours 
			
			for(Td monTd : monCours.getTd()) {//On recupere tous les Td de chaque cours afin de les ajouter a notre et on stock dans notre map le numero de colonne afin de pouvoir se situer pour placer les 0 1 lorque on ajoutera les etudiant
				colTd.put(numColonne, monTd) ;
				TdCol.put(monTd, numColonne);
				premeireLigne.add(";"+monTd.toString()) ;
				
				numColonne++ ; 
				}
			}
		
		monFichier.add(premeireLigne) ;
		
		
		
		for (Student monEtu : monArray) {//Pour chaque etudiant
			
			
			ArrayList<String>maLigne = new ArrayList<String>() ; //Ligne d'un etudiant
			maLigne.add(Integer.toString(monEtu.getIdentifiant() )) ;//On place le premier element qui correspond a l'identifiant
			
		
			
			for (Td lesTd : colTd.values()) {//Pour tout les Td
					
					
					if(lesTd.isPresent(monEtu)) {//On verifie si l'etudiant est present dans ce TD
						
						maLigne.add(TdCol.get(lesTd),";1");
					}
					else {
                                            maLigne.add(TdCol.get(lesTd),";0");

						
						
						
					}
				}
			monFichier.add(maLigne);
			

		

			
				
				
				
}
		String ligne ;//Ligne ecriture fichier

		
		for (ArrayList<String> e : monFichier) {//Pour chaque ligne du fichier
			ligne = "" ;
			for (String chaine : e) {//Pour chaque element de la ligne
				ligne = ligne.concat(chaine) ;
                        }
			ligne = ligne.concat("\n") ;
			sortie.write(ligne);//On ecrit notre ligne 
			

			
			
		}
		sortie.close();
		
	}
        
          /** 
 * Methode qui permet de changer la borne d'un Td
 *
 * 
 
 * 
 */   
        public boolean changerBorne() throws IOException {

              	if(this.lecture == false) {
              		System.out.println("Veuillez lire les fichiers avant !");//Si les fichier n'ont pas encore était lu 
              		
              		
              		return false ; 
              	}
        	
        	System.out.println("Changer la borne de TD de quels cours ? ");
        	Td unTd = null ; 
        	
        	for(Integer a  : this.m_idColNomCours.keySet()) {
        		System.out.println("Cours :" + this.m_listeCoursFichier.get(this.m_idColNomCours.get(a)) +"Choix:"+a); //Pour chaque cours on propose un choix
        		
        	}
        	int a = -1 ; 
                
                try {
                    a = m_user.nextInt() ; 
                    
                
                }catch(NumberFormatException | InputMismatchException e){
                    System.out.println("Saisir uniquement chiffre");
                     m_user.next() ;
                     this.changerBorne();
                
                
                }
        	if(this.m_idColNomCours.containsKey(a)) {
        		
        		for(Td mesTd : this.m_listeCoursFichier.get(this.m_idColNomCours.get(a)).getTd()) {//Pour chaque Td on propose
            		System.out.println("Td a remplacer ? "+ mesTd +" saisir 1 pour confirmer ou 0 pour refuser");
            		while(true) {
            			try {
            			a = m_user.nextInt() ;
            			if(a == 1 || a == 0) {//Si on as un refus ou un accord on change de Td
            				
            				break ;
            			}
            			else {
            				
            				System.out.println("Ne Correspond a aucun chiffre ");
            			}
            			}catch(NumberFormatException | InputMismatchException e) {
            				System.out.println("Saisir uniquement chiffre");
                                        m_user.next() ;
                                               
                                                
            				
            				
            			}
            	}
            		if(a==1) {//Si on as un accord on quitte la boucle de recherche de Td
            			unTd = mesTd ;
            			break ;
            			
            			
            			
            		}
            		
            }
        		
        	}
        	
        	if(a == 1){
                while(true) {
                                    System.out.println("Saisir borne :");

            			try {
            			int borne= m_user.nextInt() ;//On recupere la nouvelle borne
            			if(borne > 0) {
                                    unTd.setBorne(borne);
                                   
                                    break ; 
                                }
                                else {
            				
            				System.out.println("Saisir chiffre positif");
            			}
            				
            			}
            			
            			catch(InputMismatchException | NumberFormatException e) {
            				System.out.println("Saisir chiffre");
                                        m_user.next() ; 
            				
            				
            			}}
            	
               
            	
              
                
            	
        		
        		
        	}
        	return true ;
        	
        	
        	
        
       
        	
        	
        	
        	
        	
        	
        	
        }
          /** 
 * Methode qui permet de generer un nouveaux problemes
 *
 * 
 
 * 
 */   
        
        public void genererNouveauPb() throws FileNotFoundException{
            
            ArrayList<Student>nouveauEtu = new ArrayList<Student>() ;
            ArrayList<Cours>mesCours = new ArrayList<Cours>() ;
            
            ArrayList<Jour>mesJour = new ArrayList<Jour>() ; 
            Random monAleatoire = new Random() ; 
            
            
            for(int i= 0 ; i < GestionGroupe.bonusCours.length ; i++){//On cree tous nos nouveau cours
                mesCours.add(new Cours(GestionGroupe.bonusCours[i])) ; 
            }
            
            
            
            for (int i = 0 ; i <= 100 ; i++){ //On cree 100 nouveaux etudiant
                Student monEtu = new Student(i) ;
                for (Cours unCours : mesCours){
                    
                    switch (monAleatoire.nextInt(3)){ //Pour chaque cours on attribue un nombre de Td alaeatoire
                        case 0 :
                            
                            unCours.ajouterTd(new Td(unCours.toString()+ "_1")) ; 
                            break ; 
                        case 1 :
                            for(int j = 1 ; j < 2; j++){
                            	Td newTd = new Td(unCours.toString()+ "_" + Integer.toString(j)) ;
                            	newTd.setId(j);
                                unCours.ajouterTd(newTd) ;
                            }
                            break ; 
                        
                        case 2 : 
                            for(int j = 1 ; j < 3; j++){
                            	Td newTd = new Td(unCours.toString()+ "_" + Integer.toString(j)) ;
                            	newTd.setId(j);
                                unCours.ajouterTd(newTd) ;
                            }
                            break ; 
                            default :
                            	break ; 
                    
                   }
                    if(monAleatoire.nextBoolean()){//Une fois sur deux on inscrit l'etudiant a un cours
                        unCours.ajouterEtudiant(monEtu) ; 
                        monEtu.ajouterCours(unCours);
                    
                    }
                }
                nouveauEtu.add(monEtu);
            }
            
            
            
            
            for(int i =0 ; i < GestionGroupe.nomJour.length ; i++){ //On crees nos jours avec nos creneaux possibilite de les fixer a l'aide d'une methode
                Jour newJour = new Jour(GestionGroupe.nomJour[i]) ; 
                for(int j = 0 ; j < GestionGroupe.nbCreneauJour ; j++){
                    Creneau nouveauCreneau = new Creneau();
		    nouveauCreneau.setJout(newJour);
		    newJour.ajouterCreneau(nouveauCreneau);
                }
                mesJour.add(newJour) ; 
              }
            
            
         
            
           for (Cours monCours : mesCours){
                boolean placer = false ;
                 
                


                for (Jour j : mesJour){
              
                for (Creneau c : j.getAllCreneau()){ //Pour chaque creneau du jour
                    
                    if(c.getNumCours() < 1 ){//Si il y'a pas de cours dans le crenezau
                    	
                    	if( c.ajouterCours(monCours) ) { //Si ajouterCours s'effectue bien dans le creneau on ajoute un creneau au cours
                    		monCours.setCreneau(c);
                    		
                            placer = true ; //Variable qui permet de savoir si on as placer le cours
                            break ; 
                    		
                    		
                    	}
                       
                        
                    }
                }
                if(placer){ //Si on as placer le cours on peut quitter la boucle pour le cours suivant 
                    break ; 
                }
              
             }
         }
            
            for (Cours monCours : mesCours){//Meme principe que le fonctionnement duu desus
                
                for (Td monTd : monCours.getTd()){
                    
                    boolean placer = false ;
                for (Jour j : mesJour){
                
                for (Creneau c : j.getAllCreneau()){
                    
                    if(c.getNumTd() < 1){//Au maximum 1 cours et 1 td en parralele dans un creneau
                    	
                    	if(c.ajouterCours(monTd)) {
                    		monTd.setCreneau(c);
                            
                            placer = true ; 
                            break ; 
                    		
                    		
                    	}
                        
                    }
                }
                if(placer){
                    break ; 
                }
                    
                    
                }
                
                
                
               
             }
                
                
         }
            
       ArrayList<ArrayList<String>> monFichier = new ArrayList<ArrayList<String>>() ; //ArrayList qui va representer fichier etudiant
       int numColonne =1; //Index de la colonne du fichier
        Map<Integer,Cours> colTd = new HashMap<Integer,Cours>() ;  //Map qui assigner un numero de colonne a un cours
		
		Map<Cours,Integer> TdCol = new HashMap<Cours,Integer>() ;//Map qui va assigner un Cours a un numero de colonne
       ArrayList<String> premeireLigne = new ArrayList<String>() ; 
       	  PrintWriter sortie = new PrintWriter("etuGene.csv") ; 

       premeireLigne.add("Id") ;
       
       for(Cours monCours : mesCours) {//Constitution de la premiere ligne qui est celle des cours
           colTd.put(numColonne, monCours) ;
            TdCol.put(monCours, numColonne);
	premeireLigne.add(";"+monCours.toString()) ;
				
	numColonne++ ; 

			
			}
		
		monFichier.add(premeireLigne) ;//On ajoute la premiere ligne a l'ArrayList qui represente le fichier etudiant
		
		
		
		for (Student monEtu : nouveauEtu) {//Pour chaque etudiant
			
			
			ArrayList<String>maLigne = new ArrayList<String>() ; 
			maLigne.add(Integer.toString(monEtu.getIdentifiant() )) ; //On ajoute identifiant de l'etudiant a la ligne
			
		
			
			for (Cours lesTd : colTd.values()) {//Pour tous les Td
					
					
					if(lesTd.isPresent(monEtu)) {//On verifie si l'etudiant est present dans le Td
						
						maLigne.add(TdCol.get(lesTd),";1");
					}
					else {
						maLigne.add(TdCol.get(lesTd),";0");
						
						
						
					}
				}
			monFichier.add(maLigne);//On ajoute la ligne qui correspond a l'etudiant et on passe a l'etudiant suivant
			

		

			
				
				
				
}
		String ligne = "" ;

		
		for (ArrayList<String> e : monFichier) {//pour chaque ligne
			ligne = "" ;
			for (String chaine : e) {//Pour chaque element de la ligne
                        ligne = ligne.concat(chaine) ;
	}
			ligne = ligne.concat("\n") ;
			sortie.write(ligne);//On ecrit la ligne
			

			
			
		}
		sortie.close();//On ferme le fichier
		
		String[][] emploiDuTemp = new String[GestionGroupe.nomJour.length][GestionGroupe.nbCreneauJour];//Tableau qui represente notre emploi du temps
		ArrayList<String> monFichier2 = new ArrayList<String>() ; //ArrayList qui represente fichier emploi du temp
		String premierLigne = "" ;
		for(int i = 0 ; i < GestionGroupe.nomJour.length ; i++) {//Premiere ligne du fichier qui correspond au jour
			if(i==0) {
				 premierLigne = premierLigne.concat(GestionGroupe.nomJour[i]) ;//Si premier element on place pas le point virgule
				
				
			}else {
				premierLigne = premierLigne.concat(";"+GestionGroupe.nomJour[i]) ;//Si se n'est pas le premier element on place le point virgule
				
				
			}
			
			
		}
		premierLigne = premierLigne.concat("\n") ;//On place le caractere de fin de la ligne
		
		monFichier2.add(premierLigne);
		int i=0 , j= 0 ; //variable qui vont nous permettre de se deplacer dans notre emploi du temps
                
                
                
                
                
               for(Jour monJour : mesJour){//Pour chaque jour
            	   j=0 ;//Lecture verticale de l'emploi du temps on traite un jour puis tous ses creneaux 
            	   
            	   for(Creneau e : monJour.getAllCreneau()) {
            		   boolean plusieur = false ;
            		   
            		   for(Cours cours : e.getCours()) {
            			   
            			   if(plusieur) {
            				   emploiDuTemp[i][j] =  emploiDuTemp[i][j].concat(" "+cours.toString()) ; //Si il y'a deja un element on rajoute un espace
            				   }
            			   else {
            				   emploiDuTemp[i][j] = cours.toString() ; //Sinon on ajoute le cours sans espace 
            				   plusieur = true ;
            				   
            			   }
            			  }
            		   j++ ;//On incremente la variable pour changer de creneau dans l'emploi du temsp 
            		  
            		   
            		   
            		   
            		   
            		   
            	   }
            	   i++;//On incremente la variable qui represente un jour dans notre emploi du temps 
  }
               
               
               
               
               
               for(i =0 ; i < GestionGroupe.nbCreneauJour ; i++) { //Lecture horizontale cette fois on traite tous les creneaux afin d'avoir une ligne directement qu'on puissent ecrire
            	   String maLigne = "" ; 
            	   
            	   for(j=0 ; j < GestionGroupe.nomJour.length ; j++) {//Pour un creneau donnee on parcourt tous les jours
            		   
            		   if(j==0) {//Si premier element on ne met pas de point virgule
            			   
            			   if(emploiDuTemp[j][i] != null) {//Si le jour associer au creneau n'est pas vide
            				   maLigne = maLigne.concat(emploiDuTemp[j][i]);
            				   
            				   
            			   }
            			   
            			   
            			   
            		   }else {//Si ce n'est pas le premier element on met le point virgule
            			   if(emploiDuTemp[j][i] != null) {
                			   maLigne = maLigne.concat(";"+emploiDuTemp[j][i]);
            				   
            				   
            			   }
            			   
            			   
            			   
            		   }
            		   
            	   }
            	   
            	   maLigne = maLigne.concat("\n");//On ajoute la ligne
            	   monFichier2.add(maLigne) ;
            	   
               }
               
            	  PrintWriter sortie2 = new PrintWriter("edtGene.csv") ; 

               
               
               for (String e : monFichier2) {
       			
       			sortie2.write(e);
       			

       			
       			
       		}
               sortie2.close();
               
               
               
               
               
                
       
       
       
}
         /** 
 * Methode non finaliser
 *
 * 
 
 * 
 */    
        
        public void bonus2() {


	int b = 0 ;
	
	for(Integer a  : this.m_idColNomCours.keySet()) {
		System.out.println(this.m_listeCoursFichier.get(this.m_idColNomCours.get(a)) +":"+a);
		b=a ;
		
	}
	Td unTd = null ; 
	
	
	for(Td mesTd : this.m_listeCoursFichier.get(this.m_idColNomCours.get(6)).getTd()) {
		System.out.println("Td a remplacer ? ");
		unTd = mesTd ; 
}
	
	
	ArrayList<Creneau> possible = new ArrayList<Creneau>() ; 
	ArrayList<Student>ar = unTd.getEtu(); 
	for(Jour unJour : this.m_listeJourFichier.values()) {
		
		
		for(Creneau e : unJour.getAllCreneau()) {
			
			for(Cours unCours : e.getCours()) {
				boolean placement = true ; 

				
				for(Student a : unCours.getEtu()) {
					
					if(! (unCours instanceof Td)) {
						
						if(unCours.toString().equals(unTd.toString().split("_")[0]) || ar.contains(a)) {
							placement = false ;
							
							break ;
							
						}
					}
					else {
						
						if(ar.contains(a)) {
							placement = false ;
							
							break ; 
						}
					}
					}
				
				if(placement == true) {
					
					possible.add(e) ;
				}
			}
		}
		
		
		
		
		
	}
	
	for(Creneau e : possible) {
		
		System.out.println(e.getJour() +  e.toString() );
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
}
        
         /** 
 * Methode récursive qui tentent d'affecter des étudiants a un Td
 *
 * 
 
 * 
 */    
        
        public boolean affecter() throws IOException {
        	
        	if(this.lecture == false) {
        		System.out.println("Veuillez lire les fichiers avant !");
        		
        		
        		return false ; 
        	}
        	else {
                  
                    
        		this.setContrainteEtu();
        		ArrayList<Student>mesEtu = new ArrayList<Student>(this.m_listeEtudiantFichier.values());
        		
        		int essai = 0;
        		
        		while(!this.assignerEtudiant(mesEtu)) {
        			if(essai>GestionGroupe.m_essai) {
                                    return false ; 
        				
        			}
        			essai++; 
        			
        		}
        		
        		this.ecrireFichierSortie();
        		
        		
        	}
        	return true ;
        	
        	
        }
        
         /** 
 * Methode qui permet de vider toutes les listeq
 *
 * 
 
 * 
 */    
        
        private void viderListe(){
            
            this.m_listeCoursFichier.clear();
            this.m_listeEtudiantFichier.clear();
            this.m_idColNomCours.clear();
            this.m_idColNomJour.clear();
            this.m_listeEtudiantPb.clear();
            this.m_listeJourFichier.clear();
            
        
        
        }
        
         /** 
 * Methode qui renvoi le nombre de cours 
 *
 * 
 
 * 
 */    
        public int getNbCours(){
            
            return this.m_listeCoursFichier.size() ; 
        
        
        }
         /** 
 * Methode qui renvoie le nombre d'etudiant
 *
 * 
 
 * 
 */    
        public int getNbEtudiant(){
        
        return this.m_listeEtudiantFichier.size() ; 
        }
        
           /** 
 * Methode qui affiche les etudiant qui n'ont pas pu etre traiter
 *
 * 
 
 * 
 */    
        
        public void afficherEtudiantPb(){
        
        Collection<Student> mesEtu = this.m_listeEtudiantPb.values() ; 
        
        if(!mesEtu.isEmpty()){
         System.out.println("Etudiant non traiter du a une erreur de saisie dans le fichier etudiant !");
        for (Student a : mesEtu){
            System.out.println(a);
        
        
        }
        
        
        
        }
       
        
        
        }
        
           /** 
 * Methode qui les Td et les etudiant qui l'ai compose 
 *
 * 
 
 * 
 */    

        public void afficherTdEtudiant(){
            for (Cours e : this.m_listeCoursFichier.values()) {
                    for (Td z : e.getTd()) {
                        
                        System.out.println(z+ ":");
				
				z.afficherEtudiant();

			}
		
		
			}
            System.out.println("Affectation reussie !\nFichier dans le dossier d'ou vous lancer l'application : resultat.csv");


}

















}


	


