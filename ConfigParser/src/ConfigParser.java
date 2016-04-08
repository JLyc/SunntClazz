import com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl;
import org.dom4j.Document;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

/**
 * Created by JLyc on 3/20/2016.
 */
public class ConfigParser {
    public static void main(String[] args) {
        ConfigParser cfgPharser =new ConfigParser();
        cfgPharser.parseXMLConfig();
    }

    private void parseXMLConfig() {
        InputStream ios = this.getClass().getClassLoader().getResourceAsStream("SAI.xml");
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(ios);

            Element root = document.getRootElement();

            Map<String, String> namespaceUris = new HashMap<String, String>();
            namespaceUris.put("ns", "http://www.tibco.com/xmlns/ApplicationManagement");

            XPath xpathSelector = DocumentHelper.createXPath("/ns:application/ns:NVPairs/*");
            xpathSelector.setNamespaceURIs(namespaceUris);

            List results = xpathSelector.selectNodes(document);

            parseGlobalVariables(results);
            for(Map.Entry entry : globalWars.entrySet()){
                System.out.println(entry.getKey()+" => "+ entry.getValue());
            }

//            treeWalk(root);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void treeWalk(Element element) {
        for ( int i = 0, size = element.nodeCount(); i < size; i++ ) {
            Node node = element.node(i);
            if ( node instanceof Element ) {
                treeWalk( (Element) node );
            }
            else {
                System.out.println(node);
            }
        }
    }

    TreeMap<String , String> globalWars = new TreeMap<>();

    private void parseGlobalVariables(List list) {
        String name = "";
        String value = "";
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Element child = (Element) iter.next();
            for ( int i = 0, size = child.nodeCount(); i < size; i++ ) {
                Node node = child.node(i);
                if ( node instanceof Element ) {
                    if(node.getName().equals("name")){
                        name = node.getText();
                    }
                    if(node.getName().equals("value"))
                    {
                        value = node.getText();
                        try {
                            return String.valueOf(ObfuscationEngine.decrypt(att.get("USER-PWD")));
                        } catch (AXSecurityException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
//                    System.out.println(node);
                }
            }
            globalWars.put(name, value);
        }
    }


    public void test(){
        try {
            File inputFile = new File("input.txt");
            SAXReader reader = new SAXReader();
            Document document = reader.read( inputFile );

            System.out.println("Root element :"
                    + document.getRootElement().getName());

            Element classElement = document.getRootElement();

            List<?> nodes = document.selectNodes("//class/student");
            System.out.println("----------------------------");
            for (Node node : (List<Node>)nodes) {
                System.out.println("\nCurrent Element :"
                        + node.getName());
                System.out.println("Student roll no : "
                        + node.valueOf("@rollno") );
                System.out.println("First Name : " + node.selectSingleNode("firstname").getText());
                System.out.println("Last Name : " + node.selectSingleNode("lastname").getText());
                System.out.println("First Name : " + node.selectSingleNode("nickname").getText());
                System.out.println("Marks : " + node.selectSingleNode("marks").getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
