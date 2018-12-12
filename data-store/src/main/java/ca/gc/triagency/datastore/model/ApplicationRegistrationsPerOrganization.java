package ca.gc.triagency.datastore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ca.gc.triagency.datastore.model.util.LocalizedParametersModel;
//@Query(value = "select org.id as orgId, org.name_en, org.name_fr, count(*) as app_reg_num, count(Distinct app.id) as app_num, count(Distinct app.dataset_program_id) as program_num\r\n"
//		+ "	    from data_cabin.dataset_application_registration as reg \r\n"
//		+ "			join data_cabin.dataset_organization ds_org \r\n"
//		+ "				on reg.dataset_organization_id=ds_org.id            \r\n"
//		+ "			join data_cabin.entity_link_organization as link_org\r\n"
//		+ "				on ds_org.entity_link_id = link_org.id\r\n"
//		+ "			join data_cabin.organization as org                  \r\n"
//		+ "				on link_org.org_id = org.id\r\n"
//		+ "	        join data_cabin.dataset_application as app\r\n"
//		+ "				on app.id = reg.dataset_application_id           \r\n"
//		+ "			join data_cabin.dataset as ds\r\n"
//		+ "				on app.dataset_id = ds.id and ds.dataset_status like 'APPROVED'\r\n"
//		+ "			group by orgId;")

@Entity
@Table(name = "report_application_registrations_per_organization", schema = "data_cabin")
public class ApplicationRegistrationsPerOrganization implements LocalizedParametersModel {
	@Id
	private Long id;

	private String nameEn;

	private String nameFr;

	@Column(name = "app_reg_num")
	private long appRegistrationCount;

	@Column(name = "app_num")
	private Long applicationCount;

	@Column(name = "program_num")
	private Long programCount;

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

	public Long getApplicationCount() {
		return applicationCount;
	}

	public void setApplicationCount(Long applicationCount) {
		this.applicationCount = applicationCount;
	}

	public Long getProgramCount() {
		return programCount;
	}

	public void setProgramCount(Long programCount) {
		this.programCount = programCount;
	}

}
