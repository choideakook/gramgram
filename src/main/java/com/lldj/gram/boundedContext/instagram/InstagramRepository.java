package com.lldj.gram.boundedContext.instagram;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstagramRepository extends JpaRepository<Instagram, Long> {

    Optional<Instagram> findByUsername(String username);
}
