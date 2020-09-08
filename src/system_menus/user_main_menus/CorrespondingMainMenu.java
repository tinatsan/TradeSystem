package system_menus.user_main_menus;

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

public class CorrespondingMainMenu {
    public DifferentUserMainMenu correctMenu;

    /**
     * Stores the menu to be run depending on the type of User.
     *
     * @param menu the menu that needs to be run.
     */
    public void setCorrectMenu(DifferentUserMainMenu menu) {
        this.correctMenu = menu;
    }

    /**
     * Calls and runs the correct main menu depending if the user is a regular user, frozen user, pseudo frozen user,
     * vacationing user or demo user.
     *
     * @param user             The User currently logged into the system
     * @param allAdmins        AdminManager which holds all the information about Admins, system thresholds and FrozenRequests
     * @param allItems         ItemManager which stores the system's inventory
     * @param allMeetings      MeetingManager which deals with creating and editing meetings
     * @param allTradeRequests TradeRequestManager which stores and edits all the TradeRequests in the system
     * @param allTransactions  TransactionManager which stores and edits all Transactions in the system
     * @param allUsers         UserManager which stores all the Users in the system
     * @param undoLogger       Logger that logs actions in the system
     * @param allUserMessages  stores all the User messages to Admin
     * @return depending on what the User inputs it will return different objects:
     * returns User to TradeSystem() to either remain logged into the system and prompt mainMenu
     * returns null to log out of the system and allow another User to log in
     * returns String "exit" to tell TradeSystem() to end the program and save all the data before
     * exiting the System
     */
    public Object runMenu(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                          UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                          AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {
        return correctMenu.mainMenu(user, allItems, allTradeRequests, allUsers, allMeetings, allTransactions, allAdmins, undoLogger, allUserMessages, currencyManager);
    }
}
