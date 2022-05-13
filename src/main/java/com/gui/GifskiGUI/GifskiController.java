package com.gui.GifskiGUI;

import io.github.palexdev.materialfx.builders.control.ProgressSpinnerBuilder;
import io.github.palexdev.materialfx.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.Objects;

public class GifskiController {

    @FXML
    public MFXTextField fps;
    public Stage stage;
    String img;
    String saveTo;
    int quality = 50;
    @FXML
    BorderPane bpane;
    Image realimg;
    @FXML
    ImageView preview;
    @FXML
    ImageView prev2;
    @FXML
    ImageView prev3;
    @FXML
    private MFXTextField width;
    @FXML
    private MFXTextField height;
    @FXML
    private MFXProgressSpinner spin;
    @FXML
    MFXTextField inputFile;
    @FXML
    Text success;
    @FXML
    MFXTextField outputFile;

    @FXML
    MFXTextField repeat;

    @FXML
    MFXButton gobutton;
    public void setStage(Stage stage) {
        this.stage = stage;
    }



    @FXML
    protected void setOutputFile(ActionEvent action) throws InvalidClassException {
        File dir = getFilePath(action, true);
        outputFile.setText(dir.getAbsolutePath());
    }


    /**
     * sets preview images, textbox and width/height according to image
     * @param action
     * @throws InvalidClassException
     */
    @FXML
    protected void setInputFile(ActionEvent action) throws InvalidClassException {
        File dir = getFilePath(action, false);
        inputFile.setText(dir.getParent());
        Image image = new Image(dir.toURI().toString());
        this.preview.setImage(image);
        prev2.setOpacity(0);
        prev3.setOpacity(0);
        realimg = image;
        this.img = dir.getParent();
        width.setText(""+realimg.widthProperty().intValue());
        height.setText(""+realimg.heightProperty().intValue());
            File[] files = new File(dir.getParent()).listFiles((dir1, name) -> name.toLowerCase().endsWith(".png"));
            if(files==null){
                return;
            }
            if(files.length>1) {
                prev2.setImage(new Image(files[1].toURI().toString()));
                prev2.setOpacity(0.5);
            }if(files.length>2) {
                prev3.setImage(new Image(files[2].toURI().toString()));
            prev3.setOpacity(0.25);
            }


    }

    /**
     * Gets Path of selected image
     *
     * @param onaction
     */
    @FXML
    protected File getFilePath(ActionEvent onaction, boolean saveDialog) throws InvalidClassException {
        FileChooser dchooser = new FileChooser();

        dchooser.setTitle("Open Resource File");
        File dir;
        if (saveDialog) {

            dchooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Gif Image", "*.gif"));
            dchooser.setInitialDirectory(new File(inputFile.getText()));
            dir = dchooser.showSaveDialog(bpane.getScene().getWindow());
        } else {
            dir = dchooser.showOpenDialog((Stage) bpane.getScene().getWindow());
            dchooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Images", "*.png"));
        }
        //Adding action on the menu item
        if (dir != null) {
            return dir;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);


        alert.setTitle("Not an image");
        alert.setContentText("Choose an image");
        if(saveDialog){
            alert.setTitle("Invalid Savepath");
            alert.setContentText("Choose a filepath");
        }
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

    /**
     *
     * @param input
     * @param output
     * @param fps
     * @param width
     * @param height
     * @param quality
     * @return String with command and agruments supplied
     */
    //Factory?
    public String constructCMD(String input,
                               String output,
                               int fps,
                               int width,
                               int height,
                               int quality,
                               int repeat) {

        String path = "cd " + System.getProperty("user.dir") + "\\src\\main\\java\\com\\gui\\GifskiGUI\\gifskiwin\\ && " + "gifski.exe ";
        //currently takes the entire folder
        String in = input + "/*";
        String framerate = " --fps " + fps;
        String out = " --output " + output;
        String sizeX = " --width " + width;
        String sizeY = " --height " + height;
        String qual = " --quality " + quality;
        String rep = " --repeat " + repeat;

        return path + in + framerate + sizeX + sizeY + qual +rep+ out;
    }

    /**
     * executes gifski cmd commands
     * @throws IOException
     */
    //TODO: Make file path variable

    public void convert() throws IOException {
        setQuality();
        if(repeat.getText().equals("")){

            repeat.setText(repeat.getPromptText());
        }
        if(fps.getText().equals("")){

            fps.setText(fps.getPromptText());
        }
        spin.setOpacity(0);
        spin.setProgress(0);
        System.out.println(quality);
        String gifskiCall = constructCMD(inputFile.getText(),
                outputFile.getText(),
                Integer.parseInt(this.fps.getText()),
                realimg.widthProperty().intValue(),
                realimg.heightProperty().intValue(),
                this.quality,
                Integer.parseInt(repeat.getText()));

        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", gifskiCall);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            String[] splittext = line.split(" ");
            if(splittext.length>2 ) {
                try {
                    double prog;
                    prog = (Integer.parseInt(splittext[1]) / Integer.parseInt(splittext[3]));
                    spin.setProgress(prog);
                }catch(NumberFormatException e){

                }
            }
            System.out.println(line);
            if(line.contains("gifski created")){
                success.setFill(Color.GREEN);
                success.setOpacity(1);
                success.setText("Gif created successfully");
                Desktop.getDesktop().open(new File(outputFile.getText()));

                //TODO:convert to alert
            }else if(line.contains("error")){
                String errparse = line.split("error")[1].trim();
                success.setFill(Color.RED);
                success.setOpacity(1);
                success.setText("Error"+errparse);
            }
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
        this.quality = (int) ((MFXSlider) bpane.getScene().lookup("#imgquality")).getValue();


    }
}