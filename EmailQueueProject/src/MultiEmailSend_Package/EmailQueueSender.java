package MultiEmailSend_Package;

import java.io.*;
import java.util.Vector;


/**
* <h1>Send Emails from the email Queue as fast as possible</h1>
* EmailQueueSender is the activator class  for this java project, 
*
* @author  Nitesh Tiwari
* @version 1.0
* @since   4-9-2015
*/
public class EmailQueueSender {
		
		public static int numOfThreads = 2; //initializing No. of threads as 2, could be changed later. 
		public static Vector< String[] > emailPairs = null; // emailParis is use to store the distinct mail pairs returned from GetDistinctMailPairs method
		
		/**
	    * This is out main class, initializes our projects.
	    * It asks for the required configuration/setting changes to send mails.
	    * It initializes our Emailsender projects and implements, calls supportive classes objects, methods to send
	    * emails as fast as possible.
	    *  @param args Unused.
	    * @return Nothing.
	    * @exception IOException On input error.
	    */
		public static void main(String args[]) throws IOException{
			showSettings();
			changeSettings();
			if(insertIntoDatabase()){
				insertValuesToDB();
			}
			
			emailPairs = GetDistinctMailPairs();
			String newline = System.getProperty("line.separator");
			System.out.println("--------------------------------------------");
			System.out.println("Distinct mailpairs obtained "+ emailPairs.size()+" "
					+ newline+"Note: If the distinct mail pair obtained value is '0', then all mails are sent. "
					+ "You need to insert more mails."+newline+"Run the program again and make sure to press Y "+newline+"to insert more random mail records"
					+ " into the database when the screen asks you for the option to insert mails.");
			System.out.println("--------------------------------------------");

			if(emailPairs != null){
				MultipleMailDriver m;
				for(int i=1; i<=numOfThreads; i++){
					m = new MultipleMailDriver(i);
					m.start();
				}
			}
		}
		
		/**
		* This method is used to show the default settings/configurations to the users
		* @return nothing
		*/
		public static void showSettings(){
			String newline = System.getProperty("line.separator");
			System.out.println("The default settings are ");
			System.out.println(newline+"*********SMTP Server Settings**********");
			System.out.println("SMTP Server Address: "+SMTPMailSendDriver.smtpHost);
			System.out.println("SMTP User Email: "+SMTPMailSendDriver.smtpUser);
			System.out.println("SMTP User Password: "+SMTPMailSendDriver.smtpPassword);
		
			System.out.println(newline+"********MySql Database Settings*********");
			System.out.println("MySql Server Name/Address: "+ DataAccessInterface.mysqlServerAddress);
			System.out.println("MySql Username: "+ DataAccessInterface.username);
			System.out.println("Mysql Password: "+ DataAccessInterface.password);
			System.out.println("MySql Database name: "+ DataAccessInterface.databaseName);
			System.out.println("MySql Table name: "+ DataAccessInterface.tableName);
		}

		/**
		* This method is used to provide edit functionality to the users, to make and 
		* update the default configurations/ settings properties according to their 
		* requirements.
		* @returns int
		*/
		public static int changeSettings()throws IOException{
			InputStreamReader inputStream = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(inputStream);
			String choice;
			String newline = System.getProperty("line.separator");
			while(true){

				System.out.println(newline+"------No. of threads used (Default)------------ "+ EmailQueueSender.numOfThreads);
				System.out.println(newline+"-----------------------------------------------");
				System.out.println("Press Y/y for changing the default settings. ");
				System.out.println("Press N/n for no changes. ");
				System.out.println("Enter q to exit.");
				System.out.println("Your Choice:");
				choice = br.readLine();
				if("q".equals(choice)){
					System.exit(0);
				}else if("Y".equals(choice) || "y".equals(choice)){
					System.out.println("Enter No. of Threads you want, ( Default is 2 ):  ");
	                EmailQueueSender.numOfThreads = Integer.parseInt(br.readLine());
	                
					System.out.println("Enter the SMTP Server Address (default is "+SMTPMailSendDriver.smtpHost+"): ");
					SMTPMailSendDriver.smtpHost = br.readLine();
					System.out.println("Enter the SMTP Username (default is  "+SMTPMailSendDriver.smtpUser+"): ");
					SMTPMailSendDriver.smtpUser = br.readLine();
					System.out.println("Enter the user password (default is "+SMTPMailSendDriver.smtpPassword+" ");
					SMTPMailSendDriver.smtpPassword = br.readLine();
					
					
					System.out.println("Please enter MySql Database Name/Address (default is "+DataAccessInterface.databaseName+"): ");
					DataAccessInterface.databaseName = br.readLine();

					System.out.println("Please MySql Table name  (default is "+DataAccessInterface.tableName+"): ");
					DataAccessInterface.tableName = br.readLine();

					System.out.println("Please enter MySql Username (default is "+DataAccessInterface.username+"): ");
					DataAccessInterface.username = br.readLine();

					System.out.println("Please enter MySql Password (default is "+DataAccessInterface.password+"): ");
					DataAccessInterface.password = br.readLine();
					System.out.println("-----------------------------------------------"+newline);
					
					break;
				}else if("N".equals(choice) || "n".equals(choice)){
					break;
				}else{
					System.out.println("You entered a wrong choice! ");
				}
			}
			return 0;
		}

		/**
		* This method is used to prompt users, if he wants to insert more records to our database.
		* @returns boolean
		*/
		public static boolean insertIntoDatabase()throws IOException{
			System.out.println("Do you wnat to insert more records into the table.");
			System.out.println("Press Y/y if your answer is  Yes");
			System.out.println("Press N/n if your answer is  No");
			System.out.println("Press q to Exit");
			InputStreamReader inputStream = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(inputStream);
	
			String option;
			option = br.readLine(); 
			if("q".equals(option)){
				System.exit(0);
			}else if("Y".equals(option) || "y".equals(option)){
				return true;
			}else{
				return false;
			}
			return false;
		}
		
		/**
		* Depending upon the user decisions to insert more records, this methods is called insert more records
		* into the database.
		* @returns nothing
		*/
		public static void insertValuesToDB(){
			EmailTemplate newRow;
			System.out.println("Inserting");
			
	        for(int i=0; i<50; i++) 
	        {
	            for(int j=0; j<10; j++) 
	            {
	            	newRow = new EmailTemplate("abc"+i+"@gmail.com", "xyz"+j+"@gmail.com", "Subject: Hello EmailQueue"+(i+j), "Body: Hello! I am sending these mails using multithreading and javax mail api.");
	                newRow.insertRows();
	            }
	        }
	
		}

		/**
		* This method is used to get a list of distinct mail pairs form the database..
		* @returns vector<String[]>
		*/
		public static Vector<String[]> GetDistinctMailPairs(){
			DataAccessInterface dAI = new DataAccessInterface();
			Vector<String[]> distinctRecords = null;
			try{
				distinctRecords = dAI.FetchDistinctMailPairs();
				return distinctRecords;
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Error Occured while Fetching distinct Mail pairs: " + e.getMessage());
				return null;
			}
		}
	
}
