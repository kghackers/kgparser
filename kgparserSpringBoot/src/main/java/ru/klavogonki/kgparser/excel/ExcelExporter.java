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
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.klavogonki.kgparser.Rank;
import ru.klavogonki.kgparser.http.UrlConstructor;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * See http://poi.apache.org/components/spreadsheet/quick-guide.html#CreateDateCells
 * and http://poi.apache.org/components/spreadsheet/quick-guide.html#CellTypes
 * for examples with different cell types.
 */
@Log4j2
public class ExcelExporter {

    public static void main(String[] args) {
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
        XSSFColor color = new XSSFColor(getRgb(255, 153, 204), colorMap);

        // check rank colors
        int rowNumber = 3;
        int column = 0;

        Map<Rank, XSSFFont> rankToFont = getRankToFontMap(workbook);

        for (Rank rank : Rank.values()) {
            XSSFFont font = rankToFont.get(rank);

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

        writeToFile(workbook, "c:/java/test-poi.xlsx");
    }

    private static Map<Rank, XSSFFont> getRankToFontMap(XSSFWorkbook workbook) {
        Map<Rank, XSSFColor> rankToColor = getRankToColorMap();

        Map<Rank, XSSFFont> rankToFont = new HashMap<>();

        for (Rank rank : Rank.values()) {
            XSSFColor rankColor = rankToColor.get(rank);

            XSSFFont font = workbook.createFont();
            font.setFontName("Arial");
//            font.setFontHeightInPoints((short) 16);
            font.setColor(rankColor);
//            font.setBold(true);

            rankToFont.put(rank, font);
        }

        return rankToFont;
    }

    private static Map<Rank, XSSFColor> getRankToColorMap() {
        DefaultIndexedColorMap colorMap = new DefaultIndexedColorMap();

        Map<Rank, XSSFColor> rankToColor = new HashMap<>();
        for (Rank rank : Rank.values()) {
            XSSFColor rankColor = new XSSFColor(getRgb(rank), colorMap);
            rankToColor.put(rank, rankColor);
        }

        return rankToColor;
    }

    private static byte[] getRgb(Rank rank) {
        Color decoded = Color.decode(Rank.getColor(rank));
        return getRgb(decoded.getRed(), decoded.getGreen(), decoded.getBlue());
    }

    private static byte[] getRgb(final int red, final int green, final int blue) {
        return new byte[]{(byte) red, (byte) green, (byte) blue};
    }

    private static void writeToFile(final Workbook workbook, final String filePath) {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            workbook.close();
            logger.debug("Successfully written an Excel workbook to file {}.", filePath);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
