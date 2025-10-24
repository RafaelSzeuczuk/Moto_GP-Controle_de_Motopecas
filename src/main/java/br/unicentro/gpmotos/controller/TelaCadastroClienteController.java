package br.unicentro.gpmotos.controller;

import br.unicentro.gpmotos.dao.ClienteDAO;
import br.unicentro.gpmotos.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TelaCadastroClienteController implements Initializable {

    @FXML private TextField tfNomeCliente;
    @FXML private Button btnSalvarCliente;
    @FXML private Button btnLimparCliente;
    @FXML private Button btnExcluirCliente;
    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, Integer> colClienteId;
    @FXML private TableColumn<Cliente, String> colClienteNome;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadClientes();

        tabelaClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarCliente(newValue)
        );
    }

    private void setupTable() {
        colClienteId.setCellValueFactory(new PropertyValueFactory<>("clienteId"));
        colClienteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tabelaClientes.setItems(listaClientes);
    }

    private void loadClientes() {
        try {
            listaClientes.setAll(clienteDAO.findAll());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void selecionarCliente(Cliente cliente) {
        if (cliente == null) {
            limparCampos();
            return;
        }

        tfNomeCliente.setText(cliente.getNome());
        btnExcluirCliente.setDisable(false);
    }

    @FXML
    private void handleSalvarCliente(ActionEvent event) {
        String nome = tfNomeCliente.getText();
        if (nome.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Dados Incompletos", "Por favor, preencha o nome do cliente.");
            return;
        }

        try {
            Cliente selecionado = tabelaClientes.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                Cliente novoCliente = new Cliente(0, nome);
                clienteDAO.insert(novoCliente);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Cliente cadastrado com sucesso!");
            } else {
                Cliente clienteAtualizado = new Cliente(selecionado.getClienteId(), nome);
                clienteDAO.update(clienteAtualizado);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Cliente atualizado com sucesso!");
            }
            limparCampos();
            loadClientes();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLimparCliente(ActionEvent event) {
        limparCampos();
    }

    @FXML
    private void handleExcluirCliente(ActionEvent event) {
        Cliente selecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            showAlert(Alert.AlertType.WARNING, "Nenhum Cliente Selecionado", "Selecione um cliente para excluir.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmar Exclusão");
        confirmAlert.setHeaderText("Excluir Cliente?");
        confirmAlert.setContentText("Tem certeza que deseja excluir o cliente " + selecionado.getNome() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    clienteDAO.delete(selecionado.getClienteId());
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Cliente excluído com sucesso!");
                    limparCampos();
                    loadClientes();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao excluir cliente: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void limparCampos() {
        tfNomeCliente.clear();
        btnExcluirCliente.setDisable(true);
        tabelaClientes.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleVoltarParaVendas(ActionEvent event) {
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
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível abrir a tela de vendas.");
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
