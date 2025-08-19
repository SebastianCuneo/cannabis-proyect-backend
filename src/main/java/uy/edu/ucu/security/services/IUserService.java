package uy.edu.ucu.security.services;

import uy.edu.ucu.security.persistence.entities.UserEntity;

import java.util.List;

public interface IUserService {

    public List<UserEntity> findAllUsers();
}
