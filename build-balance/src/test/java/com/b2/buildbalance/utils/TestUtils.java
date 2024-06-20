package com.b2.buildbalance.utils;

import com.b2.buildbalance.dto.request.UserRequest;
import com.b2.buildbalance.model.RoleEntity;
import com.b2.buildbalance.model.TokenEntity;
import com.b2.buildbalance.model.UserEntity;
import com.b2.buildbalance.security.auth.AuthenticationRequest;
import com.b2.buildbalance.security.auth.AuthenticationResponse;

public class TestUtils {
    public static final String USER_ROLE = "USER";
    public static final String USER_EMAIL = "mail@mail.com";
    public static final String USER_PASSWORD = "password";
    public static final String AUTH_TOKEN = "Bearer valid.token.here";


    public static AuthenticationRequest createAuthRequest() {
        return AuthenticationRequest.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
    }

    public static AuthenticationResponse createAuthResponse() {
        return AuthenticationResponse.builder()
                .accessToken(AUTH_TOKEN)
                .build();
    }

    public static UserRequest createUserRequest() {
        return UserRequest.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
    }

    public static UserEntity createUserEntity() {
        return UserEntity.builder()
                .id(1)
                .role(createRoleEntity())
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
    }

    static UserEntity createUserResponse() {
        return UserEntity.builder()
                .role(createRoleEntity())
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
    }

    public static RoleEntity createRoleEntity() {
        return RoleEntity.builder()
                .name(USER_ROLE)
                .build();
    }

    public static TokenEntity createTokenEntity(final UserEntity user, final boolean revoked, final boolean expired) {
        return TokenEntity.builder()
                .token(AUTH_TOKEN)
                .tokenType("BEARER")
                .user(user)
                .revoked(revoked)
                .expired(expired)
                .build();
    }

    public static TokenEntity createRefreshToken(final UserEntity user, final boolean revoked, final boolean expired) {
        return TokenEntity.builder()
                .token(AUTH_TOKEN)
                .tokenType("REFRESH")
                .user(user)
                .revoked(revoked)
                .expired(expired)
                .build();
    }
}
