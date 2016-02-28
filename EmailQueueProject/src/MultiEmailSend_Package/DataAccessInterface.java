package MultiEmailSend_Package;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
*<h1>Interface to interact with out database.</h1>
*This class is responsible for declaring/defining the required objects that makes the 
*database connections possible. Implements methods to retrieve, update etc to the database table.
* @author  Nitesh Tiwari
* @version 1.0
* @since   4-9-2015
*/
public class DataAccessInterface {

	public static String jdbcConnector = "com.mysql.jdbc.Driver";  //jdbc driver used for connection to mysql
    public static String databaseName = "CouponDunia";		   //Default Database Name
    public static String tableName = "EmailQueue";			  //Default Database Table Name
    public static String mysqlServerAddress = "localhost"; // MySql server name
    public static int mysqlPort = 3306; 					//MySql port
    public static String username = "root"; 			   //Default MySql Username	as per my System.		
    public static String password = "localhost";		  //MySql password as per my system, Change it as per your requirement
    public static String mysqlHost = "jdbc:mysql://localhost:3306/";      //MySql host address
    public static String databaseUrl  = mysqlHost + databaseName;      //database connection url
    private static Connection conn = null;

    /**
    * Constructor method for DataAccesInterface class, Starts connection to our database,
    * Makes sure that the required table already exists, if not, then create the table. 
	* @return Nothing.
	*/
    public DataAccessInterface() {
        if(DataAccessInterface.conn == null) {
            int database_exists = 0;	// Flags to check if the required database exists
            int table_exists = 0;		// Flags to check if the required table exists
            try {
                Class.forName(jdbcConnector);
                DataAccessInterface.conn = DriverManager.getConnection(mysqlHost, username, password); //connecting to mysql server

                Statement s = conn.createStatement();
                //create database if it does not exist 
                s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + databaseName);
                database_exists = 1; // set the flag for database existence, 0 if not exists.

            } catch(ClassNotFoundException | SQLException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                if(database_exists == 1) {     //Check if database exists
                    try {
                        conn.close();
                        conn = DriverManager.getConnection(databaseUrl, username, password);
                        
                        if(table_exists == 0) {     //create table if it does not exist 
                            Statement s = conn.createStatement();
                            //Create table is the required table does not exist
                            s.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " ("
                                + "  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                                + "  `from_email_address` varchar(100) NOT NULL,"
                                + "  `to_email_address` varchar(100) NOT NULL,"
                                + "  `subject` varchar(200) NOT NULL,"
                                + "  `body` varchar(1000) NOT NULL,"
                                + "  `status` int(11) NOT NULL DEFAULT '0');");  //to record whether this email has been sent or not.
                            
                            table_exists = 1;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(DataAccessInterface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    /**
    * This method is used to insert emails into our database table,
	* @return int.
	*/
	public int insertIntoDB(String from, String to, String subject, String body){
		if(DataAccessInterface.conn == null){
			return -1;
		}else{
			Statement s = null;
			try{
			s = DataAccessInterface.conn.createStatement();
			//Sql query to insert emails into our datbase.
			String sqlQuery = "INSERT INTO "+ DataAccessInterface.tableName
                    
                    + "(from_email_address, to_email_address, subject, body)"
                    + "VALUES ('"+ from +"', '"+to+"', '"+subject+"', '"+body +"')";
			int nomOfAffectedRows = s.executeUpdate(sqlQuery);
            return nomOfAffectedRows;                  
		
		}catch (Exception e) {
			e.printStackTrace();
			return -1;
		}  finally {
            try {
                s.close();
            } catch(Exception e) {
                return -1;
              }
        	}
		}
	}

    /**
    * This method is used to fetch distinct mail pairs from our database.
	* @return Vector<String[]>
	*/
	public Vector<String[]> FetchDistinctMailPairs()throws Exception{
		if(DataAccessInterface.conn == null){
			return null;
		}else{
			Vector<String[]> distinctMailPairs = new Vector<String[]>();
			ResultSet r = null;
			Statement s = null;
			try{
				s = DataAccessInterface.conn.createStatement();
				//Sql query to retrieve distinct mail pairs.
				String sqlQuery = "SELECT DISTINCT from_email_address, to_email_address FROM "+DataAccessInterface.tableName+" where status = 0;";
				r = s.executeQuery(sqlQuery);
				while(r.next()){
					String[] record = {
							r.getString("from_email_address"),
							r.getString("to_email_address")
					};
					distinctMailPairs.add(record);
				}
			}catch(Exception e){
				e.printStackTrace();
				throw e;
			}finally{
				r.close();
				s.close();
			}
			return distinctMailPairs;
		}
	}

    /**
    * This method is used to retrieve mail records from our database based on from and to mail's fields.
	* @return Vector<String[]>
	*/
	public Vector<String[]> RetriveEmailRecords(String from, String to)throws Exception{
		if(DataAccessInterface.conn == null){
			return null;
		}else{
			Vector<String[]> records = new Vector<String[]>();
			Statement s;
			ResultSet r;
			try{
				s = DataAccessInterface.conn.createStatement();
				//Sql query to retrieve records from the database based on from and to email's fields.
				String sqlQuery = "SELECT id, from_email_address, to_email_address, subject, body FROM " 
                        + DataAccessInterface.tableName + " WHERE from_email_address = '" 
                        + from + "' AND to_email_address = '" 
                        + to + "' AND status = 0 ORDER BY id";
                r = s.executeQuery(sqlQuery);
                while(r.next()){
                	String[] record = new String[5];
                    record[0] = ""+r.getInt("id");
                    record[1] = r.getString("from_email_address");
                    record[2] = r.getString("to_email_address");
                    record[3] = r.getString("subject");
                    record[4] = r.getString("body");
                    records.add(record);
                }
                r.close();
                s.close();
			}catch(Exception e){
				e.printStackTrace();
				throw e;
			}
			return records;
		}
	}

    /**
    * This method is used to set the status field as 1(sent) for the sent mails.
	* @return int
	*/	
	public int MarkSent(Vector<Integer> sentList)throws SQLException{
		if(DataAccessInterface.conn == null){
			return -1;
		}else{
			int rowsAffected =0;
			Statement s = null;
			try{
				s = DataAccessInterface.conn.createStatement();
				if(sentList.size() > 0){
					String idList = "(";
					for(int i=0; i<sentList.size(); i++){
						idList += sentList.get(i);
						idList += ",";
					}
                    idList += sentList.get(sentList.size()-1);
                    idList += ")";
                    //Sql query to set the status bit as 1(sent) for the sent mails
                    String sqlQuery = "UPDATE "+DataAccessInterface.tableName+" SET status = 1 WHERE id IN " + idList;
                    rowsAffected= s.executeUpdate(sqlQuery);

				}
			}catch(SQLException e){
					e.printStackTrace();
					throw e;
			}finally{
					s.close();
			}
			return rowsAffected;
		}
	}

}