package com.gui.GifskiGUI;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Material;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import com.gui.GifskiGUI.GifskiGUI;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;

public class GifskiController {

    String img;
    String saveTo;
    int quality =50;
    @FXML
    BorderPane bpane;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage stage;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }



    @FXML
    protected void setOutputFile(ActionEvent action) throws InvalidClassException{
        File dir = getFilePath(action,true);
        Node n = (Node) action.getSource();
        Scene sc = n.getScene();
        TextField tx = (TextField) sc.lookup("#fileoutput");
        tx.setText(dir.getAbsolutePath());
        saveTo = dir.getAbsolutePath();
    }


    protected Node getElementByID(Node node,String id){
        Scene sc = node.getScene();
        Node tx =  sc.lookup(id);
        return tx;
    }
    @FXML
    protected void setInputFile(ActionEvent action) throws InvalidClassException{
        File dir = getFilePath(action,false);
        Node n = (Node) action.getSource();
        Scene sc = n.getScene();
        TextField tx = (TextField) sc.lookup("#dirinput");
        tx.setText(dir.getAbsolutePath());
        ImageView preview = (ImageView) sc.lookup("#preview");
        preview.setImage(new Image(dir.toURI().toString()));
        this.img = dir.getParent();

    }

    /**
     * Gets Path of selected image
     *
     * @param onaction
     */
    @FXML
    protected File getFilePath(ActionEvent onaction,boolean saveDialog) throws InvalidClassException {
        FileChooser dchooser = new FileChooser();

        dchooser.setTitle("Open Resource File");
        File dir;
        if(saveDialog) {
            dchooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.gif"));
            dir=dchooser.showSaveDialog(bpane.getScene().getWindow());
        }else {
            dir = dchooser.showOpenDialog((Stage) bpane.getScene().getWindow());
        }
        //Adding action on the menu item
        if (dir != null) {
            return dir;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Not an image");
        alert.setContentText("Choose an image");
        alert.showAndWait();
        throw new InvalidClassException("Bad FileType");

    }

    /**
     * Takes an Object and sets the scene to ID of object
     *
     * @param actionEvent
     */
    @FXML
    public void switchScene(ActionEvent actionEvent) {


        Node idnode = (Node) actionEvent.getSource();
        FXMLLoader fxmlLoader;
        String SceneName = "ErrorPage";
        switch (idnode.getId()) {
            case "Info":
                SceneName = "AboutInfo.fxml";
                break;
            case "Gifski":
                SceneName = "GifskiMain.fxml";
        }
        try {
            setScene(SceneName, (Node) actionEvent.getSource());

        } catch (IOException e) {
            System.out.println("Error setting scene");
        }
    }

    //TODO: Make file path variable

    @FXML
    public void convert()throws IOException{
        setQuality();
        System.out.println(quality);


        String path ="gifski.exe "+this.img+"/*";
        System.out.println(path);
                ProcessBuilder builder = new ProcessBuilder(
                        "cmd.exe", "/c", "cd C:\\Users\\Timo\\IdeaProjects\\demo2\\src\\main\\java\\com\\gui\\GifskiGUI\\gifskiwin\\ && " +path+" --quality"+quality+" --output "+this.saveTo);
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) { break; }
                    System.out.println(line);

        }

    }


    public void setScene(String sceneFileName, Node node) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GifskiGUI.class.getResource(sceneFileName));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        Stage stage = (Stage) (node.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Links to Website
     *
     * @param mouseEvent
     */
    @FXML
    public void redirectToSite(MouseEvent mouseEvent) {
        Hyperlink h = new Hyperlink();
        h.setText("https://gif.ski/");
        try {


            Desktop.getDesktop().browse(new URI("https://gif.ski/"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void setQuality() {
           this.quality=Integer.parseInt( ""+((MFXSlider)bpane.getScene().lookup("#imgquality")).getValue());



    }
}