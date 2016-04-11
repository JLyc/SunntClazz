import org.dom4j.*;

import java.util.*;

/**
 * Created by JLyc on 4/10/2016.
 */
public class ParVariables {
    private static final String PREFIX_NS = "ns";
    private static final String NAME_SPACE_URL = "http://www.tibco.com/xmlns/ApplicationManagement";
    private static final String NS = "/" + PREFIX_NS + ":";
    private TreeMap<String, String> parslWars = new TreeMap<>();
    private Map<String, String> namespaceUris = new HashMap<>();

    private Document document;

    public ParVariables(Document document) {
        this.document = document;
        namespaceUris.put(PREFIX_NS, NAME_SPACE_URL);

        XPath xpathSelector = DocumentHelper.createXPath(NS + "application" + NS + "services/.");
        xpathSelector.setNamespaceURIs(namespaceUris);

        parseParVariables(xpathSelector.selectNodes(document));
    }

    private Map<String, String> parseParVariables(List list) {
        parslWars = new TreeMap<>();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Element child = (Element) iter.next();
            for (int i = 0, size = child.nodeCount(); i < size; i++) {
                Node node = child.node(i);
                if (node instanceof Element) {
                    parslWars.put(((Element) node).attribute("name").getValue(), node.getName());
                }
            }
        }

        for (Map.Entry<String, String > parName : parslWars.entrySet()) {
            mainVariables(parName.getKey());
            bindingsVariables(parName.getKey());
        }
        return parslWars;
    }

    private void bindingsVariables(String parName) {
        String rootPath = NS + "application" + NS + "services" + NS + "bw[@name='" + parName + "']" + NS + "bindings"+NS+"binding[@name='" + parName.subSequence(0,parName.length()-4) + "']";
        System.out.println(rootPath+NS+"machine");
        XPath xpathSelector = DocumentHelper.createXPath(rootPath+NS+"machine");
        xpathSelector.setNamespaceURIs(namespaceUris);
        Element machine = (Element) xpathSelector.selectNodes(document).get(0);
        System.out.println(machine.getName()+" -> " + machine.getText());
        System.out.println(rootPath+NS+"settings/.");
        xpathSelector = DocumentHelper.createXPath(rootPath+NS+"settings/.");
        xpathSelector.setNamespaceURIs(namespaceUris);
        List list = xpathSelector.selectNodes(document);
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Element child = (Element) iter.next();
            for (int i = 0, size = child.nodeCount(); i < size; i++) {
                Node node = child.node(i);
                if (node instanceof Element) {
                    System.out.println(node.getName() + " -> " + node.getText());
                }
            }
        }

        System.out.println("===========================================");
    }

    private void mainVariables(String parName) {
        System.out.println(parName + "\n===========================================");
        XPath xpathSelector = DocumentHelper.createXPath(NS + "application" + NS + "services" + NS + "bw[@name='" + parName + "']/.");
        xpathSelector.setNamespaceURIs(namespaceUris);
        List list1 = xpathSelector.selectNodes(document);
        for (Iterator iter = list1.iterator(); iter.hasNext(); ) {
            Element child = (Element) iter.next();
            for (int i = 0, size = child.nodeCount(); i < size; i++) {
                Node node = child.node(i);
                if (node instanceof Element && ((Element) node).nodeCount() == 1) {
                    System.out.println(node.getName() + " -> " + node.getText());
                }
            }
        }
        System.out.println("===========================================");
    }

    public TreeMap<String, String> getParslWars() {
        return parslWars;
    }
}
