package gui;

/**
 * Created by JLyc.Development@gmail.com on 1/19/2016.
 */
public class MainGUI {


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
