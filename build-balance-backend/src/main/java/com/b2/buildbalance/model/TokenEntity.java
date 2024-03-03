package com.b2.buildbalance.model;

import com.b2.buildbalance.model.types.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "token")
public class TokenEntity extends BaseEntity {
    @Column(name = "token",
            nullable = false,
            unique = true)
    private String token;
    @Enumerated(EnumType.STRING)
    @Column(name = "token_type",
            nullable = false)
    private TokenType type;
    @Column(name = "expired",
            nullable = false)
    private Boolean expired;
    @Column(name = "revoked",
            nullable = false)
    private Boolean revoked;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id",
            referencedColumnName = "id")
    private UserEntity user;
}
