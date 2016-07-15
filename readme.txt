
Hi Jun I have video attached here which shows how to run my project . I recommend you to go through this link . (http://screencast.com/t/AMKu314ar2)

Steps To Run Project
---------------------

(1) Open your command line ,iterate through directory which contains this project and type ant . 
(2) Ant will start web server on port 8888
(3) Go to your browser and type localhost:8888
(4) It will take you to my website and all other steps are there . 


Functionalities I have given to this server 
------------------------------------------- 
(1) I have given support for http 1.1 and http 1.0 both , so it will work fine with both . 

(2)I have studentDb.txt file which i use as database and by which server will give support to show all students and AddNewStudent in database . 

(3) I have given Cookie support by creating architecture like Java Cookie Constructor , By which server will add Cookie in client system . 

(4)I have started RestFul Webservices which will give you output in json .
 
(5)I have started SOAP based webservices which will give you XML support .

(6) I have implemented Filter interface with one abstract method doFilter(Same like java) which will give you Filter Support (I have created my own Filter interface and Requestdispatcher Class )

(7) I have created one Session class which is singleton and user can call getSession and setAttribute and getAttribute same like Java which will give you  Session support on server context level.

(8) I have used java reflection API in my Servlet Class . I have applied downcasting too .

(9) I have given Listeners support by using observer design pattern , so you can set observers on session,request,page attributes and it will notify observers . 

(10) I have given support to Icalender files by using ical4.jar apis which will parse your calender and shows to client . 

(11) I have given support for 3rd party authentication service with gmail . So you can login with it by your gmail credentials . 

(12)I have given support to show documents . client request will be parsed and document will be shown in pdf format . 

(13) I have created Servlet class somewhat like java which have doGet method and which will support you to create controllers .


NOTE : (1) You have asked to give classnames as per slides but hw 28-2 is on last page of slides and i started implementing it without going through next page , when i implemented half 28-2 and shuffle next page then i come to know you have asked for  TinyHttpd , HttpExchange classnames.  
So as per my system TinyHttpd = Server 
					HttpExchange =HttpExchangeRequest
					HttpHandler=Http1_1RequestHandler (chnages according  to protocol version)
					
	    

					

If  you didn't go through my video yet , i highly recommend you to go through this video in case of any error or exceptions . 
(http://screencast.com/t/AMKu314ar2)

Please let me know by any chance server won't work in your system . (shahadish@yahoo.com)
