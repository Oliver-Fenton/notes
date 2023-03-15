module notes.shared {
    requires kotlin.stdlib;
    requires javafx.base;
    requires org.jsoup;
    requires java.sql;
    exports notes.shared;
    exports notes.shared.model;
    exports notes.shared.preferences;
}