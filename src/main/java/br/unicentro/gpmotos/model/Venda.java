package br.unicentro.gpmotos.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Venda {

    private final IntegerProperty vendaId;
    private final IntegerProperty clienteId;
    private final ObjectProperty<LocalDateTime> dataVenda;
    private final ObjectProperty<BigDecimal> valorTotal;

    public Venda(int vendaId, int clienteId, LocalDateTime dataVenda, BigDecimal valorTotal) {
        this.vendaId = new SimpleIntegerProperty(vendaId);
        this.clienteId = new SimpleIntegerProperty(clienteId);
        this.dataVenda = new SimpleObjectProperty<>(dataVenda);
        this.valorTotal = new SimpleObjectProperty<>(valorTotal);
    }


    // Getters e Setters
    public int getVendaId() {
        return vendaId.get();
    }

    public IntegerProperty vendaIdProperty() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId.set(vendaId);
    }

    public int getClienteId() {
        return clienteId.get();
    }

    public IntegerProperty clienteIdProperty() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId.set(clienteId);
    }

    public LocalDateTime getDataVenda() {
        return dataVenda.get();
    }

    public ObjectProperty<LocalDateTime> dataVendaProperty() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda.set(dataVenda);
    }

    public BigDecimal getValorTotal() {
        return valorTotal.get();
    }

    public ObjectProperty<BigDecimal> valorTotalProperty() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal.set(valorTotal);
    }

    private final ObjectProperty<String> nomeCliente = new SimpleObjectProperty<>();

    public String getNomeCliente() {
        return nomeCliente.get();
    }

    public ObjectProperty<String> nomeClienteProperty() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente.set(nomeCliente);
    }

    public String getDataFormatada() {
        return dataVenda.get().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

}
