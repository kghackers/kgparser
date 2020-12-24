package ru.klavogonki.kgparser.excel.player;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.excel.ExcelExportContext;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.time.LocalDateTime;
import java.util.function.Function;

@Log4j2
public class RegisteredColumn implements PlayerColumn<LocalDateTime> {

    @Override
    public String getColumnName() {
        return "Зарегистрирован";
    }

    @Override
    public int getColumnWidth() {
        return 8000;
    }

    @Override
    public Function<PlayerDto, LocalDateTime> playerFieldGetter() {
        return PlayerDto::getRegisteredDateTime;
    }

    @Override
    public Class<LocalDateTime> fieldClass() {
        return LocalDateTime.class;
    }

    @Override
    public void formatCell(final ExcelExportContext context) {
        context.setAlignRight(); // date with a custom format is rendered right in Excel

        LocalDateTime registered = getValue(context.player);

        if (registered == null) {
            logger.warn("Player with id = {}, login = \"{}\" has no registeredDateTime. Cannot add a registerd date field for this player  player.", context.player.getPlayerId(), context.player.getLogin());

            context.setTextFormat();
            context.cell.setCellValue("—");
            return;
        }

        context.cell
            .getCellStyle()
            .setDataFormat(context.dataFormat.getFormat("yyyy-MM-dd HH:mm:ss"));

        context.cell.setCellValue(registered);
    }
}
