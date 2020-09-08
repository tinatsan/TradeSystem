package system_menus.user_main_menus;

import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.ItemManager;
import meetings.MeetingManager;
import notifications.NotifyUserOfAdminUndo;
import notifications.NotifyUserOfVIPStatusChange;
import requests.TradeRequestManager;
import system_menus.ChosenOption;
import system_menus.user_main_menus.options.*;
import transactions.TransactionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class FrozenUserMainMenu implements DifferentUserMainMenu {
    /**
     * Displays the main menu for a frozen or pseudoFrozen user and prompts user for input depending on what they want to do.
     * <p>
     * Frozen users are able to do the following:
     * View wishlist, view inventory, browse items, add items to inventory, view most recent trades, view most
     * frequent trading partners, view item statuses, add items to wishlist, request unfreeze and log out.
     *
     * @param user             the user that is currently logged in to the system
     * @param allItems         ItemManager that stores the system's inventory
     * @param allTradeRequests TradeRequestManager that stores all the Trade Requests in the system
     * @param allUsers         UserManager that stores all the Users in the system
     * @param allMeetings      MeetingManager that deals with the creation of meetings
     * @param allTransactions  TransactionManager that stores all the Transactions in the system
     * @param allUserMessages  UserMessageManager which stores all the Users messages to Admin
     * @return depending on what the User inputs it will return different objects:
     * returns User to TradeSystem() to either remain logged into the system and prompt mainMenu
     * returns null to log out of the system and allow another User to log in
     * returns String "exit" to tell TradeSystem() to end the program and save all the data before
     * exiting the System
     */
    public Object mainMenu(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                           UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                           AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {

        System.out.print("----------------------------------------------------------------------------------------------" +
                "\n\uD83D\uDC4B Welcome back, " + user.getName() + "!\n");
        String actionTaker = "by Admin.";
        if (user.getIsPseudoFrozen())
            actionTaker = "by System.";

        System.out.print("\uD83E\uDD76 Your account is frozen " + actionTaker +
                " You are not able to do any trades until you are unfrozen by admin.\n" +
                "\uD83C\uDFC1 Please ask Admin to unfreeze your account!\n\n");

        if (allUsers.getUser(user).getAdminMessages().size() > 0) {
            System.out.println("You have " + allUsers.getUser(user).getAdminMessages().size() + " message(s) from Admin!");
        }

        // if admin has undone any actions on user's account, a String will be printed when user logs in
        NotifyUserOfAdminUndo notifyActions = new NotifyUserOfAdminUndo();
        notifyActions.notify(user, allUsers);

        // if admin has changed user's VIP status, a String will be printed when user logs in
        NotifyUserOfVIPStatusChange notification = new NotifyUserOfVIPStatusChange();
        notification.notify(user, allUsers);

        System.out.print("Please select number from the following:\n" +
                "1. View and edit Wishlist\n" +
                "2. View Inventory\n" +
                "3. Browse Items\n" +
                "4. Add Item to inventory\n" +
                "5. View most recent trades\n" +
                "6. View most frequent trading partners\n" +
                "7. View status of my items\n" +
                "8. Request unfreeze!\n" +
                "9. Message Admin\n" +
                "10. Change Account Settings\n11. Logout" +
                "\nEnter 'exit' to exit the system at any time.\n");

        ChosenOption option = new ChosenOption();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            String input = br.readLine();
            if (!input.equals("exit")) {
                switch (input) {
                    case "1":  //view and edit wishlist
                        option.setChosenOption(new WishlistManager());
                        break;
                    case "2":  //view inventory
                        option.setChosenOption(new InventoryManager());
                        break;
                    case "3":  //browse
                        option.setChosenOption(new Browse());
                        break;
                    case "4":  //add item to inventory
                        option.setChosenOption(new AddItemToSystem());
                        break;
                    case "5":  //view recent trades
                        option.setChosenOption(new PrintMostRecentTrades());
                        break;
                    case "6":  //view most freq trading partners
                        option.setChosenOption(new PrintTop3TradingPartners());
                        break;
                    case "7":   //view item status
                        option.setChosenOption(new PrintItemHistory());
                        break;
                    case "8":  //request to be unfrozen
                        option.setChosenOption(new NotifyAdminOfUnfreezeRequest());
                        break;
                    case "9":  //message admin
                        option.setChosenOption(new UserMessage());
                        break;
                    case "10":  //change account settings
                        option.setChosenOption(new AccountSettingsManager());
                        break;
                    case "11":  //logout
                        return null;
                    default:  //returns to main menu
                        System.out.println("That is not a valid option. Please try again.");
                        return user;
                }
                Object result = option.executeOption(user, allItems, allTradeRequests, allUsers, allMeetings,
                        allTransactions, allAdmins, undoLogger, allUserMessages, currencyManager);
                while (result == null) {
                    result = option.executeOption(user, allItems, allTradeRequests, allUsers, allMeetings,
                            allTransactions, allAdmins, undoLogger, allUserMessages, currencyManager);
                }
                return user;
            }
            return input;
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }
        return user;
    }

}
