package com.ngoconnect.ui;

import com.ngoconnect.model.Category;
import com.ngoconnect.model.Organisation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class BrowseView {

    private final MainApp app;
    private final BorderPane root;

    public BrowseView(MainApp app) {
        this.app = app;
        this.root = app.pageWrapper("Browse");
        root.setCenter(buildContent());
    }

    public BorderPane getRoot() { return root; }

    private SplitPane buildContent() {
        SplitPane split = new SplitPane();
        split.setStyle("-fx-background-color: " + MainApp.BG_GREY + ";");

        // Left sidebar filters
        VBox sidebar = buildSidebar();
        sidebar.setMinWidth(220);
        sidebar.setMaxWidth(220);

        // Right results panel
        VBox results = new VBox(12);
        results.setPadding(new Insets(20, 24, 20, 16));

        Label heading = new Label("All Organisations");
        heading.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");

        VBox listBox = new VBox(10);
        ScrollPane scroll = new ScrollPane(listBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        results.getChildren().addAll(heading, scroll);
        VBox.setVgrow(results, Priority.ALWAYS);

        // Initially show all
        Runnable showAll = () -> {
            heading.setText("All Organisations (" + app.getOrgService().getAll().size() + ")");
            populateList(listBox, app.getOrgService().getAll());
        };
        showAll.run();

        // Wire sidebar callbacks
        buildSidebarCallbacks(sidebar, listBox, heading);

        split.getItems().addAll(sidebar, results);
        split.setDividerPositions(0.20);
        return split;
    }

    private VBox buildSidebar() {
        VBox sb = new VBox(0);
        sb.setStyle("-fx-background-color: white; -fx-border-color: " + MainApp.BORDER + "; -fx-border-width: 0 1 0 0;");
        sb.setPrefWidth(220);

        Label title = new Label("Filter By");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + "; -fx-padding: 18 16 10 16;");
        sb.getChildren().add(title);

        // All button
        sb.getChildren().add(filterBtn("📋  All Organisations", "ALL"));

        // Verified
        sb.getChildren().add(sectionLabel("Status"));
        sb.getChildren().add(filterBtn("✅  Verified Only", "VERIFIED"));

        // Top Rated
        sb.getChildren().add(filterBtn("⭐  Top Rated", "TOP_RATED"));

        // Categories
        sb.getChildren().add(sectionLabel("Category"));
        for (Category cat : Category.values()) {
            sb.getChildren().add(filterBtn("  " + cat.getDisplayName(), "CAT:" + cat.name()));
        }

        return sb;
    }

    private void buildSidebarCallbacks(VBox sidebar, VBox listBox, Label heading) {
        for (javafx.scene.Node node : sidebar.getChildren()) {
            if (node instanceof Button btn) {
                String tag = (String) btn.getUserData();
                if (tag == null) continue;
                btn.setOnAction(e -> {
                    // Reset all button styles
                    for (javafx.scene.Node n : sidebar.getChildren()) {
                        if (n instanceof Button b && b.getUserData() != null) {
                            b.setStyle(filterBtnStyle(false));
                        }
                    }
                    btn.setStyle(filterBtnStyle(true));

                    List<Organisation> result;
                    String label;
                    if ("ALL".equals(tag)) {
                        result = app.getOrgService().getAll();
                        label = "All Organisations";
                    } else if ("VERIFIED".equals(tag)) {
                        result = app.getOrgService().getAll().stream()
                                .filter(Organisation::isVerified).toList();
                        label = "Verified Organisations";
                    } else if ("TOP_RATED".equals(tag)) {
                        result = app.getOrgService().getTopRated(20);
                        label = "Top Rated";
                    } else if (tag.startsWith("CAT:")) {
                        Category cat = Category.valueOf(tag.substring(4));
                        result = app.getOrgService().filterByCategory(cat);
                        label = cat.getDisplayName();
                    } else {
                        result = app.getOrgService().getAll();
                        label = "All Organisations";
                    }
                    heading.setText(label + " (" + result.size() + ")");
                    populateList(listBox, result);
                });
            }
        }
    }

    private Button filterBtn(String text, String tag) {
        Button b = new Button(text);
        b.setUserData(tag);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setStyle(filterBtnStyle(false));
        return b;
    }

    private String filterBtnStyle(boolean active) {
        if (active) return "-fx-background-color: " + MainApp.GREEN_PALE + "; -fx-text-fill: " + MainApp.GREEN_DARK +
                "; -fx-font-size: 12px; -fx-padding: 8 16; -fx-background-radius: 0; -fx-cursor: hand; -fx-font-weight: bold; -fx-border-color: " + MainApp.GREEN_LIGHT + "; -fx-border-width: 0 0 0 3;";
        return "-fx-background-color: transparent; -fx-text-fill: " + MainApp.TEXT_MED +
                "; -fx-font-size: 12px; -fx-padding: 8 16; -fx-background-radius: 0; -fx-cursor: hand;";
    }

    private Label sectionLabel(String text) {
        Label l = new Label(text.toUpperCase());
        l.setStyle("-fx-font-size: 10px; -fx-text-fill: " + MainApp.TEXT_LIGHT + "; -fx-padding: 14 16 4 16; -fx-font-weight: bold;");
        return l;
    }

    private void populateList(VBox listBox, List<Organisation> orgs) {
        listBox.getChildren().clear();
        if (orgs.isEmpty()) {
            Label none = new Label("No organisations found in this category.");
            none.setStyle("-fx-font-size: 14px; -fx-text-fill: " + MainApp.TEXT_LIGHT + "; -fx-padding: 20 0;");
            listBox.getChildren().add(none);
            return;
        }
        for (Organisation org : orgs) {
            listBox.getChildren().add(buildRow(org));
        }
    }

    private HBox buildRow(Organisation org) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 14 16; -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.06),4,0,0,1); -fx-cursor: hand;");

        Label icon = new Label(org.getName().substring(0, 1).toUpperCase());
        icon.setPrefSize(42, 42);
        icon.setMinSize(42, 42);
        icon.setAlignment(Pos.CENTER);
        icon.setStyle("-fx-background-color: " + MainApp.GREEN_PALE + "; -fx-background-radius: 21; -fx-text-fill: " + MainApp.GREEN_DARK + "; -fx-font-size: 16px; -fx-font-weight: bold;");

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);

        HBox nameRow = new HBox(6);
        nameRow.setAlignment(Pos.CENTER_LEFT);
        Label name = new Label(org.getName());
        name.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        if (org.isVerified()) {
            Label badge = new Label("✔");
            badge.setStyle("-fx-background-color: " + MainApp.GREEN_LIGHT + "; -fx-text-fill: white; -fx-font-size: 9px; -fx-padding: 1 5; -fx-background-radius: 8;");
            nameRow.getChildren().addAll(name, badge);
        } else {
            nameRow.getChildren().add(name);
        }

        Label meta = new Label(org.getType().getLabel() + " · " + org.getLocation() + ", " + org.getState());
        meta.setStyle("-fx-font-size: 11px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");

        // Category chips
        HBox chips = new HBox(4);
        for (Category c : org.getCategories()) {
            Label chip = new Label(c.getDisplayName());
            chip.setStyle("-fx-background-color: " + MainApp.GREEN_PALE + "; -fx-text-fill: " + MainApp.GREEN_DARK + "; -fx-font-size: 10px; -fx-padding: 1 7; -fx-background-radius: 10;");
            chips.getChildren().add(chip);
            if (chips.getChildren().size() >= 3) break;
        }

        info.getChildren().addAll(nameRow, meta, chips);

        VBox right = new VBox(6);
        right.setAlignment(Pos.CENTER_RIGHT);
        if (org.getAverageRating() > 0) {
            Label rating = new Label("★ " + String.format("%.1f", org.getAverageRating()));
            rating.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.ACCENT + ";");
            right.getChildren().add(rating);
        }
        Button view = MainApp.outlineButton("View →");
        view.setOnAction(e -> app.showOrgProfile(org));
        right.getChildren().add(view);

        row.getChildren().addAll(icon, info, right);
        row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: " + MainApp.GREEN_PALE + "; -fx-background-radius: 8; -fx-padding: 14 16; -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.10),6,0,0,2); -fx-cursor: hand;"));
        row.setOnMouseExited(e -> row.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 14 16; -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.06),4,0,0,1); -fx-cursor: hand;"));
        row.setOnMouseClicked(e -> app.showOrgProfile(org));
        return row;
    }
}
