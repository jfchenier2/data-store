package ca.gc.triagency.datastore.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class EntityLinkOrganization {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "dataset_config_id")
	private DatasetConfiguration datasetConfiguration;

	private Long extId;

	@ManyToOne
	@JoinColumn(name = "org_id")
	private Organization org;

	public Long getId() {
		return id;
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public Long getExtId() {
		return extId;
	}

	public void setExtId(Long extId) {
		this.extId = extId;
	}

	public DatasetConfiguration getDatasetConfiguration() {
		return datasetConfiguration;
	}

	public void setDatasetConfiguration(DatasetConfiguration datasetConfiguration) {
		this.datasetConfiguration = datasetConfiguration;
	}

}
