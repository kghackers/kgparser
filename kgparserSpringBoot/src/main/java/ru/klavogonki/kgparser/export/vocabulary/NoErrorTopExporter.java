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
public class NoErrorTopExporter implements VocabularyTopExporter {
    private static final int RACES_COUNT_MIN = 100; // todo: maybe increase, 3027 players >= 100, 1727 players >= 200

    @Autowired
    private PlayerVocabularyStatsRepository repository;

    @Override
    public String getVocabularyCode() {
        return StandardDictionary.noerror.name();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public String topByBestSpeedPageTitle() {
        return "Топ по лучшей скорости в «Безошибочном»";
    }
    @Override
    public String topByBestSpeedHeader() {
        return "Топ по лучшей скорости в «Безошибочном»";
    }
    @Override
    public String topByBestSpeedAdditionalHeader() {
        return String.format("Учтены игроки с минимальным пробегом %d в «Безошибочном»", RACES_COUNT_MIN);
    }
    @Override
    public String topByBestSpeedExcelSheetName() {
//        return "Топ по лучшей скорости в «Безошибочном»"; // 34 characters, too long
        return "Топ по рекорду в «БО»";
    }

    @Override
    public String topByRacesCountPageTitle() {
        return "Топ по пробегу в «Безошибочном»";
    }
    @Override
    public String topByRacesCountHeader() {
        return "Топ по пробегу в «Безошибочном»";
    }
    @Override
    public String topByRacesCountAdditionalHeader() {
        return String.format("Учтены игроки с минимальным пробегом %d в «Безошибочном»", RACES_COUNT_MIN);
    }
    @Override
    public String topByRacesCountExcelSheetName() {
        return "Топ по пробегу в «Безошибочном»"; // 31 chars!
    }

    @Override
    public String topByHaulPageTitle() {
        return "Топ по времени в «Безошибочном»";
    }
    @Override
    public String topByHaulHeader() {
        return "Топ по времени в «Безошибочном»";
    }
    @Override
    public String topByHaulAdditionalHeader() {
        return String.format("Учтены игроки с минимальным пробегом %d в «Безошибочном»", RACES_COUNT_MIN);
    }
    @Override
    public String topByHaulExcelSheetName() {
        return "Топ по времени в «Безошибочном»"; // 31 chars!
    }

    @Override
    public List<PlayerVocabularyStatsEntity> getPlayersByBestSpeed() {
        List<PlayerVocabularyStatsEntity> players = repository.findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualOrderByBestSpeedDesc(getVocabularyCode(), RACES_COUNT_MIN);
        logger.debug("Total players by best speed, min total races = {}: {}", RACES_COUNT_MIN, players.size());

        return players;
    }

    @Override
    public List<PlayerVocabularyStatsEntity> getPlayersByRacesCount() {
        List<PlayerVocabularyStatsEntity> players = repository.findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualOrderByRacesCountDesc(getVocabularyCode(), RACES_COUNT_MIN);
        logger.debug("Total players by races count, min total races = {}: {}", RACES_COUNT_MIN, players.size());

        return players;
    }

    @Override
    public List<PlayerVocabularyStatsEntity> getPlayersByHaul() {
        List<PlayerVocabularyStatsEntity> players = repository.findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualOrderByHaulDesc(getVocabularyCode(), RACES_COUNT_MIN);
        logger.debug("Total players by haul, min total races = {}: {}", RACES_COUNT_MIN, players.size());

        return players;
    }
}
