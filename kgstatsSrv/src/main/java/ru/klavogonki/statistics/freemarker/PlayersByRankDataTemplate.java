package ru.klavogonki.statistics.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.statistics.export.ExportContext;

@Log4j2
public class PlayersByRankDataTemplate extends FreemarkerTemplate {

    private static final String RANK_TO_TOTAL_RACES_COUNT_KEY = "rankToTotalRacesCount";

    @Override
    public String getTemplatePath() {
        return "ftl/players-by-rank-data.ftl";
    }

    public PlayersByRankDataTemplate rankToTotalRacesCount(String rankToTotalRacesCount) {
        templateData.put(RANK_TO_TOTAL_RACES_COUNT_KEY, rankToTotalRacesCount);
        return this;
    }

    public String getRankToTotalRacesCount() {
        return (String) templateData.get(RANK_TO_TOTAL_RACES_COUNT_KEY);
    }

    @Override
    public void export(ExportContext context, String filePath) {
        // todo: validate keys presence?
        super.export(context, filePath);

        logger.debug(
            "Saved rankLevel -> totalRacesCount mapping to file {}.",
            filePath
        );
    }
}
