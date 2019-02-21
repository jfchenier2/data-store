package ca.gc.triagency.datastore.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.DatasetApplicationRegistration;

public interface DatasetAppRegistrationRepository extends JpaRepository<DatasetApplicationRegistration, Long> {

	List<DatasetApplicationRegistration> findByDatasetApplicationId(long id);

}
