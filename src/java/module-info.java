module IBAN.checker {
    requires javafx.controls;
    requires javafx.fxml;
    requires iban;

    opens task to javafx.fxml;
    exports task;
}
