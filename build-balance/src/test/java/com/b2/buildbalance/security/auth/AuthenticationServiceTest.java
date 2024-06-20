package com.b2.buildbalance.security.auth;

import com.b2.buildbalance.dto.request.UserRequest;
import com.b2.buildbalance.model.TokenEntity;
import com.b2.buildbalance.model.UserEntity;
import com.b2.buildbalance.repository.TokenRepository;
import com.b2.buildbalance.security.config.JwtService;
import com.b2.buildbalance.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

import static com.b2.buildbalance.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void testRegister() {
        final UserRequest request = createUserRequest();
        final UserEntity user = mock(UserEntity.class);
        final TokenEntity token = createTokenEntity(user, false, false);
        final TokenEntity refreshToken = createRefreshToken(user, false, false);

        when(this.jwtService.generateToken(user)).thenReturn(token.getToken());
        when(this.jwtService.generateRefreshToken(user)).thenReturn(refreshToken.getToken());
        when(this.userService.saveUser(request)).thenReturn(user);
        when(this.tokenRepository.save(token)).thenReturn(token);

        this.authenticationService.register(request);

        verify(this.tokenRepository).save(token);
    }

    @Test
    public void testAuthenticate() {
        final UserEntity user = createUserEntity();
        final AuthenticationRequest authRequest = createAuthRequest();

        final String expectedToken = "token";
        final TokenEntity token = createTokenEntity(user, false, false);
        final TokenEntity revokedToken = createTokenEntity(user, true, true);

        when(this.userService.findUserByEmail(authRequest.getEmail())).thenReturn(user);
        when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(this.jwtService.generateToken(user)).thenReturn(expectedToken);
        when(this.tokenRepository.findAllValidTokenByUser(Long.valueOf(user.getId()))).thenReturn(List.of(token));
        when(this.tokenRepository.saveAll(List.of(token))).thenReturn(List.of(revokedToken));
        when(this.tokenRepository.save(any())).thenReturn(token);

        final AuthenticationResponse response = authenticationService.authenticate(authRequest);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(user);

        assertNotNull(response);
        assertEquals(expectedToken, response.getAccessToken());
    }
}