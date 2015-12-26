package rocks.huwi.cardea;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * CLI to choose Excel file and upload it to remote EJB
 *
 * @author marc
 */
public class UploadExcel {

    Properties properties;
    InitialContext ctx;

    {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
            //props.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
            //props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
            //props.put("java.naming.provider.url", "localhost");
        } catch (IOException ex) {
            Logger.getLogger(UploadExcel.class.getName()).log(Level.SEVERE, "Could not load the config.properties file.", ex);
        }

//        Hashtable<String, String> hashTable = new Hashtable<>();
//        hashTable.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.impl.SerialInitContextFactory");
//        hashTable.put(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
//        hashTable.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
        try {
            ctx = new InitialContext(properties);
            //ctx = new InitialContext(props);
            //ctx = new InitialContext();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UploadExcel upload = new UploadExcel();
        upload.run();
    }

    private void printBanner() {
        System.out.println("Huwi Privilege Excel-Uploader");
        System.out.println("=============================");
    }

    /**
     * Get instructions of the current server implementation and print them.
     *
     * @param uploadBean
     */
    private void printServerInstructions(UploadSessionBeanRemote uploadBean) {
        System.out.println("Instructions sent by the server:\n"
                + uploadBean.getExcelInstructions() + "\n");
    }

    private void run() {

        try {
            UploadSessionBeanRemote uploadBean
                    = (UploadSessionBeanRemote) ctx.lookup(JNDI_IDENTIFIER_UPLOADBEAN);

            printServerInstructions(uploadBean);
            File excelFile = chooseFile();
            byte[] fileContent = readFile(excelFile);

            System.out.print("Uploading file... ");
            uploadBean.uploadExcelFile(fileContent);
            System.out.println("done");

            System.out.println("\nPress Enter to end.");
            System.in.read();
        } catch (NamingException | IOException ex) {
            Logger.getLogger(UploadExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static final String JNDI_IDENTIFIER_UPLOADBEAN = "java:global/Cardea-ear/Cardea-ejb-1.0-SNAPSHOT/UploadSessionBean";

    private File chooseFile() {
        System.out.println("I just opened a File Dialog (it might be somewhere in the background).\nPlease choose a file to upload.");

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Excel *.xlsx", "xlsx"));
        chooser.setApproveButtonText("Upload");

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }

    private byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }
}
