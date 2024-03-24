package com.b2.buildbalance.security.auth;

import com.b2.buildbalance.dto.request.UserRequest;
import com.b2.buildbalance.model.UserEntity;
import com.b2.buildbalance.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFacade {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;

    public AuthenticationFacade(
            final ModelMapper modelMapper,
            final UserService userService, AuthenticationManager authenticationManager,
            final AuthenticationService authenticationService
    ) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
    }

    public AuthenticationResponse registerUser(UserRequest request) {

        final UserEntity user;

        user = userService.saveUser(modelMapper.map(request, UserEntity.class));

        return authenticationService.register(user);
    }

    public AuthenticationResponse authenticateUser(final AuthenticationRequest request) {

        final UserEntity user = userService.findUserByEmail(request.getEmail());

        return authenticationService.authenticate(user, request.getPassword());
    }
}
