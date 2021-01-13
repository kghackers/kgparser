package ru.klavogonki.kgparser.excel.player;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.excel.ExcelExportContext;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;

import java.time.LocalDateTime;
import java.util.function.Function;

@Log4j2
public class VocabularyUpdatedColumn implements PlayerColumn<PlayerVocabularyDto, LocalDateTime> {

    @Override
    public String getColumnName() {
        return "Обновлено";
    }

    @Override
    public int getColumnWidth() {
        return 6000;
    }

    @Override
    public Function<PlayerVocabularyDto, LocalDateTime> playerFieldGetter() {
        return PlayerVocabularyDto::getUpdatedDateTime;
    }

    @Override
    public Class<LocalDateTime> fieldClass() {
        return LocalDateTime.class;
    }

    @Override
    public void formatCell(final ExcelExportContext<PlayerVocabularyDto> context) {
        LocalDateTime registered = getValue(context.player);

        if (registered == null) {
            logger.warn("Player with id = {}, login = \"{}\" has no updatedDateTime. Cannot add an updated date field for this player.", context.player.getPlayerId(), context.player.getLogin());

            context.setTextAlignRightStyle();

            context.cell.setCellValue("—");
            return;
        }

        context.setDateTimeStyle();
        context.cell.setCellValue(registered);
    }
}
