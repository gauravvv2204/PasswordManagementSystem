package com.gaurav;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) {
        try{
            FastReader read = new FastReader();
            int logged_in_id = -1;
            int choice = -1;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(
                    "jdbc:oracle:thin:@172.16.64.222:1522/ORCLPDB","CS2003087","GAURAV");
//        Statement stmt=con.createStatement();
//        ResultSet rs=stmt.executeQuery("select * from MASTER_TABLE");
//        while(rs.next())
//            System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
//        con.close();
            while(true){
                if(logged_in_id==-1){
                    System.out.println("Please choose a option:");
                    System.out.println("1. Create New Account");
                    System.out.println("2. Login");
                    System.out.println("3. Forgot Password");
                    System.out.println("4. Exit.");
                    choice = read.nextInt();
                    if(choice==1){
                        String email,password;
                        System.out.println("Enter your EmailId");
                        email = read.nextLine();
                        System.out.println("Enter your Password");
                        password = read.nextLine();
                        CallableStatement stmt=con.prepareCall("{?= call create_user(?,?)}");
                        stmt.setString(2,email);
                        stmt.setString(3,password);
                        stmt.registerOutParameter(1,Types.INTEGER);
                        stmt.execute();
                        int reply = stmt.getInt(1);
                        if(reply==1){
                            System.out.println("User created Successfully");
                        }
                        else if(reply==-1){
                            System.out.println("Account already exists.Log in or Please use different emaiiId.");
                        }
                        else if(reply==-2){
                            System.out.println("Enter valid email id.");
                        }
                        else if(reply==-3) {
                            System.out.println("Password should consist of alphabets,numbers,special charaters and should have a length greater than or equal to 8");
                        }
                    }
                    else if(choice==2){
                        String email,password;
                        System.out.println("Enter your EmailId");
                        email = read.nextLine();
                        System.out.println("Enter your Password");
                        password = read.nextLine();
                        CallableStatement stmt=con.prepareCall("{?= call login(?,?)}");
                        stmt.setString(2,email);
                        stmt.setString(3,password);
                        stmt.registerOutParameter(1,Types.INTEGER);
                        stmt.execute();
                        int reply = stmt.getInt(1);
                        if(reply>0){
                            logged_in_id = reply;
                            System.out.println("You have successfully logged in.");
                        }
                        else{
                            System.out.println("Incorrect email/password.");
                        }

                    }
                    else if(choice == 3){
                        System.out.println("Please enter your email address");
                        String recipient = read.nextLine();
                        CallableStatement stmt=con.prepareCall("{call isPresent(?,?)}");
                        stmt.setString(1,recipient);
                        stmt.registerOutParameter(2, Types.VARCHAR);
                        stmt.execute();
                        String result = stmt.getString(2);
                        if(result.equals("FALSE")){
                            System.out.println("Email doesn't exist");
                            continue;
                        }
                        // email ID of  Sender.
                        String sender = "gauravpahwa2204@gmail.com";

                        // using host as localhost
                        String host = "smtp.gmail.com";

                        // Getting system properties
                        Properties properties = System.getProperties();

                        // Setting up mail server
                        properties.setProperty("mail.smtp.host", host);
                        properties.put("mail.smtp.port", "465");
                        properties.put("mail.smtp.ssl.enable", "true");
                        properties.put("mail.smtp.auth", "true");

                        // Get the Session object.// and pass username and password
                        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

                            protected PasswordAuthentication getPasswordAuthentication() {

                                return new PasswordAuthentication("gpahwa_be20@thapar.edu", "uwkuqojabdamywff");

                            }

                        });

                        try
                        {
                            // MimeMessage object.
                            MimeMessage message = new MimeMessage(session);

                            // Set From Field: adding senders email to from field.
                            message.setFrom(new InternetAddress(sender));

                            // Set To Field: adding recipient's email to from field.
                            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

                            // Set Subject: subject of the email
                            message.setSubject("OTP");

                            // set body of the email.
                            int max = 999999;
                            int min = 100000;
                            int b = (int)(Math.random()*(max-min+1)+min);
                            message.setText("Your otp is: "+b);
                            // Send email.
                            Transport.send(message);
                            System.out.println("Mail successfully sent");
                            int getOtp = read.nextInt();
                            if(getOtp==b){
                                //Update Password option
                                System.out.println("You may update your password");
                            }
                            else{
                                System.out.println("Wrong Otp");
                            }
                        }
                        catch (MessagingException mex)
                        {
                            mex.printStackTrace();
                        }
                    }
                    else{
                        System.out.println("Good Bye! 👋👋");
                        break;
                    }

                }
                else{
                    System.out.println("Please choose a option:");
                    System.out.println("1. Update Profile Settings.");
                    System.out.println("2. Add/Update Password");
                    System.out.println("3. Show Passwords");
                    System.out.println("4. Logout");
                    choice = read.nextInt();
                    if(choice == 1){
                        while(true){
                            System.out.println("Please choose a option: ");
                            System.out.println("1. Update Name");
                            System.out.println("2. Add/Delete Mobile Number");
                            System.out.println("3. Update DOB");
                            System.out.println("4. Update Gender");
                            System.out.println("5. Update Address");
                            System.out.println("6. Display Profile");
                            System.out.println("7. Go back.");
                            choice = read.nextInt();
                            if(choice==1){
                                String fname,lname;
                                System.out.println("Enter your First Name");
                                fname = read.nextLine();
                                System.out.println("Enter your Last Name");
                                lname = read.nextLine();
                                CallableStatement stmt=con.prepareCall("{call update_name(?,?,?)}");
                                stmt.setInt(1,logged_in_id);
                                stmt.setString(2,fname);
                                stmt.setString(3,lname);
                                stmt.execute();
                                System.out.println("Name update successfully");
                            }
                            else if(choice==2)
                            {
                                System.out.println("1. Add Number");
                                System.out.println("2. Delete Number");
                                choice=read.nextInt();
                                if(choice==1)
                                {
                                    long mno;
                                    System.out.println("Enter your Mobile Number:");
                                    mno=read.nextLong();
                                    CallableStatement stmt=con.prepareCall("{call add_mno(?,?)}");
                                    stmt.setInt(1,logged_in_id);
                                    stmt.setLong(2,mno);
                                    stmt.execute();
                                    System.out.println("Mobile number added Successfully!");
                                }
                                else
                                {
                                    long mno;
                                    System.out.println("Enter your Mobile Number:");
                                    mno=read.nextLong();
                                    CallableStatement stmt=con.prepareCall("{call del_mno(?,?)}");
                                    stmt.setInt(1,logged_in_id);
                                    stmt.setLong(2,mno);
                                    stmt.execute();
                                    System.out.println("Mobile number deleted Successfully");
                                }
                            }
                            else if(choice==3)
                            {
                                String DoB;
                                System.out.println("Enter your Date of Birth (DD-MON-YYYY format)");
                                DoB=read.nextLine();
                                CallableStatement stmt=con.prepareCall("{call update_dob(?,?)}");
                                stmt.setInt(1,logged_in_id);
                                stmt.setString(2,DoB);
                                stmt.execute();
                                System.out.println("Date of Birth Updated!");
                            }
                            else if(choice==4)
                            {
                                int gchoice;
                                String gender;
                                System.out.println("Please select your preferred Gender:");
                                System.out.println("1. Male");
                                System.out.println("2. Female");
                                System.out.println("3. Others");
                                gchoice=read.nextInt();
                                if(gchoice==1)
                                    gender="M";
                                else if(gchoice==2)
                                    gender="F";
                                else
                                    gender="O";
                                CallableStatement stmt=con.prepareCall("{call update_gender(?,?)}");
                                stmt.setInt(1,logged_in_id);
                                stmt.setString(2,gender);
                                stmt.execute();
                                System.out.println("Gender Updated Successfully");
                            }
                            else if(choice==5)
                            {
                                String Hno,Area,City,State,Country;
                                System.out.println("Please Enter your House number:");
                                Hno=read.nextLine();
                                System.out.println("Enter your Area/Locality:");
                                Area= read.nextLine();
                                System.out.println("Enter your City");
                                City=read.nextLine();
                                System.out.println("Enter your State");
                                State=read.nextLine();
                                System.out.println("Enter your Country");
                                Country=read.nextLine();
                                CallableStatement stmt=con.prepareCall("{call update_address(?,?,?,?,?,?)}");
                                stmt.setInt(1,logged_in_id);
                                stmt.setString(2,Hno);
                                stmt.setString(3,Area);
                                stmt.setString(4,City);
                                stmt.setString(5,State);
                                stmt.setString(6,Country);
                                stmt.execute();
                                System.out.println("Address Updated Successfully.");
                            }
                            else if(choice==6)
                            {
                                
                            }
                            else{
                                break;
                            }
                        }
                    }
                    else if(choice==2){

                    }
                    else if(choice==3){

                    }
                    else{
                        logged_in_id = -1;
                        continue;
                    }

                }


            }

        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader()
        {
            br = new BufferedReader(
                    new InputStreamReader(System.in));
        }

        String next()
        {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() { return Integer.parseInt(next()); }

        long nextLong() { return Long.parseLong(next()); }

        double nextDouble()
        {
            return Double.parseDouble(next());
        }

        String nextLine()
        {
            String str = "";
            try {
                str = br.readLine();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }
}



