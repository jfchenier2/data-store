package ca.gc.triagency.datastore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.DatasetAward;

public interface DatasetAwardRepository extends JpaRepository<DatasetAward, Long> {

}
