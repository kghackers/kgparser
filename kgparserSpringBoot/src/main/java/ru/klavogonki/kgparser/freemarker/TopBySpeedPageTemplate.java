package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.List;

@Log4j2
public class TopBySpeedPageTemplate extends FreemarkerTemplate {

    private static final String TOTAL_PAGES_KEY = "totalPages";
    private static final String PAGE_NUMBER_KEY = "pageNumber";
    private static final String PLAYERS_KEY = "players";
    private static final String TOTAL_RACES_COUNT_MIN_KEY = "totalRacesCountMin";

    @Override
    public String getTemplatePath() {
        return "ftl/top-by-speed-page.ftl";
    }

    public TopBySpeedPageTemplate totalPages(int totalPages) {
        templateData.put(TOTAL_PAGES_KEY, totalPages);
        return this;
    }

    public Integer getTotalPages() {
        return (Integer) templateData.get(TOTAL_PAGES_KEY);
    }

    public TopBySpeedPageTemplate pageNumber(int pageNumber) {
        templateData.put(PAGE_NUMBER_KEY, pageNumber);
        return this;
    }

    public Integer getPageNumber() {
        return (Integer) templateData.get(PAGE_NUMBER_KEY);
    }

    public TopBySpeedPageTemplate players(List<PlayerDto> players) {
        templateData.put(PLAYERS_KEY, players);
        return this;
    }

    public List<PlayerDto> getPlayers() {
        return (List<PlayerDto>) templateData.get(PLAYERS_KEY);
    }

    public TopBySpeedPageTemplate totalRacesCountMin(int totalRacesCountMin) {
        templateData.put(TOTAL_RACES_COUNT_MIN_KEY, Integer.toString(totalRacesCountMin)); // #toString to avoid ugly Freemarker formatting
        return this;
    }

    public Integer getTotalRacesCountMin() {
        return (Integer) templateData.get(TOTAL_RACES_COUNT_MIN_KEY);
    }

    @Override
    public void export(final String filePath) {
        // todo: validate keys presence?
        super.export(filePath);

        logger.debug(
            "Top speed {} players (page {}) exported to file {}",
            getPlayers().size(),
            getPageNumber(),
            filePath
        );
    }

    public void exportTopPageToHtml(int totalPages, int pageNumber, List<PlayerDto> players, int totalRacesCountMin, String filePath) {
        totalPages(totalPages);
        pageNumber(pageNumber);
        players(players);
        totalRacesCountMin(totalRacesCountMin);

        export(filePath);
        logger.debug("Top speed {} players (page {}) exported to file {}", players.size(), pageNumber, filePath);
    }

}
