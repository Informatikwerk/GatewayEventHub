package de.informatikwerk.gatewayeventhub.repository;

import de.informatikwerk.gatewayeventhub.domain.Gateways;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Gateways entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GatewaysRepository extends JpaRepository<Gateways, Long> {

}
