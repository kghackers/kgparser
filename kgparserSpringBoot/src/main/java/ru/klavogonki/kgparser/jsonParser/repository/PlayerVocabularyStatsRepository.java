package ru.klavogonki.kgparser.jsonParser.repository;

import org.springframework.data.repository.CrudRepository;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerVocabularyStatsEntity;

import java.util.List;

public interface PlayerVocabularyStatsRepository extends CrudRepository<PlayerVocabularyStatsEntity, Long> {

    List<PlayerVocabularyStatsEntity> findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByBestSpeedDesc(String vocabularyCode, int racesCount, int blocked);

    List<PlayerVocabularyStatsEntity> findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByRacesCountDesc(String vocabularyCode, int racesCount, int blocked);

    List<PlayerVocabularyStatsEntity> findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByHaulDesc(String vocabularyCode, int racesCount, int blocked);
}
