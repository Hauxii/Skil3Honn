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
            st.executeUpdate("UPDATE users SET user_fullname=" + user.get("fullname").toString() +",user_email =" + user.get("email").toString() + " WHERE user_name = " + nameOfUser);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public boolean addFavoriteVideo(long id, JsonNode video){
        try{
            Statement st = conn.createStatement();
            String statement = "VALUES ( "
                    + id
                    + ", "
                    + video.get("title")
                    +  ","
                    + video.get("url")
                    + ")";


            st.executeUpdate("INSERT INTO favoritevideos (user_id, video_title,video_url)" + statement);
            return true;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public boolean deleteFavoriteVideo(JsonNode video){
        try{
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE * FROM favoritevideos WHERE video_url = " + video.get("url").toString());
            return true;
        }
        catch(Exception ex){

        }
        return false;
    }

    public boolean addCloseFriend(JsonNode user){
        

        return false;
    }
}
