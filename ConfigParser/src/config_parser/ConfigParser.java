package config_parser;

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

    TreeMap<String, TreeMap<String, String>> parWars = new TreeMap<>();
    public ConfigParser() {
        this.parseXMLConfig();
    }

    public static void main(String[] args) {
        ConfigParser cfgPharser = new ConfigParser();
        cfgPharser.parseXMLConfig();
    }

    private void parseXMLConfig() {
        InputStream ios = this.getClass().getClassLoader().getResourceAsStream("SAI.xml");
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(ios);

            GlobalVariables gv = new GlobalVariables(document);
            globalWars = gv.getGlobalWars();

            for (Map.Entry entry : globalWars.entrySet()) {
                System.out.println(entry.getKey() + " => " + entry.getValue());
            }

            ParVariables pv = new ParVariables(document);
            parWars = pv.getParsWars();
            for (Map.Entry<String, TreeMap<String, String>> pars: parWars.entrySet()){
                System.out.println("****************************"+pars.getKey()+"****************************");
                for (Map.Entry<String, String> vars : pars.getValue().entrySet()) {
                    System.out.println(vars.getKey() +" -> "+vars.getValue());
                }
            }


        } catch (DocumentException e) {
            e.printStackTrace();

        }
    }

    public TreeMap<String, String> getGlobalWars() {
        return globalWars;
    }

    public TreeMap<String, TreeMap<String, String>> getParWars() {
        return parWars;
    }
}
