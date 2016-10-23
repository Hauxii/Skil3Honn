package services;

import domain.User;
import play.data.DynamicForm;
import play.data.Form;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenny on 22.10.2016.
 */
public class UserService {

    public List<User> _users;

    public UserService(){
        this._users = new ArrayList<>();
        _users.add(new User(0,"Hauxii", "Haukur Ingi", "haukura14@ru.is", "password"));
    }

    public User getUserById(Long id) throws ServiceException {
        for (User user : _users) {
            if(user.ID == id){
                return user;
            }
        }

        throw new ServiceException("User not found");

    }

    public List<User> getUsers(){
        return _users;
    }

    public boolean createUser(DynamicForm form) throws ServiceException {
        //User user = new User(10, form.get("username"), form.get("fullname"), form.get("email"), form.get("password"));
        //if(!user.validate()){
            //throw new ServiceException("Validation error");
        //}
        try{
            String myDriver = "com.mysql.jdbc.Driver";
            Class.forName(myDriver);
        }
        catch (ClassNotFoundException e){
            System.out.println("Class not found: " + e.getMessage());
        }
        try{

            String myUrl = "jdbc:mysql://localhost:3306/skil3?autoReconnect=true&useSSL=false";

            Connection conn = DriverManager.getConnection(myUrl,"berglindoma13","Sjonvarp115");
            Statement st = conn.createStatement();

            String statement = "VALUES ( '" + form.get("fullname") + "', '" + form.get("username") +  "'," + "'" + form.get("email") + "'," + "'" + form.get("password") + "'" + ")";

            // note that i'm leaving "date_created" out of this insert statement
            st.executeUpdate("INSERT INTO users (user_fullname, user_name,user_email,user_password)" + statement);

            conn.close();
        }
        catch(Exception ex){
            System.out.println("sql error: " + ex.getMessage());
        }
        return true;
    }
}
