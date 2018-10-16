package ca.gc.triagency.datastore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.Program;

public interface ProgramRepository extends JpaRepository<Program, Long> {

}
