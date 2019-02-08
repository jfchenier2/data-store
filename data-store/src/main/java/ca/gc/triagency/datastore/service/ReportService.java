package ca.gc.triagency.datastore.service;

import java.util.List;

import ca.gc.triagency.datastore.model.ApplicationRegistrationsPerOrganization;
import ca.gc.triagency.datastore.model.ApprovedApplicationParticipation;
import ca.gc.triagency.datastore.model.ApprovedAward;
import ca.gc.triagency.datastore.model.Dataset;

public interface ReportService {

	// List<String> getYearsInput();

	// List<ApplicationRegistrationsPerOrganization>
	// getApplicationsPerOrganization(String year);
	List<ApplicationRegistrationsPerOrganization> getApplicationsPerOrganization();

	List<Dataset> getApprovedDatasets();

	List<ApprovedAward> getApprovedAwards();

	List<ApprovedApplicationParticipation> getApprovedAppParticipations();
}
