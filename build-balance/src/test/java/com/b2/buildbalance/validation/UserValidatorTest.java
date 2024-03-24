package com.b2.buildbalance.validation;

import com.b2.buildbalance.exception.IllegalArgumentException;
import com.b2.buildbalance.model.UserEntity;
import com.b2.buildbalance.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    private static final String VALID_EMAIL = "example@mail.com";
    private static final String INVALID_EMAIL = "invalidEmail";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void validateUserRequest_whenUserRequestIsValid() {

        final UserEntity user = createUserEntity(VALID_EMAIL);

        when(userRepository.existsByEmailAndId(VALID_EMAIL, null)).thenReturn(false);

        boolean result = userValidator.validateUserRequest(user);

        assertTrue(result);
    }

    @Test
    void validateUserRequest_whenEmailIsInvalid() {

        final UserEntity user = createUserEntity(INVALID_EMAIL);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidator.validateUserRequest(user);
        });

        assertEquals("Email is invalid", exception.getMessage());
    }

    @Test
    void validateUserRequest_whenEmailIsAlreadyTaken() {

        final UserEntity user = createUserEntity(VALID_EMAIL);

        when(userRepository.existsByEmailAndId(VALID_EMAIL, null)).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidator.validateUserRequest(user);
        });

        assertEquals("Email is already taken", exception.getMessage());
    }

    private static UserEntity createUserEntity(final String email) {
        return UserEntity.builder()
                .email(email)
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();
    }
}