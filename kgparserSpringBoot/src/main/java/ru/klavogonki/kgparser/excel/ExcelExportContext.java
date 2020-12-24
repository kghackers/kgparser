package ru.klavogonki.kgparser.excel;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ExcelExportContext {

    public XSSFWorkbook workbook;

    public DataFormat dataFormat;

    public IndexedColorMap colorMap;

    public Cell cell;

    public PlayerDto player; // todo: this may be <T> parameterizable in the future when we need to export other types

    public Map<Rank, XSSFFont> rankToFont;

    private ExcelExportContext() {
        // creation allowed only via #initContext()
    }

    public static ExcelExportContext initContext() {
        XSSFWorkbook workbook = new XSSFWorkbook();

        DataFormat dataFormat = workbook.createDataFormat();

        DefaultIndexedColorMap colorMap = new DefaultIndexedColorMap();

        ExcelExportContext context = new ExcelExportContext();
        context.workbook = workbook;
        context.dataFormat = dataFormat;
        context.colorMap = colorMap;
        context.rankToFont = getRankToFontMap(context);
        return context;
    }

    private static Map<Rank, XSSFFont> getRankToFontMap(ExcelExportContext context) {
        Map<Rank, XSSFColor> rankToColor = getRankToColorMap(context);

        Map<Rank, XSSFFont> rankToFont = new HashMap<>();

        for (Rank rank : Rank.values()) {
            XSSFColor rankColor = rankToColor.get(rank);

            XSSFFont font = context.workbook.createFont();
//            font.setFontName("Arial");
//            font.setFontHeightInPoints((short) 16);
            font.setColor(rankColor);
//            font.setBold(true);

            rankToFont.put(rank, font);
        }

        return rankToFont;
    }

    private static Map<Rank, XSSFColor> getRankToColorMap(ExcelExportContext context) {
        IndexedColorMap colorMap = context.colorMap;

        Map<Rank, XSSFColor> rankToColor = new HashMap<>();
        for (Rank rank : Rank.values()) {
            XSSFColor rankColor = new XSSFColor(ExcelUtils.getRgb(rank), colorMap);
            rankToColor.put(rank, rankColor);
        }

        return rankToColor;
    }

    public void setIntegerFormat() {
        ExcelUtils.setIntegerFormat(cell, dataFormat);
    }

    public void setTextFormat() {
        ExcelUtils.setTextFormat(cell, dataFormat);
    }

    // todo: setDateFormat

    public void setAlignRight() {
        ExcelUtils.setAlignRight(cell);
    }

    public void setRankTextColor() {
        Rank rank = player.getRank();
        if (rank == null) {
            logger.warn("Player with id = {}, login = \"{}\" has no rank. Cannot define text color for this player.", player.getPlayerId(), player.getLogin());
            return;
        }

        XSSFFont font = rankToFont.get(rank);

        cell
            .getCellStyle()
            .setFont(font);
    }
}
