package com.b2.buildbalance.security.config;

import com.b2.buildbalance.model.RoleEntity;
import com.b2.buildbalance.model.UserEntity;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    public void shouldNotAuthenticateIfAuthHeaderIsMissing() throws ServletException, IOException {

        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void shouldAuthenticateWithValidToken() throws ServletException, IOException {

        String email = "user@example.com";
        String token = "Bearer valid.token.here";
        String password = "password";

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(password)
                .role(
                        RoleEntity.builder()
                                .name("USER")
                                .build()
                )
                .build();

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractEmail(anyString())).thenReturn(email);
        when(jwtService.isTokenValid(anyString(), any())).thenReturn(true);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(user);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(email, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }
}
