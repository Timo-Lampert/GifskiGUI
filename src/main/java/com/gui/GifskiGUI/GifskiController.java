package com.gui.GifskiGUI;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GifskiController {

    @FXML
    public MFXTextField fps;

    @FXML
    private Text filedesttext;

    public Stage stage;
    @FXML
    GridPane intropane;
    String imgpath;
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
    MFXTextField inputFile;
    @FXML
    Text success;
    @FXML
    MFXTextField outputFile;
    @FXML
    MFXTextField repeat;
    @FXML
    MFXButton gobutton;
    @FXML
    Pane pane2;
    @FXML
    Pane pane1;
    @FXML
    HBox hbox;
    @FXML
    ImageView lock;
    @FXML
    AnchorPane anchpane;
    String initialuserSetPath;
    @FXML
    private MFXTextField width;
    @FXML
    private MFXTextField height;
    @FXML
    private MFXProgressSpinner spin;
    @FXML
    private AnchorPane spinpane;

    double aspectratio=1;

    private boolean lockwidthheight = true;
    public void initialize() {
        if (intropane != null) {
            intropane.setVisible(true);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    //aspect lock for width text
    @FXML
    private void setLockedWidth(Event action){
        intValidator(action);
        if(lockwidthheight){
            try {
                int width = Integer.parseInt(this.width.getText());
                this.height.setText("" + (int) ((width) * aspectratio));
            }catch (NumberFormatException ignored){

            }
        }
    }
    //aspect lock for height text
    @FXML
    private void setLockedHeight(Event action){
        intValidator(action);
        if(lockwidthheight){
            try {
                int height = Integer.parseInt(this.height.getText());
                this.width.setText("" + (int) ((height) / aspectratio));
            }catch (NumberFormatException ignored){

            }
        }
    }
    //click on lock icon
    @FXML
    private void toggleAspectLock(Event event){
        lockwidthheight = !lockwidthheight;
        try {
            aspectratio = setAspectratio();
        }catch (Exception ignore){
            //if unable to parse or attempt to divide by 0 no changes will be made to the ratio
        }
        if (lockwidthheight) {

            lock.setOpacity(1);
        } else {
            lock.setOpacity(0.5);
        }

    }


    private double setAspectratio()throws Exception{
        return (double)Integer.parseInt(height.getText())/(double)Integer.parseInt(width.getText());
    }



    @FXML
    private void scaledownMouseExit(Event e) {
        Node n = (Node) e.getSource();
        String nid = n.getId();
        ScaleTransition x = new ScaleTransition(Duration.millis(200), n);
        x.setFromX(1.1);
        x.setInterpolator(Interpolator.EASE_BOTH);
        x.setToX(1);
        ScaleTransition y = new ScaleTransition(Duration.millis(200), n);
        y.setFromY(1.1);
        y.setInterpolator(Interpolator.EASE_BOTH);
        y.setToY(1);
        x.play();
        y.play();


    }

    @FXML
    private void scaleOnMOuseOver(Event e) {
        Node n = (Node) e.getSource();
        String nid = n.getId();
        ScaleTransition x = new ScaleTransition(Duration.millis(200), n);
        x.setFromX(1);
        x.setInterpolator(Interpolator.EASE_BOTH);
        x.setToX(1.1);
        ScaleTransition y = new ScaleTransition(Duration.millis(200), n);
        y.setFromY(1);
        y.setInterpolator(Interpolator.EASE_BOTH);
        y.setToY(1.1);
        x.play();
        y.play();


    }

    @FXML
    protected void setOutputFile(ActionEvent action) throws InvalidClassException {
        File dir = getFilePath(true);
        if(dir ==null){
            return;
        }
        outputFile.setText(dir.getAbsolutePath());
        //Instantiating TranslateTransition class
        TranslateTransition translate = new TranslateTransition();
        translate.setByX(-400);
        translate.setDuration(Duration.millis(1000));
        translate.setNode(pane1);
    }

    /**
     * sets preview images, textbox and width/height according to image
     *
     * @param
     * @throws InvalidClassException
     */
    @FXML
    protected void setInputFile() throws InvalidClassException {
        File dir;

            dir = getFilePath(false);
        if(dir ==null){
            return;
        }
        prev2.setOpacity(0);
        prev3.setOpacity(0);
        initialuserSetPath = dir.getParent();

        File[] files = new File(dir.getParent()).listFiles((dir1, name) -> name.toLowerCase().endsWith(".png"));
        Image badImg;
        if (files.length <1) {
            displayAlert("No valid png files detected", "only png files are valid.");
            return;
        }
        if (imagesValid(files) != null) {
            badImg = imagesValid(files);
            displayAlert("Invalid width/height", "One or more images dont have the same width/height: " + badImg.getUrl());
            return;
        }


        File[] all = new File(dir.getParent()).listFiles();


        if (files.length < all.length) {
            displayAlert("Non png files detected", "Only PNG files are supported. Only valid files have been added to selection", Alert.AlertType.INFORMATION);
        }
        success.setOpacity(1);
        success.setText("Files moved to temp directory for compability");
        success.setFill(Color.BLUEVIOLET);
        FadeTransition fd = (FadeTransition) Transit(new FadeTransition(),2500,success,1,0);
        fd.setDelay(Duration.millis(5000));
        fd.setInterpolator(Interpolator.EASE_BOTH);
        fd.play();


        String newDir;
        try {

            newDir = moveFilesToTMP(files);

            Image image = new Image(new File(newDir).listFiles()[0].toURI().toString());
            realimg = image;
            this.preview.setImage(image);
            FadeTransition pretransit = (FadeTransition) Transit(new FadeTransition(), 500, preview, 0, 1);
            pretransit.play();
            this.imgpath = newDir;
            inputFile.setText(newDir);
            width.setText("" + image.widthProperty().intValue());
            aspectratio = image.heightProperty().doubleValue()/image.widthProperty().doubleValue();
            height.setText("" + image.heightProperty().intValue());

        } catch (IOException e) {
            System.out.println(e);
        }

        //set 2 othe rpreview images with a fade in
        if (files.length > 1) {
            prev2.setImage(new Image(files[1].toURI().toString()));

            FadeTransition f1 = new FadeTransition();
            f1.setFromValue(0);
            f1.setToValue(0.5);
            f1.setDuration(Duration.millis(500));
            f1.setNode(prev2);
            f1.play();
        }
        if (files.length > 2) {
            prev3.setImage(new Image(files[2].toURI().toString()));

            FadeTransition f1 = new FadeTransition();
            f1.setFromValue(0);
            f1.setToValue(0.25);
            f1.setDelay(Duration.millis(250));
            f1.setDuration(Duration.millis(500));
            f1.setNode(prev3);
            f1.play();
        }


    }

    private Image imagesValid(File[] files) {
        Image reference = new Image(files[0].toURI().toString());
        for (File f : files
        ) {
            Image img = new Image(f.toURI().toString());
            if (img.getWidth() != reference.getWidth() && img.getHeight() != reference.getHeight()) {
                return img;
            }
        }
        return null;
    }


    TranslateTransition ts;

    @FXML
    protected void nextSLide() {
        if (pane1.getTranslateX() <= -200 || (ts != null && ts.getStatus() == Animation.Status.RUNNING)) {
            return;
        }


        FadeTransition f2 = new FadeTransition();

        f2.setDuration(Duration.millis(200));
        f2.setFromValue(1);
        f2.setToValue(0);
        f2.setNode(pane1);
        f2.play();
        TranslateTransition translate = new TranslateTransition();
        ts = translate;
        translate.setByX(-300);
        translate.setDuration(Duration.millis(500));
        translate.setNode(pane1);
        translate.setInterpolator(Interpolator.EASE_BOTH);
        translate.play();

        translate.setOnFinished(e -> {
            TranslateTransition translate2 = new TranslateTransition();
            translate2.setByX(-200);
            translate2.setDuration(Duration.millis(500));
            translate2.setNode(pane2);
            translate2.play();
            translate2.setInterpolator(Interpolator.EASE_BOTH);


            FadeTransition f = new FadeTransition();
            f.setDuration(Duration.millis(500));
            f.setFromValue(0);
            f.setToValue(1);
            f.setNode(pane2);
            f.play();

        });


    }

    @FXML
    protected void introskip() {

        FadeTransition f = new FadeTransition();
        f.setDuration(Duration.millis(550));
        f.setFromValue(intropane.getOpacity());
        f.setToValue(0);
        f.setNode(intropane);
        f.play();
        TranslateTransition translate = new TranslateTransition();
        translate.setByY(-400);
        translate.setDuration(Duration.millis(1000));
        translate.setNode(intropane);

        translate.setInterpolator(Interpolator.EASE_BOTH);
        translate.play();
        translate.setOnFinished(s -> {
                    intropane.setVisible(false);
                }
        );


    }


    /**
     * Alert helper, standard alert is error
     *
     * @param title
     * @param descr
     */
    void displayAlert(String title, String descr) {
        displayAlert(title, descr, Alert.AlertType.ERROR);
    }

    void displayAlert(String title, String descr, Alert.AlertType type) {
        Alert al = new Alert(type);
        al.setTitle(title);
        al.setContentText(descr);
        al.showAndWait();
    }

    /**
     * Gets Path of selected image
     */
    @FXML
    protected File getFilePath(boolean saveDialog) throws InvalidClassException {
        FileChooser dchooser = new FileChooser();

        dchooser.setTitle("Open Resource File");
        File dir;
        if (saveDialog) {

            dchooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Gif Image", "*.gif"));
            if (initialuserSetPath != null) {
                dchooser.setInitialDirectory(new File(initialuserSetPath));
            }
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
        if (saveDialog) {
            alert.setTitle("Invalid Savepath");
            alert.setContentText("Choose a filepath");
        }
        alert.showAndWait();
        return null;

    }

    /**
     * returns path to tmp directory containing passed files
     *
     * @param files
     * @return
     * @throws IOException
     */
    private String moveFilesToTMP(File[] files) throws IOException {
        String newDir;

        newDir = Files.createTempDirectory("gifskitmp").toString();
        System.out.println(newDir);
        int i = 0;
        assert files != null;
        for (File file : files
        ) {

            FileUtils.copyFile(file, new File(newDir + "/file" + i + ".png"));
            i++;
        }

        return newDir;
    }


    @FXML
    protected void intValidator(Event action) {
        TextField val = ((TextField) action.getSource());

        char[] charcters = val.getText().toCharArray();
        String resulttext="";
        for (int i = 0; i < charcters.length; i++) {
            if(isInteger(charcters[i]+"")){
                resulttext+=charcters[i];
            }else {
                displayAlert("Not numeric value", "Select a number for this Textfield");
                val.setText(resulttext);
            }
        }



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


    public boolean isInteger(String str) {
        return str.matches("\\d+");
    }

    /**
     * @param input
     * @param output
     * @param fps
     * @param width
     * @param height
     * @param quality
     * @return String with PATH to gifski and CMD with supplied commands
     */
    //TODO:Factory?
    public String constructCMD(String input,
                               String output,
                               int fps,
                               int width,
                               int height,
                               int quality,
                               int repeat) {

        String path = "cd " + System.getProperty("user.dir") + "\\src\\main\\java\\com\\gui\\GifskiGUI\\gifskiwin\\ && " + "gifski.exe ";
        //currently takes the entire folder
        String in = input + "\\*";
        String framerate = " --fps " + fps;
        String out = " --output " + '"'+output+'"';
        String sizeX = " --width " + width;
        String sizeY = " --height " + height;
        String qual = " --quality " + quality;
        String rep = " --repeat " + repeat;

        return path + in + framerate + sizeX + sizeY + qual + rep + out;
    }

    /**
     * executes gifski cmd commands
     *
     * @throws IOException
     */
    //TODO: Make file path variable
    public void convert() throws IOException {
        setQuality();


        if (outputFile.getText().equals("")) {
            displayAlert("Invalid path", "Invalid Output path");
            return;
        }

        if (!(new File(inputFile.getText()).exists())) {
            displayAlert("Invalid path", "Invalid Input path");
            return;
        }
        String repeat = this.repeat.getText();
        if (repeat.equals("")) {

            repeat = "0"; //default prompt text = infinite
        }
        if (fps.getText().equals("")) {

            fps.setText(fps.getPromptText());
        }
        int width;
        int height;

        try{
            width =Integer.parseInt(this.width.getText());
        }catch (NumberFormatException e){
            width=realimg.widthProperty().intValue();

        }
        try{
            height =Integer.parseInt(this.height.getText());
        }catch (NumberFormatException e){
            height = realimg.heightProperty().intValue();

        }


        success.setText("Processing Image....");
        success.setFill(Color.YELLOW);
        success.setOpacity(1);

        FadeTransition spinin = new FadeTransition();
        spinin = (FadeTransition) Transit(spinin, 2000, spinpane, 0, 1);
        spinpane.setVisible(true);
        spin.setVisible(true);
        spinin.setInterpolator(Interpolator.EASE_BOTH);
        spinin.play();
        filedesttext.setText("Creating File: "+outputFile.getText());


        spin.setProgress(0);

        String gifskiCall = constructCMD(inputFile.getText(),
                outputFile.getText(),
                Integer.parseInt(this.fps.getText()),
                width,
                height,
                this.quality,
                Integer.parseInt(repeat));

        try {
            Task task = new Task<Void>() {
                @Override
                public Void call() {

                    try {
                        runtask(gifskiCall);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            task.setOnSucceeded(e -> {
                FadeTransition f = new FadeTransition();


                FadeTransition pane = (FadeTransition) Transit(f, 1000, spinpane, 1, 0);
                pane.setOnFinished(a -> {
                    spin.setVisible(false);
                    spinpane.setVisible(false);
                });
                pane.play();

            });
            new Thread(task).start();

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public void runtask(String call) throws Exception {

        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", call);
        builder.redirectErrorStream(true);
        Process p = builder.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));


        while (true) {
            try {
                String line = r.readLine();
                if (line == null) {
                    break;
                }
                //update loading icon
                String[] splittext = line.split(" ");
                if (splittext.length > 2) {

                    try {
                        double prog;
                        prog = ((double) Integer.parseInt(splittext[1]) / (double) Integer.parseInt(splittext[3]));
                        Timeline timeline = new Timeline();

                        KeyValue keyValue = new KeyValue(spin.progressProperty(), prog);
                        KeyFrame keyFrame = new KeyFrame(new Duration(500), keyValue);

                        timeline.getKeyFrames().add(keyFrame);

                        timeline.play();
                        spin.setProgress(prog);


                    } catch (NumberFormatException a) {

                    }
                }
                System.out.println(line);
                if (line.contains("gifski created")) {
                    success.setFill(Color.LIMEGREEN);
                    success.setOpacity(1);
                    success.setText("Gif created successfully");
                    Desktop.getDesktop().open(new File(outputFile.getText()));

                    //TODO:convert to alert
                } else if (line.contains("error")) {
                    String errparse = line.split("error")[1].trim();
                    success.setFill(Color.RED);
                    success.setOpacity(1);
                    success.setText("Error " + errparse);
                    spin.setProgress(0);
                }
            } catch (Exception d) {
                d.printStackTrace();
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


    private Transition Transit(Transition transition, int time, Node node, double from, double to) {
        if (transition.getClass() == FadeTransition.class) {


            transition = (FadeTransition) transition;
            ((FadeTransition) transition).setDuration(Duration.millis(time));
            ((FadeTransition) transition).setFromValue(from);
            ((FadeTransition) transition).setToValue(to);
            ((FadeTransition) transition).setNode(node);
        } else if (transition.getClass() == TranslateTransition.class) {
            transition = (TranslateTransition) transition;
            ((TranslateTransition) transition).setDuration(Duration.millis(time));
            ((TranslateTransition) transition).setFromX(from);
            ((TranslateTransition) transition).setFromX(to);
            ((TranslateTransition) transition).setNode(node);
        }
        return transition;


    }


}