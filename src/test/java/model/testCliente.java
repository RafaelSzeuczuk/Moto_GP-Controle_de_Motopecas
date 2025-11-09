package model;

import br.unicentro.gpmotos.model.Cliente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class testCliente {
    //Henrique
    @Test
    public void testConstrutorInicializaCorretamente() {
        Cliente cliente = new Cliente(1, "João da Silva");

        assertEquals(1, cliente.getClienteId(), "ID deve ser 1");
        assertEquals("João da Silva", cliente.getNome(), "Nome deve ser 'João da Silva'");
    }

    //Henrique
    @Test
    public void testSettersFuncionamCorretamente() {
        Cliente cliente = new Cliente(2,"Rosana");

        assertEquals(2, cliente.getClienteId(), "ID deve ter sido alterado para 2");
        assertEquals("Rosana", cliente.getNome(), "Nome deve ter sido alterado para 'Rosana'");
    }

}
