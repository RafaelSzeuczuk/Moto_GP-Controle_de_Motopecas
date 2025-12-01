package br.unicentro.gpmotos.dao;

import br.unicentro.gpmotos.model.Venda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static br.unicentro.gpmotos.dao.Conexao.PASSWORD;
import static br.unicentro.gpmotos.dao.Conexao.URL;
import static br.unicentro.gpmotos.dao.Conexao.USER;

public class VendaDAO implements GenericDAO<Venda, Integer> {

    @Override
    /**
    public void insert(Venda venda) throws SQLException {
        String sql = "INSERT INTO Vendas (clienteId, dataVenda, valorTotal) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, venda.getClienteId());
            stmt.setTimestamp(2, Timestamp.valueOf(venda.getDataVenda()));
            stmt.setBigDecimal(3, venda.getValorTotal());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    venda.setVendaId(rs.getInt(1));
                }
            }
        }
    }
     Motivo da refatoracao Conexao.getConexao(), pode ser eliminado
*/
    public void insert(Venda venda) throws SQLException {
        String sql = "INSERT INTO Vendas (clienteId, dataVenda, valorTotal) VALUES (?, ?, ?)";
        try ( Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, venda.getClienteId());
            stmt.setTimestamp(2, Timestamp.valueOf(venda.getDataVenda()));
            stmt.setBigDecimal(3, venda.getValorTotal());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    venda.setVendaId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Venda venda) throws SQLException {
        String sql = "UPDATE Vendas SET clienteId = ?, dataVenda = ?, valorTotal = ? WHERE vendaId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, venda.getClienteId());
            stmt.setTimestamp(2, Timestamp.valueOf(venda.getDataVenda()));
            stmt.setBigDecimal(3, venda.getValorTotal());
            stmt.setInt(4, venda.getVendaId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Vendas WHERE vendaId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Venda findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Vendas WHERE vendaId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Venda(
                            rs.getInt("vendaId"),
                            rs.getInt("clienteId"),
                            rs.getTimestamp("dataVenda").toLocalDateTime(),
                            rs.getBigDecimal("valorTotal")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Venda> findAll() throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM Vendas";
        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vendas.add(new Venda(
                        rs.getInt("vendaId"),
                        rs.getInt("clienteId"),
                        rs.getTimestamp("dataVenda").toLocalDateTime(),
                        rs.getBigDecimal("valorTotal")
                ));
            }
        }
        return vendas;
    }

    public List<Venda> findAllComCliente() throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        String sql = """
        SELECT v.vendaId, v.clienteId, c.nome AS nomeCliente, v.dataVenda, v.valorTotal
        FROM Vendas v
        JOIN Clientes c ON v.clienteId = c.clienteId
        ORDER BY v.dataVenda DESC
    """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venda venda = new Venda(
                        rs.getInt("vendaId"),
                        rs.getInt("clienteId"),
                        rs.getTimestamp("dataVenda").toLocalDateTime(),
                        rs.getBigDecimal("valorTotal")
                );
                venda.setNomeCliente(rs.getString("nomeCliente"));
                vendas.add(venda);
            }
        }

        return vendas;
    }

}
