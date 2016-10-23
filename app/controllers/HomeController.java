package controllers;

import domain.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import java.sql.*;

import services.ServiceException;
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

    public Result getUserById(Long id) {
        try{
            return ok(user.render(_userService.getUserById(id)));
        }
        catch (ServiceException e){
            return notFound(e.getMessage());
        }

    }

    public Result getUsers() {return ok(users.render(_userService.getUsers()));}

    public Result addUser(){
        DynamicForm form = Form.form().bindFromRequest();
        try{
            if(_userService.createUser(form)){
                return created(users.render(_userService.getUsers()));
            }
            else {
                return internalServerError();
            }
        }
        catch (ServiceException e){
            //return badRequest(e.getMessage());
        }




        //System.out.println(form.get("fullname"));
        /*if(!user.validate()){
            return Results.status(412);
        }*/
        return created(users.render(_userService.getUsers()));

    }

}
