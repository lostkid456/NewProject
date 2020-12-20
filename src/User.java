import java.util.*;

/**
 * This class is for creating a User for our mock ATM system
 */
public class User {
    private String name;
    private int card_number;
    private String pin_number;
    private int checking_acc;
    private int savings;
    private Queue<String> transactionQueue=new LinkedList<>();

    /**
     * A user with the certain paramters
     * @param name Name of user
     * @param card_number Card Number for the user
     * @param pin_number Pin Number to access their account
     * @param checking_acc Amount in their checking account
     * @param savings Amount in their savings account
     */
    public User(String name,int card_number,String pin_number,int checking_acc,
                int savings){
        this.name=name;
        this.card_number=card_number;
        this.pin_number=pin_number;
        this.checking_acc=checking_acc;
        this.savings=savings;
    }


    public String getName() {
        return name;
    }


    public int getCard_number() {
        return card_number;
    }


    public int getChecking_acc() {
        return checking_acc;
    }


    public String getPin_number() {
        return pin_number;
    }


    public int getSavings() {
        return savings;
    }


    public void setChecking_acc(int checking_acc) {
        this.checking_acc = checking_acc;
    }


    /**
     * Make a withdrawal from your checking account
     * @param value
     * The amount we want to withdraw
     * @return
     * A message of whether we were able to withdraw or not
     */
    public String withdraw(int value){
        if(value>checking_acc){
            return "Unable to withdraw that much money";
        }else{
            setChecking_acc(checking_acc-value);
            transactionQueue.add("-"+value);
        }
        return "Successfully withdrew "+value+ " from your checking account";
    }

    /**
     * Make a deposit to your checking account
     * @param value
     * The amount we want to deposit
     * @return
     * A message saying we successfully deposited the money
     */
    public String deposit(int value){
        setChecking_acc(checking_acc+value);
        transactionQueue.add("+"+value);
        return "Successfully deposited "+value+" from to your checking account";
    }

    /**
     * Return all of the transactions made in the account
     * @return
     * A readable String representation of the transactions
     */
    public String getTransactions(){
        String str="";
        for(String s:transactionQueue){
            str+=s+",";
        }
        return str;
    }

    /**
     * A readable representation of the User's information
     * @return
     * String representation of the User's information
     */
    @Override
    public String toString() {
        String str=String.format("%5s%12d%10s%15d%15d",name,card_number,
                                 pin_number,
                                 checking_acc,savings);
        return str;
    }
}
