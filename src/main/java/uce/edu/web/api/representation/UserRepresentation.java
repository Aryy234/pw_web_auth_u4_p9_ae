package uce.edu.web.api.representation;

import java.util.Set;

public class UserRepresentation {
    public String id;
    public String username;
    public String email;
    public boolean enabled;
    public Set<String> roles;
}