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

import java.util.Scanner;
import java.util.logging.Logger;

public class AccountSettingsManager implements UserMainMenuOptions {

    /**
     * Deals with changing the account settings of User-- they are able to change their username,
     * password and/or location.
     *
     * @param user            the User who wants to change their account settings
     * @param allUsers        UserManager which stores all the users in the system
     * @param currencyManager CurrencyManager which deals with the in-system currency
     * @return null if the current menu is to be reprinted; User user if the user is to be redirected to the main menu;
     * String "exit" if the user is to be logged out.
     */
    public Object execute(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                          UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                          AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {
        System.out.println("Please select the number beside the option you would like or 'back' " +
                "to return to the main menu.");
        System.out.println("1. View Admin Change log");
        System.out.println("2. Change username");
        System.out.println("3. Change password");
        System.out.println("4. Change location");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        if (input.equals("back")) {
            return "back";
        } else if (input.equals("1")) {
            if (user.getAdminActionHistory().size() == 0) {
                System.out.println("Admin has not taken actions against your account! :)");
            }
            for (int i = 0; i < user.getAdminActionHistory().size(); i++) {
                System.out.println(user.getAdminActionHistory().get(i));
            }
            return null;
        } else if (input.equals("2")) {
            Object temp = userNameChange(user, allUsers);
            while (temp == null) {
                temp = userNameChange(user, allUsers);
            }
            if (temp.equals("back")) {
                return "back";
            }
            return null;
        } else if (input.equals("3")) {
            Object temp = passwordChange(user);
            while (temp == null) {
                temp = passwordChange(user);
            }
            if (temp.equals("back")) {
                return "back";
            }
            return null;
        } else if (input.equals("4")) {
            Object temp = locationChange(user);
            while (temp == null) {
                temp = locationChange(user);
            }
            if (temp.equals("back")) {
                return "back";
            }
            return null;
        }
        System.out.println("\nThat is not a valid option, please try again.\n");
        return null;
    }

    /**
     * Deals with changing the User user's location.
     *
     * @param user the User that wants to change their location
     * @return returns different objects depending on the User user's input
     * returns "back" to return to the main menu
     * returns null to tell the accountSettings() to call locationChange() again
     * returns "account settings" to tell main menu to call accountSettings() again
     */
    public Object locationChange(User user) {
        if (user.getLocation() == null) {
            System.out.println("\nYou currently have no set location.");
        } else {
            System.out.println("\nYour current set location is " + user.getLocation());
        }
        System.out.println("\nHot tip: By setting a location, you will have the option to sort browse items" +
                " so you will only see items from users in the same location as you!\n");
        System.out.println("Please select from the following options:");
        System.out.println("1.Set location");
        System.out.println("2.Remove location");
        System.out.println("3.Return to account settings");
        System.out.println("Enter 'back' to return to the main menu.");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        if (input.equals("back")) {
            return "back";
        } else if (input.equals("1")) {
            if (user.getLocation() == null) {
                System.out.println("\nYou currently have no set location.\n");
            } else {
                System.out.println("\nYour current set location is " + user.getLocation() + "\n");
            }
            System.out.println("Please enter the name of the new location:");
            user.setLocation(sc.nextLine());
            System.out.println("You have successfully changed your location to " + user.getLocation() + ".");
            return null;
        } else if (input.equals("2")) {
            user.setLocation(null);
            System.out.println("You have successfully removed your location.");
        } else if (input.equals("3")) {
            return "account settings";
        }
        System.out.println("\nYou selected an invalid option, please try again.\n");
        return null;
    }

    /**
     * Deals with changing the User user's password.
     *
     * @param user the User that wants to change their password
     * @return returns different objects depending on the User user's input
     * returns "back" to return to the main menu
     * returns null to tell the accountSettings() to call passwordChange() again
     * returns "account settings" to tell main menu to call accountSettings() again
     */
    public Object passwordChange(User user) {
        System.out.println("If you like to continue with changing your password, please enter '1'. " +
                "Otherwise, please enter '2' to go back to the" +
                " other account settings.\nEnter 'back' to return to the main menu.");
        Scanner sc = new Scanner(System.in);
        String input2 = sc.nextLine();
        if (input2.equals("back")) {
            return "back";
        } else if (input2.equals("1")) {
            System.out.println("Please enter your current password:");
            String oldPassword = sc.nextLine();
            if (oldPassword.equals(user.getPassword())) {
                System.out.println("Please enter the new password:");
                String newPassword = sc.nextLine();
                user.setPassword(newPassword);
                System.out.println("\nYou have successfully changed your password.\n");
                return "account settings";
            }
            System.out.println("Incorrect password. Please try again.");
            return null;
        } else if (input2.equals("2")) {
            return "account settings";
        }
        System.out.println("\nYou did not select a valid option. Please try again.\n");
        return null;
    }

    /**
     * Deals with changing the User user's username.
     *
     * @param user     the User that wants to change their username
     * @param allUsers UserManager that stores all the users in the system
     * @return returns different objects depending on the User user's input
     * returns "back" to return to the main menu
     * returns null to tell the accountSettings() to call userNameChange() again
     * returns "account settings" to tell main menu to call accountSettings() again
     */
    public Object userNameChange(User user, UserManager allUsers) {
        System.out.println("\nYour current username is " + user.getName() + "\n");
        System.out.println("If you like to continue with changing your username, please enter '1'. " +
                "Otherwise, please enter '2' to go back to the" +
                " other account settings.\nEnter 'back' to return to the main menu.");
        Scanner sc = new Scanner(System.in);
        String input2 = sc.nextLine();
        if (input2.equals("back")) {
            return "back";
        } else if (input2.equals("1")) {
            System.out.println("Please enter a new username.");
            String newName = sc.nextLine();
            for (int i = 0; i < allUsers.getAllUsers().size(); i++) {
                if (allUsers.getAllUsers().get(i).getName().equals(newName)) {
                    System.out.println("Username " + newName + "is already taken! Please try again.");
                    return null;
                }
            }
            //if it makes it through without finding the same username, it will set newName as the
            //user's new username
            user.setName(newName);
            System.out.println("\nYou have successfully changed your username. Your username is now " +
                    user.getName() + ".\n");
            return "account settings";
        } else if (input2.equals("2")) {
            return "account settings";
        }
        System.out.println("\nYou did not select a valid option. Please try again.\n");
        return null;
    }

}
