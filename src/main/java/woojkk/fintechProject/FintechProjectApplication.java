package woojkk.fintechProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
@ServletComponentScan
public class FintechProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FintechProjectApplication.class, args);
	}

}
