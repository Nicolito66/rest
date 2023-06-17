package classes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.crypto.Data;

public class User implements Data {

    private Long id;
    private String username;
    private String password;
    private String email;

    // Default constructor
    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("email") String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }


}
