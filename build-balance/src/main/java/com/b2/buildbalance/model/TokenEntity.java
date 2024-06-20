package com.b2.buildbalance.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenEntity token1 = (TokenEntity) o;
        return Objects.equals(token, token1.token) && Objects.equals(tokenType, token1.tokenType) && Objects.equals(expired, token1.expired) && Objects.equals(revoked, token1.revoked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, tokenType, expired, revoked);
    }
}
