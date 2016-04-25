package xml_parser;

import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;
import org.dom4j.*;

import java.util.*;

/**
 * Created by JLyc on 4/10/2016.
 */
public class ParVariables {
    private static final String PREFIX_NS = "ns";
    private static final String NAME_SPACE_URL = "http://www.tibco.com/xmlns/ApplicationManagement";
    private static final String NS = "/" + PREFIX_NS + ":";
    private LinkedHashMap<String, LinkedHashMap<String, String>> parsWars = new LinkedHashMap<>();
    private Map<String, String> namespaceUris = new HashMap<>();

    private Document document;
    private String deliminiter = "-";

    public ParVariables(Document document) {
        this.document = document;
        namespaceUris.put(PREFIX_NS, NAME_SPACE_URL);

        XPath xpathSelector = DocumentHelper.createXPath(NS + "application" + NS + "services/.");
        xpathSelector.setNamespaceURIs(namespaceUris);

        parseParVariables(xpathSelector.selectNodes(document));
    }

    private void parseParVariables(List list) {
        parsWars = new LinkedHashMap<>();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Element child = (Element) iter.next();
            for (int i = 0, size = child.nodeCount(); i < size; i++) {
                Node node = child.node(i);
                if (node instanceof Element) {
                    parsWars.put(((Element) node).attribute("name").getValue(), new LinkedHashMap<String, String>());
                }
            }
        }

        for (Map.Entry<String, LinkedHashMap<String, String>> parName : parsWars.entrySet()) {
            String nameOfPar = parName.getKey();
            parsWars.get(nameOfPar).put("%basic", deliminiter);
            mainVariables(nameOfPar);
            parsWars.get(nameOfPar).put("%bindings", deliminiter);
            bindingsVariables(nameOfPar);
            parsWars.get(nameOfPar).put("%runtime", deliminiter);
            nvpairVariables(nameOfPar,"Runtime Variables");
            parsWars.get(nameOfPar).put("%SDK_properies", deliminiter);
            nvpairVariables(nameOfPar,"Adapter SDK Properties");
            parsWars.get(nameOfPar).put("%bwProcesses", deliminiter);
            bwProcessesVariables(nameOfPar);
        }
    }

    private void bwProcessesVariables(String parName) {
        String rootPath = NS + "application" + NS + "services" + NS + "bw[@name='" + parName + "']" + NS + "bwprocesses";
        XPath xpathSelector = DocumentHelper.createXPath(rootPath);
        xpathSelector.setNamespaceURIs(namespaceUris);
        List list = xpathSelector.selectNodes(document);
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Element child = (Element) iter.next();
            for (int i = 0, size = child.nodeCount(); i < size; i++) {
                Node node = child.node(i);
                if (node instanceof Element) {
                    parsWars.get(parName).put(node.getName(), ((Element) node).attribute("name").getValue());
                    for (int j = 0; j < ((Element) node).nodeCount(); j++) {
                        Node subNode = ((Element) node).node(j);
                        if (subNode instanceof Element) {
                            parsWars.get(parName).put(subNode.getName(), subNode.getText());
                        }
                    }
                }
            }
        }
    }

    private void nvpairVariables(String parName, String nvpairName) {
        String rootPath = NS + "application" + NS + "services" + NS + "bw[@name='" + parName + "']" + NS + "NVPairs[@name='"+nvpairName+"']/*";
        XPath xpathSelector = DocumentHelper.createXPath(rootPath);
        xpathSelector.setNamespaceURIs(namespaceUris);
        List list = xpathSelector.selectNodes(document);
        for (Map.Entry<String, String> pair : parseNVPairs(list).entrySet()) {
            parsWars.get(parName).put(pair.getKey(), pair.getValue());
        }
    }

    private void bindingsVariables(String parName) {
        String rootPath = NS + "application" + NS + "services" + NS + "bw[@name='" + parName + "']" + NS + "bindings" + NS + "binding[@name='" + parName.subSequence(0, parName.length() - 4) + "']";
        XPath xpathSelector = DocumentHelper.createXPath(rootPath + NS + "machine");
        xpathSelector.setNamespaceURIs(namespaceUris);
        Element machine = (Element) xpathSelector.selectNodes(document).get(0);
        parsWars.get(parName).put(machine.getName(), machine.getText());
        xpathSelector = DocumentHelper.createXPath(rootPath + NS + "setting");
        xpathSelector.setNamespaceURIs(namespaceUris);
        List list = xpathSelector.selectNodes(document);
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Element child = (Element) iter.next();
            for (int i = 0, size = child.nodeCount(); i < size; i++) {
                Node node = child.node(i);
                if (node instanceof Element) {
                    if (((Element) node).nodeCount() == 1) {
                        parsWars.get(parName).put(node.getName(), node.getText());
                    } else {
                        for (int j = 0; j < ((Element) node).nodeCount(); j++) {
                            Node subNode = child.node(j);
                            if (subNode instanceof Element) {
                                parsWars.get(parName).put(node.getName() + "/" + subNode.getName(), subNode.getText());
                            }
                        }
                    }
                }
            }
        }
    }

    private void mainVariables(String parName) {
        XPath xpathSelector = DocumentHelper.createXPath(NS + "application" + NS + "services" + NS + "bw[@name='" + parName + "']");
        xpathSelector.setNamespaceURIs(namespaceUris);
        List list1 = xpathSelector.selectNodes(document);
        for (Iterator iter = list1.iterator(); iter.hasNext(); ) {
            Element child = (Element) iter.next();
            for (int i = 0, size = child.nodeCount(); i < size; i++) {
                Node node = child.node(i);
                if (node instanceof Element && ((Element) node).nodeCount() == 1) {
                    parsWars.get(parName).put(node.getName(), node.getText());
                }
            }
        }
    }

    private Map<String, String> parseNVPairs(List list) {
        Map<String, String> nvpairs = new TreeMap<>();
        String name = "";
        String value = "";
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Element child = (Element) iter.next();
            for (int i = 0, size = child.nodeCount(); i < size; i++) {
                Node node = child.node(i);
                if (node instanceof Element) {
                    if (node.getName().equals("name")) {
                        name = node.getText();
                    }
                    if (node.getName().equals("value")) {
                        if ((value = node.getText()).startsWith("#!"))
                            try {
                                value = String.valueOf(ObfuscationEngine.decrypt(value));
                            } catch (AXSecurityException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }
            nvpairs.put(name, value);
        }
        return nvpairs;
    }

    public LinkedHashMap<String, LinkedHashMap<String, String>> getParsWars() {
        return parsWars;
    }
}
