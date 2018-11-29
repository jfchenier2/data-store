package ca.gc.triagency.datastore.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ca.gc.triagency.datastore.model.util.LocalizedParametersModel;

@Entity
public class DatasetProgram implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String extId;

	@ManyToOne
	@JoinColumn(name = "dataset_configuration_id")
	private DatasetConfiguration datasetConfiguration;

	private String nameEn;

	private String nameFr;

	public String toString() {
		return "DatasetProgram: " + id + " : " + extId + " : " + nameEn + " : " + nameFr;
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

	public String getExtId() {
		return extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public DatasetConfiguration getDatasetConfiguration() {
		return datasetConfiguration;
	}

	public void setDatasetConfiguration(DatasetConfiguration datasetConfiguration) {
		this.datasetConfiguration = datasetConfiguration;
	}
}
