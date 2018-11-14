package ca.gc.triagency.datastore.app.setup;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.gc.triagency.datastore.model.DatasetConfiguration;
import ca.gc.triagency.datastore.model.GrantSystemCapability;
import ca.gc.triagency.datastore.model.GrantSystemCapability.GrantingFunction;
import ca.gc.triagency.datastore.model.GrantingSystem;
import ca.gc.triagency.datastore.repo.DatasetConfigurationRepository;
import ca.gc.triagency.datastore.repo.GrantSystemCapacityRepository;
import ca.gc.triagency.datastore.repo.GrantingSystemRepository;

@Component
public class DataSetup {
	@Autowired
	GrantingSystemRepository grantingSystemRepo;

	@Autowired
	GrantSystemCapacityRepository systemCapacityRepo;

	@Autowired
	DatasetConfigurationRepository configRepo;

	@PostConstruct
	private void setupData() {
		if (grantingSystemRepo.findAll().isEmpty()) {
			GrantingSystem rp1 = new GrantingSystem();
			rp1.setNameEn("Research Portal");
			rp1.setNameEn("Portail de Recherche");
			rp1.setAcronymEn("RP1");
			rp1.setAcronymFr("RP1");
			grantingSystemRepo.save(rp1);

			GrantingSystem crm = new GrantingSystem();
			crm.setNameEn("CRM");
			crm.setNameEn("CRM");
			crm.setAcronymEn("CRM");
			crm.setAcronymFr("CRM");
			grantingSystemRepo.save(crm);

			GrantingSystem sshrcOnline = new GrantingSystem();
			sshrcOnline.setNameEn("SSHRC Online");
			sshrcOnline.setNameFr("SSHRC Online");
			sshrcOnline.setAcronymEn("SSHRC Online");
			sshrcOnline.setAcronymFr("SSHRC Online");
			grantingSystemRepo.save(sshrcOnline);

			GrantingSystem amis = new GrantingSystem();
			amis.setNameEn("AMIS");
			amis.setNameFr("AMIS");
			amis.setAcronymEn("AMIS");
			amis.setAcronymFr("AMIS");
			grantingSystemRepo.save(amis);

			GrantingSystem nsercOnline = new GrantingSystem();
			nsercOnline.setNameEn("NSERC Online");
			nsercOnline.setNameFr("NSERC Online");
			nsercOnline.setAcronymEn("NSERC Online");
			nsercOnline.setAcronymFr("NSERC Online");
			grantingSystemRepo.save(nsercOnline);

			GrantingSystem namis = new GrantingSystem();
			namis.setNameEn("NAMIS");
			namis.setNameFr("NAMIS");
			namis.setAcronymEn("NAMIS");
			namis.setAcronymFr("NAMIS");
			grantingSystemRepo.save(namis);

			GrantSystemCapability capability = new GrantSystemCapability();
			capability.setGrantingSystem(rp1);
			capability.setGrantingFunction(GrantingFunction.APPLY);
			capability.setNameEn("RP1 Applications");
			capability.setNameFr("Applications de Portail");
			capability.setAcronymEn("RP1_APPS");
			capability.setAcronymFr("RP1_APPS");
			systemCapacityRepo.save(capability);

			DatasetConfiguration config = new DatasetConfiguration();
			config.setAcronym("RP1MASTER");
			config.setGrantSystemCapability(capability);
			config.setNameEn("RP1 Master datset");
			config.setNameFr("Info packet RP1");
			configRepo.save(config);
		}

	}

}
