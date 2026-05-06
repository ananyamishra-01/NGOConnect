package com.ngoconnect.ui;

import com.ngoconnect.model.Organisation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.util.List;

public class HomeView {

    private final MainApp app;
    private final BorderPane root;

    public HomeView(MainApp app) {
        this.app = app;
        this.root = app.pageWrapper("Home");

        ScrollPane scroll = new ScrollPane(buildContent());
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        root.setCenter(scroll);
    }

    public BorderPane getRoot() { return root; }

    private VBox buildContent() {
        VBox content = new VBox(0);

        // Hero banner
        content.getChildren().add(buildHero());

        // Stats row
        content.getChildren().add(buildStatsRow());

        // Featured / Top-rated organisations
        content.getChildren().add(buildFeaturedSection());

        // Quick search bar at bottom
        content.getChildren().add(buildQuickSearch());

        return content;
    }

    private VBox buildHero() {
        VBox hero = new VBox(14);
        hero.setAlignment(Pos.CENTER);
        hero.setPadding(new Insets(60, 40, 50, 40));
        hero.setStyle("-fx-background-color: linear-gradient(to bottom right, " +
                MainApp.GREEN_DARK + ", " + MainApp.GREEN_MID + ");");

        Label title = new Label("Discover • Collaborate • Make a Difference");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        title.setWrapText(true);
        title.setTextAlignment(TextAlignment.CENTER);

        Label sub = new Label("Find NGOs, Self-Help Groups, and social organisations across India.\n" +
                              "Connect with causes you care about.");
        sub.setStyle("-fx-text-fill: #C8E6C9; -fx-font-size: 15px;");
        sub.setTextAlignment(TextAlignment.CENTER);
        sub.setWrapText(true);

        HBox btns = new HBox(12);
        btns.setAlignment(Pos.CENTER);
        Button explore = MainApp.primaryButton("🔍  Explore Organisations");
        explore.setStyle(explore.getStyle() + " -fx-background-color: white; -fx-text-fill: " + MainApp.GREEN_DARK + ";");
        explore.setOnAction(e -> app.showBrowse());

        Button search = MainApp.outlineButton("🔎  Search by Keyword");
        search.setStyle(search.getStyle().replace(MainApp.GREEN_LIGHT, "white").replace(MainApp.GREEN_MID, "white") + " -fx-text-fill: white;");
        search.setOnAction(e -> app.showSearch());

        btns.getChildren().addAll(explore, search);
        hero.getChildren().addAll(title, sub, btns);
        return hero;
    }

    private HBox buildStatsRow() {
        long total    = app.getOrgService().count();
        long verified = app.getOrgService().getAll().stream().filter(Organisation::isVerified).count();
        long events   = app.getOrgService().getAll().stream().mapToLong(o -> o.getEvents().size()).sum();
        long vols     = app.getOrgService().getAll().stream().mapToLong(Organisation::getVolunteerCount).sum();

        HBox row = new HBox(0);
        row.setStyle("-fx-background-color: " + MainApp.BG_WHITE + ";");
        row.setAlignment(Pos.CENTER);

        row.getChildren().addAll(
            statTile("🏢", String.valueOf(total),    "Organisations"),
            sep(),
            statTile("✅", String.valueOf(verified),  "Verified"),
            sep(),
            statTile("📅", String.valueOf(events),    "Events Listed"),
            sep(),
            statTile("🤝", String.format("%,d", vols), "Volunteers")
        );
        return row;
    }

    private VBox statTile(String icon, String value, String label) {
        VBox tile = new VBox(4);
        tile.setAlignment(Pos.CENTER);
        tile.setPadding(new Insets(20, 50, 20, 50));

        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size: 22px;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.GREEN_MID + ";");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");

        tile.getChildren().addAll(ico, val, lbl);
        return tile;
    }

    private Region sep() {
        Region r = new Region();
        r.setStyle("-fx-background-color: " + MainApp.BORDER + "; -fx-pref-width: 1px; -fx-min-height: 50px;");
        return r;
    }

    private VBox buildFeaturedSection() {
        VBox section = new VBox(16);
        section.setPadding(new Insets(30, 32, 20, 32));

        Label heading = new Label("⭐  Top-Rated Organisations");
        heading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");

        List<Organisation> topRated = app.getOrgService().getTopRated(6);
        if (topRated.isEmpty()) {
            topRated = app.getOrgService().getAll().stream().limit(6).toList();
        }

        FlowPane grid = new FlowPane(16, 16);
        grid.setPrefWrapLength(1050);

        for (Organisation org : topRated) {
            grid.getChildren().add(buildOrgCard(org));
        }

        Button viewAll = MainApp.outlineButton("View All Organisations →");
        viewAll.setOnAction(e -> app.showBrowse());

        HBox footer = new HBox(viewAll);
        footer.setAlignment(Pos.CENTER_RIGHT);

        section.getChildren().addAll(heading, grid, footer);
        return section;
    }

    private VBox buildOrgCard(Organisation org) {
        VBox card = new VBox(8);
        card.setPrefWidth(310);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2);" +
            "-fx-padding: 14;" +
            "-fx-cursor: hand;"
        );

        // Header row: name + verified badge
        HBox header = new HBox(6);
        header.setAlignment(Pos.CENTER_LEFT);
        Label name = new Label(org.getName());
        name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + "; -fx-wrap-text: true;");
        name.setMaxWidth(230);
        if (org.isVerified()) {
            Label badge = new Label("✔");
            badge.setStyle("-fx-background-color: " + MainApp.GREEN_LIGHT + "; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 1 5; -fx-background-radius: 10;");
            header.getChildren().addAll(name, badge);
        } else {
            header.getChildren().add(name);
        }

        // Type + location
        Label meta = new Label(org.getType().getLabel() + " · " + org.getLocation() + ", " + org.getState());
        meta.setStyle("-fx-font-size: 11px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");

        // Rating
        double rating = org.getAverageRating();
        Label stars = new Label(rating > 0
                ? "★".repeat((int) Math.round(rating)) + "  " + String.format("%.1f", rating)
                : "No reviews yet");
        stars.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.ACCENT + ";");

        // Description snippet
        String desc = org.getDescription();
        if (desc.length() > 100) desc = desc.substring(0, 100) + "…";
        Label descLabel = new Label(desc);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_MED + ";");
        descLabel.setWrapText(true);

        Button viewBtn = MainApp.outlineButton("View Profile");
        viewBtn.setOnAction(e -> app.showOrgProfile(org));
        HBox.setHgrow(viewBtn, Priority.NEVER);

        card.getChildren().addAll(header, meta, stars, descLabel, viewBtn);

        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: " + MainApp.GREEN_PALE + ";" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 8, 0, 0, 3);" +
            "-fx-padding: 14;" +
            "-fx-cursor: hand;"
        ));
        card.setOnMouseExited(e -> card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2);" +
            "-fx-padding: 14;" +
            "-fx-cursor: hand;"
        ));
        card.setOnMouseClicked(e -> app.showOrgProfile(org));

        return card;
    }

    private HBox buildQuickSearch() {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(24, 32, 32, 32));
        bar.setStyle("-fx-background-color: " + MainApp.BG_GREY + ";");

        Label lbl = new Label("Quick Search:");
        lbl.setStyle("-fx-font-size: 14px; -fx-text-fill: " + MainApp.TEXT_MED + ";");

        TextField tf = new TextField();
        tf.setPromptText("Enter keyword, city, or cause...");
        tf.setPrefWidth(350);
        tf.setStyle("-fx-font-size: 13px; -fx-padding: 8 12; -fx-background-radius: 6; -fx-border-color: " + MainApp.BORDER + "; -fx-border-radius: 6;");

        Button go = MainApp.primaryButton("Search →");
        go.setOnAction(e -> {
            String kw = tf.getText().trim();
            if (!kw.isEmpty()) {
                app.showSearch();
                // Pass keyword via system property trick for SearchView to pick up
                System.setProperty("ngoconnect.prefill", kw);
            }
        });
        tf.setOnAction(go.getOnAction());

        bar.getChildren().addAll(lbl, tf, go);
        return bar;
    }
}
