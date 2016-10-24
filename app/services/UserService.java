package services;

import com.fasterxml.jackson.databind.JsonNode;
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

    public void updateProfile(JsonNode user){

        String nameOfUser = user.get("username").toString();
        try{
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE users SET user_fullname=" + user.get("fullname").toString() +",user_email =" + user.get("email").toString() + ",user_password =" + user.get("password").toString() + " WHERE user_name = " + nameOfUser);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
