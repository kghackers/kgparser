package ru.klavogonki.kgparser.jsonParser.repository;

import org.springframework.data.repository.CrudRepository;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerVocabularyStatsEntity;

public interface PlayerVocabularyStatsRepository extends CrudRepository<PlayerVocabularyStatsEntity, Long> {
}
