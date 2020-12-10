package ru.klavogonki.kgparser.springboot;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.jsonParser.repository.PlayerRepository;

import java.time.LocalDateTime;

@SpringBootApplication
@EntityScan(basePackages= {"ru.klavogonki.kgparser.jsonParser.entity"})
@ComponentScan({"ru.klavogonki.kgparser"})
@EnableJpaRepositories("ru.klavogonki.kgparser")
@Log4j2
public class KgParserApplication implements CommandLineRunner {

	@Autowired
	private PlayerRepository playerRepository;

	public static void main(String[] args) {
		SpringApplication.run(KgParserApplication.class, args);
	}

	@Override
	public void run(final String... args) {
		logger.debug("Starting player import...");

		PlayerEntity player = new PlayerEntity();
		player.setPlayerId(666);
		player.setLogin("nosferatum");
		player.setRegistered(LocalDateTime.now());
		playerRepository.save(player);

		logger.debug("Successfully created player {}. Player dbId: {}.", player.getLogin(), player.getDbId());
	}
}
