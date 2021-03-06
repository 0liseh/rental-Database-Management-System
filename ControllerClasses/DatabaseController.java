//uses static method getConnection(database URL, username, password)


import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.lang.*;

public class DatabaseController{
  
	private Vector<Property> properties = new Vector<Property>();
	private Vector<Landlord> landlords = new Vector<Landlord>();
	private Vector<RegisteredRenter> renters = new Vector<RegisteredRenter>();
	private Vector<Property> dud = new Vector<Property>();
	
	private static final String url = "jdbc:mysql://localhost:3306/propertymanagement";
	private static final String username = "root";
	private static final String password = "kaboomy";
	
	public static Connection mysql_con;
	private Statement stmt; //object of type statement from JDBC class that enables the creation "Query statements"
	private ResultSet rs;//object of type ResultSet from the JDBC class that stores the result of the query
	
	//Database Controller constructor
	public DatabaseController() {
		try {
            //Register JDBC driver
			Driver driver = new com.mysql.cj.jdbc.Driver();
			DriverManager.registerDriver(driver);
            //Open a connection
			mysql_con = DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	//Gets the list of users 
	public void getUsers() {
		try {
			//Class.forName("com.mysql.cj.jdbc.Driver");
			stmt = mysql_con.createStatement();  
			rs = stmt.executeQuery("select * from user");  
			
			while(rs.next())  
				System.out.println(rs.getString("username")+"\t\t\t\t\t"+rs.getInt("id")+"\t\t\t"+rs.getInt("password1"));
		}
		catch(Exception e){ 
			System.out.println(e);
		}  
	}

	//Gets the list of all the properties
	public Vector<Property> getProperties(){
		try {
			stmt = mysql_con.createStatement();  
			rs = stmt.executeQuery("select * from property");  
			
			while(rs.next()) {
				int propID = rs.getInt("propertyId");
				String propType = rs.getString("propertyType");
				int numOfBed = rs.getInt("numberOfBed");
				int numOfBath = rs.getInt("numberOfBath");
				boolean furn = rs.getBoolean("furnished");
				String area = rs.getString("area");
				String status = rs.getString("status1");
				int llID = rs.getInt("landlordID");
				
				Property temp = new Property(propID, propType, numOfBed, numOfBath, furn, area, status, llID);
				properties.add(temp);
			}
			
		}
		catch(Exception e){ 
			System.out.println(e);
		}  
		return properties;
	}

	//Gets the properties based on the landlord ID
	public Vector<Integer> getMyProperties(int lID){
		Vector<Integer> landlordProperties = new Vector<Integer>();
		try {
			stmt = mysql_con.createStatement();  
			rs = stmt.executeQuery("select * from property");  
			while(rs.next()) {
				System.out.println("In getMyproperties while loop");
				if(lID == rs.getInt("landlordID")){
					//System.out.println("Found landlord properties");
					int propID = rs.getInt("propertyId");
					landlordProperties.add(propID);
				}
			}
		}
		catch(Exception e){ 
			System.out.println(e);
		}  
		return landlordProperties;
	}

	//Gets the properties which are not active based on the landlord ID
	public Vector<Integer> getNRProperties(int lID){
		Vector<Integer> llProperties = new Vector<Integer>();
		try {
			stmt = mysql_con.createStatement();  
			rs = stmt.executeQuery("select * from property");  
			while(rs.next()) {
				if(lID == rs.getInt("landlordID") && !"active".equals(rs.getString("status1"))){
					
					//System.out.println("Found landlord properties");
					System.out.println("This are the prop not active: "+rs.getInt("propertyID"));
					int propID = rs.getInt("propertyId");
					llProperties.add(propID);
				}
			}
		}
		catch(Exception e){ 
			System.out.println(e);
		}  
		return llProperties;
	}

	//sends a list of landlords
	public Vector<Landlord> getLandlords(){
		try {
			String type = "Landlord";
			stmt = mysql_con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery("select * from user");
			landlords.clear(); 
			while(rs.next()) {
				//System.out.println("In getLandlords while loop");
				if(type.equals(rs.getString("type1"))){
					Landlord temp = new Landlord(rs.getString("username"), rs.getInt("id"), rs.getString("email"), rs.getString("phoneNumber"), rs.getString("password1"), dud);//getMyProperties(rs.getInt("id")));
					landlords.add(temp);
				}
			}
			//System.out.println("Outside getLandlords while loop");
		} catch (SQLException ex) {
            ex.printStackTrace();
        }
		return landlords;
	}
	//verify the login information is correct for the correct type
	public int checkUser(String email, String password, String type) {
		//System.out.println("System is in checkUser");
		try {
			stmt = mysql_con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery("select * from user");
			//renters.clear(); 
			while(rs.next()) {
				//System.out.println("System is checking for user in database");
				if(type.equalsIgnoreCase(rs.getString("type1")) && email.equals(rs.getString("email")) && password.equals(rs.getString("password1"))){
					return rs.getInt("id");
				}
			}
		} catch (SQLException ex) {
            ex.printStackTrace();
        }
		System.out.println("System is done with checkUser");
		return -1;
	}
	//get vector of all registered renters
	public Vector<RegisteredRenter> getRegisteredRenters(){
		try {
			String type = "Renter";
			stmt = mysql_con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery("select * from user");
			renters.clear(); 
			while(rs.next()) {
				if(type.equals(rs.getString("type1"))){
					RegisteredRenter temp;
					temp = new RegisteredRenter(rs.getString("username"), rs.getInt("id"), rs.getString("email"), rs.getString("phoneNumber"), rs.getString("password1"), 0); //getNotification(rs.getInt("id")));
					renters.add(temp);
				}
			}
		} catch (SQLException ex) {
            ex.printStackTrace();
        }
		return renters;
	}
	// get notifications for specified landlord
	public int getNotification(int id){
		try {
			stmt = mysql_con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery("SELECT * FROM NOTIFICATION");
			while(rs.next()) {
				if(id == rs.getInt("id")){
					return rs.getInt("notification");
				}
        }
		} catch (SQLException ex) {
            ex.printStackTrace();
        }
		System.out.println("Registered Renter was not found");
		return -1;
	}
	
	//get all properties that used to have a specific status
	public Vector<Property> getStatusProperties(String stat){
		Vector<Property> prop = new Vector<Property>();
		try {
			Calendar c1 = Calendar.getInstance();	
			long time = c1.getTimeInMillis() - 2592000000L;
			stmt = mysql_con.createStatement();  
			rs = stmt.executeQuery("select * from property");  
			while(rs.next()) {
				if(stat.equals("rented")) {
					if(rs.getString("dateRented") != null){
						if(time < rs.getLong("dateRented")) {
						Property temp;
						int propID = rs.getInt("propertyId");
						String propType = rs.getString("propertyType");
						int numOfBed = rs.getInt("numberOfBed");
						int numOfBath = rs.getInt("numberOfBath");
						boolean furn = rs.getBoolean("furnished");
						String area = rs.getString("area");
						String status = rs.getString("status1");
						int llID = rs.getInt("landlordID");
						
						temp = new Property(propID, propType, numOfBed, numOfBath, furn, area, status, llID);
						prop.add(temp);
						}
					}
				}
				else if(stat.equals("active")) {
					if(rs.getString("datePosted") != null){
						if(time < rs.getLong("datePosted")) {
							Property temp;
							int propID = rs.getInt("propertyId");
							String propType = rs.getString("propertyType");
							int numOfBed = rs.getInt("numberOfBed");
							int numOfBath = rs.getInt("numberOfBath");
							boolean furn = rs.getBoolean("furnished");
							String area = rs.getString("area");
							String status = rs.getString("status1");
							int llID = rs.getInt("landlordID");
							
							temp = new Property(propID, propType, numOfBed, numOfBath, furn, area, status, llID);
							prop.add(temp);
						}
					}
				}
			}
		}
		catch(Exception e){ 
			System.out.println(e);
		}  
		return prop;
	}
	//get properties related to what was searched
	public Vector<Property> getSearchedProperties(String type, String numBed, String numBath , String furnished, String location){
        Vector<Property> searched = new Vector<Property>();
        try {
            stmt = mysql_con.createStatement();  
            rs = stmt.executeQuery("select * from property");  
            
            while(rs.next()) {
                int propID = rs.getInt("propertyId");
                String propType = rs.getString("propertyType");
                int numOfBed = rs.getInt("numberOfBed");
                int numOfBath = rs.getInt("numberOfBath");
                String furn = rs.getString("furnished");
                String area = rs.getString("area");
                String status = rs.getString("status1");
                int llID = rs.getInt("landlordID");
                
                if((type.equals(propType) || type.equals("-------")) && (numBed.equals("-------") || numBed.equals(Integer.toString(numOfBed))) && (numBath.equals("-------") || numBath.equals(Integer.toString(numOfBath))) &&
                        (furnished.equals("-------") || furnished.equals(furn)) && (location.equals("-------") || location.equals(area)) ){
                	boolean f = false;
                	if(furnished == "true") {
                		f = true; 
                	}
                    Property temp = new Property(propID, propType, numOfBed, numOfBath, f, area, status, llID);
                    searched.add(temp);
                }
            }
        }
        catch(Exception e){ 
            System.out.println(e);
        }  
        return searched;
    }
	
	//add a row into a specific table
    public void addItems(String table, ArrayList<String> attributes) {
        try {
            String query = "INSERT INTO " + table + " VALUES (";
            for(int i = 0; i < attributes.size(); i++){
                if(i != attributes.size()-1){
                    query += attributes.get(i) + ", ";
                }else{
                    query += attributes.get(i) + ")";
                }
            }
            System.out.println(query);
            PreparedStatement preparedStatment = mysql_con.prepareStatement(query);

            preparedStatment.executeUpdate();
            preparedStatment.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
	// adds a message in the messages table for the landlord of that property
	public void sendMailToLandlord(String message, int propertyId)  
    {
        try {
            stmt = mysql_con.createStatement();
            rs = stmt.executeQuery("select * from property");

            while(rs.next())
            {
                if(rs.getInt("propertyId") == propertyId)
                {
                    String sql = "INSERT INTO messages (landlordID, message)" +
                            "VALUES (?, ?)";
                    PreparedStatement preparedStatement = mysql_con.prepareStatement(sql);
                    preparedStatement.setInt(1, rs.getInt("landlordID"));
                    preparedStatement.setString(2, message);
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
	
	// adds a message in the messages table for the landlord of that property
    public Vector<String> getAllMails(int landlord_id)  
    {
        Vector<String> result = new Vector<>();
        try {
            stmt = mysql_con.createStatement();
            rs = stmt.executeQuery("select * from messages");

            while(rs.next())
            {
                if(rs.getInt("landlordID") == landlord_id)
                {
                    result.add(rs.getString("message"));
                }
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }

        return result;
    }
    
    //Manager can set a fee
    public void setFee(String newFeeText, String newFeeDurationText) {

    	 try {
 			String query = "INSERT INTO FEE (fee, duration) VALUES (?,?) ON DUPLICATE KEY UPDATE fee=";
 			query+=newFeeText;
 			System.out.println("This is the query in setFee: " + query);
 			PreparedStatement pStat = mysql_con.prepareStatement(query);
 			pStat.setString(1, newFeeText);
 			pStat.setString(2, newFeeDurationText);
 			pStat.executeUpdate();
 			pStat.close();
 		} catch (SQLException e) {
 			e.printStackTrace();
 		}
    	
    }
    
  //Landlords can get a fee
    public Vector<String> getFee() {
    	Vector<String> feeStr = new Vector<String>();
    	 try {
             stmt = mysql_con.createStatement();
             rs = stmt.executeQuery("select * from FEE");
             
             while(rs.next()) {
            	 feeStr.add( "Fee amount: " + rs.getDouble("fee") + " for " + rs.getInt("duration") + " days");
             }
    	 }catch(Exception e) {
             System.out.println(e);
    	 }
    	 //System.out.println(feeStr);
		return feeStr;
    	
    }
    
  //Manager can change status of a property
    public boolean changeStatus(String propID, String status){
    	 try {
    		 
			 if(status.equals("-------")){
				 return false;
			 }else{
				if(status.equals("active")) {
    			 datePosted(propID); 
	    		 }else if (status.equals("rented")) {
	    			 System.out.println("rented");
	    			 dateRented(propID);
	    		 } 
				String query = "UPDATE PROPERTY SET status1='" + status + "' WHERE (propertyId='" + propID.trim() + "')" ;
  				System.out.println(query);
  				PreparedStatement pStat = mysql_con.prepareStatement(query);
  				pStat.executeUpdate();
			  	return true;
			 }
    		
 		} catch (SQLException e) {
 			e.printStackTrace();
 		}
		 return false;
    }
    
    //add property to database 
	public int registerProperty(String type, int noOfBed, int noOfBath, boolean furn, String location, int llID) {
		int newPropID = -1;
    	try {
	    		stmt = mysql_con.createStatement();
	            rs = stmt.executeQuery("select * from property");
				 
	            while(rs.next()){
	            	int propID = rs.getInt("propertyId");
					newPropID = propID;
				}
	            newPropID++;
	            
    			String query = "INSERT INTO PROPERTY (propertyID, propertyType, numberOfBed, numberOfBath, furnished, area, landlordID,status1) VALUES (?,?,?,?,?,?,?,?)";
    			PreparedStatement pStat = mysql_con.prepareStatement(query);
     			
    			pStat.setInt(1, newPropID);
    			pStat.setString(2, type);
     			pStat.setInt(3, noOfBed);
     			pStat.setInt(4, noOfBath);
     			pStat.setBoolean(5, furn);
     			pStat.setString(6, location);
     			pStat.setInt(7, llID);
     			pStat.setString(8, "suspended");
     			pStat.executeUpdate();
     			pStat.close();
     			
	    		
			 
    	}catch (SQLException e) {
			e.printStackTrace();
		}
		return newPropID;
	}

	//This function registers users - landlords and renters
	public int registerUser(String name, String phNo, String mail, String pwd, String uType) {
		int newUserID = -1;
    	try {
	    		stmt = mysql_con.createStatement();
	            rs = stmt.executeQuery("select * from user");
				 
	            while(rs.next()){
	            	int userID = rs.getInt("id");
					newUserID = userID;
				}
	            newUserID++;
	            
    			String query = "INSERT INTO USER (username, id, email, phoneNumber, password1, type1) VALUES (?,?,?,?,?,?)";
    			PreparedStatement pStat = mysql_con.prepareStatement(query);
     			
    			pStat.setString(1, name);
    			pStat.setInt(2, newUserID);
     			pStat.setString(3, mail);
     			pStat.setString(4, phNo);
     			pStat.setString(5, pwd);
     			pStat.setString(6, uType);
     			pStat.executeUpdate();
     			pStat.close();
     			
	    		
			 
    	}catch (SQLException e) {
			e.printStackTrace();
		}
		return newUserID;
	}
	
	//Landlord gets his emails
	public Vector<String> getEmail(int llID){
		Vector<String> mail = new Vector<String>();
		
		try {
			stmt = mysql_con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery("select * from emails");
			mail.clear(); 
			while(rs.next()) {
				if(llID == rs.getInt("landlordID")){
					String temp = rs.getString("emailMessage");
					mail.add(temp);
				}
			}
		} catch (SQLException ex) {
            ex.printStackTrace();
        }
		
		for(String bruh: mail) {
			System.out.println("This is landlord's mail: " + bruh + "\n");
		}
		return mail;
	}
	
	//This function send emails to a specific landlord
	public void sendMail(int llID, String email) {
			int msgID = 0;
	    	try {
		    		stmt = mysql_con.createStatement();
		            rs = stmt.executeQuery("select * from emails");
					 
		            while(rs.next()){
		            	int emailID = rs.getInt("messageID");
						msgID = emailID;
					}
		            msgID++;
		            
	    			String query = "INSERT INTO EMAILS (messageID, emailMessage, landlordID) VALUES (?,?,?)";
	    			PreparedStatement pStat = mysql_con.prepareStatement(query);
	     	
	    			pStat.setInt(1, msgID);
	     			pStat.setString(2, email);
	     			pStat.setInt(3, llID);
	     			
	     			pStat.executeUpdate();
	     			pStat.close();
	     			
	    	}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	//when logging out, add the time of when the user logged out
	public void logout(String id){	
		try {
	        Calendar c1 = Calendar.getInstance();	        
			String query = "UPDATE USER SET lastLogin='" + Long.toString(c1.getTimeInMillis()) + "' WHERE (id='" + id + "')" ;
			System.out.println(query);
			PreparedStatement pStat = mysql_con.prepareStatement(query);
			pStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//when was the property posted
	public void datePosted(String id){	
		try {
	        Calendar c1 = Calendar.getInstance();	        
			String query = "UPDATE PROPERTY SET datePosted='" + Long.toString(c1.getTimeInMillis()) + "' WHERE (propertyId='" + id.trim() + "')" ;
			System.out.println(query);
			PreparedStatement pStat = mysql_con.prepareStatement(query);
			pStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//when was the property declared rented
	public void dateRented(String id){	
		try {
	        Calendar c1 = Calendar.getInstance();	        
			String query = "UPDATE PROPERTY SET dateRented='" + Long.toString(c1.getTimeInMillis()) + "' WHERE (propertyId='" + id.trim() + "')" ;
			System.out.println(query);
			PreparedStatement pStat = mysql_con.prepareStatement(query);
			pStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//get new properties based on when the last login time was
	public Vector<Property> getNewProperties(String id){
		Vector<Property> props = new Vector<Property>();
		try {
			stmt = mysql_con.createStatement();  
			rs = stmt.executeQuery("SELECT * FROM USER");
			long lastTime = 0;
			while(rs.next()) {
				if(rs.getString("id").equals(id.trim())) {
					if(rs.getString("lastLogin") != null) {
						lastTime = rs.getLong("lastLogin");
					}
					break;
				}
			}			
			stmt = mysql_con.createStatement(); 
			rs = stmt.executeQuery("SELECT * FROM PROPERTY");  
			long time = 10;
			while(rs.next()) {
				if(rs.getString("datePosted") != null) {
					 time = rs.getLong("datePosted");
				}
				if(lastTime < time){
					Property temp;
					int propID = rs.getInt("propertyId");
					String propType = rs.getString("propertyType");
					int numOfBed = rs.getInt("numberOfBed");
					int numOfBath = rs.getInt("numberOfBath");
					boolean furn = rs.getBoolean("furnished");
					String area = rs.getString("area");
					String status = rs.getString("status1");
					int llID = rs.getInt("landlordID");
					
					temp = new Property(propID, propType, numOfBed, numOfBath, furn, area, status, llID);
					props.add(temp);
				}
			}
		}
		catch(Exception e){ 
			System.out.println(e);
		}  
		return props;
	}
	//get whether user has notifications on or off
	public boolean getNotifications(String id) {
		try {
			stmt = mysql_con.createStatement();  
			rs = stmt.executeQuery("SELECT * FROM USER");
			while(rs.next()) {
				if(rs.getString("id").equals(id.trim())) {
					if(rs.getString("notifications") == null) {
						return true;
					}else if(rs.getString("notifications").equals("on")){
						return true;
					}
					else {
						return false;
					}
				}
			}
		}
		catch(Exception e){ 
			System.out.println(e);
		}  
		return false;
	}
	//set notifications for user on or off
	public void setNotifications(String id,boolean notifs) {
		try {
			String query = "UPDATE USER SET notifications='";
			if(notifs) {
				query += "on' WHERE (id='" + id.trim() + "')";
			}else {
				query += "off' WHERE (id='" + id.trim() + "')";
	
			}
			PreparedStatement pStat = mysql_con.prepareStatement(query);
			pStat.executeUpdate();
		}
		catch(Exception e){ 
			System.out.println(e);
		}  
	}
	//get new emails based on when the landlord last logged on
	public boolean newEmails(String id) {
		try {
			stmt = mysql_con.createStatement();  
			rs = stmt.executeQuery("SELECT * FROM USER");
			long time = 0;
			while(rs.next()) {
				if(rs.getString("landlordID").equals(id.trim())) {
					if(rs.getString("lastLogin") != null) {
						time = rs.getLong("lastLogin");
					}
					break;
				}
			}
			stmt = mysql_con.createStatement();  
			rs = stmt.executeQuery("SELECT * FROM EMAIL");
			while(rs.next()) {
				if(rs.getString("landlordID").equals(id.trim())) {
					if(rs.getString("time") == null) {
						return true;
					}else if(time < rs.getLong("time")){
						return true;
					}
					else {
						return false;
					}
				}
			}
		}
		catch(Exception e){ 
			System.out.println(e);
		}  
		return false;
	}
	//get the number of rented or listed properties in the last 30 days
	public int[] last30Days() {
		int rented = 0; 
		int posted = 0;
		int[] temp = new int[2];
		try {
			stmt = mysql_con.createStatement();  
			rs = stmt.executeQuery("SELECT * FROM PROPERTY");
			 Calendar c1 = Calendar.getInstance();	
			 long time = c1.getTimeInMillis() - 2592000000L;
			 
			while(rs.next()) {
				if(rs.getString("dateRented") != null) {
					if(time < rs.getLong("dateRented")) {
						rented++;
					}
				}if (rs.getString("datePosted") != null) {
					if(time < rs.getLong("datePosted")) {
						posted++;
					}
				}
			}
			temp[0] = rented;
			temp[1] = posted; 
		}
		catch(Exception e){ 
			System.out.println(e);
		}  
		return temp;
	}

}
