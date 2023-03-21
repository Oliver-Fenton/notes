module notes.shared {
    requires kotlin.stdlib;
    requires javafx.base;
    requires org.jsoup;
    requires java.sql;
    requires javafx.web;
    exports notes.shared;
    exports notes.shared.model;
    exports notes.shared.preferences;
}