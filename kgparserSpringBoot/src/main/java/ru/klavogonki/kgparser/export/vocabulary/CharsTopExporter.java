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
    public String topByBestSpeedExcelSheetName() {
//        return "Топ по лучшей скорости в «Буквах»"; // 33 characters, too long
        return "Топ по скорости в «Буквах»";
    }

    @Override
    public String topByRacesCountPageTitle() {
        return "Топ по пробегу в «Буквах»";
    }
    @Override
    public String topByRacesCountHeader() {
        return "Топ по пробегу в «Буквах»";
    }
    @Override
    public String topByRacesCountAdditionalHeader() {
        return String.format("Учтены игроки с минимальным пробегом %d в «Буквах»", RACES_COUNT_MIN);
    }
    @Override
    public String topByRacesCountExcelSheetName() {
        return "Топ по пробегу в «Буквах»";
    }

    @Override
    public String topByHaulPageTitle() {
        return "Топ по времени в «Буквах»";
    }
    @Override
    public String topByHaulHeader() {
        return "Топ по времени в «Буквах»";
    }
    @Override
    public String topByHaulAdditionalHeader() {
        return String.format("Учтены игроки с минимальным пробегом %d в «Буквах»", RACES_COUNT_MIN);
    }
    @Override
    public String topByHaulExcelSheetName() {
        return "Топ по времени в «Буквах»";
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

    @Override
    public List<PlayerVocabularyStatsEntity> getPlayersByHaul() {
        List<PlayerVocabularyStatsEntity> players = repository.findByVocabularyCodeEqualsAndRacesCountGreaterThanOrderByHaulDesc(getVocabularyCode(), RACES_COUNT_MIN);
        logger.debug("Total players by haul, min total races = {}: {}", RACES_COUNT_MIN, players.size());

        return players;
    }
}
