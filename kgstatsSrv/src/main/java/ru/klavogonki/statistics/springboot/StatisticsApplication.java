package ru.klavogonki.statistics.springboot;

import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.klavogonki.openapi.model.GetStatsOverviewGameType;
import ru.klavogonki.openapi.model.GetStatsOverviewResponse;
import ru.klavogonki.statistics.Config;
import ru.klavogonki.statistics.download.PlayerDataDownloader;
import ru.klavogonki.statistics.download.PlayerJsonData;
import ru.klavogonki.statistics.download.PlayerJsonParser;
import ru.klavogonki.statistics.entity.PlayerEntity;
import ru.klavogonki.statistics.entity.PlayerVocabularyStatsEntity;
import ru.klavogonki.statistics.export.StatisticsGenerator;
import ru.klavogonki.statistics.mapper.PlayerMapper;
import ru.klavogonki.statistics.mapper.PlayerVocabularyStatsMapper;
import ru.klavogonki.statistics.repository.PlayerRepository;
import ru.klavogonki.statistics.repository.PlayerVocabularyStatsRepository;
import ru.klavogonki.statistics.util.DateUtils;
import ru.klavogonki.statistics.util.JacksonUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
@SpringBootApplication(exclude = {
	DataSourceAutoConfiguration.class,
	DataSourceTransactionManagerAutoConfiguration.class,
	HibernateJpaAutoConfiguration.class
})
*/
@SpringBootApplication
@EntityScan(basePackages= {"ru.klavogonki.statistics.entity"})
@ComponentScan({"ru.klavogonki.statistics"})
@EnableJpaRepositories("ru.klavogonki.statistics")
@Log4j2
public class StatisticsApplication implements CommandLineRunner {

	public enum Mode {
		DOWNLOAD_PLAYER_DATA(3),
		IMPORT_JSON_TO_DATABASE(2),
		GENERATE_STATISTICS_FROM_DATABASE(2),
		;

		Mode(final int requiredArgumentsCount) {
			this.requiredArgumentsCount = requiredArgumentsCount;
		}

		public final int requiredArgumentsCount; // including mode
	}

	@Autowired(required = false)
	private PlayerRepository playerRepository;

	@Autowired
	private PlayerVocabularyStatsRepository playerVocabularyStatsRepository;

	@Autowired(required = false)
	private StatisticsGenerator statisticsGenerator;

	// todo: autowire it, @see https://mapstruct.org/documentation/stable/reference/html/#using-dependency-injection
	private final PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

	// todo: autowire it, @see https://mapstruct.org/documentation/stable/reference/html/#using-dependency-injection
	private final PlayerVocabularyStatsMapper statsMapper = Mappers.getMapper(PlayerVocabularyStatsMapper.class);

	public static void main(String[] args) {
		SpringApplication.run(StatisticsApplication.class, args);
	}

	private List<PlayerEntity> playersBatch = new ArrayList<>(); // todo: find a nicer solution

	@Override
	public void run(final String... args) {
		if (args.length == 0) {
			System.out.printf("Usage must be one of the following: %n");
			System.out.printf("%s %s <inputConfigFilePath> <outputConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.DOWNLOAD_PLAYER_DATA.name());
			System.out.printf("%s %s <inputConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.IMPORT_JSON_TO_DATABASE.name());
			System.out.printf("%s %s <inputConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.GENERATE_STATISTICS_FROM_DATABASE.name());

			return;
		}

		String modeString = args[0];
		Mode mode = Mode.valueOf(modeString);
		// todo: nice handling of incorrect mode value

		switch (mode) {
			case DOWNLOAD_PLAYER_DATA:
				handleDownloadPlayerData(args);
				break;

			case IMPORT_JSON_TO_DATABASE:
				handleImportJsonToDatabase(args);
				break;

			case GENERATE_STATISTICS_FROM_DATABASE:
				handleGenerateStatistics(args);
				break;

			default:
				throw new IllegalArgumentException(String.format("Unknown mode value: %s", mode));
		}
	}

	private void handleDownloadPlayerData(final String[] args) {
		if (args.length != Mode.DOWNLOAD_PLAYER_DATA.requiredArgumentsCount) {
			System.out.printf("Usage: %s %s <inputConfigFilePath> <outputConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.DOWNLOAD_PLAYER_DATA.name());
			return;
		}

		String inputConfigFilePath = args[1];
		String outputConfigFilePath = args[2];
		String[] playerDataDownloaderArgs = {inputConfigFilePath, outputConfigFilePath};

		PlayerDataDownloader.main(playerDataDownloaderArgs);
	}

	private void handleImportJsonToDatabase(final String[] args) {
		if (args.length != Mode.IMPORT_JSON_TO_DATABASE.requiredArgumentsCount) {
			System.out.printf("Usage: %s %s <inputConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.IMPORT_JSON_TO_DATABASE.name());
			return;
		}

		Config config = JacksonUtils.parseConfig(args[1]);
		importJsonToDatabase(config);
	}

	private void handleGenerateStatistics(final String[] args) {
		if (args.length != Mode.GENERATE_STATISTICS_FROM_DATABASE.requiredArgumentsCount) {
			System.out.printf("Usage: %s %s <inputConfigFilePath> %n", StatisticsApplication.class.getSimpleName(), Mode.GENERATE_STATISTICS_FROM_DATABASE.name());
			return;
		}

		Config config = JacksonUtils.parseConfig(args[1]);
		statisticsGenerator.generateStatistics(config);
	}

	private void importJsonToDatabase(final Config config) {
		// todo: move all related logic to a separate Spring component

		// export from json files with API call results to the database
		PlayerJsonParser.handlePlayers(config, this::handlePlayer);

		if (!playersBatch.isEmpty()) { // save remainder from batch size
			handlePlayersBatch(true);
		}
	}

	private void handlePlayer(int playerId, Optional<PlayerJsonData> jsonDataOptional) {
		if (jsonDataOptional.isEmpty()) {
			String errorMessage = String.format("Something really serious happened for player %d. JsonData is not present.", playerId);
			logger.error(errorMessage);
			throw new IllegalStateException(errorMessage);
		}

/*
		if (true) { // just test the parsing + validation, no DB save. Todo: make it configurable, like "dry-run"
			return;
		}
*/

		PlayerJsonData jsonData = jsonDataOptional.get();

		PlayerEntity player = mapper.playerJsonDataToPlayerEntity(jsonData);

		// todo: this can be done in @AfterMapping in the mapper, passing playerId as @Context
		if (player.getPlayerId() == null) { // non-existing user -> no playerId in neither summary nor indexData
			logger.debug(
				"Player {}: setting playerId manually. Most probably this player does not exist.\n/get-summary error: {}\n/get-index/data error: {}",
				playerId,
				player.getGetSummaryError(),
				player.getGetIndexDataError()
			);

			player.setPlayerId(playerId);
		}

//		savePlayerToDatabase(player);

		GetStatsOverviewResponse statsOverview = jsonData.statsOverview;
		Map<String, GetStatsOverviewGameType> gameTypes = statsOverview.getGametypes();

		List<PlayerVocabularyStatsEntity> allPlayerStats = new ArrayList<>();
		for (Map.Entry<String, GetStatsOverviewGameType> entry : gameTypes.entrySet()) {
			String vocabularyCode = entry.getKey();
			GetStatsOverviewGameType gameType = entry.getValue();

			LocalDateTime importDateUtc = DateUtils.convertToUtcLocalDateTime(jsonData.importDate);

			PlayerVocabularyStatsEntity stats = statsMapper.statsGameTypeToEntity(gameType, importDateUtc, statsOverview, vocabularyCode, player);
			allPlayerStats.add(stats);
		}


		player.setStats(allPlayerStats);

		playersBatch.add(player);
		handlePlayersBatch(false);
//		savePlayerToDatabase(player); // cascade save player with its stats

//		saveStatsToDatabase(player, allPlayerStats);
	}

	private void handlePlayersBatch(boolean force) {
		int size = playersBatch.size();
		if ((size == 1000) || force) { // force for last execution
			logger.info("!!! Batch size is {}. Saving {} players to the database...", size, size);

			playerRepository.saveAll(playersBatch);

			// todo: time measure for saving 1000 players?
			logger.info("!!! {} players saved to the database.", size);

			playersBatch.clear();
			logger.info("Batch cleared from {} to {} players", size, playersBatch.size());

		}
		else {
			logger.info("Batch size is {}. Do not save it yet.", size);
		}
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

	private void saveStatsToDatabase(PlayerEntity player, List<PlayerVocabularyStatsEntity> statsList) {
		logger.debug("Saving player {} stats (total {} vocabularies) to database (NO EXISTING STATS CHECK)...", player.getPlayerId(), statsList.size());

		/* todo: any db checks if required */

		playerVocabularyStatsRepository.saveAll(statsList);

		logger.debug("Successfully player {} stats (total {} vocabularies) to database.", player.getPlayerId(), statsList.size());
	}
}
