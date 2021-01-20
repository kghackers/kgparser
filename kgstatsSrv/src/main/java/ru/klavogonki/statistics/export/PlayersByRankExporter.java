package ru.klavogonki.statistics.export;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.klavogonki.statistics.freemarker.PageUrls;
import ru.klavogonki.statistics.freemarker.PlayersByRankDataTemplate;
import ru.klavogonki.statistics.freemarker.PlayersByRankTemplate;
import ru.klavogonki.statistics.dto.PlayerRankLevelAndTotalRacesCount;
import ru.klavogonki.statistics.repository.PlayerRepository;
import ru.klavogonki.statistics.util.JacksonUtils;

import java.util.Comparator;
import java.util.List;

@Log4j2
@Component
public class PlayersByRankExporter implements DataExporter {

    private static final int MIN_TOTAL_RACES_COUNT = 1;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public void export(final ExportContext context) {
        List<PlayerRankLevelAndTotalRacesCount> playersData = playerRepository.getActualPlayersRankLevelAndTotalRacesCount(MIN_TOTAL_RACES_COUNT);
        logger.debug("Total players with totalRacesCount >= {}: {}", MIN_TOTAL_RACES_COUNT, playersData.size());
        logger.debug("Players: \n{}", playersData);

        // convert to a minimalistic 2d-array to have minimum required data on JS side
        String playersDataArrayString = convertToJs2DArrayString(playersData);
        logger.debug("Players as 2D-Array (rank and totalRacesCount): \n{}", playersDataArrayString);

        int totalPlayersCount = playersData.size();

        Integer maxTotalRacesCount = playersData
            .stream()
            .map(PlayerRankLevelAndTotalRacesCount::getTotalRacesCount)
            .max(Comparator.naturalOrder())
            .orElseThrow(() -> new IllegalStateException(String.format("Cannot get maxTotalRacesCount from %d players.", totalPlayersCount)));

        logger.debug("maxTotalRacesCount for all selected players: {}", maxTotalRacesCount);

        // export data js
        new PlayersByRankDataTemplate()
            .rankToTotalRacesCount(playersDataArrayString)
            .export(PageUrls.getPlayerByRankDataFilePath(context.webRootDir));

        // export html page
        new PlayersByRankTemplate()
            .totalPlayersCount(totalPlayersCount)
            .minTotalRacesCount(MIN_TOTAL_RACES_COUNT)
            .maxTotalRacesCount(maxTotalRacesCount)
            .export(PageUrls.getPlayerByRankFilePath(context.webRootDir));
    }

    public String convertToJs2DArrayString(final List<PlayerRankLevelAndTotalRacesCount> playersData) {
        // todo: is it possible via MapStruct?
        int [][] playersDataAsArray = new int[playersData.size()][2];

        for (int i = 0; i < playersData.size(); i++) {
            final PlayerRankLevelAndTotalRacesCount player = playersData.get(i);

            playersDataAsArray[i] = PlayerRankLevelAndTotalRacesCount.toArray(player);
        }

        return JacksonUtils.serializeToString(playersDataAsArray);
    }
}
