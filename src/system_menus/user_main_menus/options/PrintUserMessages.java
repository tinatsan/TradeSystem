package system_menus.user_main_menus.options;

import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.ItemManager;
import meetings.MeetingManager;
import requests.TradeRequestManager;
import transactions.TransactionManager;

import java.util.logging.Logger;

public class PrintUserMessages implements UserMainMenuOptions {

    /**
     * Prints the messages that User user has. Messages include Trade Requests and Meeting requests.
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
     * @return User object so user can return to main menu
     */
    public Object execute(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                          UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                          AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {
        //no error checking needed!
        System.out.print("\nHere are your pending Trades: \n");
        User Person = allUsers.getUser(user);
        if (Person.getPendingRequests().size() == 0) {
            System.out.print("\nYou have no pending requests.\n");
            return user;
        } else {


        }
        return user;
    }

}
