package com.unri.pbo.model;

import com.unri.pbo.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Supplier {

    // Ambil semua data supplier -> bungkus ke objek com.unri.pbo.Supplier (JAR dosen)
    public List<com.unri.pbo.Supplier> getAllSupplier() throws SQLException {
        List<com.unri.pbo.Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM supplier ORDER BY id_supplier ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id_supplier");
                String namaPerusahaan = adaKolom(rs, "nama_perusahaan") && rs.getString("nama_perusahaan") != null
                        ? rs.getString("nama_perusahaan")
                        : (adaKolom(rs, "nama") ? rs.getString("nama") : "-");
                String nama = adaKolom(rs, "nama") && rs.getString("nama") != null
                        ? rs.getString("nama") : namaPerusahaan;
                String noTelp = adaKolom(rs, "no_telepon") && rs.getString("no_telepon") != null
                        ? rs.getString("no_telepon") : "-";
                String alamat = adaKolom(rs, "alamat") && rs.getString("alamat") != null
                        ? rs.getString("alamat") : "-";

                // Urutan konstruktor JAR dosen: (id, nama, alamat, noTelepon, namaPerusahaan)
                list.add(new com.unri.pbo.Supplier(id, nama, alamat, noTelp, namaPerusahaan));
            }
        }
        return list;
    }

    // Generate ID otomatis (SUP001, SUP002, dst.)
    public String generateIdSupplier() {
        String sql = "SELECT id_supplier FROM supplier ORDER BY id_supplier DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next() && rs.getString("id_supplier") != null) {
                String kodeTerakhir = rs.getString("id_supplier");
                int angka = Integer.parseInt(kodeTerakhir.replaceAll("[^0-9]", "")) + 1;
                return String.format("SUP%03d", angka);
            }
        } catch (Exception ignored) {
        }
        return "SUP001";
    }

    // Simpan supplier baru dari objek JAR dosen ke MySQL
    public void simpan(com.unri.pbo.Supplier sup) throws SQLException {
        String sql = "INSERT INTO supplier (id_supplier, nama, nama_perusahaan, no_telepon, alamat) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sup.getId());
            ps.setString(2, sup.getNama());
            ps.setString(3, sup.getNamaPerusahaan());
            ps.setString(4, sup.getNoTelepon());
            ps.setString(5, sup.getAlamat());
            ps.executeUpdate();
        }
    }

    // Ubah data supplier yang sudah ada di MySQL
    public void ubah(com.unri.pbo.Supplier sup) throws SQLException {
        String sql = "UPDATE supplier SET nama = ?, nama_perusahaan = ?, no_telepon = ?, alamat = ? WHERE id_supplier = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sup.getNama());
            ps.setString(2, sup.getNamaPerusahaan());
            ps.setString(3, sup.getNoTelepon());
            ps.setString(4, sup.getAlamat());
            ps.setString(5, sup.getId());
            ps.executeUpdate();
        }
    }

    // Hapus supplier berdasarkan id_supplier
    public void hapus(String idSupplier) throws SQLException {
        String sql = "DELETE FROM supplier WHERE id_supplier = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idSupplier);
            ps.executeUpdate();
        }
    }

    private boolean adaKolom(ResultSet rs, String namaKolom) {
        try {
            rs.findColumn(namaKolom);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}