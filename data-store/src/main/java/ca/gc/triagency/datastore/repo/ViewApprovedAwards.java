package ca.gc.triagency.datastore.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.ApprovedAward;

public interface ViewApprovedAwards extends JpaRepository<ApprovedAward, Long> {

	List<ApprovedAward> findByAgencyNameEn(String userAgencyAcronym);

}
