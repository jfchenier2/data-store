package ca.gc.triagency.datastore.service;

import java.io.File;
import java.util.List;

import ca.gc.triagency.datastore.model.Dataset;
import ca.gc.triagency.datastore.model.DatasetConfiguration;
import ca.gc.triagency.datastore.model.DatasetOrganization;
import ca.gc.triagency.datastore.model.DatasetProgram;
import ca.gc.triagency.datastore.model.Organization;

public interface DatasetService {

	public List<Dataset> getAllDatasets();

	public List<DatasetConfiguration> getAllDatasetConfigurations();

	public Dataset saveDataset(Dataset d);

	public Dataset getDataset(Long id);

	public DatasetConfiguration getDatasetConfiguration(long id);

	public List<File> getDatasetFiles();

	public void uploadData(Dataset dataset);

	public Dataset configureNewDatasetFromFilename(String filename);

	public List<DatasetOrganization> getDatasetWarningOrgs(long id);

	public DatasetProgram getDatasetProgram(long id);

	public DatasetOrganization getDatasetOrganization(long id);

	public Organization createOrgFromDatasetOrg(DatasetOrganization org);

	public void linkDatasetOrg(DatasetOrganization org, Organization newOrg);

	public boolean approveDataset(Long id);

	public Dataset markAssessIfFirstTimeView(Dataset ds);

}
