module notes.shared {
    requires kotlin.stdlib;
    requires javafx.base;
    requires org.jsoup;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.web;
    requires java.net.http;
    requires spring.web;
    requires com.google.gson;
    exports notes.shared;
    exports notes.shared.model;
    exports notes.shared.preferences;
}