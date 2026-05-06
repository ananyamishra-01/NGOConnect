package com.ngoconnect;

import com.ngoconnect.service.DataInitializerService;
import com.ngoconnect.ui.MainApp;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        DataInitializerService.initialize();
        Application.launch(MainApp.class, args);
    }
}
