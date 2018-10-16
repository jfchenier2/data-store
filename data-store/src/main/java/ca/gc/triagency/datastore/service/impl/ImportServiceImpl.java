package ca.gc.triagency.datastore.service.impl;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;

import ca.gc.triagency.datastore.model.Agency;
import ca.gc.triagency.datastore.model.Program;
import ca.gc.triagency.datastore.model.file.ProgramFromFile;
import ca.gc.triagency.datastore.repo.AgencyRepository;
import ca.gc.triagency.datastore.repo.ProgramRepository;
import ca.gc.triagency.datastore.service.ImportService;

@Service
public class ImportServiceImpl implements ImportService {
	/** Logger */
	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	ProgramRepository programRepo;

	@Autowired
	AgencyRepository agencyRepo;

	private HashMap<String, Agency> agencyMap;

	void importAll() {

	}

	public void importAgencies() {
		Agency sshrc, nserc, cihr;
		sshrc = agencyRepo.save(new Agency("Social Sciences and Humanities Research Council",
				"Conseil de recherches en sciences humaines", "SSHRC", "CRSH"));
		cihr = agencyRepo.save(new Agency("Canadian Institutes of Health Research",
				"Instituts de recherche en santé du Canada", "CIHR", "CRSC"));
		nserc = agencyRepo.save(nserc = new Agency("Natural Sciences and Engineering Research Council of Canada",
				"Conseil de recherches en sciences naturelles et en génie du Canada", "NSERC", "CRSNG"));

		agencyMap = new HashMap<String, Agency>();
		agencyMap.put(sshrc.getAcronymEn(), sshrc);
		agencyMap.put(cihr.getAcronymEn(), cihr);
		agencyMap.put(nserc.getAcronymEn(), nserc);
		agencyRepo.saveAll(agencyMap.values());
	}

	public void importProgramsFromFile() {

		if (agencyMap == null) {
			importAgencies();
		}
		Collection<ProgramFromFile> programsFromFile = extractProgramsFromFile("programFile.xlsm");
		for (ProgramFromFile p : programsFromFile) {
			Program newProgram = new Program();
			if (p.getNameEn().trim().length() == 0) {
				LOG.log(Level.WARN, "empty name, assuming empty row.  skipping row");
				continue;
			}
			newProgram.setNameEn(p.getNameEn());
			newProgram.setNameFr(p.getNameFr());
			newProgram.setDivision(p.getDivision());
			newProgram.setFundingType(p.getFundingType());
			Agency leadAgency = agencyMap.get(p.getLeadAgency());
			newProgram.setLeadAgency(leadAgency);
			if (leadAgency == null) { // ASSUMPTION
				newProgram.setLeadAgency(agencyMap.get("NSERC"));
			}

			// ASSUMPTION: IF BLANK, THEN ITS SINGLE. ??
			if (p.getNumberOfAgencies() == null) {
				p.setNumberOfAgencies("Single");
			}
			String numAgencyText = p.getNumberOfAgencies().toLowerCase();
			if (numAgencyText.indexOf("single", 0) >= 0) {
				newProgram.getAgencies().add(leadAgency);
			} else {
				if (numAgencyText.indexOf("tri") >= 0) {
					newProgram.getAgencies().addAll(agencyMap.values());
				} else {
					if (numAgencyText.indexOf("bi") >= 0) {
						newProgram.getAgencies().add(agencyMap.get("NSERC"));
						newProgram.getAgencies().add(agencyMap.get("SSHRC"));
					} else {
						LOG.log(Level.ERROR, "unknown config for 'number of agencies':: " + numAgencyText);
					}
				}
			}

			newProgram.setFrequency(p.getFrequency());
			newProgram.setApplyMethod(p.getApplyMethod());
			newProgram.setAwardManagementSystem(p.getAwardManagementSystem());
			newProgram.setProgramLeadName(p.getProgramLeadName());

			LOG.log(Level.INFO, "about to store:" + newProgram);
			System.out.println("about to store:" + newProgram);
			programRepo.save(newProgram);

		}

	}

	public Collection<ProgramFromFile> extractProgramsFromFile(String filename) {
		Collection<ProgramFromFile> programs = null;

		Xcelite xcelite;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
		xcelite = new Xcelite(file);
		XceliteSheet sheet = xcelite.getSheet(0);
		SheetReader<ProgramFromFile> reader = sheet.getBeanReader(ProgramFromFile.class);
		programs = reader.read();

		return programs;

	}

}
