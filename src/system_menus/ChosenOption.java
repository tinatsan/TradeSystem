package system_menus;

import accounts.admins.Admin;
import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.ItemManager;
import meetings.MeetingManager;
import requests.TradeRequestManager;
import system_menus.admin_main_menus.options.AdminMainMenuOptions;
import system_menus.user_main_menus.options.UserMainMenuOptions;
import transactions.TransactionManager;

import java.util.logging.Logger;

public class ChosenOption {
    public Object chosenOption;

    /**
     * Stores the main menu option chosen by the Admin or User.
     *
     * @param option the option chosen by the Admin or User
     */
    public void setChosenOption(Object option) {
        this.chosenOption = option;
    }

    /**
     * Classes that implement this interface are the main menu options for User. All those classes should
     * have the 'execute' method which will execute the particular prompts and actions specific to whatever the
     * menu option is.
     *
     * @param user             The User currently logged into the system
     * @param allAdmins        AdminManager which holds all the information about Admins, system thresholds and FrozenRequests
     * @param allItems         ItemManager which stores the system's inventory
     * @param allMeetings      MeetingManager which deals with creating and editing meetings
     * @param allTradeRequests TradeRequestManager which stores and edits all the TradeRequests in the system
     * @param allTransactions  TransactionManager which stores and edits all Transactions in the system
     * @param allUsers         UserManager which stores all the Users in the system
     * @param undoLogger       Logger that logs actions in the system
     * @param allUserMessages  UserMessageManager which stores all the User messages in the system
     * @param allCurrency      CurrencyManager which deals with the in-system currency
     * @return depending on what the User inputs it will return different objects:
     * returns null to tell mainmenu() to call execute() again
     * returns String "back" to tell mainmenu() to prompt main menu again so User can choose another
     * main menu option
     * returns String "exit" to prompt TradeSystem to save all the information and exit the System
     */
    public Object executeOption(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                                UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                                AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages,
                                CurrencyManager allCurrency) {

        return ((UserMainMenuOptions) chosenOption).execute(user, allItems, allTradeRequests, allUsers,
                allMeetings, allTransactions, allAdmins, undoLogger, allUserMessages, allCurrency);
    }

    /**
     * Calls the method stored in chosenOption and returns an Object.
     *
     * @param admin           the current Admin logged into the system
     * @param allAdmins       AdminManager which holds all the information about Admins, system thresholds and FrozenRequests
     * @param allItems        ItemManager which stores the system's inventory
     * @param allTransactions TransactionManager which stores and edits all Transactions in the system
     * @param allUsers        UserManager which stores all the Users in the system
     * @param undoLogger      Logger that logs actions in the system
     * @param allUserMessages UserMessageManager which stores all the User messages in the system
     * @param allCurrency     CurrencyManager which deals with the in-system currency
     * @return depending on what the Admin inputs it will return different objects:
     * returns null to tell mainmenu() to call system_options.admin_main_menus.options.AddAdmin() again
     * returns String "back" to tell mainmenu() to prompt main menu again so User can choose another
     * main menu option
     * returns String "exit" to prompt TradeSystem to save all the information and exit the System
     */
    public Object executeOption(Admin admin, AdminManager allAdmins, UserManager allUsers, ItemManager allItems,
                                UserMessageManager allUserMessages, TransactionManager allTransactions,
                                TradeRequestManager allRequests, CurrencyManager allCurrency, Logger undoLogger) {
        return ((AdminMainMenuOptions) chosenOption).execute(admin, allAdmins, allUsers, allItems, allUserMessages, allTransactions,
                allRequests, allCurrency, undoLogger);
    }
}
