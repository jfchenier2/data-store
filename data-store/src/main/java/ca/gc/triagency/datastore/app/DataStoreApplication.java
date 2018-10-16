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

	public static void main(String[] args) {
		SpringApplication.run(DataStoreApplication.class, args);
	}

	// @Bean
	// public CommandLineRunner importBaseFileData() {
	// return (args) -> {
	// importService.importProgramsFromFile();
	// };
	// }
}
