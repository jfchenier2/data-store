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

			GrantSystemCapability amisApplsCapability = new GrantSystemCapability();
			amisApplsCapability.setGrantingSystem(amis);
			amisApplsCapability.setGrantingFunction(GrantingFunction.APPLY);
			amisApplsCapability.setNameEn("AMIS Applications");
			amisApplsCapability.setNameFr("Applications de AMIS");
			amisApplsCapability.setAcronymEn("AMIS_APPS");
			amisApplsCapability.setAcronymFr("AMIS_APPS");
			systemCapacityRepo.save(amisApplsCapability);

			DatasetConfiguration amisApplsConfig = new DatasetConfiguration();
			amisApplsConfig.setAcronym("AMISMASTER");
			amisApplsConfig.setGrantSystemCapability(amisApplsCapability);
			amisApplsConfig.setNameEn("AMIS Master dataset");
			amisApplsConfig.setNameFr("Ensemble de donnees AMIS Complet");
			configRepo.save(amisApplsConfig);

			GrantSystemCapability amisAwardsCapability = new GrantSystemCapability();
			amisAwardsCapability.setGrantingSystem(amis);
			amisAwardsCapability.setGrantingFunction(GrantingFunction.APPLY);
			amisAwardsCapability.setNameEn("AMIS Applications");
			amisAwardsCapability.setNameFr("Applications de AMIS");
			amisAwardsCapability.setAcronymEn("AMIS_APPS");
			amisAwardsCapability.setAcronymFr("AMIS_APPS");
			systemCapacityRepo.save(amisAwardsCapability);

			DatasetConfiguration amisAwardsConfig = new DatasetConfiguration();
			amisAwardsConfig.setAcronym("AMISAWARDS");
			amisAwardsConfig.setGrantSystemCapability(amisApplsCapability);
			amisAwardsConfig.setNameEn("AMIS Awards dataset");
			amisAwardsConfig.setNameFr("Ensemble de donnees AMIS ");
			configRepo.save(amisAwardsConfig);

			GrantSystemCapability namisApplsCapability = new GrantSystemCapability();
			namisApplsCapability.setGrantingSystem(namis);
			namisApplsCapability.setGrantingFunction(GrantingFunction.APPLY);
			namisApplsCapability.setNameEn("NAMIS Applications");
			namisApplsCapability.setNameFr("Applications de NAMIS");
			namisApplsCapability.setAcronymEn("NAMIS_APPS");
			namisApplsCapability.setAcronymFr("NAMIS_APPS");
			systemCapacityRepo.save(namisApplsCapability);

			DatasetConfiguration namisApplsConfig = new DatasetConfiguration();
			namisApplsConfig.setAcronym("NAMISMASTER");
			namisApplsConfig.setGrantSystemCapability(namisApplsCapability);
			namisApplsConfig.setNameEn("NAMIS Master dataset");
			namisApplsConfig.setNameFr("Ensemble de donnees NAMIS Complet");
			configRepo.save(namisApplsConfig);

			GrantSystemCapability namisAwardsCapability = new GrantSystemCapability();
			namisAwardsCapability.setGrantingSystem(namis);
			namisAwardsCapability.setGrantingFunction(GrantingFunction.AWARD);
			namisAwardsCapability.setNameEn("NAMIS Applications");
			namisAwardsCapability.setNameFr("Applications de NAMIS");
			namisAwardsCapability.setAcronymEn("NAMIS_APPS");
			namisAwardsCapability.setAcronymFr("NAMIS_APPS");
			systemCapacityRepo.save(namisAwardsCapability);

			DatasetConfiguration namisAwardsConfig = new DatasetConfiguration();
			namisAwardsConfig.setAcronym("NAMISAWARDS");
			namisAwardsConfig.setGrantSystemCapability(namisApplsCapability);
			namisAwardsConfig.setNameEn("NAMIS Awards dataset");
			namisAwardsConfig.setNameFr("Ensemble de awards NAMIS");
			configRepo.save(namisAwardsConfig);

		}

	}

}
