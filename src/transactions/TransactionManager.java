package transactions;

import accounts.admins.AdminManager;
import accounts.users.User;
import accounts.users.UserManager;
import currency.CurrencyManager;
import items.Item;
import items.ItemManager;
import meetings.Meeting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages all Transactions. Changing their values by accessing their setters/getters. Stores all instances of Transactions. Should only be instantiated once.
 */
public class TransactionManager implements Serializable {

    private List<Transaction> inProgressTransaction; //0
    private List<Transaction> finalizedMeeting; //1
    private List<Transaction> pendingSecondExchange; //2
    private List<Transaction> completedTransaction; //3
    private List<Transaction> cancelledTransaction; //4
    private List<Transaction> allTransactions; // stores every Transaction ever instantiated.


    /**
     * Constructs TransactionManager. Creates three ArrayList which store all instances of Transaction: inProgressTransaction, completedTransaction, cancelledTransaction.
     * Does not require argument for instantiation.
     */
    public TransactionManager() {
        inProgressTransaction = new ArrayList<>();
        completedTransaction = new ArrayList<>();
        cancelledTransaction = new ArrayList<>();
        pendingSecondExchange = new ArrayList<>();
        finalizedMeeting = new ArrayList<>();
        allTransactions = new ArrayList<>();
    }

    /**
     * Updates the tradeStatus of a given Transaction.
     * 0: In progress.
     * 1: Finalized Meeting (the initialMeeting has been set!).
     * 2: Pending Second Exchange (only for temporary Transaction).
     * 3: Completed
     * 4: Cancelled.
     * Notice by updating the tradeStatus the instance of Transaction is being moved to a new attribute list within TransactionManager.
     * finalizedMeeting implies the initialMeeting has been set.
     * pendingSecondExchange means the first exchange was successful, but the User(s) must return their Item a month after the initialMeeting.
     * Cancelled implies one of the following: User(s) did not show up to the Meeting or User(s) altered
     * meeting too many times, and completed means the Transaction was successful, and the Item(s) have been officially swapped.
     * Completed means either the finalizedMeeting had occurred (for permanent Transaction), or the secondExchange has occurred.
     *
     * @param itemManager Class that manages Items.
     * @param userManager Class that manages Users.
     * @param transaction The given Transaction.
     * @param tradeStatus The current status of a given Transaction.
     * @param undoLogger  Keeps track of all actions that can be reasonably undone.
     */
    public void updateTransactionStatus(ItemManager itemManager, UserManager userManager, AdminManager adminManager,
                                        Transaction transaction, int tradeStatus, CurrencyManager currencyManager, Logger undoLogger) {
        User user1;
        User user2;
        User user3;
        if (transaction.getTradeStatus() == 0) {
            this.inProgressTransaction.remove(transaction);
        } else if (transaction.getTradeStatus() == 1) {
            this.finalizedMeeting.remove(transaction);
        } else if (transaction.getTradeStatus() == 2) {
            this.pendingSecondExchange.remove(transaction);
        } else if (transaction.getTradeStatus() == 3) {
            this.completedTransaction.remove(transaction);
        } else {
            this.cancelledTransaction.remove(transaction);
        }

        if (tradeStatus == 0) {
            this.inProgressTransaction.add(transaction);
        } else if (tradeStatus == 1) {
            this.finalizedMeeting.add(transaction);
            if (transaction instanceof OneWay) {
                user1 = userManager.getUser(((OneWay) transaction).getFirstTrader());
                user2 = userManager.getUser(((OneWay) transaction).getSecondTrader());
                user3 = null;
            } else if (transaction instanceof TwoWay) {
                user1 = userManager.getUser(((TwoWay) transaction).getFirstTrader());
                user2 = userManager.getUser(((TwoWay) transaction).getSecondTrader());
                user3 = null;
            } else {
                user1 = userManager.getUser(((ThreeWay) transaction).getFirstTrader());
                user2 = userManager.getUser(((ThreeWay) transaction).getSecondTrader());
                user3 = userManager.getUser(((ThreeWay) transaction).getThirdTrader());
            }
            userManager.removeFromPendingTrades(user1, transaction);
            userManager.removeFromPendingTrades(user2, transaction);
            userManager.addToAgreedUponMeetings(user1, transaction);
            userManager.addToAgreedUponMeetings(user2, transaction);
            if (user3 != null) {
                userManager.removeFromPendingTrades(user3, transaction);
                userManager.addToAgreedUponMeetings(user3, transaction);
            }
        } else if (tradeStatus == 2) {
            this.pendingSecondExchange.add(transaction);
            handleFirstExchange(userManager, transaction, itemManager, currencyManager);
        } else if (tradeStatus == 3) {
            this.completedTransaction.add(transaction);
            if (transaction.getTemp()) {
                handleSecondExchange(userManager, itemManager, transaction);
            } else {
                handleCompletedPerm(userManager, itemManager, transaction, currencyManager);
            }
        } else {
            this.cancelledTransaction.add(transaction);
            handleCancelledTrade(adminManager, userManager, transaction, currencyManager);
        }

        transaction.setTradeStatus(tradeStatus);
        if (!(transaction instanceof OneWayMonetized))
            transaction.getInitialMeeting().setConfirmedTrue();

        undoLogger.log(Level.INFO, transaction.toString());

    }


    /**
     * Initiates and stores the finalMeeting within Transaction. Only accessed if Transaction temp = True, otherwise is kept Null
     *
     * @param transaction The given transaction. InitialMeeting is stored in this instance
     * @param meeting     The initial meeting, which is stored in the given Transaction
     */
    public void setFinalMeeting(Transaction transaction, Meeting meeting) {
        transaction.setReturnMeeting(meeting);
    }

    /**
     * Adds a given new Transaction to the attribute list pendingTransaction held in TransactionManager.
     * Pending implies the Transaction is in progress.
     *
     * @param transaction The specified Transaction that has been created.
     * @param userManager The instance of UserManager.
     */
    public void addToPendingTransactions(Transaction transaction, UserManager userManager,
                                         CurrencyManager currencyManager) {
        User user1;
        User user2;
        User user3;
        if (transaction instanceof OneWay) {
            user1 = userManager.getUser(((OneWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((OneWay) transaction).getSecondTrader());
            user3 = null;
            if (transaction instanceof OneWayMonetized) {
                currencyManager.holdFunds((OneWayMonetized) transaction, userManager);
            }

        } else if (transaction instanceof TwoWay) {
            user1 = userManager.getUser(((TwoWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((TwoWay) transaction).getSecondTrader());
            user3 = null;
        } else {
            user1 = userManager.getUser(((ThreeWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((ThreeWay) transaction).getSecondTrader());
            user3 = userManager.getUser(((ThreeWay) transaction).getThirdTrader());
        }
        userManager.addToPendingTrades(user1, transaction);
        userManager.addToPendingTrades(user2, transaction);
        if (user3 != null) {
            userManager.addToPendingTrades(user3, transaction);
        }
        this.allTransactions.add(transaction);
        inProgressTransaction.add(transaction);
    }


    /**
     * Executes the back-end results of a Transaction being cancelled. A Transaction being cancelled can result in being frozen (unable to participate in trading).
     *
     * @param userManager The instance of UserManager.
     * @param transaction The given instance of Transaction.
     */
    public void handleCancelledTrade(AdminManager adminManager, UserManager userManager, Transaction transaction,
                                     CurrencyManager currencyManager) {
        User user1;
        User user2;
        User user3;
        if (transaction instanceof OneWay) {
            user1 = userManager.getUser(((OneWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((OneWay) transaction).getSecondTrader());
            user3 = null;
            if (transaction instanceof OneWayMonetized) {
                currencyManager.reverseHold((OneWayMonetized) transaction, userManager);
            }
        } else if (transaction instanceof TwoWay) {
            user1 = userManager.getUser(((TwoWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((TwoWay) transaction).getSecondTrader());
            user3 = null;
        } else {
            user1 = userManager.getUser(((ThreeWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((ThreeWay) transaction).getSecondTrader());
            user3 = userManager.getUser(((ThreeWay) transaction).getThirdTrader());
        }
        userManager.addToCancelledTransactions(user1, transaction);
        userManager.addToCancelledTransactions(user2, transaction);

        if (transaction.getTradeStatus() == 0) {
            userManager.removeFromPendingTrades(user1, transaction);
            userManager.removeFromPendingTrades(user2, transaction);
        } else if (transaction.getTradeStatus() == 1) {
            userManager.removeFromAgreedUponMeetings(user1, transaction);
            userManager.removeFromAgreedUponMeetings(user2, transaction);
        }

        if (user1.getCancelledTransactions().size() == adminManager.getIncompleteTransactionLimit()) {
            userManager.pseudoFreeze(user1);
        }
        if (user2.getCancelledTransactions().size() == adminManager.getIncompleteTransactionLimit()) {
            userManager.pseudoFreeze(user2);
        }

        if (user3 != null) {
            userManager.addToCancelledTransactions(user3, transaction);

            if (transaction.getTradeStatus() == 0) {
                userManager.removeFromPendingTrades(user3, transaction);
            } else if (transaction.getTradeStatus() == 1) {
                userManager.removeFromAgreedUponMeetings(user3, transaction);
            }

            if (user3.getCancelledTransactions().size() == adminManager.getIncompleteTransactionLimit()) {
                userManager.pseudoFreeze(user3);
            }
        }
    }

    /**
     * Handles the back-end completion of a temporary Transaction. Notice the second exchange is returning objects back to original owner. Only called in updateTransactionStatus.
     *
     * @param userManager The instance of UserManager.
     * @param transaction The given temporary Transaction.
     * @param itemManager The instance of ItemManager.
     */
    public void handleSecondExchange(UserManager userManager, ItemManager itemManager, Transaction transaction) {
        User user1;
        User user2;
        User user3;
        Item item1;
        if (transaction instanceof OneWay) {
            item1 = ((OneWay) transaction).getItem();
            user1 = userManager.getUser(((OneWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((OneWay) transaction).getSecondTrader());
            user3 = null;
            userManager.removeFromInventory(user1, item1);
            userManager.addToInventory(user2, item1);
            itemManager.setCurrentHolder(item1, user2);

            //item 1 is for user 1, item 2 is for user 2 etc, probably called wrong items when creating transaction
            //aka requesterItem vs receiverItem
        } else if (transaction instanceof TwoWay) {
            user1 = userManager.getUser(((TwoWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((TwoWay) transaction).getSecondTrader());
            user3 = null;
            item1 = ((TwoWay) transaction).getFirstItem();
            Item item2 = ((TwoWay) transaction).getSecondItem();
            userManager.removeFromInventory(user2, item2);
            userManager.removeFromInventory(user1, item1);
            userManager.addToInventory(user1, item2);
            userManager.addToInventory(user2, item1);
            itemManager.setCurrentHolder(item1, user2);
            itemManager.setCurrentHolder(item2, user1);

        } else {
            user1 = userManager.getUser(((ThreeWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((ThreeWay) transaction).getSecondTrader());
            user3 = userManager.getUser(((ThreeWay) transaction).getThirdTrader());
            item1 = ((ThreeWay) transaction).getFirstItem();
            Item item2 = ((ThreeWay) transaction).getSecondItem();
            Item item3 = ((ThreeWay) transaction).getThirdItem();
            userManager.removeFromInventory(user2, item1);
            userManager.removeFromInventory(user1, item3);
            userManager.removeFromInventory(user3, item2);
            userManager.addToInventory(user1, item1);
            userManager.addToInventory(user2, item2);
            userManager.addToInventory(user3, item3);
            itemManager.setCurrentHolder(item1, user1);
            itemManager.setCurrentHolder(item2, user2);
            itemManager.setCurrentHolder(item3, user3);
        }
        userManager.updateTradeHistory(user1, transaction);
        userManager.updateTradeHistory(user2, transaction);
        userManager.removeFromSecondAgreedUponMeetings(user1, transaction);
        userManager.removeFromSecondAgreedUponMeetings(user2, transaction);
        if (user3 != null) {
            userManager.updateTradeHistory(user3, transaction);
            userManager.removeFromSecondAgreedUponMeetings(user3, transaction);
        }
    }

    /**
     * Handles the back-end completion of a temporary Transaction. Notice the first exchange is giving Item(s) to the User(s). Only called in updateTransactionStatus.
     *
     * @param userManager The instance of UserManager.
     * @param transaction The given temporary Transaction.
     * @param itemManager The instance of ItemManager.
     */
    public void handleFirstExchange(UserManager userManager, Transaction transaction, ItemManager itemManager, CurrencyManager currencyManger) {
        User user1;
        User user2;
        User user3;
        Item item1;
        if (transaction instanceof OneWay) {
            item1 = ((OneWay) transaction).getItem();
            user1 = userManager.getUser(((OneWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((OneWay) transaction).getSecondTrader());
            user3 = null;
            userManager.removeFromInventory(user2, item1);
            userManager.addToInventory(user1, item1);
            itemManager.setCurrentHolder(item1, user1);
            user1.increaseEligibility();
            user2.decreaseEligibility();
            if (transaction instanceof OneWayMonetized) {
                currencyManger.completeSale((OneWayMonetized) transaction, userManager);
            }
        } else if (transaction instanceof TwoWay) {
            user1 = userManager.getUser(((TwoWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((TwoWay) transaction).getSecondTrader());
            user3 = null;
            item1 = ((TwoWay) transaction).getFirstItem();
            Item item2 = ((TwoWay) transaction).getSecondItem();
            userManager.removeFromInventory(user1, item1);
            userManager.removeFromInventory(user2, item2);
            userManager.addToInventory(user2, item1);
            userManager.addToInventory(user1, item2);
            itemManager.setCurrentHolder(item1, user2);
            itemManager.setCurrentHolder(item2, user1);

        } else {
            user1 = userManager.getUser(((ThreeWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((ThreeWay) transaction).getSecondTrader());
            user3 = userManager.getUser(((ThreeWay) transaction).getThirdTrader());
            item1 = ((ThreeWay) transaction).getFirstItem();
            Item item2 = ((ThreeWay) transaction).getSecondItem();
            Item item3 = ((ThreeWay) transaction).getThirdItem();
            userManager.removeFromInventory(user1, item1);
            userManager.removeFromInventory(user2, item2);
            userManager.removeFromInventory(user3, item3);
            userManager.addToInventory(user2, item1);
            userManager.addToInventory(user1, item3);
            userManager.addToInventory(user3, item2);
            itemManager.setCurrentHolder(item1, user2);
            itemManager.setCurrentHolder(item3, user1);
            itemManager.setCurrentHolder(item2, user3);
        }
        userManager.removeFromAgreedUponMeetings(user1, transaction);
        userManager.removeFromAgreedUponMeetings(user2, transaction);
        userManager.addToSecondAgreedUponMeetings(user1, transaction);
        userManager.addToSecondAgreedUponMeetings(user2, transaction);
        if (user3 != null) {
            userManager.removeFromAgreedUponMeetings(user3, transaction);
            userManager.addToSecondAgreedUponMeetings(user3, transaction);
        }

    }

    /**
     * Handles the back-end results of a permanent Transaction occurring. This is only called by updateTransactionStatus.
     *
     * @param userManager The instance of UserManager.
     * @param itemManager The instance of ItemManager.
     * @param transaction The given temporary Transaction.
     */
    public void handleCompletedPerm(UserManager userManager, ItemManager itemManager, Transaction transaction, CurrencyManager currencyManager) {
        User user1;
        User user2;
        User user3;
        Item item1;
        if (transaction instanceof OneWay) {
            item1 = ((OneWay) transaction).getItem();
            user1 = userManager.getUser(((OneWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((OneWay) transaction).getSecondTrader());
            user3 = null;
            userManager.removeFromInventory(user2, item1);
            userManager.addToInventory(user1, item1);
            itemManager.setCurrentHolder(item1, user1);
            itemManager.setOwner(item1, user1);
            user1.decreaseEligibility();
            user2.increaseEligibility();
            if (transaction instanceof OneWayMonetized) {
                currencyManager.completeSale((OneWayMonetized) transaction, userManager);
            }
        } else if (transaction instanceof TwoWay) {
            user1 = userManager.getUser(((TwoWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((TwoWay) transaction).getSecondTrader());
            user3 = null;
            item1 = ((TwoWay) transaction).getFirstItem();
            Item item2 = ((TwoWay) transaction).getSecondItem();
            userManager.removeFromInventory(user1, item1);
            userManager.removeFromInventory(user2, item2);
            userManager.addToInventory(user2, item1);
            userManager.addToInventory(user1, item2);
            itemManager.setCurrentHolder(item1, user2);
            itemManager.setCurrentHolder(item2, user1);
            itemManager.setOwner(item1, user2);
            itemManager.setOwner(item2, user1);
        } else {
            user1 = userManager.getUser(((ThreeWay) transaction).getFirstTrader());
            user2 = userManager.getUser(((ThreeWay) transaction).getSecondTrader());
            user3 = userManager.getUser(((ThreeWay) transaction).getThirdTrader());
            item1 = ((ThreeWay) transaction).getFirstItem();
            Item item2 = ((ThreeWay) transaction).getSecondItem();
            Item item3 = ((ThreeWay) transaction).getThirdItem();
            userManager.removeFromInventory(user1, item1);
            userManager.removeFromInventory(user2, item2);
            userManager.removeFromInventory(user3, item3);
            userManager.addToInventory(user2, item1);
            userManager.addToInventory(user1, item3);
            userManager.addToInventory(user3, item2);
            itemManager.setCurrentHolder(item1, user2);
            itemManager.setCurrentHolder(item2, user3);
            itemManager.setCurrentHolder(item3, user1);
            itemManager.setOwner(item1, user2);
            itemManager.setOwner(item2, user3);
            itemManager.setOwner(item3, user1);
        }
        userManager.updateTradeHistory(user1, transaction);
        userManager.updateTradeHistory(user2, transaction);
        userManager.removeFromAgreedUponMeetings(user1, transaction);
        userManager.removeFromAgreedUponMeetings(user2, transaction);
        if (user3 != null) {
            userManager.updateTradeHistory(user3, transaction);
            userManager.removeFromAgreedUponMeetings(user3, transaction);
        }
    }

    /**
     * Returns a List of all System Transactions
     *
     * @return List of Transactions
     */
    public List<Transaction> getAllTransactions() {
        return allTransactions;
    }
}
