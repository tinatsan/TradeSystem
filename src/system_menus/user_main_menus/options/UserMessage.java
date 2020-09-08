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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Display in User's main menu. Allow user to send and view messages to and from Admin.
 */

public class UserMessage implements UserMainMenuOptions, Serializable {
    /**
     * Initiates a one-way or two-way trade between two Users. Prompts user for details of the trade.
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
     * @return depend on user's input
     * return back that bring user back to the main menu, so that user can choose other options
     * return null if user has no messages, or user has viewed or sent massage successfully, or the input is not valid.
     */
    public Object execute(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                          UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                          AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter '1' to view message replies or '2' to send Admin a message. Enter 'back' to return to the main menu.");
        String input = sc.nextLine();

        if (input.equals("back")) {
            return "back";
        } else if (input.equals("1")) {
            if (user.getAdminMessages().size() == 0) {
                System.out.println("You have no messages!");
                return null;
            }
            System.out.println("Here are your message replies: ");
            for (int i = 0; i < user.getAdminMessages().size(); i++) {
                System.out.println((i + 1) + ". " + user.getAdminMessages().get(i));
            }
            return null;
        } else if (input.equals("2")) {
            Object temp = messageAdmin(user, allUserMessages);
            while (temp == null) {
                temp = messageAdmin(user, allUserMessages);
            }
            return null;
        } else {
            System.out.println("That is not a valid option! Please try again.");
            return null;
        }
    }

    public Object messageAdmin(User user, UserMessageManager allUserMessages) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please leave a message to Admin:");
        String user_message = sc.nextLine();

        System.out.println("Your message is: \n" + user_message);

        System.out.println("Please enter '1' to confirm or '2' to cancel this message.");
        String confirmation = sc.nextLine();
        if (confirmation.equals("1")) {
            ArrayList<String> userMessage = new ArrayList<>();
            userMessage.add(user.getName());
            userMessage.add(user_message);
            allUserMessages.addUserMessage(userMessage);
            System.out.println("Your message has been sent successfully!");
            return "back";
        } else if (confirmation.equals("2")) {
            System.out.println("Your message has been cancelled.");
            return "back";
        } else {
            System.out.println("That is not a valid option! Please try again.");
            return null;
        }
    }
}

