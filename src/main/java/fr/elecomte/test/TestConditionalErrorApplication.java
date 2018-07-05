package fr.elecomte.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import fr.elecomte.test.services.impls.StandardSampleService;

/**
 * @author elecomte
 * @since v1.0.0
 * @version 1
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = StandardSampleService.class)
public class TestConditionalErrorApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(TestConditionalErrorApplication.class, args);
	}

}