package uce.edu.web.api.infraestructura;

import uce.edu.web.api.domain.Role;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {
    public Role findByName(String name) {
        return find("name", name).firstResult();
    }
}
