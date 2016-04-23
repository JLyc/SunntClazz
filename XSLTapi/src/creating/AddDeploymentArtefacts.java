package creating;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sochaa on 21. 4. 2016.
 */
public class AddDeploymentArtefacts {

    private static int rowCount = 0;
    private XSSFCellStyle hintStyle;

    private Set<String> earsSet = new HashSet<>();
    private Set<String> sqlSet= new HashSet<>();;
    private Set<String> certificatesSet= new HashSet<>();;
    private Set<String> bcSet= new HashSet<>();;
    private Set<String> emsSet= new HashSet<>();;
    private Set<String> otherSet= new HashSet<>();;


    public AddDeploymentArtefacts(XSSFWorkbook releaseNotes) {
        XSSFSheet deploymentArtifacts = releaseNotes.createSheet("Deployment artifacts");
        hintStyle = getHintStyle(releaseNotes);

        XSSFRow header = deploymentArtifacts.createRow(rowCount);
        header.createCell(0).setCellValue("List of artefacts to be provided for cutover");

        addArtifactsTables(deploymentArtifacts,"EARs", "List of TIBCO BW EARs. See sheets EAR* for configuration values",earsSet);
        addArtifactsTables(deploymentArtifacts,"SQL", "List of SQL scripts. See DB Deployment sheet for more info ",sqlSet);
        addArtifactsTables(deploymentArtifacts,"Certificates", "List of SSL certificates. See Infrastructure sheet for configuration values",certificatesSet);
        addArtifactsTables(deploymentArtifacts,"BC", "List of Business Connect artefacts ",bcSet);
        addArtifactsTables(deploymentArtifacts,"Ems", "List of EMS scripts. See EMS sheet for more info ",emsSet);
        addArtifactsTables(deploymentArtifacts,"Other", "Like list of xml configuration files that are not part of EAR ",otherSet);

        deploymentArtifacts.autoSizeColumn(0);

    }

    private void addArtifactsTables(XSSFSheet deploymentArtifacts, String name, String hint, Set<String > list) {
        XSSFRow row = deploymentArtifacts.createRow(rowCount+=2);
        row.createCell(0).setCellValue(name);
        crateHintCell(row, 1, hint);

        for (String earName: list){
            XSSFRow rows = deploymentArtifacts.createRow(rowCount++);
            rows.createCell(0).setCellValue(earName);
        }
    }

    private void crateHintCell(XSSFRow ears, int position, String text) {
        XSSFCell cell = ears.createCell(position);
        cell.setCellValue(text);
        cell.setCellStyle(hintStyle);
    }

    private XSSFCellStyle getHintStyle(XSSFWorkbook releaseNotes) {
        XSSFFont font = releaseNotes.createFont();
        font.setItalic(true);
        font.setColor(HSSFColor.GREY_40_PERCENT.index);
        XSSFCellStyle style = releaseNotes.createCellStyle();
        style.setFont(font);
        return style;
    }
}
