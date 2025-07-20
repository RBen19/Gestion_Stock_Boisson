package org.beni.gestionboisson.auth.service;

import org.beni.gestionboisson.auth.dto.AuthRequestDTO;
import org.beni.gestionboisson.auth.dto.AuthResponseDTO;
import org.beni.gestionboisson.auth.dto.ChangePasswordRequestDTO;
import org.beni.gestionboisson.auth.exceptions.PasswordChangeRequiredException;

public interface AuthService {
    AuthResponseDTO login(AuthRequestDTO authRequestDTO) throws PasswordChangeRequiredException;
    AuthResponseDTO changePassword(ChangePasswordRequestDTO request);
}
