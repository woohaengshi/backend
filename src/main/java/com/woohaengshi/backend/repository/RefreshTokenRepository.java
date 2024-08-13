package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query("select r from RefreshToken r join fetch r.member where r.token = :token")
    Optional<RefreshToken> findByToken(@Param("token") String token);

    void deleteAllByMemberId(Long memberId);
}
