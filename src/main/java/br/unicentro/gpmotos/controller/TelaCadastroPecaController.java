package br.unicentro.gpmotos.controller;

import br.unicentro.gpmotos.dao.PecaDAO;
import br.unicentro.gpmotos.model.Peca;
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
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TelaCadastroPecaController implements Initializable {

    @FXML private TextField tfNomePeca;
    @FXML private TextField tfMarcaPeca;
    @FXML private TextField tfModeloPeca;
    @FXML private TextField tfCategoriaPeca;
    @FXML private TextField tfFornecedorPeca;
    @FXML private TextField tfPrecoPeca;

    @FXML private Button btnSalvarPeca;
    @FXML private Button btnLimparPeca;
    @FXML private Button btnExcluirPeca;

    @FXML private TableView<Peca> tabelaPecasCadastro;
    @FXML private TableColumn<Peca, Integer> colPecaId;
    @FXML private TableColumn<Peca, String> colPecaNome;
    @FXML private TableColumn<Peca, String> colPecaMarca;
    @FXML private TableColumn<Peca, String> colPecaModelo;
    @FXML private TableColumn<Peca, String> colPecaCategoria;
    @FXML private TableColumn<Peca, String> colPecaFornecedor;
    @FXML private TableColumn<Peca, Double> colPecaPreco;

    private PecaDAO pecaDAO = new PecaDAO();
    private final ObservableList<Peca> listaPecas = FXCollections.observableArrayList();
    private Peca pecaSelecionada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabela();
        carregarPecas();

        tabelaPecasCadastro.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> selecionarPeca(newSelection)
        );
    }

    private void configurarTabela() {
        colPecaId.setCellValueFactory(new PropertyValueFactory<>("pecaId"));
        colPecaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPecaMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colPecaModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colPecaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPecaFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        colPecaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tabelaPecasCadastro.setItems(listaPecas);
    }

    private void carregarPecas() {
        try {
            listaPecas.setAll(pecaDAO.findAll());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar peças: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void selecionarPeca(Peca peca) {
        if (peca == null) {
            limparCampos();
            return;
        }

        pecaSelecionada = peca;
        tfNomePeca.setText(peca.getNome());
        tfMarcaPeca.setText(peca.getMarca());
        tfModeloPeca.setText(peca.getModelo());
        tfCategoriaPeca.setText(peca.getCategoria());
        tfFornecedorPeca.setText(peca.getFornecedor());
        tfPrecoPeca.setText(String.valueOf(peca.getPreco()));
        btnExcluirPeca.setDisable(false);
    }

    @FXML
    public void handleSalvarPeca(ActionEvent event) {
        String nome = tfNomePeca.getText();
        String marca = tfMarcaPeca.getText();
        String modelo = tfModeloPeca.getText();
        String categoria = tfCategoriaPeca.getText();
        String fornecedor = tfFornecedorPeca.getText();
        String precoStr = tfPrecoPeca.getText();

        if (nome.isEmpty() || precoStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos obrigatórios", "Preencha pelo menos o nome e o preço.");
            return;
        }

        try {
            BigDecimal preco = new BigDecimal(precoStr);


            if (pecaSelecionada == null) {
                Peca nova = new Peca(0, nome, marca, modelo, categoria, fornecedor, preco);
                pecaDAO.insert(nova);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Peça cadastrada com sucesso!");
            } else {
                Peca atualizada = new Peca(pecaSelecionada.getPecaId(), nome, marca, modelo, categoria, fornecedor, preco);
                pecaDAO.update(atualizada);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Peça atualizada com sucesso!");
            }

            limparCampos();
            carregarPecas();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de formato", "Preço inválido.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar peça: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLimparPeca(ActionEvent event) {
        limparCampos();
    }

    @FXML
    private void handleExcluirPeca(ActionEvent event) {
        if (pecaSelecionada == null) {
            showAlert(Alert.AlertType.WARNING, "Nenhuma peça selecionada", "Selecione uma peça para excluir.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar exclusão");
        confirm.setHeaderText("Excluir peça?");
        confirm.setContentText("Tem certeza que deseja excluir a peça " + pecaSelecionada.getNome() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    pecaDAO.delete(pecaSelecionada.getPecaId());
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Peça excluída com sucesso!");
                    limparCampos();
                    carregarPecas();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao excluir peça: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
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
            showAlert(Alert.AlertType.ERROR, "Erro de navegação", "Não foi possível abrir a tela de vendas.");
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        tfNomePeca.clear();
        tfMarcaPeca.clear();
        tfModeloPeca.clear();
        tfCategoriaPeca.clear();
        tfFornecedorPeca.clear();
        tfPrecoPeca.clear();
        tabelaPecasCadastro.getSelectionModel().clearSelection();
        pecaSelecionada = null;
        btnExcluirPeca.setDisable(true);
    }

    private void showAlert(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public void initForTest(TextField tfNomePeca, TextField tfMarcaPeca, TextField tfModeloPeca,
                            TextField tfCategoriaPeca, TextField tfFornecedorPeca, TextField tfPrecoPeca,
                            Button btnExcluirPeca, TableView<Peca> tabelaPecasCadastro, PecaDAO pecaDAO) {
        this.tfNomePeca = tfNomePeca;
        this.tfMarcaPeca = tfMarcaPeca;
        this.tfModeloPeca = tfModeloPeca;
        this.tfCategoriaPeca = tfCategoriaPeca;
        this.tfFornecedorPeca = tfFornecedorPeca;
        this.tfPrecoPeca = tfPrecoPeca;
        this.btnExcluirPeca = btnExcluirPeca;
        this.tabelaPecasCadastro = tabelaPecasCadastro;
        this.pecaDAO = pecaDAO;
    }

}
