package rocks.huwi.cardea;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import org.apache.poi.POITextExtractor;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Upload mail addresses of Fachschaft members to base privilege handling on
 *
 * @author marc
 */
@Stateless
public class UploadSessionBean implements UploadSessionBeanRemote {

    /**
     * Upload and save mails in a Excel .xlsx file
     *
     * @param excelFileContent byte array of a Excel .xlsx file
     * @throws java.io.IOException
     */
    @Override
    public void uploadExcelFile(byte[] excelFileContent) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(excelFileContent);
        String text = extractTextFromExcel(inputStream);

        List<String> mails = extractMails(text);

        // TODO: persist mails into database
    }

    /**
     * Extract all mail addresses from a string
     *
     * @param text
     * @return a List of mail addresses
     */
    private List<String> extractMails(String text) {
        Matcher mailMatcher = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(text);
        List<String> mails = new ArrayList<>();
        while (mailMatcher.find()) {
            mails.add(mailMatcher.group());
        }

        return mails;
    }

    /**
     * Extracts all text from a Excel .xlsx file.
     *
     * @param inputStream a .xlsx file
     * @return Whole text in Spreadsheet
     */
    private String extractTextFromExcel(InputStream inputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        POITextExtractor extractor = new XSSFExcelExtractor(workbook);
        return extractor.getText();
    }

    /**
     * Returns instructions about the servers behaviour
     *
     * @return
     */
    @Override
    public String getExcelInstructions() {
        return "- The file must be an .xlsx file\n"
                + "- Every mail address in this file will be treated as a Fachschaft member.\n";
    }
}
