package ca.gc.triagency.datastore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.DatasetProgram;

public interface DatasetProgramRepository extends JpaRepository<DatasetProgram, Long> {

}
