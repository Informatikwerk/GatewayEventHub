package de.informatikwerk.gatewayeventhub.repository;

import de.informatikwerk.gatewayeventhub.domain.Realmkeys;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Realmkeys entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RealmkeysRepository extends JpaRepository<Realmkeys, Long> {

}
