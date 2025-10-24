package br.unicentro.gpmotos.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

public class ItemVenda {
    private final IntegerProperty itemVendaId;
    private final IntegerProperty vendaId;
    private final IntegerProperty pecaId;
    private final IntegerProperty quantidade;
    private final ObjectProperty<BigDecimal> precoUnitario;
    private final ObjectProperty<BigDecimal> subtotal;

    public ItemVenda(int itemVendaId, int vendaId, int pecaId, int quantidade, BigDecimal precoUnitario, BigDecimal subtotal) {
        this.itemVendaId = new SimpleIntegerProperty(itemVendaId);
        this.vendaId = new SimpleIntegerProperty(vendaId);

        this.pecaId = new SimpleIntegerProperty(pecaId);
        this.quantidade = new SimpleIntegerProperty(quantidade);
        this.precoUnitario = new SimpleObjectProperty<>(precoUnitario);
        this.subtotal = new SimpleObjectProperty<>(subtotal);
    }

    // Getters e Setters
    public int getItemVendaId() {
        return itemVendaId.get();
    }

    public IntegerProperty itemVendaIdProperty() {
        return itemVendaId;
    }

    public void setItemVendaId(int itemVendaId) {
        this.itemVendaId.set(itemVendaId);
    }

    public int getVendaId() {
        return vendaId.get();
    }

    public IntegerProperty vendaIdProperty() {
        return vendaId;
    }

    public void setVendaId(int vendaId) {
        this.vendaId.set(vendaId);
    }

    public int getPecaId() {
        return pecaId.get();
    }

    public IntegerProperty pecaIdProperty() {
        return pecaId;
    }

    public void setPecaId(int pecaId) {
        this.pecaId.set(pecaId);
    }

    public int getQuantidade() {
        return quantidade.get();
    }

    public IntegerProperty quantidadeProperty() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade.set(quantidade);
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario.get();
    }

    public ObjectProperty<BigDecimal> precoUnitarioProperty() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario.set(precoUnitario);
    }

    public BigDecimal getSubtotal() {
        return subtotal.get();
    }

    public ObjectProperty<BigDecimal> subtotalProperty() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal.set(subtotal);
    }
}
