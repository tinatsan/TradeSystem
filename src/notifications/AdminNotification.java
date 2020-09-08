package notifications;

import accounts.admins.Admin;
import accounts.admins.AdminManager;

public interface AdminNotification {
    /**
     * Interface method for printing notifications for an Admin when they login
     *
     * @param admin     Admin who logged in
     * @param allAdmins The instance of AdminManager
     */
    void notify(Admin admin, AdminManager allAdmins);
}
