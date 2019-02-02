/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.net.MalformedURLException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Yonah/Bilal
 */
public class Console implements Runnable {

    private ArrayList<Projet> m_allProjects;
    private Package m_structure;
    private ProcessBuilder m_terminal;
    private ArrayList<String> cheminTestUnitaire;
    private ArrayList<String> cheminScore;
    private HashMap<String, Integer> TestUnitaireNbEchec;
    private boolean isExecute = false  ; 
    private boolean isUnzip = false ; 
    private HashMap<Projet,String> logProjet ; 
/**
 *
 * Constructeur qui initialise structure avec le fichier XML ainsi que les tests unitaires et Benchmark
 */
    public Console() throws IOException {
        this.deleteLocalFolder(new File("HTML"));
        Files.createDirectories(Paths.get("HTML"));
        this.createLocalFolder(new File("Component"));

        m_allProjects = new ArrayList<Projet>();
        cheminTestUnitaire = new ArrayList<String>();
        cheminScore = new ArrayList<String>();
        m_terminal = new ProcessBuilder();
        TestUnitaireNbEchec = new HashMap<String, Integer>();
        logProjet = new HashMap<Projet,String>() ; 

        this.getTestUnitaire();
        this.getBenchmark();

        //new HTMLEtudiant(test); 
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            try {
                final Document document = builder.parse(new File("structure.xml"));
                Element a = document.getDocumentElement();
                m_structure = this.constituerArchitecture(a, new Package(""));
                //System.exit(0);

            } catch (SAXException ex) {
                System.out.println("Fichier XML Incorrect");
                System.exit(0) ; 
                
                
            } catch (IOException ex) {
                System.out.println("Fichier XML Introuvable");
                System.exit(0) ; 
                Logger.getLogger(Projet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        }

    }
/**
 *
 * Vérifie l'existence des dossiers demandées
 */
    public boolean verifyFolder() {

        for (nomDossier nomDeDossier : nomDossier.values()) {
            if (!Files.exists(Paths.get(nomDeDossier.toString()))) {
                return false;
            }
        }
        return true;

    }
  /**
 *
 * Reinitialise attribut qui correspond a l'execution du processus de test
 */  
    public void reset() throws IOException{
        this.deleteLocalFolder(new File("HTML"));
        Files.createDirectories(Paths.get("HTML"));
        this.deleteLocalFolder(new File("log"));
        Files.createDirectories(Paths.get("log"));
        this.createLocalFolder(new File("Component"));
        this.isExecute = false ; 
    
    
    }
/**
 *
 * Decompresse tous les projets
 */
    public void unzipProject() throws IOException {
        this.reset();
        this.m_allProjects.clear();
        Path monDossier = Paths.get(nomDossier.Soumis.toString());
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(monDossier)) {
            for (Path child : ds) {
                if (child.toString().contains(".zip")) {
                    Archive nouveauProjet = new Archive(child.toString());
                    nouveauProjet.unZip();
                    if (nouveauProjet.isUnzip()) {
                        Projet monProjet = new Projet(nouveauProjet.getUnzipFolder());
                        monProjet.verifierStrucure(m_structure,"");
                        m_allProjects.add(monProjet);
                    }

                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.isUnzip = true ; 
        this.isExecute = false ; 

    }
/**
 * Constitue archicture demandée a partir du fichier xml 
 */
    private Package constituerArchitecture(Node monNode, Package monPackage) {

        if (monNode.getNodeName().equals("class")) {

            if (monPackage.getNom().equals("")) {
                monPackage.ajouterClasse(new Classe(monNode.getFirstChild().getNodeValue().replaceAll("\n", "").replaceAll("\t", "")));

            } else {
                monPackage.ajouterClasse(new Classe(monPackage.getNom() + File.separator + monNode.getFirstChild().getNodeValue().replaceAll("\n", "").replaceAll("\t", "")));

            }
        }
        if (monNode.getNodeName().equals("package")) {
            Package nouveau = new Package(monNode.getFirstChild().getNodeValue().replaceAll("\n", "").replaceAll("\t", ""));
            monPackage.ajouterPackage(nouveau);
            monPackage = nouveau;
        }

        NodeList racine = monNode.getChildNodes();

        for (int i = 0, len = racine.getLength(); i < len; i++) {
            Node nodeActuel = racine.item(i);
            if (nodeActuel.getNodeType() == Element.ELEMENT_NODE) {

                this.constituerArchitecture(nodeActuel, monPackage);
            }

        }
        return monPackage;

    }
/**
 *initialise le processus de compilation
 */
    public void compilation() throws IOException, ClassNotFoundException, InterruptedException {
        int numProjet = 0;

        for (Projet unProjet : this.m_allProjects) {
            numProjet++;

            System.out.println("Traitement Projet :" + unProjet.getNom() + " Projet : " + numProjet + "/" + this.m_allProjects.size());

            m_terminal.directory(unProjet.getFolder());
            System.out.println("Génération JAVADOC :");

            this.javadoc(unProjet.getPackage(), unProjet.allPackage("", unProjet.getPackage()));
            System.out.println("Compilation classe :");

            this.compilPackage(unProjet.getPackage(), unProjet.allPackage("", unProjet.getPackage()));
            for (String e : this.cheminTestUnitaire) {
                if (!this.TestUnitaireNbEchec.containsKey(e)) {
                    this.TestUnitaireNbEchec.put(e, 0);

                }

                System.out.println("Traitement test unitaire :" + e.split(File.separator)[e.split(File.separator).length - 1].replace(".java", ""));
                unProjet.addTestUnitaire(e);
                this.TestUnitaireNbEchec.put(e, this.TestUnitaireNbEchec.get(e) + unProjet.getTest().get(unProjet.getTest().size() - 1).getTestEchec());

            }
            for (String e : this.cheminScore) {
                System.out.println("Traitement Benchmark");

                unProjet.addBenchmark(e);

            }
            unProjet.getClasseErreur(unProjet.getPackage()) ;
            String test = this.genererLog(unProjet.getPackage(), "", 1) ; 
            
            if(!test.equals("")){
                test = ("*********************************" + unProjet.getNom()).concat("  "+test+ "*********************************") ;
                logProjet.put(unProjet, test) ; 
            }

        }
        this.isExecute = true ; 
        Thread fin = new Thread(this);
        fin.start();
        fin.join();
        this.isExecute = true ; 
        System.out.println("Génération rapport HTML");
        this.buildHtmlEtudiant();
        this.buildIndexHtml();
    }
  /**
 *
 * Permet de savoir si la compilation a été executée
 */  
    public boolean getIsExecute(){
    return this.isExecute ; 
    }
    
    /**
 *
 * Permet de savoir si décompressé
 */
    public boolean getIsUnzip(){
    return this.isUnzip ; 
    }
/**
 *
 * Permet de compiler les classes 
 */
    public Package compilPackage(Package unPackage, String allPack) throws MalformedURLException, ClassNotFoundException {

        System.out.println("Package :" + unPackage.getNom().split(File.separator)[unPackage.getNom().split(File.separator).length - 1]);
        for (Classe a : unPackage.getClasse()) {

            m_terminal.command("javac", "-cp", allPack, unPackage.getNom() + File.separator + a.toString());
            try {
                Process test = m_terminal.start();
                test.waitFor();
                InputStream i = test.getErrorStream();
                int nbOctet;

                if (i.available() != 0) {

                    byte[] buffer = new byte[2048];
                    String erreur = new String();
                    while ((nbOctet = i.read(buffer, 0, 2048)) > 0) {
                        erreur = erreur.concat(new String(buffer, 0, nbOctet));

                        // System.out.print(erreur.concat(new String(buffer,0,nbOctet)));
                    }
                    if (erreur.contains("error")) {

                        System.out.println("Classe erreur :" + a.toString());
                        a.addError(erreur);
                        a.setError(true);
                        a.setValide(false);
                        a.setWarning(false);
                    } else {
                        System.out.println("Classe warning :" + a.toString());
                        a.initMethod(unPackage.getNom(), allPack);
                        a.setWarning(true);
                        a.setValide(true);
                        a.setError(false);

                    }

                } else {
                    a.initMethod(unPackage.getNom(), allPack);
                    System.out.println("Classe valide :" + a.toString());
                    a.setWarning(false);
                    a.setError(false);
                    a.setValide(true);

                }

            } catch (IOException ex) {
                Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        ArrayList<Package> racine = unPackage.getPack();
        for (int i = 0; i < racine.size(); i++) {
            Package actuel = racine.get(i);

            this.compilPackage(actuel, allPack);

        }
        return unPackage;

    }
/**
 *
 * Permet de compiler la javadoc
 */
    public Package javadoc(Package unPackage, String allPack) throws IOException, ClassNotFoundException {

        System.out.println("Package :" + unPackage.getNom().split(File.separator)[unPackage.getNom().split(File.separator).length - 1]);
        if (unPackage.getClasse().size() > 0) {
            Files.createDirectory(Paths.get(unPackage.getNom() + File.separator + "JAVADOC"));

        }
        for (Classe a : unPackage.getClasse()) {

            System.out.println("JavaDoc classe :" + a.toString());

            m_terminal.command("javadoc", "-cp", allPack, "-d", unPackage.getNom() + File.separator + "JAVADOC", unPackage.getNom() + File.separator + a.toString());
            try {
                Process test = m_terminal.start();
                test.waitFor();
                InputStream i = test.getErrorStream();
                int nbOctet;

                if (i.available() != 0) {

                    byte[] buffer = new byte[2048];
                    String erreur = new String();
                    while ((nbOctet = i.read(buffer, 0, 2048)) > 0) {
                        erreur = erreur.concat(new String(buffer, 0, nbOctet));

                        // System.out.print(erreur.concat(new String(buffer,0,nbOctet)));
                    }
                    if (erreur.contains("error: class")) {

                        

                        //System.out.println("Classe erreur :"+ a.toString() + erreur);
                       // a.initJavaDoc(unPackage.getNom() + File.separator + "JAVADOC" + File.separator + a.toString().replace(".java", ".html"));

                        
                        a.addError(erreur);
                    } else if (erreur.contains("warning")) {
                        // System.out.println("Classe warning :"+ a.toString() );
                        a.initJavaDoc(unPackage.getNom() + File.separator + "JAVADOC" + File.separator + a.toString().replace(".java", ".html"));

                    } else {
                        a.initJavaDoc(unPackage.getNom() + File.separator + "JAVADOC" + File.separator + a.toString().replace(".java", ".html"));
                    }

                } else {
                    a.initJavaDoc(unPackage.getNom() + File.separator + "JAVADOC" + File.separator + a.toString().replace(".java", ".html"));

                }

            } catch (IOException ex) {
                Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        ArrayList<Package> racine = unPackage.getPack();
        for (int i = 0; i < racine.size(); i++) {
            Package actuel = racine.get(i);

            this.javadoc(actuel, allPack);

        }
        return unPackage;

    }
/**
 * Permet de recuperer les differents tests unitaires
 */
    public void getTestUnitaire() {
        Path monDossier = Paths.get(nomDossier.testU.toString());
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(monDossier)) {
            for (Path child : ds) {
                if (child.toString().contains(".java")) {
                    cheminTestUnitaire.add(child.toAbsolutePath().toString());
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
/**
 * Permet de recuperer le Benchmark
 */
    public void getBenchmark() {
        Path monDossier = Paths.get(nomDossier.testScore.toString());
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(monDossier)) {
            for (Path child : ds) {
                if (child.toString().contains(".java")) {
                    cheminScore.add(child.toAbsolutePath().toString());
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
/**
 * Permet de construire la page HTML d'un etudiant 
 */
    public void buildHtmlEtudiant() throws FileNotFoundException, IOException {

        ArrayList<String> listeProjet = new ArrayList();

        Collections.sort(this.m_allProjects);

        for (Projet a : this.m_allProjects) {

            listeProjet.add(a.getNom() + "/" + a.getNom() + ".html");

        }

        for (int i = 0; i < this.m_allProjects.size(); i++) {

            Projet a = this.m_allProjects.get(i);
            a.getHtml().buildNavBar(listeProjet);
            if (i + 1 < this.m_allProjects.size()) {
                a.getHtml().buildNextStudent(this.m_allProjects.get(i + 1).getNom() + ".html");
            }
            a.getHtml().buildTitle(this.m_allProjects.get(i).getNom());
            String code = "<br/><center><table><tr></tr><tr><td><center>JavaDoc</center></td><td><center>Classe Valide</center></td><td><center>Classe en erreur</center></td><td><center>Classe en Warnings</center></td></tr><tr><td>";
            int methodeDoc = a.getNbMethodeDoc(a.getPackage(), 0);
            int methodeTotal = a.getNbMethodeTotal(a.getPackage(), 0);
            int classeErreur = a.getClasseErreur();
            int classeValide = a.getClasseValide();
            int classeWarnings = a.getClasseWarnings();
            int classeTotal = a.getClasseTotal();
            int pourcentage = (int) ((float) ((float) methodeDoc / (float) methodeTotal) * 100);
            int pourcentageVal = (int) ((float) ((float) classeValide / (float) classeTotal) * 100);
            int pourcentageErreur = (int) ((float) ((float) classeErreur / (float) classeTotal) * 100);
            int pourcentageWarnings = (int) ((float) ((float) classeWarnings / (float) classeTotal) * 100);

            if (pourcentage > 50) {
                code = code.concat("<div class=\"progress-circle over50  p" + Integer.toString(pourcentage) + "\">" + "<span>" + Integer.toString(pourcentage) + "%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>");

            } else {
                code = code.concat("<div class=\"progress-circle p" + Integer.toString(pourcentage) + "\">" + "<span>" + Integer.toString(pourcentage) + "%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>");

            }

            if (pourcentageVal > 50) {
                code = code.concat("<td><div class=\"progress-circle over50  p" + Integer.toString(pourcentageVal) + "\">" + "<span>" + Integer.toString(pourcentageVal) + "%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>");

            } else {
                code = code.concat("<td><div class=\"progress-circle p" + Integer.toString(pourcentageVal) + "\">" + "<span>" + Integer.toString(pourcentageVal) + "%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>");

            }

            if (pourcentageErreur > 50) {
                code = code.concat("<td><div class=\"progress-circle over50  p" + Integer.toString(pourcentageErreur) + "\">" + "<span>" + Integer.toString(pourcentageErreur) + "%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>");

            } else {
                code = code.concat("<td><div class=\"progress-circle p" + Integer.toString(pourcentageErreur) + "\">" + "<span>" + Integer.toString(pourcentageErreur) + "%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>");

            }
            if (pourcentageWarnings > 50) {
                code = code.concat("<td><div class=\"progress-circle over50  p" + Integer.toString(pourcentageWarnings) + "\">" + "<span>" + Integer.toString(pourcentageWarnings) + "%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>");

            } else {
                code = code.concat("<td><div class=\"progress-circle p" + Integer.toString(pourcentageWarnings) + "\">" + "<span>" + Integer.toString(pourcentageWarnings) + "%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>");

            }

            code = code.concat("</tr><tr></tr></table></center>");
            a.getHtml().addCodeSource(code);

            String code_html = "<center><div id=\"Cadre\" style=\"width:50%;height:50%\"><u><h1 color=\"blue\" >JAVADOC</h1></u>La javadoc a t-elle été générée? oui<br/><br /><div id=\"menu\"><ul class=\"niveau1\">";

            String test = a.getHtml().buildJavaDocHtml(a.getPackage(), 1, code_html);
            test = test.concat("</div></div></center>");
            a.getHtml().addCodeSource(test);
            String separator = "SEPARATOR";
            if (a.getTest() != null) {
                ArrayList<String> listeTestUnitaire = new ArrayList();
                for (TestUnitaire b : a.getTest()) {
                    String uneLigne = b.getNom().replace(".java", "");

                    if (b.getIsValideAndIscompil()) {

                        if (b.getTestEchec() == b.getTestTotal()) {
                            uneLigne = uneLigne.concat(separator + "Component/rouge.jpg" + separator + "Nombre Test :" + Integer.toString(b.getTestTotal()) + " Nombre test echec :" + Integer.toString(b.getTestEchec()) + "</br>");
                            HashMap<String, String> erreurTest = b.getTestEchecDet();

                            for (String e : erreurTest.keySet()) {
                                uneLigne = uneLigne.concat("Nom du test :" + e + "Erreur : " + erreurTest.get(e) + "</br>");

                            }
                            uneLigne = uneLigne.concat("Temps execution(ns) :" + b.getTemps());

                        } else if (b.getTestEchec() == 0) {
                            uneLigne = uneLigne.concat(separator + "Component/vert.jpg" + separator);
                            uneLigne = uneLigne.concat("Tous les tests on étais effectuer avec succès</br>");
                            uneLigne = uneLigne.concat("Temps execution(ns) :" + b.getTemps());

                        } else {
                            uneLigne = uneLigne.concat(separator + "Component/vert.jpg" + separator + "Nombre Test :" + Integer.toString(b.getTestTotal()) + " Nombre test echec :" + Integer.toString(b.getTestEchec()) + "</br>");
                            HashMap<String, String> erreurTest = b.getTestEchecDet();

                            for (String e : erreurTest.keySet()) {
                                uneLigne = uneLigne.concat("Nom du test :" + e + " Erreur : " + erreurTest.get(e) + "</br>");
                            }
                            uneLigne = uneLigne.concat("Temps execution(ns) :" + b.getTemps());
                        }
                    } else {
                        uneLigne = uneLigne.concat(separator + "Component/rouge.jpg" + separator + "Echec de compilation ! ");
                    }
                    listeTestUnitaire.add(uneLigne);
                }
                a.getHtml().buildTabTest(listeTestUnitaire);

            }
            /* String code ="<br/><center><table><tr></tr><tr><td><center>JavaDoc</center></td><td><center>Classe Valide</center></td><td><center>Classe en erreur</center></td><td><center>Classe en Warnings</center></td></tr><tr><td>" ; 
             int methodeDoc = a.getNbMethodeDoc(a.getPackage(), 0) ; 
             int methodeTotal = a.getNbMethodeTotal(a.getPackage(), 0) ; 
             int classeErreur = a.getClasseErreur() ;
             int classeValide = a.getClasseValide() ;
             int classeWarnings = a.getClasseWarnings() ; 
             int classeTotal = a.getClasseTotal() ; 
             int pourcentage = (int)((float)((float)methodeDoc/(float)methodeTotal) *100) ;
             int pourcentageVal = (int)((float)((float)classeValide/(float)classeTotal) *100) ;
             int pourcentageErreur = (int)((float)((float)classeErreur/(float)classeTotal) *100) ;
             int pourcentageWarnings = (int)((float)((float)classeWarnings/(float)classeTotal) *100) ;



             
             if(pourcentage > 50){
                 code = code.concat("<div class=\"progress-circle over50  p"+Integer.toString(pourcentage) +"\">" + "<span>" +Integer.toString(pourcentage) +"%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>") ; 
             
             }
             else{
                code = code.concat("<div class=\"progress-circle p"+Integer.toString(pourcentage) +"\">" + "<span>" +Integer.toString(pourcentage) +"%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>") ; 

             
             }
             
             if(pourcentageVal > 50){
                 code = code.concat("<td><div class=\"progress-circle over50  p"+Integer.toString(pourcentageVal) +"\">" + "<span>" +Integer.toString(pourcentageVal) +"%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>") ; 
             
             }
             else{
                code = code.concat("<td><div class=\"progress-circle p"+Integer.toString(pourcentageVal) +"\">" + "<span>" +Integer.toString(pourcentageVal) +"%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>") ; 

             
             }
             
             if(pourcentageErreur > 50){
                 code = code.concat("<td><div class=\"progress-circle over50  p"+Integer.toString(pourcentageErreur) +"\">" + "<span>" +Integer.toString(pourcentageErreur) +"%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>") ; 
             
             }
             else{
                code = code.concat("<td><div class=\"progress-circle p"+Integer.toString(pourcentageErreur) +"\">" + "<span>" +Integer.toString(pourcentageErreur) +"%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>") ; 

             
             }
             if(pourcentageWarnings > 50){
                 code = code.concat("<td><div class=\"progress-circle over50  p"+Integer.toString(pourcentageWarnings) +"\">" + "<span>" +Integer.toString(pourcentageWarnings) +"%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>") ; 
             
             }
             else{
                code = code.concat("<td><div class=\"progress-circle p"+Integer.toString(pourcentageWarnings) +"\">" + "<span>" +Integer.toString(pourcentageWarnings) +"%</span><div class=\"left-half-clipper\"><div class=\"first50-bar\"></div><div class=\"value-bar\"></div></div></div></td>") ; 

             
             }
             
             code = code.concat("</tr><tr></tr></table></center>") ; */

 /*code = code.concat(
"      <div class=\"first50-bar\"></div>\n" +
"      <div class=\"value-bar\"></div>\n" +
"   </div>\n" +
"</div>\n" +
"       </td><td colspan=\"2\"><div class=\"progress-circle over50 p61\">\n" +
"   <span>61%</span>\n" +
"   <div class=\"left-half-clipper\">\n" +
"      <div class=\"first50-bar\"></div>\n" +
"      <div class=\"value-bar\"></div>\n" +
"   </div>\n" +
"</div></td>\n" +
"       <td colspan=\"2\"><div class=\"progress-circle over50 p96\">\n" +
"   <span>96%</span>\n" +
"   <div class=\"left-half-clipper\">\n" +
"      <div class=\"first50-bar\"></div>\n" +
"      <div class=\"value-bar\"></div>\n" +
"   </div>\n" +
"</div></td>\n" +
"   </tr>\n" +
"   <tr>\n" +
"    \n" +
"   </tr>\n" +
"</table>\n" +
"</center>"); */
            //  a.getHtml().addCodeSource(code);
            String code_html1;
            if (a.structureIsValide()) {
                code_html1 = "<center><u><h1 color=\"blue\" > STRUCTURE DU PROJET(Valide) </h1></u><br/><br /><div id=\"menu\"><ul class=\"niveau1\">";

            } else {
                code_html1 = "<center><u><h1 color=\"blue\" > STRUCTURE DU PROJET(Non Valide) </h1></u><br/><br /><div id=\"menu\"><ul class=\"niveau1\">";

            }

            String test1 = a.getHtml().buildStructHtml(this.m_structure, 1, code_html1, a);
            test1 = test1.concat("</div></div></center></br></br></br></br>");
            a.getHtml().addCodeSource(test1);

            String code_bench = " <center><u><h1 color=\"blue\" face=\"Trebuchet MS\"> SCORE BENCHMARK </h1> </u><b><FONT size=\"20pt\">" + a.getScore() + "</FONT></b></center></div><br/><br/><br/><br/><br/><br/></div></body></html>";
            a.getHtml().addCodeSource(code_bench);

            a.getHtml().ecrireFichier();

        }

    }
/**
 * Permet de construire la page Index HTML 
 */
    public void buildIndexHtml() throws FileNotFoundException, IOException {

        String code_source = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"><html><head><meta charset=\"utf-8\"><link rel=\"stylesheet\" type=\"text/css\" href=\"Component/style.css\" media=\"all\" /><title></title></head><body><div  height=\"50\"><IMG src=\"Component/bonhomme.jpg\" height=\"130\" width=\"130\"align=\"right\"><br/><strong><FONT size=60 face=\"Trebuchet MS\" color=\"black\" ><CENTER>Resultats du projet</center></font></strong><br/><br/><br/><hr style=\"height: 3px; color: #839D2D; width: 100%; border: 1px dashed #000;\"></div><FONT color=\"#B7729B\"><u> <strong><center>Résumé des éléves</center></strong></u></FONT><center><div class=\"table\" ><div class=\"table-cell\"><h3>Nom de l'élève</h3></div><div class=\"table-cell plattform\"><h3>Score</h3></div><div class=\"table-cell enterprise\"><h3>Classement</h3></div>";
        ArrayList<String> listeProjet = new ArrayList();

        Collections.sort(this.m_allProjects,Collections.reverseOrder());

        for (Projet a : this.m_allProjects) {

            listeProjet.add(a.getNom() + "/" + a.getNom() + ".html" + "/" + a.getScore());

        }

        for (int i = 1; i < listeProjet.size() + 1; i++) {
            String newTest = "<div class=\"table-cell cell-feature\">" + listeProjet.get(i - 1).split("/")[0] + "</div><div class=\"table-cell\">" + listeProjet.get(i - 1).split("/")[2] + "</div><div class=\"table-cell\"><td><a href=\"" + listeProjet.get(i - 1).split("/")[1] + "\"> <IMG src=\"Component/paint.jpg\" height=\"30\" width=\"30\"align=\"right\"> </A>" + i + "/" + listeProjet.size() + "</td></div>";
            code_source = code_source.concat(newTest);

        }
        Map<String, Integer> sort = Console.sortByValue(TestUnitaireNbEchec);

        String fin = "</div></center><br/><FONT color=\"#B7729B\"><u><strong><center>Résumés des tests</center></strong></u></FONT><strong><center>1)Les tests les plus réussis</center></strong>";
        for (String e : sort.keySet()) {
            fin = fin.concat("<center>" + e.split(File.separator)[e.split(File.separator).length - 1].replace(".java", "") + " Nombre echec :" + sort.get(e) + "</center><br/>");

        }

        fin = fin.concat("<br/><strong><center> 2)Les tests les moins réussis</center></strong></body></html>");
        ArrayList<String> monSet = new ArrayList(sort.keySet());

        for (int i = monSet.size() - 1; i >= 0; i--) {
            fin = fin.concat("<center>" + monSet.get(i).split(File.separator)[monSet.get(i).split(File.separator).length - 1].replace(".java", "") + " Nombre echec :" + sort.get(monSet.get(i)) + "</center><br/>");

        }

        code_source = code_source.concat(fin);
        PrintWriter sortie = new PrintWriter("HTML/index.html");
        sortie.write(code_source);
        sortie.close();

    }
/**
 * Permet de trier une HashMap par rapport a ses valeurs 
 */
    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Integer>> list
                = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                    Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/
        return sortedMap;
    }
/**
 * Permet de supprimer un dossier 
 */
    public static void deleteLocalFolder(File monFile) {

        if (monFile.isDirectory()) {
            File[] mesFichiers = monFile.listFiles();
            for (int i = 0; i < mesFichiers.length; i++) {
                Console.deleteLocalFolder(mesFichiers[i]);

            }

        } else {
            monFile.delete();

        }
        monFile.delete();

    }
/**
 * Permet de créer un dossier 
 */
    public void createLocalFolder(File monFile) throws IOException {
        if (monFile.isDirectory()) {
            Files.createDirectory(Paths.get("HTML/" + monFile.getAbsolutePath().substring(monFile.getAbsolutePath().indexOf("ProjetS2") + 17)));
            File[] mesFichiers = monFile.listFiles();
            for (int i = 0; i < mesFichiers.length; i++) {
                this.createLocalFolder(mesFichiers[i]);

            }

        } else {
            copy(monFile.getAbsolutePath(), "HTML/" + monFile.getAbsolutePath().substring(monFile.getAbsolutePath().indexOf("ProjetS2") + 17));

        }

    }
/**
 * Permet de copier un fichier
 */
    public static void copy(String src, String dst) {
        File source = new File(src);
        File destination = new File(dst);
        FileInputStream sourceFile = null;
        FileOutputStream destinationFile = null;
        try {
            destination.createNewFile();
            sourceFile = new FileInputStream(source);
            destinationFile = new java.io.FileOutputStream(destination);
            // Read by 0.5MB segment.
            byte buffer[] = new byte[512 * 1024];
            int nbRead;
            while ((nbRead = sourceFile.read(buffer)) != -1) {
                destinationFile.write(buffer, 0, nbRead);
            }
            sourceFile.close();
            destinationFile.close();
        } catch (Exception e) {

        }

    }
    /**
 * Permet de supprimer un dossier en indiquant quel niveau de dossier ne pas supprimer 
 */
    public void supprimerDossierSoumission(File dossier,int numDossier){
         if (dossier.isDirectory()) {
            File[] mesFichiers = dossier.listFiles();
            for (int i = 0; i < mesFichiers.length; i++) {
                this.supprimerDossierSoumission(mesFichiers[i],numDossier + 1 );

            }

        } else {
            dossier.delete();

        }
         if(numDossier != 0){
           dossier.delete();
         }
        
    
    
    }
    /**
 * Permet de generer les log d'un projetL 
 */
    public String genererLog(Package unPackage,String log,int numPack){
        
        for(Classe a : unPackage.getClasse()){
            if(a.isError()){
                String nomPack = "" ; 
                for(int i = unPackage.getNom().split(File.separator).length - numPack; i < unPackage.getNom().split(File.separator).length; i++){
                    nomPack = nomPack.concat(unPackage.getNom().split(File.separator)[i]);
                    nomPack = nomPack.concat("/") ; 
                
                }
                log = log.concat("---------------Package :" + nomPack + "   Classe :" + a.toString() + "\n"  +"Erreur : " + a.getError() + "\n---------------"); 
            }
        }
        ArrayList<Package> racine = unPackage.getPack();
        for (int i = 0; i < racine.size(); i++) {
            Package actuel = racine.get(i);

            log = this.genererLog(actuel,log ,numPack + 1);

        }
        return log;
        
    
    
    }
    /**
 * Permet d'ecrire les différents fichier log
 */
    public void ecrireLog() throws IOException{
        ArrayList<Projet> projet = new ArrayList<>(this.logProjet.keySet()) ; 
        if(!projet.isEmpty()){
            if(Files.exists(Paths.get("log"))){
                this.supprimerDossierSoumission(new File("log"), 0);
                for(Projet a : projet){
                    
                    PrintWriter b = new PrintWriter("log/" +a.getNom()+".log") ; 
                   b.write(this.logProjet.get(a));
                   b.close();
                
                }
            
            
            }else{
                Files.createDirectories(Paths.get("log")) ;
                for(Projet a : projet){
                   PrintWriter b = new PrintWriter("log/" +a.getNom()+".log") ; 
                   b.write(this.logProjet.get(a));
                   b.close();
                
                }
            
            }
            System.out.println("Log generer dossier log");
            
            
          
        
        }else{
            System.out.println("Aucun projet ne contient des probleme de compilation");
        }
       
    
    
    }
/**
 * Permet d'attendre ques tous les differents Thread soit terminés 
 */
    @Override
    public void run() {
        
        for(Projet e : this.m_allProjects){
            
            for(TestUnitaire u : e.getTest()){
                
                while(u.getThread() != true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                }
            
            }
            
            
          
        
        }
    }
}
