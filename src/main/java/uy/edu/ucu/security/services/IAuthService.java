package uy.edu.ucu.security.services;

import uy.edu.ucu.security.services.models.dtos.LoginDTO;
import uy.edu.ucu.security.persistence.entities.UserEntity;
import uy.edu.ucu.security.services.models.dtos.ResponseDTO;

import java.util.HashMap;

public interface IAuthService {

    public HashMap<String, String> login(LoginDTO loginRequest) throws Exception;
    public ResponseDTO register(UserEntity user) throws Exception;
}
