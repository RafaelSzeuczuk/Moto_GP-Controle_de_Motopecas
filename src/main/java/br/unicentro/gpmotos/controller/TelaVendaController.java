package br.unicentro.gpmotos.controller;

import br.unicentro.gpmotos.dao.*;
import br.unicentro.gpmotos.model.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TelaVendaController implements Initializable {

    @FXML
    private ComboBox<String> cbMarca;
    @FXML
    private ComboBox<String> cbModelo;
    @FXML
    private ComboBox<String> cbCategoria;
    @FXML
    private ComboBox<Cliente> cbClientes;
    @FXML
    private TextField tfBusca;
    @FXML
    private TableView<Peca> tabelaPecas;
    @FXML
    private TableColumn<Peca, String> colNome;
    @FXML
    private TableColumn<Peca, String> colMarca;
    @FXML
    private TableColumn<Peca, String> colModelo;
    @FXML
    private TableColumn<Peca, BigDecimal> colPreco;
    @FXML
    private TableColumn<Peca, Button> colAdicionar;

    @FXML
    private TableView<ItemVendaDisplay> tabelaCarrinho;
    @FXML
    private TableColumn<ItemVendaDisplay, String> colCarrinhoNome;
    @FXML
    private TableColumn<ItemVendaDisplay, Integer> colCarrinhoQtd;
    @FXML
    private TableColumn<ItemVendaDisplay, BigDecimal> colCarrinhoPreco;
    @FXML
    private TableColumn<ItemVendaDisplay, BigDecimal> colCarrinhoSubtotal;

    @FXML
    private Label lblSubtotal;

    private ObservableList<Peca> listaPecas = FXCollections.observableArrayList();
    private ObservableList<ItemVendaDisplay> carrinho = FXCollections.observableArrayList();
    private PecaDAO pecaDAO = new PecaDAO();
    private VendaDAO vendaDAO = new VendaDAO();
    private ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTablePecas();
        setupTableCarrinho();
        loadPecas();
        loadClientes();
        populateFilters();
        updateSubtotal();
        configurarColunaRemover();

        tfBusca.textProperty().addListener((obs, oldVal, newVal) -> buscarPecas());
        cbMarca.valueProperty().addListener((obs, oldVal, newVal) -> buscarPecas());
        cbModelo.valueProperty().addListener((obs, oldVal, newVal) -> buscarPecas());
        cbCategoria.valueProperty().addListener((obs, oldVal, newVal) -> buscarPecas());
    }

    private void setupTablePecas() {
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colMarca.setCellValueFactory(cellData -> cellData.getValue().marcaProperty());
        colModelo.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        colPreco.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPreco()));

        colAdicionar.setCellValueFactory(param -> {
            Button addButton = new Button("Adicionar");
            addButton.setOnAction(event -> adicionarAoCarrinho(param.getValue()));
            return new SimpleObjectProperty<>(addButton);
        });

        tabelaPecas.setItems(listaPecas);
    }

    private void setupTableCarrinho() {
        colCarrinhoNome.setCellValueFactory(cellData -> cellData.getValue().nomePecaProperty());
        colCarrinhoQtd.setCellValueFactory(cellData -> cellData.getValue().quantidadeProperty().asObject());
        colCarrinhoPreco.setCellValueFactory(cellData -> cellData.getValue().precoUnitarioProperty());
        colCarrinhoSubtotal.setCellValueFactory(cellData -> cellData.getValue().subtotalProperty());

        tabelaCarrinho.setItems(carrinho);
    }

    private void loadPecas() {
        try {
            listaPecas.setAll(pecaDAO.findAll());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar peças: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadClientes() {
        try {
            cbClientes.setItems(FXCollections.observableArrayList(clienteDAO.findAll()));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // REFATORAÇÃO 5: Extract Variable - Para simplificar a lógica de filtros em populateFilters
    private void populateFilters() {
        try {
            List<Peca> allPecas = pecaDAO.findAll();

            // ANTES: Código complexo e repetitivo
            // cbMarca.setItems(FXCollections.observableArrayList(
            //     allPecas.stream().map(Peca::getMarca).distinct().sorted().collect(Collectors.toList())));
            // cbModelo.setItems(FXCollections.observableArrayList(
            //     allPecas.stream().map(Peca::getModelo).distinct().sorted().collect(Collectors.toList())));
            // cbCategoria.setItems(FXCollections.observableArrayList(
            //     allPecas.stream().map(Peca::getCategoria).distinct().sorted().collect(Collectors.toList())));

            // DEPOIS: Extraindo variáveis para melhor legibilidade
            List<String> marcasDistintas = allPecas.stream().map(Peca::getMarca).distinct().sorted().collect(Collectors.toList());
            List<String> modelosDistintos = allPecas.stream().map(Peca::getModelo).distinct().sorted().collect(Collectors.toList());
            List<String> categoriasDistintas = allPecas.stream().map(Peca::getCategoria).distinct().sorted().collect(Collectors.toList());

            cbMarca.setItems(FXCollections.observableArrayList(marcasDistintas));
            cbModelo.setItems(FXCollections.observableArrayList(modelosDistintos));
            cbCategoria.setItems(FXCollections.observableArrayList(categoriasDistintas));

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar filtros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // REFATORAÇÃO 6: Inline Temp - Para a variável searchTerm em buscarPecas
    @FXML
    private void buscarPecas() {
        // ANTES: Variável temporária desnecessária
        // String searchTerm = tfBusca.getText();
        // String marca = cbMarca.getValue();
        // String modelo = cbModelo.getValue();
        // String categoria = cbCategoria.getValue();
        //
        // try {
        //     listaPecas.setAll(pecaDAO.search(searchTerm, marca, modelo, categoria));
        // } catch (SQLException e) {
        //     showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao buscar peças: " + e.getMessage());
        //     e.printStackTrace();
        // }

        // DEPOIS: Variável inline para simplificar
        try {
            listaPecas.setAll(pecaDAO.search(
                    tfBusca.getText(),  // searchTerm inline
                    cbMarca.getValue(),
                    cbModelo.getValue(),
                    cbCategoria.getValue()
            ));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao buscar peças: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // REFATORAÇÃO 7: Introduce Explaining Variable - Para o cálculo do subtotal em adicionarAoCarrinho
    private void adicionarAoCarrinho(Peca peca) {
        carrinho.stream()
                .filter(item -> item.getPecaId() == peca.getPecaId())
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            // ANTES: Cálculo direto sem explicação
                            // item.setQuantidade(item.getQuantidade() + 1);
                            // item.setSubtotal(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));

                            // DEPOIS: Variáveis explicativas para melhor compreensão
                            int novaQuantidade = item.getQuantidade() + 1;
                            BigDecimal precoUnitario = item.getPrecoUnitario();
                            BigDecimal novoSubtotal = precoUnitario.multiply(BigDecimal.valueOf(novaQuantidade));

                            item.setQuantidade(novaQuantidade);
                            item.setSubtotal(novoSubtotal);
                            tabelaCarrinho.refresh();
                        },
                        () -> {
                            ItemVendaDisplay novoItem = new ItemVendaDisplay(
                                    0,
                                    0,
                                    peca.getPecaId(),
                                    peca.getNome(),
                                    1,
                                    peca.getPreco(),
                                    peca.getPreco()
                            );
                            carrinho.add(novoItem);
                        }
                );
        updateSubtotal();
    }

    @FXML private TableColumn<ItemVendaDisplay, Void> colCarrinhoRemover;

    private void configurarColunaRemover() {
        colCarrinhoRemover.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Remover");

            {
                btn.setOnAction(event -> {
                    ItemVendaDisplay item = getTableView().getItems().get(getIndex());
                    carrinho.remove(item);
                    tabelaCarrinho.setItems(carrinho);
                    updateSubtotal();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void updateSubtotal() {
        BigDecimal total = carrinho.stream()
                .map(ItemVendaDisplay::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        lblSubtotal.setText(String.format("R$ %.2f", total));
    }

    @FXML
    private void btnConcluirVenda() {
        if (carrinho.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Carrinho Vazio", "Adicione itens ao carrinho antes de concluir a venda.");
            return;
        }

        Cliente clienteSelecionado = cbClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado == null) {
            showAlert(Alert.AlertType.WARNING, "Nenhum Cliente Selecionado", "Por favor, selecione um cliente para a venda.");
            return;
        }

        try {
            // REFATORAÇÃO 8: Extract Variable - Para cálculo do valor total da venda
            // ANTES: Cálculo complexo inline
            // BigDecimal valorTotalVenda = carrinho.stream()
            //         .map(ItemVendaDisplay::getSubtotal)
            //         .reduce(BigDecimal.ZERO, BigDecimal::add);

            // DEPOIS: Variável extraída para melhor legibilidade
            BigDecimal valorTotalVenda = carrinho.stream()
                    .map(ItemVendaDisplay::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Venda novaVenda = new Venda(
                    0,
                    clienteSelecionado.getClienteId(),
                    LocalDateTime.now(),
                    valorTotalVenda
            );
            vendaDAO.insert(novaVenda);

            for (ItemVendaDisplay item : carrinho) {
                itemVendaDAO.insert(new ItemVenda(
                        0,
                        novaVenda.getVendaId(),
                        item.getPecaId(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.getSubtotal()
                ));
            }

            showAlert(Alert.AlertType.INFORMATION, "Venda Concluída", "Venda realizada com sucesso!");
            carrinho.clear();
            updateSubtotal();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao concluir venda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void btnVendasOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/unicentro/gpmotos/view/TelaVendasRealizadas.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Historico de Vendas");

            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.centerOnScreen();

            stage.show();

            Stage telaAtual = (Stage) ((Button) event.getSource()).getScene().getWindow();
            telaAtual.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnCadastroClientesOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/unicentro/gpmotos/view/TelaCadastroClientes.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Clientes");

            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.centerOnScreen();

            stage.show();

            Stage telaAtual = (Stage) ((Button) event.getSource()).getScene().getWindow();
            telaAtual.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnCadastroPecasOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/unicentro/gpmotos/view/TelaCadastroPecas.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Peças");

            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.centerOnScreen();

            stage.show();

            Stage telaAtual = (Stage) ((Button) event.getSource()).getScene().getWindow();
            telaAtual.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnSairOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/unicentro/gpmotos/view/TelaInicial.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Peças");

            Scene scene = new Scene(root, 700, 500);
            stage.setScene(scene);

            stage.setResizable(false);
            stage.centerOnScreen();

            stage.show();

            Stage telaAtual = (Stage) ((Button) event.getSource()).getScene().getWindow();
            telaAtual.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível voltar para a tela inicial.");
        }
    }

    @FXML
    private void btnLimparFiltrosOnAction(ActionEvent event) {
        cbMarca.getSelectionModel().clearSelection();
        cbModelo.getSelectionModel().clearSelection();
        cbCategoria.getSelectionModel().clearSelection();
        cbClientes.getSelectionModel().clearSelection();
        tfBusca.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class ItemVendaDisplay {
        private final IntegerProperty itemVendaId;
        private final IntegerProperty vendaId;
        private final IntegerProperty pecaId;
        private final StringProperty nomePeca;
        private final IntegerProperty quantidade;
        private final ObjectProperty<BigDecimal> precoUnitario;
        private final ObjectProperty<BigDecimal> subtotal;

        public ItemVendaDisplay(int itemVendaId, int vendaId, int pecaId, String nomePeca, int quantidade, BigDecimal precoUnitario, BigDecimal subtotal) {
            this.itemVendaId = new SimpleIntegerProperty(itemVendaId);
            this.vendaId = new SimpleIntegerProperty(vendaId);
            this.pecaId = new SimpleIntegerProperty(pecaId);
            this.nomePeca = new SimpleStringProperty(nomePeca);
            this.quantidade = new SimpleIntegerProperty(quantidade);
            this.precoUnitario = new SimpleObjectProperty<>(precoUnitario);
            this.subtotal = new SimpleObjectProperty<>(subtotal);
        }

        public int getItemVendaId() { return itemVendaId.get(); }
        public IntegerProperty itemVendaIdProperty() { return itemVendaId; }
        public void setItemVendaId(int itemVendaId) { this.itemVendaId.set(itemVendaId); }

        public int getVendaId() { return vendaId.get(); }
        public IntegerProperty vendaIdProperty() { return vendaId; }
        public void setVendaId(int vendaId) { this.vendaId.set(vendaId); }

        public int getPecaId() { return pecaId.get(); }
        public IntegerProperty pecaIdProperty() { return pecaId; }
        public void setPecaId(int pecaId) { this.pecaId.set(pecaId); }

        public String getNomePeca() { return nomePeca.get(); }
        public StringProperty nomePecaProperty() { return nomePeca; }
        public void setNomePeca(String nomePeca) { this.nomePeca.set(nomePeca); }

        public int getQuantidade() { return quantidade.get(); }
        public IntegerProperty quantidadeProperty() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade.set(quantidade); }

        public BigDecimal getPrecoUnitario() { return precoUnitario.get(); }
        public ObjectProperty<BigDecimal> precoUnitarioProperty() { return precoUnitario; }
        public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario.set(precoUnitario); }

        public BigDecimal getSubtotal() { return subtotal.get(); }
        public ObjectProperty<BigDecimal> subtotalProperty() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal.set(subtotal); }
    }

    public void setTfBusca(TextField tfBusca) {
        this.tfBusca = tfBusca;
    }
    public void setCbMarca(ComboBox<String> cbMarca) {
        this.cbMarca = cbMarca;
    }
    public void setCbModelo(ComboBox<String> cbModelo) {
        this.cbModelo = cbModelo;
    }
    public void setCbCategoria(ComboBox<String> cbCategoria) {
        this.cbCategoria = cbCategoria;
    }
    public void setPecaDAO(PecaDAO pecaDAO) {
        this.pecaDAO = pecaDAO;
    }
    public void buscarPecasPublic() {
        buscarPecas();
    }
    public void initForTest(TextField tfBusca, ComboBox<String> cbMarca,
                            ComboBox<String> cbModelo, ComboBox<String> cbCategoria,
                            PecaDAO pecaDAO) {
        this.tfBusca = tfBusca;
        this.cbMarca = cbMarca;
        this.cbModelo = cbModelo;
        this.cbCategoria = cbCategoria;
        this.pecaDAO = pecaDAO;
    }
}