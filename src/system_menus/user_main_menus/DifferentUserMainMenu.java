package system_menus.user_main_menus;

import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import currency.CurrencyManager;
import items.ItemManager;
import meetings.MeetingManager;
import requests.TradeRequestManager;
import transactions.TransactionManager;

import java.util.logging.Logger;

public interface DifferentUserMainMenu {
    Object mainMenu(User user, ItemManager allItems, TradeRequestManager allTradeRequests,
                    UserManager allUsers, MeetingManager allMeetings, TransactionManager allTransactions,
                    AdminManager allAdmins, Logger undoLogger, UserMessageManager allUserMessages, CurrencyManager currencyManager);
}
