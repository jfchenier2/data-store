package ca.gc.triagency.datastore.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.DatasetOrganization;

public interface DatasetOrganizationRepository extends JpaRepository<DatasetOrganization, Long> {

	List<DatasetOrganization> findByDatasetConfigurationIdAndExtIdNotIn(long id, Collection<Long> extIds);

}
