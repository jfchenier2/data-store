package ca.gc.triagency.datastore.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.Program;

public interface ProgramRepository extends JpaRepository<Program, Long> {
	List<Program> findByLeadAgencyId(long id);

}
