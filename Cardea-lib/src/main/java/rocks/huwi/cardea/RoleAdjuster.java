package rocks.huwi.cardea;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchResultEntry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marc
 */
public class RoleAdjuster {

    private final LDAPConnection ldapConnection;

    public RoleAdjuster(LDAPConnection ldapConnection) {
        this.ldapConnection = ldapConnection;
    }

    public void adjustRoles() throws LDAPException {
        UserLister userLister = new UserLister(this.ldapConnection);
        List<SearchResultEntry> people = userLister.getPeople();

        for (SearchResultEntry person : people) {
            adjustRoles(person);
        }
    }

    private void adjustRoles(SearchResultEntry person) {
        this.adjustStudentRole(person);
        this.adjustFachschaftRole(person);
    }

    private void adjustStudentRole(SearchResultEntry person) {
        if (PrivilegeChecker.isStudent(person)) {
            setRole(person, Roles.Student);
        } else {
            unsetRole(person, Roles.Student);
        }
    }

    private void adjustFachschaftRole(SearchResultEntry person) {
        if (PrivilegeChecker.isFachschaft(person)) {
            setRole(person, Roles.Fachschaft);
        } else {
            unsetRole(person, Roles.Fachschaft);
        }
    }

    private void unsetRole(SearchResultEntry person, Roles role) {
        String roleName = RoleNames.get(role);

        if (Tools.hasOU(person, roleName) == true) {
            Logger.getLogger(RoleAdjuster.class.getName())
                    .log(Level.INFO, "Unset Role ''{0}'' for {1}", new Object[]{role.name(), person.getDN()});
            Tools.removeOU(person, roleName, this);
        }
    }

    private void setRole(SearchResultEntry person, Roles role) {
        String roleName = RoleNames.get(role);

        if (Tools.hasOU(person, roleName) == false) {
            Logger.getLogger(RoleAdjuster.class.getName())
                    .log(Level.INFO, "Set Role '{0}' for {1}", new Object[]{role.name(), person.getDN()});
            Tools.addOU(person, roleName, this);
        }
    }
}
