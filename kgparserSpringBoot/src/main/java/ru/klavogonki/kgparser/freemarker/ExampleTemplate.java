package ru.klavogonki.kgparser.freemarker;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.jsonParser.entity.PlayerEntity;

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
    public void export(final String filePath) {
        templateData.put("testInteger", 123456789);
        templateData.put("testDouble", 222.265);

        PlayerEntity player = new PlayerEntity();
        player.setLogin("nosferatum");
        player.setPlayerId(242585);
        player.setRankLevel(Rank.getLevel(Rank.superman).intValue());

        templateData.put("testPlayer", player);

        String result = super.exportToString();
        logger.debug("result:\n{}", result);
    }

    public static void main(String[] args) {
        new ExampleTemplate().export("example.html");
    }
}
