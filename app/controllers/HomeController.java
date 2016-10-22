package controllers;

import domain.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import java.sql.*;

import services.UserService;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private UserService _userService = new UserService();

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result getUserById(Long id) {return ok(user.render(_userService.getUserById(id)));}

    public Result getUsers() {return ok(users.render(_userService.getUsers()));}

    public Result addUser(){
        DynamicForm form = Form.form().bindFromRequest();
        System.out.println(form);
        if(form.hasErrors()){
            return badRequest();
        }

        try{
            String myDriver = "org.jdbc.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/skil3";

            Connection conn = DriverManager.getConnection(myUrl,"root","");
            Statement st = conn.createStatement();

            // note that i'm leaving "date_created" out of this insert statement
            st.executeUpdate("INSERT INTO Customers " + "VALUES (1001, 'Simpson', 'Mr.', 'Springfield', 2001)");

            conn.close();
        }
        catch(Exception ex){
            System.out.println("error");
        }


        //System.out.println(form.get("fullname"));
        /*if(!user.validate()){
            return Results.status(412);
        }*/
        return created(users.render(_userService.getUsers()));
    }

}
