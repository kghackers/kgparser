package ru.klavogonki.kgparser.export;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.jsonParser.JacksonUtils;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerRankLevelAndTotalRacesCount;
import ru.klavogonki.kgparser.jsonParser.repository.PlayerRepository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        // todo: is it possible via mapStruct?

        String playersDataArrayString = convertToJs2DArrayString(playersData);
        logger.debug("Players as 2D-Array (rank and totalRacesCount): \n{}", playersDataArrayString);

        try {
            FileUtils.writeStringToFile(new File("c:/java/players-rank-to-races-data.json"), playersDataArrayString, StandardCharsets.UTF_8); // todo: remove this
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // todo: ftl export
    }

    public String convertToJs2DArrayString(final List<PlayerRankLevelAndTotalRacesCount> playersData) {
        int [][] playersDataAsArray = new int[playersData.size()][2];

        for (int i = 0; i < playersData.size(); i++) {
            final PlayerRankLevelAndTotalRacesCount player = playersData.get(i);

            playersDataAsArray[i] = PlayerRankLevelAndTotalRacesCount.toArray(player);
        }

        return JacksonUtils.serializeToString(playersDataAsArray);
    }
}
