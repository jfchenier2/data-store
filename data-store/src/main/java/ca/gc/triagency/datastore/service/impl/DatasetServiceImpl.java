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
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import ca.gc.triagency.datastore.model.Agency;
import ca.gc.triagency.datastore.model.Dataset;
import ca.gc.triagency.datastore.model.Dataset.DatasetStatus;
import ca.gc.triagency.datastore.model.DatasetAppRegistrationRole;
import ca.gc.triagency.datastore.model.DatasetApplication;
import ca.gc.triagency.datastore.model.DatasetApplicationRegistration;
import ca.gc.triagency.datastore.model.DatasetAward;
import ca.gc.triagency.datastore.model.DatasetConfiguration;
import ca.gc.triagency.datastore.model.DatasetOrganization;
import ca.gc.triagency.datastore.model.DatasetPerson;
import ca.gc.triagency.datastore.model.DatasetProgram;
import ca.gc.triagency.datastore.model.EntityLinkOrganization;
import ca.gc.triagency.datastore.model.EntityLinkProgram;
import ca.gc.triagency.datastore.model.Organization;
import ca.gc.triagency.datastore.model.ParticipationEdiData;
import ca.gc.triagency.datastore.model.Program;
import ca.gc.triagency.datastore.model.file.ApplyDatasetRow;
import ca.gc.triagency.datastore.model.file.AwardDatasetRow;
import ca.gc.triagency.datastore.repo.AgencyRepository;
import ca.gc.triagency.datastore.repo.DatasetAppRegistrationRepository;
import ca.gc.triagency.datastore.repo.DatasetAppRegistrationRoleRepository;
import ca.gc.triagency.datastore.repo.DatasetApplicationRepository;
import ca.gc.triagency.datastore.repo.DatasetAwardRepository;
import ca.gc.triagency.datastore.repo.DatasetConfigurationRepository;
import ca.gc.triagency.datastore.repo.DatasetOrganizationRepository;
import ca.gc.triagency.datastore.repo.DatasetPersonRepository;
import ca.gc.triagency.datastore.repo.DatasetProgramRepository;
import ca.gc.triagency.datastore.repo.DatasetRepository;
import ca.gc.triagency.datastore.repo.EntityLinkOrgRepository;
import ca.gc.triagency.datastore.repo.EntityLinkProgramRepository;
import ca.gc.triagency.datastore.repo.OrganizationRepository;
import ca.gc.triagency.datastore.repo.ParticipationEdiDataRepository;
import ca.gc.triagency.datastore.repo.ProgramRepository;
import ca.gc.triagency.datastore.service.DatasetService;

@Service
public class DatasetServiceImpl implements DatasetService {
	/** Logger */
	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	DatasetRepository datasetRepo;

	@Autowired
	AgencyRepository agencyRepo;

	@Autowired
	ProgramRepository programRepo;

	@Autowired
	DatasetApplicationRepository datasetApplicationRepo;

	@Autowired
	OrganizationRepository orgRepo;

	@Autowired
	EntityLinkOrgRepository entityLinkOrgRepo;

	@Autowired
	EntityLinkProgramRepository entityLinkProgramRepo;

	@Autowired
	DatasetProgramRepository datasetProgramRepo;

	@Autowired
	DatasetAwardRepository datasetAwardRepo;

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

	@Autowired
	ParticipationEdiDataRepository ediRepo;

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

	@Async
	@Override
	public void uploadAwardData(Dataset dataset) {
		HashMap<Long, DatasetApplication> datasetAppsHash = new HashMap<Long, DatasetApplication>();
		Long parentDatasetId = dataset.getParentDataset().getId();
		List<DatasetApplication> relevantApps = datasetApplicationRepo.findByDatasetId(parentDatasetId);
		for (DatasetApplication app : relevantApps) {
			datasetAppsHash.put(app.getExtId(), app);
		}

		HashMap<String, DatasetPerson> datasetPersonHash = new HashMap<String, DatasetPerson>();
		List<DatasetPerson> relevantPersons = datasetPersonRepo.findAll(); // fixme
		for (DatasetPerson p : relevantPersons) {
			datasetPersonHash.put(p.getExtId(), p);
		}

		long rownum = 0;
		for (AwardDatasetRow row : loadAwardObjectList(dataset.getFilename())) {
			DatasetAward award = new DatasetAward();
			row.fixApplicationId();
			row.fixPersonId();
			if (row.getAwardedAmmount() == null || row.getAwardedAmmount().contains("NULL")) {
				continue;
			}
			rownum++;
			award.setAmount(Float.parseFloat(row.getAwardedAmmount()));
			award.setDatasetApplication(datasetAppsHash.get(Long.parseLong(row.getApplicationId())));
			award.setDatasetPerson(datasetPersonHash.get(Long.parseLong(row.getPersonIdentifier())));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
			Date parsedDate = null;
			try {
				parsedDate = formatter.parse(row.getCompetitionYear());
				award.setProgramYear(parsedDate);
				parsedDate = formatter.parse(row.getFundingYear());
				award.setFundingYear(parsedDate);
			} catch (ParseException e) {
				System.out.println("unvalid competition year:" + row.getCompetitionYear());
			}
			datasetAwardRepo.save(award);

//			if(rownum % 10 == 0) {
//				datasetRepo.save(dataset);
//			}
		}
		dataset.setTotalRecords(rownum);
		datasetRepo.save(dataset);

	}

	@Async
	@Override
	public void uploadData(Dataset dataset) {
		List<Agency> agencies = agencyRepo.findAll();
		Agency SSHRC = null, NSERC = null;
		for (Agency agency : agencies) {
			if (agency.getAcronymEn().compareTo("NSERC") == 0) {
				NSERC = agency;
			} else if (agency.getAcronymEn().compareTo("SSHRC") == 0) {
				SSHRC = agency;
			}
		}
		HashMap<String, DatasetProgram> programHash = new HashMap<String, DatasetProgram>();
		HashMap<String, DatasetAppRegistrationRole> roleHash = new HashMap<String, DatasetAppRegistrationRole>();
		HashMap<String, DatasetOrganization> orgHash = new HashMap<String, DatasetOrganization>();
		HashMap<String, DatasetPerson> personHash = new HashMap<String, DatasetPerson>();

		// fill repos with relevant config lookups

		String currentAppId = "";
		int rowNum = 0;
		DatasetApplication currentApplication = null;
		DatasetProgram currentProgram = null;
		DatasetAppRegistrationRole currentAppRole = null;
		DatasetPerson currentPerson = null;
		DatasetOrganization currentOrg = null;
		for (ApplyDatasetRow row : loadObjectList(dataset.getFilename())) {
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
					if (row.getSource().compareTo("NAMIS") == 0) {
						currentProgram.setLeadAgency(NSERC);
					} else {
						currentProgram.setLeadAgency(SSHRC);
					}
					currentProgram.setDataset(dataset);
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
				currentApplication.setExtIdentifier(row.getApplicationIdentifier());
				row.fixApplId();
				if(row.getApplId().contains("-")) {
					row.setApplId(row.getApplId().replace('-', '0'));
				}
				currentApplication.setExtId(Long.parseLong(row.getApplId()));
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
			//Long rowOrgId = new Long(row.getOrgId());
			currentOrg = orgHash.get(row.getOrgId());
			if (currentOrg == null) {
				currentOrg = new DatasetOrganization();
				currentOrg.setExtId(row.getOrgId());
				currentOrg.setNameEn(row.getOrgNameEn());
				currentOrg.setNameFr(row.getOrgNameFr());
				currentOrg.setDataset(dataset);
				currentOrg.setPostalZipCode(row.getPostalZipCode());
				currentOrg.setCity(row.getCity());
				currentOrg.setStateProvCode(row.getStateProvCode());
				currentOrg = datasetOrgRepo.save(currentOrg);
				orgHash.put(currentOrg.getExtId(), currentOrg);
				System.out.println("created DatasetOrganization: " + currentOrg);
			}
			row.fixPersonIdentifier();
			currentPerson = personHash.get(row.getPersonIdentifier());
			if (currentPerson == null) {
				currentPerson = new DatasetPerson();
				currentPerson.setExtId(row.getPersonIdentifier());
				currentPerson.setFamilyName(row.getFamilyName());
				currentPerson.setGivenName(row.getGivenName());
				currentPerson = datasetPersonRepo.save(currentPerson);
				personHash.put("" + currentPerson.getExtId(), currentPerson);
				System.out.println("created DatasetPerson: " + currentPerson);
			}
			ParticipationEdiData ediData = generateRandomEdi();
			ediData = ediRepo.save(ediData);
			DatasetApplicationRegistration appRegistration = new DatasetApplicationRegistration();
			appRegistration.setParticipationEdiData(ediData);
			appRegistration.setDatasetOrganization(currentOrg);
			appRegistration.setPerson(currentPerson);
			appRegistration.setRegistrationRole(currentAppRole);
			appRegistration.setDatasetApplication(currentApplication);
			appRegistrationRepo.save(appRegistration);
			System.out.println("created DatasetOrganization: " + appRegistration);

//			if(rowNum % 10 == 0) {
//				datasetRepo.save(dataset);
//			}
			
			System.out.println("Row #" + rowNum++);
			dataset.setCurrentRow(rowNum);
		}
		dataset.setTotalRecords(rowNum);
		datasetRepo.save(dataset);

	}

	private ParticipationEdiData generateRandomEdi() {
		ParticipationEdiData retval = new ParticipationEdiData();
		String[] genderOptions = { "Man", "Woman", "Other" };
		String[] indIdentityOptions = { "Dakelh", "Haida", "Huron", "Innu", "Iroquois", "Kichesipirni", "Mi'kmaq",
				"Syilx" };

		String[] visMinorityOptions = { "South Asian", "Chinese", "Black", "Latin American", "Arab", "Southest Asian",
				"West Asian", "Korean", "Japanese" };

		String[] disabilityOptions = { "Death", "Blind", "Physical", "Other" };

		Random random = new Random();

		int genderRandom = random.nextInt(10);
		int genderIndex = genderRandom / 5;
		retval.setGenderSelection(genderOptions[genderIndex]);

		retval.setVisMinRespPreferNot(false);
		int vizMinIndex = random.nextInt(visMinorityOptions.length - 1);
		if (vizMinIndex >= 4) {
			if (vizMinIndex == visMinorityOptions.length - 1) {
				retval.setVisMinRespPreferNot(true);
			} else {
				retval.setVisibleMinorityResponse(visMinorityOptions[random.nextInt(visMinorityOptions.length - 1)]);
			}
		}

		retval.setIndIdRespPreferNot(false);
		int indIdentityIndex = random.nextInt(indIdentityOptions.length - 1);
		if (indIdentityIndex > 5) {
			if (indIdentityIndex == indIdentityOptions.length - 1) {
				retval.setIndIdRespPreferNot(true);
			} else {
				retval.setIndIdentityResponse(indIdentityOptions[random.nextInt(indIdentityOptions.length - 1)]);
			}
		}

		int randomAge = random.nextInt(50);
		if (randomAge <= 5) {
			retval.setDateOfBirthPreferNot(true);
		} else {
			if (randomAge <= 17) {
				randomAge += 12;
			}
			try {
				retval.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("" + (2019 - randomAge) + "-1-1"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		retval.setDisabilityRespPreferNot(false);
		int disabilityRand = random.nextInt(20);
		if (disabilityRand >= 19) {
			int disabilityIndex = random.nextInt(disabilityOptions.length);
			if (disabilityIndex == disabilityOptions.length) {
				retval.setDisabilityRespPreferNot(true);
			} else {
				retval.setDisabilityResponse(disabilityOptions[disabilityIndex]);
			}
		}
		return retval;

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

	public Collection<ApplyDatasetRow> loadObjectList(String fileName) {
		Collection<ApplyDatasetRow> rows = null;
		// rows = loadObjectList(AwardDatasetRow.class, "datasets/" + fileName);

		Xcelite xcelite;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("datasets/" + fileName).getFile());
		xcelite = new Xcelite(file);
		XceliteSheet sheet = xcelite.getSheet(0);
		SheetReader<ApplyDatasetRow> reader = sheet.getBeanReader(ApplyDatasetRow.class);
		rows = reader.read();

		// return rows;
		return rows;

	}

	public Collection<AwardDatasetRow> loadAwardObjectList(String fileName) {
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
	public Program createProgramFromDatasetProg(DatasetProgram prog, Agency agency) {
		Program newProg = new Program();
		newProg.setNameEn(prog.getNameEn());
		newProg.setNameFr(prog.getNameFr());
		newProg.setLeadAgency(agency);
		newProg.setLeadAgency(prog.getLeadAgency());
		return newProg;

	}

	@Override
	public void linkDatasetOrg(DatasetOrganization org, Organization newOrg) {
		EntityLinkOrganization link = new EntityLinkOrganization();
		link.setDatasetConfiguration(org.getDataset().getDatasetConfiguration());
		link.setExtId(org.getExtId());
		link.setOrg(newOrg);
		entityLinkOrgRepo.save(link);

		org.setEntityLink(link);
		datasetOrgRepo.save(org);

	}

	@Override
	public void linkDatasetProgram(DatasetProgram prog, Program newProg) {
		EntityLinkProgram link = new EntityLinkProgram();
		link.setDatasetConfiguration(prog.getDataset().getDatasetConfiguration());
		link.setExtId(prog.getExtId());
		link.setProgram(newProg);
		entityLinkProgramRepo.save(link);

		prog.setEntityLink(link);
		datasetProgramRepo.save(prog);

	}

	@Override
	public boolean approveDataset(Long id) {
		boolean retval = false;
		Dataset ds = datasetRepo.getOne(id);
		if (ds != null) {
			List<Dataset> approvedDatasets = datasetRepo.findByDatasetStatusAndDatasetType(DatasetStatus.APPROVED,
					ds.getDatasetType());
			for (Dataset set : approvedDatasets) {
				if (set.getDatasetConfiguration().getId() == ds.getDatasetConfiguration().getId()) {
					set.setDatasetStatus(DatasetStatus.TO_DELETE);
					datasetRepo.save(set);
				}
			}
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

	@Override
	public long linkMatchingOrgEntities(Long id) {
		long retval = 0;
		List<Organization> orgs = orgRepo.findAll();
		for (DatasetOrganization datasetOrg : getUnlinkedDatasetOrgs(id)) {
			for (Organization targetOrg : orgs) {
				if (datasetOrg.getNameEn().compareTo(targetOrg.getNameEn()) == 0) {
					linkDatasetOrg(datasetOrg, targetOrg);
					retval++;
					break;
				}
			}

		}
		return retval;
	}

	@Override
	public long linkMatchingProgramEntities(Long id) {
		long retval = 0;
		List<Program> progs = programRepo.findAll();
		for (DatasetProgram datasetProg : getUnlinkedDatasetPrograms(id)) {
			for (Program targetProg : progs) {
				if (datasetProg.getNameEn().compareTo(targetProg.getNameEn()) == 0) {
					linkDatasetProgram(datasetProg, targetProg);
					retval++;
					break;
				}
			}

		}
		return retval;
	}

	@Override
	public List<DatasetOrganization> getUnlinkedDatasetOrgs(long datasetId) {
		List<DatasetOrganization> retval = datasetOrgRepo.findByDatasetIdAndEntityLinkIsNull(datasetId);
		return retval;
	}

	@Override
	public List<DatasetProgram> getUnlinkedDatasetPrograms(long datasetId) {
		List<DatasetProgram> retval = datasetProgramRepo.findByDatasetIdAndEntityLinkIsNull(datasetId);
		return retval;

	}

	@Override
	public long linkToProgramEntityLinks(Long datasetId) {
		long retval = 0;
		Dataset dataset = getDataset(datasetId);
		List<EntityLinkProgram> links = entityLinkProgramRepo
				.findByDatasetConfigurationId(dataset.getDatasetConfiguration().getId());
		for (DatasetProgram datasetProg : getUnlinkedDatasetPrograms(datasetId)) {
			for (EntityLinkProgram link : links) {
				if (datasetProg.getExtId().compareTo(link.getExtId()) == 0) {
					datasetProg.setEntityLink(link);
					datasetProgramRepo.save(datasetProg);
					retval++;
					break;
				}
			}

		}
		return retval;
	}

	@Override
	public long linkToOrgEntityLinks(Long datasetId) {
		long retval = 0;
		Dataset dataset = getDataset(datasetId);
		List<EntityLinkOrganization> links = entityLinkOrgRepo
				.findByDatasetConfigurationId(dataset.getDatasetConfiguration().getId());
		for (DatasetOrganization datasetOrg : getUnlinkedDatasetOrgs(datasetId)) {
			for (EntityLinkOrganization link : links) {
				if (datasetOrg.getExtId().compareTo(link.getExtId()) == 0) {
					datasetOrg.setEntityLink(link);
					datasetOrgRepo.save(datasetOrg);
					retval++;
					break;
				}
			}

		}
		return retval;
	}

	@Override
	public List<EntityLinkProgram> getDatasetConfigProgramLinks(long id) {
		return entityLinkProgramRepo.findByDatasetConfigurationId(id);
	}

	@Override
	public List<EntityLinkOrganization> getDatasetConfigOrgLinks(long id) {
		return entityLinkOrgRepo.findByDatasetConfigurationId(id);
	}

}
