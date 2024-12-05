package ru.klavogonki.statistics.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.statistics.dto.PlayerDto;
import ru.klavogonki.statistics.export.ExportContext;

import java.util.List;

@Log4j2
@SuppressWarnings("unchecked")
public class TopByAchievementsCountTemplate extends FreemarkerTemplate {

    private static final String PLAYERS_KEY = "players";

    @Override
    public String getTemplatePath() {
        return "ftl/top-by-achievements-count.ftl";
    }

    public TopByAchievementsCountTemplate players(List<PlayerDto> players) {
        templateData.put(PLAYERS_KEY, players);
        return this;
    }

    public List<PlayerDto> getPlayers() {
        return (List<PlayerDto>) templateData.get(PLAYERS_KEY);
    }

    @Override
    public void export(ExportContext context, String filePath) {
        // todo: validate keys presence?
        super.export(context, filePath);

        logger.debug(
            "Top by achievements count: {} players exported to file {}",
            getPlayers().size(),
            filePath
        );
    }
}
