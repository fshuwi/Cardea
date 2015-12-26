package rocks.huwi.cardea.groupupdate;

import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchResultEntry;
import java.io.Console;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rocks.huwi.cardea.Authenticator;
import rocks.huwi.cardea.RoleAdjuster;
import rocks.huwi.cardea.UserLister;

/**
 *
 * @author marc
 */
public class CLI {

    Settings settings;
    Authenticator authenticator;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.run();
    }

    public void run() {
        this.printBanner();
        this.settings = new Settings();
        this.authenticate();
        this.printPeople();
        this.adjustRoles();
        this.authenticator.close();
    }

    private void printBanner() {
        System.out.println("Huwi LDAP Client");
        System.out.println("================");
        System.out.println("");
    }

    private Console getConsole() {
        Console console = System.console();

        if (console == null) {
            System.err.println("Couldn't get Console instance");
            System.exit(0);
        }

        return console;
    }

    private void authenticate() {
        String host = this.settings.getHost();
        if (this.settings.getHost() == null) {
            host = this.getConsole().readLine("Host: ");
        }

        String adminDN = this.settings.getAdminDN();
        if (this.settings.getAdminDN() == null) {
            adminDN = this.getConsole().readLine("LDAP Admin DN: ");
        }

        String password = this.settings.getPassword();
        if (this.settings.getPassword() == null) {
            password = new String(this.getConsole().readPassword("Password: "));
        }

        this.authenticator = new Authenticator();
        try {
            this.authenticator.authenticate(host, 389, adminDN, password);
        } catch (LDAPException ex) {
            Logger.getLogger(CLI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void adjustRoles() {
        Logger.getLogger(CLI.class.getName()).log(Level.INFO, "Adjust roles.");

        RoleAdjuster roleAdjuster = new RoleAdjuster(authenticator.getConnection());
        try {
            roleAdjuster.adjustRoles();
        } catch (LDAPException ex) {
            Logger.getLogger(CLI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printPeople() {
        Logger.getLogger(CLI.class.getName()).log(Level.INFO, "Print People.");

        try {
            UserLister userLister = new UserLister(authenticator.getConnection());
            List<SearchResultEntry> people = userLister.getPeople();

            for (SearchResultEntry entry : people) {
                System.out.println(entry.getDN());
            }
        } catch (LDAPSearchException ex) {
            Logger.getLogger(CLI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LDAPException ex) {
            Logger.getLogger(CLI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
