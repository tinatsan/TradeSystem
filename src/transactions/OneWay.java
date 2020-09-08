package transactions;

import accounts.users.User;
import items.Item;

import java.io.Serializable;

/**
 * Subclass of Transaction, instantiated when a OnwWay Transaction is made (an attempt to strictly borrow/lend).
 */
public class OneWay extends Transaction implements Serializable {

    private final User user1;

    private final User user2;

    private final Item item;

    /**
     * Constructor for OneWay. Requires a borrower of type User, an Item for the User to attain, and a boolean temp which specifies if OneWay is temporary.
     *
     * @param user1 The User that intends to borrow an item from another User.
     * @param item  The Item the borrower intends to attain.
     * @param temp  If the OneWay is temporary or not.
     */
    public OneWay(User user1, Item item, boolean temp, boolean virtual) {
        super(temp, virtual);
        this.user1 = user1;
        this.user2 = item.getOwner();
        this.item = item;
    }

    /**
     * Getter for the borrower of the OneWay.
     *
     * @return The borrower of a given OneWay.
     */
    public User getFirstTrader() {
        return this.user1;
    }

    /**
     * Getter for the lender, or the User giving an Item to another User.
     *
     * @return The lender of a given OneWay.
     */
    public User getSecondTrader() {
        return this.user2;
    }

    /**
     * Setter for the lender, or the User giving an Item to another User.
     *
     * @return The lender of a given OneWay.
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * Returns a String representation of a Transaction, with nicely formatted attributes
     *
     * @return String representation of this Transaction
     */
    public String toString() {

        return "Transaction; One-Way" +
                "; \nTrader 1: " + getFirstTrader().getName() +
                "; \nTrader 2: " + getSecondTrader().getName() + " Item: " + getItem().getName() +
                "; \nStatus: " + getTradeStatus() +
                "; \nIs temporary?: " + getTemp() +
                "; \nIs in-person?: " + !getVirtual() + " \n";
    }
}