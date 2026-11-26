/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unri.pbo.model;
import java.math.BigDecimal;
/**
 *
 * @author Strgirlezz
 */
    public class Produk extends com.unri.pbo.Produk {

    public Produk(String id, String nama, BigDecimal hargaBeli, BigDecimal hargaJual, int stok) {
        super(id, nama, hargaBeli, hargaJual, stok);
    }

    public String getStatusStok() {
        int s = getStok();
        if (s <= 0) return "HABIS";
        if (s <= 5) return "MENIPIS";
        return "TERSEDIA";
    }

    @Override
    public String toString() {
        return getIdProduk() + " - " + getNamaProduk() + " (stok " + getStok() + ")";
    }
}
