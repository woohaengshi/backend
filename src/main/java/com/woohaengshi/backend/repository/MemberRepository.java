package com.woohaengshi.backend.repository;

import com.woohaengshi.backend.domain.member.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {}
