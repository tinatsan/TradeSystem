package system_menus.user_main_menus;

import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.ItemManager;
import meetings.MeetingManager;
import requests.TradeRequestManager;
import system_menus.ChosenOption;
import system_menus.user_main_menus.options.Browse;
import system_menus.user_main_menus.options.WishlistManager;
import transactions.TransactionManager;

import java.util.Scanner;
import java.util.logging.Logger;

public class DemoUserMainMenu implements DifferentUserMainMenu {

    /**
     * Displays the main menu for a demo user and prompts user for input depending on what they want to do.
     * <p>
     * Demo users are able only able to browse items and log out.
     * However, they can choose from the following and it will show them what those menu options do if they create
     * an account:
     * View wishlist, view inventory, initiate trade, view messages, approve pending trades, add
     * items to inventory, view recent trades, view most frequent trading partners, view item statuses, add items to
     * wishlists, view approved trades, approve meetings, confirm meetings, and change account settings.
     *
     * @param user             the user that is currently logged in to the system
     * @param allItems         ItemManager that stores the system's inventory
     * @param allTradeRequests TradeRequestManager that stores all the Trade Requests in the system
     * @param allUsers         UserManager that stores all the Users in the system
     * @param allMeetings      MeetingManager that deals with the creation of meetings
     * @param allTransactions  TransactionManager that stores all the Transactions in the system
     * @param allUserMessages  UserMessageManager which stores all the User messages to Admin
     * @return depending on what the User inputs it will return different objects:
     * returns User to TradeSystem() to either remain logged into the system and prompt mainMenu
     * returns null to log out of the system and allow another User to log in
     * returns String "exit" to tell TradeSystem() to end the program and save all the data before
     * exiting the System
     */
    public Object mainMenu(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                           UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                           AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {
        System.out.print("-------------------------------------------------------\n\uD83E\uDD16 Hello Demo User \uD83E\uDD16\n");

        System.out.println("Please select number from the following: ");
        System.out.println("\uD83C\uDD94 Indicates that signup/login as user is required!");
        System.out.println("\n1. View and edit Wishlist\n" +
                "2. View Inventory\uD83C\uDD94\n" +
                "3. Browse Items\n" +
                "4. Initiate Trade\uD83C\uDD94\n" +
                "5. View Pending Trade Requests\uD83C\uDD94\n" +
                "6. Approve Pending Trade Requests\uD83C\uDD94\n" +
                "7. Add Item to inventory\uD83C\uDD94\n" +
                "8. View most recent trades\uD83C\uDD94\n" +
                "9. View most frequent trading partners\uD83C\uDD94\n" +
                "10. View status of my items\uD83C\uDD94\n" +
                "11. Approve Meeting\uD83C\uDD94\n" +
                "12. Confirm Meetings for Approved Trades\uD83C\uDD94\n" +
                "13. View status of outbound requests\uD83C\uDD94\n" +
                "14. Message Admin and view replies\uD83C\uDD94\n" +
                "15. Change Account Settings\uD83C\uDD94\n" +
                "16. Go on vacation\uD83C\uDD94\n" +
                "17. Check my points\n" +
                "18. Logout\n" +
                "Enter 'exit' to exit the system at any time.\n");


        Scanner sc = new Scanner(System.in);
        String a = sc.nextLine();
        if (!a.equals("exit")) {
            switch (a) {
                case "1": { //view wishlist
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 Your wishlist are items that you want to have in the future! You can add or remove items from" +
                            " it as you please.\n");
                    ChosenOption option = new ChosenOption();
                    option.setChosenOption(new WishlistManager());
                    Object temp = option.executeOption(user, allItems, allTradeRequests, allUsers, allMeetings,
                            allTransactions, allAdmins, undoLogger, allUserMessages, currencyManager);
                    while (temp == null) {
                        temp = option.executeOption(user, allItems, allTradeRequests, allUsers, allMeetings,
                                allTransactions, allAdmins, undoLogger, allUserMessages, currencyManager);
                    }
                    return user;
                }
                case "2":  //view inventory
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, you can add items to be added to your inventory. " +
                            "Once you add an item, the item is sent to approval to Admin.\n");
                    return user;
                case "3": { //browse items
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, you can see all items available in the system to trade. " +
                            "Each item is approved by an admin.\n");
                    ChosenOption option = new ChosenOption();
                    option.setChosenOption(new Browse());
                    Object temp = option.executeOption(user, allItems, allTradeRequests, allUsers, allMeetings,
                            allTransactions, allAdmins, undoLogger, allUserMessages, currencyManager);
                    while (temp == null) {
                        temp = option.executeOption(user, allItems, allTradeRequests, allUsers, allMeetings,
                                allTransactions, allAdmins, undoLogger, allUserMessages, currencyManager);
                    }
                    return user;
                }
                case "4":  //choose the id?
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, you can choose an item and initiate a 1way or 2way " +
                            "permanent/temporary trade.\n");
                    //else input was "back", returns to main menu
                    return user;
                case "5":  //pending trade requests
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, you can see any pending trade requests.\n");
                    return user;
                case "6":  //approve pending trade requests
                    System.out.print("-------------------------------------------------------\n" +
                            "\uD83D\uDC81 As a user, you can look through all trade requests and their details and " +
                            "decide if you want to confirm or reject the trade.\n");
                    return user;
                case "7":  //request to add new item
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, you can add items to your inventory. However, once added, " +
                            "the request will be sent to admin for approval. \n" +
                            "Once approved, the item will show up in your inventory and it will be visible to other " +
                            "users when browsing.\n");
                    return user;
                case "8":  //View most recent trades
                    System.out.print("-------------------------------------------------------\n" +
                            "\uD83D\uDC81 As a user, you can see information about your 3 most recent trades.\n");
                    return user;
                case "9":  //View most frequent trading partners
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, you can see who your most frequently trading partners are.\n");
                    break;
                case "10":  //view item status
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, you can see a list of all items you have ever submitted to the " +
                            "system and whether they were approved/rejected by admin or still pending on admin's approval.\n");
                    return user;
                case "11":  //approve meeting
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, once a trade is accepted by the other party, " +
                            "you can see the suggested meeting, approve meeting or suggest an alternative " +
                            "meeting here.\n");
                    return user;
                case "12":  //confirm meetings
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, here you can confirm a meeting happened or if it did " +
                            "not happen. If the meeting did not happen, both parties may be penalized.\n");
                    return user;
                case "13":  //view outbound reqs
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, you can see the status of the outbound trade requests " +
                            "you have sent.\n");
                    return user;
                case "14":  //message admin
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, you message the Admin about general inquiries, questions, etc!\n" +
                            "You will also be able to view any replies from admin.");
                    return user;
                case "15":  //change account settings
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 As a user, you can change your account settings; this includes changing" +
                            " your username, password and set location.\nYou can view a log of all actions" +
                            " taken against your account by the Admin!\n");
                    return user;
                case "16":
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 By 'going on vacation', all your current items will be stored and not" +
                            "shown in the system inventory \nuntil you return! This prevents attempts to trade while you" +
                            "are out of town.\n");
                    return user;

                case "17":
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 This is where you can see how many points you have. You can get more points by completing more transactions.\n");
                    return user;
                case "18":
                    System.out.print("-------------------------------------------------------" +
                            "\n\uD83D\uDC81 Logging out as Demo!\n");
                    //logout
                    return null;
                default:  //if they input invalid response
                    System.out.print("\uD83E\uDDD0 Invalid Response!\n");
                    return user;
            }
        } else {//input is "exit"
            return a;
        }
        return user;
    }
}

