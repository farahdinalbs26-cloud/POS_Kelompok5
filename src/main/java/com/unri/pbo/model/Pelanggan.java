package com.unri.pbo.model;

import com.unri.pbo.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class Pelanggan {

    // Ambil semua data pelanggan -> bungkus ke objek com.unri.pbo.Pelanggan (JAR dosen)
    public List<com.unri.pbo.Pelanggan> getAllPelanggan() throws SQLException {
        List<com.unri.pbo.Pelanggan> list = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan ORDER BY kode_pelanggan ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("kode_pelanggan");
                String nama = rs.getString("nama_pelanggan");
                String alamat = adaKolom(rs, "alamat") && rs.getString("alamat") != null ? rs.getString("alamat") : "-";
                String telp = adaKolom(rs, "no_telepon") && rs.getString("no_telepon") != null ? rs.getString("no_telepon") : "-";
                String jenis = adaKolom(rs, "jenis_pelanggan") && rs.getString("jenis_pelanggan") != null ? rs.getString("jenis_pelanggan") : "Instansi";

                list.add(new com.unri.pbo.Pelanggan(id, nama, alamat, telp, jenis));
            }
        }
        return list;
    }

    // Data untuk JTable di PanelPelanggan (termasuk kolom No & Email)
    public DefaultTableModel getTabelPelanggan() throws SQLException {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"No", "Kode Pelanggan", "Nama Pelanggan", "Alamat", "No. Telepon", "Email"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String sql = "SELECT * FROM pelanggan ORDER BY kode_pelanggan ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            int no = 1;
            while (rs.next()) {
                String id = rs.getString("kode_pelanggan");
                String nama = rs.getString("nama_pelanggan");
                String alamat = adaKolom(rs, "alamat") && rs.getString("alamat") != null ? rs.getString("alamat") : "-";
                String telp = adaKolom(rs, "no_telepon") && rs.getString("no_telepon") != null ? rs.getString("no_telepon") : "-";
                String jenis = adaKolom(rs, "jenis_pelanggan") && rs.getString("jenis_pelanggan") != null ? rs.getString("jenis_pelanggan") : "Instansi";
                String email = adaKolom(rs, "email") && rs.getString("email") != null ? rs.getString("email") : "-";

                // Validasi lewat objek JAR dosen
                com.unri.pbo.Pelanggan p = new com.unri.pbo.Pelanggan(id, nama, alamat, telp, jenis);
                model.addRow(new Object[]{
                    no++,
                    p.getId(),
                    p.getNama(),
                    p.getAlamat(),
                    p.getNoTelepon(),
                    email
                });
            }
        }
        return model;
    }

    public String generateKodePelanggan() {
        String sql = "SELECT kode_pelanggan FROM pelanggan ORDER BY kode_pelanggan DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next() && rs.getString("kode_pelanggan") != null) {
                String kodeTerakhir = rs.getString("kode_pelanggan");
                int angka = Integer.parseInt(kodeTerakhir.replaceAll("[^0-9]", "")) + 1;
                return String.format("P%03d", angka);
            }
        } catch (Exception ignored) {
        }
        return "P001";
    }

    // Simpan pelanggan baru dari objek JAR dosen ke MySQL
    public void simpan(com.unri.pbo.Pelanggan p, String email) throws SQLException {
        String sql = "INSERT INTO pelanggan (kode_pelanggan, nama_pelanggan, alamat, no_telepon, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getId());
            ps.setString(2, p.getNama());
            ps.setString(3, p.getAlamat());
            ps.setString(4, p.getNoTelepon());
            ps.setString(5, email);
            ps.executeUpdate();
        }
    }

    // Ubah data pelanggan dari objek JAR dosen ke MySQL
    public void ubah(com.unri.pbo.Pelanggan p, String email) throws SQLException {
        String sql = "UPDATE pelanggan SET nama_pelanggan = ?, alamat = ?, no_telepon = ?, email = ? WHERE kode_pelanggan = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNama());
            ps.setString(2, p.getAlamat());
            ps.setString(3, p.getNoTelepon());
            ps.setString(4, email);
            ps.setString(5, p.getId());
            ps.executeUpdate();
        }
    }

    // Hapus pelanggan berdasarkan kode_pelanggan
    public void hapus(String kodePelanggan) throws SQLException {
        String sql = "DELETE FROM pelanggan WHERE kode_pelanggan = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kodePelanggan);
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