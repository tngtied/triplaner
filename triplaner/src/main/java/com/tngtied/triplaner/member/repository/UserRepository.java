package com.tngtied.triplaner.member.repository;

import com.tngtied.triplaner.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername (String username);
    Optional<Member> findByEmail (String email);
    Optional<Member> findByEmailAndPassword (String email, String Password);


}
