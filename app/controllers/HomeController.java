package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;

import play.twirl.api.Content;
import scala.util.parsing.json.JSONObject;
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
            return ok(_accountService.getUserById(id));
        }
        catch (ServiceException e){
            return notFound(e.getMessage());
        }
    }

    public Result getUsers() {return ok();}


    public Result addUser(){
        JsonNode user = request().body().asJson();

        try{
            if( _accountService.createUser(user)){
                return created();
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
