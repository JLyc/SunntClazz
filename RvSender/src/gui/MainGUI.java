package gui;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.lang.reflect.Field;

/**
 * Created by JLyc.Development@gmail.com on 1/19/2016.
 */
public class MainGUI {


    public static void main(String[] args) {

        if(args.length==0){
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.getRootFrame();
                }
            });
            th.start();
            MainFX.launchFX();
        }
        else{
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
//                    new MainFrame();
                }
            });
        }
    }


}
