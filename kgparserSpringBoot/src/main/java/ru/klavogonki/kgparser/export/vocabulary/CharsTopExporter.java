package ru.klavogonki.kgparser.export.vocabulary;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.StandardDictionary;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerVocabularyStatsEntity;
import ru.klavogonki.kgparser.jsonParser.repository.PlayerVocabularyStatsRepository;

import java.util.List;

@Log4j2
@Component
public class CharsTopExporter implements VocabularyTopExporter {
    private static final int RACES_COUNT_MIN = 100;

    @Autowired
    private PlayerVocabularyStatsRepository repository;

    @Override
    public String getVocabularyCode() {
        return StandardDictionary.chars.name();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public String topByBestSpeedPageTitle() {
        return "Топ по лучшей скорости в «Буквах»";
    }

    @Override
    public String topByBestSpeedHeader() {
        return "Топ по лучшей скорости в «Буквах»";
    }

    @Override
    public String topByBestSpeedAdditionalHeader() {
        return String.format("Учтены игроки с минимальным пробегом %d в «Буквах»", RACES_COUNT_MIN);
    }

    @Override
    public List<PlayerVocabularyStatsEntity> getPlayersByBestSpeed() {
        List<PlayerVocabularyStatsEntity> players = repository.findByVocabularyCodeEqualsAndRacesCountGreaterThanOrderByBestSpeedDesc(getVocabularyCode(), RACES_COUNT_MIN);
        logger.debug("Total players by best speed, min total races = {}: {}", RACES_COUNT_MIN, players.size());

        return players;
    }

    @Override
    public List<PlayerVocabularyStatsEntity> getPlayersByRacesCount() {
        List<PlayerVocabularyStatsEntity> players = repository.findByVocabularyCodeEqualsAndRacesCountGreaterThanOrderByRacesCountDesc(getVocabularyCode(), RACES_COUNT_MIN);
        logger.debug("Total players by races count, min total races = {}: {}", RACES_COUNT_MIN, players.size());

        return players;
    }
}
