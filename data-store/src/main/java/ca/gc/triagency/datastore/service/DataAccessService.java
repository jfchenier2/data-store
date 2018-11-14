package ca.gc.triagency.datastore.service;

import java.util.List;

import ca.gc.triagency.datastore.model.Agency;
import ca.gc.triagency.datastore.model.Organization;
import ca.gc.triagency.datastore.model.Program;

public interface DataAccessService {

	public List<Program> getAllPrograms();

	public List<Agency> getAllAgencies();

	public Program saveProgram(Program p);

	public Program getProgram(Long id);

	public Agency getAgency(long id);

	public List<Program> getAgencyPrograms(long id);

	public List<Organization> getAllOrganizations();

	public Organization saveOrganization(Organization newOrg);

	public Organization getOrganization(Long orgId);

}
