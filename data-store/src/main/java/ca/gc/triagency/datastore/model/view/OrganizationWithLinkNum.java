package ca.gc.triagency.datastore.model.view;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import ca.gc.triagency.datastore.model.util.LocalizedParametersModel;

@Entity
@Table(name = "orgs_with_external_link_num", schema = "data_cabin")
public class OrganizationWithLinkNum implements LocalizedParametersModel {
	@Id
	private Long id;

	private String nameEn;

	private String nameFr;

	@Column(name = "link_num")
	private long linkNum;

	@CreationTimestamp
	private LocalDateTime createDateTime;

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

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Long getId() {
		return id;
	}

	public long getLinkNum() {
		return linkNum;
	}

	public void setLinkNum(long linkNum) {
		this.linkNum = linkNum;
	}

	// @UpdateTimestamp
	// private LocalDateTime updateDateTime;

}
