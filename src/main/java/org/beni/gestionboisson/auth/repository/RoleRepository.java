package org.beni.gestionboisson.auth.repository;

import org.beni.gestionboisson.auth.dto.RoleDTO;
import org.beni.gestionboisson.auth.entities.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByCode(String code);
    Role save(Role role);
    List<Role> findAll();
    Role getRoleById(Long id);
    Role createRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
    Optional<Role> findById(Long id);
}
