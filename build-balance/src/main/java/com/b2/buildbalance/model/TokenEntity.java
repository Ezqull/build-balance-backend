package com.b2.buildbalance.model;

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

    @Column(name = "token_type", nullable = false)
    private String tokenType;

    @Column(name = "expired",
            nullable = false)
    private Boolean expired;

    @Column(name = "revoked",
            nullable = false)
    private Boolean revoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            referencedColumnName = "id")
    @ToString.Exclude
    private UserEntity user;
}
