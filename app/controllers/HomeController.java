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

    public Result getUserById(Long id) {
        try{
            return ok(_accountService.getUserById(id));
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

    public Result deleteUser(Long id){
        try {
            _accountService.deleteUser(id);
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

    public Result updateUser(Long id){
        try{
            JsonNode user = request().body().asJson();
            _userService.updateProfile(id, user);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }
    }

    public Result changePassword(Long id) {
        try{
            JsonNode user = request().body().asJson();
            _accountService.changeUserPassword(id, user);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }
    }

    public Result addFavoriteVideo(Long id) {
        try {
            JsonNode video = request().body().asJson();
            _userService.addFavoriteVideo(id, video);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }
    }

    public Result addCloseFriend(Long id) {
        try {
            JsonNode friend = request().body().asJson();
            _userService.addCloseFriend(id, friend);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }
    }

    public Result deleteFavoriteVideo(Long id) {
        try {
            JsonNode video = request().body().asJson();
            _userService.deleteFavoriteVideo(id, video);
            return ok();
        }
        catch (ServiceException e){
            return badRequest(e.getMessage());
        }
    }

}
