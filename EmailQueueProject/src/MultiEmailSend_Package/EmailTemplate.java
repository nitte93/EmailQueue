package MultiEmailSend_Package;


/**
* <h1>Define the Email Template <id, from, to, subject, body></h1>
*This class is responsible for declaring/defining the email template.
*Things that consist to make email an email is specified here
* @author  Nitesh Tiwari
* @version 1.0
* @since   4-9-2015
*/
public class EmailTemplate {
	
	public String from_email_address, to_email_address, subject, body;
	public int id;
	
	public EmailTemplate(){
		
	}
	
	/**
	* Constructor to implement EmailTempate class as soon as an object of that type is created with the required
	* email template arguments.     
	*/
	public EmailTemplate(String from_email_address, String to_email_address, String subject, String body){
		this.from_email_address = from_email_address;
		this.to_email_address =  to_email_address;
		this.subject = subject;
		this.body = body;
	}
	
	/**
	* This method creates a DataAccessInterface object and is used to insert more mails into database. 
    *  @param args Unused.
    * @return int.
	*/	
	public int insertRows(){
		DataAccessInterface dataAccess = new DataAccessInterface();
		int rowInserted = dataAccess.insertIntoDB(this.from_email_address, this.to_email_address, this.subject, this.body);
		
		if(rowInserted != -1){
			return 1;
		}
		return 0;
	}
}
