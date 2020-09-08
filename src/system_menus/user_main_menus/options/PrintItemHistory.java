package system_menus.user_main_menus.options;

import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.Item;
import items.ItemManager;
import meetings.MeetingManager;
import requests.TradeRequestManager;
import transactions.TransactionManager;

import java.util.Map;
import java.util.logging.Logger;

public class PrintItemHistory implements UserMainMenuOptions {

    /**
     * Allows the User to view their item history and it's status. It will display whether it is
     * "Pending", "Approved", or "Rejected".
     *
     * @param user             The User currently logged into the system
     * @param allItems         ItemManager which stores the system's inventory
     * @param allTradeRequests TradeRequestManager which stores and edits all the TradeRequests in the system
     * @param allUsers         UserManager which stores all the Users in the system
     * @param allMeetings      MeetingManager which deals with creating and editing meetings
     * @param allTransactions  TransactionManager which stores and edits all Transactions in the system
     * @param allAdmins        AdminManager which holds all the information about Admins, system thresholds and FrozenRequests
     * @param undoLogger       Logger that logs actions in the system
     * @param currencyManager  CurrencyManager which deals with the in-system currency
     * @return null if the current menu is to be reprinted; User user if the user is to be redirected to the main menu;
     * String "exit" if the user is to be logged out.
     */
    public Object execute(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                          UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                          AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {
        if (allUsers.getUser(user).getItemHistory().size() == 0) {
            System.out.println("\uD83D\uDE25 No items here! Please add an item to your inventory!");
            return user;

        }
        for (Map.Entry m : allUsers.getUser(user).getItemHistory().entrySet()) {
            Item object = (Item) m.getKey();
            System.out.println(object.getName() + ": " + m.getValue());
        }
        return user;
    }

}
