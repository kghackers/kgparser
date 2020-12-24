package ru.klavogonki.kgparser.excel;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.klavogonki.kgparser.Rank;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * See http://poi.apache.org/components/spreadsheet/quick-guide.html#CreateDateCells
 * and http://poi.apache.org/components/spreadsheet/quick-guide.html#CellTypes
 * for examples with different cell types.
 */
@Log4j2
public class ExcelExporter {

    private static final int HEADER_ROW = 0;
    private static final String BORDER_COLOR = "#808080";
    private static final String HEADER_BACKGROUND_COLOR = "#E0E0E0";
    private static final String EVEN_ROW_BACKGROUND_COLOR = "#EEEEEE";
    private static final String ODD_ROW_BACKGROUND_COLOR = "#FFFFFF";

    public static void main(String[] args) {
        testExportTotalRacesCountTop();
        if (true) {
            return;
        }

        // from https://www.baeldung.com/java-microsoft-excel#2-writing-to-excel
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Persons");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 8000); // will be displayed as #### in case of not enough width

        Row header = sheet.createRow(0);

        // see https://stackoverflow.com/a/59005983/8534088
        // todo: maybe use CustomIndexedColorMap ! or somehow get ColorMap from Workbook and enrich it
        DefaultIndexedColorMap colorMap = new DefaultIndexedColorMap();
        XSSFColor color = new XSSFColor(ExcelUtils.getRgb(255, 153, 204), colorMap);

        // check rank colors
        int rowNumber = 3;
        int column = 0;

        ExcelExportContext context = ExcelExportContext.initContext();

        for (Rank rank : Rank.values()) {
            XSSFFont font = context.rankToFont.get(rank);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font); // cellStyle#setFillForegroundColor cannot take XSSFColor

            DataFormat dataFormat = workbook.createDataFormat();
            cellStyle.setDataFormat(dataFormat.getFormat("@")); // String format

            Row row = sheet.createRow(rowNumber++);

            Cell cell = row.createCell(column);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(Rank.getDisplayName(rank));
        }

        // check hyperlink
        // see https://stackoverflow.com/questions/57300034/how-to-use-apache-poi-to-create-excel-hyper-link-that-links-to-long-url
        XSSFCreationHelper creationHelper = workbook.getCreationHelper();
        Hyperlink link = creationHelper.createHyperlink(HyperlinkType.URL);

        // known bug of # in links in Excel:
        // see https://stackoverflow.com/questions/25070176/hyperlink-changes-from-to-20-20-when-clicked-in-excel
        String url = UrlConstructor.userProfileLinkWithNoHash(242585); // needs some escaping, # in URL
        // link with # does not work in freaking Excel, but if you import it into Spreadsheets, it will work
/*
        String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8);
        encodedUrl = "https://klavogonki.ru/u/%23/242585/";
        encodedUrl = "https%3A%2F%2Fklavogonki.ru%2Fu%2F%23%2F242585%2F"; // by https://www.w3schools.com/tags/ref_urlencode.asp
        encodedUrl = url;
*/

//        String url = "http://klavogonki.ru"; // works, no # in URL
        link.setAddress(url); // nosferatum
        logger.debug("Url: {}", url);
//        logger.debug("Encoded url: {}", encodedUrl);
        logger.debug("Set address {} to link", url);

        Row rowWithHyperlink = sheet.createRow(rowNumber++);
        Cell cellWithHyperLink = rowWithHyperlink.createCell(0);
        String cellText = "Open link to long URL having length of " + url.length() + " characters.";
        cellWithHyperLink.setCellValue(cellText);
        cellWithHyperLink.setHyperlink(link);

//        sheet.setColumnWidth(0, cellText.length() * 256);


        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setColor(color);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Age");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row = sheet.createRow(2);
        Cell cell = row.createCell(0);
        cell.setCellValue("John Smith");
        cell.setCellStyle(style);

        DataFormat dataFormat = workbook.createDataFormat();
        CellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setDataFormat(dataFormat.getFormat("0.00")); // works, 2 decimals
        numberCellStyle.setDataFormat(dataFormat.getFormat("0")); // no decimals. Works, with rounding
        numberCellStyle.setDataFormat(dataFormat.getFormat("@")); // text format, i.e. treat number as text see https://stackoverflow.com/a/36021850/8534088

        cell = row.createCell(1);
//        cell.setCellType(CellType.NUMERIC); // todo: this is deprecated :(
        cell.setCellValue(8789);
        cell.setCellStyle(numberCellStyle);


        LocalDateTime localDateTime = LocalDateTime.now();
        CellStyle dateCellStyle = workbook.createCellStyle();
//        DataFormat dataFormat2 = workbook.createDataFormat();

//        dateCellStyle.setDataFormat(dataFormat.getFormat("dd.mm.yyyy")); // works for LDT
        dateCellStyle.setDataFormat(dataFormat.getFormat("yyyy-MM-dd HH:mm:ss")); // same as in DateUtils, 2020-12-23 02:48:09. works!
//        dateCellStyle.setDataFormat(dataFormat.getFormat("m/d/yy h:mm")); // 23-12-20 02:45, works


        // treat as true "date" format, i.e. Locale-dependent, see https://stackoverflow.com/a/41124537/8534088
        // displays as 23-12-20 for me
//        dateCellStyle.setDataFormat((short)14); // see BuiltinFormats

        // treat as true "date" format, i.e. Locale-dependent, see https://stackoverflow.com/a/41124537/8534088
        // displays as 23-12-20 for me
//        dateCellStyle.setDataFormat((short)22); // custom, 23-12-20 02:54

        Cell dateCell = row.createCell(2);
//        dateCell.setCellValue(new Date());
        dateCell.setCellValue(localDateTime);
        dateCell.setCellStyle(dateCellStyle);

        ExcelUtils.writeToFile(workbook, "c:/java/test-poi.xlsx");
    }

    public static void testExportTotalRacesCountTop() {
        String filePath = "c:/java/export-top-by-total-races-count.xlsx";
        String sheetName = "Топ-500 по общему пробегу";

        PlayerDto player1 = new PlayerDto();
        player1.setRank(Rank.extracyber);
        player1.setOrderNumber("1");
        player1.setLogin("ant1k");
        player1.setProfileLink(UrlConstructor.userProfileLinkWithNoHash(146269));
        player1.setBestSpeed(1070);
        player1.setTotalRacesCount(30259);
        player1.setRegistered("2009-07-31 12:31:23"); // todo: good date
        player1.setAchievementsCount(74);
        player1.setRatingLevel(38);
        player1.setFriendsCount(101);
        player1.setVocabulariesCount(44);
        player1.setCarsCount(8);

        PlayerDto player2 = new PlayerDto();
        player2.setRank(Rank.maniac);
        player2.setOrderNumber("2–3");
        player2.setLogin("170000");
        player2.setProfileLink(UrlConstructor.userProfileLinkWithNoHash(169106));
        player2.setBestSpeed(967);
        player2.setTotalRacesCount(57976);
        player2.setRegistered("2009-12-26 14:30:55"); // todo: good date
        player2.setAchievementsCount(121);
        player2.setRatingLevel(53);
        player2.setFriendsCount(178);
        player2.setVocabulariesCount(32);
        player2.setCarsCount(19);

        PlayerDto player3 = new PlayerDto();
        player3.setRank(Rank.amateur);
        player3.setOrderNumber("2–3");
        player3.setLogin("HRUST");
        player3.setProfileLink(UrlConstructor.userProfileLinkWithNoHash(61254));
        player3.setBestSpeed(967);
        player3.setTotalRacesCount(154973);
        player3.setRegistered("2008-11-04 18:38:19"); // todo: good date
        player3.setAchievementsCount(220);
        player3.setRatingLevel(62);
        player3.setFriendsCount(375);
        player3.setVocabulariesCount(150);
        player3.setCarsCount(7);

        PlayerDto player4 = new PlayerDto(); // maximum nulls, test it
        player4.setRank(Rank.novice);
        player4.setPlayerId(123456);

        List<PlayerDto> players = List.of(player1, player2, player3, player4);

        exportTotalRacesCountTop(filePath, sheetName, players);
    }

    public static void exportTotalRacesCountTop(String filePath, String sheetName, List<PlayerDto> players) {
        List<? extends PlayerColumn<?>> columns = List.of(
            new OrderNumberColumn(),
            new LoginColumn(),
            new ProfileLinkColumn(),
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

    public static void export(
        String filePath,
        String sheetName,
        List<PlayerDto> players,
        List<? extends PlayerColumn<?>> columns
    ) {
        ExcelExportContext context = ExcelExportContext.initContext();

        Sheet sheet = context.workbook.createSheet(sheetName);

        setColumnWidths(columns, sheet);

        addHeaderRow(columns, context, sheet);

        addPlayerRows(players, columns, context, sheet);

        ExcelUtils.writeToFile(context.workbook, filePath);
    }

    private static void setColumnWidths(final List<? extends PlayerColumn<?>> columns, final Sheet sheet) {
        for (int columnNumber = 0; columnNumber < columns.size(); columnNumber++) {
            final PlayerColumn<?> column = columns.get(columnNumber);
            sheet.setColumnWidth(columnNumber, column.getColumnWidth());
        }
    }

    private static void addHeaderRow(final List<? extends PlayerColumn<?>> columns, final ExcelExportContext context, final Sheet sheet) {
        // header row
        Row headerRow = sheet.createRow(HEADER_ROW);

        // same style for all headers
        XSSFCellStyle headerStyle = ExcelUtils.createStyle(context.workbook, context.colorMap, HEADER_BACKGROUND_COLOR, BORDER_COLOR);
        ExcelUtils.setAlignCenter(headerStyle);
        ExcelUtils.setTextFormat(headerStyle, context.dataFormat);

        // add all columns from the header row
        for (int columnNumber = 0; columnNumber < columns.size(); columnNumber++) {
            final PlayerColumn<?> column = columns.get(columnNumber);
            String headerText = column.getColumnName();

            Cell headerCell = headerRow.createCell(columnNumber);
            headerCell.setCellValue(headerText);
            headerCell.setCellStyle(headerStyle);

            logger.debug("Added \"{}\" header to row {}, column {}.", headerText, HEADER_ROW, columnNumber);
        }
    }

    private static void addPlayerRows(
        final List<PlayerDto> players,
        final List<? extends PlayerColumn<?>> columns,
        final ExcelExportContext context,
        final Sheet sheet
    ) {
        int rowNumber = HEADER_ROW + 1;

        for (PlayerDto player : players) {
            String rowBackgroundColor = ((rowNumber % 2) == 0) ? EVEN_ROW_BACKGROUND_COLOR : ODD_ROW_BACKGROUND_COLOR;

            Row playerRow = sheet.createRow(rowNumber++);

            for (int columnNumber = 0; columnNumber < columns.size(); columnNumber++) {
                final PlayerColumn<?> column = columns.get(columnNumber);

                XSSFCellStyle cellStyle = ExcelUtils.createStyle(context.workbook, context.colorMap, rowBackgroundColor, BORDER_COLOR);

                Cell cell = playerRow.createCell(columnNumber);
                cell.setCellStyle(cellStyle);

                context.cell = cell;
                context.player = player;

                column.formatCell(context);

                logger.debug("Added player \"{}\" column {} header to row {}, column {}.", player.getLogin(), column.getColumnName(), rowNumber, columnNumber);
            }
        }
    }
}
