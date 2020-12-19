package ru.klavogonki.kgparser.export;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;
import ru.klavogonki.kgparser.jsonParser.repository.PlayerRepository;

import java.util.List;

@Log4j2
@Component
public class Top500PagesExporter implements DataExporter {

    @Autowired
    private PlayerRepository playerRepository;

    public void export(ExportContext context) {
        // todo: hardcoded top1 and top500 are ok for now,
        // todo: or implement with setMaxResults or something like this

        // todo: add additional users with same criteria count as last of top500
        // todo: within 500, order by 2nd criteria, eg totalRacesCount for all, bestSpeed in totalRacesCountTop500
        List<PlayerEntity> top500PlayersByTotalRacesCount = playerRepository.findTop500ByOrderByTotalRacesCountDesc();
        logger.debug("Top 500 players by total races count: {}", top500PlayersByTotalRacesCount);

        List<PlayerEntity> top500PlayersByBestSpeed = playerRepository.findTop500ByOrderByBestSpeedDesc();
        logger.debug("Top 500 players by best speed: {}", top500PlayersByBestSpeed);

        List<PlayerEntity> top500PlayersByRatingLevel = playerRepository.findTop500ByOrderByRatingLevelDesc();
        logger.debug("Top 500 players by rating level: {}", top500PlayersByRatingLevel);

        List<PlayerEntity> top500PlayersByAchievementsCount = playerRepository.findTop500ByOrderByAchievementsCountDesc();
        logger.debug("Top 500 players by achievements count: {}", top500PlayersByAchievementsCount);

        List<PlayerEntity> top500PlayersByFriendsCount = playerRepository.findTop500ByOrderByFriendsCountDesc();
        logger.debug("Top 500 players by friends count: {}", top500PlayersByFriendsCount);

        List<PlayerEntity> top500PlayersByVocabulariesCount = playerRepository.findTop500ByOrderByVocabulariesCountDesc();
        logger.debug("Top 500 players by vocabularies count: {}", top500PlayersByVocabulariesCount);

        List<PlayerEntity> top500PlayersByCarsCount = playerRepository.findTop500ByOrderByCarsCountDesc();
        logger.debug("Top 500 players by cars count: {}", top500PlayersByCarsCount);
    }
}
