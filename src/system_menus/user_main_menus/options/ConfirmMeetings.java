package system_menus.user_main_menus.options;

import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.ItemManager;
import meetings.Meeting;
import meetings.MeetingManager;
import requests.TradeRequestManager;
import transactions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ConfirmMeetings implements UserMainMenuOptions {

    /**
     * Displays the pending transactions that the User user has. It will allow users to approve or edit the transactions
     * they currently have pending.
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
     * String "back" if the user is to be returned to the main menu..
     */
    public Object execute(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                          UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                          AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {
        System.out.print("Please select '1' for all your initial pending meetings+virtual meetings and '2' for all return meetings\n" +
                "Enter 'back' to return to the main menu.\n");
        Scanner sc4 = new Scanner(System.in);    //System.in is a standard input stream
        String selection = sc4.nextLine();
        if (selection.equals("back")) {
            return "back";
        }
        if (selection.equals("1")) {
            List<Transaction> userTransactions = new ArrayList<>();
            userTransactions = user.getAgreedUponMeeting();
            if (userTransactions.size() == 0) {
                System.out.print("No initial pending trade for you to confirm!\n");
                return null;
            }
            System.out.print("Here are your pending meetings ready to be confirmed!\n");
            //prints the pending meetings
            for (int i = 0; i < userTransactions.size(); i++) {
                if (userTransactions.get(i).getVirtual()) {
                    //if virtual, do these
                    OneWayMonetized t = (OneWayMonetized) userTransactions.get(i);
                    String otherSide = t.getFirstTrader().getName();
                    //finding the other side of this transaction

                    if (otherSide.equals(user.getName())) {
                        otherSide = t.getSecondTrader().getName();
                    }
                    System.out.print((i + 1) + " . " + "Virtual trade with: " + otherSide + " - email: (" + t.getEmail() + ")" + "\n");
                } else {
                    String otherSide = "";
                    Integer confirmed = userTransactions.get(i).getInitialMeeting().userconfirmed(user.getName());
                    String status = "";
                    if (confirmed == 1) {
                        status = " [CONFIRMED BY YOU] ";
                    }
                    //SAMPLE : 1 . Sat, Aug 8, 2020 11:00AM at 1 With: Mo

                    System.out.print((i + 1) + " . " + userTransactions.get(i).getInitialMeeting() + " With: " + userTransactions.get(i).getInitialMeeting().getOtherSide(user.getName()) + status + "\n");
                }
            }
            System.out.print("Please enter the ID of the transaction you would like to confirm or 'back' to return.\n");
            Scanner sc11 = new Scanner(System.in);
            if (sc11.equals("back")) {
                return "back";
            }
            int meetingIndex;
            try {
                meetingIndex = (Integer.parseInt(sc11.nextLine())) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please try again!");
                return null;
            }
            Transaction selectedTransaction = userTransactions.get(meetingIndex);
            //VIRTUAL
            if (selectedTransaction.getVirtual()) {
                OneWayMonetized t = (OneWayMonetized) selectedTransaction;
                System.out.print("You have selected:\n");

                System.out.print("Virtual trade - sending file to email: " + t.getEmail() + "\n");
                System.out.print("Press '1' to confirm that email has been sent from you! Press '2' to cancel the trade.\n" +
                        "Enter 'back' to return to the your list of pending meetings.\n");

                String action = sc11.nextLine();
                if (action.equals("back")) {
                    return null;
                }
                if (action.equals("1")) {
                    String otherSide = t.getFirstTrader().getName();
                    //finding the other side of this transaction
                    if (otherSide.equals(user.getName())) {
                        otherSide = t.getSecondTrader().getName();
                    }
                    if (!t.getPerson1Confirmed() && !t.getPerson2Confirmed()) { //if they both have not confirmed
                        t.Person1Confirmed();
                        System.out.print("Confirmed by you! Waiting on " + otherSide + " to confirm!\n");
                        return null;
                    } else if (t.getPerson1Confirmed()) {
                        //if one person has confirmed
                        t.Person2Confirmed();
                        allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 3, currencyManager, undoLogger);
                        //i probably should deal with money here or moe/aidan should idk
                        System.out.print("Confirmed by you! Looks like both sides have confirmed!\n");
                        return null;
                    }
                } else if (action.equals("2")) {
                    System.out.print("\u2639 Cancelling this transaction! We are sorry to hear that! Better luck next time!\n");
                    allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 4, currencyManager, undoLogger);
                }
            }
            System.out.print("You have selected:\n");
            Integer confirmed = selectedTransaction.getInitialMeeting().userconfirmed(user.getName());

            if (confirmed == 1) {
                System.out.print("You have already confirmed this meeting!\n");
                return null;
            }
            System.out.print(selectedTransaction.getInitialMeeting() + " With: " + selectedTransaction.getInitialMeeting().getOtherSide(user.getName()) + "\n");
            System.out.print("Press '1' to confirm that the meeting is done. Press '2' to cancel the meeting and press '3' if you got stood up\n" +
                    "Enter 'back' to return to your list of pending trades.\n");

            String action = sc11.nextLine();
            if (action.equals("back")) {
                return "back";
            }
            if (action.equals("1")) {
                //confirm meeting by the user
                selectedTransaction.getInitialMeeting().meetingConfirmed(user.getName());
                System.out.print("Confirmed that the meeting occurred on " + selectedTransaction.getInitialMeeting() + "\n");
                //lets check if both people have confirmed meeting
                if (selectedTransaction.getInitialMeeting().confirmedByBothSides()) {
                    //looks like the meeting was confirmed by both parties!
                    //now i have to check if it was 2 way or 3 way
                    if (selectedTransaction instanceof OneWay || selectedTransaction instanceof OneWayMonetized) {
                        System.out.print("\uD83E\uDD29 Looks like the meeting was confirmed by both sides!\n ");
                        if (!selectedTransaction.getTemp()) {
                            allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 3, currencyManager, undoLogger);
                            return null;
                        } else if (selectedTransaction.getTemp()) {
                            //if it was a temporary meeting, then I need to set up a second meeting
                            allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 2, currencyManager, undoLogger);
                            //by now, the second agreed upon meeting is set for both users

                            Calendar date = selectedTransaction.getInitialMeeting().getDate();

                            if (selectedTransaction instanceof OneWayMonetized) { //if it is rent
                                OneWayMonetized temp = (OneWayMonetized) selectedTransaction;
                                date.add(Calendar.DATE, temp.getItem().getRentDuration());

                            } else {
                                date.add(Calendar.MONTH, 1);
                            }
                            Meeting returnMeeting = new Meeting(date, selectedTransaction.getInitialMeeting().getPlace());
                            returnMeeting.initialconfirm(user.getName(), selectedTransaction.getInitialMeeting().getOtherSide(user.getName()));
                            System.out.print("REMINDER: You need to return the borrowed item(s) back by " + returnMeeting.toString() + "\n");
                            //need to add return meeting to transactions
                            allTransactions.setFinalMeeting(selectedTransaction, returnMeeting);
                        }
                    } else if (selectedTransaction instanceof TwoWay) {
                        System.out.print("\uD83E\uDD29 Looks like the meeting was confirmed by both sides!\n ");
                        if (!selectedTransaction.getTemp()) { //if it was a permanent transaction
                            allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 3, currencyManager, undoLogger);
                        } else if (selectedTransaction.getTemp()) {
                            //if it was a temporary meeting, then I need to set up a second meeting
                            allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 2, currencyManager, undoLogger);
                            //by now, the second agreed upon meeting is set for both users
                            Calendar date = selectedTransaction.getInitialMeeting().getDate();
                            date.add(Calendar.MONTH, 1);
                            Meeting returnMeeting = new Meeting(date, selectedTransaction.getInitialMeeting().getPlace());
                            returnMeeting.initialconfirm(user.getName(), selectedTransaction.getInitialMeeting().getOtherSide(user.getName()));
                            System.out.print("REMINDER: You need to return the borrowed item(s) back by " + returnMeeting.toString() + "\n");
                            //need to add return meeting to transactions
                            allTransactions.setFinalMeeting(selectedTransaction, returnMeeting);
                        }
                    } else if (selectedTransaction instanceof ThreeWay) {
                        selectedTransaction.getInitialMeeting().meetingConfirmed(user.getName());
                        //   System.out.print("Confirmed that the meeting occurred on " + selectedTransaction.getInitialMeeting() + "\n");
                        //if its confirmed by all 3 side
                        if (selectedTransaction.getInitialMeeting().confirmByThreeSides()) {
                            if (!selectedTransaction.getTemp()) { //if it was a permenant transaction
                                allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 3, currencyManager, undoLogger);
                                System.out.print("Guess it is confirmed by all 3 people?!\n");
                            } else if (selectedTransaction.getTemp()) { //if it was temporary
                                //if it was a temporary meeting, then I need to set up a second meeting
                                allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 2, currencyManager, undoLogger);
                                //by now, the second agreed upon meeting is set for both users
                                Calendar date = selectedTransaction.getInitialMeeting().getDate();
                                date.add(Calendar.MONTH, 1);
                                Meeting returnMeeting = new Meeting(date, selectedTransaction.getInitialMeeting().getPlace());
                                returnMeeting.initial3confirm(((ThreeWay) selectedTransaction).getFirstTrader().getName(), ((ThreeWay) selectedTransaction).getSecondTrader().getName(), ((ThreeWay) selectedTransaction).getThirdTrader().getName());

                                System.out.print("REMINDER: You need to return the borrowed item(s) back by " + returnMeeting.toString() + "\n");
                                //need to add return meeting to transactions
                                allTransactions.setFinalMeeting(selectedTransaction, returnMeeting);
                                return null;
                            }
                        }
                    }
                }
            } else if (selection.equals("2") || selection.equals("3")) { //cancelling
                System.out.print("\u2639 We are sorry to hear that! Better luck next time!\n");
                allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 4, currencyManager, undoLogger);
                return null;
            }
        } else if (selection.equals("2")) {
            System.out.print("Here are your meetings to return items:\n");
            List<Transaction> userTransactions = new ArrayList<>();
            userTransactions = user.getSecondAgreedUponMeeting();

            if (userTransactions.size() == 0) {
                System.out.print("No pending trade for you to confirm!\n");
                return null;
            }
            System.out.print("Here are your return pending meetings ready to be confirmed!\n");
            //prints the pending meetings
            for (int i = 0; i < userTransactions.size(); i++) {
                String otherSide = "";
                Integer confirmed = userTransactions.get(i).getReturnMeeting().userconfirmed(user.getName());
                String status = "";
                if (confirmed == 1) {
                    status = " [CONFIRMED BY YOU] ";
                }
                System.out.print((i + 1) + " . " + userTransactions.get(i).getInitialMeeting() + " With: " + userTransactions.get(i).getInitialMeeting().getOtherSide(user.getName()) + status + "\n");
            }
            System.out.print("Please enter the ID of the meeting you would like to confirm or 'back' to return to your list of pending meetings.\n");
            Scanner sc11 = new Scanner(System.in);
            if (sc11.equals("back")) {
                return null;
            }
            int meetingIndex;
            try {
                meetingIndex = (Integer.parseInt(sc11.nextLine())) - 1;
            } catch (NumberFormatException e) {
                System.out.println("This is not a valid input! Please try again!\n");
                return null;
            }
            if (meetingIndex >= userTransactions.size()) {
                System.out.println("This is not a valid input! Please try again!\n");
                return null;
            }
            Transaction selectedTransaction = userTransactions.get(meetingIndex);
            System.out.print("You have selected:\n");
            Integer confirmed = selectedTransaction.getInitialMeeting().userconfirmed(user.getName());
            //printing the confirmed transaction and its meeting and stuff
            System.out.print(selectedTransaction.getInitialMeeting() + " With: " + selectedTransaction.getInitialMeeting().getOtherSide(user.getName()) + "\n");
            System.out.print("Press '1' to confirm that the meeting is done! Enter 'back' to return to your list of pending meetings.\n");
            String action = sc11.nextLine();
            if (action.equals("back")) {
                return null;
            }
            if (action.equals("1")) {
                //confirm meeting by the user
                selectedTransaction.getReturnMeeting().meetingConfirmed(user.getName());

                System.out.print("Confirmed that the meeting occurred on " + selectedTransaction.getReturnMeeting() + "\n");

                //lets check if both people have confirmed meeting
                if (selectedTransaction.getReturnMeeting().confirmedByBothSides()) {
                    //looks like the meeting was confirmed by both parties!
                    if (selectedTransaction instanceof OneWay) {
                        System.out.print("\uD83E\uDD29 Looks like the meeting was confirmed by both sides!\n ");
                        allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 3, currencyManager, undoLogger);
                    }
                    if (selectedTransaction instanceof TwoWay) {
                        System.out.print("\uD83E\uDD29 Looks like the meeting was confirmed by both sides!\n ");
                        allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 3, currencyManager, undoLogger);
                    } else if (selectedTransaction instanceof ThreeWay) {
                        selectedTransaction.getInitialMeeting().meetingConfirmed(user.getName());
                        System.out.print("Confirmed!\n");
                        if (selectedTransaction.getReturnMeeting().confirmByThreeSides()) {
                            //if everyone confirmed
                            System.out.print("Looks like everyone confirmed!\n");
                            allTransactions.updateTransactionStatus(allItems, allUsers, allAdmins, selectedTransaction, 3, currencyManager, undoLogger);
                        }
                    }
                }
            }
        }
        return null;
    }
}
