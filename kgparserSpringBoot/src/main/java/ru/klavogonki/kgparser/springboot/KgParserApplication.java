package ru.klavogonki.kgparser.springboot;

import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.klavogonki.kgparser.PlayerJsonData;
import ru.klavogonki.kgparser.PlayerJsonParser;
import ru.klavogonki.kgparser.PlayerSummaryDownloader;
import ru.klavogonki.kgparser.export.ExportContext;
import ru.klavogonki.kgparser.export.IndexPageExporter;
import ru.klavogonki.kgparser.export.TopBySpeedExporter;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.jsonParser.mapper.PlayerMapper;
import ru.klavogonki.kgparser.jsonParser.repository.PlayerRepository;
import ru.klavogonki.kgparser.util.DateUtils;

import java.util.Optional;

@SpringBootApplication
@EntityScan(basePackages= {"ru.klavogonki.kgparser.jsonParser.entity"})
@ComponentScan({"ru.klavogonki.kgparser"})
@EnableJpaRepositories("ru.klavogonki.kgparser")
@Log4j2
public class KgParserApplication implements CommandLineRunner {
	public static final int REQUIRED_ARGUMENTS_COUNT = 4;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private IndexPageExporter indexPageExporter;

	@Autowired
	private TopBySpeedExporter topBySpeedExporter;

	// todo: autowire it, @see https://mapstruct.org/documentation/stable/reference/html/#using-dependency-injection
	private final PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

	public static void main(String[] args) {
		SpringApplication.run(KgParserApplication.class, args);
	}

	@Override
	public void run(final String... args) {
		// todo: parse context from args of from json file given by args
		ExportContext context = new ExportContext();
		context.webRootDir = "C:/java/kgparser/kgparserWeb/src/main/webapp/";
		context.minPlayerId = 1;
		context.minPlayerId = 625000;
		context.dataDownloadStartDate = DateUtils.parseLocalDateTimeWithUiDateFormat("2020-12-08 02:39:07");
		context.dataDownloadEndDate = DateUtils.parseLocalDateTimeWithUiDateFormat("2020-12-09 16:28:01");

		// todo: select mode (what to do) by arguments

/*
		indexPageExporter.export(context);
		if (true) {
			return;
		}
*/

		topBySpeedExporter.export(context);
		if (true) {
			return;
		}


		// todo: pass a path to a json file with config instead

		if (args.length != REQUIRED_ARGUMENTS_COUNT) {
			// todo: use logger instead of System.out??
			System.out.printf("Usage: %s <rootJsonDir> <minPlayerId> <maxPlayerId> <yyyy-MM-dd HH-mm-ss> %n", KgParserApplication.class.getSimpleName());
			return;
		}

		PlayerSummaryDownloader.Config config = PlayerSummaryDownloader.Config.parseFromArguments(args);
		config.setStartDate(args[3]);
		config.log();

		PlayerJsonParser.handlePlayers(config, this::handlePlayer);
	}

	private void handlePlayer(int playerId, Optional<PlayerJsonData> jsonDataOptional) {
		if (jsonDataOptional.isEmpty()) {
			String errorMessage = String.format("Something really serious happened for player %d. JsonData is not present.", playerId);
			logger.error(errorMessage);
			throw new IllegalStateException(errorMessage);
		}

		PlayerJsonData jsonData = jsonDataOptional.get();

		PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

		if (player.getPlayerId() == null) { // non-existing user -> no playerId in neither summary nor indexData
			logger.debug(
				"Player {}: setting playerId manually. Most probably this player does not exist.\n/get-summary error: {}\n/get-index/data error: {}",
				playerId,
				player.getGetSummaryError(),
				player.getGetIndexDataError()
			);

			player.setPlayerId(playerId);
		}

		savePlayerToDatabase(player);
	}

	private void savePlayerToDatabase(PlayerEntity player) {
		final Integer playerId = player.getPlayerId();
		logger.debug("Saving player {} to database (NO EXISTING PLAYERS CHECK)...", playerId);

/*
		// todo: we must actually search by importedDate + playerId, if data is not sharded to different tables by importDate
		List<PlayerEntity> existingPlayersWithId = playerRepository.findByPlayerId(playerId);

		List<Long> playerDbIds = existingPlayersWithId
			.stream()
			.map(PlayerEntity::getDbId)
			.sorted()
			.collect(Collectors.toList());

		if (existingPlayersWithId.isEmpty()) {
			logger.debug("No players with playerId = {} found in the database. Nothing to delete.", playerId);
		}
		else {
			logger.debug("Found {} players with playerId = {}. Deleting them...", existingPlayersWithId.size(), playerId);
			playerRepository.deleteAll(existingPlayersWithId);
			logger.debug("Deleted {} players with db ids: {}.", existingPlayersWithId.size(), playerDbIds);
		}
*/

		playerRepository.save(player);
		logger.debug("Successfully saved player {} (login = \"{}\") to database. Player dbId: {}.", player.getPlayerId(), player.getLogin(), player.getDbId());
	}
}
