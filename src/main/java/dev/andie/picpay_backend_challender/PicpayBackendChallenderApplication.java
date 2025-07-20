package dev.andie.picpay_backend_challender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@EnableJdbcAuditing
@SpringBootApplication
public class PicpayBackendChallenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(PicpayBackendChallenderApplication.class, args);
	}

}
