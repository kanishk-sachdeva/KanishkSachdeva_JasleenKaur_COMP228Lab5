module com.kanishkjasleen.kanishksachdeva_jasleenkaur_comp228lab5 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires ojdbc7;

    opens com.kanishkjasleen.kanishksachdeva_jasleenkaur_comp228lab5 to javafx.fxml;
    exports com.kanishkjasleen.kanishksachdeva_jasleenkaur_comp228lab5;
}