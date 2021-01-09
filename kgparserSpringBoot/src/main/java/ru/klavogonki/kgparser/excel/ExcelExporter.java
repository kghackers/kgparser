package ru.klavogonki.kgparser.excel;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.StandardDictionary;
import ru.klavogonki.kgparser.excel.data.ExcelExportContextData;
import ru.klavogonki.kgparser.excel.player.AchievementsCountColumn;
import ru.klavogonki.kgparser.excel.player.BestSpeedColumn;
import ru.klavogonki.kgparser.excel.player.CarsCountColumn;
import ru.klavogonki.kgparser.excel.player.FriendsCountColumn;
import ru.klavogonki.kgparser.excel.player.LoginColumn;
import ru.klavogonki.kgparser.excel.player.OrderNumberColumn;
import ru.klavogonki.kgparser.excel.player.PlayerColumn;
import ru.klavogonki.kgparser.excel.player.ProfileLinkColumn;
import ru.klavogonki.kgparser.excel.player.RatingLevelColumn;
import ru.klavogonki.kgparser.excel.player.RegisteredColumn;
import ru.klavogonki.kgparser.excel.player.TotalRacesCountColumn;
import ru.klavogonki.kgparser.excel.player.VocabulariesCountColumn;
import ru.klavogonki.kgparser.http.UrlConstructor;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerDto;
import ru.klavogonki.kgparser.jsonParser.dto.PlayerVocabularyDto;
import ru.klavogonki.kgparser.util.DateUtils;

import java.util.List;

/**
 * See http://poi.apache.org/components/spreadsheet/quick-guide.html#CreateDateCells
 * and http://poi.apache.org/components/spreadsheet/quick-guide.html#CellTypes
 * for examples with different cell types.
 */
@Log4j2
public class ExcelExporter {

    public static final int MAX_SHEET_NAME_LENGTH = 31; // for freaking Windows Excel
    public static final String FORBIDDEN_EXCEL_LINK_CHARACTER = "#"; // for freaking Windows Excel
    private static final int HEADER_ROW = 0;
    private static final String BORDER_COLOR = "#808080";
    private static final String HEADER_BACKGROUND_COLOR = "#E0E0E0";
    private static final String EVEN_ROW_BACKGROUND_COLOR = "#FFFFFF";
    private static final String ODD_ROW_BACKGROUND_COLOR = "#EEEEEE";

    public static void main(String[] args) {
//        String filePath = "C:/java/kg/xls/export-top-by-total-races-count.xlsx";
//        testExportTotalRacesCountTop(filePath);

        String topBySpeedXlsx = "C:/java/kg/xls/export-top-by-speed.xlsx";
        String topBySpeedZip = "C:/java/kg/xls/export-top-by-speed.zip";
        testExportTopByVocabularySpeed(topBySpeedXlsx, topBySpeedZip);
    }

    public static void testExportTopByVocabularySpeed(String filePath, String zipFilePath) {
        PlayerVocabularyDto player1 = new PlayerVocabularyDto();
        player1.setOrderNumber("1");
        player1.setPlayerId(169106);
        player1.setLogin("170000");
        player1.setRank(Rank.extracyber);
        player1.setBestSpeed(652);
        player1.setRacesCount(6703);
        player1.setAverageSpeed(525.438);
        player1.setAverageError(2.0761);
        player1.setQual(546);
        player1.setHaul("44 ч. 27 мин. 1 сек.");
        player1.setUpdatedDateTime(DateUtils.parseLocalDateTimeWithUiDateFormat("2020-12-27 16:04:01"));
        player1.setVocabularyStatsLinkWithoutHash(UrlConstructor.userStatsByVocabularyWithoutHash(player1.getPlayerId(), StandardDictionary.chars.name()));

        List<PlayerVocabularyDto> players = List.of(
            player1
        );

        new VocabularyTopByBestSpeedExcelTemplate("Топ по скорости в «Буквах»")
            .players(players)
            .export(
                filePath,
                zipFilePath
            );
    }

    public static void testExportTotalRacesCountTop(String filePath) {
        String sheetName = "Топ-500 по общему пробегу";

        PlayerDto player1 = new PlayerDto();
        player1.setRank(Rank.extracyber);
        player1.setOrderNumber("1");
        player1.setLogin("ant1k");
        player1.setPlayerId(146269);
        player1.setProfileLink(UrlConstructor.userProfileLinkWithNoHash(player1.getPlayerId()));
        player1.setBestSpeed(1070);
        player1.setTotalRacesCount(30259);
        player1.setRegistered("2009-07-31 12:31:23");
        player1.setRegisteredDateTime(DateUtils.parseLocalDateTimeWithUiDateFormat("2009-07-31 12:31:23"));
        player1.setAchievementsCount(74);
        player1.setRatingLevel(38);
        player1.setFriendsCount(101);
        player1.setVocabulariesCount(44);
        player1.setCarsCount(8);

        PlayerDto player2 = new PlayerDto();
        player2.setRank(Rank.maniac);
        player2.setOrderNumber("2–3");
        player2.setLogin("170000");
        player2.setPlayerId(169106);
        player2.setProfileLink(UrlConstructor.userProfileLinkWithNoHash(player2.getPlayerId()));
        player2.setBestSpeed(967);
        player2.setTotalRacesCount(57976);
        player2.setRegistered("2009-12-26 14:30:55");
        player2.setRegisteredDateTime(DateUtils.parseLocalDateTimeWithUiDateFormat("2009-12-26 14:30:55"));
        player2.setAchievementsCount(121);
        player2.setRatingLevel(53);
        player2.setFriendsCount(178);
        player2.setVocabulariesCount(32);
        player2.setCarsCount(19);

        PlayerDto player3 = new PlayerDto();
        player3.setRank(Rank.amateur);
        player3.setOrderNumber("2–3");
        player3.setLogin("HRUST");
        player3.setPlayerId(61254);
        player3.setProfileLink(UrlConstructor.userProfileLinkWithNoHash(player3.getPlayerId()));
        player3.setBestSpeed(967);
        player3.setTotalRacesCount(154973);
        player3.setRegistered("2008-11-04 18:38:19");
        player3.setRegisteredDateTime(DateUtils.parseLocalDateTimeWithUiDateFormat("2008-11-04 18:38:19"));
        player3.setAchievementsCount(220);
        player3.setRatingLevel(62);
        player3.setFriendsCount(375);
        player3.setVocabulariesCount(150);
        player3.setCarsCount(7);

        PlayerDto player4 = new PlayerDto(); // maximum nulls, test it
        player4.setPlayerId(123456);

        List<PlayerDto> players = List.of(player1, player2, player3, player4);

        exportTotalRacesCountTop(filePath, sheetName, players);
    }

    public static void exportTotalRacesCountTop(String filePath, String sheetName, List<PlayerDto> players) {
        // same as TopByTotalRacesCountExcelTemplate
        List<? extends PlayerColumn<PlayerDto, ?>> columns = List.of(
            new OrderNumberColumn<>(),
            new LoginColumn<>(),
            new ProfileLinkColumn<>(),
            new TotalRacesCountColumn(),
            new BestSpeedColumn(),
            new RegisteredColumn(),
            new AchievementsCountColumn(),
            new RatingLevelColumn(),
            new FriendsCountColumn(),
            new VocabulariesCountColumn(),
            new CarsCountColumn()
        );

        export(filePath, sheetName, players, columns);
    }

    public static boolean isValidSheetName(String sheetName) {
        if (StringUtils.isBlank(sheetName)) {
            return false;
        }

        return sheetName.length() <= MAX_SHEET_NAME_LENGTH;
    }

    public static void validateSheetName(String sheetName) {
        if (StringUtils.isBlank(sheetName)) {
            throw new IllegalArgumentException("Excel sheet name cannot be null or empty.");
        }

        if (sheetName.length() > MAX_SHEET_NAME_LENGTH) {
            throw new IllegalArgumentException(String.format(
                "Excel sheet name cannot be longer than %d characters (Windows Excel will cut it). Sheet name \"%s\" is %d characters long.",
                MAX_SHEET_NAME_LENGTH,
                sheetName,
                sheetName.length()
            ));
        }
    }

    public static void validateHyperlink(String hyperlink) {
        if (StringUtils.isBlank(hyperlink)) {
            throw new IllegalArgumentException("Excel hyperlink cannot be null or empty.");
        }

        if (hyperlink.contains(FORBIDDEN_EXCEL_LINK_CHARACTER)) {
            throw new IllegalArgumentException(String.format(
                "Excel hyperlink cannot contain %s character (Windows Excel will not handle it). Hyperlink \"%s\" contains %s character.",
                FORBIDDEN_EXCEL_LINK_CHARACTER,
                hyperlink,
                FORBIDDEN_EXCEL_LINK_CHARACTER
            ));
        }
    }

    public static <D extends ExcelExportContextData> void export(
        String filePath,
        String sheetName,
        List<D> players,
        List<? extends PlayerColumn<D, ?>> columns
    ) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        ExcelStylesMap.Config config = createStylesMapConfig(workbook);

        ExcelExportContext<D> context = ExcelExportContext.initContext(workbook, config);

        ExcelExporter.validateSheetName(sheetName);
        Sheet sheet = context.workbook.createSheet(sheetName);

        setColumnWidths(columns, sheet);

        addHeaderRow(columns, context, sheet);

        addPlayerRows(players, columns, context, sheet);

        ExcelUtils.writeToFile(context.workbook, filePath);
    }

    private static ExcelStylesMap.Config createStylesMapConfig(XSSFWorkbook workbook) {
        ExcelStylesMap.Config config = new ExcelStylesMap.Config();
        config.headerBackgroundColorHex = HEADER_BACKGROUND_COLOR;
        config.borderColorHex = BORDER_COLOR;
        config.evenRowBackgroundColorHex = EVEN_ROW_BACKGROUND_COLOR;
        config.oddRowBackgroundColorHex = ODD_ROW_BACKGROUND_COLOR;
        config.linkFont = ExcelUtils.getLinkFont(workbook);

        return config;
    }

    private static void setColumnWidths(final List<? extends PlayerColumn<?, ?>> columns, final Sheet sheet) {
        for (int columnNumber = 0; columnNumber < columns.size(); columnNumber++) {
            final PlayerColumn<?, ?> column = columns.get(columnNumber);
            sheet.setColumnWidth(columnNumber, column.getColumnWidth());
        }
    }

    private static void addHeaderRow(final List<? extends PlayerColumn<?, ?>> columns, final ExcelExportContext context, final Sheet sheet) {
        // header row
        Row headerRow = sheet.createRow(HEADER_ROW);

        // add all columns from the header row
        for (int columnNumber = 0; columnNumber < columns.size(); columnNumber++) {
            final PlayerColumn<?, ?> column = columns.get(columnNumber);
            String headerText = column.getColumnName();

            Cell headerCell = headerRow.createCell(columnNumber);
            headerCell.setCellValue(headerText);
            context.setStyle(headerCell, ExcelStylesMap.Style.HEADER);

            logger.debug("Added \"{}\" header to row {}, column {}.", headerText, HEADER_ROW, columnNumber);
        }
    }

    private static <D extends ExcelExportContextData> void addPlayerRows(
        final List<D> players,
        final List<? extends PlayerColumn<D, ?>> columns,
        final ExcelExportContext<D> context,
        final Sheet sheet
    ) {
        int rowNumber = HEADER_ROW + 1;

        for (D player : players) {
            Row playerRow = sheet.createRow(rowNumber);

            for (int columnNumber = 0; columnNumber < columns.size(); columnNumber++) {
                final PlayerColumn<D, ?> column = columns.get(columnNumber);

                Cell cell = playerRow.createCell(columnNumber);

                context.rowNumber = rowNumber;
                context.cell = cell;
                context.player = player;

                column.formatCell(context);

                logger.debug("Added player \"{}\" column \"{}\" to row {}, column {}.", player.getLogin(), column.getColumnName(), rowNumber, columnNumber);
            }

            rowNumber++;
        }
    }
}
