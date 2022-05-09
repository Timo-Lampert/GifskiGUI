package com.gui.GifskiGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import com.gui.GifskiGUI.GifskiGUI;
import javafx.stage.Stage;

import java.io.File;

public class GifskiController {
    @FXML
    BorderPane bpane;
    public void setStage(Stage stage){
        this.stage=stage;
    }
    public Stage stage;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void getInputFile(ActionEvent onaction){
        DirectoryChooser dchooser = new DirectoryChooser();

        dchooser.setTitle("Open Resource File");

        File dir = dchooser.showDialog((Stage) bpane.getScene().getWindow());
        if(dir !=null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Not a directory, you dumbfuck");
        alert.setContentText("Choose a fucking directory");
        alert.showAndWait();

        getInputFile(onaction);

    }
}