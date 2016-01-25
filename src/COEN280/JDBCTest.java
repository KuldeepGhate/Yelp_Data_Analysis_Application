package COEN280;

import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;

/**
 * Created by Kuldeep on 11/10/2015.
 */
public class JDBCTest {

    public static void main(String[] args) {

        String query = "SELECT EMPID, NAME, SALARY FROM EMPLOYEE";
        String query_insert = "INSERT INTO EMPLOYEE VALUES('3','SHABEENA',1000000)";

        try {
          /*1.Load the class drivers*/
            Class.forName("oracle.jdbc.driver.OracleDriver");

        /*2. Define the connection URL*/
            String host = "localhost";
            String dbName = "orcl";
            int port = 1521;
            String oracleURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;

        /*3.Establish the connection*/
            String username = "scott";
            String password = "tiger";
            Connection connection = DriverManager.getConnection(oracleURL, username, password);

        /*4.Create a statement*/
            Statement statement = connection.createStatement();

        /*5.Execute the statement*/
             statement.executeQuery(query_insert);
//
//            while(resultSet.next()){
//                System.out.println("Emp ID:" +resultSet.getString(1) + "Name: " + resultSet.getString(2)+ "Salary: " +resultSet.getInt(3));
//            }
           connection.close();
        }

        catch (SQLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
