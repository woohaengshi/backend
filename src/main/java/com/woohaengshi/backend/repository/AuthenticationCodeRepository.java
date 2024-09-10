package com.woohaengshi.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationCodeRepository extends CrudRepository<AuthenticationCode, String> {}
