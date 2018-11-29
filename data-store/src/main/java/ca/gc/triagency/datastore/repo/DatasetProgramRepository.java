package ca.gc.triagency.datastore.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.DatasetProgram;

public interface DatasetProgramRepository extends JpaRepository<DatasetProgram, Long> {

	List<DatasetProgram> findByDatasetConfigurationIdAndExtIdNotIn(long configId, Collection<Long> extIds);

}
