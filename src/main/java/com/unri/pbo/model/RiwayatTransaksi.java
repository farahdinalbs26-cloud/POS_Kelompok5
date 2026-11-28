package com.unri.pbo.model;

import com.unri.pbo.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RiwayatTransaksi {

    // Ambil gabungan data penjualan dan pembelian berdasarkan kata kunci pencarian
    public List<Object[]> getDaftarRiwayat(String keyword) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String kataKunci = "%" + (keyword != null ? keyword.trim() : "") + "%";

        String sql = "SELECT * FROM (" +
                     "  SELECT id_penjualan AS kode, tanggal, 'Penjualan' AS jenis, " +
                     "         kode_pelanggan AS keterangan, total " +
                     "  FROM penjualan " +
                     "  UNION ALL " +
                     "  SELECT id_pembelian AS kode, tanggal, 'Pembelian' AS jenis, " +
                     "         id_supplier AS keterangan, total " +
                     "  FROM pembelian " +
                     ") AS gabungan " +
                     "WHERE kode LIKE ? OR tanggal LIKE ? OR jenis LIKE ? OR keterangan LIKE ? " +
                     "ORDER BY tanggal DESC, kode DESC";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, kataKunci);
            ps.setString(2, kataKunci);
            ps.setString(3, kataKunci);
            ps.setString(4, kataKunci);

            try (ResultSet rs = ps.executeQuery()) {
                int no = 1;
                while (rs.next()) {
                    list.add(new Object[]{
                        no++,
                        rs.getString("kode"),
                        rs.getString("tanggal"),
                        rs.getString("jenis"),
                        rs.getString("keterangan"),
                        String.format("%.0f", rs.getDouble("total"))
                    });
                }
            }
        }
        return list;
    }
}