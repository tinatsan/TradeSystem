package system_menus.admin_main_menus.options;

import accounts.admins.Admin;
import accounts.admins.AdminManager;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.ItemManager;
import requests.TradeRequestManager;
import transactions.TransactionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class ChangeThreshold implements AdminMainMenuOptions {

    /**
     * Changes the lentMinusBorrowedThreshold which dictates how much more a user has to have lent than borrowed,
     * before trading. The threshold change affects all Users in the system.
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
     */
    public Object execute(Admin admin, AdminManager allAdmins, UserManager allUsers, ItemManager allItems,
                          UserMessageManager allUserMessages, TransactionManager allTransactions,
                          TradeRequestManager allRequests, CurrencyManager allCurrency, Logger undoLogger) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //prints the current thresholds of the system
        System.out.println("\nHere are the current thresholds:");
        System.out.println("Lent - Borrowed Threshold: " + allAdmins.getLentMinusBorrowedThreshold());
        System.out.println("Weekly transaction limit: " + allAdmins.getWeeklyTransactionLimit());
        System.out.println("Incomplete transaction limit: " + allAdmins.getIncompleteTransactionLimit());
        System.out.println("Meeting edits threshold: " + allAdmins.getMeetingEditThreshold());
        //prompts user to enter what threshold they wish to edit
        System.out.println("\nWhich threshold would you like to edit? Please enter the number beside the option.");
        System.out.println("1. Lent - Borrowed Threshold\n2. Weekly Transaction Limit\n" +
                "3. Incomplete Transaction Limit\n4. Meeting Edits Threshold\n");
        System.out.println("Enter 'back' to return to the main menu.");
        Object thresholdOption = null;
        //checks to see if they entered a valid input (one of the options)
        try {
            String option = br.readLine();
            if (option.equals("back")) {
                return "back";
            } else {
                try {
                    thresholdOption = Integer.parseInt(option);
                } catch (NumberFormatException e) {
                    thresholdOption = "boo!";
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid input. Please try again.");
            return null;
        }
        //threshold option doesn't contain a number option
        if (!(thresholdOption instanceof Integer)) {
            System.out.println("Invalid input. Please try again.");
            return null;
        }
        if ((int) thresholdOption > 4 || (int) thresholdOption < 1) {
            System.out.println("Invalid input! Please try again.");
            return null;
        }
        //input was valid, now prompts admin to input what they want the new threshold to be
        System.out.println("Please enter the integer you wish to change the system threshold to or 'back' to the previous page.");
        Object newThreshold = null;
        //checks to see if they entered a valid input (one of the options)
        try {
            String input = br.readLine();
            if (input.equals("back")) {
                return null;
            } else {
                try {
                    newThreshold = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    newThreshold = "boo!";
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid input. Please try again.");
            return null;
        }
        if (newThreshold instanceof Integer) {
            if ((Integer) thresholdOption == 1) {
                allAdmins.setLentMinusBorrowedThreshold((Integer) newThreshold);
                System.out.println("\nThe threshold has been changed successfully. The lent - borrowed threshold is now: " +
                        newThreshold + ".");
            } else if ((Integer) thresholdOption == 2) {
                allAdmins.setWeeklyTransactionLimit((Integer) newThreshold);
                System.out.println("\nThe threshold has been changed successfully. The weekly transaction limit " +
                        "threshold is now: " + newThreshold + ".");
            } else if ((Integer) thresholdOption == 3) {
                allAdmins.setIncompleteTransactionLimit((Integer) newThreshold);
                System.out.println("\nThe threshold has been changed successfully. The incomplete transaction limit " +
                        "threshold is now: " + newThreshold + ".");
            } else { //thresholdOption == 4
                allAdmins.setMeetingEditThreshold((Integer) newThreshold);
                System.out.println("\nThe threshold has been changed successfully. The meeting edits " +
                        "threshold is now: " + newThreshold + ".");
            }
        } else {
            System.out.println("\nNo threshold has been changed. Please try again.");
            return null;
        }
        return null;
    }
}
