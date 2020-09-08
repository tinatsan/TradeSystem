package transactions;

import accounts.users.User;
import items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Subclass of transactions. Transaction, instantiated when a transactions. TwoWay Transaction is made (an exchange of Items).
 */

public class TwoWay extends Transaction implements Serializable {
    private User user1;
    private User user2;
    private List<Item> items;

    /**
     * Constructor of TwoWay. Requires the item of the first User, the Item of the second User, and the boolean regarding if it's temporary. True: TwoWay is temporary. False: TwoWay is permanent.
     *
     * @param item1 The Item the first User is giving to the second User.
     * @param item2 The Item the second User is giving to the first User.
     * @param temp  If the TwoWay is temporary.
     */
    public TwoWay(Item item1, Item item2, boolean temp, boolean virtual) {

        super(temp, virtual);
        this.user1 = item1.getOwner();
        this.user2 = item2.getOwner();
        this.items = new ArrayList<>();
        this.items.add(item1);
        this.items.add(item2);
    }

    /**
     * Getter for the firstTrader of a TwoWay; The User who initiates the TwoWay.
     *
     * @return The firstTrader; the User who initiates the TwoWay.
     */
    public User getFirstTrader() {
        return this.user1;
    }

    /**
     * Setter for the secondTrader of a TwoWay; the User who accepts the TwoWay.
     *
     * @return The secondTrader; the User who accepts the TwoWay.
     */
    public User getSecondTrader() {
        return this.user2;
    }

    /**
     * Getter for the Item the firstTrader is giving.
     *
     * @return The Item the firstTrader is giving.
     */
    public Item getFirstItem() {
        return this.items.get(0);
    }

    /**
     * Getter for the Item the secondTrader is giving.
     *
     * @return The Item the secondTrader is giving.
     */
    public Item getSecondItem() {
        return this.items.get(1);
    }

    /**
     * Returns a String representation of a Transaction, with nicely formatted attributes
     *
     * @return String representation of this Transaction
     */
    public String toString() {

        return "Transaction; Two-way " +
                "; \nTrader 1: " + getFirstTrader().getName() + " Item: " + getFirstItem().getName() +
                "; \nTrader 2: " + getSecondTrader().getName() + " Item: " + getSecondItem().getName() +
                "; \nStatus: " + getTradeStatus() +
                "; \nIs temporary?: " + getTemp() +
                "; \nIs in-person?: " + !getVirtual() + " \n";
    }
}
