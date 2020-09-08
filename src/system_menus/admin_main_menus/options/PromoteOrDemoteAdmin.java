package system_menus.admin_main_menus.options;

import accounts.admins.Admin;
import accounts.admins.AdminManager;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.ItemManager;
import requests.TradeRequestManager;
import transactions.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class PromoteOrDemoteAdmin implements AdminMainMenuOptions {

    /**
     * Allows a super admin to:
     * promote an admin to super admin, or
     * demote a super admin to admin.
     * <p>
     * Prevents a non-super admin from accessing this menu.
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
     * returns Admin object to tell mainmenu() to prompt main menu again so Admin can choose another
     * main menu option
     */
    public Object execute(Admin admin, AdminManager allAdmins, UserManager allUsers, ItemManager allItems,
                          UserMessageManager allUserMessages, TransactionManager allTransactions,
                          TradeRequestManager allRequests, CurrencyManager allCurrency, Logger undoLogger) {

        if (!admin.getIsSuperAdmin()) {
            System.out.println("Sorry, but only super admins can access this menu!");
            return admin;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Press 1 to promote an admin to super admin. Press 2 to demote a super admin to an admin. " +
                "Press 3 to cancel.");
        switch (scanner.nextLine()) {
            case "1":
                List<String> listOfAdminNames = new ArrayList<>();
                System.out.println("Type the name of the admin to promote to super admin.");

                for (Admin indexedAdmin : allAdmins.getAllAdmins()) {

                    // the list of admins that will be displayed to the logged in super admin
                    if (!indexedAdmin.getIsSuperAdmin()) {
                        listOfAdminNames.add(indexedAdmin.getName());
                    }
                }

                if (listOfAdminNames.isEmpty()) {
                    System.out.println("No non-super admins found!");
                    return null;
                } else {
                    for (String adminName : listOfAdminNames) {
                        System.out.println(adminName);
                    }

                    String nameOfAdminChosenForPromotion = scanner.nextLine();

                    // first check whether the super admin typed in an actual admin name
                    if (listOfAdminNames.contains(nameOfAdminChosenForPromotion)) {

                        // loop through the list of admins to find the admin to promote
                        for (Admin indexedAdmin : allAdmins.getAllAdmins()) {
                            if (indexedAdmin.getName().equals(nameOfAdminChosenForPromotion)) {
                                indexedAdmin.setIsSuperAdmin(true);
                                allAdmins.addToSuperAdminStatusChangeNotifications(indexedAdmin,
                                        "Super admin " + admin.getName() +
                                                " has promoted you to super admin!");
                                System.out.println("Admin " + nameOfAdminChosenForPromotion +
                                        " was promoted to super admin!");
                            }
                        }
                        return admin;
                    } else {
                        System.out.println("Invalid input. Please try again.");
                        return null;
                    }
                }

            case "2":
                List<String> listOfSuperAdminNames = new ArrayList<>();
                System.out.println("Type the name of the super admin to demote to admin.");

                for (Admin indexedAdmin : allAdmins.getAllAdmins()) {

                    // the list of super admins that will be displayed to the logged in super admin
                    // add super admins to the list of super admins, but do not add the logged in super admin
                    if (indexedAdmin.getIsSuperAdmin() && !indexedAdmin.getName().equals(admin.getName())) {
                        listOfSuperAdminNames.add(indexedAdmin.getName());
                    }
                }

                if (listOfSuperAdminNames.isEmpty()) {
                    System.out.println("No other super admins found!");
                    return null;
                } else {
                    for (String superAdminName : listOfSuperAdminNames) {
                        System.out.println(superAdminName);
                    }

                    String nameOfSuperAdminChosenForDemotion = scanner.nextLine();

                    // first check whether the super admin typed in an actual super admin name
                    if (listOfSuperAdminNames.contains(nameOfSuperAdminChosenForDemotion)) {

                        // loop through the list of admins to find the super admin to demote
                        for (Admin indexedAdmin : allAdmins.getAllAdmins()) {
                            if (indexedAdmin.getName().equals(nameOfSuperAdminChosenForDemotion)) {
                                indexedAdmin.setIsSuperAdmin(false);
                                allAdmins.addToSuperAdminStatusChangeNotifications(indexedAdmin,
                                        "Super admin " + admin.getName() +
                                                " has demoted you to admin!");
                                System.out.println("Super admin " + nameOfSuperAdminChosenForDemotion +
                                        " was demoted to admin!");
                            }
                        }
                        return admin;
                    } else {
                        System.out.println("Invalid input. Please try again.");
                        return null;
                    }
                }
            case "3":
                return admin;
        }
        System.out.println("Invalid input. Please try again.");
        return null;
    }

}
