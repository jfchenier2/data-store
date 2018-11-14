package ca.gc.triagency.datastore.service.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import ca.gc.triagency.datastore.model.ArtifactLinkOrganization;
import ca.gc.triagency.datastore.model.Dataset;
import ca.gc.triagency.datastore.model.Dataset.DatasetStatus;
import ca.gc.triagency.datastore.model.DatasetAppRegistrationRole;
import ca.gc.triagency.datastore.model.DatasetApplication;
import ca.gc.triagency.datastore.model.DatasetApplicationRegistration;
import ca.gc.triagency.datastore.model.DatasetConfiguration;
import ca.gc.triagency.datastore.model.DatasetOrganization;
import ca.gc.triagency.datastore.model.DatasetPerson;
import ca.gc.triagency.datastore.model.DatasetProgram;
import ca.gc.triagency.datastore.model.Organization;
import ca.gc.triagency.datastore.model.file.AwardDatasetRow;
import ca.gc.triagency.datastore.repo.ArtifactLinkOrgRepository;
import ca.gc.triagency.datastore.repo.DatasetAppRegistrationRepository;
import ca.gc.triagency.datastore.repo.DatasetAppRegistrationRoleRepository;
import ca.gc.triagency.datastore.repo.DatasetApplicationRepository;
import ca.gc.triagency.datastore.repo.DatasetConfigurationRepository;
import ca.gc.triagency.datastore.repo.DatasetOrganizationRepository;
import ca.gc.triagency.datastore.repo.DatasetPersonRepository;
import ca.gc.triagency.datastore.repo.DatasetProgramRepository;
import ca.gc.triagency.datastore.repo.DatasetRepository;
import ca.gc.triagency.datastore.service.DatasetService;

@Service
public class DatasetServiceImpl implements DatasetService {
	/** Logger */
	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	DatasetRepository datasetRepo;

	@Autowired
	DatasetApplicationRepository datasetApplicationRepo;

	@Autowired
	ArtifactLinkOrgRepository artifactLinkOrgRepo;

	@Autowired
	DatasetProgramRepository datasetProgramRepo;

	@Autowired
	DatasetOrganizationRepository datasetOrgRepo;

	@Autowired
	DatasetPersonRepository datasetPersonRepo;

	@Autowired
	DatasetConfigurationRepository configRepo;

	@Autowired
	DatasetAppRegistrationRoleRepository appRoleRepo;

	@Autowired
	DatasetAppRegistrationRepository appRegistrationRepo;

	@Override
	public List<Dataset> getAllDatasets() {
		return datasetRepo.findAll();
	}

	@Override
	public List<DatasetConfiguration> getAllDatasetConfigurations() {
		return configRepo.findAll();
	}

	@Override
	public Dataset saveDataset(Dataset d) {
		return datasetRepo.save(d);
	}

	@Override
	public Dataset getDataset(Long id) {
		return datasetRepo.getOne(id);
	}

	@Override
	public DatasetConfiguration getDatasetConfiguration(long id) {
		return configRepo.getOne(id);
	}

	@Override
	public List<File> getDatasetFiles() {
		String datasetsFolder = "datasets";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(datasetsFolder).getFile());
		List<File> list = new ArrayList<File>();
		for (File l : file.listFiles()) {
			list.add(l);
		}
		return list;
	}

	@Override
	public void uploadData(Dataset dataset) {
		HashMap<String, DatasetProgram> programHash = new HashMap<String, DatasetProgram>();
		HashMap<String, DatasetAppRegistrationRole> roleHash = new HashMap<String, DatasetAppRegistrationRole>();
		HashMap<Long, DatasetOrganization> orgHash = new HashMap<Long, DatasetOrganization>();
		HashMap<String, DatasetPerson> personHash = new HashMap<String, DatasetPerson>();

		// fill repos with relevant config lookups

		String currentAppId = "";
		int rowNum = 0;
		DatasetApplication currentApplication = null;
		DatasetProgram currentProgram = null;
		DatasetAppRegistrationRole currentAppRole = null;
		DatasetPerson currentPerson = null;
		DatasetOrganization currentOrg = null;
		for (AwardDatasetRow row : loadObjectList(dataset.getFilename())) {
			if (currentAppId.compareTo(row.getApplicationIdentifier()) != 0) {
				if (currentApplication != null) {
					currentApplication = datasetApplicationRepo.save(currentApplication);
					System.out.println("created DatasetApplication: " + currentApplication);
				}
				currentApplication = new DatasetApplication();
				currentApplication.setDataset(dataset);

				currentAppId = row.getApplicationIdentifier();
				currentProgram = programHash.get(row.getProgramId());
				if (currentProgram == null) {
					currentProgram = new DatasetProgram();
					currentProgram.setExtId(row.getProgramId());
					currentProgram.setNameEn(row.getProgramEn());
					currentProgram.setNameFr(row.getProgramFr());
					currentProgram = datasetProgramRepo.save(currentProgram);
					programHash.put(currentProgram.getExtId(), currentProgram);
					System.out.println("created DatasetProgram: " + currentProgram);
				}
				currentApplication.setDatasetProgram(currentProgram);

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
				Date parsedDate = null;
				try {
					parsedDate = formatter.parse(row.getCompetitionYear());
				} catch (ParseException e) {
					System.out.println("unvalid competition year:" + row.getCompetitionYear());
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
				currentApplication.setProgramYear(parsedDate);

				formatter = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss aa");
				parsedDate = null;
				try {
					if (row.getCreateDate() != null) {
						parsedDate = formatter.parse(row.getCreateDate());
					}
				} catch (ParseException e) {
					System.out.println("invalid create date:" + row.getCreateDate());
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
				currentApplication.setCreateDateTime(parsedDate);
				currentApplication = datasetApplicationRepo.save(currentApplication);

			}

			currentAppRole = roleHash.get(row.getRoleCode());
			if (currentAppRole == null) {
				currentAppRole = new DatasetAppRegistrationRole();
				currentAppRole.setIdentifier(row.getRoleCode());
				currentAppRole.setNameEn(row.getRoleEn());
				currentAppRole.setNameFr(row.getRoleFr());
				currentAppRole = appRoleRepo.save(currentAppRole);
				roleHash.put(currentAppRole.getIdentifier(), currentAppRole);
				System.out.println("created DatasetAppRegistrationRole: " + currentAppRole);
			}
			row.fixOrgId();
			Long rowOrgId = new Long(row.getOrgId());
			currentOrg = orgHash.get(rowOrgId);
			if (currentOrg == null) {
				currentOrg = new DatasetOrganization();
				currentOrg.setExtId(rowOrgId);
				currentOrg.setNameEn(row.getOrgNameEn());
				currentOrg.setNameFr(row.getOrgNameFr());
				currentOrg.setDatasetConfiguration(dataset.getDatasetConfiguration());
				currentOrg = datasetOrgRepo.save(currentOrg);
				orgHash.put(currentOrg.getExtId(), currentOrg);
				System.out.println("created DatasetOrganization: " + currentOrg);
			}
			currentPerson = personHash.get(row.getPersonIdentifier());
			if (currentPerson == null) {
				currentPerson = new DatasetPerson();
				currentPerson.setExtId(Long.getLong(row.getPersonIdentifier()));
				currentPerson.setFamilyName(row.getFamilyName());
				currentPerson.setGivenName(row.getGivenName());
				currentPerson = datasetPersonRepo.save(currentPerson);
				personHash.put("" + currentPerson.getExtId(), currentPerson);
				System.out.println("created DatasetPerson: " + currentPerson);
			}
			DatasetApplicationRegistration appRegistration = new DatasetApplicationRegistration();
			appRegistration.setDatasetOrganization(currentOrg);
			appRegistration.setPerson(currentPerson);
			appRegistration.setRegistrationRole(currentAppRole);
			appRegistration.setDatasetApplication(currentApplication);
			appRegistrationRepo.save(appRegistration);
			System.out.println("created DatasetOrganization: " + appRegistration);

			System.out.println("Row #" + rowNum++);
			dataset.setCurrentRow(rowNum);
		}
		dataset.setTotalRecords(rowNum);
		datasetRepo.save(dataset);

	}

	private <T> List<T> loadObjectList(Class<T> type, String fileName) {
		try {
			CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
			CsvMapper mapper = new CsvMapper();
			File file = new ClassPathResource(fileName).getFile();
			MappingIterator<T> readValues = mapper.reader(type).with(bootstrapSchema).readValues(file);
			return readValues.readAll();
		} catch (Exception e) {
			LOG.error("Error occurred while loading object list from file " + fileName, e);
			return Collections.emptyList();
		}
	}

	public Collection<AwardDatasetRow> loadObjectList(String fileName) {
		Collection<AwardDatasetRow> rows = null;
		// rows = loadObjectList(AwardDatasetRow.class, "datasets/" + fileName);

		Xcelite xcelite;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("datasets/" + fileName).getFile());
		xcelite = new Xcelite(file);
		XceliteSheet sheet = xcelite.getSheet(0);
		SheetReader<AwardDatasetRow> reader = sheet.getBeanReader(AwardDatasetRow.class);
		rows = reader.read();

		// return rows;
		return rows;

	}

	@Override
	public Dataset configureNewDatasetFromFilename(String filename) {
		Dataset retval = new Dataset();
		retval.setCurrentRow(0);
		String targetAcronym = filename.substring(20, filename.length() - 5);
		retval.setDatasetConfiguration(configRepo.findOneByAcronym(targetAcronym));
		retval.setFilename(filename);
		return retval;
	}

	@Override
	public List<DatasetOrganization> getDatasetWarningOrgs(long id) {
		Collection<Long> extIds = new ArrayList<Long>();
		extIds.add(new Long(0));
		Dataset dataset = datasetRepo.getOne(id);
		long configId = dataset.getDatasetConfiguration().getId();
		for (ArtifactLinkOrganization link : artifactLinkOrgRepo.findByDatasetConfigurationId(configId)) {
			extIds.add(link.getExtId());
			// String asdf = "";

		}
		List<DatasetOrganization> retval = datasetOrgRepo.findByDatasetConfigurationIdAndExtIdNotIn(configId, extIds);
		return retval;
	}

	@Override
	public DatasetProgram getDatasetProgram(long id) {
		return datasetProgramRepo.getOne(id);
	}

	@Override
	public DatasetOrganization getDatasetOrganization(long id) {
		return datasetOrgRepo.getOne(id);
	}

	@Override
	public Organization createOrgFromDatasetOrg(DatasetOrganization org) {
		Organization newOrg = new Organization();
		newOrg.setNameEn(org.getNameEn());
		newOrg.setNameFr(org.getNameFr());
		return newOrg;

	}

	@Override
	public void linkDatasetOrg(DatasetOrganization org, Organization newOrg) {
		ArtifactLinkOrganization link = new ArtifactLinkOrganization();
		link.setDatasetConfiguration(org.getDatasetConfiguration());
		link.setExtId(org.getExtId());
		link.setOrg(newOrg);
		artifactLinkOrgRepo.save(link);

		org.setLink(link);
		datasetOrgRepo.save(org);

	}

	@Override
	public boolean approveDataset(Long id) {
		boolean retval = false;
		Dataset ds = datasetRepo.getOne(id);
		if (ds != null) {
			ds.setDatasetStatus(DatasetStatus.APPROVED);
			datasetRepo.save(ds);
			retval = true;
		}
		return retval;
	}

	@Override
	public Dataset markAssessIfFirstTimeView(Dataset ds) {
		Dataset retval = ds;
		if (ds.getDatasetStatus() == DatasetStatus.CREATED) {
			ds.setDatasetStatus(DatasetStatus.ASSESS);
			retval = datasetRepo.save(ds);
		}
		return retval;

	}

}
