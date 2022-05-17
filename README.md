# Password Management System
This is a Database Mangement System project for your Password Management in daily life.\
We have implimented this using Java and PL/SQL.
We have used RSA and SHA256 encryptions for the protection of all the passwords stored in our Database.
Each seprate user gets their own RSA keys generated at the time of profile creation.
There are diffrent classes implimented for RSA and Table printings.
All the user logs are stored in a diffrent `Logs` table and can be access by either the server directly or by an admin user (default set as u_id=1, shall be implimented in a better way in future).

## Usage
Execute all files in `SQL` folder on the SQL Developer Server.
Once done, fill in the `user` , `password` and `url` in the `Main.java` file to connect to your server.
You can also change the `salt` which is default set to NULL.

Now Run and Enjoy :)

We have used a package for SHA256 which can be found [here](https://github.com/CruiserX/sha256_plsql).

## More Information on Project

![ERDiagram](https://cdn.discordapp.com/attachments/959084744098873374/976024985170751518/ER_dia_1_0_bleck.png)

The following functionalities of PL/SQL have been used and implimented in the project: 
* Functions
* Procedures
* Cursors
* Sequences
* Triggers
* Packages

Along with in Java:
* Javamail API
* Java Activation framework

You may download the latest version of both JavaMail API and JAF from the official website of Java. After successfully downloading these two, extract them. Add mail.jar , smtp.jar and activation.jar file in your classpath to be eligible to send emails.

> **A Detailed Doc for Project Submission can be accessed [here](https://docs.google.com/document/d/1pk1eIFU5mCQso2pmEC5o4vZciyNP3i1oU9aHJqCW1WQ/edit?usp=sharing).**

### Future Prospects
* Add `Admin:bool` column to `master_table`.
* Add Email verification on Create user.
* Forgot Password OTP expiration.
* New password shall not be same as old.
---

This Project was made by 

[Gaurav Pahwa](https://www.linkedin.com/in/gaurav-pahwa-44698418b/) and [Kunal Demla](https://www.linkedin.com/in/kunal-demla-a85116b1/)\
_(For Final Database Management Project, 2021-22)_