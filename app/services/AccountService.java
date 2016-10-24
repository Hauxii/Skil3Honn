package services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.User;
import play.data.DynamicForm;
import play.data.Form;
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
            node.put("fullname",rs.getString("user_fullname"));
            node.put("username",rs.getString("user_name"));
            node.put("email",rs.getString("user_email"));
            node.put("password",rs.getString("user_password"));
            return node;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        throw new ServiceException("User not found");
    }

    public List<User> getUsers(){
        List<User> _users = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users");

            while(rs.next()){
                User tmp = new User(rs.getString("user_name"), rs.getString("user_fullname"), rs.getString("user_email"), rs.getString("user_password"));
                _users.add(tmp);
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return _users;
    }

    public boolean createUser(JsonNode user) throws ServiceException {
        User tmpUser = new User(user.get("username").toString(), user.get("fullname").toString(), user.get("email").toString(), user.get("password").toString());
        if(!tmpUser.validate()){
            throw new ServiceException("Validation error");
        }

        try{

            Statement st = conn.createStatement();

            String statement = "VALUES ( '" + tmpUser.getFullName() + "', '" + tmpUser.getUserName() +  "'," + "'" + tmpUser.getEmail() + "'," + "'" + tmpUser.getPassword() + "'" + ")";

            // note that i'm leaving "date_created" out of this insert statement
            st.executeUpdate("INSERT INTO users (user_fullname, user_name,user_email,user_password)" + statement);
        }
        catch(Exception ex){
            System.out.println("sql error: " + ex.getMessage());
        }
        return true;
    }
}
