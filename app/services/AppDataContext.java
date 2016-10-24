package services;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Berglind on 24.10.2016.
 */
public class AppDataContext {

    Connection conn;
    public AppDataContext(){

        try{
            String myDriver = "com.mysql.jdbc.Driver";
            Class.forName(myDriver);

        }
        catch (ClassNotFoundException e){
            System.out.println("Class not found: " + e.getMessage());
        }

        try{
            String myUrl = "jdbc:mysql://localhost:3306/skil3?autoReconnect=true&useSSL=false";

            conn = DriverManager.getConnection(myUrl,"berglindoma13","Sjonvarp115");
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
