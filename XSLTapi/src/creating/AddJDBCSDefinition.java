package creating;

import org.dom4j.*;
import xml_parser.ConfigParser;
import org.apache.poi.xssf.usermodel.*;

import java.nio.file.Path;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellStyle.BORDER_MEDIUM;

/**
 * Created by JLyc on 4/23/2016.
 */
public class AddJDBCSDefinition {
    private XSSFCellStyle borderStyle;
    private static int rowCount = 0;

    private static final String PREFIX_NS = "ns";
    private static final String NAME_SPACE_URL = "http://www.tibco.com/xmlns/ApplicationManagement";
    private static final String NS = "/" + PREFIX_NS + ":";
    private LinkedHashMap<String, LinkedHashMap<String, String>> parsWars = new LinkedHashMap<>();
    private Map<String, String> namespaceUris = new HashMap<>();

    private Document document;
    private String deliminiter = "-";

    public AddJDBCSDefinition(XSSFWorkbook releaseNotes, List<Path> jdbCs, ConfigParser cfg) {
        XSSFSheet bwArtifacts = releaseNotes.createSheet("DB connections");
        borderStyle = getBorderStyle(releaseNotes);

        for(Path path : jdbCs) {
            Document  document= cfg.loadDocument(path.toString());
            parseJDBCVariables(document);
        }

    }

    private void parseJDBCVariables(Document document) {
        XPath xpathSelector = DocumentHelper.createXPath("/BWSharedResource/");
        List list1 = xpathSelector.selectNodes(document);
        for (Iterator iter = list1.iterator(); iter.hasNext(); ) {
            Element child = (Element) iter.next();
            for (int i = 0, size = child.nodeCount(); i < size; i++) {
                Node node = child.node(i);
                if (node instanceof Element && ((Element) node).nodeCount() == 1) {
//                    parsWars.get(parName).put(node.getName(), node.getText());
                    System.out.println(node.getText()+" "+node.getStringValue());
                }
                else{
                    System.out.println("array");
                }
            }
        }
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
