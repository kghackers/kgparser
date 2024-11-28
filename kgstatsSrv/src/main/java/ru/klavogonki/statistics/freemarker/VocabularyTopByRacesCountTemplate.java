package ru.klavogonki.statistics.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.statistics.export.ExportContext;

@Log4j2
public class VocabularyTopByRacesCountTemplate extends VocabularyTopTemplate {

    @Override
    public String getTemplatePath() {
        return "ftl/vocabulary-top-by-races-count.ftl";
    }

    @Override
    public void export(ExportContext context, String filePath) {
        super.export(context, filePath);

        logger.debug(
            "Top by races count {} players (page {}) exported to file {}",
            getPlayers().size(),
            getPageNumber(),
            filePath
        );
    }
}
