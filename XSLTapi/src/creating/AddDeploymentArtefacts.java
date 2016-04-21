package creating;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

/**
 * Created by sochaa on 21. 4. 2016.
 */
public class AddDeploymentArtefacts {

    public AddDeploymentArtefacts(XSSFWorkbook releaseNotes) {
        XSSFSheet deploymentArtifacts = releaseNotes.createSheet("Deployment artifacts");
        XSSFRow row = deploymentArtifacts.createRow(2);
        //Create a new font and alter it.
        XSSFFont font = releaseNotes.createFont();
//        font.setFontHeightInPoints((short) 30);
//        font.setFontName("IMPACT");
        font.setItalic(true);
        font.setColor(HSSFColor.GREY_40_PERCENT.index);
        //Set font into style
        XSSFCellStyle style = releaseNotes.createCellStyle();
        style.setFont(font);

        XSSFRow header = deploymentArtifacts.createRow(0);
        header.createCell(0).setCellValue("List of artefacts to be provided for cutover");
        XSSFRow ears = deploymentArtifacts.createRow(1);
        ears.createCell(0).setCellValue("EARs");
        XSSFCell cell = ears.createCell(1);
        cell.setCellValue("List of TIBCO BW EARs. See sheets EAR* for configuration values");
        cell.setCellStyle(style);
//        ears.createCell(1).setCellValue();
//        ears.createCell(1).setCellStyle(hintStyle);
//        ears.createCell(1).setCellStyle(CreateXLSX.style);

        // Create a cell with a value and set style to it.
//        XSSFCell cell = row.createCell(1);
//        cell.setCellValue("Font Style");
//        cell.setCellStyle(style);
//        Row sql = deploymentArtifacts.createRow(2);
//        Row certificates = deploymentArtifacts.createRow(4);
//        Row bc = deploymentArtifacts.createRow(4);
//        Row ems = deploymentArtifacts.createRow(4);
//        Row other = deploymentArtifacts.createRow(4);
//        deploymentArtifacts.autoSizeColumn(0);

    }
}
