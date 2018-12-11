package ca.gc.triagency.datastore.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.DatasetProgram;

public interface DatasetProgramRepository extends JpaRepository<DatasetProgram, Long> {

	List<DatasetProgram> findByDatasetIdAndEntityLinkIsNull(long configId);

}
