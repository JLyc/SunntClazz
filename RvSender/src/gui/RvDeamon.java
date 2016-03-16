package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import rv_wrappers.RvCommunication;

import javax.swing.*;
import java.awt.*;

/**
 * Created by JLyc on 3/6/2016.
 */
public class RvDeamon extends TitledPane {

    RvCommunication rvCommunicationDeamon;

    String name;

    String rvDeamon = null;
    String rvNetwork = null;
    String rvService = null;

    GridPane gridPane = new GridPane();
    Accordion accordion;

    public RvDeamon(RvCommunication rvCommunicationDeamon, GridPane layut, String rvDeamon, String rvNetwork, String rvService) {
        super("Deamon: "+ rvCommunicationDeamon.getName(), layut);

        this.rvCommunicationDeamon = rvCommunicationDeamon;
        name = rvCommunicationDeamon.getName();
        gridPane = layut;

        if (rvDeamon != null)
            this.rvDeamon = rvDeamon;
        if (rvNetwork != null)
            this.rvNetwork = rvNetwork;
        if (rvService != null)
            this.rvService = rvService;

        init();
    }

    private void init() {
        Label deamon = new Label("Deamon:\t" + rvDeamon);
        GridPane.setConstraints(deamon, 1, 1);
        GridPane.setColumnSpan(deamon, 2);

        Label network = new Label("Network:\t" + rvNetwork);
        GridPane.setConstraints(network, 1, 2);
        GridPane.setColumnSpan(network, 2);

        Label service = new Label("Service:\t" + rvService);
        GridPane.setConstraints(service, 1, 3);
        GridPane.setColumnSpan(service, 2);

//        final ProgressBar connected = new ProgressBar(0);
//        connected.setPrefWidth(130);
//        GridPane.setConstraints(connected, 1, 8);
//        GridPane.setColumnSpan(connected, 3);

        Separator separator = new Separator(Orientation.HORIZONTAL);
        GridPane.setConstraints(separator, 1, 9);
        GridPane.setColumnSpan(separator, 3);
        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        GridPane.setConstraints(separator1, 1, 10);
        GridPane.setColumnSpan(separator1, 3);

//        final ToggleButton startStop = new ToggleButton("Start");
//        ToggleGroup tgDeadmon = new ToggleGroup();
//        startStop.setToggleGroup(tgDeadmon);
//        tgDeadmon.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
//            public void changed(ObservableValue<? extends Toggle> ov,
//                                Toggle toggle, Toggle new_toggle) {
//                if (new_toggle == null) {
//                    startStop.setText("Start");
//                    connected.setProgress(0);
//                } else {
//                    startStop.setText("Stop");
//                    connected.setProgress(-1);
//                }
//            }
//        });
//        GridPane.setConstraints(startStop, 1, 5);

        Button sendMsg = new Button("Send msg");
        GridPane.setConstraints(sendMsg, 1, 5);
        final ComboBox<String> msgSend = new ComboBox<>();
        GridPane.setConstraints(msgSend, 2, 5, 2, 1);

        Button delete = new Button("X");
        GridPane.setConstraints(delete, 3, 1);
        delete.setStyle("-fx-base: red;");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rvCommunicationDeamon.close();
                accordion.getExpandedPane().setVisible(false);
                accordion.getPanes().remove(accordion.getExpandedPane());
            }
        });

        Label listenerLabel = new Label("Listener");
        GridPane.setColumnSpan(listenerLabel, 3);
        GridPane.setConstraints(listenerLabel, 1, 11);

        final ProgressBar connectedListener = new ProgressBar(0);
        connectedListener.setPrefWidth(200);
        GridPane.setConstraints(connectedListener, 1, 14);
        GridPane.setColumnSpan(connectedListener, 3);

        final Thread rvListenerInstance = new Thread(rvCommunicationDeamon);
        final ToggleButton startStopListener = new ToggleButton("Start");
        ToggleGroup tgListener = new ToggleGroup();
        startStopListener.setToggleGroup(tgListener);
        tgListener.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null) {
                    startStopListener.setText("Start");
                    connectedListener.setProgress(0);
                    rvCommunicationDeamon.stopListening();
                } else {
                    startStopListener.setText("Stop");
                    connectedListener.setProgress(-1);
                    if (!rvListenerInstance.isAlive()) {
                        rvListenerInstance.start();
                        System.out.println("started");
                    }
                }
            }
        });
        GridPane.setConstraints(startStopListener, 1, 12);

        Button sendRply = new Button("Sending reply?");
        GridPane.setConstraints(sendRply, 2, 12);


        final CheckBox isMocking = new CheckBox("is Mocking");
        isMocking.setDisable(true);
        GridPane.setConstraints(isMocking, 2, 13, 3, 1);

        final Label replyMsg = new Label("Msg");
        GridPane.setConstraints(replyMsg, 1, 15);

        final ComboBox<String> msg = new ComboBox<>();
        GridPane.setConstraints(msg, 2, 15, 2, 1);

        final TextArea msgReply = new TextArea();
        msgReply.setMaxWidth(130);
        msgReply.setEditable(isMocking.isSelected());
        msgReply.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2 && isMocking.isSelected()){
                        MainFX.messageEditor(msgReply);
                    }
                }
            }
        });
        GridPane.setConstraints(msgReply, 1, 16, 2, 4);
//        msgReply.setVisible(false);
        sendRply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(isMocking.isSelected()) {
                    isMocking.setSelected(false);
                    msgReply.setEditable(false);
                    gridPane.getChildren().removeAll(replyMsg, msg, msgReply);
                }else {
                    isMocking.setSelected(true);
                    msgReply.setEditable(true);
                    gridPane.getChildren().addAll(replyMsg, msg, msgReply);
                }
            }
        });

        gridPane.getChildren().addAll(deamon, network, service, sendMsg, msgSend, delete, separator, separator1,
                listenerLabel, startStopListener, sendRply, connectedListener, isMocking);
    }



    public void addToAccordion(Accordion accordion) {
        this.accordion = accordion;
        for (TitledPane tilePane : accordion.getPanes()) {
            if (!tilePane.isVisible()) {

            }
        }
        accordion.getPanes().add(this);
        accordion.setExpandedPane(this);
    }
}
