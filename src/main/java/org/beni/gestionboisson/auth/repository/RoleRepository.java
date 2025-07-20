package org.beni.gestionboisson.auth.repository;

import org.beni.gestionboisson.auth.entities.Role;
import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByCode(String code);
    Role save(Role role);
}
