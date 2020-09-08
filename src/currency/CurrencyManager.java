package currency;

import accounts.users.User;
import accounts.users.UserManager;
import transactions.OneWayMonetized;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that manages the in-app currency of a User.
 */
public class CurrencyManager {


    private Map<OneWayMonetized, Double> inProgressSale;

    /**
     * Always instantiated when the main method is initially called. Creates a HashMap that stores all the sales in progress, where the key
     * is the OneWayMonetized TradeRequest, and value is the Double.
     */
    public CurrencyManager() {
        this.inProgressSale = new HashMap<>();
    }

    /**
     * Adds in-app currency to the User. It requires that the CreditCard is not expired. It is assumed that the User always has enough to upload.
     *
     * @param user   The User that is uploading in-app currency
     * @param card   The associated CreditCard
     * @param amount The amount the User would like to upload
     * @return a boolean. True: The funds were successfully added. False: The card is expired)
     */
    public boolean addFunds(User user, CreditCard card, double amount) {
        if (card.checkExpiration()) {
            user.updateCapital(amount);
            return true;
        }
        return false;
    }

    /**
     * Takes the appropriate amount of in-app currency from user1, and stores it. When the Transaction has been confirmed, the amount will be deposited into
     * user2's account. If the Transaction has been cancelled, the currency will be returned to user1.
     *
     * @param trade       The given OneWayMonetized request.
     * @param userManager The instance of UserManager, to call getUser()
     */
    public void holdFunds(OneWayMonetized trade, UserManager userManager) {
        this.inProgressSale.put(trade, trade.getCost());
        User user1 = userManager.getUser(trade.getFirstTrader());
        user1.updateCapital(-trade.getCost());
    }

    /**
     * Returns the "held" currency into user1's account.
     *
     * @param trade       The given OneWayMonetized request.
     * @param userManager The instance of UserManager, to call getUser()
     */
    public void reverseHold(OneWayMonetized trade, UserManager userManager) {
        User user1 = userManager.getUser(trade.getFirstTrader());
        user1.updateCapital(trade.getCost());
        this.inProgressSale.remove(trade);
    }

    /**
     * Deposits the "held" funds into user2's account
     *
     * @param trade       The given OneWayMonetized request.
     * @param userManager The instance of UserManager, to call getUser()
     */
    public void completeSale(OneWayMonetized trade, UserManager userManager) {
        User user2 = userManager.getUser(trade.getSecondTrader());
        user2.updateCapital(trade.getCost());
        this.inProgressSale.remove(trade);

    }

    /**
     * Gets the current date, with setting the day to the 30th (as only month and year are on credit card's)
     *
     * @param date A string of the date dd-mm-yyyy
     * @return A Calender representation of the current date.
     */
    public Calendar getDate(String date) {
        Calendar cal = Calendar.getInstance();
        String[] temp = date.split("-");
        cal.set(Integer.parseInt(temp[1]), Integer.parseInt(temp[0]), 1);
        return cal;
    }

    /**
     * Gets the date in SimpleDateFormat
     *
     * @param date Calender object representing the date
     * @return SimpleDateFormat of the date.
     */
    public String dateString(Calendar date) {

        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        return dateFormat.format(date.getTime());
    }

    public boolean checkExpiration(Calendar date) {
        Calendar today = Calendar.getInstance();
        return date.after(today);
    }
}
