package creating;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by sochaa on 21. 4. 2016.
 */
public class AddSummary {

    public AddSummary(Workbook releaseNotes) {
        Sheet summarySheet = releaseNotes.createSheet("Summary");
        Row releaseDate = summarySheet.createRow(0);
        Row deliveredProject = summarySheet.createRow(1);
        Row documentVersion = summarySheet.createRow(2);
        Row author = summarySheet.createRow(4);

        releaseDate.createCell(0).setCellValue("Release Date:");
        releaseDate.createCell(1).setCellValue("");

        deliveredProject.createCell(0).setCellValue("Delivered Project:");
        deliveredProject.createCell(1).setCellValue(CreateXLSX.getProjectName());

        documentVersion.createCell(0).setCellValue("Document Version:");
        documentVersion.createCell(1).setCellValue(Float.toString(CreateXLSX.getProjectVer()));

        author.createCell(0).setCellValue("Author:");
        author.createCell(1).setCellValue(System.getProperty("user.name"));
        summarySheet.autoSizeColumn(0);
    }
}
