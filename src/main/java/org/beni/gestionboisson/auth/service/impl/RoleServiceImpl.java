package org.beni.gestionboisson.auth.service.impl;

//import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.auth.dto.RoleDTO;
import org.beni.gestionboisson.auth.entities.Role;
import org.beni.gestionboisson.auth.mappers.RoleMapper;
import org.beni.gestionboisson.auth.repository.RoleRepository;
import org.beni.gestionboisson.auth.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.beni.gestionboisson.auth.exceptions.RoleNotFoundException;

import java.util.List;
import java.util.stream.Collectors;
@ApplicationScoped
//@ApplicationScoped
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Inject
    private RoleRepository roleRepository;

    @Override
    public List<RoleDTO> getAllRoles() {
        logger.info("Fetching all roles.");
        return roleRepository.findAll().stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        logger.info("Fetching role by ID: {}", id);
        return roleRepository.findById(id)
                .map(RoleMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
        logger.info("Creating new role with code: {}", roleDTO.getCode());
        Role role = RoleMapper.toEntity(roleDTO);
        Role createdRole = roleRepository.save(role);
        logger.info("Role created successfully with ID: {}", createdRole.getIdRole());
        return RoleMapper.toDto(createdRole);
    }

    @Override
    @Transactional
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        logger.info("Updating role with ID: {}", id);
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Role with ID {} not found for update.", id);
                    return new RoleNotFoundException("Role not found");
                });
        role.setCode(roleDTO.getCode());
        role.setLibelle(roleDTO.getLibelle());
        Role updatedRole = roleRepository.save(role);
        logger.info("Role with ID {} updated successfully.", updatedRole.getIdRole());
        return RoleMapper.toDto(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        logger.info("Deleting role with ID: {}", id);
        roleRepository.deleteRole(id);
        logger.info("Role with ID {} deleted successfully.", id);
    }
}
