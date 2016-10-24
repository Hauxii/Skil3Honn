package services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.User;
import play.api.libs.json.Json;

import play.data.DynamicForm;
import play.data.Form;
import scala.util.parsing.json.JSONArray;
import scala.util.parsing.json.JSONObject;
import sun.security.ec.ECDHKeyAgreement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lenny on 22.10.2016.
 */
public class AccountService extends AppDataContext{

    public JsonNode getUserById(Long id) throws ServiceException {
        JsonNodeFactory factory = new JsonNodeFactory(false);
        ObjectNode node = factory.objectNode();
        try{
            Statement st = conn.createStatement();
            String query = " SELECT * "
                    + " FROM users"
                    + " WHERE user_id = "
                    + id;
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                node.put("fullname",rs.getString("user_fullname"));
                node.put("username",rs.getString("user_name"));
                node.put("email",rs.getString("user_email"));
                node.put("password",rs.getString("user_password"));
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

    public boolean createUser(JsonNode user) throws ServiceException {
        User tmpUser = new User(user.get("username").toString(), user.get("fullname").toString(), user.get("email").toString(), user.get("password").toString());
        if(!tmpUser.validate()){
            System.out.println("Validation error");
            throw new ServiceException("Validation error");
        }

        try{

            Statement st = conn.createStatement();

            String statement = "VALUES ( "
                    + tmpUser.getFullName()
                    + ", "
                    + tmpUser.getUserName()
                    +  ","
                    + tmpUser.getEmail()
                    + ","
                    + tmpUser.getPassword()
                    + ")";

            // note that i'm leaving "date_created" out of this insert statement
            st.executeUpdate("INSERT INTO users (user_fullname, user_name,user_email,user_password)" + statement);
        }
        catch(Exception ex){
            System.out.println("sql error: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public void deleteUser(long id) throws ServiceException{
        try{
            Statement st = conn.createStatement();
            String statement = "DELETE * FROM users WHERE id = " + id;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            throw new ServiceException("Error deleting user: " + ex.getMessage());
        }

    }

    public void authenticateUser(JsonNode user) throws ServiceException{
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * from users WHERE user_name = " + user.get("username").toString());
            while(rs.next()){
                if(rs.getString("password") == user.get("password").toString()){
                    return;
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            throw new ServiceException("Error authenticating user: " + ex.getMessage());
        }
        throw new ServiceException("Username and password do not match");
    }

    public void changeUserPassword(JsonNode user) throws ServiceException{
        try{
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE users SET user_password = " + user.get("password").toString());
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            throw new ServiceException("Error changin user password: " + ex.getMessage());
        }
    }


}
