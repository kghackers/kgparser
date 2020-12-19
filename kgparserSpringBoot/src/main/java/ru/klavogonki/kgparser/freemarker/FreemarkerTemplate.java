package ru.klavogonki.kgparser.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.export.TopBySpeedExporter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public abstract class FreemarkerTemplate {

    public static final String FREEMARKER_VERSION = "2.3.30";

    private static final String LINKS_KEY = "links";

    protected Map<String, Object> templateData = new HashMap<>();

    /**
     * @return file path in {@code /resources}
     */
    public abstract String getTemplatePath();

    public void export(String filePath) {
        templateData.put(LINKS_KEY, new Links());

        exportFreemarker(getTemplatePath(), filePath, templateData);
    }

    protected static void exportFreemarker(
        final String ftlTemplate,
        final String filePath,
        final Map<String, Object> templateData
    ) {
        try (FileWriter out = new FileWriter(filePath)) {
            Configuration cfg = new Configuration(new Version(FREEMARKER_VERSION));

            cfg.setClassForTemplateLoading(TopBySpeedExporter.class, "/");
            cfg.setDefaultEncoding(StandardCharsets.UTF_8.displayName());

            Template template = cfg.getTemplate(ftlTemplate);

            template.process(templateData, out);

            out.flush();
        }
        catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
