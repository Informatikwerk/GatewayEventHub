package de.informatikwerk.gatewayeventhub.repository;

import de.informatikwerk.gatewayeventhub.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
