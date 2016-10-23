package services;

import domain.User;
import play.data.DynamicForm;
import play.data.Form;

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
        User user = new User(10, form.get("username"), form.get("fullname"), form.get("email"), form.get("password"));
        if(!user.validate()){
            throw new ServiceException("Validation error");
        }
        return _users.add(user);
    }
}
