package it.univaq.msa.pwrtool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
	
/**
 * Hello world!
 *
 */
@ComponentScan
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:cfg.properties")
public class App {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);

	}

}
