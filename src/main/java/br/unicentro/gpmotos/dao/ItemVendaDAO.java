package br.unicentro.gpmotos.dao;

import br.unicentro.gpmotos.model.ItemVenda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaDAO implements GenericDAO<ItemVenda, Integer> {

    @Override
    public void insert(ItemVenda itemVenda) throws SQLException {
        String sql = "INSERT INTO ItensVenda (vendaId, pecaId, quantidade, precoUnitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, itemVenda.getVendaId());
            stmt.setInt(2, itemVenda.getPecaId());
            stmt.setInt(3, itemVenda.getQuantidade());
            stmt.setBigDecimal(4, itemVenda.getPrecoUnitario());
            stmt.setBigDecimal(5, itemVenda.getSubtotal());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    itemVenda.setItemVendaId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(ItemVenda itemVenda) throws SQLException {
        String sql = "UPDATE ItensVenda SET vendaId = ?, pecaId = ?, quantidade = ?, precoUnitario = ?, subtotal = ? WHERE itemVendaId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemVenda.getVendaId());
            stmt.setInt(2, itemVenda.getPecaId());
            stmt.setInt(3, itemVenda.getQuantidade());
            stmt.setBigDecimal(4, itemVenda.getPrecoUnitario());
            stmt.setBigDecimal(5, itemVenda.getSubtotal());
            stmt.setInt(6, itemVenda.getItemVendaId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM ItensVenda WHERE itemVendaId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public ItemVenda findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM ItensVenda WHERE itemVendaId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ItemVenda(
                            rs.getInt("itemVendaId"),
                            rs.getInt("vendaId"),
                            rs.getInt("pecaId"),
                            rs.getInt("quantidade"),
                            rs.getBigDecimal("precoUnitario"),
                            rs.getBigDecimal("subtotal")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<ItemVenda> findAll() throws SQLException {
        List<ItemVenda> itensVenda = new ArrayList<>();
        String sql = "SELECT * FROM ItensVenda";
        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                itensVenda.add(new ItemVenda(
                        rs.getInt("itemVendaId"),
                        rs.getInt("vendaId"),
                        rs.getInt("pecaId"),
                        rs.getInt("quantidade"),
                        rs.getBigDecimal("precoUnitario"),
                        rs.getBigDecimal("subtotal")
                ));
            }
        }
        return itensVenda;
    }

    public List<ItemVenda> findByVendaId(int vendaId) throws SQLException {
        List<ItemVenda> itensVenda = new ArrayList<>();
        String sql = "SELECT * FROM ItensVenda WHERE vendaId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vendaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itensVenda.add(new ItemVenda(
                            rs.getInt("itemVendaId"),
                            rs.getInt("vendaId"),
                            rs.getInt("pecaId"),
                            rs.getInt("quantidade"),
                            rs.getBigDecimal("precoUnitario"),
                            rs.getBigDecimal("subtotal")
                    ));
                }
            }
        }
        return itensVenda;
    }
}
