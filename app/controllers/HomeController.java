package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.*;

import services.ServiceException;
import services.AccountService;
import services.UserService;
import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {
    private AccountService _accountService = new AccountService();
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

    public Result getUserById(String username) {
        try{
            return ok(_accountService.getUserById(username));
        }
        catch (ServiceException e){
            return notFound(e.getMessage());
        }
    }

    public Result getUsers() {return ok(_accountService.getUsers());}


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

    public Result deleteUser(String username){
        try {
            _accountService.deleteUser(username);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }
    }

    public Result authenticateUser(){
        try{
            JsonNode user = request().body().asJson();
            _accountService.authenticateUser(user);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }

    }

    public Result updateUser(String username){
        try{
            JsonNode user = request().body().asJson();
            _userService.updateProfile(username, user);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }
    }

    public Result changePassword(String username) {
        try{
            JsonNode user = request().body().asJson();
            _accountService.changeUserPassword(username, user);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }
    }

    public Result addFavoriteVideo(String username) {
        try {
            JsonNode video = request().body().asJson();
            _userService.addFavoriteVideo(username, video);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }
    }

    public Result addCloseFriend(String username) {
        try {
            JsonNode friend = request().body().asJson();
            _userService.addCloseFriend(username, friend);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }
    }

    public Result deleteFavoriteVideo(String username) {
        try {
            JsonNode video = request().body().asJson();
            _userService.deleteFavoriteVideo(username, video);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }
    }

}
