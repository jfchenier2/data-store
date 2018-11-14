package ca.gc.triagency.datastore.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DatasetPerson {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long extId;

	private String familyName;
	private String givenName;

	public String toString() {
		return "" + id + " : " + extId + " : " + familyName + " : " + givenName;

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

}
