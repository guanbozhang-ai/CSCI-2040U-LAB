import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class HomeController {

    private static final String CARS_FILE = "src/main/resources/static/cars.json";

    private final BorderPane root = new BorderPane();
    private final CarStock carStock = new CarStock(CARS_FILE);
    private final List<CarCardComponent> carCards = new ArrayList<>();
    private final FlowPane carGrid = new FlowPane();

    private boolean panelOpen = false;
    private String activeFilter = "All";

    private Slider budgetSlider, hpSlider, milesSlider, seatsSlider;
    private Label budgetVal, hpVal, milesVal, seatsVal;
    private final List<String> selectedMakes = new ArrayList<>();
    private final List<String> selectedBodies = new ArrayList<>();
    private final Map<String, Integer> importance = new HashMap<>();

    private VBox surveyPanel;
    private ScrollPane surveyScroll;
    private Button surveyToggleBtn;

    public HomeController() {
        importance.put("cost", 3);
        importance.put("hp", 2);
        importance.put("miles", 1);

        // Pre-populate selected makes and bodies before building UI
        carStock.getCars().forEach(c -> {
            if (!selectedMakes.contains(c.getMake())) selectedMakes.add(c.getMake());
            if (!selectedBodies.contains(c.getBodyType())) selectedBodies.add(c.getBodyType());
        });

        buildTopBar();
        buildMainContent();
        buildSurveyPanel();
    }

    // ── Top Bar ──────────────────────────────────────────────────────────────────

    private void buildTopBar() {
        HBox topBar = new HBox();
        topBar.getStyleClass().add("topbar");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setMinHeight(60);
        topBar.setPadding(new Insets(0, 28, 0, 28));

        VBox brandBlock = new VBox(2);
        brandBlock.setAlignment(Pos.CENTER_LEFT);
        Label brandName = new Label("APEX AUTO");
        brandName.getStyleClass().add("brand-logo");
        Label tagline = new Label("PRECISION. PERFORMANCE. PRESTIGE.");
        tagline.getStyleClass().add("brand-tagline");
        brandBlock.getChildren().addAll(brandName, tagline);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button browseBtn = new Button("INVENTORY");
        browseBtn.getStyleClass().add("nav-link");

        surveyToggleBtn = new Button("FIND MY MATCH");
        surveyToggleBtn.getStyleClass().add("survey-toggle-btn");
        surveyToggleBtn.setOnAction(e -> togglePanel());

        topBar.getChildren().addAll(brandBlock, spacer, browseBtn, surveyToggleBtn);
        root.setTop(topBar);
    }

    // ── Main Content ─────────────────────────────────────────────────────────────

    private void buildMainContent() {
        VBox page = new VBox(0);
        page.getChildren().add(buildHero());

        VBox contentArea = new VBox(16);
        contentArea.setPadding(new Insets(24, 28, 28, 28));
        contentArea.setStyle("-fx-background-color: #f8f8f6;");

        Label sectionLabel = new Label("ALL VEHICLES");
        sectionLabel.setStyle(
                "-fx-font-size: 10px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #aaaaaa; -fx-letter-spacing: 2px;"
        );

        HBox filterBar = new HBox(8);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        List<String> bodyTypes = carStock.getCars().stream()
                .map(Car::getBodyType).distinct().sorted().collect(Collectors.toList());
        List<String> filters = new ArrayList<>();
        filters.add("All");
        filters.addAll(bodyTypes);

        ToggleGroup filterGroup = new ToggleGroup();
        for (String f : filters) {
            ToggleButton chip = new ToggleButton(f.equals("All") ? "ALL" : f.toUpperCase());
            chip.getStyleClass().add("filter-chip");
            chip.setToggleGroup(filterGroup);
            if (f.equals("All")) chip.setSelected(true);
            final String filter = f;
            chip.setOnAction(e -> { activeFilter = filter; renderCards(); });
            filterBar.getChildren().add(chip);
        }

        carGrid.setHgap(16);
        carGrid.setVgap(16);

        carStock.getCars().forEach(car -> {
            CarCardComponent card = new CarCardComponent(car);
            card.setOnCardClick(() -> showCarDetail(car));
            carCards.add(card);
        });

        contentArea.getChildren().addAll(sectionLabel, filterBar, carGrid);
        page.getChildren().add(contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        ScrollPane scroll = new ScrollPane(page);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("main-scroll");
        root.setCenter(scroll);
        renderCards();
    }

    private HBox buildHero() {
        HBox hero = new HBox();
        hero.getStyleClass().add("hero");
        hero.setAlignment(Pos.CENTER_LEFT);
        hero.setPadding(new Insets(36, 28, 36, 28));

        VBox left = new VBox(8);
        left.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Find Your Perfect Drive.");
        title.getStyleClass().add("hero-title");
        Label sub = new Label("Browse our curated collection of premium vehicles.\nUse our smart match tool to find cars tailored to you.");
        sub.getStyleClass().add("hero-sub");
        sub.setWrapText(true);
        left.getChildren().addAll(title, sub);
        HBox.setHgrow(left, Priority.ALWAYS);

        HBox stats = new HBox(20);
        stats.setAlignment(Pos.CENTER_RIGHT);
        long totalCars = carStock.getCars().size();
        long makes     = carStock.getCars().stream().map(Car::getMake).distinct().count();
        long bodyTypes = carStock.getCars().stream().map(Car::getBodyType).distinct().count();
        stats.getChildren().addAll(
                buildHeroStat(String.valueOf(totalCars), "VEHICLES"),
                buildHeroDivider(),
                buildHeroStat(String.valueOf(makes),     "MAKES"),
                buildHeroDivider(),
                buildHeroStat(String.valueOf(bodyTypes), "BODY TYPES")
        );

        hero.getChildren().addAll(left, stats);
        return hero;
    }

    private VBox buildHeroStat(String value, String label) {
        VBox stat = new VBox(2);
        stat.setAlignment(Pos.CENTER);
        Label val = new Label(value);
        val.getStyleClass().add("hero-stat-value");
        Label lbl = new Label(label);
        lbl.getStyleClass().add("hero-stat-label");
        stat.getChildren().addAll(val, lbl);
        return stat;
    }

    private Region buildHeroDivider() {
        Region div = new Region();
        div.getStyleClass().add("hero-divider");
        div.setPrefWidth(1);
        div.setPrefHeight(36);
        return div;
    }

    private void renderCards() {
        carGrid.getChildren().clear();
        carCards.stream()
                .filter(c -> activeFilter.equals("All") || c.getCar().getBodyType().equals(activeFilter))
                .forEach(c -> carGrid.getChildren().add(c));
    }

    // ── Car Detail Popup ─────────────────────────────────────────────────────────

    private void showCarDetail(Car car) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("APEX AUTO  —  " + car.getMake().toUpperCase() + " " + car.getModel().toUpperCase());
        dialog.setMinWidth(440);
        dialog.setResizable(false);

        VBox layout = new VBox(0);

        StackPane bannerStack = new StackPane();
        bannerStack.setPrefHeight(160);

        VBox banner = new VBox(6);
        banner.setPrefHeight(160);
        banner.setMaxWidth(Double.MAX_VALUE);
        banner.setMaxHeight(Double.MAX_VALUE);
        banner.setAlignment(Pos.BOTTOM_LEFT);
        banner.setStyle("-fx-background-color: #0f0f0f; -fx-padding: 20 28 20 28;");

        if (car.getImageUrl() != null && !car.getImageUrl().isEmpty()) {
            try {
                javafx.scene.image.Image img = new javafx.scene.image.Image(
                        car.getImageUrl(), 440, 160, true, true, true);
                javafx.scene.image.ImageView iv = new javafx.scene.image.ImageView(img);
                iv.setFitWidth(440);
                iv.setFitHeight(160);
                iv.setPreserveRatio(false);
                iv.setOpacity(0.55);
                bannerStack.getChildren().add(iv);
            } catch (Exception ignored) {}
        }

        Label make = new Label(car.getMake().toUpperCase());
        make.setStyle("-fx-font-size: 11px; -fx-text-fill: #888888; -fx-letter-spacing: 2px;");
        Label model = new Label(car.getModel());
        model.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        Label bodyLabel = new Label(car.getBodyType().toUpperCase());
        bodyLabel.setStyle(
                "-fx-font-size: 9px; -fx-text-fill: #0f0f0f; -fx-font-weight: bold;" +
                        "-fx-letter-spacing: 1px; -fx-background-color: #ffffff; -fx-padding: 3 8 3 8;"
        );
        HBox badgeRow = new HBox(8);
        badgeRow.setAlignment(Pos.CENTER_LEFT);
        badgeRow.getChildren().add(bodyLabel);
        banner.getChildren().addAll(make, model, badgeRow);
        bannerStack.getChildren().add(banner);

        VBox content = new VBox(16);
        content.setPadding(new Insets(24, 28, 24, 28));
        content.setStyle("-fx-background-color: #ffffff;");

        GridPane stats = new GridPane();
        stats.setHgap(12);
        stats.setVgap(12);
        addStatCard(stats, "PRICE",      String.format("$%,.0f", (double) car.getPrice()),     0, 0);
        addStatCard(stats, "HORSEPOWER", car.getHorsepower() + " HP",                          0, 1);
        addStatCard(stats, "MILEAGE",    String.format("%,.0f km", (double) car.getMileage()),  1, 0);
        addStatCard(stats, "SEATING",    car.getSeating() + " seats",                           1, 1);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        stats.getColumnConstraints().addAll(col1, col2);

        Button closeBtn = new Button("CLOSE");
        closeBtn.setStyle(
                "-fx-background-color: #0f0f0f; -fx-border-color: #0f0f0f;" +
                        "-fx-border-radius: 0; -fx-background-radius: 0;" +
                        "-fx-text-fill: #ffffff; -fx-font-size: 11px; -fx-font-weight: bold;" +
                        "-fx-letter-spacing: 1px; -fx-padding: 10 28 10 28; -fx-cursor: hand;"
        );
        closeBtn.setOnAction(e -> dialog.close());
        HBox btnRow = new HBox();
        btnRow.setAlignment(Pos.CENTER_RIGHT);
        btnRow.getChildren().add(closeBtn);

        content.getChildren().addAll(stats, new Separator(), btnRow);
        layout.getChildren().addAll(bannerStack, content);

        Scene scene = new Scene(layout);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void addStatCard(GridPane grid, String label, String value, int row, int col) {
        VBox card = new VBox(4);
        card.setStyle(
                "-fx-background-color: #f8f8f6; -fx-border-color: #eeeeee;" +
                        "-fx-border-width: 1; -fx-padding: 12 14 12 14;"
        );
        card.setMaxWidth(Double.MAX_VALUE);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 9px; -fx-text-fill: #aaaaaa; -fx-letter-spacing: 1.5px; -fx-font-weight: bold;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0f0f0f;");
        card.getChildren().addAll(lbl, val);
        GridPane.setFillWidth(card, true);
        grid.add(card, col, row);
    }

    // ── Survey Panel ─────────────────────────────────────────────────────────────

    private void buildSurveyPanel() {
        surveyPanel = new VBox(16);
        surveyPanel.getStyleClass().add("survey-panel");
        surveyPanel.setPadding(new Insets(24));
        surveyPanel.setPrefWidth(290);

        Label title = new Label("FIND MY MATCH");
        title.getStyleClass().add("panel-title");
        Label sub = new Label("Adjust preferences — results update instantly.");
        sub.getStyleClass().add("panel-sub");
        sub.setWrapText(true);

        budgetSlider = makeSlider(0, 100000, 30000, 5000);
        budgetVal    = new Label("$30,000");
        budgetSlider.valueProperty().addListener((o, old, val) -> {
            budgetVal.setText("$" + String.format("%,.0f", val.doubleValue()));
            runMatch();
        });

        hpSlider = makeSlider(50, 700, 200, 10);
        hpVal    = new Label("200 HP");
        hpSlider.valueProperty().addListener((o, old, val) -> {
            hpVal.setText(String.format("%.0f HP", val.doubleValue()));
            runMatch();
        });

        milesSlider = makeSlider(0, 200000, 80000, 5000);
        milesVal    = new Label("80,000 km");
        milesSlider.valueProperty().addListener((o, old, val) -> {
            milesVal.setText(String.format("%,.0f km", val.doubleValue()));
            runMatch();
        });

        seatsSlider = makeSlider(2, 8, 5, 1);
        seatsVal    = new Label("5 seats");
        seatsSlider.valueProperty().addListener((o, old, val) -> {
            seatsVal.setText(String.format("%.0f seats", val.doubleValue()));
            runMatch();
        });

        Label makesLabel = new Label("PREFERRED MAKES");
        makesLabel.getStyleClass().add("p-label");
        List<String> allMakes = carStock.getCars().stream()
                .map(Car::getMake).distinct().sorted().collect(Collectors.toList());
        FlowPane makesTags = buildTagGroup(allMakes, selectedMakes);

        Label bodiesLabel = new Label("BODY TYPE");
        bodiesLabel.getStyleClass().add("p-label");
        List<String> allBodies = carStock.getCars().stream()
                .map(Car::getBodyType).distinct().sorted().collect(Collectors.toList());
        FlowPane bodyTags = buildTagGroup(allBodies, selectedBodies);

        surveyPanel.getChildren().addAll(
                title, sub, new Separator(),
                sliderRow("BUDGET",      budgetSlider, budgetVal, "cost"),
                sliderRow("HORSEPOWER",  hpSlider,     hpVal,     "hp"),
                sliderRow("MAX MILEAGE", milesSlider,  milesVal,  "miles"),
                sliderRow("SEATS",       seatsSlider,  seatsVal,  null),
                new Separator(),
                makesLabel, makesTags,
                bodiesLabel, bodyTags
        );

        // Wrap in scroll pane
        surveyScroll = new ScrollPane(surveyPanel);
        surveyScroll.setFitToWidth(true);
        surveyScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        surveyScroll.getStyleClass().add("main-scroll");
        surveyScroll.setPrefWidth(0);
        surveyScroll.setMaxWidth(0);
        surveyScroll.setMinWidth(0);

        root.setRight(surveyScroll);
    }

    private Slider makeSlider(double min, double max, double val, double tick) {
        Slider s = new Slider(min, max, val);
        s.setBlockIncrement(tick);
        s.setMaxWidth(Double.MAX_VALUE);
        return s;
    }

    private VBox sliderRow(String labelText, Slider slider, Label valueLabel, String impKey) {
        VBox box = new VBox(5);
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("p-label");
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(slider, Priority.ALWAYS);
        valueLabel.getStyleClass().add("sl-val");
        valueLabel.setMinWidth(88);
        row.getChildren().addAll(slider, valueLabel);
        box.getChildren().addAll(lbl, row);
        if (impKey != null) box.getChildren().add(buildImpDots(impKey));
        return box;
    }

    private HBox buildImpDots(String key) {
        HBox row = new HBox(5);
        row.setAlignment(Pos.CENTER_LEFT);
        Label impLabel = new Label("IMPORTANCE:");
        impLabel.getStyleClass().add("imp-label");
        row.getChildren().add(impLabel);
        for (int i = 1; i <= 5; i++) {
            final int level = i;
            Button dot = new Button();
            dot.getStyleClass().add("imp-dot");
            dot.setPrefSize(12, 12);
            dot.setMinSize(12, 12);
            dot.setMaxSize(12, 12);
            if (i <= importance.get(key)) dot.getStyleClass().add("imp-dot-on");
            dot.setOnAction(e -> {
                importance.put(key, level);
                row.getChildren().stream()
                        .filter(n -> n instanceof Button)
                        .map(n -> (Button) n)
                        .forEach(b -> {
                            b.getStyleClass().remove("imp-dot-on");
                            int idx = row.getChildren().indexOf(b);
                            if (idx <= level) b.getStyleClass().add("imp-dot-on");
                        });
                runMatch();
            });
            row.getChildren().add(dot);
        }
        return row;
    }

    private FlowPane buildTagGroup(List<String> options, List<String> selected) {
        FlowPane pane = new FlowPane(6, 6);
        options.forEach(opt -> {
            ToggleButton tag = new ToggleButton(opt.toUpperCase());
            tag.getStyleClass().add("tag-btn");
            tag.setSelected(selected.contains(opt));
            tag.setOnAction(e -> {
                if (tag.isSelected()) { if (!selected.contains(opt)) selected.add(opt); }
                else selected.remove(opt);
                runMatch();
            });
            pane.getChildren().add(tag);
        });
        return pane;
    }

    // ── Panel Toggle ──────────────────────────────────────────────────────────────

    private void togglePanel() {
        panelOpen = !panelOpen;
        double width = panelOpen ? 290 : 0;
        surveyPanel.setPrefWidth(width);
        surveyPanel.setMaxWidth(width);
        surveyScroll.setPrefWidth(width);
        surveyScroll.setMaxWidth(width);
        surveyScroll.setMinWidth(width);
        surveyToggleBtn.setText(panelOpen ? "CLOSE" : "FIND MY MATCH");
        if (panelOpen) runMatch();
        else carCards.forEach(c -> c.setMatched(false, 0));
    }

    // ── Matching Logic ────────────────────────────────────────────────────────────

    private void runMatch() {
        if (!panelOpen) return;
        User user = new User(
                (int) budgetSlider.getValue(), importance.get("cost"),
                (int) hpSlider.getValue(),     importance.get("hp"),
                (int) milesSlider.getValue(),  importance.get("miles"),
                (int) seatsSlider.getValue(),  1.0,
                new ArrayList<>(selectedMakes),
                new ArrayList<>(selectedBodies)
        );
        List<Car> allCars = carStock.getCars();
        double[] scores   = allCars.stream().mapToDouble(User::match).toArray();
        double best       = Arrays.stream(scores).min().orElse(0);
        double worst      = Arrays.stream(scores).max().orElse(1);
        Integer[] indices = new Integer[allCars.size()];
        for (int i = 0; i < indices.length; i++) indices[i] = i;
        Arrays.sort(indices, Comparator.comparingDouble(i -> scores[i]));
        Set<Integer> top3 = new HashSet<>(Arrays.asList(indices[0], indices[1], indices[2]));
        for (int i = 0; i < carCards.size(); i++) {
            int pct = (worst == best) ? 100
                    : (int) Math.round(100 - (scores[i] - best) / (worst - best) * 60);
            carCards.get(i).setMatched(top3.contains(i), pct);
        }
    }

    public BorderPane getRoot() {
        return root;
    }
}
