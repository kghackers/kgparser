package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;

import java.util.List;

@Log4j2
@SuppressWarnings("unchecked")
public class VocabularyTopBySpeedTemplate extends FreemarkerTemplate {

    private static final String PAGE_TITLE_KEY = "pageTitle";
    private static final String HEADER_KEY = "header";
    private static final String ADDITIONAL_HEADER_KEY = "additionalHeader";
    private static final String TOTAL_PAGES_KEY = "totalPages";
    private static final String PAGE_NUMBER_KEY = "pageNumber";
    private static final String PLAYERS_KEY = "players";
    private static final String LOGIN_TO_PAGE_JS_PATH_KEY = "loginToPageJsPath";
    private static final String PAGE_URL_TEMPLATE_PATH_KEY = "pageUrlTemplate";
    private static final String EXCEL_ZIP_URL_KEY = "excelZipUrl";

    @Override
    public String getTemplatePath() {
        return "ftl/vocabulary-top-by-best-speed.ftl";
    }

    public VocabularyTopBySpeedTemplate pageTitle(String pageTitle) {
        templateData.put(PAGE_TITLE_KEY, pageTitle);
        return this;
    }

    public VocabularyTopBySpeedTemplate header(String header) {
        templateData.put(HEADER_KEY, header);
        return this;
    }

    public VocabularyTopBySpeedTemplate additionalHeader(String additionalHeader) {
        templateData.put(ADDITIONAL_HEADER_KEY, additionalHeader);
        return this;
    }

    public VocabularyTopBySpeedTemplate totalPages(int totalPages) {
        templateData.put(TOTAL_PAGES_KEY, totalPages);
        return this;
    }

    public VocabularyTopBySpeedTemplate pageNumber(int pageNumber) {
        templateData.put(PAGE_NUMBER_KEY, pageNumber);
        return this;
    }

    public Integer getPageNumber() {
        return (Integer) templateData.get(PAGE_NUMBER_KEY);
    }

    public VocabularyTopBySpeedTemplate players(List<PlayerVocabularyDto> players) {
        templateData.put(PLAYERS_KEY, players);
        return this;
    }

    public List<PlayerVocabularyDto> getPlayers() {
        return (List<PlayerVocabularyDto>) templateData.get(PLAYERS_KEY);
    }

    public VocabularyTopBySpeedTemplate loginToPageJsPath(String loginToPageJsPath) {
        templateData.put(LOGIN_TO_PAGE_JS_PATH_KEY, loginToPageJsPath);
        return this;
    }

    public VocabularyTopBySpeedTemplate pageUrlTemplate(String pageUrlTemplate) {
        templateData.put(PAGE_URL_TEMPLATE_PATH_KEY, pageUrlTemplate);
        return this;
    }

    public VocabularyTopBySpeedTemplate excelZipUrl(String excelZipUrl) {
        templateData.put(EXCEL_ZIP_URL_KEY, excelZipUrl);
        return this;
    }

    @Override
    public void export(final String filePath) {
        super.export(filePath);

        logger.debug(
            "Top by speed {} players (page {}) exported to file {}",
            getPlayers().size(),
            getPageNumber(),
            filePath
        );
    }
}
