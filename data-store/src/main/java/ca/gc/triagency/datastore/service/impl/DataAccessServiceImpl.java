package ca.gc.triagency.datastore.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gc.triagency.datastore.model.Agency;
import ca.gc.triagency.datastore.model.Program;
import ca.gc.triagency.datastore.repo.AgencyRepository;
import ca.gc.triagency.datastore.repo.ProgramRepository;
import ca.gc.triagency.datastore.service.DataAccessService;

@Service
public class DataAccessServiceImpl implements DataAccessService {
	/** Logger */
	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	ProgramRepository programRepo;
	@Autowired
	AgencyRepository agencyRepo;

	@Override
	public List<Program> getAllPrograms() {
		return programRepo.findAll();
	}

	@Override
	public Program getProgram(Long id) {
		return programRepo.findById(id).orElse(null);
	}

	@Override
	public List<Agency> getAllAgencies() {
		return agencyRepo.findAll();
	}

	@Override
	public Program saveProgram(Program p) {
		return programRepo.save(p);
	}

}
