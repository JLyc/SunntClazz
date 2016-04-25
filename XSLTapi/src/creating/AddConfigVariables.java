package creating;

import xml_parser.ConfigParser;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM;

/**
 * Created by sochaa on 22. 4. 2016.
 */
public class AddConfigVariables {

    private XSSFCellStyle borderStyle;
    private XSSFCellStyle aligmentStyle;
    private static int rowCount = 0;

    public AddConfigVariables(XSSFWorkbook releaseNotes, String name,ConfigParser cfg) {
        XSSFSheet earArtifacts = releaseNotes.createSheet("EAR " + name);
        borderStyle = getBorderStyle(releaseNotes);
        aligmentStyle = getAligmentStyle(releaseNotes);
        XSSFRow legend = earArtifacts.createRow(rowCount);
        legend.createCell(0).setCellValue("Ear name: ");
        legend.createCell(1).setCellValue(name);
        XSSFRow header = earArtifacts.createRow(rowCount += 2);
        header.setRowStyle(borderStyle);
        createBorderCell(header, 0, "Global var.");
        createBorderCell(header, 1, "Name");
        createBorderCell(header, 2, "Value");
        createBorderCell(header, 3, "Status");

        for (Map.Entry<String, String> entry : cfg.getGlobalWars().entrySet()) {
            XSSFRow row = earArtifacts.createRow(++rowCount);
            row.setRowStyle(aligmentStyle);
            row.createCell(1).setCellValue(entry.getKey());
            XSSFCell cell = row.createCell(2);
            cell.setCellValue(entry.getValue());
            cell.setCellStyle(getWrapStyle(releaseNotes));
            row.createCell(3).setCellValue("New");
        }

        for (Map.Entry<String, LinkedHashMap<String, String>> entry : cfg.getParWars().entrySet()) {
            XSSFRow row = earArtifacts.createRow(rowCount+=3);
            row.setRowStyle(borderStyle);
            createBorderCell(row, 0, entry.getKey());
            createBorderCell(row, 1, "Name");
            createBorderCell(row, 2, "Value");
            createBorderCell(row, 3, "Status");
            for (Map.Entry<String, String> entrySet : entry.getValue().entrySet()) {
                XSSFRow subRow = earArtifacts.createRow(++rowCount);
                subRow.setRowStyle(aligmentStyle);
                if (entrySet.getKey().startsWith("%")) {
                    subRow.createCell(0).setCellValue(entrySet.getKey().replace("%", ""));
                    continue;
                }
                XSSFCell cellName = subRow.createCell(1);
                cellName.setCellValue(entrySet.getKey());
                XSSFCell cellValue = subRow.createCell(2);
                cellValue.setCellValue(entrySet.getValue());
                subRow.createCell(3).setCellValue("New");
            }
        }


        earArtifacts.autoSizeColumn(0);
        earArtifacts.autoSizeColumn(1);
        earArtifacts.setColumnWidth(2, 20000);

    }

    private void createBorderCell(XSSFRow ears, int position, String text) {
        XSSFCell cell = ears.createCell(position);
        cell.setCellValue(text);
        cell.setCellStyle(borderStyle);
    }

    private XSSFCellStyle getBorderStyle(XSSFWorkbook releaseNotes) {
        XSSFFont font = releaseNotes.createFont();
        font.setBold(true);
        XSSFCellStyle style = releaseNotes.createCellStyle();
        style.setBorderBottom(BORDER_MEDIUM);
        style.setFont(font);
        return style;
    }

    private XSSFCellStyle getWrapStyle(XSSFWorkbook releaseNotes) {
        XSSFCellStyle style = releaseNotes.createCellStyle();
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        return style;
    }

    private XSSFCellStyle getAligmentStyle(XSSFWorkbook releaseNotes) {
        XSSFCellStyle style = releaseNotes.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setWrapText(true);
        return style;
    }
}
