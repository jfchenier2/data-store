package ca.gc.triagency.datastore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.DatasetPerson;

public interface DatasetPersonRepository extends JpaRepository<DatasetPerson, Long> {

}
