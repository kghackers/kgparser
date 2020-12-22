package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.List;

@Log4j2
@SuppressWarnings("unchecked")
public class TopByFriendsCountTemplate extends FreemarkerTemplate {

    private static final String PLAYERS_KEY = "players";

    @Override
    public String getTemplatePath() {
        return "ftl/top-by-friends-count.ftl";
    }

    public TopByFriendsCountTemplate players(List<PlayerDto> players) {
        templateData.put(PLAYERS_KEY, players);
        return this;
    }

    public List<PlayerDto> getPlayers() {
        return (List<PlayerDto>) templateData.get(PLAYERS_KEY);
    }

    @Override
    public void export(final String filePath) {
        // todo: validate keys presence?
        super.export(filePath);

        logger.debug(
            "Top by friends count: {} players exported to file {}",
            getPlayers().size(),
            filePath
        );
    }
}
