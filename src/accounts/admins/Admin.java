package accounts.admins;

import accounts.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Admin with name and password
 */
public class Admin extends Account {
    private boolean isInitialAdmin;
    private List<String> SuperAdminStatusChangeNotifications = new ArrayList<>();

    /**
     * constructs an instance of Admin with name and password
     *
     * @param name     of this Admin
     * @param password of this Admin
     */
    public Admin(String name, String password) {
        this.name = name;
        this.password = password;
    }

    /**
     * Getter for if this Admin is a super admin as boolean. Only super admins can add Admins to the system
     *
     * @return the result of if this Admin is the initial admin as boolean
     */
    public boolean getIsSuperAdmin() {
        return isInitialAdmin;
    }

    /**
     * Setter for if this Admin is a super admin as boolean. Only super admins can add Admins to the system
     *
     * @param isInitialAdmin of this Admin as boolean
     */
    public void setIsSuperAdmin(boolean isInitialAdmin) {
        this.isInitialAdmin = isInitialAdmin;
    }

    /**
     * Getter for this Admin's list of notifications regarding a change in their "super admin" status
     *
     * @return this Admin's list of "super admin" status change notifications
     */
    public List<String> getSuperAdminStatusChangeNotifications() {
        return this.SuperAdminStatusChangeNotifications;
    }
}
