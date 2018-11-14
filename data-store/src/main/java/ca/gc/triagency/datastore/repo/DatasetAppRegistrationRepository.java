package ca.gc.triagency.datastore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.DatasetApplicationRegistration;

public interface DatasetAppRegistrationRepository extends JpaRepository<DatasetApplicationRegistration, Long> {

}
