package com.ngoconnect.ui;

import com.ngoconnect.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

public class OrgProfileView {

    private final MainApp app;
    private final Organisation org;
    private final BorderPane root;

    public OrgProfileView(MainApp app, Organisation org) {
        this.app = app;
        this.org = org;
        this.root = app.pageWrapper("Browse");
        root.setCenter(buildContent());
    }

    public BorderPane getRoot() { return root; }

    private ScrollPane buildContent() {
        VBox page = new VBox(0);

        // Back button + header strip
        HBox backBar = new HBox();
        backBar.setPadding(new Insets(12, 32, 0, 32));
        backBar.setStyle("-fx-background-color: " + MainApp.BG_WHITE + ";");
        Button back = new Button("← Back");
        back.setStyle("-fx-background-color: transparent; -fx-text-fill: " + MainApp.GREEN_MID + "; -fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 4 0;");
        back.setOnAction(e -> app.showBrowse());
        backBar.getChildren().add(back);

        // Hero header
        VBox hero = new VBox(10);
        hero.setPadding(new Insets(20, 32, 24, 32));
        hero.setStyle("-fx-background-color: " + MainApp.BG_WHITE + "; -fx-border-color: " + MainApp.BORDER + "; -fx-border-width: 0 0 1 0;");

        HBox nameRow = new HBox(10);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        Label initial = new Label(org.getName().substring(0, 1).toUpperCase());
        initial.setPrefSize(54, 54);
        initial.setMinSize(54, 54);
        initial.setAlignment(Pos.CENTER);
        initial.setStyle("-fx-background-color: " + MainApp.GREEN_PALE + "; -fx-background-radius: 27; -fx-text-fill: " + MainApp.GREEN_DARK + "; -fx-font-size: 22px; -fx-font-weight: bold;");

        VBox nameInfo = new VBox(4);
        HBox nameLine = new HBox(8);
        nameLine.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label(org.getName());
        nameLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        if (org.isVerified()) {
            Label badge = new Label("✔  Verified");
            badge.setStyle("-fx-background-color: " + MainApp.GREEN_LIGHT + "; -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 2 8; -fx-background-radius: 10;");
            nameLine.getChildren().addAll(nameLabel, badge);
        } else {
            nameLine.getChildren().add(nameLabel);
        }
        Label metaLine = new Label(
            org.getType().getLabel() + "  ·  " + org.getLocation() + ", " + org.getState() +
            "  ·  Est. " + org.getFoundedYear() + "  ·  " + org.getTenureYears() + " years"
        );
        metaLine.setStyle("-fx-font-size: 13px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");

        if (org.getAverageRating() > 0) {
            Label rating = new Label("★  " + String.format("%.1f", org.getAverageRating()) + " / 5  (" + org.getReviews().size() + " reviews)");
            rating.setStyle("-fx-font-size: 13px; -fx-text-fill: " + MainApp.ACCENT + ";");
            nameInfo.getChildren().addAll(nameLine, metaLine, rating);
        } else {
            nameInfo.getChildren().addAll(nameLine, metaLine);
        }

        nameRow.getChildren().addAll(initial, nameInfo);

        // Action buttons
        HBox actions = new HBox(10);
        Button connectBtn = MainApp.primaryButton("🤝  Connect / Volunteer");
        connectBtn.setOnAction(e -> showConnectDialog());
        Button donateBtn = MainApp.outlineButton("💰  Donate");
        donateBtn.setOnAction(e -> showDonateDialog());
        Button reviewBtn = MainApp.outlineButton("✍  Leave a Review");
        reviewBtn.setOnAction(e -> showReviewDialog());
        actions.getChildren().addAll(connectBtn, donateBtn, reviewBtn);

        hero.getChildren().addAll(nameRow, actions);

        // Body: two columns
        HBox body = new HBox(20);
        body.setPadding(new Insets(20, 32, 30, 32));

        VBox left = new VBox(16);
        HBox.setHgrow(left, Priority.ALWAYS);

        // Description card
        VBox descCard = MainApp.card();
        Label descTitle = sectionHead("About");
        Label descText = new Label(org.getDescription());
        descText.setWrapText(true);
        descText.setStyle("-fx-font-size: 13px; -fx-text-fill: " + MainApp.TEXT_MED + "; -fx-line-spacing: 3;");
        descCard.getChildren().addAll(descTitle, descText);

        // Area of work + categories
        VBox infoCard = MainApp.card();
        infoCard.getChildren().add(sectionHead("Details"));
        infoCard.getChildren().add(infoRow("📌  Area of Work", org.getAreaOfWork()));
        infoCard.getChildren().add(infoRow("👥  Volunteers", String.format("%,d", org.getVolunteerCount())));
        if (org.getRegistrationNumber() != null && !org.getRegistrationNumber().isEmpty()) {
            infoCard.getChildren().add(infoRow("🔖  Reg. Number", org.getRegistrationNumber()));
        }
        infoCard.getChildren().add(infoRow("🗂  SDG Alignment", org.getSdgAlignment()));

        // Categories chips
        HBox chips = new HBox(6);
        chips.setWrapLength(400);
        chips.setPadding(new Insets(4, 0, 0, 0));
        for (Category c : org.getCategories()) {
            Label chip = new Label(c.getDisplayName());
            chip.setStyle("-fx-background-color: " + MainApp.GREEN_PALE + "; -fx-text-fill: " + MainApp.GREEN_DARK + "; -fx-font-size: 11px; -fx-padding: 3 10; -fx-background-radius: 12;");
            chips.getChildren().add(chip);
        }
        infoCard.getChildren().addAll(new Label(""), chips);

        // Recent activities
        if (!org.getRecentActivities().isEmpty()) {
            VBox actCard = MainApp.card();
            actCard.getChildren().add(sectionHead("Recent Activities"));
            for (String act : org.getRecentActivities()) {
                Label l = new Label("▪  " + act);
                l.setWrapText(true);
                l.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_MED + ";");
                actCard.getChildren().add(l);
            }
            left.getChildren().addAll(descCard, infoCard, actCard);
        } else {
            left.getChildren().addAll(descCard, infoCard);
        }

        // Events card
        VBox eventsCard = MainApp.card();
        eventsCard.getChildren().add(sectionHead("Upcoming & Past Events (" + org.getEvents().size() + ")"));
        if (org.getEvents().isEmpty()) {
            eventsCard.getChildren().add(emptyNote("No events listed."));
        } else {
            for (Event ev : org.getEvents()) {
                eventsCard.getChildren().add(buildEventRow(ev));
            }
        }
        left.getChildren().add(eventsCard);

        // Right column: contact + reviews
        VBox right = new VBox(16);
        right.setPrefWidth(300);
        right.setMinWidth(280);

        VBox contactCard = MainApp.card();
        contactCard.getChildren().add(sectionHead("Contact"));
        ContactInfo ci = org.getContactInfo();
        contactCard.getChildren().add(infoRow("📞", ci.getPhone()));
        contactCard.getChildren().add(infoRow("✉", ci.getEmail()));
        if (!ci.getWebsite().equalsIgnoreCase("N/A") && !ci.getWebsite().isEmpty()) {
            contactCard.getChildren().add(infoRow("🌐", ci.getWebsite()));
        }
        contactCard.getChildren().add(infoRow("🏠", ci.getAddress()));

        // Reviews card
        VBox reviewsCard = MainApp.card();
        reviewsCard.getChildren().add(sectionHead("Reviews (" + org.getReviews().size() + ")"));
        if (org.getReviews().isEmpty()) {
            reviewsCard.getChildren().add(emptyNote("No reviews yet. Be the first!"));
        } else {
            for (Review r : org.getReviews()) {
                reviewsCard.getChildren().add(buildReviewRow(r));
            }
        }

        right.getChildren().addAll(contactCard, reviewsCard);

        body.getChildren().addAll(left, right);
        page.getChildren().addAll(backBar, hero, body);

        ScrollPane scroll = new ScrollPane(page);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    private Label sectionHead(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + "; -fx-border-color: " + MainApp.BORDER + "; -fx-border-width: 0 0 1 0; -fx-padding: 0 0 6 0;");
        l.setMaxWidth(Double.MAX_VALUE);
        return l;
    }

    private HBox infoRow(String label, String value) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.TOP_LEFT);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_LIGHT + "; -fx-min-width: 110;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_MED + ";");
        val.setWrapText(true);
        row.getChildren().addAll(lbl, val);
        return row;
    }

    private Label emptyNote(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_LIGHT + "; -fx-font-style: italic;");
        return l;
    }

    private VBox buildEventRow(Event ev) {
        VBox v = new VBox(2);
        v.setStyle("-fx-border-color: " + MainApp.BORDER + "; -fx-border-width: 0 0 1 0; -fx-padding: 6 0 8 0;");
        HBox top = new HBox(6);
        top.setAlignment(Pos.CENTER_LEFT);
        Label typeBadge = new Label(ev.getType().getLabel());
        typeBadge.setStyle("-fx-background-color: " + (ev.isUpcoming() ? MainApp.GREEN_PALE : "#F5F5F5") + "; -fx-text-fill: " + (ev.isUpcoming() ? MainApp.GREEN_DARK : MainApp.TEXT_LIGHT) + "; -fx-font-size: 10px; -fx-padding: 1 7; -fx-background-radius: 10;");
        Label titleL = new Label(ev.getTitle());
        titleL.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        if (ev.isUpcoming()) {
            Label upTag = new Label("Upcoming");
            upTag.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: " + MainApp.GREEN_MID + "; -fx-font-size: 10px; -fx-padding: 1 7; -fx-background-radius: 10;");
            top.getChildren().addAll(titleL, typeBadge, upTag);
        } else {
            top.getChildren().addAll(titleL, typeBadge);
        }
        Label dateLoc = new Label(ev.getDate() + "  ·  " + ev.getLocation());
        dateLoc.setStyle("-fx-font-size: 11px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");
        Label desc = new Label(ev.getDescription());
        desc.setStyle("-fx-font-size: 11px; -fx-text-fill: " + MainApp.TEXT_MED + ";");
        desc.setWrapText(true);
        v.getChildren().addAll(top, dateLoc, desc);
        return v;
    }

    private VBox buildReviewRow(Review r) {
        VBox v = new VBox(3);
        v.setStyle("-fx-border-color: " + MainApp.BORDER + "; -fx-border-width: 0 0 1 0; -fx-padding: 6 0 8 0;");
        HBox top = new HBox(8);
        top.setAlignment(Pos.CENTER_LEFT);
        Label stars = new Label(r.getStars());
        stars.setStyle("-fx-text-fill: " + MainApp.ACCENT + "; -fx-font-size: 12px;");
        Label reviewer = new Label(r.getReviewerName());
        reviewer.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + MainApp.TEXT_DARK + ";");
        Label date = new Label("·  " + r.getTimestamp().toLocalDate().toString());
        date.setStyle("-fx-font-size: 11px; -fx-text-fill: " + MainApp.TEXT_LIGHT + ";");
        top.getChildren().addAll(stars, reviewer, date);
        Label comment = new Label(r.getComment());
        comment.setWrapText(true);
        comment.setStyle("-fx-font-size: 12px; -fx-text-fill: " + MainApp.TEXT_MED + ";");
        v.getChildren().addAll(top, comment);
        return v;
    }

    // ── Dialogs ───────────────────────────────────────────────

    private void showConnectDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Connect with " + org.getName());
        dlg.setHeaderText("How would you like to get involved?");

        ButtonType volunteer = new ButtonType("🤝  Volunteer", ButtonBar.ButtonData.LEFT);
        ButtonType collaborate = new ButtonType("🏢  Collaborate", ButtonBar.ButtonData.LEFT);
        ButtonType cancel = ButtonType.CANCEL;
        dlg.getDialogPane().getButtonTypes().addAll(volunteer, collaborate, cancel);

        dlg.showAndWait().ifPresent(bt -> {
            if (bt == volunteer) {
                if (app.getCurrentUser() != null) app.getCurrentUser().connectToOrg(org.getId());
                MainApp.showAlert("Volunteering Interest Registered",
                    "✔  Your interest has been registered with " + org.getName() + "!\n\nContact: " + org.getContactInfo().getEmail(),
                    Alert.AlertType.INFORMATION);
            } else if (bt == collaborate) {
                TextInputDialog tid = new TextInputDialog();
                tid.setTitle("Collaboration Request");
                tid.setHeaderText("Enter your organisation's name:");
                tid.showAndWait().ifPresent(name -> {
                    if (!name.isBlank()) {
                        MainApp.showAlert("Collaboration Request Sent",
                            "✔  Collaboration request from '" + name + "' sent to " + org.getName() + "!",
                            Alert.AlertType.INFORMATION);
                    }
                });
            }
        });
    }

    private void showDonateDialog() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Donate to " + org.getName());
        dlg.setHeaderText("Enter donation amount (₹):");
        dlg.setContentText("Amount:");
        dlg.showAndWait().ifPresent(amtStr -> {
            try {
                double amt = Double.parseDouble(amtStr.trim());
                if (amt <= 0) throw new NumberFormatException();
                if (app.getCurrentUser() != null) app.getCurrentUser().addDonation(amt);
                MainApp.showAlert("Donation Recorded",
                    "💚  Thank you! ₹" + String.format("%.2f", amt) + " donation recorded for " + org.getName() + ".",
                    Alert.AlertType.INFORMATION);
            } catch (NumberFormatException ex) {
                MainApp.showAlert("Invalid Amount", "Please enter a valid positive number.", Alert.AlertType.ERROR);
            }
        });
    }

    private void showReviewDialog() {
        Dialog<Void> dlg = new Dialog<>();
        dlg.setTitle("Leave a Review");
        dlg.setHeaderText("Review: " + org.getName());

        VBox form = new VBox(10);
        form.setPadding(new Insets(10));

        // Reviewer name
        TextField nameField = new TextField(app.getCurrentUser() != null ? app.getCurrentUser().getUsername() : "");
        nameField.setPromptText("Your name");

        // Rating slider
        Label ratingLabel = new Label("Rating: 3 ★");
        Slider ratingSlider = new Slider(1, 5, 3);
        ratingSlider.setMajorTickUnit(1);
        ratingSlider.setSnapToTicks(true);
        ratingSlider.setShowTickLabels(true);
        ratingSlider.valueProperty().addListener((obs, o, n) ->
            ratingLabel.setText("Rating: " + n.intValue() + " ★")
        );

        TextArea comment = new TextArea();
        comment.setPromptText("Write your review...");
        comment.setPrefRowCount(3);

        form.getChildren().addAll(
            new Label("Your name:"), nameField,
            ratingLabel, ratingSlider,
            new Label("Comment:"), comment
        );

        dlg.getDialogPane().setContent(form);
        ButtonType submit = new ButtonType("Submit Review", ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(submit, ButtonType.CANCEL);

        dlg.setResultConverter(bt -> null);
        dlg.showAndWait();

        // Check if submitted
        if (dlg.getResult() != null || !comment.getText().isBlank()) {
            String reviewer = nameField.getText().isBlank() ? "Anonymous" : nameField.getText();
            int rating = (int) Math.round(ratingSlider.getValue());
            String text = comment.getText().trim();
            if (text.isEmpty()) {
                MainApp.showAlert("Missing Comment", "Please write a comment.", Alert.AlertType.WARNING);
                return;
            }
            try {
                app.getOrgService().addReview(org.getId(), reviewer, rating, text);
                MainApp.showAlert("Review Submitted", "✔  Thank you for your review!", Alert.AlertType.INFORMATION);
                // Refresh the view
                app.showOrgProfile(org);
            } catch (Exception ex) {
                MainApp.showAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
