package ru.klavogonki.kgparser.excel;

import lombok.extern.log4j.Log4j2;
import ru.klavogonki.kgparser.excel.data.ExcelExportContextData;
import ru.klavogonki.kgparser.excel.player.PlayerColumn;
import ru.klavogonki.kgparser.zip.ZipUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
public abstract class ExcelTemplate<D extends ExcelExportContextData> {

    protected List<D> players = new ArrayList<>();

    // Sheet name in excel is limited to 31 chars, it will be to 31 characters if too long
    // @see https://excel.uservoice.com/forums/304921-excel-for-windows-desktop-application/suggestions/10770162-allow-31-characters-in-a-sheet-name
    public abstract String getSheetName();

    public abstract List<? extends PlayerColumn<D, ?>> getColumns();

    public List<D> getPlayers() {
        return players;
    }

    public void setPlayers(final List<D> players) {
        this.players = players;
    }

    public ExcelTemplate<D> players(final List<D> players) {
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
