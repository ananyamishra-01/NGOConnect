package com.ngoconnect.ui;

import com.ngoconnect.model.*;
import com.ngoconnect.util.Validator;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.List;

public class AdminView {

    private final MainApp app;
    private final BorderPane root;

    public AdminView(MainApp app) {
        this.app = app;
        this.root = app.pageWrapper("Account");
        if (app.getCurrentUser() == null || !app.getCurrentUser().isAdmin()) {
            root.setCenter(buildAccessDenied());
        } else {
            root.setCenter(buildContent());
        }
    }

    public BorderPane getRoot() { return root; }

    private VBox buildAccessDenied() {
        VBox v = new VBox(10);
        v.setAlignment(Pos.CENTER);
        Label l = new Label("⛔  Admin access required.");
        l.setStyle("-fx-font-size: 16px; -fx-text-fill: #D32F2F;");
        v.getChildren().add(l);
        return v;
    }

    private SplitPane buildContent() {
        SplitPane split = new SplitPane();

        // Sidebar
        VBox sidebar = buildSidebar();
        sidebar.setMinWidth(200);
        sidebar.setMaxWidth(200);

        // Main area with tab pane
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab statsTab = new Tab("📊  Dashboard", buildStatsPanel());
        Tab orgsTab = new Tab("🏢  Organisations", buildOrgsPanel());
        Tab addTab = new Tab("➕  Register Org", buildRegisterPanel());

        tabs.getTabs().addAll(statsTab, orgsTab, addTab);

        split.getItems().addAll(tabs);
        split.setDividerPositions(0.18);
        return split;
    }

    private VBox buildSidebar() {
        VBox sb = new VBox(0);
        sb.setStyle("-fx-background-color: " + MainApp.GREEN_DARK + ";");
        Label title = new Label("⚙  Admin Panel");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 16 14 14 14;");
        sb.getChildren().add(title);
        Label user = new Label("👤  " + app.getCurrentUser().getUsername());
        user.setStyle("-fx-text-fill: #A5D6A7; -fx-font-size: 12px; -fx-padding: 0 14 16 14;");
        sb.getChildren().add(user);
        Button backBtn = new Button("← Back to App");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #C8E6C9; -fx-font-size: 12px; -fx-padding: 8 14; -fx-cursor: hand;");
        backBtn.setOnAction(e -> app.showHome());
        sb.getChildren().add(backBtn);
        return sb;
    }

    // ── Stats Dashboard ───────────────────────────────────────

    private ScrollPane buildStatsPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(24, 28, 28, 28));

        Label head = new Label("Dashboard Statistics");
        head.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");

        long total    = app.getOrgService().count();
        long verified = app.getOrgService().getAll().stream().filter(Organisation::isVerified).count();
        long reviews  = app.getOrgService().getAll().stream().mapToLong(o -> o.getReviews().size()).sum();
        long events   = app.getOrgService().getAll().stream().mapToLong(o -> o.getEvents().size()).sum();
        long vols     = app.getOrgService().getAll().stream().mapToLong(Organisation::getVolunteerCount).sum();

        FlowPane statsGrid = new FlowPane(16, 16);
        statsGrid.getChildren().addAll(
            statCard("🏢", String.valueOf(total),    "Total Organisations"),
            statCard("✅", String.valueOf(verified),  "Verified"),
            statCard("📅", String.valueOf(events),    "Events"),
            statCard("⭐", String.valueOf(reviews),   "Reviews"),
            statCard("👥", String.format("%,d", vols), "Volunteers")
        );

        // Category distribution
        VBox catSection = MainApp.card();
        catSection.getChildren().add(sectionHead("Category Distribution"));
        for (Category c : Category.values()) {
            long cnt = app.getOrgService().getAll().stream()
                    .filter(o -> o.getCategories().contains(c)).count();
            if (cnt == 0) continue;
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            Label lbl = new Label(c.getDisplayName());
            lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_MED + "; -fx-min-width: 260;");
            ProgressBar bar = new ProgressBar((double) cnt / total);
            bar.setPrefWidth(180);
            bar.setStyle("-fx-accent: " + MainApp.GREEN_LIGHT + ";");
            Label cntL = new Label(cnt + " org(s)");
            cntL.setStyle("-fx-font-size: 11px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");
            row.getChildren().addAll(lbl, bar, cntL);
            catSection.getChildren().add(row);
        }

        panel.getChildren().addAll(head, statsGrid, catSection);
        ScrollPane sp = new ScrollPane(panel);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return sp;
    }

    private VBox statCard(String icon, String value, String label) {
        VBox card = new VBox(6);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16, 30, 16, 30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.08),6,0,0,2);");
        Label ico = new Label(icon);
        ico.setStyle("-fx-font-size: 22px;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.GREEN_MID + ";");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");
        card.getChildren().addAll(ico, val, lbl);
        return card;
    }

    // ── Organisations List ────────────────────────────────────

    private ScrollPane buildOrgsPanel() {
        VBox panel = new VBox(16);
        panel.setPadding(new Insets(20, 24, 24, 24));

        Label head = new Label("All Organisations");
        head.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");

        VBox list = new VBox(8);
        for (Organisation org : app.getOrgService().getAll()) {
            list.getChildren().add(buildAdminOrgRow(org, list));
        }

        panel.getChildren().addAll(head, list);
        ScrollPane sp = new ScrollPane(panel);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return sp;
    }

    private HBox buildAdminOrgRow(Organisation org, VBox listRef) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 12 16; -fx-effect: dropshadow(gaussian,rgba(0,0,0,0.06),4,0,0,1);");

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);
        HBox nameRow = new HBox(8);
        nameRow.setAlignment(Pos.CENTER_LEFT);
        Label name = new Label(org.getName());
        name.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        Label status = new Label(org.isVerified() ? "✔ Verified" : "Unverified");
        status.setStyle("-fx-background-color: " + (org.isVerified() ? MainApp.GREEN_LIGHT : "#EF9A9A") + "; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 1 6; -fx-background-radius: 8;");
        nameRow.getChildren().addAll(name, status);
        Label meta = new Label(org.getType().getLabel() + " · " + org.getLocation() + ", " + org.getState() + " · ID: " + org.getId());
        meta.setStyle("-fx-font-size: 11px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");
        info.getChildren().addAll(nameRow, meta);

        HBox actions = new HBox(6);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button viewBtn = MainApp.outlineButton("View");
        viewBtn.setOnAction(e -> app.showOrgProfile(org));

        Button verifyBtn = new Button(org.isVerified() ? "Unverify" : "Verify");
        verifyBtn.setStyle("-fx-background-color: " + (org.isVerified() ? "#FF8F00" : MainApp.GREEN_LIGHT) + "; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 5 12; -fx-background-radius: 5; -fx-cursor: hand;");
        verifyBtn.setOnAction(e -> {
            app.getOrgService().verify(org.getId(), !org.isVerified());
            app.showAdmin(); // refresh
        });

        Button addEventBtn = MainApp.outlineButton("+ Event");
        addEventBtn.setOnAction(e -> showAddEventDialog(org));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #D32F2F; -fx-border-color: #D32F2F; -fx-border-width: 1; -fx-font-size: 11px; -fx-padding: 5 12; -fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete '" + org.getName() + "'? This cannot be undone.", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(bt -> {
                if (bt == ButtonType.YES) {
                    app.getOrgService().getAll().removeIf(o -> o.getId().equals(org.getId()));
                    app.showAdmin();
                }
            });
        });

        actions.getChildren().addAll(viewBtn, verifyBtn, addEventBtn, deleteBtn);
        row.getChildren().addAll(info, actions);
        return row;
    }

    // ── Register New Organisation ─────────────────────────────

    private ScrollPane buildRegisterPanel() {
        VBox panel = new VBox(14);
        panel.setPadding(new Insets(24, 28, 28, 28));
        panel.setMaxWidth(680);

        Label head = new Label("Register New Organisation");
        head.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");

        TextField nameF      = styledField("Organisation Name *");
        TextArea descF       = styledArea("Description *");
        TextField locationF  = styledField("City / Location *");
        TextField stateF     = styledField("State *");
        TextField yearF      = styledField("Founded Year *");
        TextField areaF      = styledField("Area of Work *");
        TextField phoneF     = styledField("Phone");
        TextField emailF     = styledField("Email *");
        TextField websiteF   = styledField("Website");
        TextField addressF   = styledField("Address");
        TextField regNumF    = styledField("Registration Number (optional)");
        TextField volCountF  = styledField("Volunteer Count");

        ComboBox<String> typeBox = new ComboBox<>(FXCollections.observableArrayList(
            "Non-Governmental Organisation", "Self-Help Group", "Charitable Trust", "Registered Society", "Foundation"));
        typeBox.setPromptText("Organisation Type *");
        typeBox.setMaxWidth(Double.MAX_VALUE);
        typeBox.setStyle("-fx-font-size: 13px;");

        // Category checkboxes
        Label catLabel = new Label("Categories:");
        catLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
        FlowPane catPane = new FlowPane(8, 6);
        CheckBox[] catBoxes = new CheckBox[Category.values().length];
        for (int i = 0; i < Category.values().length; i++) {
            catBoxes[i] = new CheckBox(Category.values()[i].getDisplayName());
            catBoxes[i].setStyle("-fx-font-size: 12px;");
            catPane.getChildren().add(catBoxes[i]);
        }

        Label errorLbl = new Label("");
        errorLbl.setStyle("-fx-text-fill: #D32F2F; -fx-font-size: 12px;");

        Button registerBtn = MainApp.primaryButton("Register Organisation");
        registerBtn.setOnAction(e -> {
            try {
                String name    = nameF.getText().trim();
                String desc    = descF.getText().trim();
                String loc     = locationF.getText().trim();
                String state   = stateF.getText().trim();
                int year       = Integer.parseInt(yearF.getText().trim());
                String area    = areaF.getText().trim();
                String phone   = phoneF.getText().trim();
                String email   = emailF.getText().trim();
                String website = websiteF.getText().isEmpty() ? "N/A" : websiteF.getText().trim();
                String address = addressF.getText().trim();

                if (typeBox.getValue() == null) throw new Exception("Please select organisation type.");
                OrgType type = OrgType.values()[typeBox.getSelectionModel().getSelectedIndex()];

                ContactInfo ci = new ContactInfo(phone, email, website, address);
                Organisation org = app.getOrgService().register(name, desc, type, loc, state, ci, year, area);

                if (!regNumF.getText().isBlank()) org.setRegistrationNumber(regNumF.getText().trim());
                if (!volCountF.getText().isBlank()) org.setVolunteerCount(Integer.parseInt(volCountF.getText().trim()));

                for (int i = 0; i < catBoxes.length; i++) {
                    if (catBoxes[i].isSelected()) org.addCategory(Category.values()[i]);
                }
                app.getOrgService().verify(org.getId(), false);

                MainApp.showAlert("Organisation Registered",
                    "✔  " + name + " registered successfully!\nID: " + org.getId(),
                    Alert.AlertType.INFORMATION);

                // Clear form
                nameF.clear(); descF.clear(); locationF.clear(); stateF.clear();
                yearF.clear(); areaF.clear(); phoneF.clear(); emailF.clear();
                websiteF.clear(); addressF.clear(); regNumF.clear(); volCountF.clear();
                typeBox.getSelectionModel().clearSelection();
                for (CheckBox cb : catBoxes) cb.setSelected(false);
                errorLbl.setText("");

            } catch (Exception ex) {
                errorLbl.setText("Error: " + ex.getMessage());
            }
        });

        panel.getChildren().addAll(
            head,
            sectionHead("Basic Information"),
            new Label("Name:"), nameF,
            new Label("Description:"), descF,
            new Label("Type:"), typeBox,
            sectionHead("Location"),
            new Label("City/Location:"), locationF,
            new Label("State:"), stateF,
            new Label("Founded Year:"), yearF,
            new Label("Area of Work:"), areaF,
            sectionHead("Contact"),
            new Label("Phone:"), phoneF,
            new Label("Email:"), emailF,
            new Label("Website:"), websiteF,
            new Label("Address:"), addressF,
            sectionHead("Additional"),
            new Label("Registration Number:"), regNumF,
            new Label("Volunteer Count:"), volCountF,
            catLabel, catPane,
            errorLbl,
            registerBtn
        );

        ScrollPane sp = new ScrollPane(panel);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return sp;
    }

    private void showAddEventDialog(Organisation org) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Event to " + org.getName());
        dlg.setHeaderText("New Event Details");

        VBox form = new VBox(10);
        form.setPadding(new Insets(10));

        TextField titleF = styledField("Event Title *");
        TextArea descF   = styledArea("Description");
        DatePicker dateP = new DatePicker(LocalDate.now().plusDays(7));
        TextField locF   = styledField("Location *");
        ComboBox<String> typeBox = new ComboBox<>(FXCollections.observableArrayList(
            "Drive", "Campaign", "Workshop", "Awareness Programme", "Fundraiser", "Training"));
        typeBox.setPromptText("Event Type *");
        typeBox.setMaxWidth(Double.MAX_VALUE);

        form.getChildren().addAll(
            new Label("Title:"), titleF,
            new Label("Description:"), descF,
            new Label("Date:"), dateP,
            new Label("Location:"), locF,
            new Label("Type:"), typeBox
        );
        dlg.getDialogPane().setContent(form);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    Event.EventType etype = Event.EventType.values()[typeBox.getSelectionModel().getSelectedIndex()];
                    Event ev = new Event(titleF.getText().trim(), descF.getText().trim(),
                            dateP.getValue(), locF.getText().trim(), etype);
                    app.getOrgService().addEventToOrg(org.getId(), ev);
                    MainApp.showAlert("Event Added", "✔  Event added to " + org.getName(), Alert.AlertType.INFORMATION);
                } catch (Exception ex) {
                    MainApp.showAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    // ── Helpers ───────────────────────────────────────────────

    private Label sectionHead(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.GREEN_DARK + "; -fx-padding: 8 0 2 0;");
        return l;
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setStyle("-fx-font-size: 13px; -fx-padding: 8 12; -fx-background-radius: 6; -fx-border-color: " + MainApp.BORDER + "; -fx-border-radius: 6;");
        return tf;
    }

    private TextArea styledArea(String prompt) {
        TextArea ta = new TextArea();
        ta.setPromptText(prompt);
        ta.setPrefRowCount(3);
        ta.setMaxWidth(Double.MAX_VALUE);
        ta.setStyle("-fx-font-size: 13px; -fx-padding: 8 12; -fx-background-radius: 6;");
        return ta;
    }
}
