package uce.edu.web.api.infraestructura;

import uce.edu.web.api.domain.UserRole;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRoleRepository implements PanacheRepository<UserRole> {
}
