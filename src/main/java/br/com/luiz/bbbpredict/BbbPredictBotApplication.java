package br.com.luiz.bbbpredict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BbbPredictBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(BbbPredictBotApplication.class, args);
	}

}
