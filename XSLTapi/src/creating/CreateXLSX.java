package creating;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sochaa on 21. 4. 2016.
 */
public class CreateXLSX {

    public static String getProjectName() {
        return projectName;
    }

    public static float getProjectVer() {
        return projectVer;
    }

    public static float getAuthor() {
        return author;
    }

    private static String projectName = "SAI";
    private static float projectVer = 0.1f;
    private static float author = 0.1f;

    public static XSSFCellStyle style;

    public static void main(String[] args) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Fontstyle");
        XSSFRow row = spreadsheet.createRow(2);
        //Create a new font and alter it.
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 30);
        font.setFontName("IMPACT");
        font.setItalic(true);
        font.setColor(HSSFColor.BRIGHT_GREEN.index);
        //Set font into style
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        // Create a cell with a value and set style to it.
        new AddSummary(workbook);
        new AddDeploymentArtefacts(workbook);
        XSSFCell cell = row.createCell(1);
        cell.setCellValue("Font Style");
        cell.setCellStyle(style);
        FileOutputStream out = new FileOutputStream(
                new File("fontstyle.xlsx"));
        workbook.write(out);
        out.close();
        System.out.println(
                "fontstyle.xlsx written successfully");


    }
}
