package com.b2.buildbalance.repository;

import com.b2.buildbalance.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(final String email);

    @Query("select case when (COUNT(u.email) > 0) then true else false end from UserEntity u where u.email = :email and (:id is null or u.id = :id)")
    boolean existsByEmailAndId(@Param("email") String email, @Param("id") Integer id);

}
