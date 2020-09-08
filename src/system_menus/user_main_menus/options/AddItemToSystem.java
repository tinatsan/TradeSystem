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

import java.util.Scanner;
import java.util.logging.Logger;

public class AddItemToSystem implements UserMainMenuOptions {
    /**
     * Deals with requesting to add a new item to the system's inventory. Prompts user for details of the item
     * and sends a request to the Admin for approval. Adds the item to the User's item history so they can
     * view its current status.
     *
     * @param user            the User requesting to add a new item to the system
     * @param allUsers        UserManager which stores all Users
     * @param currencyManager CurrencyManager which deals with the in-system currency
     * @return null if the current menu is to be reprinted; User user if the user is to be redirected to the main menu;
     * String "exit" if the user is to be logged out.
     */
    public Object execute(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                          UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                          AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {
        System.out.println("Please enter the name of item you would like to add or 'back' to go back to the main menu.");
        Scanner sc = new Scanner(System.in);
        String itemName = sc.nextLine();
        if (itemName.equals("back")) {
            return "back";
        }
        System.out.println("Please enter the description of item you would like to add or 'back' to go back to the main menu.");
        String description = sc.nextLine();
        if (description.equals("back")) {
            return "back";
        }

        boolean digital = false;
        System.out.println("If this item is digital, please enter '1'. Otherwise enter '2'. Enter 'back' to return to the main menu.");
        String input = sc.nextLine();
        if (input.equals("back")) {
            return "back";
        } else if (input.equals(1)) {
            digital = true;
        }

        System.out.println("Please enter '1' if you wish to trade this item, '2' if you wish to sell this item or\n" +
                "'3' if you wish make item rentable. If you wish to choose more than 1 option, please put the numbers together without space.\n For example 12 would be tradable and sellable.\n");
        System.out.println("Enter 'back' to go back to the main menu.");
        String option = sc.nextLine();
        String tradeOrSell = "Undefined";
        if (option.equals("back")) {
            return "back";
        }
        boolean t = false;
        boolean s = false;
        boolean r = false;
        while (tradeOrSell.equals("Undefined")) {
            if (option.equals("1")) {
                t = true;
                break;
            } else if (option.equals("2")) {
                s = true;
                break;
            } else if (option.equals("3")) {
                r = true;
                break;
            } else if (option.equals("12")) {
                t = true;
                s = true;
                break;
            } else if (option.equals("13")) {
                r = true;
                t = true;
                break;
            } else if (option.equals("23")) {
                s = true;
                r = true;
                break;
            } else if (option.equals("123")) {
                s = true;
                r = true;
                t = true;
                break;
            } else {
                System.out.println("That is not a valid option! Please try again.");
            }
        }
        Double rprice = null;
        Double sprice = null;


        if (s) { //if just sellable
            System.out.println("Please enter the price you would like to sell this item for \n");
            String priceInput = sc.nextLine();
            if (priceInput.equals("back")) {
                return "back";
            }
            try {
                Double temp = Double.parseDouble(priceInput);
                sprice = (double) Math.round(temp * 100) / 100;
            } catch (NumberFormatException e) {
                System.out.println("Invalid price! Please try again.");
            }
        }

        Integer days = null;
        if (r) { //if just sellable
            System.out.println("Please enter the price you would like to rent this item for \n");
            String priceInput = sc.nextLine();
            if (priceInput.equals("back")) {
                return "back";
            }
            try {
                Double temp = Double.parseDouble(priceInput);
                rprice = (double) Math.round(temp * 100) / 100;
            } catch (NumberFormatException e) {
                System.out.println("Invalid price! Please try again.");
            }

            System.out.println("Please enter number of days you would like to have this item be rented\n");
            String input2 = sc.nextLine();
            if (input2.equals("back")) {
                return "back";
            }
            try {
                days = Integer.parseInt(input2);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price! Please try again.");
            }

        }


        System.out.print("Please choose the category of item you would like to add " +
                "by typing the number or 'back' to go back to the main menu.\n");
        System.out.print("1.Electronics\n2.Automotive and car accessories\n3.Baby\n4.Beauty, Health and Personal Care" +
                "\n5.Books\n6.Home and Kitchen Supplies\n" +
                "7.Clothing\n8.Movies, music and TV\n9.Office Supplies\n10.Gaming\n");

        String ID = sc.nextLine();

        String category = "Undefined";
        while (category.equals("Undefined")) {
            if (ID.equals("back")) {
                return "back";
            } else if (ID.equals("1")) {
                category = "Electronics";
            } else if (ID.equals("2")) {
                category = "Automotive and car accessories";
            } else if (ID.equals("3")) {
                category = "Baby";
            } else if (ID.equals("4")) {
                category = "Beauty, Health and Personal Care";
            } else if (ID.equals("5")) {
                category = "Books";
            } else if (ID.equals("6")) {
                category = "Home and Kitchen supplies";

            } else if (ID.equals("7")) {
                category = "Clothing";

            } else if (ID.equals("8")) {
                category = "Movies, music and TV";

            } else if (ID.equals("9")) {
                category = "Office Supplies";

            } else if (ID.equals("10")) {
                category = "Gaming";
            } else {
                System.out.print("Please enter a category for the product!\n");
            }
        }
        Item newItem;

        newItem = new Item(itemName, user, description, category, digital, t, s,
                r, sprice, rprice, days);
        System.out.println("\nThe item you wish to add is the following: ");
        System.out.println("Item name: " + newItem.getName() + "\n" + "Item description: " +
                newItem.getDescription() + "\n"
                + "Item Category:" + newItem.getCategory());

        System.out.println("\nIf this is correct, please enter '1'. If you would like to change the item, " +
                "please enter '2'.");
        String confirmation = sc.nextLine();
        if (!confirmation.equals("1")) {
            if (!confirmation.equals("2")) {
                System.out.println("Invalid input. Please try adding the item again.");
                return null;
            }
            return null;
        }

        allUsers.addToDraftInventory(user, newItem);
        System.out.print("\n\u2705 Your request is sent to admin for approval!\n");
        allUsers.addToItemHistory(user, newItem);
        return null;
    }
}
