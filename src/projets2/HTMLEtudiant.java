/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Yonah/Bilal
 */
public class HTMLEtudiant {

    private static final String m_head = " <!DOCTYPE html>\n"
            + "<html>\n"
            + " <head>\n"
            + "       <meta charset=\"utf-8\">\n"
            + "       <title>Performance projet</title>\n"
            + "       <link rel=\"stylesheet\" type=\"text/css\" href=\"Component/style.css\" media=\"all\" />\n"
            + "       <link href=\"Component/css-circular-prog-bar.css\" media=\"all\" rel=\"stylesheet\" />\n"
            + "     \n"
            + "    </head>";

    private String code_source;
    private final String nomProjet;
    private PrintWriter sortie;
    
    /**
 * Constructeur qui permet de construire une page html avec le nom du projet  
 */

    public HTMLEtudiant(String nomProjet) {
        this.nomProjet = nomProjet;
        this.code_source = m_head;
    }
/**
 * construit l'en tete de la page HTML  
 */
    public void buildNavBar(ArrayList<String> etudiant) {
        String test = "<nav id=\"primary_nav_wrap\">\n"
                + "<ul>\n"
                + "  \n"
                + "  <li class=\"current-menu-item\"><a href=\"#\">Liste des étudiants</a>\n"
                + "    <ul>";

        for (String e : etudiant) {
            test = test.concat("<li><a href=\"" + e.split("/")[1] + "\">" + e.split("/")[0] + "</a></li>");
        }
        test = test.concat("</li></ul></nav>");
        this.code_source = this.code_source.concat(test);
    }
/**
 * Permet de mettre l'etudiant suivant dans le HTML
 */
    public void buildNextStudent(String next) {
        String code = "<div align=right>\n"
                + " <a href=\"" + next + "\">  <img src=\"Component/capt.jpg\" class=\"images_petit\" alt=\"Photo fleche\" /></a>\n"
                + " </div>";
        this.code_source = this.code_source.concat(code);

    }
    
    /**
 * Permet de construire le titre de la page HTML
 */

    public void buildTitle(String projet) {
        String code = "<hr style=\"height: 3px; color: #839D2D; width: 100%; border: 1px dashed #000;\"> \n"
                + "<br />\n"
                + "<FONT face=\"Trebuchet MS\" color=\"black\" ><h1 align=\"center\"><span >Projet :" + projet + "</span>  </h1></FONT>    \n"
                + "<br />";
        this.code_source = this.code_source.concat(code);

    }
    /**
 * Permet de construire le tableau des tests unitaires
 */

    public void buildTabTest(ArrayList<String> testU) {
        String separator = "SEPARATOR";
        String code = "<center> \n"
                + "<br />\n"
                + "<u><h1>TESTS UNITAIRES</h1></u>\n"
                + "<div class=\"table\">\n"
                + "  <div class=\"table-cell\"></div>\n"
                + "  <div class=\"table-cell plattform\">\n"
                + "    <h3>Résultat</h3>\n"
                + "    \n"
                + "  </div><div class=\"table-cell enterprise\">\n"
                + "    <h3>Commentaires (temps d'éxecution...)</h3>\n"
                + "   \n"
                + "  </div>";

        for (String e : testU) {
            String newTest = "<div class=\"table-cell cell-feature\">" + e.split(separator)[0] + "</div>" + "<div class=\"table-cell\"><img src=\"" + e.split(separator)[1] + "\" height=30/></div><div class=\"table-cell\"><td>" + e.split(separator)[2] + "</td></div>";
            code = code.concat(newTest);

        }
        code = code.concat("</div></center>");
        this.code_source = this.code_source.concat(code);

    }
    
    /**
 * Permet d'ajouter du code source a la page HTML 
 */

    public void addCodeSource(String code) {
        this.code_source = this.code_source.concat(code);
    }
    
    /**
 * Permet d'ecrire le fichier HTML 
 */

    public void ecrireFichier() throws FileNotFoundException {
        sortie = new PrintWriter("HTML/" + nomProjet + ".html");
        sortie.write(this.code_source);
        sortie.close();

    }
    /**
 * Permet de constituer la liste des differentes classes a partir d'un Package en preservant l'architecture 
 */

    public String buildJavaDocHtml(Package unPackage, int ulniveau, String html) {

        html = html.concat("<li class=\"sousmenu\">" + unPackage.getNom().split(File.separator)[unPackage.getNom().split(File.separator).length - 1] + "</a>");

        html = html.concat("<ul class=\"niveau" + Integer.toString(ulniveau + 1) + "\">");
        boolean ul = false;

        for (Classe maClasse : unPackage.getClasse()) {

            if (maClasse.isJavadoc()) {
                html = html.concat("<li class=\"sousmenu\"><a>" + maClasse.toString().replace(".java", "") + "</a>" + "<ul class=\"niveau" + Integer.toString(ulniveau + 2) + "\">");
                ul = true;

            } else {
                //html = html.concat("<li class=\"sousmenu\"><a>"+ maClasse.toString().replace(".java", "") +"</a>" +"<ul class=\"niveau"+   Integer.toString(ulniveau+2) +  "\">");
                html = html.concat("<li class=\"sousmenu\"><a>" + maClasse.toString().replace(".java", "") + "\nNoCompil" + "</a>");

            }
            HashMap<Methodes, Boolean> methode = maClasse.getMethode();
            for (Methodes c : new ArrayList<Methodes>(methode.keySet())) {
                if (methode.get(c)) {
                    html = html.concat("<li><img src=\"Component/vert2.jpg\" height=10 />" + c.getNom() + "</li>");

                } else {
                    html = html.concat("<li><img src=\"Component/rouge2.jpg\" height=10 />" + c.getNom() + "</li>");

                }
            }

            if (ul) {
                html = html.concat("</ul></li>");
                ul = false;

            } else {
                html = html.concat("</li>");

            }

        }

        for (Package unPack : unPackage.getPack()) {

            html = this.buildJavaDocHtml(unPack, ulniveau + 1, html);

        }

        html = html.concat("</ul></li>");

        return html;

    }
/**
 * Permet de constituer la liste des differentes classe de la structure a partir d'un Package en preservant l'architecture 
 */
    public String buildStructHtml(Package unPackage, int ulniveau, String html, Projet monProjet) {

        if (ulniveau == 1) {

            html = html.concat("<li class=\"sousmenu\">STRUCTURE</a>");

        } else {
            html = html.concat("<li class=\"sousmenu\">" + unPackage.getNom().split(File.separator)[unPackage.getNom().split(File.separator).length - 1] + "</a>");

        }

        html = html.concat("<ul class=\"niveau" + Integer.toString(ulniveau + 1) + "\">");

        HashMap<Classe, Boolean> methode = monProjet.getStruct();
        ArrayList<Classe> mesClasse = new ArrayList<>(methode.keySet());
        for (Classe maClasse : unPackage.getClasse()) {

            if (mesClasse.contains(maClasse)) {

                if (methode.get(maClasse)) {
                    if (maClasse.toString().contains(File.separator)) {
                        html = html.concat("<li  class=\"sousmenu\"><img src=\"Component/vert2.jpg\" height=10 /><a>" + maClasse.toString().split(File.separator)[maClasse.toString().split(File.separator).length - 1].replace(".java", "") + "</a></li>");

                    } else {
                        //html = html.concat("<li  class=\"sousmenu\"><img src=\"Component/vert2.jpg\" height=10 /><a>"+ maClasse.toString().replace(".java", "") +"</a>" +"<ul class=\"niveau"+   Integer.toString(ulniveau+2) +  "\">");
                        html = html.concat("<li  class=\"sousmenu\"><img src=\"Component/vert2.jpg\" height=10 /><a>" + maClasse.toString().replace(".java", "") + "</a></li>");

                    }

                } else {
                    if (maClasse.toString().contains(File.separator)) {
                        html = html.concat("<li  class=\"sousmenu\"><img src=\"Component/rouge2.jpg\" height=10 /><a>" + maClasse.toString().split(File.separator)[maClasse.toString().split(File.separator).length - 1].replace(".java", "") + "</a></li>");

                        // html = html.concat("<li  class=\"sousmenu\"><img src=\"Component/rouge2.jpg\" height=10 /><a>"+ maClasse.toString().split(File.separator)[maClasse.toString().split(File.separator).length - 1].replace(".java", "") +"</a>" +"<ul class=\"niveau"+   Integer.toString(ulniveau+2) +  "\">");
                    } else {
                        html = html.concat("<li  class=\"sousmenu\"><img src=\"Component/rouge2.jpg\" height=10 /><a>" + maClasse.toString().replace(".java", "") + "</a></li>");

                        //  html = html.concat("<li  class=\"sousmenu\"><img src=\"Component/rouge2.jpg\" height=10 /><a>"+ maClasse.toString().replace(".java", "") +"</a>" +"<ul class=\"niveau"+   Integer.toString(ulniveau+2) +  "\">");
                    }

                }

            }

            /*if(mesClasse.contains(maClasse)){
                if(methode.get(maClasse)){
                    html = html.concat("<li><img src=\"Component/vert2.jpg\" height=10 />"+ maClasse.getNom+"</li>");
                    
                
                }else{
                    html = html.concat("<li><img src=\"Component/rouge2.jpg\" height=10 />"+ c.getNom()+"</li>");
                
                
                }
                
            
            
            
            }*/
            //  html = html.concat("</ul></li>");
        }

        for (Package unPack : unPackage.getPack()) {
            html = this.buildStructHtml(unPack, ulniveau + 1, html, monProjet);
        }

        html = html.concat("</ul></li>");

        return html;

    }

    /*for(Package a : unPackage.getPack()){
    html = html.concat("<li class=\"sousmenu\">"+ a.getNom().split(File.separator)[a.getNom().split(File.separator).length - 1]+"</a>") ; 

            
            html = html.concat("<ul class=\"niveau" + Integer.toString(ulniveau + 1) + "\">");
            for(Classe b : a.getClasse()){
                 html = html.concat("<li class=\"sousmenu\"><a>"+ b.toString().replace(".java", "") +"</a>" +"<ul class=\"niveau"+   Integer.toString(ulniveau+2) +  "\">");
            HashMap<Methodes,Boolean>methode = b.getMethode() ; 
            for(Methodes c : new ArrayList<Methodes>(methode.keySet())){
                if(methode.get(c)){
                    html = html.concat("<li><img src=\"vert2.jpg\" height=10 />"+ c.getNom()+"</li>");
                    
                
                }else{
                    html = html.concat("<li><img src=\"rouge2.jpg\" height=10 />"+ c.getNom()+"</li>");
                
                
                }}
            html = html.concat("</ul></li>");
            }
           
            
        
        }
  
        html = html.concat("</ul></li>");
        
        for(Package z : unPackage.getPack()){
            html = this.buildJavaDocHtml(z, ulniveau++, html) ; 
        
        }
        
        return html ; */
}
