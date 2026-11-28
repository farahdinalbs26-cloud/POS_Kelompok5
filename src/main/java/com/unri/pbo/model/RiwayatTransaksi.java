/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unri.pbo.model;

/**
 *
 * @author Strgirlezz
 */
public class RiwayatTransaksi {
    private String idTransaksi;
    private String tanggalWaktu;
    private String jenis;
    private String pelangganSupplier;
    private double total;

    public RiwayatTransaksi() {
    }

    public RiwayatTransaksi(String idTransaksi, String tanggalWaktu, String jenis, String pelangganSupplier, double total) {
        this.idTransaksi = idTransaksi;
        this.tanggalWaktu = tanggalWaktu;
        this.jenis = jenis;
        this.pelangganSupplier = pelangganSupplier;
        this.total = total;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getTanggalWaktu() {
        return tanggalWaktu;
    }

    public void setTanggalWaktu(String tanggalWaktu) {
        this.tanggalWaktu = tanggalWaktu;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getPelangganSupplier() {
        return pelangganSupplier;
    }

    public void setPelangganSupplier(String pelangganSupplier) {
        this.pelangganSupplier = pelangganSupplier;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

