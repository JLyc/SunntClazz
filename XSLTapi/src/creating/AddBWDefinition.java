package creating;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM;

/**
 * Created by JLyc on 4/23/2016.
 */
public class AddBWDefinition {
    private XSSFCellStyle borderStyle;
    private static int rowCount = 0;
    private XSSFCellStyle boltStyle;

    public AddBWDefinition(XSSFWorkbook releaseNotes, String name, List<Path> files) {
        XSSFSheet bwArtifacts = releaseNotes.createSheet("BW " + name);
        borderStyle = getBorderStyle(releaseNotes);
        boltStyle = getBoldStyle(releaseNotes);

        XSSFRow legend = bwArtifacts.createRow(rowCount);
        legend.createCell(0).setCellValue("BW project name: ");
        legend.createCell(1).setCellValue(name);
        XSSFRow header = bwArtifacts.createRow(rowCount += 2);
        header.setRowStyle(borderStyle);
        createBorderCell(header, 0, "Path.");
        createBorderCell(header, 8, "File");
        createBorderCell(header, 9, "Status");

        Path prewiousPah = Paths.get("");
        int subpathIndex = 0;
        for (Path path : files) {
//            XSSFRow row;
            XSSFRow row = bwArtifacts.createRow(++rowCount);
            if (subpathIndex == 0) {
                for (int i = 0; true; i++) {
                    if (path.getName(i).toString().equals(name)) {
                        subpathIndex = ++i;
                        break;
                    }
                }
            }

            int index = subpathIndex;
            if (prewiousPah.getNameCount() > index) {
                while (path.getName(index).compareTo(prewiousPah.getName(index)) == 0) {
                    index++;
                    if (prewiousPah.getNameCount() < index)
                        break;
                }
            }

            boolean skippRow = true;
            for (int i = index; i < path.getNameCount() - 1; i++) {
                if(i!=index){
                    row = bwArtifacts.createRow(++rowCount);}
                createBoldCell(row, (i - subpathIndex), String.valueOf(path.getName(i)));
//                row.createCell(i - subpathIndex).setCellValue(String.valueOf(path.getName(i)));
                skippRow = false;
            }
            if(!skippRow){
            row = bwArtifacts.createRow(++rowCount);}
//            createBoldCell(row,8,String.valueOf(path.getFileName()));
            row.createCell(8).setCellValue(String.valueOf(path.getFileName()));
            row.createCell(9).setCellValue("new");
            prewiousPah = path;
        }
        bwArtifacts.autoSizeColumn(7);
        bwArtifacts.autoSizeColumn(8);
        bwArtifacts.setColumnWidth(0, 1000);
        bwArtifacts.setColumnWidth(1, 1000);
        bwArtifacts.setColumnWidth(2, 1000);
        bwArtifacts.setColumnWidth(3, 1000);
        bwArtifacts.setColumnWidth(4, 1000);
        bwArtifacts.setColumnWidth(5, 1000);
        bwArtifacts.setColumnWidth(6, 1000);
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

    private void createBoldCell(XSSFRow ears, int position, String text) {
        XSSFCell cell = ears.createCell(position);
        cell.setCellValue(text);
        cell.setCellStyle(boltStyle);
    }

    private XSSFCellStyle getBoldStyle(XSSFWorkbook releaseNotes) {
        XSSFFont font = releaseNotes.createFont();
        font.setBold(true);
        XSSFCellStyle style = releaseNotes.createCellStyle();
        style.setFont(font);
        return style;
    }
}
