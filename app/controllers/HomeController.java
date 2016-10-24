package controllers;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;

import services.ServiceException;
import services.AccountService;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private AccountService _accountService = new AccountService();

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
            return ok(user.render(_accountService.getUserById(id)));
        }
        catch (ServiceException e){
            return notFound(e.getMessage());
        }
    }

    public Result getUsers() {return ok(users.render(_accountService.getUsers()));}


    public Result addUser(){
        DynamicForm form = Form.form().bindFromRequest();

        try{
            if( _accountService.createUser(form)){
                return created(users.render(_accountService.getUsers()));
            }
            else{
                return internalServerError();
            }
        }
        catch(ServiceException ex){
            System.out.println(ex.getMessage());
            return badRequest("Precondition Failed");
        }

    }

}
