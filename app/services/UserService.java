package services;

import com.fasterxml.jackson.databind.JsonNode;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Berglind on 24.10.2016.
 */
public class UserService extends AppDataContext{

    //works if you make sure to put username, fullname and email in the JSON that is sent in
    //question? should we make it so that you dont have to change or state all of them?
    public void updateProfile(long id, JsonNode user) throws ServiceException{

        try{
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE users "
                    +"SET user_fullname=" + user.get("fullname").toString()
                    +",user_name = " + user.get("username").toString()
                    +",user_email =" + user.get("email").toString()
                    +" WHERE user_id = " + id);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            throw new ServiceException("Error updating profile: " + ex.getMessage());
        }
    }

    //works
    public void addFavoriteVideo(long id, JsonNode video) throws ServiceException{
        try{
            Statement st = conn.createStatement();

            String getvideoid = "SELECT video_id FROM videos WHERE video_name = " + video.get("title");

            ResultSet videoid = st.executeQuery(getvideoid);
            int videoId = 0;
            while(videoid.next()){
                videoId = videoid.getInt("video_id");
            }

            String statement = "VALUES ( "
                    + id
                    + ", "
                    + videoId
                    + ")";


            st.executeUpdate("INSERT INTO favoritevideos (user_id, video_id)" + statement);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            throw new ServiceException("Error adding a favorite video: " + ex.getMessage());
        }
    }


    public void deleteFavoriteVideo(long id,JsonNode video) throws ServiceException{
        try{
            Statement st = conn.createStatement();

            st.executeUpdate("DELETE * FROM favoritevideos WHERE video_id = "
                    + video.get("video_id").toString()
                    + "AND user_id = " + id);
        }
        catch(Exception ex){
            throw new ServiceException("Error removing a favorite video: " + ex.getMessage());
        }
    }

    //works when JSON includes just the name of the friend
    public void addCloseFriend(long id, JsonNode user) throws ServiceException{
        try{
            Statement st = conn.createStatement();

            String getUserId = "SELECT user_id FROM users WHERE user_fullname = " + user.get("name").toString();
            ResultSet result = st.executeQuery(getUserId);
            int userid = 0;
            while(result.next()){
                userid = result.getInt("user_id");
            }
            String statement = "VALUES ( "
                    + id
                    + ", "
                    + userid
                    + ")";
            st.executeUpdate("INSERT INTO friends(user_id, friend_id)" + statement);
        }
        catch(Exception ex){
            throw new ServiceException("Error adding a close friend: " + ex.getMessage());
        }
    }
}
