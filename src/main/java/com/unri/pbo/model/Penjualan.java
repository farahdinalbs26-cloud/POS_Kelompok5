package com.unri.pbo.model;

import com.unri.pbo.DetailPenjualan;
import com.unri.pbo.Pelanggan;
import com.unri.pbo.Produk;
import com.unri.pbo.config.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Penjualan {

    // 1. Ambil semua produk dari MySQL -> bungkus ke objek Produk (JAR dosen)
    public List<Produk> getAllProduk() throws SQLException {
        List<Produk> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM produk ORDER BY id_produk ASC")) {
            while (rs.next()) {
                String id = rs.getString("id_produk");
                String nama = rs.getString("nama_produk");
                BigDecimal hargaJual = rs.getBigDecimal("harga_jual");
                BigDecimal hargaBeli = adaKolom(rs, "harga_beli") && rs.getBigDecimal("harga_beli") != null
                        ? rs.getBigDecimal("harga_beli") : hargaJual;
                int stok = rs.getInt("stok");

                list.add(new Produk(id, nama, hargaBeli, hargaJual, stok));
            }
        }
        return list;
    }

    // 2. Ambil semua pelanggan dari MySQL -> bungkus ke objek Pelanggan (JAR dosen)
    public List<Pelanggan> getAllPelanggan() throws SQLException {
        List<Pelanggan> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM pelanggan ORDER BY kode_pelanggan ASC")) {
            while (rs.next()) {
                String kode = rs.getString("kode_pelanggan");
                String nama = rs.getString("nama_pelanggan");
                String alamat = adaKolom(rs, "alamat") && rs.getString("alamat") != null ? rs.getString("alamat") : "-";
                String telp = adaKolom(rs, "no_telepon") && rs.getString("no_telepon") != null ? rs.getString("no_telepon") : "-";

                list.add(new Pelanggan(kode, nama, alamat, telp, "Instansi"));
            }
        }
        return list;
    }

    // 3. Generate nomor faktur otomatis (format: TRX001, TRX002, dst.)
    public String generateNoFaktur() {
        String sql = "SELECT id_penjualan FROM penjualan ORDER BY id_penjualan DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next() && rs.getString("id_penjualan") != null) {
                String lastId = rs.getString("id_penjualan");
                int num = Integer.parseInt(lastId.replaceAll("[^0-9]", "")) + 1;
                return String.format("TRX%03d", num);
            }
        } catch (Exception ignored) {
        }
        return "TRX001";
    }

    // 4. Simpan transaksi final dari objek Penjualan (JAR dosen) ke MySQL
    public void simpanTransaksi(com.unri.pbo.Penjualan pj, String tanggal, String kodePelanggan) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            ResultSet rsMetaJual = conn.createStatement().executeQuery("SELECT * FROM penjualan LIMIT 1");
            String kolIdJual = rsMetaJual.getMetaData().getColumnName(1);
            String kolTgl = adaKolom(rsMetaJual, "tanggal") ? "tanggal" : "tgl_penjualan";
            String kolPel = adaKolom(rsMetaJual, "kode_pelanggan") ? "kode_pelanggan"
                          : (adaKolom(rsMetaJual, "id_pelanggan") ? "id_pelanggan" : "pelanggan");
            String kolTot = adaKolom(rsMetaJual, "total") ? "total" : "total_harga";

            String sqlPenjualan = adaKolom(rsMetaJual, "status")
                    ? "INSERT INTO penjualan (" + kolIdJual + ", " + kolTgl + ", " + kolPel + ", " + kolTot + ", status) VALUES (?, ?, ?, ?, 'Selesai')"
                    : "INSERT INTO penjualan (" + kolIdJual + ", " + kolTgl + ", " + kolPel + ", " + kolTot + ") VALUES (?, ?, ?, ?)";

            try (PreparedStatement psJual = conn.prepareStatement(sqlPenjualan)) {
                psJual.setString(1, pj.getIdPenjualan());
                psJual.setString(2, tanggal);
                psJual.setString(3, kodePelanggan);
                psJual.setBigDecimal(4, pj.hitungTotal());
                psJual.executeUpdate();
            }

            ResultSet rsMetaDet = conn.createStatement().executeQuery("SELECT * FROM detail_penjualan LIMIT 1");
            String kolDetIdJual = adaKolom(rsMetaDet, "id_penjualan") ? "id_penjualan" : "no_nota";
            String kolDetIdProd = adaKolom(rsMetaDet, "id_produk") ? "id_produk" : "kode_produk";
            String kolDetHarga = adaKolom(rsMetaDet, "harga_jual") ? "harga_jual" : "harga";
            String kolDetQty = adaKolom(rsMetaDet, "jumlah") ? "jumlah" : "qty";
            boolean punyaIdDetail = adaKolom(rsMetaDet, "id_detail");
            boolean punyaSubtotal = adaKolom(rsMetaDet, "subtotal");

            StringBuilder sbKolom = new StringBuilder(kolDetIdJual + ", " + kolDetIdProd + ", " + kolDetHarga + ", " + kolDetQty);
            StringBuilder sbTanya = new StringBuilder("?, ?, ?, ?");
            if (punyaIdDetail) {
                sbKolom.append(", id_detail");
                sbTanya.append(", ?");
            }
            if (punyaSubtotal) {
                sbKolom.append(", subtotal");
                sbTanya.append(", ?");
            }

            ResultSet rsMetaProd = conn.createStatement().executeQuery("SELECT * FROM produk LIMIT 1");
            String kolPkProd = rsMetaProd.getMetaData().getColumnName(1);

            String sqlDetail = "INSERT INTO detail_penjualan (" + sbKolom + ") VALUES (" + sbTanya + ")";
            String sqlStok = "UPDATE produk SET stok = ? WHERE " + kolPkProd + " = ?";

            try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                 PreparedStatement psStok = conn.prepareStatement(sqlStok)) {

                int nomor = 1;
                for (DetailPenjualan d : pj.getDaftarDetail()) {
                    int idxParam = 1;
                    psDetail.setString(idxParam++, pj.getIdPenjualan());
                    psDetail.setString(idxParam++, d.getProduk().getIdProduk());
                    psDetail.setBigDecimal(idxParam++, d.getHargaJual());
                    psDetail.setInt(idxParam++, d.getJumlah());
                    if (punyaIdDetail) {
                        psDetail.setString(idxParam++, pj.getIdPenjualan() + "-" + (nomor++));
                    }
                    if (punyaSubtotal) {
                        psDetail.setBigDecimal(idxParam++, d.getSubtotal());
                    }
                    psDetail.addBatch();

                    psStok.setInt(1, d.getProduk().getStok());
                    psStok.setString(2, d.getProduk().getIdProduk());
                    psStok.addBatch();
                }
                psDetail.executeBatch();
                psStok.executeBatch();
            }

            conn.commit();
        }
    }

    // 5. Ambil objek Penjualan (JAR dosen) beserta detail itemnya untuk Cetak Nota di PanelPenjualan
    public List<com.unri.pbo.Penjualan> getDaftarPenjualan(String dari, String sampai, String noNota) throws SQLException {
        List<com.unri.pbo.Penjualan> hasil = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM penjualan ORDER BY 1 DESC")) {

            String kolomId = rs.getMetaData().getColumnName(1);

            while (rs.next()) {
                String idNota = rs.getString(kolomId);

                if (!noNota.isEmpty() && (idNota == null || !idNota.toLowerCase().contains(noNota.toLowerCase()))) {
                    continue;
                }

                String tanggal = adaKolom(rs, "tanggal") ? rs.getString("tanggal")
                               : (adaKolom(rs, "tgl_penjualan") ? rs.getString("tgl_penjualan") : "-");

                if (!dari.isEmpty() && !sampai.isEmpty() && tanggal != null && tanggal.length() >= 10) {
                    String tglSaja = tanggal.substring(0, 10);
                    if (tglSaja.compareTo(dari) < 0 || tglSaja.compareTo(sampai) > 0) {
                        continue;
                    }
                }

                String namaPel = "Umum";
                if (adaKolom(rs, "pelanggan") && rs.getString("pelanggan") != null) {
                    namaPel = rs.getString("pelanggan");
                } else if (adaKolom(rs, "nama_pelanggan") && rs.getString("nama_pelanggan") != null) {
                    namaPel = rs.getString("nama_pelanggan");
                } else if (adaKolom(rs, "kode_pelanggan") && rs.getString("kode_pelanggan") != null) {
                    namaPel = rs.getString("kode_pelanggan");
                }

                String kasir = (adaKolom(rs, "kasir") && rs.getString("kasir") != null) ? rs.getString("kasir") : "Kasir";

                Pelanggan pelanggan = new Pelanggan("UMUM", namaPel, "-", "-", "Reguler");
                com.unri.pbo.Penjualan pj = new com.unri.pbo.Penjualan(idNota, pelanggan);

                muatDetailKeObjek(conn, pj, kolomId);
                if (!pj.getDaftarDetail().isEmpty()) {
                    pj.finalisasiTransaksi(kasir);
                }

                hasil.add(pj);
            }
        }
        return hasil;
    }

    // 6. Ambil baris siap tampil untuk JTable di PanelPenjualan
    public List<Object[]> getRiwayatUntukTabel(String dari, String sampai, String noNota) throws SQLException {
        List<Object[]> barisTabel = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM penjualan ORDER BY 1 DESC")) {

            String kolomId = rs.getMetaData().getColumnName(1);

            while (rs.next()) {
                String idNota = rs.getString(kolomId);

                if (!noNota.isEmpty() && (idNota == null || !idNota.toLowerCase().contains(noNota.toLowerCase()))) {
                    continue;
                }

                String tanggal = adaKolom(rs, "tanggal") ? rs.getString("tanggal")
                               : (adaKolom(rs, "tgl_penjualan") ? rs.getString("tgl_penjualan") : "-");

                if (!dari.isEmpty() && !sampai.isEmpty() && tanggal != null && tanggal.length() >= 10) {
                    String tglSaja = tanggal.substring(0, 10);
                    if (tglSaja.compareTo(dari) < 0 || tglSaja.compareTo(sampai) > 0) {
                        continue;
                    }
                }

                String namaPel = "Umum";
                if (adaKolom(rs, "pelanggan") && rs.getString("pelanggan") != null) {
                    namaPel = rs.getString("pelanggan");
                } else if (adaKolom(rs, "nama_pelanggan") && rs.getString("nama_pelanggan") != null) {
                    namaPel = rs.getString("nama_pelanggan");
                } else if (adaKolom(rs, "kode_pelanggan") && rs.getString("kode_pelanggan") != null) {
                    namaPel = rs.getString("kode_pelanggan");
                }

                String kasir = (adaKolom(rs, "kasir") && rs.getString("kasir") != null) ? rs.getString("kasir") : "Kasir";
                String jam = adaKolom(rs, "jam") ? rs.getString("jam") : "-";
                String total = adaKolom(rs, "total") ? "Rp " + rs.getString("total")
                             : (adaKolom(rs, "total_harga") ? "Rp " + rs.getString("total_harga") : "Rp 0");

                barisTabel.add(new Object[]{idNota, tanggal, jam, kasir, namaPel, total});
            }
        }
        return barisTabel;
    }

    private void muatDetailKeObjek(Connection conn, com.unri.pbo.Penjualan pj, String kolomIdPenjualan) {
        try {
            ResultSet rsCek = conn.createStatement().executeQuery("SELECT * FROM detail_penjualan LIMIT 1");
            String kolomRef = adaKolom(rsCek, kolomIdPenjualan) ? kolomIdPenjualan
                            : (adaKolom(rsCek, "id_penjualan") ? "id_penjualan" : "no_nota");
            String kolomProd = adaKolom(rsCek, "id_produk") ? "id_produk" : "kode_produk";

            ResultSet rsProdCek = conn.createStatement().executeQuery("SELECT * FROM produk LIMIT 1");
            String kolomPkProd = rsProdCek.getMetaData().getColumnName(1);

            String sqlDetail = "SELECT d.*, pr.* FROM detail_penjualan d " +
                               "JOIN produk pr ON d." + kolomProd + " = pr." + kolomPkProd + " " +
                               "WHERE d." + kolomRef + " = ?";

            try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
                ps.setString(1, pj.getIdPenjualan());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String idProd = rs.getString(kolomPkProd);
                        String namaProd = adaKolom(rs, "nama_produk") ? rs.getString("nama_produk") : "Produk";
                        int qty = adaKolom(rs, "qty") ? rs.getInt("qty")
                                : (adaKolom(rs, "jumlah") ? rs.getInt("jumlah") : 1);

                        BigDecimal hargaJual = adaKolom(rs, "harga_jual") ? rs.getBigDecimal("harga_jual")
                                             : (adaKolom(rs, "harga") ? rs.getBigDecimal("harga") : BigDecimal.ZERO);
                        BigDecimal hargaBeli = adaKolom(rs, "harga_beli") ? rs.getBigDecimal("harga_beli") : hargaJual;
                        int stokAwal = (adaKolom(rs, "stok") ? rs.getInt("stok") : 0) + qty;

                        Produk prod = new Produk(idProd, namaProd, hargaBeli, hargaJual, stokAwal);
                        pj.tambahPenjualan(prod, qty, "Sistem", "Muat data penjualan");
                    }
                }
            }
        } catch (Exception ignored) {
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