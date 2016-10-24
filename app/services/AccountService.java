package services;

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

    public User getUserById(Long id) throws ServiceException {
        try{
            Statement st = conn.createStatement();
            String query = "SELECT * FROM users WHERE user_id = " + id;
            ResultSet rs = st.executeQuery(query);
            User tmp = new User(rs.getString("user_name"), rs.getString("user_fullname"), rs.getString("user_email"), rs.getString("user_password"));
            System.out.println(tmp.getFullName() + " " + tmp.getUserName());
            return tmp;
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

    public boolean createUser(DynamicForm form) throws ServiceException {
        User user = new User(form.get("username"), form.get("fullname"), form.get("email"), form.get("password"));
        /*if(!user.validate()){
            throw new ServiceException("Validation error");
        }*/

        try{

            Statement st = conn.createStatement();

            String statement = "VALUES ( '" + form.get("fullname") + "', '" + form.get("username") +  "'," + "'" + form.get("email") + "'," + "'" + form.get("password") + "'" + ")";

            // note that i'm leaving "date_created" out of this insert statement
            st.executeUpdate("INSERT INTO users (user_fullname, user_name,user_email,user_password)" + statement);

            //conn.close();
        }
        catch(Exception ex){
            System.out.println("sql error: " + ex.getMessage());
        }
        return true;
    }
}
