package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.User;

import java.sql.ResultSet;
import java.sql.Statement;


/**
 * Created by Lenny on 22.10.2016.
 */
public class AccountService extends AppDataContext{

    public JsonNode getUserByName(String username) throws ServiceException {
        JsonNodeFactory factory = new JsonNodeFactory(false);
        ObjectNode node = factory.objectNode();
        ArrayNode favoritevideos = factory.arrayNode();
        ArrayNode allfriends = factory.arrayNode();
        int videos[] = new int[100];
        int counter = 0;
        try{
            Statement st = conn.createStatement();

            String getuserid = "SELECT user_id FROM users WHERE user_name = " + username;
            ResultSet rs = st.executeQuery(getuserid);
            int user_id = 0;
            while(rs.next()){
                user_id = rs.getInt("user_id");
            }

            String query = " SELECT * "
                    + " FROM favoritevideos"
                    + " WHERE user_id = "
                    + user_id;
            ResultSet rs1 = st.executeQuery(query);

            while(rs1.next()){
                videos[counter++] = rs1.getInt("video_id");
            }
            for(int i = 0; i < counter; i++){
                ResultSet video = st.executeQuery("SELECT * FROM videos WHERE video_id = " + videos[i]);
                while(video.next()){
                    ObjectNode nodetmp = factory.objectNode();
                    nodetmp.put("title", video.getString("video_name"));
                    nodetmp.put("url", video.getString("video_url"));
                    favoritevideos.add(nodetmp);
                }
            }
            counter = 0;
            ResultSet getfriends = st.executeQuery("SELECT * FROM friends WHERE user_id = " + user_id);
            int friendlist[] = new int[100];
            while(getfriends.next()){
                videos[counter++] = getfriends.getInt("friend_id");
            }

            for(int j = 0; j < counter; j++){
                ResultSet friends = st.executeQuery("SELECT * FROM users WHERE user_id = " + videos[j]);
                while(friends.next()){
                    ObjectNode nodetmp = factory.objectNode();
                    nodetmp.put("name", friends.getString("user_fullname"));
                    nodetmp.put("email", friends.getString("user_email"));
                    allfriends.add(nodetmp);
                }
            }

            String query3 = " SELECT * "
                    + " FROM users"
                    + " WHERE user_id = "
                    + user_id;
            ResultSet rs2 = st.executeQuery(query3);

            while(rs2.next()){

                node.put("fullname",rs2.getString("user_fullname"));
                node.put("username",rs2.getString("user_name"));
                node.put("email",rs2.getString("user_email"));
                node.put("password",rs2.getString("user_password"));
                node.put("videos", favoritevideos);
                node.put("friends", allfriends);
            }

            return node;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            System.out.println("catching exception in getuserbyid service");
        }

        throw new ServiceException("User not found");
    }

    public ArrayNode getUsers(){
        JsonNodeFactory factory = new JsonNodeFactory(false);

        ArrayNode listOfUsers = factory.arrayNode();

        try {
            Statement st = conn.createStatement();
            String query = " SELECT * "
                    + " FROM users";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                ObjectNode node = factory.objectNode();
                node.put("fullname", rs.getString("user_fullname"));
                node.put("username", rs.getString("user_name"));
                node.put("email", rs.getString("user_email"));
                node.put("password", rs.getString("user_password"));
                listOfUsers.add(node);
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return listOfUsers;
    }

    //works
    public boolean createUser(JsonNode user) throws ServiceException {
        User tmpUser = new User(user.get("username").toString(), user.get("fullname").toString(), user.get("email").toString(), user.get("password").toString());
        if(!tmpUser.validate()){
            System.out.println("Validation error");
            throw new ServiceException("Validation error");
        }

        try{

            Statement st = conn.createStatement();

            String checkifuserexists = "SELECT user_name FROM users WHERE user_name = " + tmpUser.getUserName();
            ResultSet rs = st.executeQuery(checkifuserexists);

            if(!rs.next()){
                String statement = "VALUES ( "
                        + tmpUser.getFullName()
                        + ", "
                        + tmpUser.getUserName()
                        +  ","
                        + tmpUser.getEmail()
                        + ","
                        + tmpUser.getPassword()
                        + ")";


                st.executeUpdate("INSERT INTO users (user_fullname, user_name,user_email,user_password)" + statement);
            }
            else{
                System.out.println("User already exists");
                throw new ServiceException ("User already exists");
            }
        }
        catch(Exception ex){
            return false;
        }
        return true;
    }

    public void deleteUser(String username) throws ServiceException{
        try{
            Statement st = conn.createStatement();
            String get_userid = "SELECT user_id FROM users WHERE user_name = " + username;
            ResultSet rs = st.executeQuery(get_userid);
            int user_id = 0;
            while(rs.next()){
                user_id = rs.getInt("user_id");
            }
            String statement = "DELETE FROM users WHERE user_id = " + user_id;
            st.executeUpdate(statement);
        }
        catch(Exception ex){
            throw new ServiceException("Error deleting user: " + ex.getMessage());
        }

    }

    public void authenticateUser(JsonNode user) throws ServiceException{
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from users WHERE user_name = " + user.get("username").toString());
            while(rs.next()){
                if(rs.getString("user_password").equals(user.get("password").asText())){
                    return;
                }
            }
        }
        catch(Exception ex){
            throw new ServiceException("Error authenticating user: " + ex.getMessage());
        }
        throw new ServiceException("Username and password do not match");
    }

    public void changeUserPassword(String username, JsonNode user) throws ServiceException{
        try{
            Statement st = conn.createStatement();
            ResultSet getuserid = st.executeQuery("SELECT user_id FROM users WHERE user_name = " + username);
            int user_id = 0;
            while(getuserid.next()){
                user_id = getuserid.getInt("user_id");
            }
            st.executeUpdate("UPDATE users SET user_password = " + user.get("password").toString() + "WHERE user_id = " + user_id);
        }
        catch(Exception ex){
            throw new ServiceException("Error changin user password: " + ex.getMessage());
        }
    }


}
