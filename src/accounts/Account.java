package accounts;

import java.io.Serializable;

/**
 * Represent an account with name and corresponding password, Superclass for User and Admin
 */
public abstract class Account implements Serializable {
    protected String name;
    protected String password;

    /**
     * getter for an account password
     *
     * @return this account's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * getter for an account name
     *
     * @return this account's name
     */
    public String getName() {
        return name;
    }

    /**
     * setter for account name
     *
     * @param name the account's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter for account password
     *
     * @param password the password of the account
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

