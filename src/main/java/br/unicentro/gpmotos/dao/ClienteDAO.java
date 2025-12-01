package br.unicentro.gpmotos.dao;

import br.unicentro.gpmotos.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements GenericDAO<Cliente, Integer> {

    @Override
    /**
    public void insert(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Clientes (nome) VALUES (?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setClienteId(rs.getInt(1));
                }
            }
        }
    }
     Motivo: Extrair Cliente cliente
     */
    public void insert(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Clientes (nome) VALUES (?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Método extraído para preencher os parâmetros
            preencherParametrosCliente(stmt, cliente);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setClienteId(rs.getInt(1));
                }
            }
        }
    }

    // Método extraído para melhorar legibilidade e reaproveitamento
    private void preencherParametrosCliente(PreparedStatement stmt, Cliente cliente) throws SQLException {
        stmt.setString(1, cliente.getNome());
    }


    @Override
    public void update(Cliente cliente) throws SQLException {
        String sql = "UPDATE Clientes SET nome = ? WHERE clienteId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setInt(2, cliente.getClienteId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Clientes WHERE clienteId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Cliente findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Clientes WHERE clienteId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("clienteId"),
                            rs.getString("nome")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Cliente> findAll() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes";
        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getInt("clienteId"),
                        rs.getString("nome")
                ));
            }
        }
        return clientes;
    }
}
