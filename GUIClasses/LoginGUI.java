import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import java.io.*;


public class LoginGUI extends GUI{


    //sets variables for to be used
    private JTextField un, pw;
    private JButton login , guest, register;
    private JLabel email , password;
    private DatabaseController dbController;
    String type = "yolo";
    public int id;


    //intializes the control panels and calls functions that will set up textFields, and Buttons
    public LoginGUI(){
        controlPanel.setLayout(new GridLayout(0 , 1 ,0 , 20));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 10, 40));
        dbController = new DatabaseController();
        setTextFields();
        setButtons();
        addObjects();
    }

    //adds all the buttons, textfields and labels to the panel
    public void addObjects()
    {
        controlPanel.removeAll();
        controlPanel.revalidate();
        controlPanel.repaint();
        controlPanel.add(headerLabel);
        controlPanel.add(email);
        controlPanel.add(un);
        controlPanel.add(password);
        controlPanel.add(pw);
        controlPanel.add(login);
        controlPanel.add(register);
        controlPanel.add(guest);
        mainFrame.setVisible(true);
    }


    //initializes textFields for the panel 
    //sets what each will look like
    private void setTextFields(){
        un = new JTextField();
        pw = new JTextField();


        un.setFont(normalFont);
        pw.setFont(normalFont);

        headerLabel = new JLabel("Login to Property Tracker!", JLabel.CENTER);
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setFont(new Font("Courier", Font.PLAIN, 25));

        email =  new JLabel("Email:");
        email.setForeground(Color.BLACK);
        email.setFont(normalFont);

        password = new JLabel("Password:");
        password.setForeground(Color.BLACK);
        password.setFont(normalFont);
    }

    //initializes buttons for panel setting what each will look like
    //also adds event listeners for each button
    private void setButtons(){

        login = new JButton("Login");
        guest = new JButton("Guest");
        register = new JButton("Register");


        login.setBackground(Color.GRAY);
        login.setForeground(Color.WHITE);
        login.setFont(normalFont);

        login.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                loginButtonPressed();
            }
        });

        guest.setBackground(Color.GRAY);
        guest.setForeground(Color.WHITE);
        guest.setFont(normalFont);

        guest.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                guestButtonPressed();
            }
        });

        register.setBackground(Color.GRAY);
        register.setForeground(Color.WHITE);
        register.setFont(normalFont);

        register.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                registerButtonPressed();
            }
        });

    }
    
    //will set type of user to unrigestered user
    private void guestButtonPressed()
    {
        type = "Unregistered Renter";

        // make it login as unregistered user
    }

    //sets type to register so GUI knows to poull up registerGUI
    private void registerButtonPressed()
    {
        type = "Register";
        // make it go to the register page
    }
    
    //if login is pressed then it will send the data from the username and password fields to the dbcontroller so it can authenticate the login
    //if there is a problem loging in it will send error otherwise it sets type to the user it logged into
    private void loginButtonPressed(){
        String user = un.getText().toString();
        System.out.println(user);

        String pass = pw.getText().toString();
        System.out.println(pass);

        if(dbController.checkUser(user, pass, "Manager") != -1){
            System.out.println("Manager");
            type = "Manager";
        }else if(dbController.checkUser(user, pass, "Landlord") != -1){
            id = dbController.checkUser(user, pass, "Landlord");
            System.out.println("landlord");
            type = "Landlord";
        }else if(dbController.checkUser(user, pass, "Renter") != -1){
            id = dbController.checkUser(user, pass, "Renter");
            System.out.println("Renter");
            type = "Registered Renter";
        }else{
            JOptionPane.showMessageDialog(mainFrame, "Incorrect Name or Password, Please try again");
            System.out.println("Didn't work");
        }
        //send these values to check database and see if it's a user and what type of user it is
    }

    //returns type to the main function running this
    public String gettype(){
        return type;
    }

}
