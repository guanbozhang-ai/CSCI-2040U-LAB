import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class CarCardComponent extends VBox {

    private final Car car;
    private boolean matched = false;
    private int matchPercent = 0;
    private Runnable onClickHandler;

    public CarCardComponent(Car car) {
        this.car = car;
        getStyleClass().add("car-card");
        setPrefWidth(215);
        setPadding(new Insets(0, 0, 14, 0));
        setSpacing(0);
        setCursor(javafx.scene.Cursor.HAND);
        setOnMouseClicked(e -> { if (onClickHandler != null) onClickHandler.run(); });
        buildCard();
    }

    private void buildCard() {
        getChildren().clear();

        // Thumbnail — image if URL available, dark silhouette fallback
        StackPane thumb = new StackPane();
        thumb.setPrefHeight(130);
        thumb.setMaxWidth(Double.MAX_VALUE);
        thumb.setStyle("-fx-background-color: " + getThumbBg() + "; -fx-background-radius: 0;");

        if (car.getImageUrl() != null && !car.getImageUrl().isEmpty()) {
            try {
                Image img = new Image(car.getImageUrl(), 215, 130, true, true, true);
                ImageView iv = new ImageView(img);
                iv.setFitWidth(215);
                iv.setFitHeight(130);
                iv.setPreserveRatio(false);
                // Show silhouette while loading, swap in image when ready
                SVGPath placeholder = makeSilhouette();
                thumb.getChildren().add(placeholder);
                img.progressProperty().addListener((obs, old, progress) -> {
                    if (progress.doubleValue() >= 1.0 && img.getWidth() > 0) {
                        thumb.getChildren().setAll(iv);
                        thumb.getChildren().add(makeBodyPill());
                    }
                });
                // If already cached/loaded
                if (img.getProgress() >= 1.0 && img.getWidth() > 0) {
                    thumb.getChildren().setAll(iv);
                }
            } catch (Exception e) {
                thumb.getChildren().add(makeSilhouette());
            }
        } else {
            thumb.getChildren().add(makeSilhouette());
        }

        // Always add body pill on top
        thumb.getChildren().add(makeBodyPill());

        // Text area
        VBox textArea = new VBox(4);
        textArea.setPadding(new Insets(12, 14, 0, 14));

        Label make = new Label(car.getMake().toUpperCase());
        make.setStyle("-fx-font-size: 9px; -fx-text-fill: #aaaaaa; -fx-letter-spacing: 1.5px;");

        Label model = new Label(car.getModel());
        model.getStyleClass().add("car-name");

        Label sub = new Label(car.getHorsepower() + " HP  ·  "
                + String.format("%,.0f", (double) car.getMileage()) + " km  ·  "
                + car.getSeating() + " seats");
        sub.getStyleClass().add("car-sub");
        sub.setWrapText(true);

        Label price = new Label(String.format("$%,.0f", (double) car.getPrice()));
        price.getStyleClass().add("car-price");

        textArea.getChildren().addAll(make, model, sub, price);

        if (matched) {
            Label badge = new Label(matchPercent + "% MATCH");
            badge.getStyleClass().add("match-badge");
            textArea.getChildren().add(badge);
            if (!getStyleClass().contains("car-card-matched"))
                getStyleClass().add("car-card-matched");
        } else {
            getStyleClass().remove("car-card-matched");
        }

        getChildren().addAll(thumb, textArea);
    }

    private SVGPath makeSilhouette() {
        SVGPath icon = new SVGPath();
        icon.setContent(getBodyIcon());
        icon.setFill(Color.web(getIconColor()));
        icon.setScaleX(1.6);
        icon.setScaleY(1.6);
        return icon;
    }

    private Label makeBodyPill() {
        Label bodyPill = new Label(car.getBodyType().toUpperCase());
        bodyPill.setStyle(
                "-fx-background-color: " + getPillBg() + ";" +
                        "-fx-text-fill: " + getPillText() + ";" +
                        "-fx-font-size: 9px; -fx-font-weight: bold;" +
                        "-fx-letter-spacing: 1px;" +
                        "-fx-padding: 3 8 3 8;"
        );
        StackPane.setAlignment(bodyPill, Pos.BOTTOM_LEFT);
        StackPane.setMargin(bodyPill, new Insets(0, 0, 8, 10));
        return bodyPill;
    }

    private String getThumbBg() {
        return switch (car.getBodyType().toLowerCase()) {
            case "suv"       -> "#1a1a1a";
            case "sedan"     -> "#222222";
            case "coupe"     -> "#161616";
            case "truck"     -> "#1e1e1e";
            case "hatchback" -> "#242424";
            default          -> "#1a1a1a";
        };
    }

    private String getIconColor() {
        return "#555555";
    }

    private String getPillBg()   { return matched ? "#ffffff" : "#222222"; }
    private String getPillText() { return matched ? "#0f0f0f" : "#aaaaaa"; }

    private String getBodyIcon() {
        return switch (car.getBodyType().toLowerCase()) {
            case "truck"     -> "M2,13 L2,9 L8,9 L8,5 L18,5 L22,9 L26,9 L26,13 Z M5,13 A2,2 0 1,0 9,13 A2,2 0 1,0 5,13 Z M19,13 A2,2 0 1,0 23,13 A2,2 0 1,0 19,13 Z";
            case "suv"       -> "M3,12 L3,8 L6,4 L20,4 L23,8 L23,12 Z M5,12 A2,2 0 1,0 9,12 A2,2 0 1,0 5,12 Z M17,12 A2,2 0 1,0 21,12 A2,2 0 1,0 17,12 Z";
            case "coupe"     -> "M3,12 L3,9 L8,5 L18,5 L23,9 L23,12 Z M5,12 A2,2 0 1,0 9,12 A2,2 0 1,0 5,12 Z M17,12 A2,2 0 1,0 21,12 A2,2 0 1,0 17,12 Z";
            case "hatchback" -> "M3,12 L3,8 L7,4 L17,4 L21,8 L23,8 L23,12 Z M5,12 A2,2 0 1,0 9,12 A2,2 0 1,0 5,12 Z M17,12 A2,2 0 1,0 21,12 A2,2 0 1,0 17,12 Z";
            default          -> "M3,12 L3,9 L7,6 L17,6 L21,9 L23,9 L23,12 Z M5,12 A2,2 0 1,0 9,12 A2,2 0 1,0 5,12 Z M17,12 A2,2 0 1,0 21,12 A2,2 0 1,0 17,12 Z";
        };
    }

    public void setMatched(boolean matched, int percent) {
        this.matched = matched;
        this.matchPercent = percent;
        buildCard();
    }

    public void setOnCardClick(Runnable handler) {
        this.onClickHandler = handler;
    }

    public Car getCar() { return car; }
}