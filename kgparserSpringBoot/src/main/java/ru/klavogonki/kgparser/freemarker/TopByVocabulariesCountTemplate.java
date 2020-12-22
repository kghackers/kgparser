package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.List;

@Log4j2
@SuppressWarnings("unchecked")
public class TopByVocabulariesCountTemplate extends FreemarkerTemplate {

    private static final String PLAYERS_KEY = "players";

    @Override
    public String getTemplatePath() {
        return "ftl/top-by-vocabularies-count.ftl";
    }

    public TopByVocabulariesCountTemplate players(List<PlayerDto> players) {
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
            "Top by vocabularies count: {} players exported to file {}",
            getPlayers().size(),
            filePath
        );
    }
}
