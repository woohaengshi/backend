package com.woohaengshi.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationCodeRepository extends CrudRepository<AuthenticationCode, String> {
    Optional<AuthenticationCode> findByCode(String code);
    boolean existsByCode(String code);
}
