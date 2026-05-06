package com.ngoconnect.ui;

import com.ngoconnect.model.Category;
import com.ngoconnect.model.Organisation;
import com.ngoconnect.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class AccountView {

    private final MainApp app;
    private final BorderPane root;

    public AccountView(MainApp app) {
        this.app = app;
        this.root = app.pageWrapper("Account");
        root.setCenter(buildContent());
    }

    public BorderPane getRoot() { return root; }

    private ScrollPane buildContent() {
        VBox page = new VBox(0);
        page.setStyle("-fx-background-color: " + MainApp.BG_GREY + ";");

        if (app.getCurrentUser() == null) {
            page.getChildren().add(buildAuthPanel());
        } else {
            page.getChildren().add(buildProfilePanel());
        }

        ScrollPane scroll = new ScrollPane(page);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    // ── Unauthenticated ───────────────────────────────────────

    private VBox buildAuthPanel() {
        VBox outer = new VBox();
        outer.setAlignment(Pos.TOP_CENTER);
        outer.setPadding(new Insets(50, 20, 20, 20));

        TabPane tabs = new TabPane();
        tabs.setMaxWidth(440);
        tabs.setStyle("-fx-background-color: white; -fx-tab-min-width: 160px;");

        Tab loginTab = new Tab("Sign In", buildLoginForm());
        loginTab.setClosable(false);
        Tab regTab = new Tab("Create Account", buildRegisterForm());
        regTab.setClosable(false);

        tabs.getTabs().addAll(loginTab, regTab);
        outer.getChildren().add(tabs);
        return outer;
    }

    private VBox buildLoginForm() {
        VBox form = new VBox(14);
        form.setPadding(new Insets(24, 28, 28, 28));
        form.setStyle("-fx-background-color: white;");

        Label head = new Label("Welcome back 👋");
        head.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");

        TextField usernameF = styledField("Username");
        PasswordField passwordF = styledPassword("Password");

        Label errorLbl = new Label("");
        errorLbl.setStyle("-fx-text-fill: #D32F2F; -fx-font-size: 12px;");

        Button loginBtn = MainApp.primaryButton("Sign In");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> {
            String u = usernameF.getText().trim();
            String p = passwordF.getText().trim();
            if (u.isEmpty() || p.isEmpty()) { errorLbl.setText("Please fill in all fields."); return; }
            var result = app.getUserService().login(u, p);
            if (result.isPresent()) {
                app.setCurrentUser(result.get());
                app.showAccount();
            } else {
                errorLbl.setText("Invalid username or password.");
            }
        });

        Label hint = new Label("Demo admin: admin / admin123");
        hint.setStyle("-fx-font-size: 11px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");

        form.getChildren().addAll(head, new Label("Username:"), usernameF,
                new Label("Password:"), passwordF, errorLbl, loginBtn, hint);
        return form;
    }

    private VBox buildRegisterForm() {
        VBox form = new VBox(12);
        form.setPadding(new Insets(24, 28, 28, 28));
        form.setStyle("-fx-background-color: white;");

        Label head = new Label("Create an account");
        head.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");

        TextField usernameF = styledField("Username");
        TextField emailF = styledField("Email address");
        PasswordField passwordF = styledPassword("Password (min 6 chars)");

        Label errorLbl = new Label("");
        errorLbl.setStyle("-fx-text-fill: #D32F2F; -fx-font-size: 12px;");

        Button regBtn = MainApp.primaryButton("Create Account");
        regBtn.setMaxWidth(Double.MAX_VALUE);
        regBtn.setOnAction(e -> {
            String u = usernameF.getText().trim();
            String em = emailF.getText().trim();
            String p = passwordF.getText().trim();
            if (u.isEmpty() || em.isEmpty() || p.isEmpty()) { errorLbl.setText("Please fill in all fields."); return; }
            try {
                User user = app.getUserService().register(u, em, p, User.UserRole.VOLUNTEER);
                app.setCurrentUser(user);
                app.showAccount();
            } catch (Exception ex) {
                errorLbl.setText(ex.getMessage());
            }
        });

        form.getChildren().addAll(head,
                new Label("Username:"), usernameF,
                new Label("Email:"), emailF,
                new Label("Password:"), passwordF,
                errorLbl, regBtn);
        return form;
    }

    // ── Authenticated ─────────────────────────────────────────

    private VBox buildProfilePanel() {
        User u = app.getCurrentUser();
        VBox page = new VBox(20);
        page.setPadding(new Insets(24, 32, 30, 32));

        // Header
        HBox header = new HBox(14);
        header.setAlignment(Pos.CENTER_LEFT);

        Label avatar = new Label(u.getUsername().substring(0, 1).toUpperCase());
        avatar.setPrefSize(56, 56);
        avatar.setMinSize(56, 56);
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle("-fx-background-color: " + MainApp.GREEN_PALE + "; -fx-background-radius: 28; -fx-text-fill: " + MainApp.GREEN_DARK + "; -fx-font-size: 22px; -fx-font-weight: bold;");

        VBox info = new VBox(3);
        Label nameL = new Label(u.getUsername());
        nameL.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        Label emailL = new Label(u.getEmail() + "  ·  " + u.getRole());
        emailL.setStyle("-fx-font-size: 13px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");
        Label donL = new Label("Total Donations: ₹" + String.format("%.2f", u.getTotalDonations()));
        donL.setStyle("-fx-font-size: 13px; -fx-text-fill: " + MainApp.GREEN_MID + ";");
        info.getChildren().addAll(nameL, emailL, donL);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button signOut = MainApp.outlineButton("Sign Out");
        signOut.setOnAction(e -> { app.setCurrentUser(null); app.showAccount(); });

        header.getChildren().addAll(avatar, info, spacer, signOut);

        // Interests section
        VBox interestsCard = MainApp.card();
        HBox interestsHeader = new HBox(8);
        interestsHeader.setAlignment(Pos.CENTER_LEFT);
        Label intTitle = new Label("My Interests");
        intTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        Region iSpacer = new Region();
        HBox.setHgrow(iSpacer, Priority.ALWAYS);
        Button editInt = MainApp.outlineButton("✏  Edit");
        editInt.setOnAction(e -> showInterestsDialog());
        interestsHeader.getChildren().addAll(intTitle, iSpacer, editInt);

        HBox chips = new HBox(8);
        chips.setWrapLength(700);
        if (u.getInterests().isEmpty()) {
            Label hint = new Label("No interests set. Click Edit to add some.");
            hint.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_LIGHT + "; -fx-font-style: italic;");
            chips.getChildren().add(hint);
        } else {
            for (Category c : u.getInterests()) {
                Label chip = new Label(c.getDisplayName());
                chip.setStyle("-fx-background-color: " + MainApp.GREEN_PALE + "; -fx-text-fill: " + MainApp.GREEN_DARK + "; -fx-font-size: 12px; -fx-padding: 3 12; -fx-background-radius: 14;");
                chips.getChildren().add(chip);
            }
        }
        interestsCard.getChildren().addAll(interestsHeader, chips);

        // Recommendations
        VBox recsCard = MainApp.card();
        Label recsTitle = new Label("Recommended for You");
        recsTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        recsCard.getChildren().add(recsTitle);

        List<Organisation> recs = u.getInterests().isEmpty()
                ? List.of()
                : app.getOrgService().recommend(u.getInterests()).stream().limit(4).toList();

        if (recs.isEmpty()) {
            Label hint = new Label("Set your interests to get personalised recommendations.");
            hint.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_LIGHT + "; -fx-font-style: italic;");
            recsCard.getChildren().add(hint);
        } else {
            FlowPane flow = new FlowPane(12, 12);
            for (Organisation org : recs) {
                flow.getChildren().add(buildMiniCard(org));
            }
            recsCard.getChildren().add(flow);
        }

        // Connected orgs
        VBox connCard = MainApp.card();
        Label connTitle = new Label("Connected Organisations");
        connTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        connCard.getChildren().add(connTitle);

        if (u.getConnectedOrgIds().isEmpty()) {
            Label hint = new Label("You haven't connected with any organisations yet.");
            hint.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_LIGHT + "; -fx-font-style: italic;");
            connCard.getChildren().add(hint);
        } else {
            for (String id : u.getConnectedOrgIds()) {
                app.getOrgService().findById(id).ifPresent(org -> {
                    Button orgBtn = new Button("🏢  " + org.getName() + " · " + org.getLocation());
                    orgBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + MainApp.GREEN_MID + "; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 2 0;");
                    orgBtn.setOnAction(e -> app.showOrgProfile(org));
                    connCard.getChildren().add(orgBtn);
                });
            }
        }

        page.getChildren().addAll(header, interestsCard, recsCard, connCard);
        return page;
    }

    private VBox buildMiniCard(Organisation org) {
        VBox card = new VBox(5);
        card.setPrefWidth(250);
        card.setStyle("-fx-background-color: " + MainApp.BG_GREY + "; -fx-background-radius: 8; -fx-padding: 10; -fx-cursor: hand;");

        Label name = new Label(org.getName());
        name.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        name.setWrapText(true);
        Label meta = new Label(org.getLocation() + ", " + org.getState());
        meta.setStyle("-fx-font-size: 11px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");
        Button view = MainApp.outlineButton("View →");
        view.setOnAction(e -> app.showOrgProfile(org));

        card.getChildren().addAll(name, meta, view);
        card.setOnMouseClicked(e -> app.showOrgProfile(org));
        return card;
    }

    private void showInterestsDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Edit Interests");
        dlg.setHeaderText("Select the causes you care about:");

        VBox box = new VBox(8);
        box.setPadding(new Insets(10));
        Category[] cats = Category.values();
        CheckBox[] checkBoxes = new CheckBox[cats.length];

        for (int i = 0; i < cats.length; i++) {
            checkBoxes[i] = new CheckBox(cats[i].getDisplayName());
            checkBoxes[i].setSelected(app.getCurrentUser().getInterests().contains(cats[i]));
            box.getChildren().add(checkBoxes[i]);
        }

        ScrollPane sp = new ScrollPane(box);
        sp.setPrefHeight(300);
        sp.setFitToWidth(true);
        dlg.getDialogPane().setContent(sp);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                app.getCurrentUser().getInterests().clear();
                for (int i = 0; i < cats.length; i++) {
                    if (checkBoxes[i].isSelected()) app.getCurrentUser().addInterest(cats[i]);
                }
                app.getUserService().updateInterests(app.getCurrentUser());
                app.showAccount(); // refresh
            }
        });
    }

    // ── Helpers ───────────────────────────────────────────────
    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-font-size: 13px; -fx-padding: 8 12; -fx-background-radius: 6; -fx-border-color: " + MainApp.BORDER + "; -fx-border-radius: 6;");
        return tf;
    }

    private PasswordField styledPassword(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setStyle("-fx-font-size: 13px; -fx-padding: 8 12; -fx-background-radius: 6; -fx-border-color: " + MainApp.BORDER + "; -fx-border-radius: 6;");
        return pf;
    }
}
