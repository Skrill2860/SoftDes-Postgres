package com.Skrill2860.service.db;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * This class helps to avoid problems with connection leaks,
 * because all direct operations with the database executes inside the class.
 */
public class JdbcTemplate {
    private final HikariDataSource connectionPool;

    @FunctionalInterface
    public interface SQLFunction<T, R> {
        R apply(T object) throws SQLException;
    }

    @FunctionalInterface
    public interface SQLConsumer<T> {
        void accept(T object) throws SQLException;
    }

    public void connection(SQLConsumer<? super Connection> consumer) throws SQLException {
        Objects.requireNonNull(consumer);
        try (Connection conn = connectionPool.getConnection()) {
            consumer.accept(conn);
        }
    }

    public <R> R connection(SQLFunction<? super Connection, ? extends R> function) throws SQLException {
        Objects.requireNonNull(function);
        try (Connection conn = connectionPool.getConnection()) {
            return function.apply(conn);
        }
    }

    public void statement(SQLConsumer<? super Statement> consumer) throws SQLException {
        Objects.requireNonNull(consumer);
        connection(conn -> {
            try (Statement stmt = conn.createStatement()) {
                consumer.accept(stmt);
            }
        });
    }

    public <R> R statement(SQLFunction<? super Statement, ? extends R> function) throws SQLException {
        Objects.requireNonNull(function);
        return connection(conn -> {
            try (Statement stmt = conn.createStatement()) {
                return function.apply(stmt);
            }
        });
    }

    public <R> R preparedStatement(String sql, SQLFunction<? super PreparedStatement, ? extends R> function) throws SQLException {
        Objects.requireNonNull(function);
        return connection(conn -> {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                return function.apply(pstmt);
            }
        });
    }

    public void preparedStatement(String sql, SQLConsumer<? super PreparedStatement> consumer) throws SQLException {
        Objects.requireNonNull(consumer);
        connection(conn -> {
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                consumer.accept(pstmt);
            }
        });
    }

    public JdbcTemplate(HikariDataSource connectionPool) {
        this.connectionPool = connectionPool;
    }
}
