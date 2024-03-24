package com.b2.buildbalance.service.impl;

import com.b2.buildbalance.model.RoleEntity;
import com.b2.buildbalance.model.UserEntity;
import com.b2.buildbalance.repository.RoleRepository;
import com.b2.buildbalance.repository.UserRepository;
import com.b2.buildbalance.service.UserService;
import com.b2.buildbalance.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserEntity findUserById(final Integer id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(format("User with id %d doesn't exist", id))
                );
    }

    @Override
    public UserEntity findUserByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(format("User with id %s doesn't exist", email))
                );
    }

    @Override
    public UserEntity saveUser(final UserEntity userEntity) {
        userValidator.validateUserRequest(userEntity);
        final RoleEntity role = roleRepository.findById(1).orElse(new RoleEntity());
        userEntity.setRole(role);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }
}
