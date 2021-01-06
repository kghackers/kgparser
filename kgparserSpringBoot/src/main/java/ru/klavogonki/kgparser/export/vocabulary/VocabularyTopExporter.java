package ru.klavogonki.kgparser.export.vocabulary;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import ru.klavogonki.kgparser.export.DataExporter;
import ru.klavogonki.kgparser.export.ExportContext;
import ru.klavogonki.kgparser.export.ExporterUtils;
import ru.klavogonki.kgparser.freemarker.VocabularyTopBySpeedTemplate;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerVocabularyStatsEntity;
import ru.klavogonki.kgparser.jsonParser.mapper.PlayerVocabularyDtoMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VocabularyTopExporter extends DataExporter {

    String getVocabularyCode();

    Logger getLogger();

    default int getPageSize() {
        return 100;
    }

    default String topByBestSpeedPageTitle() { // <title> in html
        return "DEFAULT_TOP_BY_BEST_SPEED_PAGE_TITLE";
    }

    default String topByBestSpeedHeader() { // <h2> in html
        return "DEFAULT_TOP_BY_BEST_SPEED_PAGE_HEADER";
    }

    default String topByBestSpeedAdditionalHeader() { // under <h2> in html
        return "DEFAULT_TOP_BY_BEST_SPEED_PAGE_ADDITIONAL_HEADER";
    }

    // todo: pageTitle, header, additional header for topByRacesCount
    // todo: pageTitle, header, additional header for topByHaul

    default List<PlayerVocabularyStatsEntity> getPlayersByBestSpeed() {
        return Collections.emptyList();
    }

    default List<PlayerVocabularyStatsEntity> getPlayersByRacesCount() {
        return Collections.emptyList();
    }

    default List<PlayerVocabularyStatsEntity> getPlayersByHaul() {
        return Collections.emptyList();
    }

    default String topByBestSpeedPageFilePath(final String rootDir, final int pageNumber) {
        return String.format("%s/voc-%s-top-by-best-speed-page-%d.html", rootDir, getVocabularyCode(), pageNumber);
    }
    default String topByBestSpeedLoginToPageFilePath(final String rootDir) {
        return String.format("%s/js/voc-%s-top-by-best-speed-login-to-page.js", rootDir, getVocabularyCode());
    }
    default String topByBestSpeedExcelFilePath(final String rootDir) {
        return String.format("%s/xlsx/voc-%s-top-by-best-speed.xlsx", rootDir, getVocabularyCode());
    }
    default String topByBestSpeedExcelZipFilePath(final String rootDir) {
        return String.format("%s/xlsx/voc-%s-top-by-best-speed.zip", rootDir, getVocabularyCode());
    }

    default String topByRacesCountFilePath(final String rootDir, final int pageNumber) {
        return String.format("%s/voc-%s-top-by-races-count-page-%d.html", rootDir, getVocabularyCode(), pageNumber);
    }
    default String topByRacesCountLoginToPageFilePath(final String rootDir) {
        return String.format("%s/js/voc-%s-top-by-races-count-login-to-page.js", rootDir, getVocabularyCode());
    }
    default String topByRacesCountExcelFilePath(final String rootDir) {
        return String.format("%s/xlsx/voc-%s-top-by-races-count.xlsx", rootDir, getVocabularyCode());
    }
    default String topByRacesCountExcelZipFilePath(final String rootDir) {
        return String.format("%s/xlsx/voc-%s-top-by-races-count.zip", rootDir, getVocabularyCode());
    }

    default String topByHaulFilePath(final String rootDir, final int pageNumber) {
        return String.format("%s/voc-%s-top-by-haul-page-%d.html", rootDir, getVocabularyCode(), pageNumber);
    }
    default String topByHaulLoginToPageFilePath(final String rootDir) {
        return String.format("%s/js/voc-%s-top-by-haul-login-to-page.js", rootDir, getVocabularyCode());
    }
    default String topByHaulExcelFilePath(final String rootDir) {
        return String.format("%s/xlsx/voc-%s-top-by-haul.xlsx", rootDir, getVocabularyCode());
    }
    default String topByHaulExcelZipFilePath(final String rootDir) {
        return String.format("%s/xlsx/voc-%s-top-by-haul.zip", rootDir, getVocabularyCode());
    }

    @Override
    default void export(ExportContext context) {
        PlayerVocabularyDtoMapper mapper = Mappers.getMapper(PlayerVocabularyDtoMapper.class);

        List<PlayerVocabularyStatsEntity> playersByBestSpeed = getPlayersByBestSpeed();
        List<PlayerVocabularyDto> dtosByBestSpeed = mapper.entitiesToDtos(playersByBestSpeed, PlayerVocabularyDto::getBestSpeed);

        int totalPlayersByBestSpeed = playersByBestSpeed.size();

        int totalPagesByBestSpeed = ExporterUtils.getPagesCount(totalPlayersByBestSpeed, getPageSize());
        getLogger().debug("Top by best speed: total pages {}", totalPagesByBestSpeed);

        Map<String, Integer> loginToPageByBestSpeed = new HashMap<>();
        String topByBestSpeedLoginToPageFilePath = topByBestSpeedLoginToPageFilePath(context.webRootDir);

        for (int pageNumber = ExporterUtils.FIRST_PAGE_NUMBER; pageNumber <= totalPagesByBestSpeed; pageNumber++) {
            List<PlayerVocabularyDto> playersOnPage = ExporterUtils.subList(dtosByBestSpeed, getPageSize(), pageNumber);

            final int finalPageNumber = pageNumber; // to use in lambda, must be effectively final
            playersOnPage.forEach(player -> {
                String login = player.getLogin();
                if (StringUtils.isBlank(login)) {
                    getLogger().warn("Player {} has no login. Do not put it to login -> page map.", player.getPlayerId());
                    return;
                }

                loginToPageByBestSpeed.put(login.toLowerCase(), finalPageNumber); // save lower-case login to make search case-insensitive
            });

            // export top by speed page to html
            String topBySpeedPageFilePath = topByBestSpeedPageFilePath(context.webRootDir, pageNumber);

            new VocabularyTopBySpeedTemplate()
                .pageTitle(topByBestSpeedPageTitle())
                .header(topByBestSpeedHeader())
                .additionalHeader(topByBestSpeedAdditionalHeader())
                .totalPages(totalPagesByBestSpeed)
                .pageNumber(pageNumber)
                .players(playersOnPage)
                .loginToPageJsPath(topByBestSpeedLoginToPageFilePath) // todo: should be changed to a relative path!
                .export(topBySpeedPageFilePath);

            getLogger().debug("Top by best speed: Exported page {}/{}.", pageNumber, totalPagesByBestSpeed);
        }

        // todo: export loginToPage to JS

        // todo: export toExcel

        // todo: top by racesCount
        // todo: top by haul
    }
}
