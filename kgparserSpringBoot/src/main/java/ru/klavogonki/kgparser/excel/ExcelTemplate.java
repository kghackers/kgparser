package ru.klavogonki.kgparser.excel;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.excel.player.PlayerColumn;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;
import ru.klavogonki.kgparser.zip.ZipUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
public abstract class ExcelTemplate {

    // todo: in perspective, make PlayerDto type a <T> template
    protected List<PlayerDto> players = new ArrayList<>();

    // Sheet name in excel is limited to 31 chars, it will be to 31 characters if too long
    // @see https://excel.uservoice.com/forums/304921-excel-for-windows-desktop-application/suggestions/10770162-allow-31-characters-in-a-sheet-name
    public abstract String getSheetName();

    public abstract List<? extends PlayerColumn<?>> getColumns();

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(final List<PlayerDto> players) {
        this.players = players;
    }

    public ExcelTemplate players(final List<PlayerDto> players) {
        Objects.requireNonNull(players);

        this.players = players;
        return this;
    }

    public void export(String excelFilePath, String zipFilePath) {
        ExcelExporter.export(
            excelFilePath,
            getSheetName(),
            players,
            getColumns()
        );

        ZipUtils.zipFile(excelFilePath, zipFilePath);
    }
}
