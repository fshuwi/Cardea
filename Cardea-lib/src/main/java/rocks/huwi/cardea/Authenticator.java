package rocks.huwi.cardea;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionOptions;
import com.unboundid.ldap.sdk.LDAPException;

/**
 *
 * @author marc
 */
public class Authenticator {

    private LDAPConnection ldapConnection;

    public void authenticate(String host, int port, String adminDN, String password)
            throws LDAPException {
        LDAPConnectionOptions connectionOptions = new LDAPConnectionOptions();
        this.ldapConnection = new LDAPConnection(connectionOptions,
                host, port, adminDN, password);

        Tools.Initialize(ldapConnection);
    }

    public LDAPConnection getConnection() {
        return ldapConnection;
    }

    public void close() {
        this.ldapConnection.close();
    }

}
