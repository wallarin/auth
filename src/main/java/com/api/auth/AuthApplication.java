package com.api.auth;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableEncryptableProperties
@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {

		//SpringApplication.run(AuthApplication.class, args);
		SpringApplication app = new SpringApplication(AuthApplication.class);

		// 운영체제에 따라 프로파일 자동 설정
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			app.setAdditionalProfiles("windows");
		} else if (os.contains("nux") || os.contains("nix")) {
			app.setAdditionalProfiles("linux");
		}

		app.run(args);

	}

}
