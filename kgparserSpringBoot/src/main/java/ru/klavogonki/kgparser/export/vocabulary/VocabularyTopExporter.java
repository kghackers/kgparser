package ru.klavogonki.kgparser.export.vocabulary;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import ru.klavogonki.kgparser.excel.ExcelExporter;
import ru.klavogonki.kgparser.excel.VocabularyTopByBestSpeedExcelTemplate;
import ru.klavogonki.kgparser.excel.VocabularyTopByHaulExcelTemplate;
import ru.klavogonki.kgparser.excel.VocabularyTopByRacesCountExcelTemplate;
import ru.klavogonki.kgparser.export.DataExporter;
import ru.klavogonki.kgparser.export.ExportContext;
import ru.klavogonki.kgparser.export.ExporterUtils;
import ru.klavogonki.kgparser.freemarker.PageUrls;
import ru.klavogonki.kgparser.freemarker.VocabularyTopByHaulLoginToPageTemplate;
import ru.klavogonki.kgparser.freemarker.VocabularyTopByHaulTemplate;
import ru.klavogonki.kgparser.freemarker.VocabularyTopByRacesCountLoginToPageTemplate;
import ru.klavogonki.kgparser.freemarker.VocabularyTopByRacesCountTemplate;
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

    boolean isStandard();

    String vocabularyCode();

    int minRacesCount();

    Logger logger();

    default int pageSize() {
        return 100;
    }

    default String topByBestSpeedPageTitle() { // <title> in html
        return "DEFAULT_TOP_BY_BEST_SPEED_PAGE_TITLE";
    }
    default String topByBestSpeedHeader() { // <h2> in html
        return "DEFAULT_TOP_BY_BEST_SPEED_PAGE_HEADER";
    }
    default String topByBestSpeedAdditionalHeader() { // under <h2> in html
        return "DEFAULT_TOP_BY_BEST_SPEED_ADDITIONAL_HEADER";
    }
    default String topByBestSpeedExcelSheetName() { // Excel sheet name, may be shorter as html header because of 31 chars limit
        return "DEFAULT_TOP_BY_BEST_SPEED_EXCEL_SHEET_NAME";
    }

    default String topByRacesCountPageTitle() { // <title> in html
        return "DEFAULT_TOP_BY_RACES_COUNT_PAGE_TITLE";
    }
    default String topByRacesCountHeader() { // <h2> in html
        return "DEFAULT_TOP_BY_RACES_COUNT_PAGE_HEADER";
    }
    default String topByRacesCountAdditionalHeader() { // under <h2> in html
        return "DEFAULT_TOP_BY_RACES_COUNT_ADDITIONAL_HEADER";
    }
    default String topByRacesCountExcelSheetName() { // Excel sheet name, may be shorter as html header because of 31 chars limit
        return "DEFAULT_TOP_BY_RACES_COUNT_EXCEL_SHEET_NAME";
    }

    default String topByHaulPageTitle() { // <title> in html
        return "DEFAULT_TOP_BY_HAUL_PAGE_TITLE";
    }
    default String topByHaulHeader() { // <h2> in html
        return "DEFAULT_TOP_BY_HAUL_PAGE_HEADER";
    }
    default String topByHaulAdditionalHeader() { // under <h2> in html
        return "DEFAULT_TOP_BY_HAUL_ADDITIONAL_HEADER";
    }
    default String topByHaulExcelSheetName() { // Excel sheet name, may be shorter as html header because of 31 chars limit
        return "DEFAULT_TOP_BY_HAUL_EXCEL_SHEET_NAME";
    }

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
        return String.format("./voc-%s-top-by-best-speed-page-%d.html", vocabularyCode(), pageNumber);
    }
    default String topByBestSpeedPageFileTemplate() { // for js paging
        return String.format("./voc-%s-top-by-best-speed-page-${%s}.html", vocabularyCode(), PAGE_NUMBER_JS_TEMPLATE_VARIABLE);
    }
    default String topByBestSpeedLoginToPageFilePath() {
        return String.format("./js/voc-%s-top-by-best-speed-login-to-page.js", vocabularyCode());
    }
    default String topByBestSpeedExcelFilePath() {
        return String.format("./xlsx/voc-%s-top-by-best-speed.xlsx", vocabularyCode());
    }
    default String topByBestSpeedExcelZipFilePath() {
        return String.format("./xlsx/voc-%s-top-by-best-speed.zip", vocabularyCode());
    }

    default String topByRacesCountPageFilePath(final int pageNumber) {
        return String.format("./voc-%s-top-by-races-count-page-%d.html", vocabularyCode(), pageNumber);
    }
    default String topByRacesCountPageFileTemplate() { // for js paging
        return String.format("./voc-%s-top-by-races-count-page-${%s}.html", vocabularyCode(), PAGE_NUMBER_JS_TEMPLATE_VARIABLE);
    }
    default String topByRacesCountLoginToPageFilePath() {
        return String.format("./js/voc-%s-top-by-races-count-login-to-page.js", vocabularyCode());
    }
    default String topByRacesCountExcelFilePath() {
        return String.format("./xlsx/voc-%s-top-by-races-count.xlsx", vocabularyCode());
    }
    default String topByRacesCountExcelZipFilePath() {
        return String.format("./xlsx/voc-%s-top-by-races-count.zip", vocabularyCode());
    }

    default String topByHaulPageFilePath(final int pageNumber) {
        return String.format("./voc-%s-top-by-haul-page-%d.html", vocabularyCode(), pageNumber);
    }
    default String topByHaulPageFileTemplate() { // for js paging
        return String.format("./voc-%s-top-by-haul-page-${%s}.html", vocabularyCode(), PAGE_NUMBER_JS_TEMPLATE_VARIABLE);
    }
    default String topByHaulLoginToPageFilePath() {
        return String.format("./js/voc-%s-top-by-haul-login-to-page.js", vocabularyCode());
    }
    default String topByHaulExcelFilePath() {
        return String.format("./xlsx/voc-%s-top-by-haul.xlsx", vocabularyCode());
    }
    default String topByHaulExcelZipFilePath() {
        return String.format("./xlsx/voc-%s-top-by-haul.zip", vocabularyCode());
    }

    @Override
    default void export(ExportContext context) {
        validate();

        PlayerVocabularyDtoMapper mapper = Mappers.getMapper(PlayerVocabularyDtoMapper.class);

        exportTopByBestSpeed(context, mapper);
        exportTopByRacesCount(context, mapper);
        exportTopByHaul(context, mapper);
    }

    private void validate() { // fail fast, not in ExcelExporter
        ExcelExporter.validateSheetName(topByBestSpeedExcelSheetName());
        ExcelExporter.validateSheetName(topByRacesCountExcelSheetName());
        ExcelExporter.validateSheetName(topByHaulExcelSheetName());
    }

    private void exportTopByBestSpeed(final ExportContext context, final PlayerVocabularyDtoMapper mapper) {
        List<PlayerVocabularyStatsEntity> players = getPlayersByBestSpeed();
        List<PlayerVocabularyDto> dtos = mapper.entitiesToDtos(players, PlayerVocabularyDto::getBestSpeed);

        int totalPlayers = players.size();

        int totalPages = ExporterUtils.getPagesCount(totalPlayers, pageSize());
        logger().debug("Top by best speed: total pages {}", totalPages);

        Map<String, Integer> loginToPage = new HashMap<>();

        for (int pageNumber = ExporterUtils.FIRST_PAGE_NUMBER; pageNumber <= totalPages; pageNumber++) {
            List<PlayerVocabularyDto> playersOnPage = ExporterUtils.subList(dtos, pageSize(), pageNumber);

            final int finalPageNumber = pageNumber; // to use in lambda, must be effectively final
            playersOnPage.forEach(player -> {
                String login = player.getLogin();
                if (StringUtils.isBlank(login)) {
                    logger().warn("Player {} has no login. Do not put it to login -> page map.", player.getPlayerId());
                    return;
                }

                loginToPage.put(login.toLowerCase(), finalPageNumber); // save lower-case login to make search case-insensitive
            });

            // export top by best speed page to html
            String pageFilePath = PageUrls.getPath(context, topByBestSpeedPageFilePath(pageNumber));

            new VocabularyTopBySpeedTemplate()
                .pageTitle(topByBestSpeedPageTitle())
                .header(topByBestSpeedHeader())
                .additionalHeader(topByBestSpeedAdditionalHeader())
                .totalPages(totalPages)
                .pageNumber(pageNumber)
                .players(playersOnPage)
                .loginToPageJsPath(topByBestSpeedLoginToPageFilePath()) // relative path
                .pageUrlTemplate(topByBestSpeedPageFileTemplate()) // to fill paging links in js
                .excelUrl(topByBestSpeedExcelFilePath())
                .excelZipUrl(topByBestSpeedExcelZipFilePath())
                .topByBestSpeedUrl(topByBestSpeedPageFilePath(ExporterUtils.FIRST_PAGE_NUMBER))
                .topByRacesCountUrl(topByRacesCountPageFilePath(ExporterUtils.FIRST_PAGE_NUMBER))
                .topByHaulUrl(topByHaulPageFilePath(ExporterUtils.FIRST_PAGE_NUMBER))
                .export(pageFilePath);

            logger().debug("Top by best speed: Exported page {}/{}.", pageNumber, totalPages);
        }

        exportTopByBestSpeedLoginToPageJs(context, loginToPage);

        exportTopByBestSpeedToExcel(context, dtos);
    }

    private void exportTopByRacesCount(final ExportContext context, final PlayerVocabularyDtoMapper mapper) {
        List<PlayerVocabularyStatsEntity> players = getPlayersByRacesCount();
        List<PlayerVocabularyDto> dtos = mapper.entitiesToDtos(players, PlayerVocabularyDto::getRacesCount);

        int totalPlayers = players.size();

        int totalPages = ExporterUtils.getPagesCount(totalPlayers, pageSize());
        logger().debug("Top by races count: total pages {}", totalPages);

        Map<String, Integer> loginToPage = new HashMap<>();

        for (int pageNumber = ExporterUtils.FIRST_PAGE_NUMBER; pageNumber <= totalPages; pageNumber++) {
            List<PlayerVocabularyDto> playersOnPage = ExporterUtils.subList(dtos, pageSize(), pageNumber);

            final int finalPageNumber = pageNumber; // to use in lambda, must be effectively final
            playersOnPage.forEach(player -> {
                String login = player.getLogin();
                if (StringUtils.isBlank(login)) {
                    logger().warn("Player {} has no login. Do not put it to login -> page map.", player.getPlayerId());
                    return;
                }

                loginToPage.put(login.toLowerCase(), finalPageNumber); // save lower-case login to make search case-insensitive
            });

            // export top by races count page to html
            String pageFilePath = PageUrls.getPath(context, topByRacesCountPageFilePath(pageNumber));

            new VocabularyTopByRacesCountTemplate()
                .pageTitle(topByRacesCountPageTitle())
                .header(topByRacesCountHeader())
                .additionalHeader(topByRacesCountAdditionalHeader())
                .totalPages(totalPages)
                .pageNumber(pageNumber)
                .players(playersOnPage)
                .loginToPageJsPath(topByRacesCountLoginToPageFilePath()) // relative path
                .pageUrlTemplate(topByRacesCountPageFileTemplate()) // to fill paging links in js
                .excelUrl(topByRacesCountExcelFilePath())
                .excelZipUrl(topByRacesCountExcelZipFilePath())
                .topByBestSpeedUrl(topByBestSpeedPageFilePath(ExporterUtils.FIRST_PAGE_NUMBER))
                .topByRacesCountUrl(topByRacesCountPageFilePath(ExporterUtils.FIRST_PAGE_NUMBER))
                .topByHaulUrl(topByHaulPageFilePath(ExporterUtils.FIRST_PAGE_NUMBER))
                .export(pageFilePath);

            logger().debug("Top by races count: Exported page {}/{}.", pageNumber, totalPages);
        }

        exportTopByRacesCountLoginToPageJs(context, loginToPage);

        exportTopByRacesCountToExcel(context, dtos);
    }

    private void exportTopByHaul(final ExportContext context, final PlayerVocabularyDtoMapper mapper) {
        List<PlayerVocabularyStatsEntity> players = getPlayersByHaul();
        List<PlayerVocabularyDto> dtos = mapper.entitiesToDtos(players, PlayerVocabularyDto::getHaulInteger);

        int totalPlayers = players.size();

        int totalPages = ExporterUtils.getPagesCount(totalPlayers, pageSize());
        logger().debug("Top by haul: total pages {}", totalPages);

        Map<String, Integer> loginToPage = new HashMap<>();

        for (int pageNumber = ExporterUtils.FIRST_PAGE_NUMBER; pageNumber <= totalPages; pageNumber++) {
            List<PlayerVocabularyDto> playersOnPage = ExporterUtils.subList(dtos, pageSize(), pageNumber);

            final int finalPageNumber = pageNumber; // to use in lambda, must be effectively final
            playersOnPage.forEach(player -> {
                String login = player.getLogin();
                if (StringUtils.isBlank(login)) {
                    logger().warn("Player {} has no login. Do not put it to login -> page map.", player.getPlayerId());
                    return;
                }

                loginToPage.put(login.toLowerCase(), finalPageNumber); // save lower-case login to make search case-insensitive
            });

            // export top by haul page to html
            String pageFilePath = PageUrls.getPath(context, topByHaulPageFilePath(pageNumber));

            new VocabularyTopByHaulTemplate()
                .pageTitle(topByHaulPageTitle())
                .header(topByHaulHeader())
                .additionalHeader(topByHaulAdditionalHeader())
                .totalPages(totalPages)
                .pageNumber(pageNumber)
                .players(playersOnPage)
                .loginToPageJsPath(topByHaulLoginToPageFilePath()) // relative path
                .pageUrlTemplate(topByHaulPageFileTemplate()) // to fill paging links in js
                .excelUrl(topByHaulExcelFilePath())
                .excelZipUrl(topByHaulExcelZipFilePath())
                .topByBestSpeedUrl(topByBestSpeedPageFilePath(ExporterUtils.FIRST_PAGE_NUMBER))
                .topByRacesCountUrl(topByRacesCountPageFilePath(ExporterUtils.FIRST_PAGE_NUMBER))
                .topByHaulUrl(topByHaulPageFilePath(ExporterUtils.FIRST_PAGE_NUMBER))
                .export(pageFilePath);

            logger().debug("Top by haul: Exported page {}/{}.", pageNumber, totalPages);
        }

        exportTopByHaulLoginToPageJs(context, loginToPage);

        exportTopByHaulToExcel(context, dtos);
    }


    private void exportTopByBestSpeedLoginToPageJs(final ExportContext context, final Map<String, Integer> loginToPage) {
        // export login -> page map to a js file
        String loginToPageFilePath = PageUrls.getPath(context, topByBestSpeedLoginToPageFilePath());

        String loginToPageString = JacksonUtils.serializeToString(loginToPage);

        new VocabularyTopBySpeedLoginToPageTemplate()
            .loginToPage(loginToPage)
            .loginToPageString(loginToPageString)
            .export(loginToPageFilePath);
    }

    private void exportTopByRacesCountLoginToPageJs(final ExportContext context, final Map<String, Integer> loginToPage) {
        // export login -> page map to a js file
        String loginToPageFilePath = PageUrls.getPath(context, topByRacesCountLoginToPageFilePath());

        String loginToPageString = JacksonUtils.serializeToString(loginToPage);

        new VocabularyTopByRacesCountLoginToPageTemplate()
            .loginToPage(loginToPage)
            .loginToPageString(loginToPageString)
            .export(loginToPageFilePath);
    }

    private void exportTopByHaulLoginToPageJs(final ExportContext context, final Map<String, Integer> loginToPage) {
        // export login -> page map to a js file
        String loginToPageFilePath = PageUrls.getPath(context, topByHaulLoginToPageFilePath());

        String loginToPageString = JacksonUtils.serializeToString(loginToPage);

        new VocabularyTopByHaulLoginToPageTemplate()
            .loginToPage(loginToPage)
            .loginToPageString(loginToPageString)
            .export(loginToPageFilePath);
    }


    private void exportTopByBestSpeedToExcel(final ExportContext context, final List<PlayerVocabularyDto> dtos) {
        // export all pages to Excel and Excel zip
        new VocabularyTopByBestSpeedExcelTemplate(topByBestSpeedExcelSheetName())
            .players(dtos)
            .export(
                PageUrls.getPath(context, topByBestSpeedExcelFilePath()),
                PageUrls.getPath(context, topByBestSpeedExcelZipFilePath())
            );
    }

    private void exportTopByRacesCountToExcel(final ExportContext context, final List<PlayerVocabularyDto> dtos) {
        // export all pages to Excel and Excel zip
        new VocabularyTopByRacesCountExcelTemplate(topByRacesCountExcelSheetName())
            .players(dtos)
            .export(
                PageUrls.getPath(context, topByRacesCountExcelFilePath()),
                PageUrls.getPath(context, topByRacesCountExcelZipFilePath())
            );
    }

    private void exportTopByHaulToExcel(final ExportContext context, final List<PlayerVocabularyDto> dtos) {
        // export all pages to Excel and Excel zip
        new VocabularyTopByHaulExcelTemplate(topByHaulExcelSheetName())
            .players(dtos)
            .export(
                PageUrls.getPath(context, topByHaulExcelFilePath()),
                PageUrls.getPath(context, topByHaulExcelZipFilePath())
            );
    }
}
