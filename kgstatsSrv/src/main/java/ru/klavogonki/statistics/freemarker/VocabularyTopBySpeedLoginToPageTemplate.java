package ru.klavogonki.statistics.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.statistics.export.ExportContext;

@Log4j2
public class VocabularyTopBySpeedLoginToPageTemplate extends VocabularyTopLoginToPageTemplate {

    @Override
    public String getTemplatePath() {
        return "ftl/vocabulary-top-by-best-speed-login-to-page.ftl";
    }

    @Override
    public void export(ExportContext context, String filePath) {
        // todo: validate keys presence?
        super.export(context, filePath);

        logger.debug(
            "Top by best speed: saved login -> page mapping to file {}. Total logins: {}",
            filePath,
            getLoginToPage().size()
        );
    }
}
