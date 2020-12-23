package ru.klavogonki.kgparser.excel;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * See http://poi.apache.org/components/spreadsheet/quick-guide.html#CreateDateCells
 * and http://poi.apache.org/components/spreadsheet/quick-guide.html#CellTypes
 * for examples with different cell types.
 */
@Log4j2
public class ExcelExporter {

    public static void main(String[] args) {
        // from https://www.baeldung.com/java-microsoft-excel#2-writing-to-excel
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Persons");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 8000); // will be displayed as #### in case of not enough width

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
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
