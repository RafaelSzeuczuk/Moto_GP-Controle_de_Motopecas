package dao;

import br.unicentro.gpmotos.dao.ClienteDAO;
import br.unicentro.gpmotos.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class testClienteDAO {
    @Mock
    private ClienteDAO clienteDAO;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Rafael
    @Test
    void testClienteDAO_FindAll() throws SQLException {
        // Cenário: O DAO mockado retorna uma lista de 2 clientes
        List<Cliente> clientesMock = new ArrayList<>();
        clientesMock.add(new Cliente(1, "Cliente A"));
        clientesMock.add(new Cliente(2, "Cliente B"));
        when(clienteDAO.findAll()).thenReturn(clientesMock);

        // Ação: Chamar o método findAll
        List<Cliente> clientes = clienteDAO.findAll();

        // Verificação: O método findAll deve ter sido chamado e a lista deve ter o tamanho esperado
        verify(clienteDAO, times(1)).findAll();
        assertNotNull(clientes, "A lista de clientes não deve ser nula.");
        assertEquals(2, clientes.size(), "A lista deve conter 2 clientes.");
    }
}
