package gui;

import com.sun.xml.internal.txw2.TXW;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rv_wrappers.RvCommunication;

import javax.swing.*;
import java.awt.*;

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


    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));

        final GridPane gridPane = new GridPane();

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        deamonProperties(gridPane);
        messagePanel(gridPane);
        deamonInstances(gridPane);

        root.getChildren().add(gridPane);
    }

    private void messagePanel(GridPane gridPane) {
        Label msgLabel = new Label("Msg");
        GridPane.setConstraints(msgLabel, 1, 7);
        final TextArea msgText = new TextArea();
        msgText.setPromptText("\t Enter your randevousz msg here");
        msgText.setPrefSize(244, 180);
        msgText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        messageEditor(msgText);
                    }
                }
            }
        });
        GridPane.setConstraints(msgText, 1, 8, 2, 1);
        gridPane.getChildren().addAll(msgLabel, msgText);
    }

    private void deamonInstances(GridPane gridPane) {
        Button testConnection = new Button("Test Connection");
        GridPane.setConstraints(testConnection, 2, 6);
        testConnection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RvCommunication rvCommunicationDeamon = new RvCommunication(rvDeamonName.getText(),rvDeamon.getText(),rvNetwork.getText(),rvService.getText(),rvSubject.getText());
                rvCommunicationDeamon.open();
                rvCommunicationDeamon.close();
            }
        });

        Button addDeamon = new Button("Add Deamon");
        GridPane.setConstraints(addDeamon, 1, 6);
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
        GridPane.setConstraints(accordion, 3, 1,4, 7);
        gridPane.getChildren().addAll(accordion, addDeamon, testConnection);
    }

    private void deamonProperties(GridPane gridPane) {
        Label rvDeamonNameLabel = new Label("RvName");
        GridPane.setConstraints(rvDeamonNameLabel, 1, 1);
        Label rvDeamonLabel = new Label("RvDeamon");
        GridPane.setConstraints(rvDeamonLabel, 1, 2);
        Label rvNetworkLabel = new Label("RvNetwork");
        GridPane.setConstraints(rvNetworkLabel, 1, 3);
        Label rvServiceLabel = new Label("RvService");
        GridPane.setConstraints(rvServiceLabel, 1, 4);
        Label rvSubjectLabel = new Label("RvSubject");
        GridPane.setConstraints(rvSubjectLabel, 1, 5);


        rvDeamonName = new TextField("RvName");
        GridPane.setConstraints(rvDeamonName, 2, 1);
        rvDeamon = new TextField("RvDeamon");
        GridPane.setConstraints(rvDeamon, 2, 2);
        rvNetwork = new TextField("RvNetwork");
        GridPane.setConstraints(rvNetwork, 2, 3);
        rvService = new TextField("RvService");
        GridPane.setConstraints(rvService, 2, 4);
        rvSubject = new TextField("RvSubject");
        GridPane.setConstraints(rvSubject, 2, 5);


        gridPane.getChildren().addAll(
                rvDeamonNameLabel,
                rvDeamonName,
                rvSubjectLabel,
                rvSubject,
                rvDeamonLabel,
                rvDeamon,
                rvNetworkLabel,
                rvNetwork,
                rvServiceLabel,
                rvService
        );
    }

    public static void messageEditor(TextArea msgReply) {
        JTextArea helpArea = new JTextArea(msgReply.getText());
        JScrollPane scrollPane = new JScrollPane(helpArea);
        helpArea.setLineWrap(true);
        helpArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(500, 500));
        System.out.println("loading");
        JOptionPane.showMessageDialog(null, scrollPane, "RV message",
                JOptionPane.PLAIN_MESSAGE);
        msgReply.setText(helpArea.getText());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.setMinWidth(470);
        primaryStage.setMinHeight(270);
        primaryStage.show();
        primaryStage.setTitle("RandevouzClien");
    }

    public static void launchFX() {
        launch();
    }
}
