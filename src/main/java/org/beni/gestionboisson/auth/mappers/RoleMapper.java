package org.beni.gestionboisson.auth.mappers;

import org.beni.gestionboisson.auth.dto.RoleDTO;
import org.beni.gestionboisson.auth.entities.Role;


public class RoleMapper {

    public static RoleDTO toDto(Role role) {
        if (role == null) {
            return null;
        }
        return RoleDTO.builder()
                .idRole(role.getIdRole())
                .code(role.getCode())
                .libelle(role.getLibelle())
                .build();
    }

    public static Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }
        return Role.builder()
                .idRole(roleDTO.getIdRole())
                .code(roleDTO.getCode())
                .libelle(roleDTO.getLibelle())
                .build();
    }
}
