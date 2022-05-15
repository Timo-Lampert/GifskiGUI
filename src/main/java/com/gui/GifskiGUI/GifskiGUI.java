package com.gui.GifskiGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;



public class GifskiGUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GifskiGUI.class.getResource("GifskiMain.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 500, 500);

        stage.setTitle("GifGUI");
        stage.getIcons().add(new Image("file:src/main/resources/com/gui/GifskiGUI/guiicon.png"));
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}