package controller;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import br.unicentro.gpmotos.controller.TelaVendaController;
import br.unicentro.gpmotos.dao.PecaDAO;
import br.unicentro.gpmotos.model.Peca;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class testTelaVendaController {

    private TelaVendaController controller;
    private TextField tfBusca;
    private ComboBox<String> cbMarca;
    private ComboBox<String> cbModelo;
    private ComboBox<String> cbCategoria;
    private PecaDAO pecaDAO;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setup() {
        tfBusca = new TextField();
        cbMarca = new ComboBox<>();
        cbModelo = new ComboBox<>();
        cbCategoria = new ComboBox<>();
        pecaDAO = mock(PecaDAO.class);

        controller = new TelaVendaController();
        controller.initForTest(tfBusca, cbMarca, cbModelo, cbCategoria, pecaDAO);
    }


    //Rafael
    @Test
    void testBuscarPecasComFiltros() throws SQLException {
        tfBusca.setText("Filtro");
        cbMarca.setValue("Honda");
        cbModelo.setValue("CG 160");
        cbCategoria.setValue("Motor");

        List<Peca> esperado = List.of(
                new Peca(1, "Filtro de Óleo", "Honda", "CG 160", "Motor", "w", new BigDecimal("25.00"))
        );

        when(pecaDAO.search("Filtro", "Honda", "CG 160", "Motor")).thenReturn(esperado);

        controller.buscarPecasPublic();

        verify(pecaDAO).search("Filtro", "Honda", "CG 160", "Motor");

        assertEquals(1, esperado.size(), "Quantidade de peças retornadas incorreta — esperado: 1 peça");
        assertEquals("Filtro de Óleo", esperado.get(0).getNome(), "Nome da peça incorreto — esperado: 'Filtro de Óleo'");
        assertEquals("Honda", esperado.get(0).getMarca(), "Marca da peça incorreta — esperado: 'Honda'");
        assertEquals("CG 160", esperado.get(0).getModelo(), "Modelo da peça incorreto — esperado: 'CG 160'");
        assertEquals("Motor", esperado.get(0).getCategoria(), "Categoria da peça incorreta — esperado: 'Motor'");
        assertEquals(new BigDecimal("25.00"), esperado.get(0).getPreco(), "Preço da peça incorreto — esperado: 25.00");
    }

}
