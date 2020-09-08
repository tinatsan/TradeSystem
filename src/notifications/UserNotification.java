package notifications;

import accounts.users.User;
import accounts.users.UserManager;

public interface UserNotification {
    /**
     * Interface method for printing notifications for a User when they login
     *
     * @param user     User who logged in
     * @param allUsers The instance of UserManager
     */
    void notify(User user, UserManager allUsers);
}
