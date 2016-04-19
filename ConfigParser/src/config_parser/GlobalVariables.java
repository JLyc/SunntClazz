package config_parser;

import com.tibco.security.AXSecurityException;
import com.tibco.security.ObfuscationEngine;
import org.dom4j.*;

import java.util.*;

/**
 * Created by JLyc on 4/10/2016.
 */
public class GlobalVariables {
    private static final String PREFIX_NS = "ns";
    private static final String NAME_SPACE_URL = "http://www.tibco.com/xmlns/ApplicationManagement";
    private TreeMap<String, String> globalWars = new TreeMap<>();
    private Map<String, String> namespaceUris = new HashMap<>();

    public GlobalVariables(Document document) {

        namespaceUris.put(PREFIX_NS, NAME_SPACE_URL);

        XPath xpathSelector = DocumentHelper.createXPath("/"+PREFIX_NS+":application/"+PREFIX_NS+":NVPairs/*");
        xpathSelector.setNamespaceURIs(namespaceUris);

        parseGlobalVariables(xpathSelector.selectNodes(document));
    }

    private Map<String, String> parseGlobalVariables(List list) {
        globalWars = new TreeMap<>();
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
            globalWars.put(name, value);
        }
        return globalWars;
    }

    public TreeMap<String, String> getGlobalWars() {
        return globalWars;
    }
}
