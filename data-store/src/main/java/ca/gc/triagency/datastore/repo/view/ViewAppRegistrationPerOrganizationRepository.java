package ca.gc.triagency.datastore.repo.view;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.view.ApplicationRegistrationsPerOrganization;

public interface ViewAppRegistrationPerOrganizationRepository
		extends JpaRepository<ApplicationRegistrationsPerOrganization, Long> {

}
