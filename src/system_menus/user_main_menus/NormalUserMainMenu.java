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
import transactions.OneWay;
import transactions.TransactionManager;
import transactions.TwoWay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class NormalUserMainMenu implements DifferentUserMainMenu {


    /**
     * Displays number of notifications into a superscript format
     *
     * @param i is the number of notifications
     * @return the string format in superscript
     */
    String supcreater(int i) {
        if (i == 0) return "";
        else if (i == 1) return "[1]";
        else if (i == 2) return "[2]";
        else if (i == 3) return "[3]";
        else if (i == 4) return "[4]";
        else if (i == 5) return "[5]";
        else if (i == 6) return "[6]";
        else if (i == 7) return "[7]";
        else if (i == 8) return "[8]";
        else if (i == 9) return "[9]";
        else {
            return "[+9]";
        }
    }

    /**
     * Displays the main menu for a normal, unfrozen user and prompts user for input depending on what
     * they want to do.
     * <p>
     * Unfrozen Users are able to do the following:
     * View wishlist, view inventory, browse items, initiate trade, view messages, approve pending trades, add
     * items to inventory, view recent trades, view most frequent trading partners, view item statuses, add items to
     * wishlists, view approved trades, approve meetings, confirm meetings, and log out.
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
        if (user.getIsVIP()) {
            System.out.print("----------------------------------------------------------------------------------------------" +
                    "\n\uD83D\uDC4B Welcome back, \u2B50VIP\u2B50 " + user.getName() + "!\n");
        } else {
            System.out.print("----------------------------------------------------------------------------------------------" +
                    "\n\uD83D\uDC4B Welcome back, " + user.getName() + "!\n");
        }

        int pendingTradeRequests = allUsers.getUser(user).getPendingRequests().size();


        int pendingTransactions = allUsers.getUser(user).getPendingTrades().size();

        for (int t = 0; t < allUsers.getUser(user).getPendingTrades().size(); t++) {

            if (allUsers.getUser(user).getPendingTrades().get(t) instanceof OneWay) {
                OneWay tt = (OneWay) allUsers.getUser(user).getPendingTrades().get(t);
                User b = tt.getSecondTrader();
                if (user.getName().equals(b.getName())) {
                    b = tt.getFirstTrader();
                }
                if (tt.getInitialMeeting().geteditHistory(user.getName()) > tt.getInitialMeeting().geteditHistory(b.getName()) || tt.getInitialMeeting().viewLastEdit().equals(user.getName())) {
                    pendingTransactions = pendingTransactions - 1;
                }
            }
            if (allUsers.getUser(user).getPendingTrades().get(t) instanceof TwoWay) {
                TwoWay tt2 = (TwoWay) allUsers.getUser(user).getPendingTrades().get(t);
                User b2 = tt2.getFirstTrader(); //user b is the other party of this trade
                if (user.getName().equals(b2.getName())) {
                    b2 = tt2.getSecondTrader();
                }
                if (tt2.getInitialMeeting().geteditHistory(user.getName()) > tt2.getInitialMeeting().geteditHistory(b2.getName()) || tt2.getInitialMeeting().viewLastEdit().equals(user.getName())) {
                    //if they have already made an edit and we are waiting on the other person to approve/suggest a new meeting
                    pendingTransactions = pendingTransactions - 1;

                }
            }

        }


        int outboundRequests = allUsers.getUser(user).getOutboundRequests().size();


        int meetings = 0;
        for (int i = 0; i < allUsers.getUser(user).getAgreedUponMeeting().size(); i++) {
            if (allUsers.getUser(user).getAgreedUponMeeting().get(i).getVirtual()) {
                meetings = meetings + 1;
                continue;
            }
            if (allUsers.getUser(user).getAgreedUponMeeting().get(i).getInitialMeeting().userconfirmed(user.getName()) == 0)
                meetings = meetings + 1;
        }
        for (int i = 0; i < allUsers.getUser(user).getSecondAgreedUponMeeting().size(); i++) {
            if (allUsers.getUser(user).getSecondAgreedUponMeeting().get(i).getReturnMeeting().userconfirmed(user.getName()) == 0)
                meetings = meetings + 1;
        }


        int adminmssge = allUsers.getUser(user).getAdminMessages().size();


        if (adminmssge != 0 || meetings != 0 || outboundRequests != 0 || pendingTradeRequests != 0 ||
                pendingTransactions != 0)
            System.out.print("You got notifications!\n");

        // if admin has undone any actions on user's account, a String will be printed when user logs in
        NotifyUserOfAdminUndo notifyActions = new NotifyUserOfAdminUndo();
        notifyActions.notify(user, allUsers);

        // if admin has changed user's VIP status, a String will be printed when user logs in
        NotifyUserOfVIPStatusChange notification = new NotifyUserOfVIPStatusChange();
        notification.notify(user, allUsers);

        System.out.print("\uD83D\uDCB5Your money: $" + user.getCapital() + "\n");

        System.out.print("Please select number from the following:\n" +
                "1. View and edit Wishlist\n" +
                "2. View Inventory\n" +
                "3. Browse Items\n" +
                "4. Initiate Trade\n" +
                "5. Manage Payment Options\n" +
                "6. Approve Pending Trade Requests" + supcreater(pendingTradeRequests) + "\n" +
                "7. Add Item to inventory\n" +
                "8. View most recent trades\n" +
                "9. View most frequent trading partners\n" +
                "10. View status of my items\n" +
                "11. Approve meetings for pending transactions" + supcreater(pendingTransactions) + "\n" +
                "12. Confirm trade is done from your side" + supcreater(meetings) + "\n" +
                "13. View status of outbound requests" + supcreater(outboundRequests) + "\n" +
                "14. Message Admin and view replies" + supcreater(adminmssge) + "\n" +
                "15. Change Account Settings\n" +
                "16. Go on vacation\n" +
                "17. Check my points\n" +
                "18. Logout\n" +
                "Enter 'exit' to exit the system at any time.\n");


        ChosenOption option = new ChosenOption(); //stores, sets and runs the menu option that the user has chosen
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            String input = br.readLine();
            if (!input.equals("exit")) {
                //depending on their input, the correct "strategy" will be created and stored in the system_options.ChosenOption class
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
                    case "4":  //initiate trade
                        option.setChosenOption(new TradeInitiator());
                        break;
                    case "5":  //view pending trade requests
                        option.setChosenOption(new ManagePaymentOptions());
                        break;
                    case "6":  //approve trade reqs
                        option.setChosenOption(new ApproveTrade());
                        break;
                    case "7":  //add item to inventory
                        option.setChosenOption(new AddItemToSystem());
                        break;
                    case "8":  //view recent trades
                        option.setChosenOption(new PrintMostRecentTrades());
                        break;
                    case "9":  //view most freq trading partners
                        option.setChosenOption(new PrintTop3TradingPartners());
                        break;
                    case "10":   //view item status
                        option.setChosenOption(new PrintItemHistory());
                        break;
                    case "11":  //approve meeting
                        option.setChosenOption(new PendingTransactionProcess());
                        break;
                    case "12":  //confirm meeting for approved trades
                        option.setChosenOption(new ConfirmMeetings());
                        break;
                    case "13":  //view outbound req status
                        option.setChosenOption(new PrintOutboundRequests());
                        break;
                    case "14":  //message admin
                        option.setChosenOption(new UserMessage());
                        break;
                    case "15":  //change account settings
                        option.setChosenOption(new AccountSettingsManager());
                        break;
                    case "16":  //go on vacation
                        option.setChosenOption(new VacationPrompter());
                        break;
                    case "17":  //points
                        option.setChosenOption(new PointsManager());
                        break;
                    case "18":  //logout
                        return null;
                    default:  //returns to main menu
                        System.out.print("\u274CCommand Invalid. Please try again!\n");
                        return user;
                }
                //the option that is chosen by the user will be run
                Object result = option.executeOption(user, allItems, allTradeRequests, allUsers, allMeetings,
                        allTransactions, allAdmins, undoLogger, allUserMessages, currencyManager);
                //if the execute() method of the option returns null, the option will be run again until the user
                //specifies that they want to return to the main menu
                while (result == null) {
                    result = option.executeOption(user, allItems, allTradeRequests, allUsers, allMeetings,
                            allTransactions, allAdmins, undoLogger, allUserMessages, currencyManager);
                }

                // this check is used to log user out when they confirm that they are going on vacation
                if (result.equals("leave")) {
                    return null;
                }
                //tells TradeSystem() to stay logged in to this user's account; helps with looping the main menu
                return user;
            }
            //tells TradeSystem() to log out and bring the user back to the login screen
            return input;
        } catch (IOException e) {
            System.out.println("Something went wrong.");
            return user;
        }
    }

}
