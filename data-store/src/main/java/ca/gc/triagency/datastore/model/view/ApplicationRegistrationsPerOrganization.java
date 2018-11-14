package ca.gc.triagency.datastore.model.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ca.gc.triagency.datastore.model.util.LocalizedParametersModel;

@Entity
@Table(name = "report_application_registrations_per_organization", schema = "data_cabin")
public class ApplicationRegistrationsPerOrganization implements LocalizedParametersModel {
	@Id
	private Long id;

	private String nameEn;

	private String nameFr;

	@Column(name = "app_num")
	private long appRegistrationCount;

	public ApplicationRegistrationsPerOrganization() {

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

	public long getAppRegistrationCount() {
		return appRegistrationCount;
	}

	public void setAppRegistrationCount(long appRegistrationCount) {
		this.appRegistrationCount = appRegistrationCount;
	}

}
