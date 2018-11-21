package ca.gc.triagency.datastore.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.EntityLinkOrganization;

public interface EntityLinkOrgRepository extends JpaRepository<EntityLinkOrganization, Long> {
	List<EntityLinkOrganization> findByDatasetConfigurationId(Long long1);

}
