package br.unicentro.gpmotos.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TelaInicialController {

    @FXML
    private Label lblLog;

    @FXML
    private Label lblLog1;

    @FXML
    private Label lblLog2;

    @FXML
    private Label lblLog3;

    @FXML
    private Label lblSenha;

    @FXML
    private PasswordField pfdSenha;

    @FXML
    private Button btnAcessar;

    // Simulação de senha válida
    private final String SENHA_CORRETA = "1";

    @FXML
    private void btnAcessarOnAction(ActionEvent event) {
        String senhaDigitada = pfdSenha.getText();

        if (senhaDigitada.equals(SENHA_CORRETA)) {
            mostrarAlerta("Acesso permitido", "Bem-vindo ao GP Motos!", AlertType.INFORMATION);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/unicentro/gpmotos/view/TelaVenda.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Venda");

                Scene scene = new Scene(root, 1100, 800);
                stage.setScene(scene);

                stage.setResizable(false);
                stage.centerOnScreen();

                stage.show();

                Stage telaAtual = (Stage) ((Button) event.getSource()).getScene().getWindow();
                telaAtual.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            mostrarAlerta("Acesso negado", "Senha incorreta. Tente novamente.", AlertType.ERROR);
            pfdSenha.clear();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
