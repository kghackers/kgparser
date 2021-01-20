package ru.klavogonki.statistics.export;

import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.klavogonki.statistics.excel.TopByAchievementsCountExcelTemplate;
import ru.klavogonki.statistics.excel.TopByBestSpeedExcelTemplate;
import ru.klavogonki.statistics.excel.TopByCarsCountExcelTemplate;
import ru.klavogonki.statistics.excel.TopByFriendsCountExcelTemplate;
import ru.klavogonki.statistics.excel.TopByRatingLevelExcelTemplate;
import ru.klavogonki.statistics.excel.TopByTotalRacesCountExcelTemplate;
import ru.klavogonki.statistics.excel.TopByVocabulariesCountExcelTemplate;
import ru.klavogonki.statistics.freemarker.PageUrls;
import ru.klavogonki.statistics.freemarker.TopByAchievementsCountTemplate;
import ru.klavogonki.statistics.freemarker.TopByBestSpeedTemplate;
import ru.klavogonki.statistics.freemarker.TopByCarsCountTemplate;
import ru.klavogonki.statistics.freemarker.TopByFriendsCountTemplate;
import ru.klavogonki.statistics.freemarker.TopByRatingLevelTemplate;
import ru.klavogonki.statistics.freemarker.TopByTotalRacesCountTemplate;
import ru.klavogonki.statistics.freemarker.TopByVocabulariesCountTemplate;
import ru.klavogonki.statistics.dto.PlayerDto;
import ru.klavogonki.statistics.entity.PlayerEntity;
import ru.klavogonki.statistics.mapper.PlayerDtoMapper;
import ru.klavogonki.statistics.repository.PlayerRepository;

import java.util.List;

@Log4j2
@Component
public class Top500PagesExporter implements DataExporter {

    @Autowired
    private PlayerRepository playerRepository;

    // todo: autowire it, @see https://mapstruct.org/documentation/stable/reference/html/#using-dependency-injection
    private final PlayerDtoMapper mapper = Mappers.getMapper(PlayerDtoMapper.class);

    @Override
    public void export(ExportContext context) {
        // todo: hardcoded top1 and top500 are ok for now,
        // todo: or implement with setMaxResults or something like this

        // todo: add additional users with same criteria count as last of top500
        // todo: within 500, order by 2nd criteria, eg totalRacesCount for all, bestSpeed in totalRacesCountTop500
        exportTop500ByTotalRacesCount(context);
        exportTop500ByBestSpeed(context);
        exportTop500ByRatingLevel(context);
        exportTop500ByFriendsCount(context);
        exportTop500ByAchievementsCount(context);
        exportTop500ByVocabulariesCount(context);
        exportTop500ByCarsCount(context);
    }

    private void exportTop500ByTotalRacesCount(final ExportContext context) {
        List<PlayerEntity> top500PlayersByTotalRacesCount = playerRepository.findTop500ByOrderByTotalRacesCountDesc();
        List<PlayerDto> top500PlayersByTotalRacesCountDto = mapper.playerEntitiesToPlayerDtos(top500PlayersByTotalRacesCount, PlayerDto::getTotalRacesCount);
        logger.debug("Top 500 players by total races count: {}", top500PlayersByTotalRacesCountDto);

        new TopByTotalRacesCountTemplate()
            .players(top500PlayersByTotalRacesCountDto)
            .export(PageUrls.getTopByTotalRacesCountFilePath(context.webRootDir));

        new TopByTotalRacesCountExcelTemplate()
            .players(top500PlayersByTotalRacesCountDto)
            .export(
                PageUrls.getTopByTotalRacesCountExcelFilePath(context.webRootDir),
                PageUrls.getTopByTotalRacesCountExcelZipFilePath(context.webRootDir)
            );
    }

    private void exportTop500ByBestSpeed(final ExportContext context) {
        List<PlayerEntity> top500PlayersByBestSpeed = playerRepository.findTop500ByOrderByBestSpeedDesc();
        List<PlayerDto> top500PlayersByBestSpeedDto = mapper.playerEntitiesToPlayerDtos(top500PlayersByBestSpeed, PlayerDto::getBestSpeed);
        logger.debug("Top 500 players by best speed: {}", top500PlayersByBestSpeedDto);

        new TopByBestSpeedTemplate()
            .players(top500PlayersByBestSpeedDto)
            .export(PageUrls.getTopByBestSpeedFilePath(context.webRootDir));

        new TopByBestSpeedExcelTemplate()
            .players(top500PlayersByBestSpeedDto)
            .export(
                PageUrls.getTopByBestSpeedExcelFilePath(context.webRootDir),
                PageUrls.getTopByBestSpeedExcelZipFilePath(context.webRootDir)
            );
    }

    private void exportTop500ByRatingLevel(final ExportContext context) {
        List<PlayerEntity> top500PlayersByRatingLevel = playerRepository.findTop500ByOrderByRatingLevelDesc();
        List<PlayerDto> top500PlayersByRatingLevelDto = mapper.playerEntitiesToPlayerDtos(top500PlayersByRatingLevel, PlayerDto::getRatingLevel);
        logger.debug("Top 500 players by rating level: {}", top500PlayersByRatingLevelDto);

        new TopByRatingLevelTemplate()
            .players(top500PlayersByRatingLevelDto)
            .export(PageUrls.getTopByRatingLevelFilePath(context.webRootDir));

        new TopByRatingLevelExcelTemplate()
            .players(top500PlayersByRatingLevelDto)
            .export(
                PageUrls.getTopByRatingLevelExcelFilePath(context.webRootDir),
                PageUrls.getTopByRatingLevelExcelZipFilePath(context.webRootDir)
            );
    }

    private void exportTop500ByFriendsCount(final ExportContext context) {
        List<PlayerEntity> top500PlayersByFriendsCount = playerRepository.findTop500ByOrderByFriendsCountDesc();
        List<PlayerDto> top500PlayersByFriendsCountDto = mapper.playerEntitiesToPlayerDtos(top500PlayersByFriendsCount, PlayerDto::getFriendsCount);
        logger.debug("Top 500 players by friends count: {}", top500PlayersByFriendsCountDto);

        new TopByFriendsCountTemplate()
            .players(top500PlayersByFriendsCountDto)
            .export(PageUrls.getTopByFriendsCountFilePath(context.webRootDir));

        new TopByFriendsCountExcelTemplate()
            .players(top500PlayersByFriendsCountDto)
            .export(
                PageUrls.getTopByFriendsCountExcelFilePath(context.webRootDir),
                PageUrls.getTopByFriendsCountExcelZipFilePath(context.webRootDir)
            );
    }

    private void exportTop500ByAchievementsCount(final ExportContext context) {
        List<PlayerEntity> top500PlayersByAchievementsCount = playerRepository.findTop500ByOrderByAchievementsCountDesc();
        List<PlayerDto> top500PlayersByAchievementsCountDto = mapper.playerEntitiesToPlayerDtos(top500PlayersByAchievementsCount, PlayerDto::getAchievementsCount);
        logger.debug("Top 500 players by achievements count: {}", top500PlayersByAchievementsCountDto);

        new TopByAchievementsCountTemplate()
            .players(top500PlayersByAchievementsCountDto)
            .export(PageUrls.getTopByAchievementsCountFilePath(context.webRootDir));

        new TopByAchievementsCountExcelTemplate()
            .players(top500PlayersByAchievementsCountDto)
            .export(
                PageUrls.getTopByAchievementsCountExcelFilePath(context.webRootDir),
                PageUrls.getTopByAchievementsCountExcelZipFilePath(context.webRootDir)
            );
    }

    private void exportTop500ByVocabulariesCount(final ExportContext context) {
        List<PlayerEntity> top500PlayersByVocabulariesCount = playerRepository.findTop500ByOrderByVocabulariesCountDesc();
        List<PlayerDto> top500PlayersByVocabulariesCountDto = mapper.playerEntitiesToPlayerDtos(top500PlayersByVocabulariesCount, PlayerDto::getVocabulariesCount);
        logger.debug("Top 500 players by vocabularies count: {}", top500PlayersByVocabulariesCountDto);

        new TopByVocabulariesCountTemplate()
            .players(top500PlayersByVocabulariesCountDto)
            .export(PageUrls.getTopByVocabulariesCountFilePath(context.webRootDir));

        new TopByVocabulariesCountExcelTemplate()
            .players(top500PlayersByVocabulariesCountDto)
            .export(
                PageUrls.getTopByVocabulariesCountExcelFilePath(context.webRootDir),
                PageUrls.getTopByVocabulariesCountExcelZipFilePath(context.webRootDir)
            );
    }

    private void exportTop500ByCarsCount(final ExportContext context) {
        List<PlayerEntity> top500PlayersByCarsCount = playerRepository.findTop500ByOrderByCarsCountDesc();
        List<PlayerDto> top500PlayersByCarsCountDto = mapper.playerEntitiesToPlayerDtos(top500PlayersByCarsCount, PlayerDto::getCarsCount);
        logger.debug("Top 500 players by cars count: {}", top500PlayersByCarsCountDto);

        new TopByCarsCountTemplate()
            .players(top500PlayersByCarsCountDto)
            .export(PageUrls.getTopByCarsCountFilePath(context.webRootDir));

        new TopByCarsCountExcelTemplate()
            .players(top500PlayersByCarsCountDto)
            .export(
                PageUrls.getTopByCarsCountExcelFilePath(context.webRootDir),
                PageUrls.getTopByCarsCountExcelZipFilePath(context.webRootDir)
            );
    }
}
