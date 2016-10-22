package domain;

/**
 * Created by Lenny on 22.10.2016.
 */
public class User {
    public int ID;
    public String UserName;
    public String FullName;
    public String Email;
    public String Password;

    public User(int id, String userName, String fullName, String email, String password){
        this.ID = id;
        this.UserName = userName;
        this.FullName = fullName;
        this.Email = email;
        this.Password = password;
    }
}
