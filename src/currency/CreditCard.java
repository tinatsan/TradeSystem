package currency;

import accounts.users.User;

import java.util.Calendar;

/**
 * Constructs a CreditCard, that is used so a User can upload cash for in-app currency, so they can request a OneWayMonetized TradeRequest.
 */
public class CreditCard {

    private long cardNumber;
    private String cardName;
    private Calendar expiration;
    private User cardHolder;
    private int CVV;

    /**
     * Constructs a credit card object. The constructor requires all relevant details about the card, in order to charge them.
     *
     * @param cardNumber a long of the User's CreditCard's number
     * @param cardName   a String of the User's name on the CreditCard
     * @param expiration a Calender of the expiration date of the Card
     * @param cardHolder the User who currently holds the CreditCard
     * @param CVV        an int of the CVV of the CreditCard
     */
    public CreditCard(long cardNumber, String cardName, Calendar expiration, User cardHolder, int CVV) {
        this.cardNumber = cardNumber;
        this.cardName = cardName;
        this.expiration = expiration;
        this.cardHolder = cardHolder;
        this.CVV = CVV;
    }

    /**
     * Gets the expiration date of the card
     *
     * @return Calender of the expiration date of the card
     */

    public boolean checkExpiration() {
        Calendar today = Calendar.getInstance();
        return expiration.after(today);
    }

    /**
     * Gets the last four cardnumbers of the given CreditCard.
     *
     * @return int of the last four numbers of the CreditCard.
     */
    public String returnEndNumbers() {
        StringBuilder end = new StringBuilder();
        String all = Long.toString(this.cardNumber);

        for (int i = all.length() - 5; i < all.length(); i++) {
            end.append(all.charAt(i));

        }

        return end.toString();
    }

    /**
     * Gets the expiration date of the given CreditCard.
     *
     * @return String of the expiration date
     */
    public Calendar getExpiration() {
        return this.expiration;
    }

    /**
     * Gets the entire card number of a given CreditCard
     *
     * @return long of the card number
     */
    public Long getCardNumber() {
        return this.cardNumber;
    }

}
