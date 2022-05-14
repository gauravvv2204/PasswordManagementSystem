package com.gaurav;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        try {
            FastReader read = new FastReader();
            int logged_in_id = -1;
            int choice = -1;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@172.16.64.222:1522/ORCLPDB", "CS2003087", "GAURAV");
            // Statement stmt=con.createStatement();
            // ResultSet rs=stmt.executeQuery("select * from MASTER_TABLE");
            // while(rs.next())
            // System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3));
            // con.close();
            while (true) {
                if (logged_in_id == -1) {
                    System.out.println("Please choose a option:");
                    System.out.println("1. Create New Account");
                    System.out.println("2. Login");
                    System.out.println("3. Forgot Password");
                    System.out.println("4. Exit.");
                    choice = read.nextInt();
                    if (choice == 1) {
                        String email, password;
                        System.out.println("Enter your EmailId");
                        email = read.nextLine();
                        System.out.println("Enter your Password");
                        password = read.nextLine();
                        CallableStatement stmt = con.prepareCall("{?= call create_user(?,?)}");
                        stmt.setString(2, email);
                        stmt.setString(3, password);
                        stmt.registerOutParameter(1, Types.INTEGER);
                        stmt.execute();
                        int reply = stmt.getInt(1);
                        if (reply == 1) {
                            System.out.println("User created Successfully");
                        } else if (reply == -1) {
                            System.out.println("Account already exists.Log in or Please use different emaiiId.");
                        } else if (reply == -2) {
                            System.out.println("Enter valid email id.");
                        } else if (reply == -3) {
                            System.out.println(
                                    "Password should consist of alphabets,numbers,special charaters and should have a length greater than or equal to 8");
                        }
                    } else if (choice == 2) {
                        String email, password;
                        System.out.println("Enter your EmailId");
                        email = read.nextLine();
                        System.out.println("Enter your Password");
                        password = read.nextLine();
                        CallableStatement stmt = con.prepareCall("{?= call login(?,?)}");
                        stmt.setString(2, email);
                        stmt.setString(3, password);
                        stmt.registerOutParameter(1, Types.INTEGER);
                        stmt.execute();
                        int reply = stmt.getInt(1);
                        if (reply > 0) {
                            logged_in_id = reply;
                            System.out.println("You have successfully logged in.");
                        } else {
                            System.out.println("Incorrect email/password.");
                        }

                    } else if (choice == 3) {
                        System.out.println("Please enter your email address");
                        String recipient = read.nextLine();
                        CallableStatement stmt1 = con.prepareCall("{call isPresent(?,?)}");
                        stmt1.setString(1, recipient);
                        stmt1.registerOutParameter(2, Types.VARCHAR);
                        stmt1.execute();
                        String result = stmt1.getString(2);
                        if (result.equals("FALSE")) {
                            System.out.println("Email doesn't exist");
                            continue;
                        }
                        // email ID of Sender.
                        String sender = "passmanagerdbmsproject@gmail.com";

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

                                return new PasswordAuthentication("passmanagerdbmsproject@gmail.com", "hello@123");

                            }

                        });

                        try {
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
                            int b = (int) (Math.random() * (max - min + 1) + min);
                            message.setText("Your OTP is: " + b);
                            // Send email.
                            Transport.send(message);
                            System.out.println("Mail successfully sent");
                            int getOtp = read.nextInt();
                            if (getOtp == b) {
                                // Update Password option
                                String newpass;
                                System.out.println("You may update your password");
                                while (true) {
                                    System.out.println("Enter your new password:");
                                    newpass = read.nextLine();
                                    CallableStatement stmt = con.prepareCall("{call update_mpassword(?,?,?)}");
                                    stmt.setString(1, recipient);
                                    stmt.setString(2, newpass);
                                    stmt.registerOutParameter(3, Types.VARCHAR);
                                    stmt.execute();
                                    String result1 = stmt.getString(3);
                                    if (result1.equals("FALSE")) {
                                        System.out.println(
                                                "Password should consist of alphabets,numbers,special charaters and should have a length greater than or equal to 8");
                                        continue;
                                    } else {
                                        System.out.println("Password Updated");
                                        break;
                                    }
                                }
                            } else {
                                System.out.println("Wrong Otp");
                            }
                        } catch (MessagingException mex) {
                            mex.printStackTrace();
                        }
                    } else {
                        System.out.println("Good Bye! 👋👋");
                        break;
                    }

                } else {
                    System.out.println("Please choose a option:");
                    System.out.println("0. Show Profile");
                    System.out.println("1. Update Profile Settings.");
                    System.out.println("2. Add/Update Password");
                    System.out.println("3. Show Passwords");
                    System.out.println("4. Logout");
                    choice = read.nextInt();
                    if (choice == 0) {
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM user_details where u_id=" + logged_in_id);
                        DBTablePrinter.printResultSet(rs);
                        CallableStatement callstmt = con.prepareCall("{call getMobileNo(?,?)}");
                        callstmt.setInt(1, logged_in_id);
                        callstmt.registerOutParameter(2, Types.REF_CURSOR);
                        callstmt.execute();
                        System.out.println("You have following mobile numbers associated with you.");
                        DBTablePrinter.printResultSet((ResultSet) callstmt.getObject(2));
                    } else if (choice == 1) {
                        while (true) {
                            System.out.println("Please choose a option: ");
                            System.out.println("1. Update Name");
                            System.out.println("2. Add/Delete Mobile Number");
                            System.out.println("3. Update DOB");
                            System.out.println("4. Update Gender");
                            System.out.println("5. Update Address");
                            System.out.println("6. Go back.");
                            choice = read.nextInt();
                            if (choice == 1) {
                                String fname, lname;
                                System.out.println("Enter your First Name");
                                fname = read.nextLine();
                                System.out.println("Enter your Last Name");
                                lname = read.nextLine();
                                CallableStatement stmt = con.prepareCall("{call update_name(?,?,?)}");
                                stmt.setInt(1, logged_in_id);
                                stmt.setString(2, fname);
                                stmt.setString(3, lname);
                                stmt.execute();
                                System.out.println("Name update successfully");
                            } else if (choice == 2) {
                                System.out.println("1. Add Number");
                                System.out.println("2. Delete Number");
                                choice = read.nextInt();
                                if (choice == 1) {
                                    long mno;
                                    System.out.println("Enter your Mobile Number:");
                                    mno = read.nextLong();
                                    CallableStatement stmt = con.prepareCall("{call add_mno(?,?)}");
                                    stmt.setInt(1, logged_in_id);
                                    stmt.setLong(2, mno);
                                    stmt.execute();
                                    System.out.println("Mobile number added Successfully!");
                                } else {
                                    long mno;
                                    System.out.println("Enter your Mobile Number:");
                                    mno = read.nextLong();
                                    CallableStatement stmt = con.prepareCall("{call del_mno(?,?)}");
                                    stmt.setInt(1, logged_in_id);
                                    stmt.setLong(2, mno);
                                    stmt.execute();
                                    System.out.println("Mobile number deleted Successfully");
                                }
                            } else if (choice == 3) {
                                String DoB;
                                System.out.println("Enter your Date of Birth (DD-MON-YYYY format)");
                                DoB = read.nextLine();
                                CallableStatement stmt = con.prepareCall("{call update_dob(?,?)}");
                                stmt.setInt(1, logged_in_id);
                                stmt.setString(2, DoB);
                                stmt.execute();
                                System.out.println("Date of Birth Updated!");
                            } else if (choice == 4) {
                                int gchoice;
                                String gender;
                                System.out.println("Please select your preferred Gender:");
                                System.out.println("1. Male");
                                System.out.println("2. Female");
                                System.out.println("3. Others");
                                gchoice = read.nextInt();
                                if (gchoice == 1)
                                    gender = "M";
                                else if (gchoice == 2)
                                    gender = "F";
                                else
                                    gender = "O";
                                CallableStatement stmt = con.prepareCall("{call update_gender(?,?)}");
                                stmt.setInt(1, logged_in_id);
                                stmt.setString(2, gender);
                                stmt.execute();
                                System.out.println("Gender Updated Successfully");
                            } else if (choice == 5) {
                                String Hno, Area, City, State, Country;
                                System.out.println("Please Enter your House number:");
                                Hno = read.nextLine();
                                System.out.println("Enter your Area/Locality:");
                                Area = read.nextLine();
                                System.out.println("Enter your City");
                                City = read.nextLine();
                                System.out.println("Enter your State");
                                State = read.nextLine();
                                System.out.println("Enter your Country");
                                Country = read.nextLine();
                                CallableStatement stmt = con.prepareCall("{call update_address(?,?,?,?,?,?)}");
                                stmt.setInt(1, logged_in_id);
                                stmt.setString(2, Hno);
                                stmt.setString(3, Area);
                                stmt.setString(4, City);
                                stmt.setString(5, State);
                                stmt.setString(6, Country);
                                stmt.execute();
                                System.out.println("Address Updated Successfully.");
                            } else {
                                break;
                            }
                        }
                    } else if (choice == 2) {
                        RSA helper = new RSA();
                        String password = read.nextLine();
                        BigInteger test = encrypt(password, helper.getpublickey(), helper.getmodulus());
                        System.out.println(test);
                        String ans = decrypt(test, helper.getprivatekey(), helper.getmodulus());
                        System.out.println(ans);
                    } else if (choice == 3) {

                    } else {
                        logged_in_id = -1;
                        continue;
                    }

                }

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(
                    new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    static BigInteger encrypt(String s, BigInteger publicKey, BigInteger modulus) {
        byte[] bytes = s.getBytes();
        BigInteger message = new BigInteger(bytes);
        return message.modPow(publicKey, modulus);
    }

    static String decrypt(BigInteger encrypted, BigInteger privateKey, BigInteger modulus) {
        BigInteger storage = encrypted.modPow(privateKey, modulus);
        byte[] bytes = storage.toByteArray();
        String ans = new String(bytes);
        return ans;
    }
}

public class RSA {
    private BigInteger P;
    private BigInteger Q;
    private BigInteger N;
    private BigInteger PHI;
    private BigInteger e;
    private BigInteger d;
    private int maxLength = 1024;
    private Random R;

    public RSA() {
        R = new Random();
        P = BigInteger.probablePrime(maxLength, R);
        Q = BigInteger.probablePrime(maxLength, R);
        N = P.multiply(Q);
        PHI = P.subtract(BigInteger.ONE).multiply(Q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(maxLength / 2, R);
        while (PHI.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(PHI) < 0) {
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(PHI);
    }

    public BigInteger getpublickey() {
        return e;
    }

    public BigInteger getprivatekey() {
        return d;
    }

    public BigInteger getmodulus() {
        return N;
    }
}
