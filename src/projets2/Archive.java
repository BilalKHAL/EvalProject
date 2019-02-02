/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Yonah/Bilal
 */
public class Archive {

    private File m_monArchive;
    private File m_deZipArchive;
    private boolean m_isUnzip;

   /**
 *
 * Constructeur qui prend en argument le chemin d'une archive
 */
    public Archive(String chemin) throws FileNotFoundException {

        if (Files.exists(Paths.get(chemin))) {
            m_monArchive = new File(chemin);
            m_deZipArchive = null;
            m_isUnzip = false;
        } else {
            throw new FileNotFoundException();
        }
    }

/**
 *
 * Methode qui permet de décompresser l'archive
 */    
    public void unZip() {
        ZipInputStream monZip = null;
        try {

            monZip = new ZipInputStream(new FileInputStream(m_monArchive));
            m_isUnzip = false;

        } catch (IOException ex) {
            System.out.println("Fichier plus accesible");
            m_isUnzip = false;
            return;
        }

        //FileSystem fileSystem = FileSystems.getDefault();
        ZipEntry monEntree = null;
        try {
            monEntree = this.changeEntry(monZip);
        } catch (IOException ex) {
            Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        String extractFolder = this.getNewArchiveName();

        boolean dezip = true;

        m_deZipArchive = new File(extractFolder);
        this.createUnzipFolder(extractFolder);

        while (monEntree != null) {
            if (monEntree.isDirectory()) {
                if (!(this.createUnzipFolder(extractFolder + File.separator + monEntree.getName()))) {
                    dezip = false;
                    break;
                }

                try {
                    monEntree = this.changeEntry(monZip);
                } catch (IOException ex) {
                    Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
                    dezip = false;
                    break;
                }

            } else {

                if (!(this.createUnzipFile(monZip, extractFolder + File.separator + monEntree.getName()))) {
                    dezip = false;
                    break;
                }
                try {
                    monEntree = this.changeEntry(monZip);
                } catch (IOException ex) {
                    Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
                    dezip = false;
                }
            }
        }

        try {
            monZip.closeEntry();
            monZip.close();
        } catch (IOException ex) {
            Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (dezip == true) {
            this.m_isUnzip = true;
        }

    
    }
   /**
 *
 * Methode qui permet d'obtenir un nom d archive unique
 */ 
   
    private String getNewArchiveName() {
        String newName = "UnZip" + File.separator + m_monArchive.getName().split("\\.")[0];

        for (int i = 2; Files.exists(Paths.get(newName)); i++) {
            newName = "UnZip" + File.separator + m_monArchive.getName().split("\\.")[0] + Integer.toString(i);
        }
        return newName;
    }
   private boolean createUnzipFolder(String path) {
        try {
            Files.createDirectories(Paths.get(path));
            return true;
        } catch (IOException ex) {
            return false;

        }
    }

      /**
 *
 * Methode qui permet de changer d'entree dans notre fichier zip 
 */ 
   
 
    private ZipEntry changeEntry(ZipInputStream monZip) throws IOException {

        ZipEntry newEntry = monZip.getNextEntry();
        return newEntry;
    }
       /**
 *
 * Methode qui permet de creer un fichier
 */ 

    private boolean createUnzipFile(ZipInputStream monZip, String chemin) {
        FileOutputStream flux_sortie = null;
        try {
            byte[] buffer = new byte[2048];
            File newFile = new File(chemin);
            flux_sortie = new FileOutputStream(newFile);

            int nbOctet;
            try {
                while ((nbOctet = monZip.read(buffer)) > 0) {

                    flux_sortie.write(buffer, 0, nbOctet);

                }
                return true;
            } catch (IOException ex) {
                Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                flux_sortie.close();
            } catch (IOException ex) {
                Logger.getLogger(Archive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

/**
 *
 * Methode qui permet de connaitre l'etat de la decompression
 */    public boolean isUnzip() {
        return this.m_isUnzip;
    }

    /**
 *
 * Methode qui permet de recuperer le dossier décompressé 
 */
    public File getUnzipFolder() {
        if (this.m_isUnzip) {
            return this.m_deZipArchive;

        } else {
            return null;
        }

    }

}
