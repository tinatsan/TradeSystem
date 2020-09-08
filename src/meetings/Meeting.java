package meetings;

import accounts.users.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Meeting {

    private Calendar date;
    private String place;
    private Boolean confirmed = false;
    private int edits;
    private HashMap<String, Integer> editHistory;
    private String lastEdit;
    private HashMap<String, Integer> confirm; //0 for not confirmed - 1 for confirmed that meeting took place

    /**
     * @param date  date of Meeting
     * @param place place of Meeting
     */
    public Meeting(Calendar date, String place) {
        this.date = date;
        this.place = place;
        editHistory = new HashMap<>();
        confirm = new HashMap<>();
    }

    /**
     * Checks if both users have confimred the Meeting
     *
     * @return True if both users have confirmed Meeting, false otherwise.
     */
    public boolean confirmedByBothSides() {
        //Used https://stackoverflow.com/questions/27254302/counting-duplicate-values-in-hashmap as reference
        int counter = 0;
        Integer countingFor = 1;
        for (String key : confirm.keySet()) {            // iterate through all the keys in this HashMap
            if (confirm.get(key).equals(countingFor)) {  // if a key maps to the string you need, increment the counter
                counter++;
            }
        }
        return counter == 2 || counter == 3;
    }

    public boolean confirmByThreeSides() {

        //Used https://stackoverflow.com/questions/27254302/counting-duplicate-values-in-hashmap as reference
        int counter = 0;
        Integer countingFor = 1;
        for (String key : confirm.keySet()) {            // iterate through all the keys in this HashMap
            if (confirm.get(key).equals(countingFor)) {  // if a key maps to the string you need, increment the counter
                counter++;
            }
        }
        return counter == 3;


    }

    /**
     * Set confirmation values of both users to 0
     *
     * @param side1 name of first user
     * @param side2 name of second user
     */
    public void initialconfirm(String side1, String side2) {
        confirm.put(side1, 0);
        confirm.put(side2, 0);
    }


    public void initial3confirm(String side1, String side2, String side3) {
        confirm.put(side1, 0);
        confirm.put(side2, 0);
        confirm.put(side3, 0);
    }

    /**
     * Confirms users confirmation value of a Meeting
     *
     * @param name name of user
     */
    public void meetingConfirmed(String name) {
        confirm.replace(name, 1);
    }


    /**
     * Gets users confirmation value of a Meeting
     *
     * @param name name of user
     * @return Integer representing if user confirmed Meeting
     */
    public Integer userconfirmed(String name) {
        return confirm.get(name);
    }

    /**
     * Get name of other user in Meeting
     *
     * @param name name of original user in Meeting
     * @return name of other user in Meeting
     */
    public String getOtherSide(String name) {
        for (String key : confirm.keySet()) {
            if (!key.equals(name))
                return key;
        }
        return "error";
    }

    /**
     * Puts a key-value pair in edit history hashmap
     *
     * @param name key to be put in
     * @param num  value to be put in
     */
    public void initialHistory(String name, int num) {
        editHistory.put(name, num);
    }

    /**
     * Change last user who edited Meeting
     *
     * @param name name of user who last edited Meeting
     */
    public void changeLastEdit(String name) {
        lastEdit = name;
    }

    /**
     * Get name of user who last edited Meeting
     *
     * @return Name of user who edited Meeting last
     */
    public String viewLastEdit() {
        return lastEdit;
    }

    /**
     * Edits the number edits of of a user
     *
     * @param user User who edited the meeting
     * @param i    New number of edits associated with user
     */
    public void changeHistory(User user, int i) {
        editHistory.replace(user.getName(), i);
    }

    /**
     * gets users edit history for Meeting
     *
     * @param user User who edited meeting
     * @return number of times user has edited the meeting
     */
    public int geteditHistory(String user) {
        return editHistory.get(user);
    }

    /**
     * gets date for Meeting
     *
     * @return this Meetings date
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * gets place of Meeting
     *
     * @return this Meeting place
     */
    public String getPlace() {
        return place;
    }

    /**
     * gets confirmed value of Meeting
     *
     * @return this Meetings confirmed value
     */
    public Boolean getConfirmed() {
        return confirmed;
    }

    /**
     * gets number of times meeting was edited
     *
     * @return number of meeting edits
     */
    public int getEdits() {
        return edits;
    }

    /**
     * sets date for Meeting
     *
     * @param date date to be set for Meeting
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /**
     * sets place for Meeting
     *
     * @param place place to be set for Meeting
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * sets value of confirmed to true
     */
    public void setConfirmedTrue() {
        this.confirmed = true;
    }

    /**
     * sets value of edits
     *
     * @param edits new number of edits
     */
    public void setEdits(int edits) {
        this.edits = edits;
    }

    /**
     * Returns a string representation of this Meeting.
     *
     * @return String of this Meetings date and place.
     */
    @Override
    public String toString() {

        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy hh:mma");
        return dateFormat.format(getDate().getTime()) + " at " + getPlace();
    }
}
