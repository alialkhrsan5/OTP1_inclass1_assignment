package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
public class Main extends Application {

    private final TemperatureUnitDAO unitDAO = new TemperatureUnitDAO();
    private final TempRecordDAO recordDAO = new TempRecordDAO();

    private TextField valueField;
    private ComboBox<TemperatureUnit> unitComboBox;
    private Label resultLabel;
    private TableView<TempRecord> tableView;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Temperature Converter - Ali");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));

        valueField = new TextField();
        unitComboBox = new ComboBox<>();
        loadUnits();

        Button calcButton = new Button("Convert & Save");
        resultLabel = new Label();

        form.add(new Label("Temperature:"), 0, 0);
        form.add(valueField, 1, 0);
        form.add(new Label("Conversion:"), 0, 1);
        form.add(unitComboBox, 1, 1);
        form.add(calcButton, 1, 2);
        form.add(resultLabel, 1, 3);

        tableView = buildTableView();
        loadRecords();

        calcButton.setOnAction(e -> handleConvertAndSave());

        VBox root = new VBox(15, form, new Label("Saved conversions:"), tableView);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_LEFT);

        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    private void loadUnits() {
        try {
            List<TemperatureUnit> units = unitDAO.getAllUnits();
            unitComboBox.getItems().addAll(units);
            if (!units.isEmpty()) {
                unitComboBox.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            showError("Failed to load conversion types: " + e.getMessage());
        }
    }

    private void handleConvertAndSave() {
        try {
            double value = Double.parseDouble(valueField.getText());
            TemperatureUnit selected = unitComboBox.getValue();

            if (selected == null) {
                showError("Please select a conversion type.");
                return;
            }

            double result = TempCalculator.convert(selected.getUnitName(), value);
            resultLabel.setText(String.format("Result: %.2f", result));

            recordDAO.save(new TempRecord(value, result, selected.getId()));

            loadRecords();
            valueField.clear();

        } catch (NumberFormatException ex) {
            showError("Temperature must be a number.");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        }
    }

    private TableView<TempRecord> buildTableView() {
        TableView<TempRecord> table = new TableView<>();

        TableColumn<TempRecord, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()));

        TableColumn<TempRecord, Number> inputCol = new TableColumn<>("Input");
        inputCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getInputValue()));

        TableColumn<TempRecord, Number> resultCol = new TableColumn<>("Result");
        resultCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getResultValue()));

        TableColumn<TempRecord, Number> unitCol = new TableColumn<>("Unit ID");
        unitCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTemperatureUnitId()));

        TableColumn<TempRecord, String> timeCol = new TableColumn<>("Created");
        timeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(data.getValue().getCreatedAt())));
        timeCol.setPrefWidth(160);

        table.getColumns().add(idCol);
        table.getColumns().add(inputCol);
        table.getColumns().add(resultCol);
        table.getColumns().add(unitCol);
        table.getColumns().add(timeCol);
        return table;
    }

    private void loadRecords() {
        try {
            List<TempRecord> records = recordDAO.getAllRecords();
            tableView.getItems().setAll(records);
        } catch (SQLException e) {
            showError("Failed to load records: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
