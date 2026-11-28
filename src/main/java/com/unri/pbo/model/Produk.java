package com.unri.pbo.model;

import com.unri.pbo.config.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Produk {

    private String idProduk;
    private String namaProduk;
    private BigDecimal hargaBeli;
    private BigDecimal hargaJual;
    private int stok;

    public Produk() {
    }

    public Produk(String idProduk, String namaProduk, BigDecimal hargaBeli, BigDecimal hargaJual, int stok) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.hargaBeli = hargaBeli;
        this.hargaJual = hargaJual;
        this.stok = stok;
    }

    public String getIdProduk() { return idProduk; }
    public void setIdProduk(String idProduk) { this.idProduk = idProduk; }

    public String getNamaProduk() { return namaProduk; }
    public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }

    public BigDecimal getHargaBeli() { return hargaBeli; }
    public void setHargaBeli(BigDecimal hargaBeli) { this.hargaBeli = hargaBeli; }

    public BigDecimal getHargaJual() { return hargaJual; }
    public void setHargaJual(BigDecimal hargaJual) { this.hargaJual = hargaJual; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    // --- METHOD DAO (AKSES DATABASE) ---

    public List<Produk> getAllProduk() throws SQLException {
        List<Produk> daftar = new ArrayList<>();
        String sql = "SELECT id_produk, nama_produk, harga_beli, harga_jual, stok FROM produk ORDER BY id_produk";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                daftar.add(new Produk(
                    rs.getString("id_produk"),
                    rs.getString("nama_produk"),
                    rs.getBigDecimal("harga_beli"),
                    rs.getBigDecimal("harga_jual"),
                    rs.getInt("stok")
                ));
            }
        }
        return daftar;
    }

    public String generateNextId() throws SQLException {
        String sql = "SELECT id_produk FROM produk ORDER BY LENGTH(id_produk) DESC, id_produk DESC LIMIT 1";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int n = 0;
            if (rs.next()) {
                try {
                    n = Integer.parseInt(rs.getString(1).substring(3));
                } catch (RuntimeException e) {
                    n = 0;
                }
            }
            return String.format("BRG%03d", n + 1);
        }
    }

    public void simpan(Produk p) throws SQLException {
        String sql = "INSERT INTO produk (id_produk, nama_produk, harga_beli, harga_jual, stok) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getIdProduk());
            ps.setString(2, p.getNamaProduk());
            ps.setBigDecimal(3, p.getHargaBeli());
            ps.setBigDecimal(4, p.getHargaJual());
            ps.setInt(5, p.getStok());
            ps.executeUpdate();
        }
    }

    public void ubah(Produk p) throws SQLException {
        String sql = "UPDATE produk SET nama_produk = ?, harga_beli = ?, harga_jual = ?, stok = ? WHERE id_produk = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNamaProduk());
            ps.setBigDecimal(2, p.getHargaBeli());
            ps.setBigDecimal(3, p.getHargaJual());
            ps.setInt(4, p.getStok());
            ps.setString(5, p.getIdProduk());
            ps.executeUpdate();
        }
    }

    public void hapus(String idProduk) throws SQLException {
        String sql = "DELETE FROM produk WHERE id_produk = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idProduk);
            ps.executeUpdate();
        }
    }
}