package com.b2.buildbalance.facade;

import com.b2.buildbalance.dto.response.UserResponse;
import com.b2.buildbalance.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserFacade(
            final UserService userService,
            final ModelMapper modelMapper
    ) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public UserResponse getUserByEmail(final String email) {

        return modelMapper.map(userService.findUserByEmail(email), UserResponse.class);
    }
}
