package SotonHKPASS.Website_testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan(basePackages = { "java.SotonHKPASS.Website_testing.controller",
		"java.SotonHKPASS.Website_testing.implementation", "java.SotonHKPASS.Website_testing.entity",
		"java.SotonHKPASS.Website_testing.repository",
		"java.SotonHKPASS.Website_testing.services" })
public class WebsiteTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsiteTestingApplication.class, args);
	}

}
