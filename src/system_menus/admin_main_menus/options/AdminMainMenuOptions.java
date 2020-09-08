package system_menus.admin_main_menus.options;

import accounts.admins.Admin;
import accounts.admins.AdminManager;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.ItemManager;
import requests.TradeRequestManager;
import transactions.TransactionManager;

import java.util.logging.Logger;

public interface AdminMainMenuOptions {
    /**
     * Classes that implement this interface are the main menu options for Admin. All those classes should
     * have the 'execute' method which will execute the particular prompts and actions specific to whatever the
     * menu option is.
     *
     * @param admin           The Admin currently logged into the system
     * @param allAdmins       AdminManager which holds all the information about Admins, system thresholds and FrozenRequests
     * @param allUsers        UserManager which stores all the Users in the system
     * @param allItems        ItemManager which stores the system's inventory
     * @param allUserMessages UserMessageManager which stores all the User messages to Admin
     * @param allTransactions TransactionManager which stores and edits all Transactions in the system
     * @param allRequests     TradeRequestManager which stores and edits all the TradeRequests in the system
     * @param allCurrency     CurrencyManager which deals with the in-system currency
     * @param undoLogger      Logger that logs actions in the system
     * @return depending on what the Admin inputs it will return different objects:
     * returns null to tell mainmenu() to call execute() again
     * returns String "back" to tell mainmenu() to prompt main menu again so Admin can choose another
     * main menu option
     * returns String "exit" to prompt TradeSystem to save all the information and exit the System
     */
    Object execute(Admin admin, AdminManager allAdmins, UserManager allUsers, ItemManager allItems,
                   UserMessageManager allUserMessages, TransactionManager allTransactions,
                   TradeRequestManager allRequests, CurrencyManager allCurrency, Logger undoLogger);
}
