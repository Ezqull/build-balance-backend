package com.b2.buildbalance.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String email;
    private String firstName;
    private String lastName;
}
