/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projets2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author Yonah/Bilal
 */
public class FTPs {

    private FTPClient monClient;
/**
 * Constructeur qui initialise la connexion avec le FTP
 */
    public FTPs(String username, String pwd, String host) throws IOException {
        monClient = new FTPClient();
        monClient.connect(host);
        monClient.login(username, pwd);
        monClient.setFileType(FTP.BINARY_FILE_TYPE);
        monClient.enterLocalPassiveMode();
        monClient.setAutodetectUTF8(true);

    }
    
    /**
 * Supprimer un dossier du serveur
 */

    public void deleteFolderRecursive(String folder) throws IOException {
        FTPFile[] files = monClient.listFiles(folder);

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                this.deleteFolderRecursive(folder + "/" + files[i].getName());

            } else {
                monClient.deleteFile(folder + "/" + files[i].getName());

            }

        }
        monClient.removeDirectory(folder);
/**
 * Upload un dossier sur le FTP 
 */
    }

    public void uploadRecursive(String folderLocal, String folderDistant) throws IOException {

        File[] enfant = new File(folderLocal).listFiles();

        if (enfant != null) {
            monClient.makeDirectory(folderDistant);

            for (int i = 0; i < enfant.length; i++) {
                if (enfant[i].isDirectory()) {
                    this.uploadRecursive(enfant[i].getAbsolutePath(), folderDistant + "/" + enfant[i].getName());
                } else {
                    monClient.changeWorkingDirectory(folderDistant);
                    FileInputStream monFichier = new FileInputStream(enfant[i]);

                    monClient.storeFile(enfant[i].getName(), monFichier);

                    monFichier.close();

                }

            }

        }

    }

}
