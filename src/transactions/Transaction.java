package transactions;

import meetings.Meeting;

/**
 * The super class for all types of Transactions. Should never be instantiated.
 */

public class Transaction {


    private int tradeStatus = 0; // 0: In progress. 1: Finalized Meeting (the initialMeeting has been set) 2: Pending Second Exchange (only for temporary Transaction). 3: Completed 4: Cancelled.

    private boolean temp; //Set true is this Transaction in temporary (must revert in a month).

    private Meeting initialMeeting;

    private Meeting returnMeeting;

    private boolean virtual;


    /**
     * Transaction constructor, requires boolean: True if transaction is temporary, False if permanent.
     *
     * @param temp If this transaction is temporary
     */
    public Transaction(boolean temp, boolean virtual) {
        this.temp = temp;
        this.virtual = virtual;
        this.initialMeeting = null;
        this.returnMeeting = null;
    }

    /**
     * Gets if the Transaction is virtual or not
     *
     * @return boolean if the Transaction is virtual or not
     */
    public boolean getVirtual() {
        return virtual;
    }

    /**
     * Getter for a given Transaction's tradeStatus. 0: In progress. 1: Finalized Meeting (the initialMeeting has been set) 2: Pending Second Exchange (only for temporary Transaction). 3: Completed 4: Cancelled.
     *
     * @return The trade status of a Transaction
     */
    public int getTradeStatus() {
        return this.tradeStatus;
    }

    /**
     * Changes a Transaction's trade status: 0: In progress. 1: Finalized Meeting (the initialMeeting has been set) 2: Pending Second Exchange (only for temporary Transaction). 3: Completed 4: Cancelled.
     *
     * @param tradeStatus The trade status of a Transaction
     */
    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    /**
     * Getter for whether a Transaction is temporary or not. True: Transaction is temporary. False: Transaction is permanent
     *
     * @return If a Transaction is temporary
     */

    public boolean getTemp() {
        return this.temp;
    }

    /**
     * Setter for whether a Transaction is temporary or not. True: Transaction is temporary. False: Transaction is permanent
     *
     * @param temp If a Transaction is temporary
     */
    public void setTemp(boolean temp) {

        this.temp = temp;
    }

    /**
     * Getter for the InitialMeeting of the Transaction. InitialMeeting will contain the time, date and place of exchanging items.
     *
     * @return The InitialMeeting of a given Transaction
     */
    public Meeting getInitialMeeting() {
        return this.initialMeeting;
    }

    /**
     * Setter of the initialMeeting of a Transaction. InitialMeeting will contain the time, date and palce of exchanging items.
     *
     * @param meeting The InitialMeeting of a given Transaction
     */
    public void setInitialMeeting(Meeting meeting) {
        this.initialMeeting = meeting;
    }

    /**
     * Getter of the returnMeeting of a Transaction. A Transaction should only have a returnMeeting if the Transaction is temporary. Otherwise, set to Null.
     *
     * @return The returnMeeting of a given Transaction
     */
    public Meeting getReturnMeeting() {
        return this.returnMeeting;
    }

    /**
     * Setter of the returnMeeting of a Transaction. A Transaction should only have a returnMeeting if the Transaction is temporary. Otherwise, set to Null.
     *
     * @param meeting The returnMeeting of a given Transaction
     */
    public void setReturnMeeting(Meeting meeting) {
        this.returnMeeting = meeting;
    }

}
