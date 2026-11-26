
package com.unri.pbo.model;

import java.math.BigDecimal;

public class Produk {

    private String idProduk;
    private String namaProduk;
    private BigDecimal hargaBeli;
    private BigDecimal hargaJual;
    private int stok;

    public Produk(String id, String nama,
                  BigDecimal hargaBeli,
                  BigDecimal hargaJual,
                  int stok) {

        this.idProduk = id;
        this.namaProduk = nama;
        this.hargaBeli = hargaBeli;
        this.hargaJual = hargaJual;
        this.stok = stok;
    }

    public String getIdProduk() {
        return idProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public BigDecimal getHargaBeli() {
        return hargaBeli;
    }

    public BigDecimal getHargaJual() {
        return hargaJual;
    }

    public int getStok() {
        return stok;
    }

    public String getStatusStok() {
        if (stok <= 0) {
            return "HABIS";
        } else if (stok < 5) {
            return "MENIPIS";
        } else {
            return "TERSEDIA";
        }
    }

    @Override
    public String toString() {
        return idProduk + " - " + namaProduk
                + " (stok " + stok + ")";
    }
}
