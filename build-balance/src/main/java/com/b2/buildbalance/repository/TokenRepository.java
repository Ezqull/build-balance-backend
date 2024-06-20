package com.b2.buildbalance.repository;

import com.b2.buildbalance.model.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Query(value = """
            select t from TokenEntity t inner join UserEntity u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<TokenEntity> findAllValidTokenByUser(final Long id);

    Optional<TokenEntity> findByToken(final String token);
}
