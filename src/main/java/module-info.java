module com.ngoconnect {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    exports com.ngoconnect;
    exports com.ngoconnect.ui;
    exports com.ngoconnect.model;
    exports com.ngoconnect.service;
    exports com.ngoconnect.repository;
    exports com.ngoconnect.exception;
    exports com.ngoconnect.util;

    opens com.ngoconnect to javafx.fxml;
    opens com.ngoconnect.ui to javafx.fxml;
    opens com.ngoconnect.model to javafx.base;
}
