package com.unri.pbo.model;

public class Supplier {
    private String idSupplier;
    private String namaPerusahaan;
    private String noTelepon;
    private String alamat;

    public Supplier() {}

    public Supplier(String idSupplier, String namaPerusahaan, String noTelepon, String alamat) {
        this.idSupplier = idSupplier;
        this.namaPerusahaan = namaPerusahaan;
        this.noTelepon = noTelepon;
        this.alamat = alamat;
    }

    public String getIdSupplier() { return idSupplier; }
    public void setIdSupplier(String idSupplier) { this.idSupplier = idSupplier; }

    public String getNamaPerusahaan() { return namaPerusahaan; }
    public void setNamaPerusahaan(String namaPerusahaan) { this.namaPerusahaan = namaPerusahaan; }

    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    @Override
    public String toString() {
        return idSupplier + " - " + namaPerusahaan;
    }
}