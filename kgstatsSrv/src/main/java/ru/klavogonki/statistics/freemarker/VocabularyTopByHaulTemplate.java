package ru.klavogonki.statistics.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.statistics.export.ExportContext;

@Log4j2
public class VocabularyTopByHaulTemplate extends VocabularyTopTemplate {

    @Override
    public String getTemplatePath() {
        return "ftl/vocabulary-top-by-haul.ftl";
    }

    @Override
    public void export(ExportContext context, String filePath) {
        super.export(context, filePath);

        logger.debug(
            "Top by haul {} players (page {}) exported to file {}",
            getPlayers().size(),
            getPageNumber(),
            filePath
        );
    }
}
