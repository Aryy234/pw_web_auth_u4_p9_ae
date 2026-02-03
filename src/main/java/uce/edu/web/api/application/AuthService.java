package uce.edu.web.api.application;

import uce.edu.web.api.domain.User;
import uce.edu.web.api.domain.Role;
import uce.edu.web.api.infraestructura.UserRepository;
import uce.edu.web.api.infraestructura.RoleRepository;
import uce.edu.web.api.representation.LoginRequest;
import uce.edu.web.api.representation.LoginResponse;
import uce.edu.web.api.representation.RegisterRequest;
import uce.edu.web.api.representation.UserRepresentation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import io.smallrye.jwt.build.Jwt;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class AuthService {
    @Inject
    UserRepository userRepository;
    @Inject
    RoleRepository roleRepository;

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.username);
        if (userOpt.isEmpty() || !userOpt.get().isEnabled()) {
            throw new RuntimeException("Usuario no encontrado o deshabilitado");
        }
        User user = userOpt.get();
        if (!BCrypt.checkpw(request.password, user.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        String token = Jwt.issuer("matricula-auth")
            .upn(user.getUsername())
            .groups(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
            .claim("email", user.getEmail())
            .claim("uid", user.getId().toString())
            .sign();
        LoginResponse resp = new LoginResponse();
        resp.token = token;
        resp.username = user.getUsername();
        resp.email = user.getEmail();
        resp.enabled = user.isEnabled();
        resp.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return resp;
    }

    @Transactional
    public UserRepresentation register(RegisterRequest request) {
        if (userRepository.findByUsername(request.username).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        if (userRepository.findByEmail(request.email).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
        User user = new User();
        user.setUsername(request.username);
        user.setEmail(request.email);
        user.setPasswordHash(BCrypt.hashpw(request.password, BCrypt.gensalt()));
        user.setEnabled(true);
        Set<Role> roles = new HashSet<>();
        if (request.roles != null) {
            for (String roleName : request.roles) {
                Role role = roleRepository.findByName(roleName);
                if (role == null) throw new RuntimeException("Rol no encontrado: " + roleName);
                roles.add(role);
            }
        }
        user.setRoles(roles);
        userRepository.persist(user);
        UserRepresentation rep = new UserRepresentation();
        rep.id = user.getId().toString();
        rep.username = user.getUsername();
        rep.email = user.getEmail();
        rep.enabled = user.isEnabled();
        rep.roles = roles.stream().map(Role::getName).collect(Collectors.toSet());
        return rep;
    }
}
