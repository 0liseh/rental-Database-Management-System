import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import java.io.*;


public class ManagerGUI extends GUI{
    
    private JTabbedPane tp;
    private JButton confirmStatusChange, confirmNewFee;
    private String prop[] = {"-------"}; // use for properties box populate with properties.name
    private JTextField changeFeeTo;
    private JPanel changeOfStatus, seeProps, seeLandlords, seeRenters , setFee, getPSummary;
    private Vector<Property> properties = new Vector<Property>();
    private Vector<Landlord> landlords = new Vector<Landlord>();
    private Vector<RegisteredRenter> renters = new Vector<RegisteredRenter>();
    public ManagerGUI(){
        mainFrame = new JFrame("Manager window");
        mainFrame.setSize(500, 600);
        mainFrame.add(controlPanel);
        controlPanel.setLayout(null);
        //needs to use controller to get a list of properties from the database and populat properties[]

        //controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 10, 40));
        tp = new JTabbedPane();

        changeOfStatus = new JPanel(new GridLayout(0, 1, 0, 20));
        seeProps = new JPanel();
        seeLandlords = new JPanel();
        seeRenters = new JPanel();
        setFee = new JPanel(new GridLayout(0, 1, 0, 20));
        getPSummary = new JPanel();
        initializeComboBoxes();
        setButtons();
        addObjects();
    }
    private void setButtons(){
        confirmStatusChange = new JButton("Confirm Status Change");
        confirmNewFee = new JButton("Confirm Fee Change");
       

        confirmStatusChange.setBackground(Color.WHITE);
        confirmStatusChange.setForeground(Color.GRAY);
        confirmStatusChange.setFont(normalFont);

        confirmNewFee.setBackground(Color.WHITE);
        confirmNewFee.setForeground(Color.GRAY);
        confirmNewFee.setFont(normalFont);

    }


    private void addObjects(){
        controlPanel.removeAll();
        controlPanel.revalidate();
        controlPanel.repaint();
        
        addToSeeLandlords();
        addToSeeRenters();
        addToSeeProperties();
        addToSetFee();
        addToChangeOfStatus();
        addToPSummary();


        addListeners();
        tp.add("Set new Fee" , setFee);
        tp.add("Change Status of Property", changeOfStatus);
        tp.add("Periodical Report", getPSummary);
        tp.add("Landlords" , seeLandlords);
        tp.add("Properties" , seeProps);
        tp.add("Renters" , seeRenters);
        
        mainFrame.add(tp);
        
        mainFrame.setVisible(true);

    }

    private void addListeners(){

        confirmStatusChange.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                confirmStatusChangeButtonPressed();
            }
        });

         confirmNewFee.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                confirmNewFeeButtonPressed();
            }
        });

        
    }

    private void initializeComboBoxes(){
        //need to contact contoller to get list of all properties
        propertiesBox = new JComboBox(prop);

    }

    private void confirmStatusChangeButtonPressed(){
        String status = (String)statusBox.getSelectedItem();
        System.out.println(status);

        String propSelected = propertiesBox.getSelectedItem().toString();
        System.out.println(propSelected);
        //IF CHANGING IT TO ACTIVE STATUS THEY NEED TO PAY FEE
        //should send status and property to the controller so it can update the database
    }

    private void addToChangeOfStatus(){
        changeOfStatus.setBorder(BorderFactory.createEmptyBorder(115, 40, 200, 40));

        JLabel whatProp = new JLabel("Select Property to Change Status of");
        whatProp.setForeground(Color.BLACK);
        whatProp.setFont(normalFont);
        changeOfStatus.add(whatProp);

        changeOfStatus.add(propertiesBox);

        JLabel whatState = new JLabel("Select Status to Change to");
        whatState.setForeground(Color.BLACK);
        whatState.setFont(normalFont);
        changeOfStatus.add(whatState);
        changeOfStatus.add(statusBox);

        changeOfStatus.add(confirmStatusChange);
    }

    private void addToSetFee(){
        setFee.setBorder(BorderFactory.createEmptyBorder(115, 40, 200, 40));

        String fee = "$100"; // get the fee from databse
        JLabel currentFee = new JLabel("Current fee is: " + fee);
        currentFee.setForeground(Color.BLACK);
        currentFee.setFont(normalFont);
        setFee.add(currentFee);

        JLabel newFee = new JLabel("Enter the Desired Fee Amount e.g 50");
        newFee.setForeground(Color.BLACK);
        newFee.setFont(normalFont);
        setFee.add(newFee);

        changeFeeTo = new JTextField();
        changeFeeTo.setFont(normalFont);
        setFee.add(changeFeeTo);

        setFee.add(confirmNewFee);
    }

    private void confirmNewFeeButtonPressed(){
        String newFeeText = changeFeeTo.getText().toString();
        System.out.println(newFeeText);

        //send the new fee to data base
    }

    private void addToSeeLandlords(){
        
        /*JLabel ll = new JLabel("Landlords");
        ll.setForeground(Color.BLACK);
        ll.setFont(normalFont);
        seeLandlords.add(ll);*/

        //landlords = dbController.getLandlords();
        //have to call funciton in db controller to get vectro of landlords
        seeLandlords.setLayout(new GridLayout(0, 1, 0, 20));
        JPanel slPanel = new JPanel(new GridLayout(0, 1, 0, 20));

        slPanel.setSize(500, 600);
        slPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 0, 50));

        for(int i = 0; i < landlords.size(); i++){
            String tempStr = "\nLandlord ID: " + landlords.get(i).getId() + "\n";
            tempStr = tempStr + "Name: " + landlords.get(i).getName() + "\n";
            tempStr = tempStr + "Email: " + landlords.get(i).getEmail() + "\n";
            tempStr = tempStr + "Phone Number: " + landlords.get(i).getPhoneNumber() + "\n";
            JTextArea temp = new JTextArea(tempStr);
            temp.setFont(normalFont);
            slPanel.add(temp);

        }



        JScrollPane scrollPane = new JScrollPane(slPanel);    
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        seeLandlords.add(scrollPane);

    }
    private void addToSeeProperties(){
        
        /*JLabel p = new JLabel("Properties");
        p.setForeground(Color.BLACK);
        p.setFont(normalFont);
        seeProps.add(p);*/
        seeProps.setLayout(new GridLayout(0, 1, 0, 20));
        JPanel sPanel = new JPanel(new GridLayout(0, 1, 0, 20));

        sPanel.setSize(500, 600);
        sPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 0, 50));

        for(int i = 0; i < properties.size(); i++){
            String tempStr = "\nProperty ID: " + properties.get(i).getPropertyid() + "\n";
            prop = Arrays.copyOf(prop, prop.length + 1);
            prop[prop.length -1 ] = String.valueOf(properties.get(i).getPropertyid());
            tempStr = tempStr + "Type of property: " + properties.get(i).getPropertyType() + "\n";
            tempStr = tempStr + "Number of Bed(s): " + properties.get(i).getNoOfBed() + "\n";
            tempStr = tempStr + "Number of Bath(s): " + properties.get(i).getNoOfBath() + "\n";
            if(properties.get(i).isFurnished()){
                tempStr = tempStr + "Furnished\n";
            }else{
                tempStr = tempStr + "Unfurnished\n";
            }
            tempStr = tempStr + "Location: " + properties.get(i).getArea() + "\n";
            tempStr = tempStr + "Landlord ID: " + properties.get(i).getLandlordID() + "\n";



            JTextArea temp = new JTextArea(tempStr);
            temp.setFont(normalFont);
            sPanel.add(temp);

        }

        JScrollPane scrollPane = new JScrollPane(sPanel);    
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        seeProps.add(scrollPane);

    }
    private void addToSeeRenters(){
        
       /* JLabel r = new JLabel("Renters");
        r.setForeground(Color.BLACK);
        r.setFont(normalFont);
        seeRenters.add(r);*/
        seeRenters.setLayout(new GridLayout(0, 1, 0, 20));
        JPanel srPanel = new JPanel(new GridLayout(0, 1, 0, 20));

        srPanel.setSize(500, 600);
        srPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 0, 50));

        for(int i = 0; i < renters.size(); i++){
            String tempStr = "\nRenter ID: " + renters.get(i).getId() + "\n";
            tempStr = tempStr + "Name: " + renters.get(i).getName() + "\n";
            tempStr = tempStr + "Email: " + renters.get(i).getEmail() + "\n";
            tempStr = tempStr + "Phone Number: " + renters.get(i).getPhoneNumber() + "\n";
            JTextArea temp = new JTextArea(tempStr);
            temp.setFont(normalFont);
            srPanel.add(temp);

        }


        JScrollPane scrollPane = new JScrollPane(srPanel);    
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        seeRenters.add(scrollPane);

    }

    private void addToPSummary(){
        JPanel sPanel = new JPanel(new GridLayout(0, 1, 0, 20));

        sPanel.setSize(500, 600);
        sPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 0, 100));
        JLabel PS = new JLabel("Periodical Summary");
        PS.setForeground(Color.BLACK);
        PS.setFont(normalFont);
        getPSummary.add(PS);

        JLabel houseListed = new JLabel("Total houses Listed: "); // retrieve from database
        houseListed.setForeground(Color.BLACK);
        houseListed.setFont(normalFont);
        sPanel.add(houseListed);

        JLabel houseRented = new JLabel("Total houses Rented: "); // retrieve from database
        houseRented.setForeground(Color.BLACK);
        houseRented.setFont(normalFont);
        sPanel.add(houseRented);

        JLabel activeListing= new JLabel("Total Active Listings: "); // retrieve from database
        activeListing.setForeground(Color.BLACK);
        activeListing.setFont(normalFont);
        sPanel.add(activeListing);

        JLabel houseRentedP = new JLabel("Houses Rented this period: "); // retrieve from database
        houseRentedP.setForeground(Color.BLACK);
        houseRentedP.setFont(normalFont);
        sPanel.add(houseRentedP);

        Vector<Property> currentlyRented = new Vector<Property>();

        //Call controller function for all properties with status rented

        for(int i = 0; i < currentlyRented.size(); i++){
            String tempStr = "\nProperty ID: " + currentlyRented.get(i).getPropertyid() + "\n";
            tempStr = tempStr + "Type of property: " + currentlyRented.get(i).getPropertyType() + "\n";
            tempStr = tempStr + "Number of Bed(s): " + currentlyRented.get(i).getNoOfBed() + "\n";
            tempStr = tempStr + "Number of Bath(s): " + currentlyRented.get(i).getNoOfBath() + "\n";
            if(currentlyRented.get(i).isFurnished()){
                tempStr = tempStr + "Furnished\n";
            }else{
                tempStr = tempStr + "Unfurnished\n";
            }
            tempStr = tempStr + "Location: " + currentlyRented.get(i).getArea() + "\n";
            tempStr = tempStr + "Landlord ID: " + currentlyRented.get(i).getLandlordID() + "\n";



            JTextArea temp = new JTextArea(tempStr);
            temp.setFont(normalFont);
            sPanel.add(temp);

        }

        //add properties bbelow to show house rented currently use textArea


        JScrollPane scrollPane = new JScrollPane(sPanel);    
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        getPSummary.add(scrollPane);
    }
}
