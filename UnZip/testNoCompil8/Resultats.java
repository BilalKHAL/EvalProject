import java.io.*;
import java.time.Duration;
import java.util.*;
import java.text.SimpleDateFormat;

public class Resultats {

	public List<Participants> listeparticipants;
	public List<Binome> listebinome;
    // Resultats liste;
    Resultats course;
    
	public Resultats() {
		listeparticipants=new ArrayList<Participants>();
	}
	
	/** Méthode ajouterParticipants est une méthode qui prend 5 arguments : prenom, nom, nationalité, sexe, age et temps.
	 * Cette méthode permet d'ajouter un concurrent à la liste.
	 */	
	public void ajouterParticipants (String prenom, String nom, String nationalite, String sexe, String age, Duration temps){
		Participants p = new Participants(prenom, nom, nationalite, sexe, age, temps);
		this.listeparticipants.add(p);	
	}
	
	/** Méthode Lecture permet de lire le fichier test et crée une liste avec l'ensemble des coureurs.
	 */
	public void lecture(String filename){
		course = new Resultats();
		try{
		    //Scanner scanLine = new Scanner(new File("results.csv"));
		    Scanner scanLine = new Scanner(new File(filename));
			while (scanLine.hasNextLine()){
			String line = scanLine.nextLine();
			Scanner scan = new Scanner(line);
			scan.useDelimiter(";");
			String prenom = scan.next();
			String nom = scan.next();
			String nationalite = scan.next();
			String categorie = scan.next();
			String sexe=categorie.substring(0, 1);
			String age=categorie.substring(1);
			String temps = scan.next();
			Scanner ts = new Scanner(temps);
			ts.useDelimiter(":");
			int hours = ts.nextInt();
			int min = ts.nextInt();
			int sec = ts.nextInt();
			Duration duration = Duration.ZERO;
			duration = duration.plusHours(hours);
			duration = duration.plusMinutes(min);
			duration = duration.plusSeconds(sec);
			
			course.ajouterParticipants(prenom, nom, nationalite, sexe, age, duration);
			scan.close();
			ts.close();	
			}
			scanLine.close();
		}
		catch(FileNotFoundException e){
			System.out.println("Results file not found!");
		}
	
	}
	
	/* Méthode convert prend comme argument un temps de type duration.
	 * La méthode retourne temps sous le format : 1h28'06". Temps est converti en String.
	 */
	public static String convert(Duration temps){
		long s = Math.abs(temps.getSeconds());
		String time=String.format("%dh%02d'%02d''", s / 3600, (s % 3600) / 60, (s % 60));
		return time;
	}
	
	/* Méthode translate prend comme argument un nombre de seconde au format Long.
	 * La méthode retourne temps sous le format : 1h28'06". Temps est converti en String.
	 */
	public static String translate(long s){
		long heure   = s / 60 / 60 % 24;
		long minute  = s / 60 % 60;
		long seconde = s % 60;
		String time=String.format("%dh%02d'%02d''", heure, minute, seconde);
		return time;
	}
	
	/* Méthode affiche permet d'afficher la liste de participants.
	 */
	public void affiche(){
		Integer compteur = 1;
		for (Participants p :listeparticipants){
				System.out.println("Position :"+compteur+" / Nom: "+p.getnom()+" / Prénom: "+p.getprenom()+" / Nationalité: "+p.getnationalite()+" / Catégorie: "+p.getsexe()+p.getage()+" / Temps: "+convert(p.gettemps()));
				compteur+=1;
			}
	}

	/* Méthode Tri utilise la classe Tri afin de trier les coureurs de "listeparticipants" en fonction de leur temps
	 */
	public void Tri(){
		Comparator<Participants> temps=new Tri();
		Collections.sort(this.listeparticipants,temps);
	}	
		
	/* Méthode Extraire prend trois arguments en paramètres : 
	 * -c orrespoond au champs choisi (prenom, nom, nationalite, sexe, age)
	 * -v correspond à la valeur donnée à ce champs
	 * -Source est une liste de participants.
	 * La méthode Extraire retourne une liste de participants à partir du fichier Source.
	 * Cette nouvelle liste ne contiendra que les participants de Source pour lesquels la valeur du champs choisi c est égale à la valeur v entrée en paramètre.
	 */
	public static List<Participants> Extraire(String c, String v, List<Participants> Source){
		List<Participants> listP = new ArrayList<Participants>();
		Iterator<Participants> iter = Source.iterator();
		while (iter.hasNext()) {
		    Participants p = iter.next();
		    if (p.get(c).equals(v)==true)
		        listP.add(p);
		}
		return listP;
	}
		
	/* Méthode Supprimer prend 4 arguments en paramètre : c1,c2 qui sont des champs (prenom, nom, nationalite, sexe, age) et v1,v2 qui sont des valeurs.
	 * Cette méthode supprime de la liste le participant pour lequel les valeurs respectives des champs c1 et c2 sont égales aux valeurs entrées v1 et v2.
	 */	
	public void delete(String c1, String c2, String v1, String v2){
		Iterator<Participants> iter = listeparticipants.iterator();
		while (iter.hasNext()) {
		    Participants p = iter.next();
		    if ((p.get(c1).toLowerCase().equals(v1.toLowerCase())==true)&&p.get(c2).toLowerCase().equals(v2.toLowerCase())==true)
		        iter.remove();
		}
	}
	
	/* La méthode procédure prend le choix de l'utilisateur et une liste de participants en paramètre.
	 * Cette méthode renvoie les résultalts du questionnaire en fonction du choix entré par l'utilisateur.
	 */	
	public void procedure (Integer choix){
		
		//QUESTION 1
		if (choix==1){
			System.out.println("Fin du programme. Au revoir!");
			System.exit(0);		
		}
		//QUESTION 2
		if (choix==2){
		    scratch();
		}
		//QUESTION 3
		if (choix==3){
		    men();
		}
		//QUESTION 4
		if (choix==4){
		    women();
		}
		//QUESTION 5
		if (choix==5){
			Resultats coursecategorie = new Resultats();
			boolean condition = false;
			String sexcategorie = null;
			do
			{ 
				Scanner s = new Scanner(System.in);
				System.out.println("Entrez le sexe de la catégorie (M/F):");
				String sexe = s.nextLine();
				if (sexe.toUpperCase().equals("M") || sexe.toUpperCase().equals("F")){	
					condition=true;
					sexcategorie=sexe.toUpperCase();
				}
				else{	
					System.out.println("Erreur : Le sexe de la catégorie doit être M ou F!"); // L'utilisateur entre une donnée erronée
				}
			}
			while (condition==false);
			coursecategorie.listeparticipants = Extraire("sexe",sexcategorie,course.listeparticipants);
			
			boolean conditionage = false;
			String agecategorie = null;
			do
			{ 
				Scanner a = new Scanner(System.in);
				System.out.println("Entrez l'âge de la catégorie parmis les choix possibles: 35/40/45/50/55/60/65/sen");
				String age = a.nextLine();
				if (age.equals("35") || age.equals("40") || age.equals("45") || age.equals("50") || age.equals("55") || age.equals("60") || age.equals("65")|| age.equals("sen")){	
					conditionage=true;
					agecategorie=age;
				}
				else{	
					System.out.println("Erreur : Cet âge ne fait pas parti des catégories!");
				}
			}
			while (conditionage==false);
			
			boolean existe = false;
			for (int i=0; i<coursecategorie.listeparticipants.size(); i++){
				if (coursecategorie.listeparticipants.get(i).getage().equals(agecategorie)){ // On vérifie que la catégorie âge donnée par l'utilisateur n'est pas vide pour la catégorie sexe déjà entrée
					existe = true;
				}
			}
			if (existe == true){
				Resultats coursecategorieResultat = new Resultats();
				coursecategorieResultat.listeparticipants = Extraire("age",agecategorie,coursecategorie.listeparticipants);
				coursecategorieResultat.Tri();
				coursecategorieResultat.affiche();
			}
			else{
				System.out.println("Cette catégorie "+sexcategorie.toUpperCase()+agecategorie+" est vide!"); // Ce message s'affiche dans le cas où cette catégorie est vide ex dans le fichier test F60
			}
				
		}
		// QUESTION 6
		if (choix==6){
			// SAISIE DU PRENOM
			String prenom = null;
			Scanner p = new Scanner(System.in);
			boolean TryAgain = true;
			do{
				System.out.println("Entrez le prénom du concurrent");
				String P = p.nextLine();
	            if(P.matches("^[a-zA-Z]*$")){
	            	TryAgain=false;
	            	prenom = P;
	            }
	            else{
	            	System.out.println("Erreur : ce n'est pas une chaine de caractère.");
	            }
	        }while(TryAgain);
			// SAISIE DU NOM
			String nom = null;
			Scanner n = new Scanner(System.in);
			boolean TryAgain2 = true;
			do{
				System.out.println("Entrez le nom du concurrent");
				String N = n.nextLine();
	            if(N.matches("^[a-zA-Z]*$")){
	            	TryAgain2=false;
	            	nom = N;
	            }
	            else{
	            	System.out.println("Erreur : ce n'est pas une chaine de caractère.");
	            }
	        }while(TryAgain2);
			// SAISIE DE LA NATIONALITE
			String nationalite = null;
			Scanner na = new Scanner(System.in);
			boolean TryAgain3 = true;
			do{
				System.out.println("Entrez la nationalité du concurrent");
				String NA = na.nextLine();
	            if(NA.matches("^[a-zA-Z]*$")){
	            	TryAgain3=false;
	            	try{
	            		nationalite = NA.substring(0,3).toUpperCase();
	            	}
	            	catch (Exception ex){
	            		System.out.println("Erreur, vous devez saisir au moins trois lettres");
	    				return;
	            	}
	            }
	            else{
	            	System.out.println("Erreur : ce n'est pas une chaine de caractère.");
	            }
	        }while(TryAgain3);
			// SAISIE DU SEXE
			boolean TryAgain4 = false;
			String sexe = null;
			Scanner s = new Scanner(System.in);
			do
			{ 
				System.out.println("Entrez le sexe de la catégorie (M/F):");
				String S = s.nextLine();
				if (S.toUpperCase().equals("M") || S.toUpperCase().equals("F")){	
					TryAgain4=true;
					sexe=S.toUpperCase();
				}
				else{	
					System.out.println("Erreur : Le sexe de la catégorie doit être M ou F!");
				}
			}
			while (TryAgain4==false);
			// SAISIE DE L'AGE
			boolean TryAgain5 = false;
			String age = null;
			do
			{ 
				Scanner a = new Scanner(System.in);
				System.out.println("Entrez la catégorie âge du concurrent parmis 35/40/45/50/55/60/65/sen");
				String A = a.nextLine();
				if (A.equals("35") || A.equals("40") || A.equals("45") || A.equals("50") || A.equals("55") || A.equals("60") || A.equals("65")|| A.equals("sen")){	
					TryAgain5=true;
					age=A;
				}
				else{	
					System.out.println("Erreur : Cet âge ne fait pas parti des catégories!");
				}
			}
			while (TryAgain5==false);
			// SAISIE DU CHRONO
			Scanner t;
			String temps;
			Scanner ts = null;
			boolean test=false;
			
			do
			{
			try {
				t = new Scanner(System.in);			
				System.out.println("Entrez le temps au format hh:mm:ss (ex 02:50:01)");
				temps = t.next();
				ts = new Scanner(temps);
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				Date date = sdf.parse(temps);
				test=true;
			} 
			catch (Exception ex) {
				System.out.println("Erreur de format: vous devez entrer le temps au format hh:mm:ss!");
			}
			}
			while (test==false);
			
			ts.useDelimiter(":");
			int hours = ts.nextInt();
			int min = ts.nextInt();
			int sec = ts.nextInt();
			Duration duration = Duration.ZERO;
			duration = duration.plusHours(hours);
			duration = duration.plusMinutes(min);
			duration = duration.plusSeconds(sec);
			
			// TEST : on vérifie que le coureur n'est pas déjà dans la liste avant de l'ajouter
			boolean trouve = false;
			for (int i=0; i<course.listeparticipants.size(); i++){
				// Cas où le concurrent est déjà dans la base de donnée, ici on considère que si on trouve quelqu'un avec le même nom, le même prénom, la même nationalité et le même âge alors il s'agit du même concurrent
				if (course.listeparticipants.get(i).getprenom().equals(prenom)&&course.listeparticipants.get(i).getnom().equals(nom)&&course.listeparticipants.get(i).getnationalite().equals(nationalite)&&course.listeparticipants.get(i).getage().equals(age)){
					System.out.println("Le concurrent "+prenom+" "+nom+" existe déjà!");
					trouve = true;
					break;
				}
			}
			if (trouve == false){
				course.ajouterParticipants(prenom, nom, nationalite, sexe, age, duration);
				System.out.println("Les données du concurrent "+prenom+" "+nom+" ont été ajoutées.\r\n");
			}
			
		}
		//QUESTION 7
		if (choix==7){
			Scanner n = new Scanner(System.in);
			System.out.println("Veuillez entrer le nom d'un coureur:");
			String nom = n.nextLine();
			course.Tri();
			Integer compteur = 1;
			boolean trouve = false;
			for (int i = 0; i<course.listeparticipants.size(); i++){
				if (course.listeparticipants.get(i).getnom().toLowerCase().equals(nom.toLowerCase())){
					System.out.println("Position: "+(i+1)+" / "+course.listeparticipants.get(i).toString()+" / Temps: "+convert(course.listeparticipants.get(i).gettemps()));
					trouve = true;
				}
				compteur +=1;
			}
			if (trouve==false){
				System.out.println("Aucun participant ne porte le nom "+nom);
			}
		}
		//QUESTION 8
		if (choix==8){
			Scanner no = new Scanner(System.in);
			System.out.println("Veuillez entrer le nom du coureur que vous souhaitez disqualifier");
			String nom = no.nextLine();
			Integer compteur = 0;
			for (int i = 0; i<course.listeparticipants.size(); i++){
				if (course.listeparticipants.get(i).getnom().toLowerCase().equals(nom.toLowerCase())){
					compteur+=1;
				}
			}
			if (compteur ==0){
				System.out.println("Aucun concurrent ne porte le nom: "+nom+" !");
			}
			if (compteur ==1){
				course.delete("nom","nom",nom,nom);
				System.out.println("Le coureur : "+nom+" a été disqualifié!");
			}
			if (compteur>1){
				// Ici nous ne considérons pas le cas où plusieurs concurrents ont le même nom et le même prénom
				Scanner p = new Scanner(System.in);
				System.out.println("Plusieurs coureurs ont le nom "+nom+". Veuillez entrer le prénom du coureur.");
				String prenom = p.nextLine();
				boolean trouve = false;
				for (int i = 0; i<course.listeparticipants.size(); i++){
					if (course.listeparticipants.get(i).getnom().toLowerCase().equals(nom.toLowerCase())&&course.listeparticipants.get(i).getprenom().toLowerCase().equals(prenom.toLowerCase())){
						course.delete("nom","prenom",nom,prenom);
						System.out.println("Le coureur : "+nom+" "+prenom+" a été disqualifié!");
						trouve = true;
					}
				}
				if (trouve==false){
					System.out.println("Le coureur : "+prenom+" "+nom+" n'est pas dans la liste des participants!");
				}
			}
		}
		//QUESTION 9
		if (choix==9){
		    makeTeam();
		}
	}

    public void scratch(){
	course.Tri();
	course.affiche();		
    }
    
    public void men(){
    	Resultats coursehomme = new Resultats();
			coursehomme.listeparticipants = Extraire("sexe","M",course.listeparticipants);
			coursehomme.Tri();
			coursehomme.affiche();
    }
    
    public void women(){
	Resultats coursefemme = new Resultats();
			coursefemme.listeparticipants = Extraire("sexe","F",course.listeparticipants);
			coursefemme.Tri();
			coursefemme.affiche();
    }
    public void makeTeam(){
			System.out.println("Le résultat arrive d'ici 15 à 30 secondes..");
			course.Tri();
			
			Resultats coursehomme = new Resultats();
			coursehomme.listeparticipants = Extraire("sexe","M",course.listeparticipants);
			coursehomme.Tri();
			
			Resultats coursefemme = new Resultats();
			coursefemme.listeparticipants = Extraire("sexe","F",course.listeparticipants);
			coursefemme.Tri();
			
			//On crée une liste de tous les binomes d'hommes possibles (sans redondance).
			//On a pour cela créé une classe Binome.
			ArrayList<Binome> listehomme=new ArrayList<Binome>();
			Integer compteurh=0;
			Long tempsmaxh=null;
			Long tempsminh=null;
			for (int i = 0; i<coursehomme.listeparticipants.size()-1; i++){
				for (int j = i+1; j<coursehomme.listeparticipants.size(); j++){
					Binome p = new Binome(coursehomme.listeparticipants.get(i),coursehomme.listeparticipants.get(j),coursehomme.listeparticipants.get(i).gettemps().getSeconds()+coursehomme.listeparticipants.get(j).gettemps().getSeconds());
					listehomme.add(p);
					compteurh+=1;
					if (i==0&&j==1){
						tempsmaxh=tempsminh=p.gettemps();
					}
					else{
						if (tempsmaxh<p.gettemps()){
							tempsmaxh=p.gettemps();
						}
						if (tempsminh>p.gettemps()){
							tempsminh=p.gettemps();
						}
					}
				}
			}
			
			//On fait de même avec les binomes féminins.
			ArrayList<Binome> listefemme=new ArrayList<Binome>();
			Integer compteurf=0;
			Long tempsmaxf=null;
			Long tempsminf=null;
			for (int i = 0; i<coursefemme.listeparticipants.size()-1; i++){
				for (int j = i+1; j<coursefemme.listeparticipants.size(); j++){
					Binome p = new Binome(coursefemme.listeparticipants.get(i),coursefemme.listeparticipants.get(j),coursefemme.listeparticipants.get(i).gettemps().getSeconds()+coursefemme.listeparticipants.get(j).gettemps().getSeconds());
					listefemme.add(p);
					compteurf+=1;
					if (i==0&&j==1){
						tempsmaxf=tempsminf=p.gettemps();
					}
					else{
						if (tempsmaxf<p.gettemps()){
							tempsmaxf=p.gettemps();
						}
						if (tempsminf>p.gettemps()){
							tempsminf=p.gettemps();
						}
					}
				}
			}

			//Nous combinons les listes dans cette dernière étape.
			//On a pour cela créé une classe Quatuor.
			//On parcourt les temps de binomes, du plus grand au plus petit possibles. Pour chacun d'entre eux, on essaie de trouver une combinaison de binomes féminin et masculin.
			Long tempsmax=tempsmaxh+tempsmaxf;
			Long tempsmin=tempsminh+tempsminf;
			Long vartemps=tempsmax;
			Long tempsx=null;
			Integer compteurfinal=null;
			do{
				compteurfinal=0;
				ArrayList<Quatuor> listefinale=new ArrayList<Quatuor>();
				for (int i = 0; i<listefemme.size(); i++){
					tempsx=vartemps-listefemme.get(i).gettemps();
					//On teste ici que le binome en question n'est pas déjà présent dans la liste de quatres coureurs.
					boolean testf=true;
					for(int m=0;m<listefinale.size();m++){
						if (listefinale.get(m).getb1().getp1().getnom()==listefemme.get(i).getp1().getnom()||listefinale.get(m).getb1().getp1().getnom()==listefemme.get(i).getp2().getnom()){
							testf=false;
						}
						if (listefinale.get(m).getb1().getp2().getnom()==listefemme.get(i).getp1().getnom()||listefinale.get(m).getb1().getp2().getnom()==listefemme.get(i).getp2().getnom()){
							testf=false;
						}
						if (listefinale.get(m).getb2().getp1().getnom()==listefemme.get(i).getp1().getnom()||listefinale.get(m).getb2().getp1().getnom()==listefemme.get(i).getp2().getnom()){
							testf=false;
						}
						if (listefinale.get(m).getb2().getp2().getnom()==listefemme.get(i).getp1().getnom()||listefinale.get(m).getb2().getp2().getnom()==listefemme.get(i).getp2().getnom()){
							testf=false;
						}
					}
					if (testf==true){
						for (int j = 0; j<listehomme.size(); j++){
							//Même test pour les hommes.
							boolean testh=true;
							for(int n=0;n<listefinale.size();n++){
								if (listefinale.get(n).getb1().getp1().getnom()==listehomme.get(j).getp1().getnom()||listefinale.get(n).getb1().getp1().getnom()==listehomme.get(j).getp2().getnom()){
									testh=false;
								}
								if (listefinale.get(n).getb1().getp2().getnom()==listehomme.get(j).getp1().getnom()||listefinale.get(n).getb1().getp2().getnom()==listehomme.get(j).getp2().getnom()){
									testh=false;
								}
								if (listefinale.get(n).getb2().getp1().getnom()==listehomme.get(j).getp1().getnom()||listefinale.get(n).getb2().getp1().getnom()==listehomme.get(j).getp2().getnom()){
									testh=false;
								}
								if (listefinale.get(n).getb2().getp2().getnom()==listehomme.get(j).getp1().getnom()||listefinale.get(n).getb2().getp2().getnom()==listehomme.get(j).getp2().getnom()){
									testh=false;
								}
							}
							if (testh==true){
								if (listehomme.get(j).gettemps()==tempsx||listehomme.get(j).gettemps()==tempsx-1||listehomme.get(j).gettemps()==tempsx-2||listehomme.get(j).gettemps()==tempsx+1||listehomme.get(j).gettemps()==tempsx+2){
									compteurfinal+=1;
									listefinale.add(new Quatuor(listefemme.get(i),listehomme.get(j)));
									break;
								}
							}
						}
					}
					
					if (compteurfinal>=5){
						Long temps1=listefinale.get(4).getb1().getp1().gettemps().getSeconds()+listefinale.get(4).getb1().getp2().gettemps().getSeconds()+listefinale.get(4).getb2().getp1().gettemps().getSeconds()+listefinale.get(4).getb2().getp2().gettemps().getSeconds();
						System.out.println("Voici 5 équipes d'un niveau similaire ! Leur temps est de "+translate(temps1)+" secondes (plus ou moins 1 ou 2 secondes) :");
						for (int m = 0; m<listefinale.size(); m++){
							String str="Equipe "+(m+1)+": ";
							str=str+listefinale.get(m).getb1().getp1().getnom()+" "+listefinale.get(m).getb1().getp1().getprenom()+" : "+translate(listefinale.get(m).getb1().getp1().gettemps().getSeconds());
							str=str+" // "+listefinale.get(m).getb1().getp2().getnom()+" "+listefinale.get(m).getb1().getp2().getprenom()+" : "+translate(listefinale.get(m).getb1().getp2().gettemps().getSeconds());
							str=str+" // "+listefinale.get(m).getb2().getp1().getnom()+" "+listefinale.get(m).getb2().getp1().getprenom()+" : "+translate(listefinale.get(m).getb2().getp1().gettemps().getSeconds());
							str=str+" // "+listefinale.get(m).getb2().getp2().getnom()+" "+listefinale.get(m).getb2().getp2().getprenom()+" : "+translate(listefinale.get(m).getb2().getp2().gettemps().getSeconds());
							System.out.println(str);
						}
						break;
					}	
				}
				vartemps=vartemps-3;
				listefinale.clear();
			}
			while(vartemps>tempsmin&&compteurfinal<5);
			if(compteurfinal<5){
				System.out.println("Impossible de trouver 5 équipes équitables :(");
			}

    }
    
	
	/* Méthode questionnaire interroge l'utilisateur sur l'action qu'il veut effectuer. 
	 * Cette méthode prend en paramètre une liste de participants de type Résultats.
	 * Si l'utilisateur entre un entier entre 1 et 9 alors procédure est exécuté. 
	 * Une fois procédure exécutée, questionnaire() permet de revenir au menu en appuyant sur la touche "Entrée".
	 * Si l'utilisateur n'entre pas un entier entre 1 et 9 alors questionnaire renvoie une erreur jusqu'à ce que l'utilisateur entre un choix correct.
	 */	
	public  void menu(){
	
		boolean condition = false;
		
		do
		{ 
			Scanner sc = new Scanner(System.in);
			System.out.println("Ce programme vous permet d'exécuter les actions suivantes:\n"
					+ "1/Quitter le programme.\n"
					+ "2/Demander le classement général.\n"
					+ "3/Demander le classement pour les hommes.\n"
					+ "4/Demander le classement pour les femmes.\n"
					+ "5/Demander le classement pour une catégorie.\n"
					+ "6/Ajouter le résultat d'un concurrent.\n"
					+ "7/Demander la position d'un concurrent au classement général.\n"
					+ "8/Disqualifier un concurrent.\n"
					+ "9/Proposer 5 équipes pour la course de relais mixte.\n"
					+ "Que voulez vous faire?");
			try{
				int number = sc.nextInt();
				if (number>0 && number<10){	
					condition=true;
					procedure(number);
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					String input = null;
					System.out.println("\nAppuyer sur la touche 'Entrée' pour revenir au menu!");
					try {
					     input = br.readLine();
					} catch (IOException e) {
						System.out.println("Erreur, veuillez relancer le programme!");
						System.exit(0);
					}
					menu();
				}
				else{	
					System.out.println("Erreur : Ce n'est pas un entier entre 1 et 9");
				}
			}
			catch (InputMismatchException e){	
				System.out.println("Erreur : Ce n'est pas un entier");
			}
		}
		while (condition==false);
	}
	
	
	public static void main(String[] args){
		Resultats resultat=new Resultats();
		resultat.lecture("results.csv");
		resultat.menu();
	}
	
}
