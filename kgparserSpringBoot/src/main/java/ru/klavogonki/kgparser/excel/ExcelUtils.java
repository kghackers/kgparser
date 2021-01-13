package ru.klavogonki.kgparser.excel;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.klavogonki.kgparser.Rank;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

@Log4j2
public final class ExcelUtils {

    private ExcelUtils() {
    }

    public static byte[] getRgb(Rank rank) {
        return getRgb(Rank.getColor(rank));
    }

    public static byte[] getRgb(final String hexColor) {
        Color decoded = Color.decode(hexColor);
        return getRgb(decoded.getRed(), decoded.getGreen(), decoded.getBlue());
    }

    public static byte[] getRgb(final int red, final int green, final int blue) {
        return new byte[]{(byte) red, (byte) green, (byte) blue};
    }

    public static XSSFCellStyle createStyle(
        final XSSFWorkbook workbook,
        final IndexedColorMap colorMap,
        final String backgroundColorHex,
        final String borderColorHex
    ) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFColor headerColor = new XSSFColor(ExcelUtils.getRgb(backgroundColorHex), colorMap);
        style.setFillForegroundColor(headerColor); // yes, it is setFillForegroundColor, NOT setFillBackgroundColor!

        XSSFColor borderColor = new XSSFColor(ExcelUtils.getRgb(borderColorHex), colorMap);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(borderColor);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(borderColor);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(borderColor);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(borderColor);

        return style;
    }

    public static XSSFFont getLinkFont(XSSFWorkbook workbook) {
        XSSFFont linkFont = workbook.createFont();
        linkFont.setUnderline(Font.U_SINGLE);
        linkFont.setColor(IndexedColors.BLUE.index);
        return linkFont;
    }

    public static void setIntegerFormat(Cell cell, DataFormat dataFormat) {
        setIntegerFormat(cell.getCellStyle(), dataFormat);
    }

    public static void setIntegerFormat(final CellStyle style, final DataFormat dataFormat) {
        style.setDataFormat(dataFormat.getFormat("0")); // no decimals. Works, with rounding
    }

    public static void setDoubleFormat(final CellStyle style, final DataFormat dataFormat) {
//        style.setDataFormat(dataFormat.getFormat("0.00")); // Works, with rounding

        // Does not work. Comma as decimal separator, see https://stackoverflow.com/a/14881269/8534088
        style.setDataFormat(dataFormat.getFormat("#,##0.00")); // Works, with rounding. Decimal separator does not work.

        // todo: still can't set "," as separator, probably hack with JVM locale
        // see https://stackoverflow.com/questions/7819423/change-decimal-and-thousands-separators-in-excel-using-apache-poi
    }

    public static void setTextFormat(Cell cell, DataFormat dataFormat) {
        setTextFormat(cell.getCellStyle(), dataFormat);
    }

    public static void setTextFormat(final CellStyle style, final DataFormat dataFormat) {
        style.setDataFormat(dataFormat.getFormat("@")); // text format, i.e. treat number as text see https://stackoverflow.com/a/36021850/8534088
    }

    public static void setAlignRight(Cell cell) {
        setAlignRight(cell.getCellStyle());
    }

    public static void setAlignRight(final CellStyle style) {
        style.setAlignment(HorizontalAlignment.RIGHT);
    }

    public static void setAlignLeft(final CellStyle style) {
        style.setAlignment(HorizontalAlignment.LEFT);
    }

    public static void setAlignCenter(Cell cell) {
        setAlignCenter(cell.getCellStyle());
    }

    public static void setAlignCenter(final CellStyle style) {
        style.setAlignment(HorizontalAlignment.CENTER);
    }

    public static void writeToFile(final Workbook workbook, final String filePath) {
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            workbook.close();
            logger.debug("Successfully written an Excel workbook to file {}.", filePath);
        }
        catch (IOException e) {
            throw new RuntimeException(e); // todo: special dedicated RuntimeException
        }
    }
}
