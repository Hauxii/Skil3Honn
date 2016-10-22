package domain;

/**
 * Created by Lenny on 22.10.2016.
 */
public class DefaultUserValidator implements Validator {

    User user;

    public DefaultUserValidator(User user){
        this.user = user;
    }

    public boolean validate(){
        if(user == null){
            return false;
        }
        if(user.getUserName() == null || user.getUserName().length() == 0){
            return false;
        }
        if(user.getFullName() == null || user.getFullName().length() == 0){
            return false;
        }
        if(user.getEmail() == null || user.getEmail().length() == 0){
            return false;
        }
        return true;
    }
}
