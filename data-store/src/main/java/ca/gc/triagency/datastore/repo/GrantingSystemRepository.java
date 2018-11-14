package ca.gc.triagency.datastore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.GrantingSystem;

public interface GrantingSystemRepository extends JpaRepository<GrantingSystem, Long> {

}
