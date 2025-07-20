package org.beni.gestionboisson.auth.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.beni.gestionboisson.auth.dto.RoleDTO;
import org.beni.gestionboisson.auth.entities.Role;
import org.beni.gestionboisson.auth.mappers.RoleMapper;
import org.beni.gestionboisson.auth.repository.RoleRepository;
import org.beni.gestionboisson.auth.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoleServiceImpl implements RoleService {

    @Inject
    private RoleRepository roleRepository;

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        return roleRepository.findById(id)
                .map(RoleMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = RoleMapper.toEntity(roleDTO);
        return RoleMapper.toDto(roleRepository.save(role));
    }

    @Override
    @Transactional
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        role.setCode(roleDTO.getCode());
        role.setLibelle(roleDTO.getLibelle());
        return RoleMapper.toDto(roleRepository.save(role));
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        roleRepository.deleteRole(id);
    }
}
