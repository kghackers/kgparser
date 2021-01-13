package ru.klavogonki.kgparser.excel.player;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import ru.klavogonki.kgparser.excel.ExcelExportContext;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;

import java.util.function.Function;

@Log4j2
public class VocabularyStatsLinkColumn implements PlayerColumn<PlayerVocabularyDto, String> {

    @Override
    public String getColumnName() {
        return "Стат. по словарю";
    }

    @Override
    public int getColumnWidth() {
        return 5000;
    }

    @Override
    public Function<PlayerVocabularyDto, String> playerFieldGetter() {
        return PlayerVocabularyDto::getVocabularyStatsLink;
    }

    @Override
    public Class<String> fieldClass() {
        return String.class;
    }

    @Override
    public void formatCell(final ExcelExportContext<PlayerVocabularyDto> context) {
        String link = context.player.getVocabularyStatsLinkWithoutHash();
        if (StringUtils.isBlank(link)) {
            logger.warn("Player with id = {}, login = \"{}\" has no vocabularyStatsLinkWithoutHash. Cannot add a hyperlink to this player.", context.player.getPlayerId(), context.player.getLogin());

            setEmptyCell(context);
        }

        // use a special link with no # in it, since Excel has a bug with links containing #
        context.setIntegerHyperlink(link, context.player.getPlayerId()); // todo: think about this,this will also set link integer format
    }

    public void setEmptyCell(final ExcelExportContext<PlayerVocabularyDto> context) { // todo: make a common method, like in ExcelExportContext?
        context.setTextAlignRightStyle(); // since profileIds are numbers, align right // todo: think about this
        context.cell.setCellValue("—");
        return;
    }
}
