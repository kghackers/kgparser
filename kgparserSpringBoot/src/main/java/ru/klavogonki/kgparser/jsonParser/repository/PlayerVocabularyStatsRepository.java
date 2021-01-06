package ru.klavogonki.kgparser.jsonParser.repository;

import org.springframework.data.repository.CrudRepository;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerVocabularyStatsEntity;

import java.util.List;

public interface PlayerVocabularyStatsRepository extends CrudRepository<PlayerVocabularyStatsEntity, Long> {

    List<PlayerVocabularyStatsEntity> findByVocabularyCodeEqualsAndRacesCountGreaterThanOrderByBestSpeedDesc(String vocabularyCode, int racesCount);

    List<PlayerVocabularyStatsEntity> findByVocabularyCodeEqualsAndRacesCountGreaterThanOrderByRacesCountDesc(String vocabularyCode, int racesCount);
}
