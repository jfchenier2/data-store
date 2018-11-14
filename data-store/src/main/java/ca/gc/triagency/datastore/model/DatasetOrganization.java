package ca.gc.triagency.datastore.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ca.gc.triagency.datastore.model.util.LocalizedParametersModel;

@Entity
public class DatasetOrganization implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long extId;

	private String nameEn;

	private String nameFr;
	// private String address;
	// private String contactInfo;

	@ManyToOne
	@JoinColumn(name = "dataset_configuration_id")
	private DatasetConfiguration datasetConfiguration;

	@ManyToOne
	@JoinColumn(name = "artifact_link_organization_id", nullable = true)
	private ArtifactLinkOrganization link;

	@Override
	public String toString() {
		return "" + id + " : " + extId + " : " + nameEn + " / " + nameFr;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameFr() {
		return nameFr;
	}

	public void setNameFr(String nameFr) {
		this.nameFr = nameFr;
	}

	public Long getId() {
		return id;
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

	public ArtifactLinkOrganization getLink() {
		return link;
	}

	public void setLink(ArtifactLinkOrganization link) {
		this.link = link;
	}
}
