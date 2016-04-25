package creating;

import xml_parser.ConfigParser;
import ear_extractor.ExtractEarFiles;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        Path fs = Paths.get("C:\\Java\\Projects\\SunntClazz\\XSLTapi\\resources\\test_group");

        XSSFWorkbook workbook = new XSSFWorkbook();
        new AddSummary(workbook);
        new AddDeploymentArtefacts(workbook);
        try(ExtractEarFiles extractEarFiles = new ExtractEarFiles(fs))
        {
            for(Path path : extractEarFiles.getZipFiles()) {
                String earProcesing = FilenameUtils.removeExtension(String.valueOf(path.getFileName()));
                ConfigParser cfg = new ConfigParser(earProcesing+".xml");
                new AddConfigVariables(workbook,earProcesing,cfg);
                new AddBWDefinition(workbook, earProcesing, extractEarFiles.getFiles(path));
                new AddBWDefinitionAll(workbook, earProcesing, extractEarFiles.getFiles(path));
//                new AddJDBCSDefinition(workbook, extractEarFiles.getJDBCs(path), cfg);

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        FileOutputStream out = new FileOutputStream(
                new File("fontstyle.xlsx"));
        workbook.write(out);
        out.close();
        System.out.println(
                "fontstyle.xlsx written successfully");


    }
}
