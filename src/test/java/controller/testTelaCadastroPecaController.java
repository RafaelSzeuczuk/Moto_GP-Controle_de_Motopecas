package controller;

import br.unicentro.gpmotos.controller.TelaCadastroPecaController;
import br.unicentro.gpmotos.dao.PecaDAO;
import br.unicentro.gpmotos.model.Peca;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class testTelaCadastroPecaController {

    private TelaCadastroPecaController controller;
    private PecaDAO pecaDAO;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setup() {
        TextField tfNomePeca = new TextField("Filtro de Óleo");
        TextField tfMarcaPeca = new TextField("Honda");
        TextField tfModeloPeca = new TextField("CG 160");
        TextField tfCategoriaPeca = new TextField("Motor");
        TextField tfFornecedorPeca = new TextField("Motoparts");
        TextField tfPrecoPeca = new TextField("25.00");
        Button btnExcluirPeca = new Button();
        TableView<Peca> tabelaPecasCadastro = new TableView<>();

        pecaDAO = mock(PecaDAO.class);

        controller = new TelaCadastroPecaController();
        controller.initForTest(tfNomePeca, tfMarcaPeca, tfModeloPeca, tfCategoriaPeca,
                tfFornecedorPeca, tfPrecoPeca, btnExcluirPeca, tabelaPecasCadastro, pecaDAO);
    }

    //Rafael
    @Test
    void testSalvarNovaPeca() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.handleSalvarPeca(new ActionEvent());
            latch.countDown();
        });

        latch.await();

        verify(pecaDAO).insert(argThat(p -> {
            assertNotNull(p, "A peça retornada está nula — verifique se os campos foram preenchidos corretamente.");
            assertEquals("Filtro de Óleo", p.getNome(), "Nome da peça incorreto — esperado: 'Filtro de Óleo'");
            assertEquals("Honda", p.getMarca(), "Marca da peça incorreta — esperado: 'Honda'");
            assertEquals("CG 160", p.getModelo(), "Modelo da peça incorreto — esperado: 'CG 160'");
            assertEquals("Motor", p.getCategoria(), "Categoria da peça incorreta — esperado: 'Motor'");
            assertEquals("Motoparts", p.getFornecedor(), "Fornecedor da peça incorreto — esperado: 'Motoparts'");
            assertEquals(new BigDecimal("25.00"), p.getPreco(), "Preço da peça incorreto — esperado: 25.00");
            return true;
        }));
    }

}
