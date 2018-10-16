package ca.gc.triagency.datastore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.Agency;

public interface AgencyRepository extends JpaRepository<Agency, Long> {

}
