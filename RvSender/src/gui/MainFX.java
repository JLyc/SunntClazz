package gui;

import com.sun.xml.internal.txw2.TXW;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rv_wrappers.RvCommunication;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JLyc on 3/6/2016.
 */
public class MainFX extends Application {
    private TextField rvDeamonName;
    private TextField rvDeamon;
    private TextField rvNetwork;
    private TextField rvService;
    private TextField rvSubject;

    private Accordion accordion = new Accordion();

    private static int panelsOffset=0;

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));

        final GridPane gridPane = new GridPane();

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        deamonProperties(gridPane);
        deamonInstances(gridPane);
        MessagePanel.getMessagePanel().init(gridPane, panelsOffset);

        root.getChildren().add(gridPane);
    }

    private void deamonInstances(GridPane gridPane) {
        int _panelsOffset = panelsOffset;
        Button testConnection = new Button("Test Connection");
        GridPane.setConstraints(testConnection, 2, _panelsOffset++);
        testConnection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RvCommunication rvCommunicationDeamon = new RvCommunication(rvDeamonName.getText(),rvDeamon.getText(),rvNetwork.getText(),rvService.getText(),rvSubject.getText());
                rvCommunicationDeamon.open();
                rvCommunicationDeamon.close();
            }
        });
        _panelsOffset = panelsOffset;
        Button addDeamon = new Button("Add Deamon");
        GridPane.setConstraints(addDeamon, 1, _panelsOffset++);
        addDeamon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                RvCommunication rvCommunicationDeamon = new RvCommunication(rvDeamonName.getText(),rvDeamon.getText(),rvNetwork.getText(),rvService.getText(),rvSubject.getText());
                rvCommunicationDeamon.open();
                GridPane layut = new GridPane();
                RvDeamon titledPane = new RvDeamon(rvCommunicationDeamon , layut, rvDeamon.getText(), rvNetwork.getText(), rvService.getText());
                titledPane.addToAccordion(accordion);
            }
        });
        GridPane.setConstraints(accordion, 3, 0,4, 10);
        gridPane.getChildren().addAll(accordion, addDeamon, testConnection);
    }

    private void deamonProperties(GridPane gridPane) {
        int _panelsOffset = panelsOffset;
        Label rvDeamonNameLabel = new Label("RvName");
        GridPane.setConstraints(rvDeamonNameLabel, 1, _panelsOffset++);
        Label rvDeamonLabel = new Label("RvDeamon");
        GridPane.setConstraints(rvDeamonLabel, 1, _panelsOffset++);
        Label rvNetworkLabel = new Label("RvNetwork");
        GridPane.setConstraints(rvNetworkLabel, 1, _panelsOffset++);
        Label rvServiceLabel = new Label("RvService");
        GridPane.setConstraints(rvServiceLabel, 1, _panelsOffset++);
        Label rvSubjectLabel = new Label("RvSubject");
        GridPane.setConstraints(rvSubjectLabel, 1, _panelsOffset++);

        _panelsOffset = panelsOffset;
        rvDeamonName = new TextField("artemis");
        GridPane.setConstraints(rvDeamonName, 2, _panelsOffset++);
        rvDeamon = new TextField("tcp:7541");
        GridPane.setConstraints(rvDeamon, 2, _panelsOffset++);
        rvNetwork = new TextField("");
        GridPane.setConstraints(rvNetwork, 2, _panelsOffset++);
        rvService = new TextField("7541");
        GridPane.setConstraints(rvService, 2, _panelsOffset++);
        rvSubject = new TextField("RvSubject");
        GridPane.setConstraints(rvSubject, 2, _panelsOffset++);

        panelsOffset = _panelsOffset;

        gridPane.getChildren().addAll(
                rvDeamonNameLabel,
                rvDeamonName,
                rvSubjectLabel,
                rvDeamon,
                rvNetworkLabel,
                rvNetwork,
                rvServiceLabel,
                rvService,
                rvSubject,
                rvDeamonLabel
        );
    }

    public static void messageEditor(TextArea msgReply) {
        JTextArea helpArea = new JTextArea(msgReply.getText());
        JScrollPane scrollPane = new JScrollPane(helpArea);
        helpArea.setLineWrap(true);
        helpArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        JOptionPane.showMessageDialog(null, scrollPane, "RV message",
                JOptionPane.PLAIN_MESSAGE);
        msgReply.setText(helpArea.getText());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(270);
        primaryStage.show();
        primaryStage.setTitle("RandevouzClient");
    }

    public static void launchFX() {
        launch();
    }
}
