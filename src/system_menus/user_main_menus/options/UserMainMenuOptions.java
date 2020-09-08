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

public interface UserMainMenuOptions {
    /**
     * Classes that implement this interface are the main menu options for User. All those classes should
     * have the 'execute' method which will execute the particular prompts and actions specific to whatever the
     * menu option is.
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
     * @return depending on what the User inputs it will return different objects:
     * returns null to tell mainmenu() to call execute() again
     * returns String "back" to tell mainmenu() to prompt main menu again so User can choose another
     * main menu option
     * returns String "exit" to prompt TradeSystem to save all the information and exit the System
     */

    Object execute(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                   UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                   AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager);
}
