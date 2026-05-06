package com.ngoconnect.ui;

import com.ngoconnect.model.User;
import com.ngoconnect.repository.OrganisationRepository;
import com.ngoconnect.repository.UserRepository;
import com.ngoconnect.service.DataInitializerService;
import com.ngoconnect.service.OrganisationService;
import com.ngoconnect.service.UserService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private OrganisationService orgService;
    private UserService userService;
    private User currentUser;

    // Shared colour palette
    public static final String GREEN_DARK   = "#1B5E20";
    public static final String GREEN_MID    = "#2E7D32";
    public static final String GREEN_LIGHT  = "#4CAF50";
    public static final String GREEN_PALE   = "#E8F5E9";
    public static final String ACCENT       = "#FF8F00";
    public static final String TEXT_DARK    = "#212121";
    public static final String TEXT_MED     = "#424242";
    public static final String TEXT_LIGHT   = "#757575";
    public static final String BG_WHITE     = "#FFFFFF";
    public static final String BG_GREY      = "#F5F5F5";
    public static final String BORDER       = "#E0E0E0";

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        OrganisationRepository orgRepo = DataInitializerService.getOrgRepo();
        UserRepository userRepo        = DataInitializerService.getUserRepo();
        this.orgService  = new OrganisationService(orgRepo);
        this.userService = new UserService(userRepo);

        stage.setTitle("NGOConnect — Bridging Communities");
        stage.setMinWidth(1024);
        stage.setMinHeight(700);

        showHome();
        stage.show();
    }

    /* ── Navigation helpers ──────────────────────────────── */

    public void showHome() {
        HomeView view = new HomeView(this);
        setScene(view.getRoot(), 1150, 750);
    }

    public void showSearch() {
        SearchView view = new SearchView(this);
        setScene(view.getRoot(), 1150, 750);
    }

    public void showBrowse() {
        BrowseView view = new BrowseView(this);
        setScene(view.getRoot(), 1150, 750);
    }

    public void showOrgProfile(com.ngoconnect.model.Organisation org) {
        OrgProfileView view = new OrgProfileView(this, org);
        setScene(view.getRoot(), 1150, 780);
    }

    public void showAccount() {
        AccountView view = new AccountView(this);
        setScene(view.getRoot(), 1150, 750);
    }

    public void showAdmin() {
        AdminView view = new AdminView(this);
        setScene(view.getRoot(), 1200, 800);
    }

    private void setScene(javafx.scene.Parent root, double w, double h) {
        Scene scene = new Scene(root, w, h);
        scene.setFill(Color.web(BG_GREY));
        primaryStage.setScene(scene);
        primaryStage.setWidth(w);
        primaryStage.setHeight(h);
    }

    /* ── Getters ─────────────────────────────────────────── */
    public Stage getStage()                      { return primaryStage; }
    public OrganisationService getOrgService()   { return orgService; }
    public UserService getUserService()          { return userService; }
    public User getCurrentUser()                 { return currentUser; }
    public void setCurrentUser(User u)           { this.currentUser = u; }

    /* ── Shared UI factory helpers ───────────────────────── */

    public static Button primaryButton(String text) {
        Button b = new Button(text);
        b.setStyle(
            "-fx-background-color: " + GREEN_LIGHT + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 20 8 20;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setStyle(
            "-fx-background-color: " + GREEN_MID + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 20 8 20;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        ));
        b.setOnMouseExited(e -> b.setStyle(
            "-fx-background-color: " + GREEN_LIGHT + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 20 8 20;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        ));
        return b;
    }

    public static Button outlineButton(String text) {
        Button b = new Button(text);
        b.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: " + GREEN_LIGHT + ";" +
            "-fx-border-width: 1.5;" +
            "-fx-text-fill: " + GREEN_MID + ";" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 6 16 6 16;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setStyle(
            "-fx-background-color: " + GREEN_PALE + ";" +
            "-fx-border-color: " + GREEN_LIGHT + ";" +
            "-fx-border-width: 1.5;" +
            "-fx-text-fill: " + GREEN_DARK + ";" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 6 16 6 16;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;"
        ));
        b.setOnMouseExited(e -> b.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: " + GREEN_LIGHT + ";" +
            "-fx-border-width: 1.5;" +
            "-fx-text-fill: " + GREEN_MID + ";" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 6 16 6 16;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;"
        ));
        return b;
    }

    /** Top navigation bar shared by all views */
    public HBox buildNavBar(String activeSection) {
        HBox nav = new HBox(0);
        nav.setStyle("-fx-background-color: " + GREEN_DARK + "; -fx-pref-height: 56px;");
        nav.setAlignment(Pos.CENTER_LEFT);

        // Brand
        Label brand = new Label("🌿 NGOConnect");
        brand.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 24 0 20;");

        // Nav items
        HBox navItems = new HBox(4);
        navItems.setAlignment(Pos.CENTER);
        String[] labels = {"Home", "Search", "Browse", "Account"};
        for (String lbl : labels) {
            Button btn = navButton(lbl, lbl.equals(activeSection));
            btn.setOnAction(e -> {
                switch (lbl) {
                    case "Home"    -> showHome();
                    case "Search"  -> showSearch();
                    case "Browse"  -> showBrowse();
                    case "Account" -> showAccount();
                }
            });
            navItems.getChildren().add(btn);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // User chip or login
        HBox right = new HBox(8);
        right.setAlignment(Pos.CENTER_RIGHT);
        right.setPadding(new Insets(0, 16, 0, 0));

        if (currentUser != null) {
            Label userLabel = new Label("👤 " + currentUser.getUsername());
            userLabel.setStyle("-fx-text-fill: #C8E6C9; -fx-font-size: 13px;");
            Button adminBtn = null;
            if (currentUser.isAdmin()) {
                adminBtn = new Button("⚙ Admin");
                String adminStyle = "-fx-background-color: " + ACCENT + "; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 4 12; -fx-background-radius: 4; -fx-cursor: hand;";
                adminBtn.setStyle(adminStyle);
                adminBtn.setOnAction(e -> showAdmin());
            }
            right.getChildren().add(userLabel);
            if (adminBtn != null) right.getChildren().add(adminBtn);
        } else {
            Button loginBtn = new Button("Sign In");
            loginBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #A5D6A7; -fx-border-radius: 4; -fx-text-fill: #A5D6A7; -fx-font-size: 12px; -fx-padding: 4 14; -fx-cursor: hand;");
            loginBtn.setOnAction(e -> showAccount());
            right.getChildren().add(loginBtn);
        }

        nav.getChildren().addAll(brand, navItems, spacer, right);
        return nav;
    }

    private Button navButton(String text, boolean active) {
        Button b = new Button(text);
        String base = active
            ? "-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 16; -fx-background-radius: 0; -fx-cursor: hand; -fx-border-width: 0 0 2 0; -fx-border-color: #A5D6A7;"
            : "-fx-background-color: transparent; -fx-text-fill: #C8E6C9; -fx-font-size: 13px; -fx-padding: 10 16; -fx-background-radius: 0; -fx-cursor: hand;";
        String hover = "-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 10 16; -fx-background-radius: 0; -fx-cursor: hand;";
        b.setStyle(base);
        if (!active) {
            b.setOnMouseEntered(e -> b.setStyle(hover));
            b.setOnMouseExited(e -> b.setStyle(base));
        }
        return b;
    }

    /** Standard page wrapper with nav on top */
    public BorderPane pageWrapper(String activeSection) {
        BorderPane bp = new BorderPane();
        bp.setStyle("-fx-background-color: " + BG_GREY + ";");
        bp.setTop(buildNavBar(activeSection));
        return bp;
    }

    /** Card style pane */
    public static VBox card() {
        VBox v = new VBox(10);
        v.setStyle(
            "-fx-background-color: " + BG_WHITE + ";" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2);" +
            "-fx-padding: 16;"
        );
        return v;
    }

    public static void showAlert(String title, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
