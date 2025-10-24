module br.unicentro.gpmotos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens br.unicentro.gpmotos.controller to javafx.fxml;
    exports br.unicentro.gpmotos.controller;

    opens br.unicentro.gpmotos.model to javafx.base;
    exports br.unicentro.gpmotos.model;

    opens br.unicentro.gpmotos.main to javafx.fxml;
    exports br.unicentro.gpmotos.main;

    opens br.unicentro.gpmotos.dao to javafx.fxml;
    exports br.unicentro.gpmotos.dao;

}

