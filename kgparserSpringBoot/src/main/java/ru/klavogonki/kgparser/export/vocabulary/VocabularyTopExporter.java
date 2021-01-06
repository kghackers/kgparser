package ru.klavogonki.kgparser.export.vocabulary;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import ru.klavogonki.kgparser.export.DataExporter;
import ru.klavogonki.kgparser.export.ExportContext;
import ru.klavogonki.kgparser.export.ExporterUtils;
import ru.klavogonki.kgparser.freemarker.PageUrls;
import ru.klavogonki.kgparser.freemarker.VocabularyTopBySpeedLoginToPageTemplate;
import ru.klavogonki.kgparser.freemarker.VocabularyTopBySpeedTemplate;
import ru.klavogonki.kgparser.jsonParser.JacksonUtils;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerVocabularyStatsEntity;
import ru.klavogonki.kgparser.jsonParser.mapper.PlayerVocabularyDtoMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VocabularyTopExporter extends DataExporter {

    String PAGE_NUMBER_JS_TEMPLATE_VARIABLE = "pageNumber";

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

    default String topByBestSpeedPageFilePath(final int pageNumber) {
        return String.format("./voc-%s-top-by-best-speed-page-%d.html", getVocabularyCode(), pageNumber);
    }
    default String topByBestSpeedPageFileTemplate() { // for js paging
        return String.format("./voc-%s-top-by-best-speed-page-${%s}.html", getVocabularyCode(), PAGE_NUMBER_JS_TEMPLATE_VARIABLE);
    }
    default String topByBestSpeedLoginToPageFilePath() {
        return String.format("./js/voc-%s-top-by-best-speed-login-to-page.js", getVocabularyCode());
    }
    default String topByBestSpeedExcelFilePath() {
        return String.format("./xlsx/voc-%s-top-by-best-speed.xlsx", getVocabularyCode());
    }
    default String topByBestSpeedExcelZipFilePath() {
        return String.format("./xlsx/voc-%s-top-by-best-speed.zip", getVocabularyCode());
    }

    default String topByRacesCountFilePath(final int pageNumber) {
        return String.format("./voc-%s-top-by-races-count-page-%d.html", getVocabularyCode(), pageNumber);
    }
    default String topByRacesCountLoginToPageFilePath() {
        return String.format("./js/voc-%s-top-by-races-count-login-to-page.js", getVocabularyCode());
    }
    default String topByRacesCountExcelFilePath() {
        return String.format("./xlsx/voc-%s-top-by-races-count.xlsx", getVocabularyCode());
    }
    default String topByRacesCountExcelZipFilePath() {
        return String.format("./xlsx/voc-%s-top-by-races-count.zip", getVocabularyCode());
    }

    default String topByHaulFilePath(final int pageNumber) {
        return String.format("./voc-%s-top-by-haul-page-%d.html", getVocabularyCode(), pageNumber);
    }
    default String topByHaulLoginToPageFilePath() {
        return String.format("./js/voc-%s-top-by-haul-login-to-page.js", getVocabularyCode());
    }
    default String topByHaulExcelFilePath() {
        return String.format("./xlsx/voc-%s-top-by-haul.xlsx", getVocabularyCode());
    }
    default String topByHaulExcelZipFilePath() {
        return String.format("./xlsx/voc-%s-top-by-haul.zip", getVocabularyCode());
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
            String topBySpeedPageFilePath = PageUrls.getPath(context, topByBestSpeedPageFilePath(pageNumber));

            new VocabularyTopBySpeedTemplate()
                .pageTitle(topByBestSpeedPageTitle())
                .header(topByBestSpeedHeader())
                .additionalHeader(topByBestSpeedAdditionalHeader())
                .totalPages(totalPagesByBestSpeed)
                .pageNumber(pageNumber)
                .players(playersOnPage)
                .loginToPageJsPath(topByBestSpeedLoginToPageFilePath()) // relative path
                .pageUrlTemplate(topByBestSpeedPageFileTemplate()) // to fill paging links in js
                .export(topBySpeedPageFilePath);

            getLogger().debug("Top by best speed: Exported page {}/{}.", pageNumber, totalPagesByBestSpeed);
        }

        // export login -> page map to a js file
        String loginToPageFilePath = PageUrls.getPath(context, topByBestSpeedLoginToPageFilePath());

        String loginToPageString = JacksonUtils.serializeToString(loginToPageByBestSpeed);

        new VocabularyTopBySpeedLoginToPageTemplate()
            .loginToPage(loginToPageByBestSpeed)
            .loginToPageString(loginToPageString)
            .export(loginToPageFilePath);

        // todo: export toExcel

        // todo: top by racesCount
        // todo: top by haul
    }
}
