package MultiEmailSend_Package;

import java.sql.SQLException;
import java.util.Vector;

import javax.mail.MessagingException;

/**
* <h1>MultiMailDriver extends the Thread class</h1>
* This class defines methods to perform multithreading email sending operations.
* @author  Nitesh Tiwari
* @version 1.0
* @since   4-9-2015
*/
public class MultipleMailDriver extends Thread {
	private Thread t = null;
	private int tIndex;			// Variable to hold Thread Index .
	public Vector<Integer> sentList;   // Variable to hold Sent list mails. (mails that're already processed(sent))
	public String tName;		  // Variable to hold Thread name
	
	/**
	 * Constructor function to initialize the corresponding thread's properties.
    * @return nothing.
    */
	public MultipleMailDriver(int num){
		this.tIndex  = num;
		this.tName = "Thread_"+num;
		System.out.println("Creating " + tName);
		sentList = new Vector<Integer>();
	}
	
	
	@Override
	public void start (){
		System.out.println("Starting " +tName);
		if(t == null){
			System.out.println("Startingagain"+tName);
			t = new Thread(this, tName);
			t.start ();
		}
	}

	@Override
	public void run(){
		try{
			DataAccessInterface dAIObj = new DataAccessInterface();
			
			//we assign the threads manually to alternate mails in the record list depending upon the number of threads
			//this alternativity is decided.
			//Like in case of NUmber of threads = 2.
			// sequence goes something like this, 0 2 4 6 8 ......size of the mail list.
			for(int i = this.tIndex-1; i<EmailQueueSender.emailPairs.size(); i = i+EmailQueueSender.numOfThreads){
				String[] str = EmailQueueSender.emailPairs.get(i);
				System.out.println("Sending Mails with thread: "+this+","+ this.tIndex +",");
				
				EmailTemplate[] emails = this.GetEmailRecordList(str[0], str[1]);
				if(emails.length > 0){

					String from_address = emails[0].from_email_address;
					String to_address = emails[0].to_email_address;
					//System.out.println(from_address+" "+to_address);
					//SendEmails(from_address, to_address, emails);
					if(this.SendEmails(from_address, to_address, emails) == 1){
						System.out.println(this.tName+ ": sent emails from" + from_address+"to"+ to_address +"are sent Successfully");
					}else{
						System.out.println(this.tName+ " Failed to send mails.");
					}
				}
			}
			if(sentList.size() > 0){
				try{
					dAIObj.MarkSent(this.sentList);
					
				}catch(SQLException e){
					System.out.println(this.tName+ "Error Occured "+ e.getMessage());
				}
			}
		}catch(Exception e){
			System.out.println("Error: " + tName+ " :"  +e.getMessage());
		}
	}

	/**
	*GetEmailRecord method gets the list of mails for a particular <from,to> mail pairs.
    * @return nothing.
    */	
	public EmailTemplate[] GetEmailRecordList(String from, String to){
		Vector<String[]> recordList;
		DataAccessInterface dAI = new  DataAccessInterface();
		try{
			recordList = dAI.RetriveEmailRecords(from, to);
			EmailTemplate[] emails = new EmailTemplate[recordList.size()]; 
			for(int i=0 ; i <recordList.size(); i++){
                String[] str = recordList.get(i);

                emails[i] = new EmailTemplate();
                emails[i].id = Integer.parseInt(str[0]);
                emails[i].from_email_address = str[1];
                emails[i].to_email_address = str[2];
                emails[i].subject = str[3];
                emails[i].body = str[4];
			}
			return emails;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error Occured while fetching records: "+ e.getMessage()) ;
			return null;
		}
	}
	/**
	 * This method is used to send mails, we pass the particular thread object using 'this'
	 * keyword, so that for each thread the thread can be identified at the Sendmail function implementation.
    * @return int.
    */
    public int SendEmails(String from, String to, EmailTemplate[] mails) throws Exception  
    {
    	System.out.println("Hello");
       SMTPMailSendDriver smtps = new SMTPMailSendDriver();
        try 
        {
            smtps.SendEmail(from, to, mails, this); 
            return 1;
        } 
        catch (MessagingException ex) 
        {
            ex.printStackTrace();
            System.out.println("Failed to send emails " + ex.getMessage());
            return 0;
        }

    	}// end of SendEmails

}