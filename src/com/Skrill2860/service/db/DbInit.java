package com.Skrill2860.service.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * Class for initializing the database
 * Source to generate table /resources/dbinit.sql
 */
public class DbInit {
    private final JdbcTemplate source;

    private String getSQL(String name) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(name, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    public void create() throws SQLException, IOException {
        String sql = getSQL("resources/dbinit.sql");
        source.statement(stmt -> {
            stmt.execute(sql);
        });
    }

    public DbInit(JdbcTemplate source) {
        this.source = source;
    }
}