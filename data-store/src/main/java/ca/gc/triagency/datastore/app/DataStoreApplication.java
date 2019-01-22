package ca.gc.triagency.datastore.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ca.gc.triagency.datastore.service.ImportService;

@SpringBootApplication
@ComponentScan("ca.gc.triagency.datastore")
@EntityScan("ca.gc.triagency.datastore.model")
@EnableJpaRepositories(basePackages = { "ca.gc.triagency.datastore.repo" })
public class DataStoreApplication {

	@Autowired
	ImportService importService;

	// private final ApplicationContext applicationContext;

	// @Autowired
	// public DataStoreApplication(ApplicationContext applicationContext) {
	// this.applicationContext = applicationContext;
	// }

	public static void main(String[] args) {
		SpringApplication.run(DataStoreApplication.class, args);
	}

	// private void setReferralForContext() {
	// LdapTemplate ldapTemplate =
	// applicationContext.getBean(LdapTemplate.class);
	// // above is necessary for LdapContextSource to be created
	//
	// LdapContextSource ldapContextSource =
	// applicationContext.getBean(LdapContextSource.class);
	// ldapContextSource.setReferral("follow");
	// ldapContextSource.afterPropertiesSet();
	// }

}
