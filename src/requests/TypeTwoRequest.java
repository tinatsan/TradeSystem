package requests;

import accounts.users.User;
import items.Item;

import java.util.Calendar;

/**
 * Constructs a TwoWay TradeRequest, that a User can send to another User if the want to initiated a Transaction. Notice this difference from a Transaction, as
 * the system does not create an instance of Transaction unless the corresponding TradeRequest has been approved by the User receiving the TradeRequest. Notice we
 * assuming noMeeting Transactions are permanent, as it's illogical to "return" and emailed item.
 */

public class TypeTwoRequest extends TradeRequest {


    private User user1;
    private User user2;
    private Item item1;
    private Item item2;

    /**
     * Constructs a TwoWay TradeRequest instance. This class is instantiated by the User that attempts to initiate the Transaction, and should be stored within the receiving User's
     * pendingRequest. If the receiving User accepts the conditions of the Transaction, they may accept the TradeRequest, and an instance of Transaction is created.
     *
     * @param item1   The User initiated the TradeRequest's Item.
     * @param item2   The Item belonging to the
     * @param message A String representation of any message user1 may want to send user2.
     * @param temp    A boolean representing if the TradeRequest will be temp or not.
     * @param date    A Calender representing the date/time the TradeRequest was sent.
     * @param virtual A boolean that determines the TradeRequest will have no meeting (true: This Transaction will not have a Meeting, false: This Transaction will have a Meeting.)
     */
    public TypeTwoRequest(Item item1, Item item2, String message, boolean temp, Calendar date, boolean virtual) {
        super(message, temp, date, virtual);
        this.user1 = item1.getOwner();
        this.user2 = item2.getOwner();
        this.item1 = item1;
        this.item2 = item2;
    }

    /**
     * Gets the User who initiates the TypeTwoRequest
     *
     * @return User that initiated the TypeTwoRequest
     */
    public User getFirstUser() {
        return this.user1;
    }

    /**
     * Gets the User who receives the TypeTwoRequest
     *
     * @return User that received TypeTwoRequest
     */
    public User getSecondUser() {
        return this.user2;
    }

    /**
     * Gets the Item that the firstUser is offering.
     *
     * @return The Item the firstUser is offering.
     */
    public Item getFirstItem() {
        return this.item1;
    }

    /**
     * Get the Item that the FirstUser wants from the secondUser.
     *
     * @return The Item the firstUser wants.
     */
    public Item getSecondItem() {
        return this.item2;
    }


    /**
     * Returns a String representation of a TypeTwoRequest, with nicely formatted attributes
     *
     * @return String representation of this TypeTwoRequest
     */
    public String toString() {
        return "Two-way trade request: '" +
                this.user1.getName() + "' offers '" + this.item1.getName() +
                "' for '" + this.user2.getName() + "''s '" + this.item2.getName() +
                "'. \nStatus: " + this.getStatus() +
                "; \nDate: " + this.dateToString() +
                "; \nIs for a temporary trade?: " + this.getTemp() +
                "; \nIs meeting in-person?: " + !this.getVirtual() +
                "; \nMessage: " + this.getMessage() + "\n";
    }

}
