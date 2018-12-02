package ca.gc.triagency.datastore.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.EntityLinkProgram;

public interface EntityLinkProgramRepository extends JpaRepository<EntityLinkProgram, Long> {
	List<EntityLinkProgram> findByDatasetConfigurationId(Long long1);

}
