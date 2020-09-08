package transactions;

import accounts.users.User;
import items.Item;

/**
 * Constructs a OneWayMonetized TradeRequest, that a User can send to another
 * User if they want to initiate a Transaction. Notice this difference from a transactions.
 * Transaction, as the system does not create an instance of Transaction unless the corresponding
 * TradeRequest has been approved by the User receiving the TradeRequest. Also
 * Notice that this type of Transaction will involve in-app currency, which distinguishes it from a
 * OneWay. Notice we are assuming noMeeting Transactions are permanent, as it's illogical to "return"
 * and emailed item.
 */
public class OneWayMonetized extends OneWay {


    private final double cost;
    private boolean person1EmailSent;
    private boolean person2EmailSent;
    private String email;


    /**
     * Constructs a OneWayMonetized object. This is the subclass of OneWayRequest, as it contains all attributes,
     * yet has an additional cost attribute, depending of if the Transaction is permanent or temporary. If the Transaction will
     * be temporary, then there is a rentDuration attribute that represents the amount of time the owner would like to rent out the Item.
     *
     * @param user1   The User who initiated the TradeRequest
     * @param item    The Item User1 currently wants
     * @param temp    A boolean representing if the TradeRequest will be temp or not.
     * @param virtual A boolean that determines the TradeRequest will have no meeting (true: This transactions.
     *                Transaction will not have a Meeting, false: This Transaction will have a Meeting.)
     */
    public OneWayMonetized(User user1, Item item, boolean temp, boolean virtual, String email) {
        super(user1, item, temp, virtual);
        person1EmailSent = false;
        person2EmailSent = false;
        this.email = email;
        if (this.getTemp()) {
            this.cost = item.getRentPrice();
        } else {
            this.cost = item.getSellPrice();
        }
    }

    /**
     * Gets the email of firstTrader
     *
     * @return String of firstTrader's email
     */
    public String getEmail() {
        return email;

    }

    /**
     * Sets that the firstTrader has confirmed that the email was sent
     */
    public void Person1Confirmed() {
        person1EmailSent = true;
    }

    /**
     * Gets if the firstTrader has confirmed that the email was sent
     *
     * @return boolean if the email was sent or not
     */
    public boolean getPerson1Confirmed() {
        return person1EmailSent;
    }

    /**
     * Sets that the secondTrader has confirmed that the email was sent
     */
    public void Person2Confirmed() {
        person2EmailSent = true;
    }

    /**
     * Gets if the secondTrader has confirmed that the email was sent
     *
     * @return boolean if the email was sent of not
     */
    public boolean getPerson2Confirmed() {
        return person2EmailSent;
    }

    /**
     * Gets the cost of this Item. Note: If the TradeRequest is temporary, the cost will represent the cost to rent. If the TradeRequest
     * is permanent, it will represent the cost to buy.
     *
     * @return A double of the current cost of the item.
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Sets the duration for which the owner of the Item will allow the receiver of the Item to borrow it for. Notice this is only
     * initiated if this TradeRequest is temporary.
     *
     * @param days Integer of the number of days the owner of the Item will allow the receiver of the Item to borrow it for.
     */

    public String toString() {

        return "Transaction; Monetized One-way" +
                "; \nTrader 1: " + getFirstTrader().getName() +
                "; \nTrader 2: " + getSecondTrader().getName() + " Item: " + getItem().getName() +
                "; \nStatus: " + getTradeStatus() +
                "; \nIs temporary?: " + getTemp() +
                "; \nIs in-person?: " + !getVirtual() + " \n";
    }
}
