package ru.klavogonki.statistics.import_db;

import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.klavogonki.openapi.model.GetStatsOverviewGameType;
import ru.klavogonki.openapi.model.GetStatsOverviewResponse;
import ru.klavogonki.statistics.Config;
import ru.klavogonki.statistics.download.PlayerJsonData;
import ru.klavogonki.statistics.download.PlayerJsonParser;
import ru.klavogonki.statistics.entity.PlayerEntity;
import ru.klavogonki.statistics.entity.PlayerVocabularyStatsEntity;
import ru.klavogonki.statistics.mapper.PlayerMapper;
import ru.klavogonki.statistics.mapper.PlayerVocabularyStatsMapper;
import ru.klavogonki.statistics.repository.PlayerRepository;
import ru.klavogonki.statistics.repository.PlayerVocabularyStatsRepository;
import ru.klavogonki.statistics.springboot.Profiles;
import ru.klavogonki.statistics.util.DateUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Profile(Profiles.DATABASE)
@Log4j2
public class DatabaseImporter {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerVocabularyStatsRepository playerVocabularyStatsRepository;

    // todo: autowire it, @see https://mapstruct.org/documentation/stable/reference/html/#using-dependency-injection
    private final PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

    // todo: autowire it, @see https://mapstruct.org/documentation/stable/reference/html/#using-dependency-injection
    private final PlayerVocabularyStatsMapper statsMapper = Mappers.getMapper(PlayerVocabularyStatsMapper.class);

    private List<PlayerEntity> playersBatch = new ArrayList<>(); // todo: find a nicer solution

    public void importJsonToDatabase(final Config config) {
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
			.toList();

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
