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
    public void updateProfile(String username, JsonNode user) throws ServiceException{

        try{
            Statement st = conn.createStatement();

            ResultSet getuserid = st.executeQuery("SELECT user_id FROM users WHERE user_name = " + username);
            int user_id = 0;
            while(getuserid.next()){
                user_id = getuserid.getInt("user_id");
            }

            if(isauthenticated(username)) {

                st.executeUpdate("UPDATE users "
                        + "SET user_fullname=" + user.get("fullname").toString()
                        + ",user_name = " + user.get("username").toString()
                        + ",user_email =" + user.get("email").toString()
                        + " WHERE user_id = " + user_id);
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            throw new ServiceException("Error updating profile: " + ex.getMessage());
        }
    }

    //works
    public void addFavoriteVideo(String username, JsonNode video) throws ServiceException{
        try{
            Statement st = conn.createStatement();

            ResultSet getuserid = st.executeQuery("SELECT user_id FROM users WHERE user_name = " + username);
            int user_id = 0;
            while(getuserid.next()){
                user_id = getuserid.getInt("user_id");
            }

            //check if authenticated
            if(isauthenticated(username)) {

                String getvideoid = "SELECT video_id FROM videos WHERE video_name = " + video.get("title");

                ResultSet videoid = st.executeQuery(getvideoid);
                int videoId = 0;
                while (videoid.next()) {
                    videoId = videoid.getInt("video_id");
                }

                String statement = "VALUES ( "
                        + user_id
                        + ", "
                        + videoId
                        + ")";


                st.executeUpdate("INSERT INTO favoritevideos (user_id, video_id)" + statement);
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            throw new ServiceException("Error adding a favorite video: " + ex.getMessage());
        }
    }


    public void deleteFavoriteVideo(String username,JsonNode video) throws ServiceException{
        try{
            Statement st = conn.createStatement();

            ResultSet getuserid = st.executeQuery("SELECT user_id FROM users WHERE user_name = " + username);
            int user_id = 0;
            while(getuserid.next()){
                user_id = getuserid.getInt("user_id");
            }

            if(isauthenticated(username)) {

                st.executeUpdate("DELETE * FROM favoritevideos WHERE video_id = "
                        + video.get("video_id").toString()
                        + "AND user_id = " + user_id);
            }
        }
        catch(Exception ex){
            throw new ServiceException("Error removing a favorite video: " + ex.getMessage());
        }
    }

    //works when JSON includes just the fullname of the friend
    public void addCloseFriend(String username, JsonNode friend) throws ServiceException{
        try{
            Statement st = conn.createStatement();

            String query = "SELECT user_id FROM users WHERE user_name = " + "'" + username + "'";
            System.out.println(query);
            ResultSet getuserid = st.executeQuery(query);

            int user_id = 0;
            while(getuserid.next()){
                user_id = getuserid.getInt("user_id");
            }

            if(isauthenticated(username)) {

                String getUserId = "SELECT user_id FROM users WHERE user_fullname = " + friend.get("name").toString();
                ResultSet result = st.executeQuery(getUserId);
                int friendId = 0;
                while (result.next()) {
                    friendId = result.getInt("user_id");
                }
                String statement = "VALUES ( "
                        + user_id
                        + ", "
                        + friendId
                        + ")";
                st.executeUpdate("INSERT INTO friends(user_id, friend_id)" + statement);
            }
        }
        catch(Exception ex){
            throw new ServiceException("Error adding a close friend: " + ex.getMessage());
        }
    }

    public boolean isauthenticated(String user_name){
        try{
            Statement st = conn.createStatement();
            ResultSet authenticated = st.executeQuery("SELECT authenticated FROM users WHERE user_name = "  + user_name);
            String check = "";
            while(authenticated.next()){
                check = authenticated.getString("authenticated");
            }
            if(check.equals("TRUE")){
                return true;
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }
}
