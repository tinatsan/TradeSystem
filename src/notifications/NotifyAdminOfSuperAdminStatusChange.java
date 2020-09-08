package notifications;

import accounts.admins.Admin;
import accounts.admins.AdminManager;

public class NotifyAdminOfSuperAdminStatusChange implements AdminNotification {
    /**
     * Notify admin of a change to their "super admin" status when they log in
     *
     * @param admin     admin who logged in
     * @param allAdmins the instance of AdminManager
     */
    public void notify(Admin admin, AdminManager allAdmins) {
        if (admin.getSuperAdminStatusChangeNotifications().size() > 0) {
            for (int i = 0; i < admin.getSuperAdminStatusChangeNotifications().size(); i++) {
                System.out.println(admin.getSuperAdminStatusChangeNotifications().get(i));
                allAdmins.removeFromSuperAdminStatusChangeNotifications(admin, admin.getSuperAdminStatusChangeNotifications().get(i));
                i = i - 1;
            }
        }
    }
}