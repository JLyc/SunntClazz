package creating;

import org.apache.poi.xssf.usermodel.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM;

/**
 * Created by JLyc on 4/23/2016.
 */
public class AddBWDefinitionAll {
    private XSSFCellStyle borderStyle;
    private static int rowCount = 0;

    public AddBWDefinitionAll(XSSFWorkbook releaseNotes, String name, List<Path> files) {
        XSSFSheet bwArtifacts = releaseNotes.createSheet("BWALL " + name);
        borderStyle = getBorderStyle(releaseNotes);
        XSSFRow legend = bwArtifacts.createRow(rowCount);
        legend.createCell(0).setCellValue("BW project name: ");
        legend.createCell(1).setCellValue(name);
        XSSFRow header = bwArtifacts.createRow(rowCount += 2);
        header.setRowStyle(borderStyle);
        createBorderCell(header, 0, "Path.");
        createBorderCell(header, 1, "File");
        createBorderCell(header, 2, "Status");

        Path prewiousPah = Paths.get("");
        int subpathIndex = 0;
        for (Path path : files) {
            XSSFRow row = bwArtifacts.createRow(++rowCount);
            if (subpathIndex == 0) {
                for (int i = 0; true; i++) {
                    if (path.getName(i).toString().equals(name)) {
                        subpathIndex = ++i;
                        break;
                    }
                }
            }

            if(prewiousPah.compareTo(path.getParent())!=0){
                row.createCell(0).setCellValue(String.valueOf(path.subpath(subpathIndex, path.getNameCount()-1)));
            }
            row.createCell(1).setCellValue(String.valueOf(path.getFileName()));
            row.createCell(2).setCellValue("new");
            prewiousPah = path;
        }
        bwArtifacts.autoSizeColumn(0);
        bwArtifacts.autoSizeColumn(1);

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
}
