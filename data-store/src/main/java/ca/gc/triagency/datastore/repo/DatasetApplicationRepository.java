package ca.gc.triagency.datastore.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.DatasetApplication;

public interface DatasetApplicationRepository extends JpaRepository<DatasetApplication, Long> {

	List<DatasetApplication> findByDatasetId(Long id);

}
