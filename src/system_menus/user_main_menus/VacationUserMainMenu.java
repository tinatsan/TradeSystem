package system_menus.user_main_menus;

import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.Item;
import items.ItemManager;
import meetings.MeetingManager;
import notifications.NotifyUserOfVIPStatusChange;
import requests.TradeRequestManager;
import system_menus.ChosenOption;
import system_menus.user_main_menus.options.ConfirmMeetings;
import transactions.TransactionManager;

import java.util.Scanner;
import java.util.logging.Logger;

public class VacationUserMainMenu implements DifferentUserMainMenu {

    /**
     * Displays the main menu for a User on vacation. Users on vacation can either stay on vacation or declare they have
     * returned from vacation, in which case their inventory Items are restored and they can participate in the trade
     * system again.
     *
     * @param user             User currently logged into the system
     * @param allItems         The instance of ItemManager
     * @param allTradeRequests The instance of TradeRequestManager
     * @param allUsers         The instance of UserManager
     * @param allMeetings      The instance of MeetingManager
     * @param allTransactions  The instance of TransactionManager
     * @param allAdmins        The instance of AdminManager
     * @param undoLogger       Logger that logs actions in the system
     * @param allUserMessages  The instance of UserMessageManager
     * @return null if the current menu is to be reprinted; User user if the user is to be redirected to the main menu;
     * String "exit" if the user is to be logged out.
     */
    public Object mainMenu(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                           UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                           AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("----------------------------------------------------------------------------------------------" +
                "\n\uD83D\uDC4B Welcome back, " + user.getName() + "!\n");

        // if admin has changed user's VIP status, a String will be printed when user logs in
        NotifyUserOfVIPStatusChange notification = new NotifyUserOfVIPStatusChange();
        notification.notify(user, allUsers);

        System.out.println("Press 1 to remain on vacation. Press 2 to return from vacation. Press 3 to confirm a trade " +
                "is done from your side.");
        String confirmation = scanner.nextLine();
        switch (confirmation) {
            case "1":
                System.out.println("Go enjoy your vacation!");
                return null;
            case "2":
                System.out.println("We hope you enjoyed your vacation!");
                user.setIsOnVacation(false);
                for (int i = 0; i < user.getVacationStorage().size(); i++) {
                    Item item = user.getVacationStorage().get(i);
                    allUsers.addToInventory(user, item);
                    allUsers.removeFromVacationStorage(user, item);
                }
                return user;
            case "3":
                ChosenOption option = new ChosenOption();
                option.setChosenOption(new ConfirmMeetings());
                option.executeOption(user, allItems, allTradeRequests, allUsers, allMeetings,
                        allTransactions, allAdmins, undoLogger, allUserMessages, currencyManager);
                return user;
            default:
                System.out.println("Invalid option. Please try again.");
                return null;
        }
    }
}
