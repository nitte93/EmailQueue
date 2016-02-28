# EmailQueue
Email client using Java.

  ______                 _ _  ____                        
 |  ____|               (_) |/ __ \                       
 | |__   _ __ ___   __ _ _| | |  | |_   _  ___ _   _  ___ 
 |  __| | '_ ` _ \ / _` | | | |  | | | | |/ _ \ | | |/ _ \
 | |____| | | | | | (_| | | | |__| | |_| |  __/ |_| |  __/
 |______|_| |_| |_|\__,_|_|_|\___\_\\__,_|\___|\__,_|\___|
                                                          

 Owner : Nitesh Tiwari.

  ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ 
 |______|______|______|______|______|______|______|______|______|______|______|                                                                                                                                                           
 		Requirements:
 ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ 
 |______|______|______|______|______|______|______|______|______|______|______|                                                                                                                                                             
 
	1. Dummy SMTP server running on localhost, Like fakeSMTP-2.0.jar is used here, default SMTP port (25).
	2. MySQL Database.
 	3. Java Mail Api (Javax.mail.jar)
    4. MySQL JDBC connector.
  

  ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ 
 |______|______|______|______|______|______|______|______|______|______|______|                                                                              
                                                                               
		 Steps to run this application:
  ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ 
 |______|______|______|______|______|______|______|______|______|______|______|
                                                                               
                                                                               
	1) First if you're doing this on a local smtp server, make sure to start the Smtp server first
   	   Here I've used FakeSMTP server version 2.0. 
           (I've used fakesmtp here because it's light weight, simple GUI, first link on google fi you search for 
	   a fakesmtp server).

	2) Run Your project.

	3) Here Make sure to see the default configurations that I've used.
   	   Change it according to your requirements.
   	   Like your mysql username, password, no of threads you want etc.

	4) Here, program will prompt you to insert some random records into the table.
   	   Use it as per your requirement.
           Please understand, Once the program completes executing, all the emails in the 
           table is already sent, marked as 1. In such a case in order to send more mails
           make sure to insert more records otherwise it'll say '0' distinct mails found.

	5) Voila! DOne. We've now sent mails as fast as possible.


  ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ 
 |______|______|______|______|______|______|______|______|______|______|______|                                                                                                                                                          
		Algorithm Used:
  ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ ______ 
 |______|______|______|______|______|______|______|______|______|______|______|
                                                                               
                                                                               
	Step 1) Load records in the database table.
	       	Each Records are an Email Template.
	
        	|FROM | TO | SUBJECT | BODY |

	Step 2) Find the distinct <FROM , TO > Pair from the inserted Records.
		Store these distinct pairs into a temp vector of pairs.


	Step 3) Here, I would first show you the normal approach(Sequential Approach).
       	        We will then tweak it to get make it work faster.
	
		Sequential Approach.
		====================			
		Substep 1) Iterate over the vector list of our distinct mail pairs.
		Substep 2) Foreach distinct pair find the total number of mails.
		Substep 3) Iterate over these mails and send them one by one.

		Faster Approach : Using Threads.
		===============================
		For this approach first we decide the No. of threads we want to use.
		Default here is 2. You can change it as per the requirement.
	
		Substeps:
		For each thread we will iterate over our already obtained list of 
		distinct pair. We assign the threads manually to alternate mails in
		the record list depending upon the number of threads
		this alternativity is decided.
		Like in case of NUmber of threads = 2.
		sequence goes something like this, 0 2 4 6 8 ......size of the mail list.

	 			
		Step 4) In this step, we use javax mail api to perform mail sending operations, while
		the thread is sending mails, it also updates the sent status after the mail
		sent operation in performed.






TTTTTTTTTTTTTTTTTTTTTTThhhhhhh                                                 kkkkkkkk                            
T:::::::::::::::::::::Th:::::h                                                 k::::::k                            
T:::::::::::::::::::::Th:::::h                                                 k::::::k                            
T:::::TT:::::::TT:::::Th:::::h                                                 k::::::k                            
TTTTTT  T:::::T  TTTTTT h::::h hhhhh         aaaaaaaaaaaaa   nnnn  nnnnnnnn     k:::::k    kkkkkkk    ssssssssss   
        T:::::T         h::::hh:::::hhh      a::::::::::::a  n:::nn::::::::nn   k:::::k   k:::::k   ss::::::::::s  
        T:::::T         h::::::::::::::hh    aaaaaaaaa:::::a n::::::::::::::nn  k:::::k  k:::::k  ss:::::::::::::s 
        T:::::T         h:::::::hhh::::::h            a::::a nn:::::::::::::::n k:::::k k:::::k   s::::::ssss:::::s
        T:::::T         h::::::h   h::::::h    aaaaaaa:::::a   n:::::nnnn:::::n k::::::k:::::k     s:::::s  ssssss 
        T:::::T         h:::::h     h:::::h  aa::::::::::::a   n::::n    n::::n k:::::::::::k        s::::::s      
        T:::::T         h:::::h     h:::::h a::::aaaa::::::a   n::::n    n::::n k:::::::::::k           s::::::s   
        T:::::T         h:::::h     h:::::ha::::a    a:::::a   n::::n    n::::n k::::::k:::::k    ssssss   s:::::s 
      TT:::::::TT       h:::::h     h:::::ha::::a    a:::::a   n::::n    n::::nk::::::k k:::::k   s:::::ssss::::::s
      T:::::::::T       h:::::h     h:::::ha:::::aaaa::::::a   n::::n    n::::nk::::::k  k:::::k  s::::::::::::::s 
      T:::::::::T       h:::::h     h:::::h a::::::::::aa:::a  n::::n    n::::nk::::::k   k:::::k  s:::::::::::ss  
      TTTTTTTTTTT       hhhhhhh     hhhhhhh  aaaaaaaaaa  aaaa  nnnnnn    nnnnnnkkkkkkkk    kkkkkkk  sssssssssss    
                                                                                                                   
                                                                                                                   
                                                                                                                   
                                                                                                                   
                                                                                