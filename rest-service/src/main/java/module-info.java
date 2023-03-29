module notes.restservice {
    requires kotlin.stdlib;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.web;
    requires spring.beans;
    requires com.google.gson;
    requires java.sql;
    exports notes.restservice;
}
