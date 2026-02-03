package uce.edu.web.api.representation;

import java.util.Set;

public class LoginResponse {
    public String token;
    public String username;
    public String email;
    public Set<String> roles;
    public boolean enabled;
}