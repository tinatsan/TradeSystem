package system_menus.user_main_menus.options;

import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.ItemManager;
import meetings.MeetingManager;
import requests.TradeRequest;
import requests.TradeRequestManager;
import transactions.TransactionManager;

import java.util.List;
import java.util.logging.Logger;

public class PrintOutboundRequests implements UserMainMenuOptions {

    /**
     * Prints a User's outbound TradeRequests
     *
     * @param user            User whose outbound TradeRequests are to be printed
     * @param currencyManager CurrencyManager which deals with the in-system currency
     * @return null if the current menu is to be reprinted; User user if the user is to be redirected to the main menu;
     * String "exit" if the user is to be logged out.
     */
    public Object execute(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                          UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                          AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {
        System.out.print("Here is the status of your pending outbound requests:\n");
        List<TradeRequest> outbound = user.getOutboundRequests();

        if (outbound.size() == 0) {
            System.out.print("You do not have any pending outbound requests!\n");
            return user;
        }
        for (int i = 0; i < outbound.size(); i++) {
            System.out.println((i + 1) + ". " + outbound.get(i).toString());
            int status = outbound.get(i).getStatus();
            //0: In progress, 1: Declined, 2: Accepted
            if (status == 0) {
                System.out.println("Status: In progress\n");
            } else if (status == 1) {
                System.out.println("Status: Declined\n");
            } else if (status == 2) {
                System.out.println("Status: Accepted\n");
            }
        }
        return user;
    }

}
