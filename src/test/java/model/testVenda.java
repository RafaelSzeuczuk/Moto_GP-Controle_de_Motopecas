package model;

import br.unicentro.gpmotos.model.Venda;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class testVenda {
    //Henrique
    @Test
    void testPropriedadesNaoNulas() {
        LocalDateTime data = LocalDateTime.of(2025, 11, 9, 14, 30);
        Venda venda = new Venda(1,101,data, new BigDecimal("250.00"));
        assertNotNull(venda.vendaIdProperty(), "Propriedade vendaIdProperty está nula — verifique se foi inicializada corretamente.");
        assertNotNull(venda.clienteIdProperty(), "Propriedade clienteIdProperty está nula — verifique se foi inicializada corretamente.");
        assertNotNull(venda.dataVendaProperty(), "Propriedade dataVendaProperty está nula — verifique se foi inicializada corretamente.");
        assertNotNull(venda.valorTotalProperty(), "Propriedade valorTotalProperty está nula — verifique se foi inicializada corretamente.");
        assertNotNull(venda.nomeClienteProperty(), "Propriedade nomeClienteProperty está nula — verifique se foi inicializada corretamente.");
    }

    //Rafael
    @Test
    void testVendaModel_ConstrutorEGetters() {
        // Cenário: Criação de um objeto Venda
        LocalDateTime data = LocalDateTime.now();
        BigDecimal total = new BigDecimal("500.00");
        Venda venda = new Venda(3, 10, data, total);

        // Verificação: Asserções para verificar o estado do objeto
        assertEquals(3, venda.getVendaId(), "O ID da venda deve ser 3.");
        assertEquals(10, venda.getClienteId(), "O ID do cliente na venda deve ser 10.");
        assertEquals(total, venda.getValorTotal(), "O valor total da venda deve ser 500.00.");
    }

    //Henrique
    @Test
    void testDataFormatada() {
        LocalDateTime data = LocalDateTime.of(2025, 11, 9, 14, 30);
        Venda venda = new Venda(1,101,data, new BigDecimal("250.00"));
        venda.setDataVenda(data);
        String dataEsperada = "09/11/2025 14:30";
        assertEquals(dataEsperada, venda.getDataFormatada(), "Formato da data incorreto — esperado: '09/11/2025 14:30'");
    }
}
