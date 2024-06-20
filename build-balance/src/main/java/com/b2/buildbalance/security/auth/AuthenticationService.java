package com.b2.buildbalance.security.auth;

import com.b2.buildbalance.dto.request.UserRequest;
import com.b2.buildbalance.model.TokenEntity;
import com.b2.buildbalance.model.UserEntity;
import com.b2.buildbalance.model.types.TokenType;
import com.b2.buildbalance.repository.TokenRepository;
import com.b2.buildbalance.security.config.JwtService;
import com.b2.buildbalance.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(final UserRequest userRequest) {

        final UserEntity user = userService.saveUser(userRequest);

        final AuthenticationResponse authenticationResponse = this.getFilledAuthResponse(user);

        this.saveUserToken(user, authenticationResponse.getAccessToken());

        return authenticationResponse;
    }

    public AuthenticationResponse authenticate(final AuthenticationRequest request) {

        final UserEntity user = this.userService.findUserByEmail(request.getEmail());

        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        request.getPassword()
                )
        );

        final AuthenticationResponse authenticationResponse = this.getFilledAuthResponse(user);

        this.revokeAllUserTokens(user);
        this.saveUserToken(user, authenticationResponse.getAccessToken());

        return authenticationResponse;
    }


    private void saveUserToken(UserEntity user, String jwtToken) {
        final TokenEntity token = TokenEntity.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(String.valueOf(TokenType.BEARER))
                .expired(false)
                .revoked(false)
                .build();
        this.tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(Long.valueOf(user.getId()));
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = this.jwtService.extractEmail(refreshToken);
        if (userEmail != null) {
            final UserEntity user = this.userService.findUserByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, user)) {
                final String accessToken = jwtService.generateToken(user);
                this.revokeAllUserTokens(user);
                this.saveUserToken(user, accessToken);
                final AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private AuthenticationResponse getFilledAuthResponse(final UserEntity user) {
        final String jwtToken = jwtService.generateToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
