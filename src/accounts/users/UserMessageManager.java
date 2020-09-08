package accounts.users;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Creates, keeps track of, and changes values of Users Messages to Admin.
 */

public class UserMessageManager implements Serializable {
    /**
     * Constructs the instance of UserManager with an empty list of Users
     */
    private ArrayList<ArrayList<String>> allUserMessage = new ArrayList<>();

    /**
     * Setter for the list of all user's messages to Admin
     *
     * @param userMessage to add it to the list of all user's messages.
     */
    public void addUserMessage(ArrayList<String> userMessage) {
        allUserMessage.add(userMessage);
    }

    /**
     * Getter for the list of all user's messages to Admin
     *
     * @return all user's messages as a List of String
     */
    public ArrayList<ArrayList<String>> getAllUserMessage() {
        return allUserMessage;
    }

    /**
     * Remove user message from the List of all user's messages when admin delete it
     *
     * @param usermessage to remove this usermessage from the List of all user's messages.
     */
    public void removeFromAllUserMessage(ArrayList<String> usermessage) {
        this.allUserMessage.remove(usermessage);
    }
}



