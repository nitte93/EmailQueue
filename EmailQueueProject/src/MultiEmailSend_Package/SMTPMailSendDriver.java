package MultiEmailSend_Package;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
* <h1>SMTPMailSendDriver configures smtp  settings and requirements</h1>
* This class defines methods to perform smtp email sending operations.
* @author  Nitesh Tiwari
* @version 1.0
* @since   4-9-2015
*/

public class SMTPMailSendDriver {
	public static String smtpHost = "localhost";    	  // SMTP host address
    public static String smtpUser = "root";	 // SMPTP User name/email, change it to your valid gmail, yahoo id if you're not using local Smtp server
    public static String smtpPassword  = "";   //SMTP password, change it to your valid gmail, yahoo password if you're not using local Smtp server     

	private static Properties props = null;	     	  // Variable to hold the properties for smtp mail send
    private static Session mailSession = null;		 //Variable to hold the session for smtp mail send
	
	/**
	*Constructor function to initialize smtp property settings and smtp session variable.
    * @return nothing.
    */	
    public SMTPMailSendDriver(){
        if(SMTPMailSendDriver.props == null){
        	SMTPMailSendDriver.props = new Properties();
	    
     /************* Uncomment the below setting for using any other company's SMTP Server***************/
     /***********But Make Sure to comment the local smtp server properties*******/
      /*  	props.put("mail.smtp.host", "smtp.mail.yahoo.com"); // for gmail use smtp.gmail.com
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.debug", "true"); 
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.port", "465");
	        props.put("mail.smtp.socketFactory.port", "465");
	        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        props.put("mail.smtp.socketFactory.fallback", "false");
		*/
     /***************For using other Company's smtp server, property settings ends here***************/   	
	    //    SMTPSend.prop = new Properties();
        	
        	SMTPMailSendDriver.props.put("mail.smtp.auth", "true"); //Authentication true
        	SMTPMailSendDriver.props.put("mail.smtp.starttls.enable", "true");
        	SMTPMailSendDriver.props.put("mail.smtp.host", SMTPMailSendDriver.smtpHost);
        	SMTPMailSendDriver.props.put("mail.smtp.port", "25"); // default Port
        }
        
        if(SMTPMailSendDriver.mailSession == null){
        mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
        	@Override
            protected PasswordAuthentication getPasswordAuthentication() {
        		/*Uncomment the next line if you want to use gmail's, yahoo's smtp server
        		 * Make sure to provide a valid email and password for authentication
        		 * */
               // return new PasswordAuthentication("youremail@yahoo.com", "yourpassword");
                return new PasswordAuthentication(SMTPMailSendDriver.smtpUser, SMTPMailSendDriver.smtpPassword);
            }
        });
        }
}
	/**
	 * SendEmail method is used to send smtp mails and it also updates the send mail list after sending of mails
    * @return boolean.
    */	

    public boolean SendEmail(String from, String to, EmailTemplate[] emails, MultipleMailDriver thread)throws Exception{
      try{
		mailSession.setDebug(true); // Enable the debug mode

        Message msg = new MimeMessage( mailSession );

        //Set the FROM, TO, DATE and SUBJECT fields
       // msg.setFrom( new InternetAddress( emails[0].from_email_address ) );
       // msg.setRecipients( Message.RecipientType.TO,InternetAddress.parse(emails[0].to_email_address) );
        msg.setFrom( new InternetAddress( emails[0].from_email_address) );
        msg.setRecipients( Message.RecipientType.TO,InternetAddress.parse(emails[0].to_email_address) );

        msg.setSentDate( new Date());
        for(int i=0; i<emails.length; i++){
        	msg.setSubject(emails[i].subject);

        	// Create the body of the mail
        	msg.setText( emails[i].body );

        	// Ask the Transport class to send our mail message
        	Transport.send( msg );
        	thread.sentList.add(emails[i].id);
        	
        }
        return true;
	}catch(Exception e){
		throw e;
	}
	}

}
