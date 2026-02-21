package br.com.luiz.bbbpredict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class BbbPredictBotApplication {

	static void main(String[] args) {
		SpringApplication.run(BbbPredictBotApplication.class, args);
		System.out.println("API Inicializada!");
	}

}
