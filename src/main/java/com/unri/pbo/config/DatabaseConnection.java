package com.unri.pbo.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String HOST = "localhost:3306";
    private static final String DB   = "db_pos_kelompok5";   
    private static final String USER = "root";
    private static final String PASS = "";             

    private static final String URL = "jdbc:mysql://" + HOST + "/" + DB
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static String cekKoneksi() {
        try (Connection c = getConnection()) {
            return "TERHUBUNG - " + c.getMetaData().getDatabaseProductName()
                    + " versi " + c.getMetaData().getDatabaseProductVersion();
        } catch (SQLException e) {
            return "GAGAL TERHUBUNG: " + e.getMessage();
        }
    }

    public static String nextId(String tabel, String kolom, String prefix) throws SQLException {
        String sql = "SELECT " + kolom + " FROM " + tabel
                + " ORDER BY LENGTH(" + kolom + ") DESC, " + kolom + " DESC LIMIT 1";
        try (Connection c = getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            int n = 0;
            if (rs.next()) {
                try {
                    n = Integer.parseInt(rs.getString(1).substring(prefix.length()));
                } catch (RuntimeException e) {
                    n = 0;
                }
            }
            return String.format("%s%03d", prefix, n + 1);
        }
    }

    public static List<Object[]> baca(String sql, Object... param) throws SQLException {
        List<Object[]> hasil = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < param.length; i++) {
                ps.setObject(i + 1, param[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                int n = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Object[] baris = new Object[n];
                    for (int i = 0; i < n; i++) {
                        baris[i] = rs.getObject(i + 1);
                    }
                    hasil.add(baris);
                }
            }
        }
        return hasil;
    }
}