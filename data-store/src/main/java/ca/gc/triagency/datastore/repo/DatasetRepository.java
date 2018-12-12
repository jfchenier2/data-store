package ca.gc.triagency.datastore.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.Dataset;
import ca.gc.triagency.datastore.model.Dataset.DatasetStatus;

public interface DatasetRepository extends JpaRepository<Dataset, Long> {

	List<Dataset> findByDatasetStatus(DatasetStatus approved);

}
