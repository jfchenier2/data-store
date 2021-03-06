package ca.gc.triagency.datastore.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.context.i18n.LocaleContextHolder;

import ca.gc.triagency.datastore.form.ProgramForm;
import ca.gc.triagency.datastore.model.util.LocalizedParametersModel;

@Entity
public class Program implements LocalizedParametersModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nameEn;

	private String nameFr;

	@ManyToOne
	@JoinColumn(name = "lead_agency_id")
	private Agency leadAgency;

	private String division;

	private String fundingType;

	private String frequency;

	private String applyMethod;

	private String awardManagementSystem;

	private String programLeadName;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable
	private Set<GrantSystemCapability> grantingCapabilities;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable
	private Set<Agency> agencies;

	public Program() {
		agencies = new HashSet<Agency>();
	}

	public void loadFromForm(ProgramForm f) {
		this.setAgencies(f.getAgencies());
		this.setApplyMethod(f.getApplyMethod());
		this.setAwardManagementSystem(f.getAwardManagementSystem());
		this.setDivision(f.getDivision());
		this.setFundingType(f.getFundingType());
		this.setFrequency(f.getFrequency());
		this.setLeadAgency(f.getLeadAgency());
		this.setNameEn(f.getNameEn());
		this.setNameFr(f.getNameFr());
		this.setProgramLeadName(f.getProgramLeadName());
	}

	public String getName() {
		String retval = "";
		if (LocaleContextHolder.getLocale().toString().contains("en")) {
			retval = getNameEn();
		} else {
			retval = getNameFr();
		}
		return retval;
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

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getApplyMethod() {
		return applyMethod;
	}

	public void setApplyMethod(String applyMethod) {
		this.applyMethod = applyMethod;
	}

	public String getAwardManagementSystem() {
		return awardManagementSystem;
	}

	public void setAwardManagementSystem(String awardManagementSystem) {
		this.awardManagementSystem = awardManagementSystem;
	}

	public String getProgramLeadName() {
		return programLeadName;
	}

	public void setProgramLeadName(String programLeadName) {
		this.programLeadName = programLeadName;
	}

	public Set<Agency> getAgencies() {
		return agencies;
	}

	public void setAgencies(Set<Agency> agencies) {
		this.agencies = agencies;
	}

	public Agency getLeadAgency() {
		return leadAgency;
	}

	public void setLeadAgency(Agency leadAgency) {
		this.leadAgency = leadAgency;
	}

	public Long getId() {
		return id;
	}

	public String toString() {
		return "" + id + " : " + nameEn + " :: " + nameFr + " :: " + leadAgency;

	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getFundingType() {
		return fundingType;
	}

	public void setFundingType(String fundingType) {
		this.fundingType = fundingType;
	}

	public Set<GrantSystemCapability> getGrantingCapabilities() {
		return grantingCapabilities;
	}

	public void setGrantingCapabilities(Set<GrantSystemCapability> grantingCapabilities) {
		this.grantingCapabilities = grantingCapabilities;
	}
}
