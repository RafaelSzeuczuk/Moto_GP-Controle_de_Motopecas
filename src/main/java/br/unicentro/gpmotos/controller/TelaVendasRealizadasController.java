package br.unicentro.gpmotos.controller;

import br.unicentro.gpmotos.dao.ItemVendaDAO;
import br.unicentro.gpmotos.dao.VendaDAO;
import br.unicentro.gpmotos.model.ItemVenda;
import br.unicentro.gpmotos.model.Venda;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class TelaVendasRealizadasController implements Initializable {

    @FXML private TableView<Venda> tabelaVendas;
    @FXML private TableColumn<Venda, Integer> colVendaId;
    @FXML private TableColumn<Venda, String> colCliente;
    @FXML private TableColumn<Venda, String> colDataVenda;
    @FXML private TableColumn<Venda, Double> colValorTotal;


    private final VendaDAO vendaDAO = new VendaDAO();
    private final ObservableList<Venda> listaVendas = FXCollections.observableArrayList();
    private final ItemVendaDAO itemVendaDAO = new ItemVendaDAO();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabela();
        carregarVendas();
    }

    private void configurarTabela() {
        colVendaId.setCellValueFactory(new PropertyValueFactory<>("vendaId"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colDataVenda.setCellValueFactory(new PropertyValueFactory<>("dataFormatada"));
        colValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        tabelaVendas.setItems(listaVendas);
    }

    private void carregarVendas() {
        try {
            listaVendas.setAll(vendaDAO.findAllComCliente());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar vendas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVoltar(ActionEvent event) {
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
            showAlert(Alert.AlertType.ERROR, "Erro de navegação", "Não foi possível abrir a tela principal.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExportarPlanilha() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Vendas");

        int rowIndex = 0;

        // Cabeçalho principal
        Row header = sheet.createRow(rowIndex++);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Cliente");
        header.createCell(2).setCellValue("Data da Venda");
        header.createCell(3).setCellValue("Valor Total");

        for (Venda venda : listaVendas) {
            // Linha da venda
            Row vendaRow = sheet.createRow(rowIndex++);
            vendaRow.createCell(0).setCellValue(venda.getVendaId());
            vendaRow.createCell(1).setCellValue(venda.getNomeCliente());
            vendaRow.createCell(2).setCellValue(venda.getDataFormatada());
            vendaRow.createCell(3).setCellValue(venda.getValorTotal().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            // Cabeçalho dos itens
            Row itemHeader = sheet.createRow(rowIndex++);
            itemHeader.createCell(1).setCellValue("Peça");
            itemHeader.createCell(2).setCellValue("Qtd");
            itemHeader.createCell(3).setCellValue("Preço Unitário");
            itemHeader.createCell(4).setCellValue("Subtotal");

            try {
                List<ItemVenda> itens = itemVendaDAO.findByVendaId(venda.getVendaId());
                for (ItemVenda item : itens) {
                    Row itemRow = sheet.createRow(rowIndex++);
                    itemRow.createCell(1).setCellValue("Peça ID " + item.getPecaId());
                    itemRow.createCell(2).setCellValue(item.getQuantidade());
                    itemRow.createCell(3).setCellValue(item.getPrecoUnitario().doubleValue());
                    itemRow.createCell(4).setCellValue(item.getSubtotal().doubleValue());
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erro ao exportar", "Não foi possível carregar os itens da venda " + venda.getVendaId());
                e.printStackTrace();
            }

            rowIndex++;
        }

        for (int i = 0; i <= 4; i++) {
            sheet.autoSizeColumn(i);
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Planilha");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivo Excel (*.xlsx)", "*.xlsx"));
        fileChooser.setInitialFileName("vendas_com_itens.xlsx");
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
                workbook.close();
                showAlert(Alert.AlertType.INFORMATION, "Exportação concluída", "Planilha salva com sucesso.");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erro ao exportar", "Não foi possível salvar a planilha.");
                e.printStackTrace();
            }
        }
    }


    private void showAlert(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
