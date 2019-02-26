package ca.gc.triagency.datastore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.ParticipationEdiData;

public interface ParticipationEdiDataRepository extends JpaRepository<ParticipationEdiData, Long> {

}
