/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author Yonah/Bilal
 */
public class Javadoc {

    private ArrayList<Methodes> m_listeMethodeDocument;
    private boolean isGenerated;
/**
 * Constitue la javadoc a partir du chemin du fichier HTML 
 */
    public Javadoc(String chemin) throws IOException, ClassNotFoundException {
        m_listeMethodeDocument = new ArrayList<Methodes>();

        String html = Fichier.lireFichier(chemin);

        // System.out.print(html);
        int index = html.indexOf("<a name=\"method.summary\">");
        html = html.substring(index);       // 
        int index2 = html.indexOf("</table>");
        html = html.substring(0, index2);
        ArrayList<String> meth = new ArrayList();
        meth = this.getMethodDocumented(meth, html);
        meth.remove(0); //Supression description cellule du tableau
        for (int i = 0; i < meth.size(); i++) {
            m_listeMethodeDocument.add(this.getNameMethod(meth.get(i)));

        }

    }
    /**
 * retourne la liste des methodes
 */
    

    public ArrayList<Methodes> getListemeth() {
        return m_listeMethodeDocument;
    }
/**
 * renvoie les methodes du fichier HTML sous forme de chaine caractere
 */
    private ArrayList<String> getMethodDocumented(ArrayList<String> mesMethode, String html) {
        int index1 = html.indexOf("<tr");
        int index2 = html.indexOf("</tr>");
        mesMethode.add(html.substring(index1, index2));
        html = html.substring(index2 + 6);//Offset pour retirer balise fermante avec l'espace </tr>
        if (html.length() > 0) {
            this.getMethodDocumented(mesMethode, html);

        }

        return mesMethode;

    }
/**
 * Renvoie une Methode a partir de la cellule du fichier HTML
 */
    private Methodes getNameMethod(String cellule) {

        int indexLigne = cellule.indexOf("<td class=\"colLast\"");
        cellule = cellule.substring(indexLigne);
        int indexMethode = cellule.indexOf("<a href");
        cellule = cellule.substring(indexMethode);
        int beginMethode = cellule.indexOf(">") + 1;
        int finMethode = cellule.indexOf("</a>");

        String nomMethode = cellule.substring(beginMethode, finMethode);
        int indexArgument = cellule.indexOf("</span>") + 7;
        int finArgument = cellule.indexOf("</code>");
        String argument = cellule.substring(indexArgument, finArgument);
        argument = argument.replace("\n", "").replace(" ", "").replace("&nbsp", "");
        String fin = "";
        String commentaire = "";
        while (argument.contains("<ahref=")) {
            int debutArg = argument.indexOf(">") + 1;
            int finArg = argument.indexOf(")") + 1;
            int debutPack = argument.indexOf("title=\"") + 7;
            int finPack = argument.indexOf("\">");
            argument = argument.substring(debutPack, finPack) + "." + argument.substring(debutArg, finArg);

        }
        argument = argument.replaceAll("</a>", "").replaceAll("classin", "").replace("interfacein", "");

        if (argument.contains("&lt;")) { //Type parametre
            argument = argument.replace("&lt;", "<").replace("&gt;", ">");
        }
        if (argument.contains(",")) {
            String[] tab = argument.split(",");
            for (int i = 0; i < tab.length; i++) {

                if (i != 0) {
                    fin = fin.concat(",");

                }

                fin = fin.concat(tab[i].split(";")[0]);

            }

        } else if (argument.contains(";")) {
            fin = fin.concat(argument.split(";")[0]);

        } else {

            fin = argument;
        }
        fin = fin.replaceAll("\\(", "").replaceAll("\\)", "");

        if (cellule.contains("<div class=\"block\">")) {

            int debutCommentaire = cellule.indexOf("<div class=\"block\">") + 19;
            int finCommentaire = cellule.indexOf("</div>");
            commentaire = cellule.substring(debutCommentaire, finCommentaire);
            commentaire = commentaire.replaceAll("<code>", "").replaceAll("</code>", "").replaceAll("\n", "").replaceAll("\t", "");
            while (commentaire.contains("<a href=")) {
                int debutArg = commentaire.indexOf("<a href=");
                int finArg = commentaire.substring(debutArg).indexOf(">");
                commentaire = commentaire.replace(commentaire.substring(debutArg, finArg + debutArg), "");

            }

            commentaire = commentaire.replaceAll("</a>", "").replaceAll("<tt>", "").replaceAll("</tt>", "");

        }

        Methodes maMethode = new Methodes(nomMethode);
        if (fin.contains(",")) {
            for (int i = 0; i < fin.split(",").length; i++) {
                maMethode.addArgument(fin.split(",")[i]);
            }

        } else {
            maMethode.addArgument(fin);

        }
        maMethode.setCommentaire(commentaire);

        return maMethode;

    }
/**
 * Permet de vérifier si la javadoc a été génerée
 */
    public void isGenerated(boolean value) {
        this.isGenerated = value;

    }

}
