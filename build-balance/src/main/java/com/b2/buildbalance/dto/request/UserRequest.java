package com.b2.buildbalance.dto.request;

import com.b2.buildbalance.dto.BaseDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest extends BaseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
