package services;

import domain.User;

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

    public User getUserById(Long id) {
        for (User user : _users) {
            if(user.ID == id){
                System.out.println(user.FullName);
                return user;
            }
        }
        System.out.println("Returning null");
        return null;
    }

    public List<User> getUsers(){
        return _users;
    }
}
