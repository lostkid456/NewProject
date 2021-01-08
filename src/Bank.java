import java.io.Serializable;
import java.util.*;

/**
 * The bank containing all accounts
 */
public class Bank implements Serializable {

    private List<User>users=new ArrayList<>();

    /**
     * Gets the list of users
     * @return
     * List of users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Sets the list of users to another list
     * @param users
     * The list we want it changed to
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }


    /**
     * String representation of all users in the bank
     * @return
     * The string representation
     */
    @Override
    public String toString() {
        String str=String.format("%7s%18s%11s%15s%10s","Name","Card_Number ",
                                 "Pin_Number "
                ,"Checking_Account ","Savings_Account \n");
        for(User user:users){
            str+= user.toString()+"\n";
        }
        return str;
    }
}
