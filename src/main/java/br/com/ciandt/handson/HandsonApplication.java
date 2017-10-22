package br.com.ciandt.handson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class HandsonApplication {

	public static void main(String[] args) {
		SpringApplication.run(HandsonApplication.class, args);		
	}

	@Bean
	public WebMvcConfigurerAdapter redirectToIndex() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addViewControllers(ViewControllerRegistry registry) {
				registry.addViewController("/").setViewName("redirect:/handson");
			}
		};
	}
}
