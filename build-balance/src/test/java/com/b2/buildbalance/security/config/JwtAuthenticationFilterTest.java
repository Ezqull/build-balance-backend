package com.b2.buildbalance.security.config;

import com.b2.buildbalance.model.TokenEntity;
import com.b2.buildbalance.model.UserEntity;
import com.b2.buildbalance.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Optional;

import static com.b2.buildbalance.utils.TestUtils.createTokenEntity;
import static com.b2.buildbalance.utils.TestUtils.createUserEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    private final static String NON_AUTH_PATH = "/api/v1/token";

    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private FilterChain filterChain;
    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    public void shouldNotAuthenticateIfAuthHeaderIsMissing() throws ServletException, IOException {

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getServletPath()).thenReturn(NON_AUTH_PATH);
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void shouldAuthenticateWithValidToken() throws ServletException, IOException {

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final UserEntity user = createUserEntity();
        final TokenEntity token = createTokenEntity(user, false, false);
        final String tokenBody = token.getToken().substring(7);

        when(request.getHeader("Authorization")).thenReturn(token.getToken());
        when(request.getServletPath()).thenReturn(NON_AUTH_PATH);
        when(jwtService.extractEmail(anyString())).thenReturn(user.getEmail());
        when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(user);
        when(this.tokenRepository.findByToken(tokenBody)).thenReturn(Optional.of(token));
        when(this.jwtService.isTokenValid(tokenBody, user)).thenReturn(true);

        this.jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(user.getEmail(), SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }
}
