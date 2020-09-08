package system;

import accounts.admins.Admin;
import accounts.admins.AdminManager;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.Item;
import items.ItemManager;
import notifications.NotifyAdminOfSuperAdminStatusChange;
import requests.TradeRequestManager;
import system_menus.ChosenOption;
import system_menus.admin_main_menus.options.*;
import transactions.TransactionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deals with the input of an Admin user-- particularly deals with the login system and displaying of main menu.
 */

public class AdminInputGetter {
    private static final Logger undoLogger = Logger.getLogger(AdminInputGetter.class.getName());
    private File undoLog = new File("UndoLog.txt");
    private static FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler("UndoLog.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to initialize FileHandler.");
        }
    }

    public AdminInputGetter() {
        undoLogger.setLevel(Level.ALL);
        fileHandler.setLevel(Level.ALL);
        undoLogger.addHandler(fileHandler);

        // Prevent fileHandler from printing to the console:
        undoLogger.setUseParentHandlers(false);
        // Credit for the above line goes to
        // https://stackoverflow.com/questions/2533227/how-can-i-disable-the-default-console-handler-while-using-the-java-logging-api
    }


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
     * Displays the main menu of an AdminUser and prompts the user for input.
     *
     * @param admin Account of the Admin.
     * @return depending on what the Admin inputs it will return different objects:
     * returns Admin to TradeSystem() to either remain logged into the system and prompt mainMenu
     * returns null to log out of the system and allow another Admin to log in
     * returns String "exit" to tell TradeSystem() to end the program and save all the data before
     * exiting the System
     */
    public Object mainMenu(Admin admin, AdminManager allAdmins, UserManager allUsers, ItemManager allItems,
                           UserMessageManager allUserMessages, TransactionManager allTransactions,
                           TradeRequestManager allRequests, CurrencyManager allCurrency) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("----------------------------------------------------------------------------------------------" +
                "\n\uD83D\uDC4B Welcome back, " + admin.getName());


        List<Item> allPendingItems = new ArrayList<>();
        for (int i = 0; i < allUsers.getAllUsers().size(); i++) {
            allPendingItems.addAll(allUsers.getAllUsers().get(i).getDraftInventory());
        }


        int frozenaccout = allUsers.getAllFrozenUsers().size();
        int pendingItem = allPendingItems.size();
        if (frozenaccout != 0 || pendingItem != 0)
            System.out.print("You have notifications!\n");


        if (allAdmins.getFrozenRequests().size() > 0) {
            System.out.println("\uD83D\uDCF3 You have " + allAdmins.getFrozenRequests().size() + " Frozen user requests!");
        }


        // if a super admin has changed this admin's super admin status,
        // a notification will be printed when this admin logs in
        NotifyAdminOfSuperAdminStatusChange notification = new NotifyAdminOfSuperAdminStatusChange();
        notification.notify(admin, allAdmins);

        System.out.println("Please select from the following by entering the number beside the option:" +
                " \n1. Add new admin\n2. Change system threshold\n3. View items that need to be approved" + supcreater(pendingItem) + "\n" +
                "4. Freeze or unfreeze users" + supcreater(frozenaccout) + "\n5. Promote a user or demote a VIP user\n" +
                "6. Promote an admin or demote a super admin\n7. View Messages from Users\n8. View and edit Undoable Actions Log\n9. Log out\n" +
                "Enter 'exit' to exit at any time.");
        ChosenOption option = new ChosenOption();
        try {
            String input = br.readLine();
            if (!input.equals("exit")) {
                switch (input) {
                    //depending on their input, the correct "strategy" will be created and stored in the system_options.ChosenOption class
                    case "1":  //add new admin
                        option.setChosenOption(new AddAdmin());
                        break;
                    case "2":  //change system threshold
                        option.setChosenOption(new ChangeThreshold());
                        break;
                    case "3":  //view items that need to be approved
                        option.setChosenOption(new ApprovePendingItem());
                        break;
                    case "4":  //freeze or unfreeze users
                        option.setChosenOption(new FreezeOrUnfreeze());
                        break;
                    case "5": //promote a user or demote a VIP user
                        option.setChosenOption(new PromoteOrDemoteUser());
                        break;
                    case "6":  //promote an admin or demote a super admin
                        option.setChosenOption(new PromoteOrDemoteAdmin());
                        break;
                    case "7":
                        option.setChosenOption(new ViewUserMessages());
                        break;
                    case "8":  //view and/or edit system log
                        option.setChosenOption(new UndoAction());
                        break;
                    case "9":  //logout
                        return null;
                    default:  //returns to main menu
                        System.out.println("That is not a valid option. Please try again.");
                        return admin;
                }
                //the option that is chosen by the Admin will be run
                Object result = option.executeOption(admin, allAdmins, allUsers, allItems, allUserMessages, allTransactions, allRequests, allCurrency, undoLogger);
                //if the execute() method of the option returns null, the option will be run again until the Admin
                //specifies that they want to return to the main menu
                while (result == null) {
                    result = option.executeOption(admin, allAdmins, allUsers, allItems, allUserMessages, allTransactions, allRequests, allCurrency, undoLogger);
                }
                return admin;
            }
            return input;
        } catch (IOException e) {
            System.out.println("Something went wrong.");
        }
        return admin;
    }

}
