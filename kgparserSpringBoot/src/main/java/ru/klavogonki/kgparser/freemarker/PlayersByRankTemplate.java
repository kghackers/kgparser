package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PlayersByRankTemplate extends FreemarkerTemplate {

    private static final String TOTAL_PLAYERS_COUNT_KEY = "totalPlayersCount";
    private static final String MIN_TOTAL_RACES_COUNT_KEY = "minTotalRacesCount";
    private static final String MAX_TOTAL_RACES_COUNT_KEY = "maxTotalRacesCount";

    @Override
    public String getTemplatePath() {
        return "ftl/players-by-rank.ftl";
    }

    public PlayersByRankTemplate totalPlayersCount(Integer totalPlayersCount) {
        templateData.put(TOTAL_PLAYERS_COUNT_KEY, totalPlayersCount);
        return this;
    }
    public Integer getTotalPlayersCount() {
        return (Integer) templateData.get(TOTAL_PLAYERS_COUNT_KEY);
    }

    public PlayersByRankTemplate minTotalRacesCount(Integer minTotalRacesCount) {
        templateData.put(MIN_TOTAL_RACES_COUNT_KEY, minTotalRacesCount);
        return this;
    }
    public Integer getMinTotalRacesCount() {
        return (Integer) templateData.get(MIN_TOTAL_RACES_COUNT_KEY);
    }

    public PlayersByRankTemplate maxTotalRacesCount(Integer maxTotalRacesCount) {
        templateData.put(MAX_TOTAL_RACES_COUNT_KEY, maxTotalRacesCount);
        return this;
    }
    public Integer getMaxTotalRacesCount() {
        return (Integer) templateData.get(MAX_TOTAL_RACES_COUNT_KEY);
    }

    @Override
    public void export(final String filePath) {
        // todo: validate keys presence?
        super.export(filePath);

        logger.debug(
            "Exported player to rank page to file {}. Total players: {}, minTotalRacesCount: {}, maxTotalRacesCount: {}.",
            filePath,
            getTotalPlayersCount(),
            getMinTotalRacesCount(),
            getMaxTotalRacesCount()
        );
    }
}
