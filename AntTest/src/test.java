import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * Created by JLyc on 3/7/2016.
 */
public class test {
    static Logger logger = Logger.getLogger(test.class);
    public static void main(String[] args) {
        JOptionPane.showInputDialog(null, JOptionPane.ERROR_MESSAGE, "msg");

        System.out.println("fuck you");
        logger.info("fuck you too");
    }
}
