package ca.gc.triagency.datastore.service;

import java.io.File;
import java.util.List;

import org.springframework.scheduling.annotation.Async;

import ca.gc.triagency.datastore.model.Agency;
import ca.gc.triagency.datastore.model.Dataset;
import ca.gc.triagency.datastore.model.DatasetConfiguration;
import ca.gc.triagency.datastore.model.DatasetOrganization;
import ca.gc.triagency.datastore.model.DatasetProgram;
import ca.gc.triagency.datastore.model.EntityLinkOrganization;
import ca.gc.triagency.datastore.model.EntityLinkProgram;
import ca.gc.triagency.datastore.model.Organization;
import ca.gc.triagency.datastore.model.Program;

public interface DatasetService {

	public List<Dataset> getAllDatasets();

	public List<DatasetConfiguration> getAllDatasetConfigurations();

	public Dataset saveDataset(Dataset d);

	public Dataset getDataset(Long id);

	public DatasetConfiguration getDatasetConfiguration(long id);

	public List<File> getDatasetFiles();
	
	@Async
	public void uploadData(Dataset dataset);

	public Dataset configureNewDatasetFromFilename(String filename);

	public DatasetProgram getDatasetProgram(long id);

	public DatasetOrganization getDatasetOrganization(long id);

	public Organization createOrgFromDatasetOrg(DatasetOrganization org);

	public void linkDatasetOrg(DatasetOrganization org, Organization newOrg);

	public boolean approveDataset(Long id);

	public Dataset markAssessIfFirstTimeView(Dataset ds);

	public long linkMatchingOrgEntities(Long id);

	public List<DatasetProgram> getUnlinkedDatasetPrograms(long id);

	List<DatasetOrganization> getUnlinkedDatasetOrgs(long id);

	public void linkDatasetProgram(DatasetProgram dsProg, Program prog);

	public Program createProgramFromDatasetProg(DatasetProgram prog, Agency defaultAgency);

	public long linkMatchingProgramEntities(Long id);

	public long linkToProgramEntityLinks(Long datasetId);

	public long linkToOrgEntityLinks(Long datasetId);

	public List<EntityLinkProgram> getDatasetConfigProgramLinks(long id);

	public List<EntityLinkOrganization> getDatasetConfigOrgLinks(long id);

	@Async
	public void uploadAwardData(Dataset dataset);
	
	public void deleteDatasetById(Long id);
	
	public void deleteMarkedDatasets();
}
