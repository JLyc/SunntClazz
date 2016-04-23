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
//            System.out.println(spreadsheet.getSheetName());
            if (!spreadsheet.getSheetName().equals("EAR SAI")) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            Iterator<Row> rowIterator = spreadsheet.iterator();
            while (rowIterator.hasNext()) {
                row = (XSSFRow) rowIterator.next();
                System.out.println(row.getRowNum()+" "+row.getRowStyle());
                if(row.getRowStyle()!=null){
                System.out.println(row.getRowNum()+" "+row.getRowStyle().getBorderBottom());}
                Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
                if (row.getRowNum() < 4) {
                    if (row.getRowNum() == 3)
                        analyzeTable(row);
                    continue;
                }
                while (cellIterator.hasNext()) {
                    org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
                    if (cell.getColumnIndex() == NAME_COLUMN) {
                        if(!(sb.lastIndexOf("\n")== sb.length()-1))
                            sb.append("\n");
                    }
                    if (cell.getColumnIndex() < VALUE_COLUMN) {
                        sb.append(readCells(cell));
                        continue;
                    }
                    if (cell.getColumnIndex() == VALUE_COLUMN) {
                        sb.append(" -> " + readCells(cell));
                        if (!cellIterator.hasNext()) {
                            sb.append("\n");
                        }
                    }
                    if (cell.getColumnIndex() == STATUS_COLUMN) {
                        sb.append(" => " + readCells(cell));
                        if (!cellIterator.hasNext()) {
                            sb.append("\n");
                        }
                    }
//                    if (!cellIterator.hasNext()) {
//                        sb.append("\n");
//                    }

                }

//                while (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    sb.append(readCells(cell));
//                    if (cellIterator.hasNext()) {
//
//                        if (cell.getColumnIndex() == 5 && cell.getCellType() != 3) {
//                            sb.append("\t->\t");
//                            sb.append(readCells(cell));
//                            if (!cellIterator.hasNext()) {
//                                sb.append("\t=>\tstate\n");
//                                continue;
//                            }
//                            cell = cellIterator.next();
//                            if (cell.getCellType() != 3) {
//                                sb.append("\t=>\t");
//                                sb.append(readCells(cell));
//                                sb.append("\n");
//                            } else {
//                                sb.append("\t=>\tstate\n");
//                            }
//                            continue;
//                        }
//                        sb.append(readCells(cell));
//                    } else {
//                        sb.append("\\");
//                    }
//                }
            }
//                System.out.println();
            System.out.println(sb);
        }


        if (file.isFile() && file.exists()) {
            System.out.println(
                    "openworkbook.xlsx file open successfully.");
        } else {
            System.out.println(
                    "Error to open openworkbook.xlsx file.");
        }
    }


    private static int NAME_COLUMN;
    private static int VALUE_COLUMN;
    private static int STATUS_COLUMN;

    private void analyzeTable(XSSFRow row) {
        Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
//            System.out.println(cell.getStringCellValue());
            switch (readCells(cell)) {
                case "Name ":
                    NAME_COLUMN = cell.getColumnIndex();
                    break;
                case "Value ":
                    VALUE_COLUMN = cell.getColumnIndex();
                    break;
                case "Modification Status ":
                    STATUS_COLUMN = cell.getColumnIndex();
                    break;
            }
        }
    }

    private String readCells(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return (cell.getBooleanCellValue() + " ");
            case Cell.CELL_TYPE_NUMERIC:
                return (cell.getNumericCellValue() + " ");
            case Cell.CELL_TYPE_STRING:
                return (cell.getStringCellValue() + " ");
            default:
                return (" ");
        }
    }
}
