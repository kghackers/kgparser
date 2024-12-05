package ru.klavogonki.statistics.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.common.Rank;
import ru.klavogonki.statistics.entity.PlayerEntity;
import ru.klavogonki.statistics.export.ExportContext;
import ru.klavogonki.statistics.export.ExportContextFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * Used to try out the FreeMarker features without waiting for getting the data from the database.
 */
@Log4j2
public class ExampleTemplate extends FreemarkerTemplate {

    @Override
    public String getTemplatePath() {
        return "ftl/example.ftl";
    }

    @Override
    public void export(final ExportContext context, final String filePath) {
        templateData.put("testString", "A&nbsp;value&nbsp;test&nbsp;non&nbsp;breaking&nbsp;space!!!!");
        templateData.put("testInteger", 123456789);
        templateData.put("testDouble", 222.265);

        PlayerEntity player = new PlayerEntity();
        player.setLogin("nosferatum");
        player.setPlayerId(242585);
        player.setRankLevel(Rank.superman.level);

        templateData.put("testPlayer", player);

        Map<String, String> map = new TreeMap<>();
        map.put("key 1", "value 1");
        map.put("key 2", "value 2");
        map.put("key 3", "value 3");

        templateData.put("testMap", map);

        Map<Integer, PlayerEntity> idToPlayerMap = new TreeMap<>();
        idToPlayerMap.put(242585, player);
//        intMap.put(2, "value 2");

        templateData.put("idToPlayerMap", idToPlayerMap);

        templateData.put("links", context.links);

        String result = super.exportToString(context);
        logger.debug("result:\n{}", result);

        exportFreemarkerToFile(
            getTemplatePath(),
            filePath,
            templateData
        );
    }

    public static void main(String[] args) {
        ExportContext context = ExportContextFactory.INSTANCE.createMock();

        String outputFileName = "C:\\java\\kgparser\\.ignoreme\\freemarker-example.html";

        new ExampleTemplate().export(context, outputFileName);
    }
}
