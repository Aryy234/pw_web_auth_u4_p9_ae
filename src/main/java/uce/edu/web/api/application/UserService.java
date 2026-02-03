package uce.edu.web.api.application;

import uce.edu.web.api.domain.User;
import uce.edu.web.api.infraestructura.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    public List<User> listAll() {
        return userRepository.listAll();
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
}
