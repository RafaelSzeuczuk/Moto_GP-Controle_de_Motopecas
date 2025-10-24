package br.unicentro.gpmotos.dao;

import br.unicentro.gpmotos.model.Peca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PecaDAO implements GenericDAO<Peca, Integer> {

    @Override
    public void insert(Peca peca) throws SQLException {
        String sql = "INSERT INTO Pecas (nome, marca, modelo, categoria, fornecedor, preco) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, peca.getNome());
            stmt.setString(2, peca.getMarca());
            stmt.setString(3, peca.getModelo());
            stmt.setString(4, peca.getCategoria());
            stmt.setString(5, peca.getFornecedor());
            stmt.setBigDecimal(6, peca.getPreco());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    peca.setPecaId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(Peca peca) throws SQLException {
        String sql = "UPDATE Pecas SET nome = ?, marca = ?, modelo = ?, categoria = ?, fornecedor = ?, preco = ? WHERE pecaId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, peca.getNome());
            stmt.setString(2, peca.getMarca());
            stmt.setString(3, peca.getModelo());
            stmt.setString(4, peca.getCategoria());
            stmt.setString(5, peca.getFornecedor());;
            stmt.setBigDecimal(6, peca.getPreco());
            stmt.setInt(7, peca.getPecaId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Pecas WHERE pecaId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Peca findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Pecas WHERE pecaId = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Peca(
                            rs.getInt("pecaId"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("modelo"),
                            rs.getString("categoria"),
                            rs.getString("fornecedor"),
                            rs.getBigDecimal("preco")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Peca> findAll() throws SQLException {
        List<Peca> pecas = new ArrayList<>();
        String sql = "SELECT * FROM Pecas";
        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pecas.add(new Peca(
                        rs.getInt("pecaId"),
                        rs.getString("nome"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("categoria"),
                        rs.getString("fornecedor"),
                        rs.getBigDecimal("preco")
                ));
            }
        }
        return pecas;
    }

    public List<Peca> search(String searchTerm, String marca, String modelo, String categoria) throws SQLException {
        List<Peca> pecas = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Pecas WHERE 1=1");

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" AND (LOWER(nome) LIKE ? OR LOWER(marca) LIKE ? OR LOWER(modelo) LIKE ? OR LOWER(categoria) LIKE ? OR LOWER(fornecedor) LIKE ?)");
        }
        if (marca != null && !marca.trim().isEmpty()) {
            sql.append(" AND LOWER(marca) = ?");
        }
        if (modelo != null && !modelo.trim().isEmpty()) {
            sql.append(" AND LOWER(modelo) = ?");
        }
        if (categoria != null && !categoria.trim().isEmpty()) {
            sql.append(" AND LOWER(categoria) = ?");
        }

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String likeTerm = "%" + searchTerm.toLowerCase() + "%";
                stmt.setString(paramIndex++, likeTerm);
                stmt.setString(paramIndex++, likeTerm);
                stmt.setString(paramIndex++, likeTerm);
                stmt.setString(paramIndex++, likeTerm);
                stmt.setString(paramIndex++, likeTerm);
            }
            if (marca != null && !marca.trim().isEmpty()) {
                stmt.setString(paramIndex++, marca.toLowerCase());
            }
            if (modelo != null && !modelo.trim().isEmpty()) {
                stmt.setString(paramIndex++, modelo.toLowerCase());
            }
            if (categoria != null && !categoria.trim().isEmpty()) {
                stmt.setString(paramIndex++, categoria.toLowerCase());
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pecas.add(new Peca(
                            rs.getInt("pecaId"),
                            rs.getString("nome"),
                            rs.getString("marca"),
                            rs.getString("modelo"),
                            rs.getString("categoria"),
                            rs.getString("fornecedor"),
                            rs.getBigDecimal("preco")
                    ));
                }
            }
        }
        return pecas;
    }
}
