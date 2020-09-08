package system_menus.user_main_menus.options;

import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.Item;
import items.ItemManager;
import meetings.MeetingManager;
import requests.TradeRequest;
import requests.TradeRequestManager;
import requests.TypeOneRequest;
import requests.TypeTwoRequest;
import transactions.TransactionManager;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TradeInitiator implements UserMainMenuOptions {

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
     * @return depending on what the User inputs it will return different objects:
     * returns null to tell mainmenu() to call execute() again
     * returns String "back" to tell mainmenu() to prompt main menu again so User can choose another
     * main menu option
     * returns String "exit" to prompt TradeSystem to save all the information and exit the System
     */
    public Object execute(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                          UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                          AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {
        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream

        if (user.getEligibility() >= allAdmins.getLentMinusBorrowedThreshold()) {
            System.out.print("You are not eligible to do a trade! Sorry!\n");
            return user;
        }

        //if eligible

        Calendar today = Calendar.getInstance(); //get today's date
        boolean limit = allUsers.checkWeeklyRequestLimit(allAdmins, user, today);
        if (!limit) {
            System.out.print("You have exceeded the weekly request limit! Can't make another trade! Sorry! \n");
            return user;
        }

        Browse browsing = new Browse();
        browsing.DisplayBrowse(user, allItems);


        //----------- SO FAR IVE DISPLAYED THE ITEMS, NOW I NEED THE PERSON TO SELECT THE ITEM THEY WANT

        System.out.print("Please type in the ID of the item you would like or 'back' to return to the main menu.\nReminder that if the item is virtual," +
                "you will only be able to buy the item!\n");
        Object inum = sc.nextLine();
        if (inum.equals("back")) {
            return "back";
        }

        if (((String) inum).matches("[0-9]+") == false) {

            System.out.print("\u274CCommand Invalid. Please try again!\n");
            return null;
        }


        try {
            inum = Integer.parseInt((String) inum);
        } catch (NumberFormatException e) {
            return null;
        }

        if (((Integer) inum < 1) || ((Integer) inum > allItems.getSystemInventory().size())) {
            System.out.print("\uD83D\uDE35Input is out of bound. Please try again!\n");
            return null;

        }


        //the item they have selected
        Item tradeItem = allItems.getSystemInventory().get((Integer) inum - 1);


        if (tradeItem.getVirtual()) {

            if (user.getCapital() < tradeItem.getSellPrice()) {
                System.out.print("\uD83D\uDE14Sorry! You have $" + user.getCapital() + " but this item is priced at $" + tradeItem.getSellPrice());
                System.out.print("\nYou do not have enough money to buy this\n");
                return user;
            }
            System.out.print("This item is virtual and it is priced at $" + tradeItem.getSellPrice() + ". Please enter the email address you would like to receive the item in or \n" +
                    "type 'back' to go to previous menu.\n");
            String email = sc.nextLine();

            if (email.equals("back")) {
                return null;
            }

            //making sure the email is in the right format.
            boolean goodEmail = false;
            while (!goodEmail) {
                if (email.contains("@"))
                    goodEmail = true;
                else {
                    System.out.print("Please include an @ in the email address. '" + email + "' is missing an '@'\n");
                    email = sc.nextLine();
                }
            }
            TypeOneRequest trade = new TypeOneRequest(user, tradeItem, email, false, today, tradeItem.getVirtual(), true);
            allTradeRequests.receiveTradeRequest(allUsers, trade);
            undoLogger.log(Level.INFO, trade.toString());
            System.out.print("Your request is sent.\n");
            return user;
        }


        Boolean monetized = false;
        if (tradeItem.getSellable() || tradeItem.getRentable()) {
            monetized = true;
        }
        //block owner from making trades with themselves
        if (tradeItem.getOwner().getName().equals(user.getName())) {
            System.out.print("\uD83E\uDDD0 You are the owner so you cannot trade with yourself!\n");
            return null;
        }


        //myList is the list of items used to feed into the 1way or 2 way trades
        List<Item> myList = new ArrayList<>();
        myList.add(tradeItem);


        //if only sellable
        if (tradeItem.getSellable() && !tradeItem.getTradable() && !tradeItem.getRentable()) { //if only sellable

            if (user.getCapital() < tradeItem.getSellPrice()) {
                System.out.print("\uD83D\uDE14Sorry! You have $" + user.getCapital() + " but this item is priced at $" + tradeItem.getSellPrice());
                System.out.print("\nYou do not have enough money to buy this\n");
                return user;
            }

            String mssge = "";
            System.out.print("Please enter message for the owner of the item\n");
            mssge = sc.nextLine();
            TypeOneRequest trade = new TypeOneRequest(user, tradeItem, mssge, false, today, tradeItem.getVirtual(), true);
            allTradeRequests.receiveTradeRequest(allUsers, trade);
            undoLogger.log(Level.INFO, trade.toString());
            System.out.print("Your request is sent!\n");
            return user;
        }


        //if only rentable
        else if (tradeItem.getRentable() && !tradeItem.getSellable() && !tradeItem.getTradable()) { //if only rentable

            if (user.getCapital() < tradeItem.getRentPrice()) {
                System.out.print("\uD83D\uDE14Sorry! You have $" + user.getCapital() + " but this item is put for rent at $" + tradeItem.getRentPrice());
                System.out.print("\nYou do not have enough money to buy this\n");
                return user;
            }

            String mssge = "";
            System.out.print("Please enter message for the owner of the item\n");
            mssge = sc.nextLine();
            TypeOneRequest trade = new TypeOneRequest(user, tradeItem, mssge, true, today, tradeItem.getVirtual(), true);
            allTradeRequests.receiveTradeRequest(allUsers, trade);
            undoLogger.log(Level.INFO, trade.toString());
            System.out.print("Your request to rent item for " + tradeItem.getRentDuration() + " days is sent!\n");
            return user;


        }


        //if rentable and sellable
        if (tradeItem.getRentable() && tradeItem.getSellable() && !tradeItem.getTradable()) { //rentable and sellable but not tradable
            System.out.print("Please enter 1 if you want to rent the item. Enter 2 if you want to buy the item.\n");
            Object in = sc.nextLine();

            if (!in.equals("1") || !in.equals("2")) {
                System.out.print("\u274CCommand Invalid. Please try again!\n");
                return user;

            }


            String mssge = "";

            System.out.print("Please enter message for the owner of the item\n");
            mssge = sc.nextLine();

            if (in.equals("1")) { //rent the item

                if (user.getCapital() < tradeItem.getRentPrice()) {
                    System.out.print("\uD83D\uDE14Sorry! You have $" + user.getCapital() + " but this item is put for rent at $" + tradeItem.getRentPrice());
                    System.out.print("\nYou do not have enough money to buy this\n");
                    return user;
                }


                TypeOneRequest trade = new TypeOneRequest(user, tradeItem, mssge, true, today, tradeItem.getVirtual(), true);
                allTradeRequests.receiveTradeRequest(allUsers, trade);
                undoLogger.log(Level.INFO, trade.toString());
                System.out.print("Your request to rent item for " + tradeItem.getRentDuration() + " days is sent!\n");
                return user;

            } else if (in.equals("2")) { //sell the item
                if (user.getCapital() < tradeItem.getSellPrice()) {
                    System.out.print("\uD83D\uDE14Sorry! You have $" + user.getCapital() + " but this item is priced at $" + tradeItem.getSellPrice());
                    System.out.print("\nYou do not have enough money to buy this\n");
                    return user;
                }

                TypeOneRequest trade = new TypeOneRequest(user, tradeItem, mssge, false, today, tradeItem.getVirtual(), true);
                allTradeRequests.receiveTradeRequest(allUsers, trade);
                System.out.print("Your request is sent!\n");
                undoLogger.log(Level.INFO, trade.toString());
                return user;

            } else {
                System.out.print("\u274CCommand Invalid. Please try again!\n");
                return user;
            }
        }


        //sellable and tradable
        else if (!tradeItem.getRentable() && tradeItem.getSellable() && tradeItem.getTradable()) { //if it is sellable or tradable
            System.out.print("Please enter 1 if you want to buy the item. Enter 2 if you want to trade the item.\n");
            Object in = sc.nextLine();
            if (!in.equals("1") || !in.equals("2")) {
                System.out.print("\u274CCommand Invalid. Please try again!\n");
                return user;

            }

            String mssge = "";
            System.out.print("Please enter message for the owner of the item\n");
            mssge = sc.nextLine();

            //they want to buy
            if (in.equals("1")) {
                if (user.getCapital() < tradeItem.getSellPrice()) {
                    System.out.print("\uD83D\uDE14Sorry! You have $" + user.getCapital() + " but this item is priced at $" + tradeItem.getSellPrice());
                    System.out.print("\nYou do not have enough money to buy this\n");
                    return user;
                }
                TypeOneRequest trade = new TypeOneRequest(user, tradeItem, mssge, false, today, tradeItem.getVirtual(), true);
                allTradeRequests.receiveTradeRequest(allUsers, trade);
                undoLogger.log(Level.INFO, trade.toString());
                System.out.print("Your request is sent!\n");
                return user;
            } else if (in.equals("2")) {
                monetized = false;
                return tradableDealer(user, allItems, allTradeRequests,
                        allUsers, allMeetings, allTransactions,
                        allAdmins, undoLogger, allUserMessages, tradeItem, monetized, myList);

            } else {
                System.out.print("\u274CCommand Invalid. Please try again!\n");
                return user;
            }
        }


        //rentable and tradable
        else if (tradeItem.getRentable() && !tradeItem.getSellable() && tradeItem.getTradable()) { //if it is rentable or tradable


            System.out.print("Please enter 1 if you want to rent the item. Enter 2 if you want to trade the item.\n");
            Object in = sc.nextLine();

            if (!in.equals("1") || !in.equals("2")) {
                System.out.print("\u274CCommand Invalid. Please try again!\n");
                return user;

            }


            String mssge = "";
            System.out.print("Please enter message for the owner of the item\n");
            mssge = sc.nextLine();

            if (in.equals("1")) {   //if they want  to rent

                if (user.getCapital() < tradeItem.getRentPrice()) {
                    System.out.print("\uD83D\uDE14Sorry! You have $" + user.getCapital() + " but this item is put for rent at $" + tradeItem.getRentPrice());
                    System.out.print("\nYou do not have enough money to buy this\n");
                    return user;
                }

                TypeOneRequest trade = new TypeOneRequest(user, tradeItem, mssge, true, today, tradeItem.getVirtual(), true);
                allTradeRequests.receiveTradeRequest(allUsers, trade);
                System.out.print("Your request to rent item for " + tradeItem.getRentDuration() + " days is sent!\n");
                return user;
            } else if (in.equals("2")) { //if they want to trade
                monetized = false;
                return tradableDealer(user, allItems, allTradeRequests,
                        allUsers, allMeetings, allTransactions,
                        allAdmins, undoLogger, allUserMessages, tradeItem, monetized, myList);

            } else {

                System.out.print("\u274CCommand Invalid. Please try again!\n");
                return user;
            }

        } else if (!tradeItem.getRentable() && !tradeItem.getSellable() && tradeItem.getTradable()) { //if only tradable
            monetized = false;
            return tradableDealer(user, allItems, allTradeRequests,
                    allUsers, allMeetings, allTransactions,
                    allAdmins, undoLogger, allUserMessages, tradeItem, monetized, myList);

        } else {//all three options
            System.out.print("Please enter 1 if you want to rent the item. Enter 2 if you want to buy the item. Enter 3 if you want to trade the item\n");
            Object in = sc.nextLine();
            //rent
            if (in.equals("1")) {//they wanna rent
                String mssge = "";
                System.out.print("Please enter message for the owner of the item\n");
                mssge = sc.nextLine();

                if (user.getCapital() < tradeItem.getRentPrice()) {
                    System.out.print("\uD83D\uDE14Sorry! You have $" + user.getCapital() + " but this item is put for rent at $" + tradeItem.getRentPrice());
                    System.out.print("\nYou do not have enough money to buy this\n");
                    return user;
                }


                TypeOneRequest trade = new TypeOneRequest(user, tradeItem, mssge, true, today, tradeItem.getVirtual(), true);
                allTradeRequests.receiveTradeRequest(allUsers, trade);
                undoLogger.log(Level.INFO, trade.toString());
                System.out.print("Your request to rent item for " + tradeItem.getRentDuration() + " days is sent!\n");
                return user;

            }
            //buy
            else if (in.equals("2")) {  //if they want to buy the item
                if (user.getCapital() < tradeItem.getSellPrice()) {
                    System.out.print("\uD83D\uDE14Sorry! You have $" + user.getCapital() + " but this item is priced at $" + tradeItem.getSellPrice());
                    System.out.print("\nYou do not have enough money to buy this\n");
                    return user;
                }

                String mssge = "";
                System.out.print("Please enter message for the owner of the item\n");
                mssge = sc.nextLine();
                TypeOneRequest trade = new TypeOneRequest(user, tradeItem, mssge, false, today, tradeItem.getVirtual(), true);
                allTradeRequests.receiveTradeRequest(allUsers, trade);
                undoLogger.log(Level.INFO, trade.toString());
                System.out.print("Your request is sent!\n");
                return user;
            } else if (in.equals("3")) { //if they wanna trade
                monetized = false;
                return tradableDealer(user, allItems, allTradeRequests,
                        allUsers, allMeetings, allTransactions,
                        allAdmins, undoLogger, allUserMessages, tradeItem, monetized, myList);
            } else {
                System.out.print("\u274CCommand Invalid. Please try again!\n");
                return user;
            }
            //error check here!
        }

        //return user;
    }


    public Object tradableDealer(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                                 UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                                 AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, Item tradeItem, boolean monetized, List<Item> myList) {
        System.out.println("Please type '1' for one way trade and '2' for two way trade or 'back' to cancel the " +
                "current trade and restart.");
        Scanner sc = new Scanner(System.in);    //System.in is a standard input stream
        Calendar today = Calendar.getInstance(); //get today's date

        //trade type
        Object tType = sc.nextLine();

        if (tType.equals("back")) {
            return null;
        }
        //input error handling
        if (!tType.equals("1") && !tType.equals("2")) {
            System.out.print("\uD83E\uDDD0 Invalid response. Please try again.\n");
            return null;
        }
        try {
            tType = Integer.parseInt((String) tType);
        } catch (NumberFormatException e) {
            //brings them back to the first prompt where it asks to type the ID of the item.
            System.out.print("\uD83E\uDDD0 Invalid response. Please try again.\n");
            return null;
        }

        System.out.println("Please type '1' for temporary trade and '2' for permanent trade or 'back' to cancel the " +
                "current trade and restart.");
        Object type = sc.nextLine();
        //type is type of the trade temporary or permanent
        if (type.equals("back")) {
            return null;
        }
        boolean temp = false;

        if (type.equals("1")) { //if its temporary or permanent, 1 means its temporary
            temp = true;
        } else if (type.equals("2")) {
            temp = false;
        } else {
            System.out.print("\uD83E\uDDD0 Invalid response. Please try again.\n");
            return null;
        }

        System.out.print("Please put a message for the owner of the item:\n");
        String message = sc.nextLine();
        if ((int) tType == 1) { //1 way trade, do the following
            System.out.print("\nYou have selected 1 way trade for item: " + tradeItem.getName() + "\nYour message was: " + message + "\n");
            System.out.print("\nPlease enter '1' to confirm this trade or '2' to cancel the current trade and restart.\n");
            String confirmation = sc.nextLine();
            if (confirmation.equals("1")) {
                TradeRequest trades = new TypeOneRequest(user, tradeItem, message, temp, today, tradeItem.getVirtual(), monetized);
                allUsers.addToWeeklyRequestLimit(user, trades);
                allTradeRequests.receiveTradeRequest(allUsers, trades);

                undoLogger.log(Level.INFO, trades.toString());

                System.out.print("\nTrade request has been sent successfully.\n");
                return "back";
            } else if (confirmation.equals("2")) {
                System.out.print("\n\u274E Trade has been cancelled.\n");
                return null;
            } else {
                System.out.print("\uD83E\uDDD0 Invalid response. Please try again.\n");
                return null;
            }
            //trade request is made and it is pending in user's pendingRequests

        } else if ((int) tType == 2) { //if its a two way trade, do the following:
            System.out.print("You have selected 2 way trade for item: " + tradeItem.getName() + "\nYour message was: " + message + "\n");
            System.out.print("Which item would you like to trade?\n Please type in the number of the item you " +
                    "would like to trade or 'back' to cancel the current trade and restart.");

            User me = allUsers.getUser(user);
            List<Item> in = me.getInventory();
            if (in.size() == 0) {
                System.out.print("Your inventory is empty!\n");
                System.out.print("\u274E Trade initiation has been cancelled.\n");
                return null;
            }
            System.out.print("\nYour inventory: \n");

            //Here is where we print the person's inventory
            boolean suggested = false;
            List<Item> otherPersonWL = new ArrayList<>();
            otherPersonWL = allUsers.getUser(tradeItem.getOwner()).getWishlist();
            for (int i = 0; i < in.size(); i++) {
                String recom = "\n";

                System.out.println(i + 1 + ". " + in.get(i).getName() + recom);
            }

            //system can make its suggestions here if nothing from the owner's wishlist was in inventory :(
            if (!suggested) {
                String Recommend = "";
                User otherSide = allUsers.getUser(tradeItem.getOwner());
                //finding the category that the person is most fan of
                Map.Entry<String, Integer> category = getMaxEntryInMapBasedOnValue(otherSide.getFrequentCategory());
                //now ive found the most frequently wanted category or if they are all zero, i recommend the first one
                //now i need to scan through the inventory to see if i can recommend anything from there

                for (int i = 0; i < in.size(); i++) {
                    if (category.getKey().equals(in.get(i).getCategory())) {
                        Recommend = in.get(i).getName();
                        break;
                    } else
                        Recommend = in.get(0).getName(); //if you cant find anything, just grab the oldest item in the inventory to trade

                }

                System.out.print("System Suggestion based on other party's interest: " + Recommend + "\n");
            }


            Object inum2 = sc.nextLine();
            //returns them to the first prompt where they enter the id of the item to trade
            if (inum2.equals("back")) {
                return null;
            }
            //should be able to select what item
            try {
                inum2 = Integer.parseInt((String) inum2);
                if ((int) inum2 > in.size() || (int) inum2 < 1) { //if out of range
                    System.out.print("\uD83E\uDDD0 Invalid response. Please try again.\n");
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.print("\uD83E\uDDD0 Invalid response. Please try again.\n");
                return null;
            }
            Item salam = in.get((int) inum2 - 1);
            System.out.println("The item you want to trade is: " + in.get((int) inum2 - 1).getName() + "\n");
            System.out.print("Please enter '1' to confirm this trade or '2' to cancel the current trade and restart.");
            String confirmation = sc.nextLine();
            if (confirmation.equals("1")) {
                myList.add(salam);
                myList.add(tradeItem);
                TradeRequest request = new TypeTwoRequest(salam, tradeItem, message, temp, today, tradeItem.getVirtual());
                ;
                allTradeRequests.receiveTradeRequest(allUsers, request);
                allUsers.addToWeeklyRequestLimit(user, request);

                undoLogger.log(Level.INFO, request.toString());

                System.out.print("\nTrade request has been sent successfully.\n");
                return "back";
            } else if (confirmation.equals("2")) {
                System.out.print("\nTrade has been cancelled.\n");
                return null;
            } else {
                System.out.print("\uD83E\uDDD0 Invalid response. Please try again.Request cancelled.\n");
                return null;
            }
        } else {
            System.out.println("Invalid response. Please try again.Request cancelled.\n");
            return null;
        }


    }

    //taken from https://www.geeksforgeeks.org/how-to-find-the-entry-with-largest-value-in-a-java-map/
    // Find the entry with highest value
    public static <K, V extends Comparable<V>> Map.Entry<K, V>
    getMaxEntryInMapBasedOnValue(Map<K, V> map) {// To store the result
        Map.Entry<K, V> entryWithMaxValue = null;
        // Iterate in the map to find the required entry
        for (Map.Entry<K, V> currentEntry : map.entrySet()) {
            if (
                // If this is the first entry, set result as this
                    entryWithMaxValue == null

                            // If this entry's value is more than the max value
                            // Set this entry as the max
                            || currentEntry.getValue()
                            .compareTo(entryWithMaxValue.getValue())
                            > 0) {
                entryWithMaxValue = currentEntry;
            }
        }
        // Return the entry with highest value
        return entryWithMaxValue;
    }

}
