package ru.klavogonki.statistics.export.vocabulary.standard;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.klavogonki.statistics.entity.PlayerEntity;
import ru.klavogonki.statistics.entity.PlayerVocabularyStatsEntity;
import ru.klavogonki.statistics.export.ExportContext;
import ru.klavogonki.statistics.export.LoggerWrapper;
import ru.klavogonki.statistics.repository.PlayerVocabularyStatsRepository;
import ru.klavogonki.statistics.springboot.Profiles;

import java.util.List;

@Component
@Profile(Profiles.DATABASE)
@Log4j2
public abstract class StandardVocabularyTopExporterDefaultImpl implements StandardVocabularyTopExporter {

    private LoggerWrapper loggerWrapper;

    @Autowired
    protected PlayerVocabularyStatsRepository repository;

    @Override
    public LoggerWrapper logger() {
        return getLoggerWrapper();
    }

    private LoggerWrapper getLoggerWrapper() {
        if (loggerWrapper == null) {
            loggerWrapper = new LoggerWrapper(logger, loggerName());
        }

        return loggerWrapper;
    }

    @Override
    public List<PlayerVocabularyStatsEntity> getPlayersByBestSpeed(ExportContext context) {
        PlayerVocabularyStatsRepository repo = getRepositorySafe(context);

        List<PlayerVocabularyStatsEntity> players = repo.findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByBestSpeedDesc(vocabularyCode(), minRacesCount(), PlayerEntity.NOT_BLOCKED);
        logger().debug("Total players by best speed, min total races = {}: {}", minRacesCount(), players.size());

        return players;
    }

    @Override
    public List<PlayerVocabularyStatsEntity> getPlayersByRacesCount(ExportContext context) {
        PlayerVocabularyStatsRepository repo = getRepositorySafe(context);

        List<PlayerVocabularyStatsEntity> players = repo.findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByRacesCountDesc(vocabularyCode(), minRacesCount(), PlayerEntity.NOT_BLOCKED);
        logger().debug("Total players by races count, min total races = {}: {}", minRacesCount(), players.size());

        return players;
    }

    @Override
    public List<PlayerVocabularyStatsEntity> getPlayersByHaul(ExportContext context) {
        PlayerVocabularyStatsRepository repo = getRepositorySafe(context);

        List<PlayerVocabularyStatsEntity> players = repo.findByVocabularyCodeEqualsAndRacesCountGreaterThanEqualAndPlayerBlockedEqualsOrderByHaulDesc(vocabularyCode(), minRacesCount(), PlayerEntity.NOT_BLOCKED);
        logger().debug("Total players by haul, min total races = {}: {}", minRacesCount(), players.size());

        return players;
    }

    private PlayerVocabularyStatsRepository getRepositorySafe(ExportContext context) {
        return (this.repository != null)
            ? this.repository // this.repository is not inherited by the dynamicaally created subclasses
            : context.repository;
    }
}
