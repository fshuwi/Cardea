package rocks.huwi.cardea;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.Modification;
import com.unboundid.ldap.sdk.ModificationType;
import com.unboundid.ldap.sdk.ModifyRequest;
import com.unboundid.ldap.sdk.SearchResultEntry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marc
 */
public class Tools {

    private static LDAPConnection ldapConnection;

    public static void Initialize(LDAPConnection ldapConnection) {
        Tools.ldapConnection = ldapConnection;
    }

    public static boolean hasOU(SearchResultEntry person, String ou) {
        return person.hasAttributeValue("ou", ou);
    }

    public static void removeOU(SearchResultEntry person, String ou, RoleAdjuster roleAdjuster) {
        try {
            ModifyRequest modifyRequest = new ModifyRequest(person.getDN(), new Modification(ModificationType.DELETE, "ou", ou));
            ldapConnection.modify(modifyRequest);
        } catch (LDAPException ex) {
            Logger.getLogger(RoleAdjuster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addOU(SearchResultEntry person, String ou, RoleAdjuster roleAdjuster) {
        try {
            ModifyRequest modifyRequest = new ModifyRequest(person.getDN(), new Modification(ModificationType.ADD, "ou", ou));
            ldapConnection.modify(modifyRequest);
        } catch (LDAPException ex) {
            Logger.getLogger(RoleAdjuster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
