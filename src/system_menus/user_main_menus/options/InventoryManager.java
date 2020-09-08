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

import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class InventoryManager implements UserMainMenuOptions {
    /**
     * Prints out the User user's inventory; also calls UserManager to remove inventory Items upon user's request
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
        User me = allUsers.getUser(user);
        List<Item> in = me.getInventory();
        //if the user's inventory is empty
        if (in.size() == 0) {
            System.out.println("\nYour inventory is empty!\n");
            return "back";//if the user's inventory is not empty
        }
        System.out.println("Your inventory:");
        for (int i = 0; i < in.size(); i++) {
            System.out.println("\uD83D\uDCE6 " + (i + 1) + " . " + in.get(i).getName());
        }
        Scanner sc = new Scanner(System.in);
        //asks if the User wants to remove an item from their wishlist
        System.out.println("If you would like to remove an item, please enter '1' or type 'back' to go to main menu");
        Object input = sc.nextLine();
        //returns them to the main menu if they wish to go "back"


        if (input.equals("back")) {
            return "back";
        }

        if (((String) input).matches("[0-9]+") == false) {
            System.out.print("\u274CCommand Invalid. Please try again!\n");
            return null;
        }


        if (input.equals("1")) {
            System.out.print("Please enter the ID of the item you would like to change. Type 'back' to go back\n");
            Object input2 = sc.nextLine();

            if (input2.equals("back")) {
                return null;
            }

            try {
                //will try to turn the input into an integer
                //if input is not an integer, returns null and recalls wishlist()
                input2 = Integer.parseInt((String) input2);
            } catch (NumberFormatException e) {
                System.out.print("\u274CCommand Invalid. Please try again!\n");
                return null;
            }


            if (((Integer) input2 < 1) || (Integer) input2 > in.size()) {
                System.out.print("\uD83D\uDE35Input is out of bound. Please try again!\n");
                return null;
            }


            //make sure this item is not borrowed
            input2 = (Integer) input2 - 1;
            if (!in.get((Integer) input2).getOwner().getName().equals(user.getName())) {
                System.out.print("You cannot remove item you have borrowed! Not removing!\n");
                return null;
            }
            //remove the item they requested from inventory
            Item select = in.get((Integer) input2);
            allUsers.removeFromInventory(allUsers.getUser(user), select);

            allItems.RemoveFromSystem(select);

            System.out.println("\uD83D\uDDD1Item has been removed successfully!");
            return null;
        } else {
            System.out.print("\u274CCommand Invalid. Please try again!\n");
            return null;

        }
    }
}