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


            String earName = root.attribute(new QName("name")).getValue();

            System.out.println("Application name "+root.getName());
            for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                System.out.println(element.getName());
            }


        } catch (DocumentException e) {
            e.printStackTrace();
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
