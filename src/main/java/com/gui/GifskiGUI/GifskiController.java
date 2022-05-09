package com.gui.GifskiGUI;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Material;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import com.gui.GifskiGUI.GifskiGUI;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.net.URI;

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


    /**
     * Gets Images to be passed on to Gifski
     * @param onaction
     */
    @FXML
    protected void getInputFile(ActionEvent onaction){
        FileChooser dchooser = new FileChooser();

        dchooser.setTitle("Open Resource File");

        File dir = dchooser.showOpenDialog((Stage) bpane.getScene().getWindow());
        if(dir !=null){
            Node n =(Node)onaction.getSource();
            Scene sc = n.getScene();
             TextField tx = (TextField) sc.lookup("#dirinput");
            tx.setText(dir.getAbsolutePath());

            Image imng = new Image(dir.toURI().toString());

            ImageView preview = (ImageView) sc.lookup("#preview");
            preview.setImage(imng);

            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Not a directory, you dumbfuck");
        alert.setContentText("Choose a fucking directory");
        alert.showAndWait();


    }

    /**
     *Takes an Object and sets the scene to ID of object
     *
     * @param actionEvent
     */
    @FXML
    public void switchScene(ActionEvent actionEvent) {


        try {
            Node idnode = (Node)actionEvent.getSource();
            FXMLLoader fxmlLoader;
            String SceneName = "ErrorPage";
            switch (idnode.getId()) {
                case "Info":
                    SceneName = "AboutInfo.fxml";
                    break;
                case "Gifski":
                    SceneName = "GifskiMain.fxml";
            }
            fxmlLoader = new FXMLLoader(GifskiGUI.class.getResource(SceneName));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }



    }

    /**
     * Links to Website
     * @param mouseEvent
     */
    @FXML
    public void redirectToSite(MouseEvent mouseEvent) {
        Hyperlink h = new Hyperlink();
        h.setText("https://gif.ski/");
        try {


            Desktop.getDesktop().browse(new URI("https://gif.ski/"));
        }catch (Exception e){
            System.out.println(e);
        }
    }
}