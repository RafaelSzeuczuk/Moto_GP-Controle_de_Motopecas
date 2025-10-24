package br.unicentro.gpmotos.main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Cria um objeto responsável por carregar e analisar sintaticamente o arquivo FXML da tela inicial
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/br/unicentro/gpmotos/view/TelaInicial.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("MotoJar - Controle de Peças");

        // Adiciona o ícone do aplicativo
        try {
            Image icon = new Image(getClass().getResourceAsStream("/br/unicentro/gpmotos/image/gp_motos.jpeg"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Erro ao carregar o ícone: " + e.getMessage());
        }

        stage.setScene(scene);
        // Define que o stage não pode ser redimensionado pelo usuário
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

