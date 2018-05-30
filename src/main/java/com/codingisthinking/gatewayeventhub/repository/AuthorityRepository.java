package com.codingisthinking.gatewayeventhub.repository;

import com.codingisthinking.gatewayeventhub.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
