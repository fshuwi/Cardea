package rocks.huwi.cardea;

import java.io.IOException;
import javax.ejb.Remote;

@Remote
public interface UploadSessionBeanRemote {

    void uploadExcelFile(byte[] excelFileContent) throws IOException;

    String getExcelInstructions();
}
