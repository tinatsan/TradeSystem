package items;

import accounts.users.User;

import java.io.Serializable;

/**
 * represent an item, store and getter for all variables of an item.
 */

public class Item implements Serializable {
    private String name;
    private User owner;
    private User currentHolder;
    private String description;
    private String category;
    private boolean virtual; //add setter/getter
    private boolean tradable;
    private boolean sellable;
    private boolean rentable;
    private Double sellPrice;
    private Double rentPrice;
    private Integer rentDuration;

    /**
     * construct an instance of an item.
     *
     * @param name         name of this item
     * @param owner        the user who owns this item
     * @param description  description of this item
     * @param category     1.Electronics 2.Automotive and car accessories 3.Baby 4.Beauty, Health and Personal Care 5.Books
     *                     6.Home and Kitchen Supplies 7.Clothing 8.Movies, music and TV 9.Office Supplies 10.Gaming
     * @param virtual
     * @param tradable     if the owner wants to trade this item
     * @param sellable     if the owner wants to sell this item
     * @param rentable     if the owner wans to rent this item
     * @param sellPrice
     * @param rentPrice
     * @param rentDuration how long the owner wants to rent this item
     */

    public Item(String name, User owner, String description, String category, boolean virtual, boolean tradable, boolean sellable, boolean rentable,
                Double sellPrice, Double rentPrice, Integer rentDuration) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.currentHolder = null;
        this.category = category;
        this.sellPrice = sellPrice;
        this.rentPrice = rentPrice;
        this.rentable = rentable;
        this.sellable = sellable;
        this.tradable = tradable;
        this.virtual = virtual;
        this.rentDuration = rentDuration;
    }


    /**
     * getter for item name
     *
     * @return item name
     */
    public String getName() {
        return name;
    }

    /**
     * setter for item name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for item's owner
     *
     * @return the owner as User
     */
    public User getOwner() {
        return this.owner;
    }

    /**
     * Getter for the user who now is having this item( e.g who borrowed this item)
     *
     * @return the user who is now having this item but may not be the owner as User
     */
    public User getCurrentHolder() {
        return this.currentHolder;
    }

    /**
     * Setter for owner
     *
     * @param owner the user who owns this item
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * setter for the user who now is having this item( e.g who borrowed this item)
     *
     * @param holder the user who is now having this item but may not be the owner
     */
    public void setCurrentHolder(User holder) {
        this.currentHolder = holder;
    }

    /**
     * update the description to the item
     *
     * @param description
     */
    public void updateDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for this item's description
     *
     * @return this item's description as String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the category this item belongs to
     *
     * @return the category for this item as String
     */
    public String getCategory() {
        return category;
    }

    /**
     * Getter for if the item is virtual
     *
     * @return if the item is virtual as boolean
     */
    public boolean getVirtual() {
        return this.virtual;
    }

    /**
     * Change the status of tradeble for this item from
     *
     * @param tradable boolean if the owner want to trade this item or not
     */
    public void changeTradable(boolean tradable) {
        this.tradable = tradable;
    }

    /**
     * change selleble status for this item
     *
     * @param sellable boolean if the owner want to sell this item or not
     */
    public void changeSellable(boolean sellable) {
        this.sellable = sellable;
    }

    /**
     * change rentable status for this item
     *
     * @param rentable boolean if the owner want to rent this item or not
     */
    public void changeRentable(boolean rentable) {
        this.rentable = rentable;
    }

    /**
     * Setter for the sell price
     *
     * @param price sell price for this item as double
     */
    public void setSellPrice(double price) {
        this.sellPrice = price;
    }

    /**
     * Setter for the rent price
     *
     * @param price rent price for this item as double
     */
    public void setRentPrice(double price) {
        this.rentPrice = price;
    }

    /**
     * Getter for if this item is sellable
     *
     * @return if this item can be sold as boolean
     */
    public boolean getSellable() {
        return this.sellable;
    }

    /**
     * Getter for if this item is rentable
     *
     * @return if this item can be rent as boolean
     */
    public boolean getRentable() {
        return this.rentable;
    }

    /**
     * Getter for if this item is tradable
     *
     * @return if this item can be traded as boolean
     */
    public boolean getTradable() {
        return this.tradable;
    }

    /**
     * Getter for this item's selling price
     *
     * @return the selling price as double
     */
    public double getSellPrice() {
        return this.sellPrice;
    }

    /**
     * Getter for this item's rent price
     *
     * @return the rent price as double
     */
    public double getRentPrice() {
        return this.rentPrice;
    }

    /**
     * Getter for this item's duration of rent
     *
     * @return number of days this item can be rent as int
     */
    public int getRentDuration() {
        return rentDuration;
    }

    /**
     * Returns a String representation of this Item, which includes name and description
     *
     * @return String representation of this Item
     */
    public String toString() {
        return "item \"" + this.getName() + "\" with description \"" + this.getDescription() + "\"";
    }
}
