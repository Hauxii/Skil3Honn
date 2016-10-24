package services;

import domain.User;
import scala.App;
import scala.util.parsing.json.JSON;
import scala.util.parsing.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Berglind on 24.10.2016.
 */
public class UserService extends AppDataContext{

    public void updateProfile(User user2, JSONObject user){
        
        String nameOfUser = user2.getUserName();
        try{
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE users SET user_fullname=" + user2.getFullName()+",user_email =" + user2.getEmail() + ",user_password =" + user2.getPassword() + " WHERE user_name = " + nameOfUser);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
