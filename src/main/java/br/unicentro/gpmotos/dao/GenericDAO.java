package br.unicentro.gpmotos.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T, ID> {
    void insert(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void delete(ID id) throws SQLException;
    T findById(ID id) throws SQLException;
    List<T> findAll() throws SQLException;
}
