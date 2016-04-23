import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by sochaa on 15. 4. 2016.
 */
public class LoadXSLT {

    public static void main(String args[]) throws Exception {
        LoadXSLT excel = new LoadXSLT();
        excel.readExcel();
    }

    private void readExcel() throws IOException, URISyntaxException {
        URL inputStream = this.getClass().getResource("ReleaseNotes-002.xlsx");
        File file = new File(inputStream.toURI());

        FileInputStream fIP = new FileInputStream(file);

        XSSFRow row;
        //Get the workbook instance for XLSX file
        XSSFWorkbook workbook = new XSSFWorkbook(fIP);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            XSSFSheet spreadsheet = workbook.getSheetAt(i);
            System.out.println(spreadsheet.getSheetName());
            Iterator<Row> rowIterator = spreadsheet.iterator();
            while (rowIterator.hasNext()) {
                row = (XSSFRow) rowIterator.next();
                Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(
                                    cell.getNumericCellValue() + " \t\t ");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(
                                    cell.getStringCellValue() + " \t\t ");
                            break;
                    }
                }
            }


            if (file.isFile() && file.exists()) {
                System.out.println(
                        "openworkbook.xlsx file open successfully.");
            } else {
                System.out.println(
                        "Error to open openworkbook.xlsx file.");
            }
        }


    }
}
