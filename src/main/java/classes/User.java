package classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.crypto.Data;

public class User implements Data {

    private Integer id;
    private String username;
    private String password;
    private String mail;

    // Default constructor
    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("mail") String mail) {
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    public User(Integer id, String username, String password, String mail) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
