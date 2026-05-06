package com.ngoconnect.ui;

import com.ngoconnect.model.Organisation;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class SearchView {

    private final MainApp app;
    private final BorderPane root;

    public SearchView(MainApp app) {
        this.app = app;
        this.root = app.pageWrapper("Search");
        root.setCenter(buildContent());
    }

    public BorderPane getRoot() { return root; }

    private VBox buildContent() {
        VBox content = new VBox(0);

        // Page header
        VBox header = new VBox(6);
        header.setPadding(new Insets(24, 32, 20, 32));
        header.setStyle("-fx-background-color: " + MainApp.BG_WHITE + ";");
        Label title = new Label("🔍  Search Organisations");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        Label sub = new Label("Search by name, cause, keyword, or location");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");
        header.getChildren().addAll(title, sub);

        // Search bar
        HBox searchBar = buildSearchBar();
        searchBar.setPadding(new Insets(16, 32, 16, 32));
        searchBar.setStyle("-fx-background-color: " + MainApp.BG_WHITE + "; -fx-border-color: " + MainApp.BORDER + "; -fx-border-width: 0 0 1 0;");

        // Results area
        VBox resultsArea = new VBox(12);
        resultsArea.setPadding(new Insets(20, 32, 20, 32));

        Label placeholder = new Label("Enter a search term above to find organisations.");
        placeholder.setStyle("-fx-font-size: 14px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");
        placeholder.setTextAlignment(TextAlignment.CENTER);

        VBox resultsBox = new VBox(10);
        resultsBox.getChildren().add(placeholder);

        // Pre-fill if navigated from home
        String prefill = System.getProperty("ngoconnect.prefill", "");
        System.clearProperty("ngoconnect.prefill");

        TextField kwField = (TextField) ((HBox) searchBar.getChildren().get(0)).getChildren().get(0);
        TextField locField = (TextField) ((HBox) searchBar.getChildren().get(0)).getChildren().get(1);
        Button goBtn = (Button) ((HBox) searchBar.getChildren().get(0)).getChildren().get(2);

        Runnable doSearch = () -> {
            String kw  = kwField.getText().trim();
            String loc = locField.getText().trim();
            List<Organisation> results;

            if (kw.isEmpty() && loc.isEmpty()) {
                results = app.getOrgService().getAll();
            } else if (!kw.isEmpty() && !loc.isEmpty()) {
                results = app.getOrgService().search(kw).stream()
                        .filter(o -> o.getLocation().toLowerCase().contains(loc.toLowerCase())
                                  || o.getState().toLowerCase().contains(loc.toLowerCase()))
                        .toList();
            } else if (!kw.isEmpty()) {
                results = app.getOrgService().search(kw);
            } else {
                results = app.getOrgService().getAll().stream()
                        .filter(o -> o.getLocation().toLowerCase().contains(loc.toLowerCase())
                                  || o.getState().toLowerCase().contains(loc.toLowerCase()))
                        .toList();
            }

            resultsBox.getChildren().clear();
            if (results.isEmpty()) {
                Label none = new Label("No organisations found. Try a different search.");
                none.setStyle("-fx-font-size: 14px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");
                resultsBox.getChildren().add(none);
            } else {
                Label cnt = new Label(results.size() + " result(s) found");
                cnt.setStyle("-fx-font-size: 13px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");
                resultsBox.getChildren().add(cnt);
                for (Organisation o : results) {
                    resultsBox.getChildren().add(buildResultRow(o));
                }
            }
        };

        goBtn.setOnAction(e -> doSearch.run());
        kwField.setOnAction(e -> doSearch.run());

        if (!prefill.isEmpty()) {
            kwField.setText(prefill);
            doSearch.run();
        }

        ScrollPane scroll = new ScrollPane(resultsBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        resultsArea.getChildren().add(scroll);
        VBox.setVgrow(resultsArea, Priority.ALWAYS);

        content.getChildren().addAll(header, searchBar, resultsArea);
        VBox.setVgrow(content, Priority.ALWAYS);
        return content;
    }

    private HBox buildSearchBar() {
        HBox outer = new HBox();
        HBox inner = new HBox(10);
        inner.setAlignment(Pos.CENTER_LEFT);

        TextField kw = new TextField();
        kw.setPromptText("🔎  Keyword, cause, or organisation name...");
        kw.setPrefWidth(380);
        kw.setStyle("-fx-font-size: 13px; -fx-padding: 9 12; -fx-background-radius: 6; -fx-border-color: " + MainApp.BORDER + "; -fx-border-radius: 6;");

        TextField loc = new TextField();
        loc.setPromptText("📍  City or state...");
        loc.setPrefWidth(220);
        loc.setStyle("-fx-font-size: 13px; -fx-padding: 9 12; -fx-background-radius: 6; -fx-border-color: " + MainApp.BORDER + "; -fx-border-radius: 6;");

        Button go = MainApp.primaryButton("Search");

        inner.getChildren().addAll(kw, loc, go);
        outer.getChildren().add(inner);
        return outer;
    }

    private HBox buildResultRow(Organisation org) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 14 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 4, 0, 0, 1);" +
            "-fx-cursor: hand;"
        );

        // Icon letter
        Label icon = new Label(org.getName().substring(0, 1).toUpperCase());
        icon.setPrefSize(46, 46);
        icon.setMinSize(46, 46);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color: " + MainApp.GREEN_PALE + "; -fx-background-radius: 23; -fx-text-fill: " + MainApp.GREEN_DARK + "; -fx-font-size: 18px; -fx-font-weight: bold;");

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);

        HBox nameRow = new HBox(6);
        nameRow.setAlignment(Pos.CENTER_LEFT);
        Label name = new Label(org.getName());
        name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        if (org.isVerified()) {
            Label badge = new Label("✔ Verified");
            badge.setStyle("-fx-background-color: " + MainApp.GREEN_LIGHT + "; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 1 6; -fx-background-radius: 10;");
            nameRow.getChildren().addAll(name, badge);
        } else {
            nameRow.getChildren().add(name);
        }

        Label meta = new Label(org.getType().getLabel() + " · " + org.getLocation() + ", " + org.getState()
                + (org.getAverageRating() > 0 ? "  ★ " + String.format("%.1f", org.getAverageRating()) : ""));
        meta.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");

        String desc = org.getDescription();
        if (desc.length() > 140) desc = desc.substring(0, 140) + "…";
        Label descL = new Label(desc);
        descL.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_MED + ";");
        descL.setWrapText(true);

        info.getChildren().addAll(nameRow, meta, descL);

        Button view = MainApp.outlineButton("View →");
        view.setOnAction(e -> app.showOrgProfile(org));

        row.getChildren().addAll(icon, info, view);

        row.setOnMouseEntered(e -> row.setStyle(
            "-fx-background-color: " + MainApp.GREEN_PALE + ";" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 14 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 6, 0, 0, 2);" +
            "-fx-cursor: hand;"
        ));
        row.setOnMouseExited(e -> row.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 14 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 4, 0, 0, 1);" +
            "-fx-cursor: hand;"
        ));
        row.setOnMouseClicked(e -> app.showOrgProfile(org));

        return row;
    }
}
