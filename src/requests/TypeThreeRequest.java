package requests;

import accounts.users.User;
import items.Item;
import meetings.AvailabilityChart;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Constructs a ThreeWay TradeRequest, that a User can send to another
 * User if the want to initiated a Transaction. Notice this difference from a transactions.
 * Transaction, as the system does not create an instance of Transaction unless the corresponding requests.
 * TradeRequest has been approved by the User receiving the TradeRequest. Notice we
 * assuming noMeeting Transactions are permanent, as it's illogical to "return" and emailed item.
 */
public class TypeThreeRequest extends TradeRequest {


    private User user1;
    private User user2;
    private User user3;
    private Item item1;
    private Item item2;
    private Item item3;
    private int approved = 0;
    private User firstApproved = null;
    private AvailabilityChart user1Availability;
    private AvailabilityChart user2Availability;
    private AvailabilityChart user3Availability;
    private ArrayList<String> firstUserLocation;
    private ArrayList<String> secondUserLocation;
    private String thirdUserLocation;


    /**
     * Constructs a ThreeWay TradeRequest instance. This class is instantiated by the User that attempts to initiate the Transaction, and should be stored within the receiving User's
     * pendingRequest. If the receiving User accepts the conditions of the Transaction, they may accept the TradeRequest, and an instance of Transaction is created. Notice this is only called
     * once a User has received a twoWayRequest, and would like to add in a third User (perhaps they want the Item user1 is offering, but doesn't want to give up the Item user1 wants).
     *
     * @param item1   The Item belonging to User1, the User who requested the twoWay.
     * @param item2   The Item belonging to User2, the User requesting the threeWay.
     * @param item3   The Item belonging to User3.
     * @param message A String representation of any message user1 may want to send user2.
     * @param temp    A boolean representing if the TradeRequest will be temp or not.
     * @param date    A Calender representing the date/time the TradeRequest was sent.
     * @param virtual A boolean that determines the TradeRequest will have no meeting (true: This Transaction will not have a Meeting, false: This Transaction will have a Meeting.)
     */
    public TypeThreeRequest(Item item1, Item item2, Item item3, String message, boolean temp, Calendar date, boolean virtual) {
        super(message, temp, date, virtual);
        this.user1 = item1.getOwner();
        this.user2 = item2.getOwner();
        this.user3 = item3.getOwner();
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        user1Availability = new AvailabilityChart();
        user2Availability = new AvailabilityChart();
        user3Availability = new AvailabilityChart();
        firstUserLocation = new ArrayList<>();
        secondUserLocation = new ArrayList<>();
        thirdUserLocation = "";


    }

    /**
     * Gets a String representation of the firstUser's location. This is for ensuring participants in a Transaction
     * are within the same city.
     *
     * @return String representation of the firstUser's location.
     */
    public ArrayList<String> getFirstUserLocation() {
        return firstUserLocation;
    }

    /**
     * Sets the location of the firstUser. This is for ensuring participants in a potential Transaction are within the same city.
     *
     * @param location A String of the User's location.
     */
    public void addtoFirstUserLocation(String location) {
        firstUserLocation.add(location);
    }

    /**
     * Gets a String representation of the secondUser's location. This is for ensuring participants in a Transaction
     * are within the same city.
     *
     * @return String representation of the secondUser's location.
     */
    public ArrayList<String> getSecondUserLocation() {
        return secondUserLocation;
    }

    /**
     * Sets the location of the secondUser. This is for ensuring participants in a potential Transaction are within the same city.
     *
     * @param location A String of the User's location.
     */
    public void addtoSecondUserLocation(String location) {
        secondUserLocation.add(location);
    }

    /**
     * Gets a String representation of the thirdUser's location. This is for ensuring participants in a Transaction
     * are within the same city.
     *
     * @return String representation of the thirdUser's location.
     */
    public String getthirdUserLocation() {
        return thirdUserLocation;
    }

    /**
     * Sets the location of the thirdUser. This is for ensuring participants in a potential Transaction are within the same city.
     *
     * @param location A String of the User's location.
     */
    public void setThirdUserLocation(String location) {
        thirdUserLocation = location;
    }

    /**
     * Gets the User who initially sent the typeTwoRequest to user2
     *
     * @return User that initially sent the typeTwoRequest
     */
    public User getFirstUser() {
        return this.user1;
    }

    /**
     * Gets the User who initiated the typeThreeRequest
     *
     * @return User that initiated the typeThreeRequest
     */
    public User getSecondUser() {
        return this.user2;
    }

    /**
     * Gets the User who user2 wanted to bring in for the typeThreeRequest
     *
     * @return User that user2 wanted to bring in for the typeThreeRequest
     */
    public User getThirdUser() {
        return this.user3;
    }

    /**
     * Gets the Item that user1 would be giving to user2.
     *
     * @return Item that user1 would be giving to user2.
     */
    public Item getFirstItem() {
        return this.item1;
    }

    /**
     * Gets the Item that user2 would be giving to user3.
     *
     * @return Item that user2 would be giving to user3.
     */
    public Item getSecondItem() {
        return this.item2;
    }

    /**
     * Gets the Item that user3 would be giving to user1.
     *
     * @return Item that user2 would be giving to user1.
     */

    public Item getThirdItem() {
        return this.item3;
    }

    /**
     * Increments the approve attribute by 1 if one of the remaining two Users has confirmed
     */
    public void userApproves() {
        this.approved += 1;
    }

    /**
     * Gets the number of people that have currently approved. Notice: 0: user1 & user3 have not approved yet. 1: One of user1 or user3 has approved. 2: All three
     * Users have approved. (we are assuming user2 approves, as they sent the request)
     *
     * @return An int of the number of people that have approved this TradeRequest.
     */
    public int getApproved() {
        return approved;
    }

    /**
     * Sets the first User to approve the ThreeWayRequest (either user1 or user3)
     *
     * @param user The User who first approved the TradeRequest
     */
    public void setFirstApproved(User user) {
        this.firstApproved = user;
    }

    /**
     * Gets the first User to approve the ThreeWayRequest (either user1 or user3)
     *
     * @return The User who first approved the TradeRequest
     */
    public User getFirstApproved() {
        return this.firstApproved;
    }

    /**
     * Gets User1's availability for the next 7 days. This aids in planning the Meeting.
     *
     * @return HashMap of user1 availability for the next 7 days.
     */
    public AvailabilityChart getUser1Availability() {
        return user1Availability;
    }

    /**
     * Gets User2's availability for the next 7 days. This aids in planning the Meeting.
     *
     * @return HashMap of user2 availability for the next 7 days.
     */
    public AvailabilityChart getUser2Availability() {
        return user2Availability;
    }

    /**
     * Gets User3s availability for the next 7 days. This aids in planning the Meeting.
     *
     * @return HasMap of user3 availability for the next 7 days.
     */
    public AvailabilityChart getUser3Availability() {
        return user3Availability;
    }

    /**
     * Returns a String representation of a TypeThreeRequest, with nicely formatted attributes
     *
     * @return String representation of this TypeThreeRequest
     */
    public String toString() {
        return "Three-way trade request" +
                "; \nUser 1: " + this.getFirstUser().getName() + " Item1: " + item1.getName() +
                "; \nUser 2: " + this.getSecondUser().getName() + "Item2: " + item2.getName() +
                "; \nUser 2: " + this.getThirdUser().getName() + "Item3: " + item3.getName() +
                "; \nStatus: " + this.getStatus() +
                "; \nDate: " + this.dateToString() +
                "; \nIs for a temporary trade?: " + this.getTemp() +
                "; \nIs meeting in-person?: " + !this.getVirtual() +
                "; \nMessage: " + this.getMessage() + "\n";
    }
}
