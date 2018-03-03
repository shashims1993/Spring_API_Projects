## Seed code - Boilerplate for step 2 - Activity Stream Assignment
### Assignment Step Description
In this Case Study, we will create an Activity Stream Application which accepts Sender's name and Message as input from the front end and displays the same along with the timestamp posting in a reverse chronological order (latest message first). 

Even though functionality-wise and the output of step-1 and step-2 are same, 
 1. In step-1 we created a monolithic application but in this step-2, we will use proper annotations like @Component, @Repository,  @Autowired etc.,  
 2. In this step-2, we should not use "new"  keyword to create an instance.  
 3. In step-1 Hibernate configuration was in the xml file but in Step-2 it is in java class file.
 4. Hibernate session factory should be created automatically instead of getting it from HibernateUtil.

In this case study, we are defining the beans related to hibernate from inside **ApplicationContextConfig** class only. Hence hibernate-cfg.xml file and HibernateUtil class are no more required in this step.

Here we will have a **Message** class which will be acting as the data model for message table in the database. Please note that this class is annotated with **@Entity annotation** (**The @Entity annotation marks the class as an entity bean, so it must have a no-argument constructor that is visible with an at least protected scope**), 
where Hibernate will scan all the packages for any Java objects annotated with the @Entity annotation. 
If it finds any, then it will begin the process of looking through that particular Java object to recreate it as a table in your database. 

### Problem Statement
In this case study: Activity Stream Step 2 we will create an application that requires us to implement two functionalities. They are as follows:
1. Get all messages from Database and display in HTML form.
2. Send and receive the message.

### Steps to be followed:

    Step 1: Clone the boilerplate in a specific folder in your local machine and import the same in your eclipse STS.
    Step 2: Add relevant dependencies in pom.xml file. 
        Note: Read the comments mentioned in pom.xml file for identifying the relevant dependencies.
    Step 3: Create Environment Variable thru script file (.sh for Linux & .bat for Windows) (refer to **How to create Environment variables?** section below)
    Step 4:Implement ApplicationContextConfig.java 
    Step 5: Specify Root config class in WebApplicationInitializer.java file.
    Step 6: Define the Message.java (Model class)
    Step 7: See the methods in MessageDAO interface.
    Step 8: Implement the MessageDAOImpl.
    Step 9: Run the JUnit testcases for MessageDAOImpl.java (MessageDAOImplTest.java)
    Step 10: Implement MessageController.java 
    Step 11: Run the MockMVCTest cases for MessageController.java (MessageControllerTest.java)
    Step 12: Design a form in index.jsp file
    Step 13: Run the application on configured web server.

### How to create Environment variables?

System variables can be created for Windows and Unix/Linux in the following ways
    
1. Windows environment 

        a) Create env-variables.bat file in the project root folder 
        (actually you can give any meaning full name anywhere, but for hobbes you need to follow this specific folder and name 
        b) Give the following 
                  setx -m MYSQL_DATABASE "activitystream_step2" 
                  setx -m MYSQL_USER "root" 
                  setx -m MYSQL_PASSWORD "root" 
                  setx -m MYSQL_HOST "localhost" 
        c) Navigate the this specific file in windows and run it as administrator
      
2. Unix/Lunix environment (And for Hobbes submission) 

        a) Create env-variables.sh (instead of .bat file)
        b) Give the following 
                  export MYSQL_DATABASE="activitystream_step2" 
                  export MYSQL_USER="root" 
                  export MYSQL_PASSWORD="root" 
                  export MYSQL_HOST="localhost" 
        c) Navigate to this specific file through command prompt and run
      
Note: Exit eclipse and restart (if the eclipse is open) if you not exit and restart, the newly created environment variables will not be available and 
hence you will get exception.

### Project structure

The folders and files you see in this repositories, is how it is expected to be in projects, which are submitted for automated evaluation by Hobbes

    Project
    |
    ├──src/main
	|	    └── com.stackroute.activitystream.config	           
	|   	        └── ApplicationContextConfig.java           // This class will contain the application-context for the application.
	|	            └── WebApplicationInitializer.java          // This class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer class.
	|	    └── com.stackroute.activitystream.controller
	|		        └── MessageController.java 	                    // This class is used to control all the transactions with the database.	               
	|	    └── com.stackroute.activitystream.dao
	|		        └── MessageDAO.java                         // An interface that provides access to an underlying database (Message) or any other persistence storage.
	|	    └── com.stackroute.activitystream.daoimpl
	|		        └── MessageDAOImpl.java                     // This class is implementing the MessageDAO interface. This class has to be annotated with @Repository annotation.
	|	    └── com.stackroute.activitystream.model
	|		        └── Message.java                            // The class will be acting as the data model for the message Table in the database.
	|	    └── webapp/WEB-INF/views
    |               └── index.jsp                               // A JSP page with a form in it, which will have textboxes for Sender Name and Message content along with a Send Submit button. 
    |
    ├──src/test/java/com/stackroute/activitystream/test      // All your test cases are written using JUnit, these test cases can be run by selecting Run As -> JUnit Test
    |           └── MessageControllerTest.java      
    |           └── MessageDAOTest.java             
    ├── .settings
	├── .classpath			                    // This file is generated automatically while creating the project in eclipse
	├── .hobbes   			                    // Hobbes specific config options, such as type of evaluation schema, type of tech stack etc., Have saved a default values for convenience
	├── .project			                    // This is automatically generated by eclipse, if this file is removed your eclipse will not recognize this as your eclipse project. 
	├── pom.xml 			                    // This is a default file generated by maven, if this file is removed your project will not get recognised in hobbes.
	└── PROBLEM.md  		                    // This files describes the problem of the assignment/project, you can provide as much as information and clarification you want about the project in this file

> PS: All lint rule files are by default copied during the evaluation process, however if need to be customizing, you should copy from this repo and modify in your project repo


#### To use this as a boilerplate for your new project, you can follow these steps

1. Clone the base boilerplate in the folder **assignment-solution-step2** of your local machine
     
    `git clone https://gitlab-wd.stackroute.in/stack_java_activitystream/activitystream-step2-boilerplate.git assignment-solution-step2`

2. Navigate to assignment-solution-step2 folder

    `cd assignment-solution-step2`

3. Remove its remote or original reference

     `git remote rm origin`

4. Create a new repo in gitlab named `assignment-solution-step2` as private repo

5. Add your new repository reference as remote

     `git remote add origin https://gitlab-wd.stackroute.in/{{yourusername}}/assignment-solution-step2.git`

     **Note: {{yourusername}} should be replaced by your username from gitlab**

5. Check the status of your repo 
     
     `git status`

6. Use the following command to update the index using the current content found in the working tree, to prepare the content staged for the next commit.

     `git add .`
 
7. Commit and Push the project to git

     `git commit -a -m "Initial commit | or place your comments according to your need"`

     `git push -u origin master`

8. Check on the git repo online, if the files have been pushed

### Important instructions for Participants
> - We expect you to write the assignment on your own by following through the guidelines, learning plan, and the practice exercises
> - The code must not be plagirized, the mentors will randomly pick the submissions and may ask you to explain the solution
> - The code must be properly indented, code structure maintained as per the boilerplate and properly commented
> - Follow through the problem statement shared with you

### Further Instructions on Release

*** Release 0.1.0 ***

- Right click on the Assignment select Run As -> Run on Server to run your Assignment.
- Right click on the Assignment select Run As -> JUnit Test to run your Assignment.