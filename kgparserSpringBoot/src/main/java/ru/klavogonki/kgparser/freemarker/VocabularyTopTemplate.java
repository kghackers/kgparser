package ru.klavogonki.kgparser.freemarker;

import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;

import java.util.List;

@SuppressWarnings("unchecked")
public abstract class VocabularyTopTemplate extends FreemarkerTemplate {

    private static final String PAGE_TITLE_KEY = "pageTitle";
    private static final String HEADER_KEY = "header";
    private static final String ADDITIONAL_HEADER_KEY = "additionalHeader";
    private static final String TOTAL_PAGES_KEY = "totalPages";
    private static final String PAGE_NUMBER_KEY = "pageNumber";
    private static final String PLAYERS_KEY = "players";
    private static final String LOGIN_TO_PAGE_JS_PATH_KEY = "loginToPageJsPath";
    private static final String PAGE_URL_TEMPLATE_PATH_KEY = "pageUrlTemplate";
    private static final String EXCEL_URL_KEY = "excelUrl";
    private static final String EXCEL_ZIP_URL_KEY = "excelZipUrl";
    private static final String TOP_BY_BEST_SPEED_URL = "topByBestSpeedUrl";
    private static final String TOP_BY_RACES_COUNT_URL = "topByRacesCountUrl";
    private static final String TOP_BY_HAUL_URL = "topByHaulUrl";

    public VocabularyTopTemplate pageTitle(String pageTitle) {
        templateData.put(PAGE_TITLE_KEY, pageTitle);
        return this;
    }

    public VocabularyTopTemplate header(String header) {
        templateData.put(HEADER_KEY, header);
        return this;
    }

    public VocabularyTopTemplate additionalHeader(String additionalHeader) {
        templateData.put(ADDITIONAL_HEADER_KEY, additionalHeader);
        return this;
    }

    public VocabularyTopTemplate totalPages(int totalPages) {
        templateData.put(TOTAL_PAGES_KEY, totalPages);
        return this;
    }

    public VocabularyTopTemplate pageNumber(int pageNumber) {
        templateData.put(PAGE_NUMBER_KEY, pageNumber);
        return this;
    }

    public Integer getPageNumber() {
        return (Integer) templateData.get(PAGE_NUMBER_KEY);
    }

    public VocabularyTopTemplate players(List<PlayerVocabularyDto> players) {
        templateData.put(PLAYERS_KEY, players);
        return this;
    }

    public List<PlayerVocabularyDto> getPlayers() {
        return (List<PlayerVocabularyDto>) templateData.get(PLAYERS_KEY);
    }

    public VocabularyTopTemplate loginToPageJsPath(String loginToPageJsPath) {
        templateData.put(LOGIN_TO_PAGE_JS_PATH_KEY, loginToPageJsPath);
        return this;
    }

    public VocabularyTopTemplate pageUrlTemplate(String pageUrlTemplate) {
        templateData.put(PAGE_URL_TEMPLATE_PATH_KEY, pageUrlTemplate);
        return this;
    }

    public VocabularyTopTemplate excelUrl(String excelUrl) {
        templateData.put(EXCEL_URL_KEY, excelUrl);
        return this;
    }

    public VocabularyTopTemplate excelZipUrl(String excelZipUrl) {
        templateData.put(EXCEL_ZIP_URL_KEY, excelZipUrl);
        return this;
    }

    public VocabularyTopTemplate topByBestSpeedUrl(String topByBestSpeedUrl) {
        templateData.put(TOP_BY_BEST_SPEED_URL, topByBestSpeedUrl);
        return this;
    }

    public VocabularyTopTemplate topByRacesCountUrl(String topByRacesCountUrl) {
        templateData.put(TOP_BY_RACES_COUNT_URL, topByRacesCountUrl);
        return this;
    }

    public VocabularyTopTemplate topByHaulUrl(String topByHaulUrl) {
        templateData.put(TOP_BY_HAUL_URL, topByHaulUrl);
        return this;
    }
}
