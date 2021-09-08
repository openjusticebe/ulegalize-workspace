package com.ulegalize.lawfirm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.ulegalize.lawfirm")
@EntityScan(value = "com.ulegalize.lawfirm", basePackageClasses = {Jsr310JpaConverters.class})
@SpringBootApplication
public class UlegalizeLawfirmApplication {

	public static void main(String[] args) {
		SpringApplication.run(UlegalizeLawfirmApplication.class, args);
	}
}
