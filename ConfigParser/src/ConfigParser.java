import com.sun.org.apache.xerces.internal.jaxp.SAXParserImpl;
import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;
import org.dom4j.Document;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

/**
 * Created by JLyc on 3/20/2016.
 */
public class ConfigParser {
    TreeMap<String, String> globalWars = new TreeMap<>();

    public static void main(String[] args) {
        ConfigParser cfgPharser = new ConfigParser();
        cfgPharser.parseXMLConfig();
    }

    private void parseXMLConfig() {
        InputStream ios = this.getClass().getClassLoader().getResourceAsStream("SAI.xml");
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(ios);

            Element root = document.getRootElement();

//            GlobalVariables gv = new GlobalVariables(document);
//            globalWars = gv.getGlobalWars();

            ParVariables pv = new ParVariables(document);
            globalWars = pv.getParslWars();

            for (Map.Entry entry : globalWars.entrySet()) {
                System.out.println(entry.getKey() + " => " + entry.getValue());
            }
        } catch (DocumentException e) {
            e.printStackTrace();

        }
    }
}
