package system_menus.user_main_menus.options;

import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.Item;
import items.ItemManager;
import meetings.Meeting;
import meetings.MeetingManager;
import requests.*;
import transactions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApproveTrade implements UserMainMenuOptions {


    //this is taken from
// https://stackoverflow.com/questions/22195093/android-how-to-get-tomorrows-date
    public static class DateUtil {
        public static Date addDays(Date date, int days) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days); //minus number would decrement the days
            return cal.getTime();
        }
    }


    /**
     * Displays User user's pending requests and deals with approving and rejecting any pending Trade Requests.
     *
     * @param user            User that wishes to view and approve or reject their pending Trade requests
     * @param allUsers        UserManager that stores all Users
     * @param allMeetings     MeetingManager that deals with creating meetings
     * @param allTransactions TransactionManager that deals with the System's Transactions
     * @param currencyManager CurrencyManager which deals with the in-system currency
     * @return null if the current menu is to be reprinted; User user if the user is to be redirected to the main menu;
     * String "exit" if the user is to be logged out.
     */
    public Object execute(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                          UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                          AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {
        User Person = allUsers.getUser(user);
        List<TradeRequest> Trades = Person.getPendingRequests();
        if (Trades.size() == 0) {
            System.out.println("There are no pending trade requests!");
            return "back";
        }
        //-----------SHOWING ALL THE PENDING TRADE REQUESTS FOR THE USER TO CHOOSE WHICH ONE THEY WANNA SEE-----------
        System.out.print("Here are your pending trade requests: \n");
        for (int i = 0; i < Trades.size(); i++) {
            String ext = "";
            if (Trades.get(i) instanceof TypeOneRequest) {
                TypeOneRequest t = (TypeOneRequest) Trades.get(i);
                //monitized or not
                String ext1 = "";
                //the person is looking to buy
                if (t.getMonetized()) {
                    //its either buy or rent
                    if (t.getTemp()) { //renting over here
                        System.out.print("\uD83E\uDD1D" + (i + 1) + ". " + t.getFirstUser().getName() +
                                " wants to rent " + t.getItem().getName() + "\n");
                    } else {
                        String virtually = "";
                        if (t.getVirtual()) {
                            virtually = " virtually via email " + t.getMessage();
                        }

                        System.out.print("\uD83E\uDD1D" + (i + 1) + ". " + t.getFirstUser().getName() +
                                " wants to buy " + t.getItem().getName() + virtually + "\n");
                    }
                } else { //then they are one way trades
                    if (t.getTemp()) { //trading temporary over here
                        System.out.print("\uD83E\uDD1D" + (i + 1) + ". " + t.getFirstUser().getName() +
                                " wants to temporarily borrow " + t.getItem().getName() + "\n");
                    } else {
                        System.out.print("\uD83E\uDD1D" + (i + 1) + ". " + t.getFirstUser().getName() +
                                " wants to permanently have " + t.getItem().getName() + "\n");
                    }

                }
            }
            if (Trades.get(i) instanceof TypeTwoRequest) {
                TypeTwoRequest t = (TypeTwoRequest) Trades.get(i);
                //monitized or not
                System.out.print("\uD83E\uDD1D" + (i + 1) + ". " + t.getFirstUser().getName() + " wants item: " + t.getSecondItem().getName() + " in return for " + t.getFirstItem().getName() +
                        "\n");
            }
            if (Trades.get(i) instanceof TypeThreeRequest) {
                TypeThreeRequest t = (TypeThreeRequest) Trades.get(i);
                //--------------------------just displaying the message for the 3 way request
                if (t.getApproved() == 1) {
                    String firstPerson = t.getFirstUser().getName();
                    String secondPerson = t.getSecondUser().getName();
                    String thirdPerson = t.getThirdUser().getName();
                    Item youget;
                    Item foryouritem;
                    String one, two;
                    if (user.getName().equals(firstPerson)) {
                        one = secondPerson;
                        two = thirdPerson;
                        youget = t.getThirdItem();
                        foryouritem = t.getFirstItem();
                    } else { //user.getName().equals(thirdperson)
                        one = firstPerson;
                        two = secondPerson;
                        youget = t.getFirstItem();
                        foryouritem = t.getThirdItem();

                    }
                    System.out.print("\uD83E\uDD1D" + (i + 1) + ". " + "You are asked to join 3 way trade with " + one + " and " + two +
                            " and you will get item " +
                            youget.getName() + " for your item " + foryouritem.getName() + "\n");

                }
                //now here both people approve
                else if (t.getApproved() == 2) {
                    String firstPerson = t.getFirstUser().getName();
                    String secondPerson = t.getSecondUser().getName();
                    String thirdPerson = t.getThirdUser().getName();
                    Item youget;
                    Item foryouritem;
                    String one, two;
                    if (user.getName().equals(firstPerson)) {
                        one = secondPerson;
                        two = thirdPerson;
                        youget = t.getThirdItem();
                        foryouritem = t.getFirstItem();
                    } else { //user.getName().equals(thirdperson)
                        one = firstPerson;
                        two = secondPerson;
                        youget = t.getFirstItem();
                        foryouritem = t.getThirdItem();

                    }
                    System.out.print("\uD83E\uDD1D" + (i + 1) + ". " + t.getSecondUser().getName() + " decided to extend the 3 way trade! You will now get " + youget.getName() + " in exchange " +
                            "for " + foryouritem.getName() + "\n");

                }

            }

        }
        //-----------------NOW LET THE PERSON DECIDE WHAT WHICH TRADE THEY WANNA APPROVE OR REJECT --------


        //select request
        System.out.print("\n");
        System.out.print("\u2754 Please type the ID of the trade you would like to view or 'back' to return to the main menu.\n");
        Scanner sc = new Scanner(System.in);
        Object input = sc.next();

        if (input.equals(new String("back"))) {
            return "back";
        }
        int pendingRequestIndex;

        try {
            input = Integer.parseInt((String) input);
            pendingRequestIndex = (Integer) input - 1;
        } catch (NumberFormatException e) {
            return null;
        }

        System.out.print("You have selected the following pending trade: \n");
        String ext2 = "";
        String temp = "Permanent";
        TradeRequest cTrade = Trades.get(pendingRequestIndex);

        if (cTrade instanceof TypeOneRequest) {
            //print the trade
            //then acccept or deny
            TypeOneRequest t = (TypeOneRequest) cTrade;
            boolean money = t.getMonetized();
            //if monetized
            System.out.print("Here is the trade you selected: " + t.getItem() + "\n");
            //if its a virtual trade
            if (t.getVirtual()) {
                System.out.print("This is a virtual trade.\n");
            }

            System.out.print("Press 1 to approve and press 2 to deny!\n");
            input = sc.next();
            if (input.equals("1")) { //approved
                allTradeRequests.updateRequestStatus(allUsers, cTrade, 1);


                //if it is virtual
                if (t.getVirtual()) {
                    OneWayMonetized final1 = new OneWayMonetized(t.getFirstUser(), t.getItem(), false, t.getVirtual(), t.getMessage());
                    //no one has approved yet.

                    System.out.print("Funds are now held until you confirm that you have sent the email and " + t.getFirstUser().getName() +
                            " confirms that they have received the email.\n");
                    allTransactions.addToPendingTransactions(final1, allUsers, currencyManager);
                    undoLogger.log(Level.INFO, final1.toString());
                    allTradeRequests.updateRequestStatus(allUsers, cTrade, 1);

                    allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, final1, 1, currencyManager, undoLogger);
                    return user;

                }

                //check if it is monetized
                //check if it is temporary
                OneWay final1;
                if (money) {
                    if (t.getTemp()) {//if temporary
                        final1 = new OneWayMonetized(t.getFirstUser(), t.getItem(), true, t.getVirtual(), null);

                    } else { //sellin
                        final1 = new OneWayMonetized(t.getFirstUser(), t.getItem(), false, t.getVirtual(), null);
                    }
                } else { //if not money but its a trade
                    if (t.getTemp()) {//if temporary
                        final1 = new OneWay(t.getFirstUser(), t.getItem(), true, t.getVirtual());

                    } else { //selling
                        final1 = new OneWay(t.getFirstUser(), t.getItem(), false, t.getVirtual());
                    }
                }
                allTransactions.addToPendingTransactions(final1, allUsers, currencyManager);
                undoLogger.log(Level.INFO, final1.toString());

                Meeting meeting = meetingInitiator(allMeetings);
                User temp1 = t.getFirstUser(); //initiating the trade
                User temp2 = t.getSecondUser(); //owns the item
                meeting.initialconfirm(temp1.getName(), temp2.getName()); //this line creates a meeting that hasnt been confirmed
                meeting.initialHistory(temp1.getName(), 0);
                meeting.initialHistory(temp2.getName(), 1);
                final1.setInitialMeeting(meeting);
                final1.getInitialMeeting().changeLastEdit(user.getName());
                System.out.print("Approved by you!\n");
                return null;
            } else if (input.equals("2")) {//denied
                //pending 0
                //denied 2
                //approved 1
                allTradeRequests.updateRequestStatus(allUsers, cTrade, 2);
                System.out.print("Denied!\n");
                return null;
            }
        }
        if (cTrade instanceof TypeTwoRequest) {

            TypeTwoRequest t = (TypeTwoRequest) cTrade;
            System.out.print("Here is the trade you selected: " + "They want: " + t.getSecondItem() + "\n");
            System.out.print("Press 1 to approve and press 2 to deny and 3 to extend to a threeway\n");
            input = sc.next();
            if (input.equals("1")) { //approved
                allTradeRequests.updateRequestStatus(allUsers, cTrade, 1);
                TwoWay final1;
                if (t.getTemp()) {//if temporary
                    final1 = new TwoWay(t.getFirstItem(), t.getSecondItem(), true, t.getVirtual());

                } else { //selling
                    final1 = new TwoWay(t.getFirstItem(), t.getSecondItem(), false, t.getVirtual());
                }
                allTransactions.addToPendingTransactions(final1, allUsers, currencyManager);
                undoLogger.log(Level.INFO, final1.toString());

                Meeting meeting = meetingInitiator(allMeetings);
                User temp1 = t.getFirstUser(); //initiating the trade
                User temp2 = t.getSecondUser(); //owns the item
                meeting.initialconfirm(temp1.getName(), temp2.getName()); //this line creates a meeting that hasnt been confirmed
                meeting.initialHistory(temp1.getName(), 0);
                meeting.initialHistory(temp2.getName(), 1);
                final1.setInitialMeeting(meeting);
                final1.getInitialMeeting().changeLastEdit(user.getName());

                System.out.print("Approved by you!\n");
                return null;

            } else if (input.equals("2")) {//denied
                //pending 0-denied 2-approved 1
                allTradeRequests.updateRequestStatus(allUsers, cTrade, 2);
                System.out.print("Denied!\n");
                return null;

                //-----bringing in a third perosn
            } else { //briging in a 3rd person

                //Currently logged in user wants the item being offered but doesnt wanna give their item away so they choose a different
                //item from the inventory

                int index = 0;
                ArrayList<Item> theItems = new ArrayList<>();
                System.out.print("Here are other items you can choose from: \n");


                ArrayList<Item> userinventory = new ArrayList<>();
                //go through currently logged in user's inventory and grab all items that arent already involved in this trade
                for (int k = 0; k < user.getInventory().size(); k++) {
                    if (!user.getInventory().get(k).getName().equals(((TypeTwoRequest) cTrade).getFirstItem().getName()) && !user.getInventory().get(k).getName().equals(((TypeTwoRequest) cTrade).getSecondItem().getName())) {
                        userinventory.add(user.getInventory().get(k));
                    }
                }

                //display all these items
                if (userinventory.size() == 0) {
                    System.out.print("You do not have enough items to engage in a 3 way transaction. Please add more items!\n");
                    return user;
                }
                //here i display all the items
                for (int j = 0; j < userinventory.size(); j++) {
                    System.out.print((j + 1) + " . " + userinventory.get(j).getName() + "\n");
                }

                System.out.print("Please choose which item from your inventory you want to offer to third person to engage in the 3 way trade with!\n");
                Object input2 = sc.next();
                Integer index3way = Integer.parseInt((String) input2);
                index3way = index3way - 1;

                Item newItemFromUser2 = userinventory.get(index3way);

                //item2
                Item sadItem = ((TypeTwoRequest) cTrade).getSecondItem();

                // i need to find an item closest to sadItem that doesnt exist in user1 or user2's inventory


                //now lets see all other items that arent common between these two users to find a third person
                Item replacement = ((TypeTwoRequest) cTrade).getSecondItem();
                ArrayList<Item> nonuserinventory = new ArrayList<>();

                for (int l = 0; l < allItems.getSystemInventory().size(); l++) {
                    if (!allItems.getSystemInventory().get(l).getOwner().getName().equals(((TypeTwoRequest) cTrade).getFirstUser().getName()) &&
                            !allItems.getSystemInventory().get(l).getOwner().getName().equals(((TypeTwoRequest) cTrade).getSecondUser().getName())) {
                        nonuserinventory.add(allItems.getSystemInventory().get(l));
                    }
                }

                //now lets make a suggestion cuz idk
                if (nonuserinventory.size() == 0) {
                    System.out.print("Sorry but there are not enough items in the system to support three way trade! Try again later. \n");
                    return user;

                }

                for (int m = 0; m < nonuserinventory.size(); m++) {
                    if (sadItem.getCategory().equals(nonuserinventory.get(m))) {
                        replacement = nonuserinventory.get(m);
                        break;
                    } else { // i couldnt find any item that is in same category cuz im dumb
                        replacement = nonuserinventory.get(0);
                    }
                }

                //so now replacement is the third user's item
                allTradeRequests.updateRequestStatus(allUsers, cTrade, 2);
                TypeThreeRequest finalReq = new TypeThreeRequest(((TypeTwoRequest) cTrade).getFirstItem(), newItemFromUser2, replacement, "Kill me please",
                        cTrade.getTemp(), Calendar.getInstance(), cTrade.getVirtual());
                //well one person approved!
                finalReq.userApproves();
                allTradeRequests.receiveTradeRequest(allUsers, finalReq);

                System.out.println("Awesome! We have found an item to continue the trade." +
                        " We are notifying " + replacement.getOwner().getName() +
                        " as he is the owner of item: " + replacement.getName());

                Date now = new Date();
                SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
                System.out.println("--------------------------------------------\n" +
                        "Today is: " + simpleDateformat.format(now));
                System.out.print("Please put 1 for available and 2 for unavailable for the following days. " +
                        "\nBased on all 3 user's availabilities, we will provide set up earliest meeting for 11am and will notify you.\n" +
                        "If the system cannot find any common days between the three users for the next week, the trade request will be automatically cancelled, so put in as many days available as possible!\n");

                for (int q = 1; q < 8; q++) {
                    Date nextday = DateUtil.addDays(now, q);
                    System.out.print(simpleDateformat.format(nextday) + " : ");
                    Object able = sc.next();
                    if (able.equals("1")) {
                        finalReq.getUser1Availability().editChart(q, true);
                    } else if (able.equals("2")) {
                        finalReq.getUser1Availability().editChart(q, false);
                    }

                }

                System.out.print("\nPlease enter 3 locations where you would like to meet up. You can press enter after every location\n");
                Scanner sc2 = new Scanner(System.in);
                String location = sc2.nextLine();
                finalReq.addtoFirstUserLocation(location);

                location = sc2.nextLine();
                finalReq.addtoFirstUserLocation(location);

                location = sc2.nextLine();
                finalReq.addtoFirstUserLocation(location);

                System.out.print("The following three locations will be sent to other two users where they can choose from:\n");
                for (int s = 0; s < finalReq.getFirstUserLocation().size(); s++) {
                    System.out.print(finalReq.getFirstUserLocation().get(s) + "\n");
                }
                System.out.print("Request is now sent to the user two users!\n");

                return user;
            }


        }
        if (cTrade instanceof TypeThreeRequest) {
            TypeThreeRequest t = (TypeThreeRequest) cTrade;
            String firstPerson = t.getFirstUser().getName();
            String secondPerson = t.getSecondUser().getName();
            String thirdPerson = t.getThirdUser().getName();
            Item youget;
            Item foryouritem;
            String one, two;
            if (user.getName().equals(firstPerson)) {
                one = secondPerson;
                two = thirdPerson;
                youget = t.getThirdItem();
                foryouritem = t.getFirstItem();
            } else { //user.getName().equals(thirdperson)
                one = firstPerson;
                two = secondPerson;
                youget = t.getFirstItem();
                foryouritem = t.getThirdItem();

            }
            System.out.print("You have selected 3 way trade with " + one + " and " + two + " where you get item " + youget.getName() + "for" +
                    " item " + foryouritem.getName() + "\n");

            //i think i only get here if im about to confirm or reject the 3 way trade
            //awaitiung 2 people to approve
            if (t.getApproved() == 1) { //its time for the two other people to approve

                System.out.print("Please press 1 to approve this three way trade request and 2 for rejecting this request.\n");
                input = sc.next();

                if (input.equals("1")) { //they approved
//                        ThreeWay final3 = new ThreeWay(t.getFirstItem(), t.getSecondItem(), t.getThirdItem(), t.getTemp(), t.getVirtual());
//                        allTransactions.addToPendingTransactions(final3, allUsers, currencyManager);
//                        System.out.print("Approved and Transaction is made!\n");
//                        return user;
                    //if they approve, now user 2 has to approve of this too right?
                    t.userApproves();
                    t.setFirstApproved(user);
                    allTradeRequests.handleSingleApproved(allUsers, t, user);

                    //first choosing a location and seeing if that works for user 2

                    System.out.print("First user chose the following two locations to meet up at:\n");
                    for (int w = 0; w < t.getFirstUserLocation().size(); w++) {
                        System.out.print((w + 1) + " . " + t.getFirstUserLocation().get(w) + "\n");
                    }
                    System.out.print("Please choose locations that you can meet up at by inserting the number.\n" +
                            "For example, for only " + t.getFirstUserLocation().get(0) + "input 1 and if you would " +
                            "to choose " + t.getFirstUserLocation().get(0) + " and " + t.getFirstUserLocation().get(1)
                            + "please enter 12 and for all 3 locations enter 123\nKeep in mind that if any user cannot meet up at a set location, the meeting will be cancelled!\n enter '*' if you cannot meet " +
                            "at any of these locations and would like to cancel the request.\n");
                    input = sc.next();
                    if (input.equals("1")) {
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(0));
                    } else if (input.equals("2")) {
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(1));
                    } else if (input.equals("3")) {
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(2));
                    } else if (input.equals("12")) {
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(0));
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(1));
                    } else if (input.equals("13")) {
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(0));
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(2));
                    } else if (input.equals("23")) {
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(1));
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(2));
                    } else if (input.equals("123")) {
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(0));
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(1));
                        t.addtoSecondUserLocation(t.getFirstUserLocation().get(2));
                    } else if (input.equals("*")) {
                        allTradeRequests.updateRequestStatus(allUsers, cTrade, 2);
                        System.out.print("Cancelling the trade request!\n");
                        return user;
                    }


                    //getting the person's availability
                    Date now = new Date();
                    SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
                    System.out.println("--------------------------------------------\n" +
                            "Today is: " + simpleDateformat.format(now));
                    System.out.print("Please put 1 for available and 2 for unavailable for the following days. " +
                            "\nBased on all 3 user's availabilities, we will provide set up earliest meeting for 11am and will notify you.\n" +
                            "If the system cannot find any common days between the three users for the next week, the trade request will be automatically cancelled, so put in as many days available as possible!\n");

                    for (int q = 1; q < 8; q++) {
                        Date nextday = DateUtil.addDays(now, q);
                        System.out.print(simpleDateformat.format(nextday) + " : ");
                        Object able = sc.next();
                        if (able.equals("1")) {
                            t.getUser2Availability().editChart(q, true);
                        } else if (able.equals("2")) {
                            t.getUser2Availability().editChart(q, false);
                        }

                    }


                    System.out.print("All Done! New locations are sent to user 3 to approve and decide! Now let us wait on our third user to approve of this trade!\n");

                } else if (input.equals("2")) {
                    allTradeRequests.updateRequestStatus(allUsers, cTrade, 2);
                    System.out.print("Denied!\n");
                    return user;
                }


            }
            //only one freakin person left to approve!!!!!!!!!!!!!!!
            else if (t.getApproved() == 2)  //we are at user 1 approving this trade so we can move onto transaction finally
            {
                System.out.print("Please press 1 to approve this three way trade request and 2 for rejecting this request.\n");
                input = sc.next();

                if (input.equals("1")) { //user 1 is also okay with the new item being given to them everyone is happy
                    //everyone is okay with this 3 way trade yay
                    //now lets see if this person is okay with the locations given to them! :(
                    System.out.print("User 1 and 2 have chosen the following locations.\n");

                    for (int z = 0; z < t.getSecondUserLocation().size(); z++) {
                        System.out.print((z + 1) + " . " + t.getSecondUserLocation().get(z) + "\n");
                    }

                    System.out.print("Please choose one location for the three of you to meet. \nIf none" +
                            " of these locations are ideal, you can press '*' to cancel the trade request!\n");


                    Scanner sc5 = new Scanner(System.in);
                    Object input2 = sc5.nextLine();

                    if (input2.equals("*")) {
                        allTradeRequests.updateRequestStatus(allUsers, cTrade, 2);
                        System.out.print("Cancelling the trade request!\n");
                        return user;
                    }

                    try {
                        input2 = Integer.parseInt((String) input2);
                    } catch (NumberFormatException e) {
                        System.out.println("\n\uD83E\uDDD0 This ID is invalid. Please try again!n\n");
                        return null;
                    }

                    Integer index = (Integer) input2 - 1;

                    if ((Integer) t.getSecondUserLocation().size() <= index) {
                        System.out.print("This seems to be out of bound! Try again!\n");
                        return null;
                    }

                    t.setThirdUserLocation(t.getSecondUserLocation().get(index));

                    System.out.print("The chosen location for the transaction will be: " + t.getthirdUserLocation() + "\n");


                    Date now = new Date();
                    SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
                    System.out.println("--------------------------------------------\n" +
                            "Today is: " + simpleDateformat.format(now));
                    System.out.print("Please put 1 for available and 2 for unavailable for the following days. " +
                            "\nBased on all 3 user's availabilities, we will provide set up earliest meeting for 11am and will notify you.\n" +
                            "If the system cannot find any common days between the three users for the next week, the trade request will be automatically cancelled, so put in as many days available as possible!\n");

                    for (int q = 1; q < 8; q++) {
                        Date nextday = DateUtil.addDays(now, q);
                        System.out.print(simpleDateformat.format(nextday) + " : ");
                        Object able = sc.next();
                        if (able.equals("1")) {
                            t.getUser3Availability().editChart(q, true);
                        } else if (able.equals("2")) {
                            t.getUser3Availability().editChart(q, false);
                        }

                    }

                    //now i have to find a day that is common between all 3 people

                    //used https://stackoverflow.com/questions/51039668/how-to-retrieve-common-key-value-pairs-from-two-hashmaps
                    //as reference to find the commonality
                    Map<Integer, Boolean> commonMapbetweenU1andU2 = new HashMap<Integer, Boolean>();
                    for (Integer key : t.getUser1Availability().getChart().keySet()) {
                        if (t.getUser2Availability().getChart().get(key) != null) {
                            if (t.getUser1Availability().getChart().get(key).equals(t.getUser2Availability().getChart().get(key))) {
                                //so i found the common but it must also be avaialble
                                if (t.getUser1Availability().getChart().get(key).equals(true))
                                    commonMapbetweenU1andU2.put(key, t.getUser1Availability().getChart().get(key));
                            }
                        }
                    }
                    //so far, hopefully I've found a time common between the first two people

                    //if I can't find a time common between all 3 people, we gotta cancel sorry
                    if (commonMapbetweenU1andU2.isEmpty()) {
                        allTradeRequests.updateRequestStatus(allUsers, cTrade, 2);
                        System.out.print("Sorry but users don't seem to be available to meet in the next week. " +
                                "Cancelling this trade request.\n");
                        return user;
                    }

                    Map<Integer, Boolean> commonMapbetweenothertwoandU3 = new HashMap<Integer, Boolean>();
                    for (Integer key : t.getUser3Availability().getChart().keySet()) {
                        if (commonMapbetweenU1andU2.get(key) != null) {
                            if (t.getUser3Availability().getChart().get(key).equals(commonMapbetweenU1andU2.get(key))) {
                                if (t.getUser3Availability().getChart().get(key).equals(true))
                                    commonMapbetweenothertwoandU3.put(key, t.getUser1Availability().getChart().get(key));
                            }
                        }
                    }

                    if (commonMapbetweenothertwoandU3.isEmpty()) {
                        allTradeRequests.updateRequestStatus(allUsers, cTrade, 2);
                        System.out.print("Sorry but users don't seem to be available to meet in the next week. " +
                                "Cancelling this trade request.\n");
                        return user;
                    }

//getting one entry from the commonality, gotten with reference https://stackoverflow.com/questions/1509391/how-to-get-the-one-entry-from-hashmap-without-iterating/1509418

                    Map.Entry<Integer, Boolean> commonEntry = commonMapbetweenothertwoandU3.entrySet().iterator().next();
                    Integer commonDayIncrement = commonEntry.getKey();


                    Date meetingday = DateUtil.addDays(now, commonDayIncrement);
                    System.out.print("We found a common day between all 3 users and that is: " + simpleDateformat.format(meetingday) + "\n");
                    System.out.print("Setting up the meeting for upcoming " + simpleDateformat.format(meetingday) + " at 11 am local time at " + t.getthirdUserLocation() + "\n");


                    //source: https://www.tutorialspoint.com/how-to-get-current-day-month-and-year-in-java-8
                    LocalDate currentdate = LocalDate.now();
                    int meetingDay = currentdate.getDayOfMonth() + commonDayIncrement;
                    String meetingDayString = String.valueOf(meetingDay);

                    int meetingMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
                    //adding 1 cuz months in java are 0 indexed

                    String meetingMonthString = String.valueOf(meetingMonth);

                    int meetingYear = currentdate.getYear();
                    String meetingYearString = String.valueOf(meetingYear);


                    String meetingDate = meetingDay + "-" + meetingMonth + "-" + meetingYear;
                    String meetingTime = "11:00";

                    //the finalized meeting for the transaction is now created
                    Meeting finalizedmeeting = allMeetings.createMeeting(meetingDate, meetingTime, t.getthirdUserLocation());
                    finalizedmeeting.setConfirmedTrue();
                    finalizedmeeting.initial3confirm(t.getFirstUser().getName(), t.getSecondUser().getName(), t.getThirdUser().getName());
                    ThreeWay final3 = new ThreeWay(t.getFirstItem(), t.getSecondItem(), t.getThirdItem(), t.getTemp(), t.getVirtual());
                    allTransactions.addToPendingTransactions(final3, allUsers, currencyManager);
                    undoLogger.log(Level.INFO, final3.toString());


                    final3.setInitialMeeting(finalizedmeeting);
                    allTradeRequests.updateRequestStatus(allUsers, cTrade, 1);

                    allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, final3, 1, currencyManager, undoLogger);

                    //FINALLY EVERYONE IS HAPPY LETS PROPOSE A MEETING

                    return user;
                } else if (input.equals("2")) {
                    allTradeRequests.updateRequestStatus(allUsers, cTrade, 2);
                    System.out.print("Denied!\n");
                    return null;
                }
            }
        }
        return null;

    }

    private int getMonthNumber(String monthName) {
        return Month.valueOf(monthName.toUpperCase()).getValue();
    }

    /**
     * Helper function. If User user rejects the trade, it will remove the Trade Request from the user's pending
     * request list.
     *
     * @param user     the User that rejected the trade and wishes to have it removed from their pending requests
     * @param allUsers UserManager that contains User user
     * @param request  the Trade Request that is to be rejected and removed
     */
    public void rejectTrade(User user, UserManager allUsers, TradeRequest request) {
        allUsers.removeFromPendingRequests(user, request);
        System.out.print("\u274E Rejected!\n");
    }

    /**
     * Initiates a Meeting by asking the User for a proposed date, time and location for the meeting.
     *
     * @param allMeetings MeetingManager that deals with creating meetings
     * @return returns Meeting object containing all information of the proposed meeting
     */
    public Meeting meetingInitiator(MeetingManager allMeetings) {
        System.out.print("\uD83D\uDCC5 Please enter your proposed date for this trade in format dd-mm-yyyy\n");
        Scanner sc = new Scanner(System.in);
        String date = sc.nextLine();
        System.out.print("\uD83D\uDD5B Please enter your proposed time for this trade in format hh:mm\n");
        String time = sc.nextLine();
        if (!dateValidate(date, time)) {
            System.out.println("Invalid date and/or time. Please try again.\n");
            return meetingInitiator(allMeetings);
        }
        System.out.print("\uD83D\uDCCD Please enter your proposed location for this trade\n");
        String location = sc.nextLine();
        Meeting meeting = allMeetings.createMeeting(date, time, location);
        System.out.println("\nThis is your proposed date for this trade:");
        System.out.println(meeting.toString());
        System.out.println("\nIf this is correct, please enter '1'. If you would like to change the proposed date, " +
                "please enter '2'.");
        String confirmation = sc.nextLine();
        if (!confirmation.equals("1")) {
            if (!confirmation.equals("2")) {
                System.out.println("Invalid input. Please try proposing a date for this trade again.");
            }
            return meetingInitiator(allMeetings);
        }
        return meeting;
    }

    /**
     * Checks to see if the String date is a valid date in the calendar.
     * <p>
     * NOTE: This code is based off the code from the following website:
     * https://stackoverflow.com/questions/33968333/how-to-check-if-a-string-is-date
     *
     * @param date the date in the format dd-mm-yyyy
     * @param time the time in the format hh:mm
     * @return returns false if it is not a valid date, returns true if it is valid
     */
    public boolean dateValidate(String date, String time) {
        String dateTime = date + " " + time;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateTime.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}
