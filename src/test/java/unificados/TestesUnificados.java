package unificados;



import br.unicentro.gpmotos.controller.TelaCadastroPecaController;
import br.unicentro.gpmotos.controller.TelaVendaController;
import br.unicentro.gpmotos.dao.ClienteDAO;
import br.unicentro.gpmotos.dao.PecaDAO;
import br.unicentro.gpmotos.model.Cliente;
import br.unicentro.gpmotos.model.Peca;
import br.unicentro.gpmotos.model.Venda;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestesUnificados {

    private TelaCadastroPecaController cadastroController;
    private TelaVendaController vendaController;

    @Mock private ClienteDAO clienteDAO;
    @Mock private PecaDAO pecaDAO;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Setup para TelaCadastroPecaController
        cadastroController = new TelaCadastroPecaController();
        setPrivateField(cadastroController, "tfNomePeca", new TextField("Filtro de Óleo"));
        setPrivateField(cadastroController, "tfMarcaPeca", new TextField("Honda"));
        setPrivateField(cadastroController, "tfModeloPeca", new TextField("CG 160"));
        setPrivateField(cadastroController, "tfCategoriaPeca", new TextField("Motor"));
        setPrivateField(cadastroController, "tfFornecedorPeca", new TextField("Motoparts"));
        setPrivateField(cadastroController, "tfPrecoPeca", new TextField("25.00"));
        setPrivateField(cadastroController, "btnExcluirPeca", new Button());
        setPrivateField(cadastroController, "tabelaPecasCadastro", new TableView<>());
        setPrivateField(cadastroController, "pecaDAO", pecaDAO);

        // Setup para TelaVendaController
        vendaController = new TelaVendaController();
        setPrivateField(vendaController, "tfBusca", new TextField());
        setPrivateField(vendaController, "cbMarca", new ComboBox<>());
        setPrivateField(vendaController, "cbModelo", new ComboBox<>());
        setPrivateField(vendaController, "cbCategoria", new ComboBox<>());
        setPrivateField(vendaController, "pecaDAO", pecaDAO);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void invokePrivateMethod(Object target, String methodName, Object... args) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName, ActionEvent.class);
        method.setAccessible(true);
        method.invoke(target, args);
    }

    private void invokePrivateMethodNoArgs(Object target, String methodName) throws Exception {
        Method method = target.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(target);
    }

    // Teste 1: Salvar nova peça
    @Test
    void testSalvarNovaPeca() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                invokePrivateMethod(cadastroController, "handleSalvarPeca", mock(ActionEvent.class));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
        });
        latch.await();

        verify(pecaDAO).insert(argThat(p -> {
            assertNotNull(p, "A peça não deveria ser nula.");
            assertEquals("Filtro de Óleo", p.getNome(), "O nome da peça está incorreto.");
            assertEquals("Honda", p.getMarca(), "A marca da peça está incorreta.");
            assertEquals("CG 160", p.getModelo(), "O modelo da peça está incorreto.");
            assertEquals("Motor", p.getCategoria(), "A categoria da peça está incorreta.");
            assertEquals("Motoparts", p.getFornecedor(), "O fornecedor da peça está incorreto.");
            assertEquals(new BigDecimal("25.00"), p.getPreco(), "O preço da peça está incorreto.");
            return true;
        }));
    }

    // Teste 2: Buscar peças com filtros
    @Test
    void testBuscarPecasComFiltros() throws Exception {
        ((TextField) getPrivateField(vendaController, "tfBusca")).setText("Filtro");
        ((ComboBox<String>) getPrivateField(vendaController, "cbMarca")).setValue("Honda");
        ((ComboBox<String>) getPrivateField(vendaController, "cbModelo")).setValue("CG 160");
        ((ComboBox<String>) getPrivateField(vendaController, "cbCategoria")).setValue("Motor");

        List<Peca> esperado = List.of(
                new Peca(1, "Filtro de Óleo", "Honda", "CG 160", "Motor", "w", new BigDecimal("25.00"))
        );

        when(pecaDAO.search("Filtro", "Honda", "CG 160", "Motor")).thenReturn(esperado);

        invokePrivateMethodNoArgs(vendaController, "buscarPecas");

        verify(pecaDAO).search("Filtro", "Honda", "CG 160", "Motor");
        assertEquals(1, esperado.size(), "Deveria retornar uma peça.");
        assertEquals("Filtro de Óleo", esperado.get(0).getNome(), "Nome da peça incorreto.");
    }

    // Teste 3: Buscar todos os clientes
    @Test
    void testClienteDAO_FindAll() throws SQLException {
        List<Cliente> clientesMock = List.of(
                new Cliente(1, "Cliente A"),
                new Cliente(2, "Cliente B")
        );
        when(clienteDAO.findAll()).thenReturn(clientesMock);

        List<Cliente> clientes = clienteDAO.findAll();

        verify(clienteDAO, times(1)).findAll();
        assertNotNull(clientes, "A lista de clientes não deve ser nula.");
        assertEquals(2, clientes.size(), "A lista deve conter 2 clientes.");
    }

    // Teste 4: Verificar construtor da venda
    @Test
    void testVendaModel_ConstrutorEGetters() {
        LocalDateTime data = LocalDateTime.now();
        BigDecimal total = new BigDecimal("500.00");
        Venda venda = new Venda(3, 10, data, total);

        assertEquals(3, venda.getVendaId(), "O ID da venda deve ser 3.");
        assertEquals(10, venda.getClienteId(), "O ID do cliente na venda deve ser 10.");
        assertEquals(total, venda.getValorTotal(), "O valor total da venda deve ser 500.00.");
    }

    private Object getPrivateField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}
