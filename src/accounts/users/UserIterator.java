package accounts.users;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Allow user to add location(optional)
 */

public class UserIterator implements Iterator<String> {
    private List<String> properties = new ArrayList<>(); //username and password
    private int current = 0;
    public boolean usergot;

    /**
     * constructor for UserIterator
     */
    public UserIterator() {
        properties.add("UserName: ");
        properties.add("Password: ");
        properties.add("Location (Optional- enter '1' if you don't wish to add location): ");
    }

    @Override
    public boolean hasNext() {
        return current < properties.size();
    }

    @Override
    public String next() {
        String res;

        // List.get(i) throws an IndexOutBoundsException if
        // we call it with i >= properties.size().
        // But Iterator's next() needs to throw a
        // NoSuchElementException if there are no more elements.
        try {
            res = properties.get(current);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
        current += 1;
        return res;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }


}
