package ca.gc.triagency.datastore.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.ApprovedApplication;

public interface ViewApprovedApplications extends JpaRepository<ApprovedApplication, Long> {

	List<ApprovedApplication> findByAgencyNameEn(String userAgencyAcronym);

}
