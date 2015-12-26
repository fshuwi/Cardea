package rocks.huwi.cardea;

import com.unboundid.ldap.sdk.SearchResultEntry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marc
 */
public class PrivilegeChecker {

    public static boolean isStudent(SearchResultEntry person) {
        Logger.getLogger(RoleAdjuster.class.getName()).log(Level.INFO, "Checking whether {0} is a Student.", person.getDN());

        String mail = person.getAttribute("mail").getValue();
        Logger.getLogger(RoleAdjuster.class.getName()).log(Level.INFO, "Mail: {0}", mail);

        if (mail.endsWith("@stud.uni-bamberg.de")) {
            Logger.getLogger(RoleAdjuster.class.getName()).log(Level.INFO, "Person is a student.");
            return true;
        } else {
            Logger.getLogger(RoleAdjuster.class.getName()).log(Level.INFO, "Person is not a student.");
            return false;
        }
    }

    public static boolean isFachschaft(SearchResultEntry person) {
        Logger.getLogger(RoleAdjuster.class.getName()).log(Level.INFO, "Checking whether {0} is Fachschaft.", person.getDN());

        String mail = person.getAttribute("mail").getValue();

        if (mail.startsWith("fs-")) // TODO: bullshit for testing :)
        {
            return true;
        }

        return false; // TODO
    }

}
