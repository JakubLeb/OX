package aplication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aplication.dao.RozgrywkaDAO;
import aplication.dao.RozgrywkaDOAImpt;
import aplication.expection.DBExpection;
import aplication.model.OXEnum;
import aplication.model.Rozgrywka;
import aplication.game.OXEngine;
import aplication.game.OXInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainControler {
    private static final Logger logger = LoggerFactory.getLogger(MainControler.class);
    
    @FXML
    public TextField PlayerO;
    @FXML
    public TextField PlayerX;
  
    @FXML
    private Button button00, button01, button02,
                   button10, button11, button12,
                   button20, button21, button22;

    private OXEngine oxEngine;
    
    @FXML
    private void initialize_butons() {
        button00.setDisable(false);
        button01.setDisable(false);
        button02.setDisable(false);
        button10.setDisable(false);
        button11.setDisable(false);
        button12.setDisable(false);
        button20.setDisable(false);
        button21.setDisable(false);
        button22.setDisable(false);

        // Initialize the game engine
        oxEngine = new OXEngine();
        oxEngine.Init();
    }

    @FXML
    private void handleButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        int buttonId = Integer.parseInt(clickedButton.getId().replace("button", ""));
        int row = buttonId / 10;
        int col = buttonId % 10;
        int fieldNumber = row * 3 + col;
        oxEngine.step++;
        if (oxEngine.blockField(fieldNumber) == 1) {
            oxEngine.setField(fieldNumber);
            clickedButton.setText(oxEngine.player == OXInterface.Player.playerX ? "X" : "O");
            oxEngine.checkResult();

            if (oxEngine.result != OXInterface.Result.none) {
                handleGameResult();
            } else {
                oxEngine.changePlayer();
            }
        }
    }

    private void handleGameResult() {
        OXEnum winner = OXEnum.NONE;
        if (oxEngine.result == OXInterface.Result.winX) {
            winner = OXEnum.X;
        } else if (oxEngine.result == OXInterface.Result.winO) {
            winner = OXEnum.O;
        } else if (oxEngine.result == OXInterface.Result.draw) {
            winner = OXEnum.NONE;
        }

        LocalDateTime currentTime = LocalDateTime.now();
        Rozgrywka rozgrywka = new Rozgrywka();
        String textPlayerO = PlayerO.getText();
        String textPlayerX = PlayerX.getText();
        Integer id = rozgrywkaDAO.getLatestId();
        rozgrywka = new Rozgrywka(id, textPlayerO, textPlayerX, winner, currentTime);
        rozgrywkaDAO.save(rozgrywka);
        history = FXCollections.observableArrayList();
        rozgrywakTable.setItems(history);

        executer.execute(() -> {
            List<Rozgrywka> rozgrywki = rozgrywkaDAO.findAll();
            Platform.runLater(() -> history.addAll(rozgrywki));
        });

        resetGame();
    }

    @FXML
    private Button startbt;
    @FXML
    private TableView<Rozgrywka> rozgrywakTable;
    @FXML
    private TableColumn<Rozgrywka, Integer> rozgrywakIdColumn;
    @FXML
    private TableColumn<Rozgrywka, String> graczXColumn;
    @FXML
    private TableColumn<Rozgrywka, String> graczOColumn;
    @FXML
    private TableColumn<Rozgrywka, OXEnum> zwyciezcaColumn;
    @FXML
    private TableColumn<Rozgrywka, LocalDateTime> dataczasRozgrywkiColumn;

    private ObservableList<Rozgrywka> history;
    private RozgrywkaDAO rozgrywkaDAO = new RozgrywkaDOAImpt();
    private ExecutorService executer;

    public MainControler() {
        this.executer = Executors.newSingleThreadExecutor();
    }

    @FXML
    private void initialize() {
        logger.info("Inicjalizacja kontrolera...");
        
        button00.setDisable(true);
        button01.setDisable(true);
        button02.setDisable(true);
        button10.setDisable(true);
        button11.setDisable(true);
        button12.setDisable(true);
        button20.setDisable(true);
        button21.setDisable(true);
        button22.setDisable(true);
        
        rozgrywakIdColumn.setCellValueFactory(new PropertyValueFactory<>("rozgrywkaId"));
        graczXColumn.setCellValueFactory(new PropertyValueFactory<>("graczX"));
        graczOColumn.setCellValueFactory(new PropertyValueFactory<>("graczO"));
        zwyciezcaColumn.setCellValueFactory(new PropertyValueFactory<>("zwyciezca"));
        dataczasRozgrywkiColumn.setCellValueFactory(new PropertyValueFactory<>("dataczasRozgrywki"));

        history = FXCollections.observableArrayList();
        rozgrywakTable.setItems(history);

        executer.execute(() -> {
            List<Rozgrywka> rozgrywki = rozgrywkaDAO.findAll();
            Platform.runLater(() -> history.addAll(rozgrywki));
        });
    }

    @FXML
    public void onActionBtnNew(ActionEvent event) {
        executer.execute(() -> testDB());
    }
    
    public void shutdown() {
        if (executer != null) {
            executer.shutdownNow();
        }
    }

    private void testDB() {
        try {
            Rozgrywka r = new Rozgrywka(1, "Jan", "Marek", OXEnum.O, LocalDateTime.now());
            this.rozgrywkaDAO.save(r);
        } catch (DBExpection e) {
            logger.error("Błąd podczas operacji bazodanowych", e);
            String errDetails = e.getCause().getMessage();
            Platform.runLater(() -> showError(e.getMessage(), errDetails));
        }
    }

    public void showError(String info, String errDetails) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Wystąpił błąd");
        alert.setContentText(info);
        alert.showAndWait();
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = getButtonByIndex(i, j);
                button.setText("");
            }
        }
        oxEngine.Init();
    }

    private Button getButtonByIndex(int row, int col) {
        switch (row) {
            case 0:
                switch (col) {
                    case 0: return button00;
                    case 1: return button01;
                    case 2: return button02;
                }
            case 1:
                switch (col) {
                    case 0: return button10;
                    case 1: return button11;
                    case 2: return button12;
                }
            case 2:
                switch (col) {
                    case 0: return button20;
                    case 1: return button21;
                    case 2: return button22;
                }
        }
        return null;
    }
}