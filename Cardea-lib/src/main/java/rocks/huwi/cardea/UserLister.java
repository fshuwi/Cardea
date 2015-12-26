package rocks.huwi.cardea;

import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionOptions;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marc
 */
public class UserLister {

    private LDAPConnection ldapConnection;

    public UserLister(LDAPConnection ldapConnection) {
        this.ldapConnection = ldapConnection;
    }

    public List<SearchResultEntry> getPeople() throws LDAPSearchException, LDAPException {
        String dn = "ou=People,dc=huwi,dc=rocks";
        SearchResult searchResult = this.ldapConnection.search(dn, SearchScope.SUBORDINATE_SUBTREE, "(ObjectClass=*)");

        List<SearchResultEntry> entries = searchResult.getSearchEntries();

        return entries;
    }
}
