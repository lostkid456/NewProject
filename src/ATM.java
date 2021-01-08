import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import java.io.*;

/**
 * The ATM management simulation we will run. Implements Serializable
 */
public class ATM implements Serializable{

    private static Bank bank=new Bank();
    private final JFrame ATMframe = new JFrame("ATM");
    private JFrame AccountFrame;
    private JLabel CardNumberLabel;
    private JTextField cardNumberText;
    private JLabel PinNum;
    private JPasswordField PinNumber;
    private JFrame WithdrawFrame;
    private JFrame DepositFrame;
    private JFrame TransactionFrame;
    private JFrame PrintFrame;
    private JTextField WText;
    private JTextField DText;



    public JFrame getTransactionFrame() {
        return TransactionFrame;
    }


    public JTextField getWText() {
        return WText;
    }


    public JTextField getDText() {
        return DText;
    }

    public JFrame getWithdrawFrame() {
        return WithdrawFrame;
    }

    public JFrame getDepositFrame() {
        return DepositFrame;
    }


    public JFrame getPrintFrame() {
        return PrintFrame;
    }

    public JPasswordField getPinNumber() {
        return PinNumber;
    }

    public JFrame getAccountFrame() {
        return AccountFrame;
    }


    public JTextField getCardNumberText() {
        return cardNumberText;
    }


    public Bank getBank() {
        return bank;
    }

    public JFrame getATMframe() {
        return ATMframe;
    }

    /**
     * Makes random users for us to manage
     *
     */
    private void SetupRandomBank() throws IOException, ClassNotFoundException {
        File f = new File("Bank.obj");
        if(!f.exists()) {
            ArrayList<User> users = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                int leftLimit = 97; // letter 'a'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 10;
                Random random = new Random();

                String generatedString = random.ints(leftLimit, rightLimit + 1)
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
                while (true) {
                    for (User user : bank.getUsers()) {
                        if (user.getName().equals(generatedString)) {
                            generatedString = random.ints(leftLimit, rightLimit + 1)
                                    .limit(targetStringLength)
                                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                    .toString();
                        }
                    }
                    break;
                }
                int card_number = (int) ((Math.random() * 10000000) + 10000000);
                while (true) {
                    for (User user : bank.getUsers()) {
                        if (user.getCard_number() == card_number) {
                            card_number =
                                    (int) ((Math.random() * 10000000) + 10000000);
                        }
                    }
                    break;
                }
                int pin = (int) (Math.random() * 10000);
                String str = "";
                if (pin < 1000) {
                    int temp = pin;
                    while (temp < 1000) {
                        temp *= 10;
                        str += 0;
                    }
                }
                String pin_num = str + Integer.toString(pin);
                int checking = (int) (Math.random() * 100000);
                int savings = (int) (Math.random() * 100000);
                User user = new User(generatedString, card_number, pin_num, checking
                        , savings);
                users.add(user);
            }
            bank.setUsers(users);
        }else{
            FileInputStream fileInputStream
                    = new FileInputStream("Bank.obj");
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(fileInputStream);
            bank = (Bank) objectInputStream.readObject();
            objectInputStream.close();
        }
    }





    /**
     * Starts up our ATM with the use of GUI
     * @param atm
     * The atm we are using
     */
    private void ATMStartup(ATM atm) {

        ATMframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ATMframe.setSize(250, 250);
        ATMframe.setLocationRelativeTo(null);
        JPanel panel=new JPanel();

        CardNumberLabel=new JLabel("Card Number");
        panel.add(CardNumberLabel);
        cardNumberText=new JTextField(20);
        panel.add(cardNumberText);

        PinNum=new JLabel("Pin");
        PinNumber=new JPasswordField(20);
        PinNumber.setEchoChar('*');
        panel.add(PinNum);
        panel.add(PinNumber);

        JButton submit=new JButton("Enter");
        panel.add(submit);
        submit.addActionListener(new ATMListener(atm));

        ATMframe.add(panel);
        ATMframe.setVisible(true);

    }



    /**
     * Used for GUI of our ATM when choosing what to do
     * @param user
     * The user we are in for the ATM
     * @param atm
     * The atm we are using
     */
    public void AccountAction(User user,ATM atm){
        ATMframe.setVisible(false);
        AccountFrame=new JFrame(user.getName()+" Frame ");
        AccountFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AccountFrame.setSize(300,300);
        AccountFrame.setLocationRelativeTo(null);
        AccountFrame.setVisible(true);
        JPanel panel= new JPanel();

        JButton WithdrawLabel=new JButton("Withdraw");
        panel.add(WithdrawLabel);
        WithdrawLabel.addActionListener(new WithdrawListener(atm,user));

        JButton DepositLabel=new JButton("Deposit");
        panel.add(DepositLabel);
        DepositLabel.addActionListener(new DepositListener(atm,user));



        JButton PrintBalances=new JButton("Print Balances");
        panel.add(PrintBalances);
        PrintBalances.addActionListener(new UserListener(atm,user));

        JButton PrintTransactions=new JButton("Print Transactions");
        panel.add(PrintTransactions);
        PrintTransactions.addActionListener(new TransactionHearer(atm,user));

        JButton Exit=new JButton("Quit");
        panel.add(Exit);
        Exit.addActionListener(new QuitListener(atm));
        AccountFrame.add(panel);

    }

    /**
     * Used for GUI when we want to withdraw money
     * @param user
     * The user that is using the atm
     * @param atm
     * The atm we are using
     */
    void Withdraw(User user, ATM atm){
        AccountFrame.setVisible(false);
        WithdrawFrame=new JFrame(user.getName()+ " Withdraw");
        WithdrawFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        WithdrawFrame.setSize(300,300);
        WithdrawFrame.setLocationRelativeTo(null);
        WithdrawFrame.setVisible(true);

        JPanel panel=new JPanel();
        JLabel WLabel=new JLabel("Amount to withdraw");
        panel.add(WLabel);
        WText=new JTextField(20);
        panel.add(WText);
        String str="W";
        WText.addActionListener(new WDListen(atm,str,user));
        WithdrawFrame.add(panel);
    }

    /**
     * Used for GUI when we want to deposit money
     * @param user
     * The user who is using the atm
     * @param atm
     * THe atm in use
     */
    void Deposit(User user, ATM atm){
        AccountFrame.setVisible(false);
        DepositFrame=new JFrame(user.getName()+ " Deposit");
        DepositFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        DepositFrame.setSize(300,300);
        DepositFrame.setLocationRelativeTo(null);
        DepositFrame.setVisible(true);

        JPanel panel=new JPanel();
        JLabel DLabel=new JLabel("Amount to deposit");
        panel.add(DLabel);
        DText=new JTextField(20);
        panel.add(DText);
        String str="D";
        DText.addActionListener(new WDListen(atm,str,user));
        DepositFrame.add(panel);
    }


    /**
     * Used for GUI when we print out transactions made
     * @param user
     * The user using the atm
     * @param atm
     * The atm in use
     */
    public void PrintTransactions(User user,ATM atm){
        AccountFrame.setVisible(false);
        JPanel panel=new JPanel();
        TransactionFrame=new JFrame(user.getName()+ " Balance");
        TransactionFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        TransactionFrame.setSize(300,300);
        TransactionFrame.setLocationRelativeTo(null);
        TransactionFrame.setVisible(true);
        JLabel Transactions=
                new JLabel("Your transactions:"+user.getTransactions());
        JButton back=new JButton("Back");
        panel.add(Transactions);
        panel.add(back);
        TransactionFrame.add(panel);
        back.addActionListener(new BackTransactionHearer(atm,user));
    }

    /**
     * Used for GUI when we want to print out balance
     * @param user
     * The user using the atm
     * @param atm
     * The atm in use
     */
    public void PrintBalance(User user,ATM atm){
        AccountFrame.setVisible(false);
        JPanel panel=new JPanel();
        PrintFrame=new JFrame(user.getName()+ " Balance");
        PrintFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        PrintFrame.setSize(300,300);
        PrintFrame.setLocationRelativeTo(null);
        PrintFrame.setVisible(true);
        JLabel Checking=new JLabel("Your balance has: "+user.getChecking_acc());
        JLabel Savings=new JLabel("Your checking has: "+user.getSavings());
        JButton Quit=new JButton("Back");
        panel.add(Checking);
        panel.add(Savings);
        panel.add(Quit);
        PrintFrame.add(panel);
        Quit.addActionListener(new BackListener(atm));
    }

    /**
     * Writes the original state of the bank into a file
     */
    static void WriteBacktoFile() throws IOException {
        BufferedWriter writer=new BufferedWriter(new FileWriter("Bank" +
                                                                        ".txt"));
        for(User user:bank.getUsers()){
            writer.write(user.toString()+"\n");
        }
        writer.close();
    }
    /**
     * Writes the updated state into another file
     */
    static void UpdateBank() throws IOException{
        BufferedWriter writer=new BufferedWriter(new FileWriter("UpdatedBank" +
                                                                        ".txt"));
        for(User user:bank.getUsers()){
            writer.write(user.toString()+"\n");
        }
        writer.close();
        FileOutputStream fileOutputStream
                = new FileOutputStream("Bank.obj");
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(bank);
    }

    /**
     * Used to simulate our ATM management simulator
     * @param args
     */
    public static void main(String... args) throws IOException, ClassNotFoundException {
        ATM atm = new ATM();
        atm.SetupRandomBank();
        WriteBacktoFile();
        atm.ATMStartup(atm);
    }

}

/**
 * Helps with login for the ATM user
 */
class ATMListener implements java.awt.event.ActionListener {

    ATM atm;

    public ATMListener(ATM atm){
        this.atm=atm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean found=false;
        int number=Integer.parseInt(atm.getCardNumberText().getText());
        char[] pin=atm.getPinNumber().getPassword();
        String pin_num=new String(pin);
        User user1=null;
        for(User user:atm.getBank().getUsers()){
            if(user.getPin_number().equals(pin_num) && user.getCard_number()==number){
                user1=user;
                JOptionPane.showMessageDialog(null,"Login Successful");
                found=true;
                atm.getPinNumber().setText("");
                break;
            }
        }
        if(!found){
            JOptionPane.showMessageDialog(null,"Incorrect information given");
            Arrays.fill(pin,'0');
        }else{
            JOptionPane.showMessageDialog(null,"Welcome "+user1.getName()+" to " +
                    "your " +
                    "account");
            atm.AccountAction(user1,atm);
        }
    }
}

/**
 * Used for when we want to withdraw
 */
class WithdrawListener implements ActionListener{
    ATM atm;
    User user;

    public WithdrawListener(ATM atm,User user){
        this.atm=atm;
        this.user=user;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        atm.Withdraw(user,atm);
    }
}

/**
 * Used for depositing
 */
class DepositListener implements ActionListener{
    ATM atm;
    User user;

    public DepositListener(ATM atm,User user){
        this.atm=atm;
        this.user=user;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        atm.Deposit(user,atm);
    }
}


/**
 * Used for printing balance
 */
class UserListener implements ActionListener{
    ATM atm;
    User user;

    public UserListener(ATM atm,User user){
        this.atm=atm;
        this.user=user;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        atm.PrintBalance(user,atm);
    }
}

/**
 * Used for quitting out of your current session
 */
class QuitListener implements ActionListener{
    ATM atm;

    public QuitListener(ATM atm){
        this.atm=atm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            ATM.UpdateBank();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        atm.getAccountFrame().setVisible(false);
        atm.getCardNumberText().setText("");
        atm.getATMframe().setVisible(true);
    }
}

/**
 * Used to go back to the main menu of your account
 */
class BackListener implements ActionListener{
    ATM atm;

    public BackListener(ATM atm){
        this.atm=atm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        atm.getAccountFrame().setVisible(true);
        atm.getPrintFrame().setVisible(false);
    }
}

/**
 * Used for performing the withdrawal/deposit requested
 */
class WDListen implements ActionListener{
    ATM atm;
    String str;
    User user;

    public WDListen(ATM atm,String str,User user){
        this.atm=atm;
        this.str=str;
        this.user=user;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(str.equals("W")){
            int val=Integer.parseInt(atm.getWText().getText());
            JOptionPane.showMessageDialog(null,user.withdraw(val));
            atm.getAccountFrame().setVisible(true);
            atm.getWithdrawFrame().setVisible(false);
            atm.getWText().setText("");
        }
        if(str.equals("D")){
            int val=Integer.parseInt(atm.getDText().getText());
            JOptionPane.showMessageDialog(null,user.deposit(val));
            atm.getAccountFrame().setVisible(true);
            atm.getDepositFrame().setVisible(false);
            atm.getDText().setText("");
        }
    }
}

/**
 * Used for when the print transaction option is selected
 */
class TransactionHearer implements ActionListener {
    ATM atm;
    User user;

    TransactionHearer(ATM atm,User user){
        this.atm=atm;
        this.user=user;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        atm.PrintTransactions(user, atm);
    }
}

/**
 * Goes back to the main menu after printing transactions
 */
class BackTransactionHearer implements ActionListener{
    ATM atm;
    User user;

    BackTransactionHearer(ATM atm,User user){
        this.atm=atm;
        this.user=user;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        atm.getAccountFrame().setVisible(true);
        atm.getTransactionFrame().setVisible(false);
    }
}




