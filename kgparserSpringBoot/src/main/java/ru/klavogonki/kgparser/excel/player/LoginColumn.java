package ru.klavogonki.kgparser.excel.player;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.excel.ExcelExportContext;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.function.Function;

@Log4j2
public class LoginColumn implements PlayerColumn<String> {

    @Override
    public String getColumnName() {
        return "Логин";
    }

    @Override
    public int getColumnWidth() {
        return 6000;
    }

    @Override
    public Function<PlayerDto, String> playerFieldGetter() {
        return PlayerDto::getLogin;
    }

    @Override
    public Class<String> fieldClass() {
        return String.class;
    }

    @Override
    public void formatCell(final ExcelExportContext context) {
        Rank rank = context.player.getRank();
        if (rank == null) {
            logger.warn("Player with id = {}, login = \"{}\" has no rank. Cannot define text color for this player.", context.player.getPlayerId(), context.player.getLogin());

            context.setTextAlignLeftStyle();
            context.cell.setCellValue("—");
            return;
        }

        context.setRankTextColor();

        String login = context.player.getLogin();
        context.cell.setCellValue(login);
    }
}
