package xml_parser;

import org.dom4j.Document;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

/**
 * Created by JLyc on 3/20/2016.
 */
public class ConfigParser {
    private final String earConfigName;
    TreeMap<String, String> globalWars = new TreeMap<>();

    LinkedHashMap<String, LinkedHashMap<String, String>> parWars = new LinkedHashMap<>();
    public ConfigParser(String earCofigName) {
        this.earConfigName = earCofigName;
        this.parseXMLConfig();

    }

    private void parseXMLConfig() {
            Document document = loadDocument(earConfigName);
            GlobalVariables gv = new GlobalVariables(document);
            globalWars = gv.getGlobalWars();
            ParVariables pv = new ParVariables(document);
            parWars = pv.getParsWars();
    }

    public TreeMap<String, String> getGlobalWars() {
        return globalWars;
    }

    public LinkedHashMap<String, LinkedHashMap<String, String>> getParWars() {
        return parWars;
    }

    public Document loadDocument(String path) {
        InputStream ios = this.getClass().getClassLoader().getResourceAsStream(path);
        SAXReader reader = new SAXReader();
        try {
            return reader.read(ios);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
