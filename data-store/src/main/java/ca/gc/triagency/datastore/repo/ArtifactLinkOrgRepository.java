package ca.gc.triagency.datastore.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.ArtifactLinkOrganization;

public interface ArtifactLinkOrgRepository extends JpaRepository<ArtifactLinkOrganization, Long> {
	List<ArtifactLinkOrganization> findByDatasetConfigurationId(Long long1);

}
