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

	@ManyToOne
	@JoinColumn(name = "entity_link_id", nullable = true)
	private EntityLinkOrganization entityLink;

	private Long extId;

	private String nameEn;

	private String nameFr;

	@ManyToOne
	@JoinColumn(name = "dataset_id")
	private Dataset dataset;

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

	public EntityLinkOrganization getEntityLink() {
		return entityLink;
	}

	public void setEntityLink(EntityLinkOrganization entityLink) {
		this.entityLink = entityLink;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}
}
