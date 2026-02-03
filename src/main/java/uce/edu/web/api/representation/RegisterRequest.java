package uce.edu.web.api.representation;

import java.util.Set;

public class RegisterRequest {
    public String username;
    public String password;
    public String email;
    public Set<String> roles;
}