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

import java.util.List;
import java.util.logging.Logger;

public class PrintTop3TradingPartners implements UserMainMenuOptions {

    /**
     * Prints out the User user's top 3 trading partners.
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
        List<User> top3TP;
        top3TP = user.getTopTradingPartners();
        System.out.print("Here are your top 3 most frequent trading partners:\n");
        if (top3TP.size() == 0) {
            System.out.print("\uD83D\uDE25 Sorry! You have no trading partners! You should do some trades!\n");
        }
        for (int i = 0; i < top3TP.size(); i++) {
            System.out.print("\uD83D\uDC51" + (i + 1) + ". " + top3TP.get(i).getName() + "\n");

        }
        return user;
    }

}
