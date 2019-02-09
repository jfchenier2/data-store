package ca.gc.triagency.datastore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.ApprovedApplicationParticipation;

public interface ViewApprovedAppRegistrations extends JpaRepository<ApprovedApplicationParticipation, Long> {

}
