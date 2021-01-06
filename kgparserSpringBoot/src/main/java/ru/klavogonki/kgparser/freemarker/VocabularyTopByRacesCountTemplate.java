package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class VocabularyTopByRacesCountTemplate extends VocabularyTopTemplate {

    @Override
    public String getTemplatePath() {
        return "ftl/vocabulary-top-by-races-count.ftl";
    }

    @Override
    public void export(final String filePath) {
        super.export(filePath);

        logger.debug(
            "Top by races count {} players (page {}) exported to file {}",
            getPlayers().size(),
            getPageNumber(),
            filePath
        );
    }
}
