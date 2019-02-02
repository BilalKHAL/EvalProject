/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author yonah/bilal
 */
public class yonah {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Javadoc tr = new Javadoc("/Users/bilalkhaldi/Desktop/ProjetS2/ProjetS2/SoumissionUnZip/30-seconds-of-java8-master28/30-seconds-of-java8-master/src/main/java/snippets/JAVADOC/snippets/Snippets.html");

        // ArrayList<String> methodeJavadoc=tr.getListemeth() ;
    }

    public static ArrayList<Method> returnMethode() throws ClassNotFoundException {

        ArrayList<Method> meth = new ArrayList<Method>();

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(System.getProperty("user.dir")))) {

            for (Path child : ds) {

                if (child.toString().contains(".java")) {

                    Class a = Class.forName("projets2.Archive");
                    Method[] methode = a.getMethods();
                    int i = 0;
                    while (i < methode.length) {
                        meth.add(methode[i]);
                        System.out.println(methode[i]);
                        i = i + 1;
                    }

                }

            }

        } catch (IOException ex) {

        }
        return meth;

    }

}
