package com.my.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.app.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
