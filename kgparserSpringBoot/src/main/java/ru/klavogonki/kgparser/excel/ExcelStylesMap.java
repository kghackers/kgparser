package ru.klavogonki.kgparser.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.excel.data.ExcelExportContextData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * We cannot use more than 64000 cell styles in Excel template.
 * Therefore we have to pre-create all required styles and reuse them.
 * @see <a href="https://bz.apache.org/bugzilla/show_bug.cgi?id=63613">Bug in Apache POI</a>
 * @see <a href="https://docs.microsoft.com/en-us/office/troubleshoot/excel/too-many-different-cell-formats-in-excel">You receive a "Too many different cell formats" error message in Excel</a>
 */
public class ExcelStylesMap {

    private final Map<Style, XSSFCellStyle> styleMap = new HashMap<>();
    private final Map<Rank, RankStyles> rankStyleMap = new HashMap<>();

    public enum Style { // names of styles
        HEADER,
        INTEGER_EVEN_ROW,
        INTEGER_ODD_ROW,
        DOUBLE_EVEN_ROW,
        DOUBLE_ODD_ROW,
        TEXT_ALIGN_LEFT_EVEN_ROW,
        TEXT_ALIGN_LEFT_ODD_ROW,
        TEXT_ALIGN_RIGHT_EVEN_ROW,
        TEXT_ALIGN_RIGHT_ODD_ROW,
        DATE_TIME_EVEN_ROW,
        DATE_TIME_ODD_ROW,
        LINK_INTEGER_EVEN_ROW,
        LINK_INTEGER_ODD_ROW,
    }

    private static class RankStyles {
        XSSFCellStyle evenRowStyle;
        XSSFCellStyle oddRowStyle;
    }

    private ExcelStylesMap() {
        // only create via #init
    }

    public static class Config {
        public String headerBackgroundColorHex;
        public String borderColorHex;
        public String evenRowBackgroundColorHex;
        public String oddRowBackgroundColorHex;
        public XSSFFont linkFont;
    }

    public static <D extends ExcelExportContextData> ExcelStylesMap init(ExcelExportContext<D> context, Config config) {
        ExcelStylesMap map = new ExcelStylesMap();

        map.addHeaderStyle(context, config.headerBackgroundColorHex, config.borderColorHex);

        map.addIntegerEvenRowStyle(context, config.evenRowBackgroundColorHex, config.borderColorHex);
        map.addIntegerOddRowStyle(context, config.oddRowBackgroundColorHex, config.borderColorHex);

        map.addDoubleEvenRowStyle(context, config.evenRowBackgroundColorHex, config.borderColorHex);
        map.addDoubleOddRowStyle(context, config.oddRowBackgroundColorHex, config.borderColorHex);

        map.addTextAlignLeftEvenRowStyle(context, config.evenRowBackgroundColorHex, config.borderColorHex);
        map.addTextAlignLeftOddRowStyle(context, config.oddRowBackgroundColorHex, config.borderColorHex);

        map.addTextAlignRightEvenRowStyle(context, config.evenRowBackgroundColorHex, config.borderColorHex);
        map.addTextAlignRightOddRowStyle(context, config.oddRowBackgroundColorHex, config.borderColorHex);

        map.addDateTimeEvenRowStyle(context, config.evenRowBackgroundColorHex, config.borderColorHex);
        map.addDateTimeOddRowStyle(context, config.oddRowBackgroundColorHex, config.borderColorHex);

        map.addLinkIntegerEvenRowStyle(context, config.evenRowBackgroundColorHex, config.borderColorHex, config.linkFont);
        map.addLinkIntegerOddRowStyle(context, config.oddRowBackgroundColorHex, config.borderColorHex, config.linkFont);

        map.addRankStyles(context, config.evenRowBackgroundColorHex, config.oddRowBackgroundColorHex, config.borderColorHex);

        return map;
    }

    public void addStyle(Style style, XSSFCellStyle cellStyle) {
        styleMap.put(style, cellStyle);
    }

    public XSSFCellStyle getStyle(Style style) {
        if (!styleMap.containsKey(style)) {
            throw new IllegalArgumentException(String.format("Style %s is not present in styles map.", style));
        }

        return styleMap.get(style);
    }

    public void setStyle(Cell cell, Style style) {
        cell.setCellStyle(getStyle(style));
    }

    public void setOddRowRankStyle(Cell cell, Rank rank) {
        Objects.requireNonNull(rank);

        RankStyles rankStyles = rankStyleMap.get(rank);
        cell.setCellStyle(rankStyles.oddRowStyle);
    }

    public void setEvenRowRankStyle(Cell cell, Rank rank) {
        Objects.requireNonNull(rank);

        RankStyles rankStyles = rankStyleMap.get(rank);
        cell.setCellStyle(rankStyles.evenRowStyle);
    }

    public void addHeaderStyle(ExcelExportContext context, String backgroundColorHex, String borderColorHex) {
        XSSFCellStyle headerStyle = ExcelUtils.createStyle(context.workbook, context.colorMap, backgroundColorHex, borderColorHex);
        ExcelUtils.setAlignCenter(headerStyle);
        ExcelUtils.setTextFormat(headerStyle, context.dataFormat);

        addStyle(Style.HEADER, headerStyle);
    }

    public void addIntegerEvenRowStyle(ExcelExportContext context, String evenRowBackgroundColorHex, String borderColorHex) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, evenRowBackgroundColorHex, borderColorHex);

        ExcelUtils.setIntegerFormat(style, context.dataFormat); // integer aligns right

        addStyle(Style.INTEGER_EVEN_ROW, style);
    }

    public void addIntegerOddRowStyle(ExcelExportContext context, String oddRowBackgroundColorHex, String borderColorHex) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, oddRowBackgroundColorHex, borderColorHex);

        ExcelUtils.setIntegerFormat(style, context.dataFormat); // integer aligns right

        addStyle(Style.INTEGER_ODD_ROW, style);
    }

    public void addDoubleEvenRowStyle(ExcelExportContext context, String evenRowBackgroundColorHex, String borderColorHex) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, evenRowBackgroundColorHex, borderColorHex);

        ExcelUtils.setDoubleFormat(style, context.dataFormat); // double aligns right

        addStyle(Style.DOUBLE_EVEN_ROW, style);
    }

    public void addDoubleOddRowStyle(ExcelExportContext context, String oddRowBackgroundColorHex, String borderColorHex) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, oddRowBackgroundColorHex, borderColorHex);

        ExcelUtils.setDoubleFormat(style, context.dataFormat); // double aligns right

        addStyle(Style.DOUBLE_ODD_ROW, style);
    }

    public void addTextAlignLeftEvenRowStyle(ExcelExportContext context, String evenRowBackgroundColorHex, String borderColorHex) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, evenRowBackgroundColorHex, borderColorHex);

        ExcelUtils.setTextFormat(style, context.dataFormat);
        ExcelUtils.setAlignLeft(style);

        addStyle(Style.TEXT_ALIGN_LEFT_EVEN_ROW, style);
    }

    public void addTextAlignLeftOddRowStyle(ExcelExportContext context, String oddRowBackgroundColorHex, String borderColorHex) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, oddRowBackgroundColorHex, borderColorHex);

        ExcelUtils.setTextFormat(style, context.dataFormat);
        ExcelUtils.setAlignLeft(style);

        addStyle(Style.TEXT_ALIGN_LEFT_ODD_ROW, style);
    }

    public void addTextAlignRightEvenRowStyle(ExcelExportContext context, String evenRowBackgroundColorHex, String borderColorHex) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, evenRowBackgroundColorHex, borderColorHex);

        ExcelUtils.setTextFormat(style, context.dataFormat);
        ExcelUtils.setAlignRight(style);

        addStyle(Style.TEXT_ALIGN_RIGHT_EVEN_ROW, style);
    }

    public void addTextAlignRightOddRowStyle(ExcelExportContext context, String oddRowBackgroundColorHex, String borderColorHex) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, oddRowBackgroundColorHex, borderColorHex);

        ExcelUtils.setTextFormat(style, context.dataFormat);
        ExcelUtils.setAlignRight(style);

        addStyle(Style.TEXT_ALIGN_RIGHT_ODD_ROW, style);
    }

    public void addDateTimeEvenRowStyle(ExcelExportContext context, String evenRowBackgroundColorHex, String borderColorHex) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, evenRowBackgroundColorHex, borderColorHex);

        style.setDataFormat(context.dataFormat.getFormat("yyyy-MM-dd HH:mm:ss"));

        addStyle(Style.DATE_TIME_EVEN_ROW, style);
    }

    public void addDateTimeOddRowStyle(ExcelExportContext context, String oddRowBackgroundColorHex, String borderColorHex) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, oddRowBackgroundColorHex, borderColorHex);

        style.setDataFormat(context.dataFormat.getFormat("yyyy-MM-dd HH:mm:ss"));

        addStyle(Style.DATE_TIME_ODD_ROW, style);
    }

    public void addLinkIntegerEvenRowStyle(ExcelExportContext context, String evenRowBackgroundColorHex, String borderColorHex, Font linkFont) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, evenRowBackgroundColorHex, borderColorHex);

        style.setFont(linkFont);
        ExcelUtils.setIntegerFormat(style, context.dataFormat); // integer is displayed align-right

        addStyle(Style.LINK_INTEGER_EVEN_ROW, style);
    }

    public void addLinkIntegerOddRowStyle(ExcelExportContext context, String oddRowBackgroundColorHex, String borderColorHex, Font linkFont) {
        XSSFCellStyle style = ExcelUtils.createStyle(context.workbook, context.colorMap, oddRowBackgroundColorHex, borderColorHex);

        style.setFont(linkFont);
        ExcelUtils.setIntegerFormat(style, context.dataFormat); // integer is displayed align-right

        addStyle(Style.LINK_INTEGER_ODD_ROW, style);
    }

    public void addRankStyles(
        ExcelExportContext context,
        String evenRowBackgroundColorHex,
        String oddRowBackgroundColorHex,
        String borderColorHex
    ) {
        for (Rank rank : Rank.values()) {
            Map<Rank, XSSFFont> rankToFont = getRankToFontMap(context);
            XSSFFont rankFont = rankToFont.get(rank);

            XSSFCellStyle evenRowStyle = ExcelUtils.createStyle(context.workbook, context.colorMap, evenRowBackgroundColorHex, borderColorHex);
            ExcelUtils.setTextFormat(evenRowStyle, context.dataFormat);
            evenRowStyle.setFont(rankFont);

            XSSFCellStyle oddRowStyle = ExcelUtils.createStyle(context.workbook, context.colorMap, oddRowBackgroundColorHex, borderColorHex);
            ExcelUtils.setTextFormat(oddRowStyle, context.dataFormat);
            oddRowStyle.setFont(rankFont);

            RankStyles rankStyles = new RankStyles();
            rankStyles.evenRowStyle = evenRowStyle;
            rankStyles.oddRowStyle = oddRowStyle;

            rankStyleMap.put(rank, rankStyles);
        }
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
}
