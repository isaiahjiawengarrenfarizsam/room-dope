
package TestGit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author garrenl
 */
public class TestGit {

public static void main(String[] args)throws SQLException {
        showavailability();
        filterRooms1();
        //updateRooms(); //user input for room id
        //roomType(); //show room description
        //bookrooms(); //not working atm
    }
    public static void updateRooms() throws SQLException { //userinput
        try{
            //System.out.println("Please enter your Room ID");
        //Scanner in = new Scanner(System.in);
        //String RoomID = in.nextLine();
        
        String RoomID = "556";
        try(Connection mycon = DriverManager.getConnection("jdbc:mysql://localhost:3306/luckyhoteltest", "root", "")){
                    updateTable(mycon, RoomID); 
                    //showtable(mycon); //show or prove that table has been updated
            }
                    
      }
        catch(Exception e){ 
            System.out.println(e);
        }
    }
    public static void updateTable(Connection mycon, String RoomID) throws SQLException{ //update the actual table
                String cmd = "UPDATE room SET roomID = ?";
                try (PreparedStatement pstmt = mycon.prepareStatement(cmd)) {
                pstmt.setString(1, RoomID);
                pstmt.executeUpdate();
    }      
  }
    public static void showtable(Connection mycon) throws SQLException{// show the updated list
        
            Statement myst = mycon.createStatement();
            
            ResultSet myRs = myst.executeQuery("select * from room");
            
            while(myRs.next()){
                String roomid = myRs.getString(1);
                int roomtype = myRs.getInt(2);
                String adminid = myRs.getString(3);
                double price = myRs.getDouble(4);
                double rating = myRs.getDouble(5);
                
                System.out.printf("%s, %d, %s, %f, %f \n", roomid, roomtype, adminid, price, rating);
            }
    }
    
    public static void roomType() throws SQLException { //showfeatures
        
        try(Connection mycon = DriverManager.getConnection("jdbc:mysql://localhost:3306/luckyhoteltest", "root", "")){
        PreparedStatement pstmt = mycon.prepareStatement("select * from roomtype where roomID = ?");
            pstmt.setString(1, "556");
            ResultSet myRs = pstmt.executeQuery();
            myRs = pstmt.executeQuery();
                while(myRs.next()){
                    int roomtype = myRs.getInt(1);
                    String roomid = myRs.getString(2);
                    int bed = myRs.getInt(3);
                    int guests = myRs.getInt(4);
                    String desc = myRs.getString(5);
                    
                    System.out.printf("ROOM TYPE = %d \nNUMBER OF BEDS = %d \nNUMBER OF GUESTS = %d \nDESCRIPTION = %s\n", roomtype,bed, guests, desc);
                    
                    
                }
    }
        
    }
    public static void bookrooms() throws SQLException{ //didnt run foreign key check //kiv
        
        System.out.println("Book ur roomlabodo");
            
        System.out.println("Please enter your booking ID, ID, roomID, and transaction ID ");
        Scanner in = new Scanner(System.in);
        
        String bookID = in.nextLine();
        String custID = in.nextLine();
        String roomid = in.nextLine();
        int transid = in.nextInt();
        try(Connection mycon = DriverManager.getConnection("jdbc:mysql://localhost:3306/luckyhoteltest", "root", "")){
             Statement myst = mycon.createStatement();
                String cmd = "INSERT INTO `bookingrooms`(`bookingID`, `customerID`, `roomID`, `transactionID`) VALUES ('bookid','custid','roomid','transid')";
                    myst.executeUpdate(cmd);
         }
    }
    
    public static void showavailability() throws SQLException {
         try(Connection mycon = DriverManager.getConnection("jdbc:mysql://localhost:3306/luckyhoteltest", "root", "")){
             
             String cmd = "select * from room where roomStatus = ?";
             
             PreparedStatement pstmt = mycon.prepareStatement(cmd);
             pstmt.setInt(1, 1);
             ResultSet myRs = pstmt.executeQuery();
                System.out.printf("Available rooms are : \n");
                while(myRs.next()){
                    String roomnum = myRs.getString(1);
                    double roomprice = myRs.getDouble(3);
                    double rating = myRs.getDouble(4);
                    
                    
                    System.out.printf("Room Number = %s, Price = %.2f, Rating = %.2f\n", roomnum, roomprice, rating);
                }
         }
    }
    
    public static void filterRooms1() throws SQLException {
        
        Scanner in = new Scanner(System.in);
         
        try(Connection mycon = DriverManager.getConnection("jdbc:mysql://localhost:3306/luckyhoteltest", "root", "")){
        String cmd = "select * from room where roomPrice <= ? and averageRating >= ? and roomStatus = ?";
        
        System.out.println("Please enter your desired price range and average rating :"); //getting the user's filter
        System.out.println("Maximum price : ");
        double maxprice = in.nextDouble();
        System.out.println("Minimum rating");
        double minrating = in.nextDouble();
        ArrayList<String> validroom = new ArrayList<>();
        
       // int roomStatus = 1;
        
        PreparedStatement pstmt = mycon.prepareStatement(cmd); // setting the filter as parameters
        pstmt.setDouble(1, maxprice);
        pstmt.setDouble(2, minrating);
        pstmt.setInt(3, 1);
             ResultSet myRs = pstmt.executeQuery();
                System.out.printf("Available rooms are : \n");
                while(myRs.next()){
                    String roomnum = myRs.getString(1);
                    double roomprice = myRs.getDouble(3);
                    double rating = myRs.getDouble(4);
                    
                         validroom.add(roomnum); //inserting values into array list
                         
                    
                    
                    System.out.printf("Room Number = %s, Price = %.2f, Rating = %.2f\n", roomnum, roomprice, rating);
                        
                } displayvalid(validroom);
                    //filterRooms2(validroom);
        }
        
    }
    
    public static void displayvalid(ArrayList<String> validroom) throws SQLException { //test array list display

        try(Connection mycon = DriverManager.getConnection("jdbc:mysql://localhost:3306/luckyhoteltest", "root", "")){
            
            
            Scanner in = new Scanner(System.in);
            int guests;
            int beds;
            Object[] vroom = validroom.toArray();
            String cmd = "Select * from roomtype where amountOfGuest >= ? and amountOfBed >= ? and roomID in (";
            String temp = "";
            
            System.out.println("Please enter the Number of Guests you would like to have");
            guests = in.nextInt();
            System.out.println("Please enter the number of beds you would like to have");
            beds = in.nextInt();
            
            
            for(int i = 0; i < vroom.length; i++) {
            temp += ",?";
            }
            
            temp = temp.replaceFirst(",", "");
            temp += ")";
            cmd = cmd + temp;
            
            PreparedStatement pstmt = mycon.prepareStatement(cmd);
            
            String[] stringArray = Arrays.copyOf(vroom, vroom.length, String[].class);
            
            pstmt.setInt(1, guests);
            pstmt.setInt(2, beds);
            
            for(int i = 0; i < stringArray.length; i++){
            pstmt.setString(i+3, stringArray[i]);
            }
            
            ResultSet myRs = pstmt.executeQuery();
            
            while (myRs.next()){
                
                String roomid = myRs.getString(2);
                int nobed = myRs.getInt(3);
                int noguests = myRs.getInt(4);
                
                System.out.printf("Room number = %s, Number of beds = %d, Number of guests = %d\n", roomid, nobed, noguests);
            }
            
                         
            
            
           /* PreparedStatement pstmt = mycon.prepareStatement(cmd);
            Array array = mycon.createArrayOf("VARCHAR", validroom.toArray());
            
            pstmt.setArray(1, array);
            ResultSet myRs = pstmt.executeQuery();
            
            while (myRs.next()){
                
                String roomid = myRs.getString(2);
                int nobed = myRs.getInt(3);
                
                System.out.printf("Room number = %s, Number of beds = %d", roomid, nobed);
            
        }*/
        }
        
        
        
        /*for(String Available : validroom) {   //just display
      System.out.print(Available + ", ");
        }
        */
    }
    public static void filterRooms2 (ArrayList<String> validroom)throws SQLException {
        
        Scanner in = new Scanner(System.in);
        
        
        try(Connection mycon = DriverManager.getConnection("jdbc:mysql://localhost:3306/luckyhoteltest", "root", "")){
            System.out.println("Please enter desired number of beds");
                int bed = in.nextInt();
            System.out.println("Please enter desired number of guests");
                int guests = in.nextInt();
                
                    String cmd = "select * from roomtype where roomID = ? and AmountOfBed = ? and AmountOfGuest = ?";
                     PreparedStatement pstmt = mycon.prepareStatement(cmd);
                    for(String Available : validroom) { 
                    pstmt.setString(1, Available); 
                    pstmt.setInt(2, bed);
                    pstmt.setInt(3, guests);
                        ResultSet myRs = pstmt.executeQuery();
                        while(myRs.next()){
                  String roomnum = myRs.getString(2);
                  int nobed = myRs.getInt(3);
                  int noguest = myRs.getInt(4);
                  String description = myRs.getString(5);
                  
                    System.out.printf("Room number = %d, Number of bed = %d, Number of guests = %d,Description = %s", roomnum, nobed, noguest, description);
              
              
              }
                        
                    }
                    /*pstmt.setInt(2, bed);
                    pstmt.setInt(3, guests);
                        ResultSet myRs = pstmt.executeQuery(); */
              
              /*while(myRs.next()){
                  String roomnum = myRs.getString(2);
                  int nobed = myRs.getInt(3);
                  int noguest = myRs.getInt(4);
                  String description = myRs.getString(5);
                  
                    System.out.printf("Room number = %d, Number of bed = %d, Number of guests = %d,Description = %s", roomnum, nobed, noguest, description);
              
              
              }*/
             }
            }
    
}
