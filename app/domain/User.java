package domain;

import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenny on 22.10.2016.
 */
public class User {
    private List<Validator> validators = new ArrayList<>();

    @Constraints.Required
    public int ID;
    @Constraints.Required
    public String UserName;
    @Constraints.Required
    public String FullName;
    @Constraints.Required
    public String Email;
    @Constraints.Required
    public String Password;

    public User(String userName, String fullName, String email, String password){
        this.UserName = userName;
        this.FullName = fullName;
        this.Email = email;
        this.Password = password;
        clearValidators();
        addValidator(new DefaultUserValidator(this));
    }

    public boolean validate(){
        for(Validator v : validators){
            if(!v.validate()){
                return false;
            }
        }
        return true;
    }

    public void clearValidators(){
        validators.clear();
    }

    public void addValidator(DefaultUserValidator duv) {
        validators.add(duv);
    }

    public void initialize(){
        clearValidators();
        addValidator(new DefaultUserValidator(this));
    }

    public int getID() {
        return ID;
    }

    public String getEmail() {
        return Email;
    }

    public String getFullName() {
        return FullName;
    }

    public String getUserName() {
        return UserName;
    }

}
