package ru.klavogonki.kgparser.excel;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.klavogonki.kgparser.excel.data.ExcelExportContextData;

@Log4j2
public class ExcelExportContext<D extends ExcelExportContextData> {

    public XSSFWorkbook workbook;

    public DataFormat dataFormat;

    public ExcelStylesMap stylesMap;

    public IndexedColorMap colorMap;

    public int rowNumber;

    public Cell cell;

    public D player;

    private ExcelExportContext() {
        // creation allowed only via #initContext()
    }

    public static ExcelExportContext initContext(XSSFWorkbook workbook, ExcelStylesMap.Config stylesMapConfig) {
        DataFormat dataFormat = workbook.createDataFormat();

        DefaultIndexedColorMap colorMap = new DefaultIndexedColorMap();

        ExcelExportContext context = new ExcelExportContext();
        context.workbook = workbook;
        context.dataFormat = dataFormat;
        context.colorMap = colorMap;
        context.stylesMap = ExcelStylesMap.init(context, stylesMapConfig);
        return context;
    }

    public boolean isEvenRowNumber() {
        return (rowNumber % 2) == 0;
    }

    public boolean isOddRowNumber() {
        return !isEvenRowNumber();
    }

    public void setStyle(ExcelStylesMap.Style style) {
        stylesMap.setStyle(cell, style);
    }

    public void setStyle(Cell cell, ExcelStylesMap.Style style) {
        stylesMap.setStyle(cell, style);
    }

    public void setIntegerStyle() {
        setStyle(isEvenRowNumber() ? ExcelStylesMap.Style.INTEGER_EVEN_ROW : ExcelStylesMap.Style.INTEGER_ODD_ROW);
    }

    public void setDoubleStyle() {
        setStyle(isEvenRowNumber() ? ExcelStylesMap.Style.DOUBLE_EVEN_ROW : ExcelStylesMap.Style.DOUBLE_ODD_ROW);
    }

    public void setTextAlignLeftStyle() {
        setStyle(isEvenRowNumber() ? ExcelStylesMap.Style.TEXT_ALIGN_LEFT_EVEN_ROW : ExcelStylesMap.Style.TEXT_ALIGN_LEFT_ODD_ROW);
    }

    public void setTextAlignRightStyle() {
        setStyle(isEvenRowNumber() ? ExcelStylesMap.Style.TEXT_ALIGN_RIGHT_EVEN_ROW : ExcelStylesMap.Style.TEXT_ALIGN_RIGHT_ODD_ROW);
    }

    public void setDateTimeStyle() {
        setStyle(isEvenRowNumber() ? ExcelStylesMap.Style.DATE_TIME_EVEN_ROW : ExcelStylesMap.Style.DATE_TIME_ODD_ROW);
    }

    public void setLinkIntegerStyle() {
        setStyle(isEvenRowNumber() ? ExcelStylesMap.Style.LINK_INTEGER_EVEN_ROW : ExcelStylesMap.Style.LINK_INTEGER_ODD_ROW);
    }

    public void setRankTextColor() {
        if (isEvenRowNumber()) {
            stylesMap.setEvenRowRankStyle(cell, player.getRank());
        }
        else {
            stylesMap.setOddRowRankStyle(cell, player.getRank());
        }
    }

    public void setIntegerHyperlink(String url, Integer text) { // Integer since we use playerId
        ExcelExporter.validateHyperlink(url);

        setLinkIntegerStyle(); // in this method we know text is Integer, therefore set it explicitly

        XSSFCreationHelper creationHelper = workbook.getCreationHelper();
        Hyperlink link = creationHelper.createHyperlink(HyperlinkType.URL);
        link.setAddress(url);

        cell.setCellValue(text);
        cell.setHyperlink(link);
    }
}
