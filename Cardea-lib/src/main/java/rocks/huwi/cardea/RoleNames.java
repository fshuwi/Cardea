package rocks.huwi.cardea;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author marc
 */
public class RoleNames {

    static final HashMap<Roles, String> roleNames;

    static {
        roleNames = new HashMap<>();
        populateRoleNames();
    }

    private static void populateRoleNames() {
        roleNames.put(Roles.Student, "huwi");
        roleNames.put(Roles.Fachschaft, "fachschaft");
    }

    public static Map<Roles, String> getRoleNames() {
        return Collections.unmodifiableMap(roleNames);
    }

    public static String get(Roles role) {
        return roleNames.get(role);
    }
}
