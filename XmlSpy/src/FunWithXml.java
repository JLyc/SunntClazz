/**
 * Created by JLyc.Development@gmail.com on 1/9/2016.
 */

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class FunWithXml {


    public static void main(String[] args) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("tl1-custom-adapter");
        Element status = root.addElement("status");
        status.addElement("statusCode").addText("0");
        status.addElement("description").addText("0");
        Element group = root.addElement("getStatus").addElement("group");

        group.addElement("1").addText("1");
        group.addElement("1").addText("1");
        group.addElement("1").addText("1");
        group.addElement("1").addText("1");
        group.addElement("1").addText("1");
        group.addElement("1").addText("1");

        System.out.println(document.asXML());
    }

}
