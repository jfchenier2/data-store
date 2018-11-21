package ca.gc.triagency.datastore.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.triagency.datastore.model.view.OrganizationWithLinkNum;

public interface ViewOrgsWithLinkNumRepository extends JpaRepository<OrganizationWithLinkNum, Long> {

}
