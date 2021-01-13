package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class VocabularyTopBySpeedTemplate extends VocabularyTopTemplate {

    @Override
    public String getTemplatePath() {
        return "ftl/vocabulary-top-by-best-speed.ftl";
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
