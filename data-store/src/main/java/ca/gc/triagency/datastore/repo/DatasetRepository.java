package ca.gc.triagency.datastore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.Dataset;

public interface DatasetRepository extends JpaRepository<Dataset, Long> {

}
