/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unri.pbo.model;

import com.unri.pbo.config.DatabaseConnection;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Pelanggan {
    private String kodePelanggan;
    private String namaInstansi;
    private String alamat;
    private String noTelepon;
    private String email;

    // Getter & Setter (sesuaikan jika memakai extends dari ProjectPOS.jar)
    public String getKodePelanggan() { return kodePelanggan; }
    public void setKodePelanggan(String kodePelanggan) { this.kodePelanggan = kodePelanggan; }
    public String getNamaInstansi() { return namaInstansi; }
    public void setNamaInstansi(String namaInstansi) { this.namaInstansi = namaInstansi; }
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // 1. Tampil Data ke JTable
    public DefaultTableModel getTabelPelanggan() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"No", "Kode Pelanggan", "Nama Pelanggan", "Alamat", "No. Telepon", "Email"}, 0
        );
        String sql = "SELECT * FROM pelanggan ORDER BY kode_pelanggan ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("kode_pelanggan"),
                    rs.getString("nama_pelanggan"), // sesuaikan nama kolom di MySQL
                    rs.getString("alamat"),
                    rs.getString("no_telepon"),
                    rs.getString("email")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    // 2. Simpan Data Baru
    public boolean simpan() {
        String sql = "INSERT INTO pelanggan (kode_pelanggan, nama_pelanggan, alamat, no_telepon, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kodePelanggan);
            ps.setString(2, namaInstansi);
            ps.setString(3, alamat);
            ps.setString(4, noTelepon);
            ps.setString(5, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Ubah Data
    public boolean ubah() {
        String sql = "UPDATE pelanggan SET nama_pelanggan=?, alamat=?, no_telepon=?, email=? WHERE kode_pelanggan=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, namaInstansi);
            ps.setString(2, alamat);
            ps.setString(3, noTelepon);
            ps.setString(4, email);
            ps.setString(5, kodePelanggan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Hapus Data
    public boolean hapus(String kode) {
        String sql = "DELETE FROM pelanggan WHERE kode_pelanggan=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
