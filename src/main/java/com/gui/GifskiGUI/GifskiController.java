package com.gui.GifskiGUI;

import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URI;

public class GifskiController {

    String img;
    String saveTo;
    int quality =50;
    @FXML
    BorderPane bpane;
    @FXML
    public MFXTextField fps;

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
    Image realimg;
    @FXML
    protected void setInputFile(ActionEvent action) throws InvalidClassException{
        File dir = getFilePath(action,false);

        Image image = new Image(dir.toURI().toString());
        this.preview.setImage(image);
        prev2.setImage(new Image(new File(dir.getParent()).listFiles()[1].toURI().toString()));
        prev3.setImage(new Image(new File(dir.getParent()).listFiles()[2].toURI().toString()));
        prev2.setOpacity(0.5);
        prev3.setOpacity(0.25);
        realimg = image;
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
    //Factory?
    public String constructCMD(String input, String output , int fps, int width, int height, int quality){

        String path ="cd "+System.getProperty("user.dir")+"\\src\\main\\java\\com\\gui\\GifskiGUI\\gifskiwin\\ && " +"gifski.exe ";
        String in = input+"/*";
        String framerate = " --fps "+fps;
        String out = " --output "+output;
        String sizeX = " --width "+width;
        String sizeY = " --height "+height;
        String qual = " --quality "+quality;

        return path+in+framerate+sizeX+sizeY+qual+out;
    }


    @FXML
    ImageView preview;
    @FXML
    ImageView prev2;

    @FXML
    ImageView prev3;



    //TODO: Make file path variable


    public void convert()throws IOException{
        setQuality();

        System.out.println(quality);


                ProcessBuilder builder = new ProcessBuilder(
                        "cmd.exe", "/c", constructCMD(this.img,this.saveTo,Integer.parseInt(this.fps.getText()),realimg.widthProperty().intValue(), realimg.heightProperty().intValue(),this.quality));
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
           this.quality=(int)((MFXSlider)bpane.getScene().lookup("#imgquality")).getValue();



    }
}