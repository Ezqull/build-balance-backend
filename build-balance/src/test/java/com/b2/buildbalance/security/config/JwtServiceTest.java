package com.b2.buildbalance.security.config;

import com.b2.buildbalance.model.RoleEntity;
import com.b2.buildbalance.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class JwtServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String SECRET_KEY = "m5iePhNrB7R/Y2tNHdRisS7GajbOF/4XJUZdJdVH8FU=\n";
    @Mock
    private Environment env;
    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    public void setup() {
        when(env.getProperty("JWT_SECRET_KEY")).thenReturn(SECRET_KEY);
        when(env.getProperty("JWT_EXPIRATION")).thenReturn(String.valueOf(100));
    }

    @Test
    public void generateToken_ShouldGenerateValidToken() {
        UserDetails userDetails = createUserEntity();

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
    }

    @Test
    public void extractEmail_ShouldExtractEmailFromToken() {
        UserDetails userDetails = createUserEntity();
        String token = jwtService.generateToken(userDetails);

        String extractedEmail = jwtService.extractEmail(token);

        assertEquals(TEST_EMAIL, extractedEmail);
    }

    @Test
    public void isTokenValid_ShouldReturnTrueForValidToken() {
        UserDetails userDetails = createUserEntity();
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    private static UserEntity createUserEntity() {
        return UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .role(RoleEntity.builder().name("USER").build())
                .build();
    }
}
