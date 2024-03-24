package com.b2.buildbalance.validation;

import com.b2.buildbalance.exception.IllegalArgumentException;
import com.b2.buildbalance.model.UserEntity;
import com.b2.buildbalance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    private final UserRepository userRepository;

    public boolean validateUserRequest(final UserEntity user) {

        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name parameter not found");
        }

        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name parameter not found");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email parameter not found");
        }

        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is invalid");
        }

        if (isEmailTaken(user)) {
            throw new IllegalArgumentException("Email is already taken");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password parameter not found");
        }

        return true;
    }

    private boolean isValidEmail(final String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private boolean isEmailTaken(final UserEntity user) {
        return userRepository.existsByEmailAndId(user.getEmail(), user.getId());
    }
}
