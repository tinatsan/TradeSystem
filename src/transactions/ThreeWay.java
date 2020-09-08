package transactions;

import accounts.users.User;
import items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * A ThreeWay Transaction. This can only be done if the User receiving a twoWay TradeRequest would like to bring in a third User (perhaps they do not want to give
 * up their item to User1). This implies the initial twoWay is denied, and user1 and user3 must accept this new request.
 */

public class ThreeWay extends Transaction {


    private User firstTrader;

    private User secondTrader;

    private User thirdTrader;

    private List<Item> items;

    /**
     * Constructs a ThreeWay request. Requires all the Item's being traded, whether it's temporary or not, and whether it's virtual or not.
     *
     * @param item1   firstTraders's Item, going to secondTrader
     * @param item2   thirdTrader's Item, going to thirdTrader
     * @param item3   thirdTrader's item, going to firstTrader
     * @param temp    boolean whether the Transaction is temporary or not
     * @param virtual boolean whether the Transaction is virtual or not
     */
    public ThreeWay(Item item1, Item item2, Item item3, boolean temp, boolean virtual) {
        super(temp, virtual);
        this.firstTrader = item1.getOwner();
        this.secondTrader = item2.getOwner();
        this.thirdTrader = item3.getOwner();
        this.items = new ArrayList<>();
        this.items.add(item1);
        this.items.add(item2);
        this.items.add(item3);
    }

    /**
     * Gets firstTrader's from the given ThreeWay Transaction
     *
     * @return The firsTrader; the User who's initially requested the *twoWay* with the secondTrader (so the secondTrader denied the firstTrader request, and requested a threeWay).
     */
    public User getFirstTrader() {
        return this.firstTrader;
    }

    /**
     * Setter for the secondTrader of a ThreeWay; the User who accepts the ThreeWay.
     *
     * @return The secondTrader; the User who accepts the ThreeWay.
     */
    public User getSecondTrader() {
        return this.secondTrader;
    }

    /**
     * Gets user3 from the given ThreeWay Transaction.
     *
     * @return the thirdTrader; the User who was brought into the threeWay
     */
    public User getThirdTrader() {
        return this.thirdTrader;
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
     * Gets the thirdTrader's Item.
     *
     * @return Item that thirdTrader is giving to firstTrader.
     */
    public Item getThirdItem() {
        return this.items.get(2);
    }

    /**
     * Returns a String representation of a Transaction, with nicely formatted attributes
     *
     * @return String representation of this Transaction
     */
    public String toString() {

        return "Transaction; Three-way" +
                "; \nTrader 1: " + getFirstTrader().getName() + " Item: " + getFirstItem().getName() +
                "; \nTrader 2: " + getSecondTrader().getName() + " Item: " + getSecondItem().getName() +
                "; \nTrader 3: " + getThirdTrader().getName() + " Item: " + getThirdItem().getName() +
                "; \nStatus: " + getTradeStatus() +
                "; \nIs temporary?: " + getTemp() +
                "; \nIs in-person?: " + !getVirtual() + " \n";
    }
}
