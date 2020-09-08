package system;

import accounts.admins.Admin;
import accounts.admins.AdminManager;
import accounts.users.UserManager;
import accounts.users.UserMessageManager;
import items.ItemManager;
import requests.TradeRequestManager;
import transactions.TransactionManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


// NOTE: This class is based off of StudentManager from logging in Week 6 Modules on Quercus.

/**
 * Gateway class that manages the actual saving and loading of Objects to the external file.
 */
public class ReadWrite implements Serializable {

    /**
     * Reads the file at fileName and either returns the Object stored in the file
     * or returns null.
     *
     * @param fileName the path of the data file
     * @throws FileNotFoundException if filePath is not a valid path
     */
    public Object readFromFile(String fileName) throws ClassNotFoundException, IOException {
        File findFile = new File(fileName);
        String filepath = findFile.getAbsolutePath();
        File theFile = new File(filepath);
        if (!theFile.exists()) {
            theFile.createNewFile();
        }
        try {
            InputStream file = new FileInputStream(filepath);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            // deserialize the list in the file by reading
            Object temp = input.readObject();
            //closes the file
            input.close();
            // as long as the file is not empty, it will populate admins with the list stored in the file
            if (temp != null) {
                return temp;
            }
        } catch (
                IOException ignored) {
        }
        return null;
    }

    /**
     * Deals with reading from the external file fileName and if it is not empty, it will return the AdminManager
     * object stored in fileName; if it is empty, it will create an initial Admin and return a new AdminManager
     *
     * @param fileName the name of the file the method is reading from
     * @return returns an AdminManager object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public AdminManager adminPopulate(String fileName) throws IOException, ClassNotFoundException {
        AdminManager allAdmins = (AdminManager) readFromFile(fileName);
        if (allAdmins == null) {
            Admin initialAdmin = new Admin("Tina", "123456");
            return new AdminManager(initialAdmin);
        }
        return allAdmins;
    }

    /**
     * Deals with reading from the external file fileName and if it is not empty, it will populate the list
     * of Users in UserManager um with the list of saved Users from the file. It will not do anything if it is empty.
     *
     * @param fileName the name of the file the method is reading from
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void userPopulate(UserManager um, String fileName) throws IOException, ClassNotFoundException {
        UserManager allUsers = (UserManager) readFromFile(fileName);
        if (!(allUsers == null)) {
            um.setAllUsers(allUsers.getAllUsers());
        }
    }

    /**
     * Deals with reading from the external file fileName and if it is not empty, it will populate the list
     * of items in ItemManager im with the list of saved items from the file. It will not do anything if it is empty.
     *
     * @param fileName the name of the file the method is reading from
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void itemPopulate(ItemManager im, String fileName) throws IOException, ClassNotFoundException {
        ItemManager allItems = (ItemManager) readFromFile(fileName);
        if (!(allItems == null)) {
            im.setSystemInventory(allItems.getSystemInventory());
        }
    }

    /**
     * Deals with reading from the external file fileName and if it is not empty, it will return the TransactionManager
     * object stored in fileName; if it is empty, it will return a new TransactionManager.
     *
     * @param fileName the name of the file the method is reading from
     * @return returns an TransactionManager object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public TransactionManager transactionManagerPopulate(String fileName) throws IOException, ClassNotFoundException {
        TransactionManager tM = (TransactionManager) readFromFile(fileName);
        if (tM == null) {
            return new TransactionManager();
        }
        return tM;
    }

    /**
     * Deals with reading from the external file fileName and if it is not empty, it will return the TradeRequestManager
     * object stored in fileName; if it is empty, it will return a new TradeRequestManager.
     *
     * @param fileName the name of the file the method is reading from
     * @return returns an TradeRequestManager object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public TradeRequestManager tradeRequestPopulate(String fileName) throws IOException, ClassNotFoundException {
        TradeRequestManager tRM = (TradeRequestManager) readFromFile(fileName);
        if (tRM == null) {
            return new TradeRequestManager();
        }
        return tRM;
    }

    /**
     * Deals with reading from the external file fileName and if it is not empty, it will return the UserMessageManager
     * object stored in fileName; if it is empty, it will return a new UserMessageManager.
     *
     * @param fileName the name of the file the method is reading from
     * @return returns an UserMessageManager object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public UserMessageManager userMessagePopulate(String fileName) throws IOException, ClassNotFoundException {
        UserMessageManager uMM = (UserMessageManager) readFromFile(fileName);
        if (uMM == null) {
            return new UserMessageManager();
        }
        return uMM;
    }

    /**
     * Reads the log file and returns a List of log messages
     *
     * @param fileName the name of the Log file
     * @return List of Strings where each String is a log
     * @throws IOException throws exception if File doesn't exist
     */
    public List<String> readLog(String fileName) throws IOException {
        //The following code is from https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
        String data = new String(Files.readAllBytes(Paths.get(fileName)));
        return helperLogSplit(data);

    }

    /**
     * Helper method for readLog()
     * Formats the logs into a List containing a String. Each string is formatted to contain: "(Date) (Time): (Log Message)"
     *
     * @param logs String containing the entire log
     * @return List of Strings where each String is a log
     */
    public List<String> helperLogSplit(String logs) {
        List<String> listLogs = new ArrayList<>();
        String[] individualLogs = logs.split("</record>");

        for (String a : individualLogs) {
            String[] findMsg = a.split("<message>|</message>");
            if (findMsg.length > 1) {
                String[] findDate = findMsg[0].split("<date>|</date>");
                String[] dateTime = findDate[1].split("T");
                listLogs.add(dateTime[0] + " " + dateTime[1] + ": " + findMsg[1]);
            }
        }
        return listLogs;
    }

    /**
     * Writes Object object to external file at filePath.
     *
     * @param object   the Object that is instantiated in the current System that needs to be saved.
     * @param fileName the file to write the records to
     * @throws IOException
     */
    public void saveToFile(String fileName, Object object) throws IOException {
        File findFile = new File(fileName);
        String filePath = findFile.getAbsolutePath();
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize Object object
        output.writeObject(object);
        output.close();
    }
}

