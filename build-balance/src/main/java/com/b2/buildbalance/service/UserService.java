package com.b2.buildbalance.service;

import com.b2.buildbalance.model.UserEntity;

public interface UserService {

    UserEntity findUserById(final Integer id);

    UserEntity findUserByEmail(String email);

    UserEntity saveUser(final UserEntity entity);
}
