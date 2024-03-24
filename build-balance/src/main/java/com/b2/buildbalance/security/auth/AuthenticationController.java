package com.b2.buildbalance.security.auth;

import com.b2.buildbalance.dto.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationFacade authenticationFacade;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody final UserRequest request) {
        return ResponseEntity.ok(authenticationFacade.registerUser(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody final AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationFacade.authenticateUser(request));

    }
}
