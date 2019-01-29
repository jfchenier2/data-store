package ca.gc.triagency.datastore.model.file;

import com.ebay.xcelite.annotations.Column;

public class AwardDatasetRow {

	@Column(name = "Source")
	private String source;

	@Column(name = "Application_Identifier")
	private String applicationIdentifier;

	@Column(name = "Appl_ID")
	private String applicationId;

	@Column(name = "Competition_Year")
	private String competitionYear;

	@Column(name = "Program_ID")
	private String programId;

	@Column(name = "Program_English")
	private String programEn;

	@Column(name = "Program_French")
	private String programFr;

	@Column(name = "Create_Date")
	private String createDate;

	@Column(name = "Role_Code")
	private String roleCode;

	@Column(name = "Role_English")
	private String roleEn;

	@Column(name = "Role_French")
	private String roleFr;

	@Column(name = "Family_Name")
	private String familyName;

	@Column(name = "Given_Name")
	private String givenName;

	@Column(name = "Person_Identifier")
	private String personIdentifier;

	@Column(name = "Organization_ID")
	private String orgId;

	@Column(name = "Organization_Name_English")
	private String orgNameEn;

	@Column(name = "Organization_Name_French")
	private String orgNameFr;

	@Column(name = "Awarded_Amount")
	private String awardedAmmount;

	// @Column(name = "Grant_Awarded_Amount")
	// private String grantAwardedAmmount;
	//
	// @Column(name = "Scholarship_Awarded_Amount")
	// private String scolarshipAwardedAmmount;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getApplicationIdentifier() {
		return applicationIdentifier;
	}

	public void setApplicationIdentifier(String applicationIdentifier) {
		this.applicationIdentifier = applicationIdentifier;
	}

	public String getCompetitionYear() {
		return competitionYear;
	}

	public void setCompetitionYear(String competitionYear) {
		this.competitionYear = competitionYear;
	}

	public String getProgramEn() {
		return programEn;
	}

	public void setProgramEn(String programEn) {
		this.programEn = programEn;
	}

	public String getProgramFr() {
		return programFr;
	}

	public void setProgramFr(String programFr) {
		this.programFr = programFr;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleEn() {
		return roleEn;
	}

	public void setRoleEn(String roleEn) {
		this.roleEn = roleEn;
	}

	public String getRoleFr() {
		return roleFr;
	}

	public void setRoleFr(String roleFr) {
		this.roleFr = roleFr;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getPersonIdentifier() {
		return personIdentifier;
	}

	public void setPersonIdentifier(String personIdentifier) {
		this.personIdentifier = personIdentifier;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public void fixApplicationId() {
		int dotLocation = applicationId.indexOf(".");
		if (dotLocation > 0) {
			applicationId = applicationId.substring(0, dotLocation);
		}
	}

	public void fixOrgId() {
		int dotLocation = orgId.indexOf(".");
		if (dotLocation > 0) {
			orgId = orgId.substring(0, dotLocation);
		}
	}

	public void fixPersonId() {
		int dotLocation = personIdentifier.indexOf(".");
		if (dotLocation > 0) {
			personIdentifier = personIdentifier.substring(0, dotLocation);
		}
	}

	public String getOrgNameEn() {
		return orgNameEn;
	}

	public void setOrgNameEn(String orgNameEn) {
		this.orgNameEn = orgNameEn;
	}

	public String getOrgNameFr() {
		return orgNameFr;
	}

	public void setOrgNameFr(String orgNameFr) {
		this.orgNameFr = orgNameFr;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getAwardedAmmount() {
		return awardedAmmount;
	}

	public void setAwardedAmmount(String awardedAmmount) {
		this.awardedAmmount = awardedAmmount;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	// public String getGrantAwardedAmmount() {
	// return grantAwardedAmmount;
	// }
	//
	// public void setGrantAwardedAmmount(String grantAwardedAmmount) {
	// this.grantAwardedAmmount = grantAwardedAmmount;
	// }
	//
	// public String getScolarshipAwardedAmmount() {
	// return scolarshipAwardedAmmount;
	// }
	//
	// public void setScolarshipAwardedAmmount(String scolarshipAwardedAmmount)
	// {
	// this.scolarshipAwardedAmmount = scolarshipAwardedAmmount;
	// }
}
