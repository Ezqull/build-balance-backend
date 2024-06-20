package com.b2.buildbalance.service;

import com.b2.buildbalance.dto.request.UserRequest;
import com.b2.buildbalance.model.UserEntity;

public interface UserService {

    UserEntity findUserById(final Integer id);

    UserEntity findUserByEmail(final String email);

    UserEntity saveUser(final UserRequest request);
}
