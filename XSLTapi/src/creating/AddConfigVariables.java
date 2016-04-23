package creating;

import org.apache.poi.xssf.usermodel.*;
import config_parser.*;

import java.util.Map;
import java.util.TreeMap;

import static org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM;

/**
 * Created by sochaa on 22. 4. 2016.
 */
public class AddConfigVariables {

    private XSSFCellStyle borderStyle;
    private static int rowCount = 0;

    public AddConfigVariables(XSSFWorkbook releaseNotes, String name) {
        XSSFSheet earArtifacts = releaseNotes.createSheet("EAR " + name);
        borderStyle = getBorderStyle(releaseNotes);
        XSSFRow legend = earArtifacts.createRow(rowCount);
        legend.createCell(0).setCellValue("Ear name: ");
        legend.createCell(1).setCellValue(name);
        XSSFRow header = earArtifacts.createRow(rowCount += 2);
        header.setRowStyle(borderStyle);
        createBorderCell(header, 0, "Global var.");
        createBorderCell(header, 1, "Name");
        createBorderCell(header, 2, "Value");
        createBorderCell(header, 3, "Status");


        ConfigParser cfg = new ConfigParser();
        for (Map.Entry<String, String> entry : cfg.getGlobalWars().entrySet()) {
            XSSFRow row = earArtifacts.createRow(++rowCount);
            row.createCell(1).setCellValue(entry.getKey());
            XSSFCell cell = row.createCell(2);
            cell.setCellValue(entry.getValue());
            cell.setCellStyle(getWrapStyle(releaseNotes));
            row.createCell(3).setCellValue("New");
        }

        for (Map.Entry<String, TreeMap<String, String>> entry : cfg.getParWars().entrySet()) {
            XSSFRow row = earArtifacts.createRow(rowCount+=3);
            row.setRowStyle(borderStyle);
            createBorderCell(row, 0, entry.getKey());
            createBorderCell(row, 1, "Name");
            createBorderCell(row, 2, "Value");
            createBorderCell(row, 3, "Status");
            for (TreeMap.Entry<String, String> entrySet : entry.getValue().entrySet()) {
                XSSFRow subRow = earArtifacts.createRow(++rowCount);
                subRow.createCell(1).setCellValue(entrySet.getKey());
                XSSFCell cell = subRow.createCell(2);
                cell.setCellValue(entrySet.getValue());
                cell.setCellStyle(getWrapStyle(releaseNotes));
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
        return style;
    }
}
