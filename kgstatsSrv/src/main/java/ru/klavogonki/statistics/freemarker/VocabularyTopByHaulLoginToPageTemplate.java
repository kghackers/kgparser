package ru.klavogonki.statistics.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.statistics.export.ExportContext;

@Log4j2
public class VocabularyTopByHaulLoginToPageTemplate extends VocabularyTopLoginToPageTemplate {

    @Override
    public String getTemplatePath() {
        return "ftl/vocabulary-top-by-haul-login-to-page.ftl";
    }

    @Override
    public void export(ExportContext context, String filePath) {
        // todo: validate keys presence?
        super.export(context, filePath);

        logger.debug(
            "Top by haul: saved login -> page mapping to file {}. Total logins: {}",
            filePath,
            getLoginToPage().size()
        );
    }
}
