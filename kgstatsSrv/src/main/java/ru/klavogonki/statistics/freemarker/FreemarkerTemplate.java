package ru.klavogonki.statistics.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import lombok.extern.log4j.Log4j2;
import ru.klavogonki.statistics.export.ExportContext;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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

    public void export(ExportContext context, String filePath) {
        Links links = Links.create(context);
        templateData.put(LINKS_KEY, links);

        exportFreemarkerToFile(getTemplatePath(), filePath, templateData);
    }

    public String exportToString(ExportContext context) {
        Links links = Links.create(context);
        templateData.put(LINKS_KEY, links);

        return exportFreemarkerToString(getTemplatePath(), templateData);
    }

    protected static void exportFreemarkerToFile(
        final String ftlTemplate,
        final String filePath,
        final Map<String, Object> templateData
    ) {
        try (FileWriter out = new FileWriter(filePath)) {
            export(ftlTemplate, templateData, out);
        }
        catch (IOException | TemplateException e) {
            throw new RuntimeException(e); // todo: special dedicated RuntimeException
        }
    }

    protected static String exportFreemarkerToString(
        final String ftlTemplate,
        final Map<String, Object> templateData
    ) {
        try (Writer out = new StringWriter()) {
            export(ftlTemplate, templateData, out);
            return out.toString();
        }
        catch (IOException | TemplateException e) {
            throw new RuntimeException(e); // todo: special dedicated RuntimeException
        }
    }

    private static void export(final String ftlTemplate, final Map<String, Object> templateData, final Writer out) throws IOException, TemplateException {
        Configuration configuration = new Configuration(new Version(FREEMARKER_VERSION));

        configuration.setClassForTemplateLoading(FreemarkerTemplate.class, "/");
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.displayName());

        configuration.setAPIBuiltinEnabled(true); // required to use non-string keys in maps

        Template template = configuration.getTemplate(ftlTemplate);

        template.process(templateData, out);

        out.flush();
    }
}
