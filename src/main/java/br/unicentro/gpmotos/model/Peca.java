package br.unicentro.gpmotos.model;

import javafx.beans.property.*;

import java.math.BigDecimal;

public class Peca {
    private IntegerProperty pecaId;
    private StringProperty nome;
    private StringProperty marca;
    private StringProperty modelo;
    private StringProperty categoria;
    private StringProperty fornecedor;
    private ObjectProperty<BigDecimal> preco;

/**
    public Peca(int pecaId, String nome, String marca, String modelo, String categoria, String fornecedor, BigDecimal preco) {
        this.pecaId = new SimpleIntegerProperty(pecaId);
        this.nome = new SimpleStringProperty(nome);
        this.marca = new SimpleStringProperty(marca);
        this.modelo = new SimpleStringProperty(modelo);
        this.categoria = new SimpleStringProperty(categoria);
        this.fornecedor = new SimpleStringProperty(fornecedor);
        this.preco = new SimpleObjectProperty<>(preco);
    }
*/
//Motivo da refatoracao, o metodo esta muito longo para consertar isso se e feito um construtor com uma versao mais curta:
    public Peca(int pecaId, String nome, String marca, String modelo,String categoria, String fornecedor, BigDecimal preco) {
        inicia_Peca(pecaId, nome, marca, modelo, categoria, fornecedor, preco);
    }



    public void inicia_Peca(int pecaId, String nome, String marca, String modelo, String categoria, String fornecedor, BigDecimal preco) {
        this.pecaId = new SimpleIntegerProperty(pecaId);
        this.nome = new SimpleStringProperty(nome);
        this.marca = new SimpleStringProperty(marca);
        this.modelo = new SimpleStringProperty(modelo);
        this.categoria = new SimpleStringProperty(categoria);
        this.fornecedor = new SimpleStringProperty(fornecedor);
        this.preco = new SimpleObjectProperty<>(preco);
    }
    // Getters e Setters para todas as propriedades
    public int getPecaId() {
        return pecaId.get();
    }

    public IntegerProperty pecaIdProperty() {
        return pecaId;
    }

    public void setPecaId(int pecaId) {
        this.pecaId.set(pecaId);
    }

    public String getNome() {
        return nome.get();
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getMarca() {
        return marca.get();
    }

    public StringProperty marcaProperty() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca.set(marca);
    }

    public String getModelo() {
        return modelo.get();
    }

    public StringProperty modeloProperty() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo.set(modelo);
    }

    public String getCategoria() {
        return categoria.get();
    }

    public StringProperty categoriaProperty() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria.set(categoria);
    }

    public String getFornecedor() {
        return fornecedor.get();
    }

    public StringProperty fornecedorProperty() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor.set(fornecedor);
    }

    public BigDecimal getPreco() {
        return preco.get();
    }

    public ObjectProperty<BigDecimal> precoProperty() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco.set(preco);
    }
}
