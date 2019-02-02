
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
	private static final int m_essai = 1000;
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
        
        private static final String[] bonusCours = {"JAVA","IA","Sociologie","Bakhaw"} ; 
        private static final String[] nomJour = {"Lundi","Mardi","Mercredi","Jeudi","Vendredi"} ; 
        private static final int nbCreneauJour = 8 ; 

	public GestionGroupe() throws IOException {
		
		m_user = new Scanner(System.in);
                
                this.bonus();
		//this.ouvrirFlux(0);
		//this.ouvrirFlux(1);
		

		
		
		//this.inscrireEtudiant();
		//this.setContrainteEtu();
		//this.assignerEtudiant(0);


		//ArrayList<String> liste2 = this.lireFichier(fluxEdt);
		//ArrayList<String> liste = this.lireFichier(fluxStudent);
	
		/*for (Cours e : this.m_listeCoursFichier.values()) {

			for (Td z : e.getTd()) {
				System.out.println(z + " :");
				z.afficherEtudiant();

			}

		}*/

	}
	
	private ArrayList<Cours> determinerChaine(String chaine) {
		ArrayList<Cours> retour = new ArrayList<Cours>();
		if (chaine.contains(" ")) {
			String[] td = chaine.split(" ");
			for (int i = 0; i < td.length; i++) {

				if (td[i].contains("_")) {
					Td unTd = new Td(td[i]);
					unTd.setId(Integer.parseInt(td[i].split("_")[1]));
					retour.add(unTd);

				} else {
					if (m_listeCoursFichier.containsKey(td[i])) {
						retour.add(m_listeCoursFichier.get(td[i]));
					}

					
				}
			}
			return retour;

		} else if (chaine.contains("_")) {
			Td unTd = new Td(chaine);
			unTd.setId(Integer.parseInt(chaine.split("_")[1]));
			retour.add(unTd);
			return retour;

		} else {

			if (m_listeCoursFichier.containsKey(chaine)) {
				retour.add(m_listeCoursFichier.get(chaine));
				return retour;

			} else {

				return null;
			}

		}

	}
        public static ArrayList<String> lireFichier(FileInputStream fichier) throws IOException {
		BufferedReader flux_entree = new BufferedReader(new InputStreamReader(fichier, "UTF-8"));
		String ligne;
		ArrayList<String> retour = new ArrayList<String>();
		flux_entree.mark(1);

		if (flux_entree.read() != 0XFEFF) { // Permet de v�rifier la presence du mask order byte en UTF-8 au debut du
											// fichier (generer par exemple par notepad sur windows)
			flux_entree.reset(); // Si on rentre dans le if pas de presence de mask order byte donc on se remet
									// au precedent mark avant la lecture
		}
		while ((ligne = flux_entree.readLine()) != null) {
			retour.add(ligne);
		}

		return retour;
	}
        private void ouvrirFlux(int fichier) {
		switch (fichier) {
		case 0:
			try {
				this.fluxEdt = new FileInputStream(this.m_pathFichierEdt);
			} catch (FileNotFoundException e) {
				System.out.println("Fichier Emploi du temps introuvable !\nmodifier chemin: ");
				m_pathFichierEdt = m_user.next();
				this.ouvrirFlux(0);
			}
		case 1:
			try {
				this.fluxStudent = new FileInputStream(this.m_pathFichierStudent);
			} catch (FileNotFoundException e) {
				System.out.println("Fichier Etudiant introuvable !\n modifier chemin: ");
				this.m_pathFichierStudent = m_user.next();
				this.ouvrirFlux(1);
			}
			break;
		default:
			throw new IllegalArgumentException();

		}
	}
        private int getNbColonne(String ligne) {
		String[] nbColonne = ligne.split(";");
		return nbColonne.length;

	}
        public int afficherMenu() {
		int choix = -1;
		System.out.print(
				"1. Lire les fichiers d'entr�e par d�faut\n2. Lire les fichier d'entr�e en entrant le nom de chaque fichier au clavier\n3. Affecter les �tudiants dans les groupes de TDs\n4. Changer le nombre maximal d'�tudiants par groupe de TD\nChoix :");
		try {
			choix = m_user.nextInt();
			if (choix < 1 || choix > 4) {
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
        private boolean creerCours(ArrayList<String> liste) {

		int nbCours = this.getNbColonne(liste.get(0));
		for (int i = 1; i < nbCours; i++) {
			Cours nouveauCours = new Cours(liste.get(0).split(";")[i].replace(" ", ""));

			if (!(this.m_listeCoursFichier.containsKey(nouveauCours.toString()))) {
				m_listeCoursFichier.put(nouveauCours.toString(), nouveauCours);
				m_idColNomCours.put(nouveauCours.getId(), nouveauCours.toString());

			} else {

				return false;

			}

		}
		return true;

	}
        private boolean creerEtudiant(ArrayList<String> liste) {
		int nbCours = this.getNbColonne(liste.get(0));
		for (int i = 1; i < liste.size(); i++) {
			boolean etudiantValide = true;
			Student nouveauEtudiant = new Student(Integer.parseInt(liste.get(i).split(";")[0]));
			for (int j = 1; j < nbCours; j++) {
				int nombre;
				try {
					nombre = Integer.parseInt(liste.get(i).split(";")[j]);

				} catch (NumberFormatException e) {
					nombre = -1;

				}

				switch (nombre) {
				case 1:
					nouveauEtudiant.ajouterCours(m_listeCoursFichier.get(m_idColNomCours.get(j)));
					break;
				case -1:
					etudiantValide = false;
					break;
				}
			}

			if (etudiantValide) {
				if (!(this.m_listeEtudiantFichier.containsKey(nouveauEtudiant.getIdentifiant()))) {

					this.m_listeEtudiantFichier.put(nouveauEtudiant.getIdentifiant(), nouveauEtudiant);
					/*
					 * ArrayList<Cours> coursEtu = nouveauEtudiant.getCours() ; for (Cours inscrit :
					 * coursEtu) { inscrit.ajouterEtudiant(nouveauEtudiant); }
					 */
				} else {
					return false;

				}

			} else {

				if (!(this.m_listeEtudiantPb.containsKey(nouveauEtudiant.getIdentifiant()))) {
					m_listeEtudiantPb.put(nouveauEtudiant.getIdentifiant(), nouveauEtudiant);
				} else {
					return false;

				}

			}

		}
		return true;

	}
        private void creerJour(ArrayList<String> liste) {

		for (int i = 0; i < 5; i++) {
			Jour nouveauJour = new Jour(liste.get(0).split(";")[i]);
			m_listeJourFichier.put(nouveauJour.getNom(), nouveauJour);
			m_idColNomJour.put(nouveauJour.getIdJour(), nouveauJour.getNom());

			for (int j = 0; j < liste.size() - 1; j++) {
				Creneau nouveauCreneau = new Creneau();
				nouveauCreneau.setJout(nouveauJour);
				nouveauJour.ajouterCreneau(nouveauCreneau);
			}
		}

	}
        public void afficherEmploiDuTemp() {

		TreeMap<Integer, String> mapOrdo = new TreeMap<Integer, String>(m_idColNomJour);
		Collection<String> c = mapOrdo.values();
		for (String e : c) {
			m_listeJourFichier.get(e).afficherCreneau();
		}

	}
        private boolean creerTd(ArrayList<String> liste) {

		for (int i = 0; i < liste.get(0).split(";").length; i++) {
			Jour monJour = m_listeJourFichier.get(m_idColNomJour.get(i));
			for (int j = 1; j < liste.size(); j++) {

				ArrayList<Cours> monCoursCreneau = this.determinerChaine(liste.get(j).split(";", -1)[i]);

				if (monCoursCreneau != null) {

					for (int x = 0; x < monCoursCreneau.size(); x++) {
						if (monCoursCreneau.get(x) instanceof Td) {

							Td unTd = (Td) monCoursCreneau.get(x);

							if (m_listeCoursFichier.containsKey(unTd.toString().split("_")[0])) {

								monJour.getCreneau(j - 1).ajouterCours(unTd);
								unTd.setCreneau(monJour.getCreneau(j - 1));
								m_listeCoursFichier.get(unTd.toString().split("_")[0]).ajouterTd((Td) unTd);

								// m_listeCoursFichier.get(unTd.toString().split("_")[0]).setCreneau(monJour.getCreneau(j-1));
							} else {

								return false;

							}
						} else if (monCoursCreneau.get(x) instanceof Cours) {

							if (m_listeCoursFichier.containsKey(monCoursCreneau.get(x).toString())) {
								monJour.getCreneau(j - 1).ajouterCours(m_listeCoursFichier.get(monCoursCreneau.get(x).toString()));
								m_listeCoursFichier.get(monCoursCreneau.get(x).toString()).setCreneau(monJour.getCreneau(j - 1));
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
        private void setContrainteEtu() {
		Collection<Student> e = m_listeEtudiantFichier.values();

		for (Student t : e) {
			ArrayList<Cours> r = t.getCours();

			ArrayList<Creneau> creneauParra = new ArrayList();

			for (Cours f : r) {

				Collection<Td> mesTd = f.getTd();
				Map<Creneau, Td> CreneauPossible = new HashMap<Creneau, Td>();
				for (Td z : mesTd) {

					if (!CreneauPossible.containsKey(z.getCreneau())) {
						CreneauPossible.put(z.getCreneau(), z);
					}
				} // Savoir si groupes de TD appartenant au meme cours sont dans le meme creneau

				Collection<Creneau> d = CreneauPossible.keySet();
				for (Creneau y : d) {
					creneauParra.add(y);
				}

			}

			Map<Creneau, Integer> detectCreneauDouble = new HashMap<Creneau, Integer>();

			for (Creneau j : creneauParra) {

				if (!(detectCreneauDouble.containsKey(j))) {

					detectCreneauDouble.put(j, 0);

				} else {
					t.incrementContrainte();
				}

			}

		}

	}
        private boolean assignerEtudiant(int essai) {


		ArrayList<Student> er = new ArrayList<Student>(this.m_listeEtudiantFichier.values());

		Collections.sort(er);

		ArrayList<ArrayList<Student>> ter = new ArrayList<ArrayList<Student>>();
		ArrayList<Student> prio = new ArrayList<Student>();
		int max = er.get(0).getContrainte();
		for (Student erd : er) {
			int maxPre = erd.getContrainte();
			if (maxPre != max) {
				ter.add(prio);
				prio = new ArrayList<Student>();
				prio.add(erd);
			} else {
				prio.add(erd);
			}

		}

		ter.add(prio);
                
                for(int i = 0 ; i  < GestionGroupe.m_essai ; i++){
                    for (ArrayList<Student> monNiveauContrainte : ter) {

			Collections.shuffle(monNiveauContrainte);

			for (Student monEtudiant : monNiveauContrainte) {

				ArrayList<Cours> tg = monEtudiant.getCours();
				boolean placer = false;

				for (Cours f : tg) {

					for (Td z : f.getTd()) {

						if ((!(z.isFull()))) {
							// z.getCreneau().ajouterCours(f);

							if ((z.ajouterEtudiant(monEtudiant))) {
								monEtudiant.ajouterTd(z);
								placer = true;
								break;
							}
						}
					}

					if (placer == false) {

						if (essai < GestionGroupe.m_essai) {
							essai++;
							//this.assignerEtudiant(essai);

						} else {

							return false;
						}

					}

				}

			}

		}
                    
                    return true ;
                
                
                
                
                
                
                }
                return true ; 

		

	}
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
        private void inscrireEtudiant() {


		for (Student etu : this.m_listeEtudiantFichier.values()) {
			ArrayList<Cours> coursEtu = etu.getCours();
			for (Cours inscrit : coursEtu) {
				inscrit.ajouterEtudiant(etu);
			}

		}

	}
        public void lireFichier() throws IOException {
		m_listeCoursFichier = new HashMap<String, Cours>();
		m_listeJourFichier = new HashMap<String, Jour>();
		m_idColNomCours = new HashMap<Integer, String>();
		m_idColNomJour = new HashMap<Integer, String>();
		m_listeEtudiantFichier = new HashMap<Integer, Student>();
		m_listeEtudiantPb = new HashMap<Integer, Student>();
		this.ouvrirFlux(0);
		this.ouvrirFlux(1);
		ArrayList<String> liste2 = this.lireFichier(fluxEdt);
		ArrayList<String> liste = this.lireFichier(fluxStudent);
		this.creerCours(liste) ; 
		this.creerJour(liste2);
		this.creerEtudiant(liste);
		this.creerTd(liste2);
		
		
		this.inscrireEtudiant();
		this.setContrainteEtu();
		this.assignerEtudiant(0);
		this.ecrireFichierSortie();
		} 
	public void lireFichier(String cheminEdt , String cheminEtu) throws IOException {
		m_listeCoursFichier = new HashMap<String, Cours>();
		m_listeJourFichier = new HashMap<String, Jour>();
		m_idColNomCours = new HashMap<Integer, String>();
		m_idColNomJour = new HashMap<Integer, String>();
		m_listeEtudiantFichier = new HashMap<Integer, Student>();
		m_listeEtudiantPb = new HashMap<Integer, Student>();
		this.m_pathFichierEdt = cheminEdt ;
		this.m_pathFichierStudent = cheminEtu ; 
		this.ouvrirFlux(0);
		this.ouvrirFlux(1);
		ArrayList<String> liste2 = this.lireFichier(fluxEdt);
		ArrayList<String> liste = this.lireFichier(fluxStudent);
		this.creerCours(liste) ; 
		this.creerJour(liste2);
		this.creerEtudiant(liste);
		this.creerTd(liste2);
		
		
		
		
		
}
        public void ecrireFichierSortie() throws IOException {
	  PrintWriter sortie = new PrintWriter("sortie.csv") ; 
		
		Collection<Student> etu = this.m_listeEtudiantFichier.values() ; 
		ArrayList<Student> monArray = new ArrayList(etu) ; 
		OrdreId monOrdre = new OrdreId() ; 
		Collections.sort(monArray,monOrdre);
		
		ArrayList<ArrayList<String>> monFichier = new ArrayList<ArrayList<String>>() ; 
		
		int numColonne =1; 
		Map<Integer,Td> colTd = new HashMap<Integer,Td>() ; 
		
		Map<Td,Integer> TdCol = new HashMap<Td,Integer>() ; 
		ArrayList<String> premeireLigne = new ArrayList<String>() ; 
		premeireLigne.add("Id") ;

		for(Cours monCours : this.m_listeCoursFichier.values()) {
			
			for(Td monTd : monCours.getTd()) {
				colTd.put(numColonne, monTd) ;
				TdCol.put(monTd, numColonne);
				premeireLigne.add(";"+monTd.toString()) ;
				
				numColonne++ ; 
				}
			}
		
		monFichier.add(premeireLigne) ;
		
		
		
		for (Student monEtu : monArray) {
			int num = 1 ;
			boolean presence = true  ;
			
			ArrayList<String>maLigne = new ArrayList<String>() ; 
			maLigne.add(Integer.toString(monEtu.getIdentifiant() )) ;
			
			for(int i = 0 ; i < colTd.size() ; i++) {
				//maLigne.add(Integer.toString(0));
				
			}
			
			for (Td lesTd : colTd.values()) {
					
					
					if(lesTd.isPresent(monEtu)) {
						
						maLigne.add(TdCol.get(lesTd),";1");
					}
					else {
						maLigne.add(TdCol.get(lesTd),";0");
						
						
						
					}
				}
			monFichier.add(maLigne);
			

		

			
				
				
				
}
		String ligne = "" ;

		
		for (ArrayList<String> e : monFichier) {
			ligne = "" ;
			for (String chaine : e) {
				ligne = ligne.concat(chaine) ;
	}
			ligne = ligne.concat("\n") ;
			sortie.write(ligne);
			

			
			
		}
		sortie.close();
		for (Cours e : this.m_listeCoursFichier.values()) {

			for (Td z : e.getTd()) {
				System.out.println(z + " :");
				z.afficherEtudiant();

			}
		
		System.out.println("");
			}
		
		
			
			
			
			
		
				
				
		
		
		
	}
        
       
        
        public void bonus() throws FileNotFoundException{
            
            ArrayList<Student>nouveauEtu = new ArrayList<Student>() ;
            ArrayList<Cours>mesCours = new ArrayList<Cours>() ;
            
            ArrayList<Jour>mesJour = new ArrayList<Jour>() ; 
            Random monAleatoire = new Random() ; 
            
            
            for(int i= 0 ; i < GestionGroupe.bonusCours.length ; i++){
                mesCours.add(new Cours(GestionGroupe.bonusCours[i])) ; 
            }
            
            
            
            for (int i = 0 ; i < 100 ; i++){
                Student monEtu = new Student(i) ;
                for (Cours unCours : mesCours){
                    
                    switch (monAleatoire.nextInt(3)){
                        case 0 :
                            
                            unCours.ajouterTd(new Td(unCours.toString()+ "_1")) ; 
                            break ; 
                        case 1 :
                            for(int j = 1 ; j <= 2 ; j++){
                                unCours.ajouterTd(new Td(unCours.toString()+ "_" + Integer.toString(j))) ;
                            }
                            break ; 
                        
                        case 2 : 
                            for(int j = 1 ; j <= 3 ; j++){
                                unCours.ajouterTd(new Td(unCours.toString()+ "_" + Integer.toString(j))) ;
                            }
                            break ; 
                    
                   }
                    if(monAleatoire.nextBoolean()){
                        unCours.ajouterEtudiant(monEtu) ; 
                        monEtu.ajouterCours(unCours);
                    
                    }
                }
                nouveauEtu.add(monEtu);
            }
            
            
            
            
            for(int i =0 ; i < GestionGroupe.nomJour.length ; i++){
                Jour newJour = new Jour(GestionGroupe.nomJour[i]) ; 
                for(int j = 0 ; j < GestionGroupe.nbCreneauJour ; j++){
                    Creneau nouveauCreneau = new Creneau();
		    nouveauCreneau.setJout(newJour);
		    newJour.ajouterCreneau(nouveauCreneau);
                }
                mesJour.add(newJour) ; 
              }
            
            
            int limiteTdParra = 4 ; 
            //int limiteCoursParra = 1  ;
              int nbCours = 0 ; 
            
           for (Cours monCours : mesCours){
                boolean placer = false ;
                 
                


                for (Jour j : mesJour){
              
                for (Creneau c : j.getAllCreneau()){
                    
                    if(c.getNumCours() < 1 ){
                        c.ajouterCours(monCours) ; 
                        monCours.setCreneau(c);
                        placer = true ; 
                        break ; 
                    }
                }
                if(placer){
                    break ; 
                }
                
             }
         }
            
            for (Cours monCours : mesCours){
                
                for (Td monTd : monCours.getTd()){
                    
                    boolean placer = false ;
                for (Jour j : mesJour){
                
                for (Creneau c : j.getAllCreneau()){
                    
                    if(c.getNumTd() < 1 ){
                        c.ajouterCours(monTd) ; 
                        monTd.setCreneau(c);
                        placer = true ; 
                        break ; 
                    }
                }
                if(placer){
                    break ; 
                }
                    
                    
                
                }
                if(placer){
                    break ; 
                }
             }
                
         }
            
       ArrayList<ArrayList<String>> monFichier = new ArrayList<ArrayList<String>>() ; 
       int numColonne =1; 
		Map<Integer,Cours> colTd = new HashMap<Integer,Cours>() ; 
		
		Map<Cours,Integer> TdCol = new HashMap<Cours,Integer>() ;
       ArrayList<String> premeireLigne = new ArrayList<String>() ; 
       	  PrintWriter sortie = new PrintWriter("sortie2.csv") ; 

       premeireLigne.add("Id") ;
       
       for(Cours monCours : mesCours) {
           colTd.put(numColonne, monCours) ;
            TdCol.put(monCours, numColonne);
	premeireLigne.add(";"+monCours.toString()) ;
				
	numColonne++ ; 

			
			}
		
		monFichier.add(premeireLigne) ;
		
		
		
		for (Student monEtu : nouveauEtu) {
			int num = 1 ;
			boolean presence = true  ;
			
			ArrayList<String>maLigne = new ArrayList<String>() ; 
			maLigne.add(Integer.toString(monEtu.getIdentifiant() )) ;
			
			for(int i = 0 ; i < colTd.size() ; i++) {
				//maLigne.add(Integer.toString(0));
				
			}
			
			for (Cours lesTd : colTd.values()) {
					
					
					if(lesTd.isPresent(monEtu)) {
						
						maLigne.add(TdCol.get(lesTd),";1");
					}
					else {
						maLigne.add(TdCol.get(lesTd),";0");
						
						
						
					}
				}
			monFichier.add(maLigne);
			

		

			
				
				
				
}
		String ligne = "" ;

		
		for (ArrayList<String> e : monFichier) {
			ligne = "" ;
			for (String chaine : e) {
				ligne = ligne.concat(chaine) ;
	}
			ligne = ligne.concat("\n") ;
			sortie.write(ligne);
			

			
			
		}
                
                ArrayList<String> monDeuxiemeFichier = new ArrayList<String>() ; 
                
                
               /* for(Jour monJour : mesJours){
                
                
                
                }*/
                
       
       
       

                
                
 
            
         
                
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
       
	
}}


	


