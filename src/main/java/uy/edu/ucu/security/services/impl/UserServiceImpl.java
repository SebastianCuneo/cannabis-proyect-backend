package uy.edu.ucu.security.services.impl;

import uy.edu.ucu.security.persistence.entities.UserEntity;
import uy.edu.ucu.security.persistence.repositories.SecurityUserRepository;
import uy.edu.ucu.security.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    SecurityUserRepository securityUserRepository;

    @Override
    public List<UserEntity> findAllUsers(){
        return securityUserRepository.findAll();
    }
}
