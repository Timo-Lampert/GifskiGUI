module com.gui.GifskiGUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires org.kordamp.bootstrapfx.core;

    opens com.gui.GifskiGUI to javafx.fxml;
    exports com.gui.GifskiGUI;
}