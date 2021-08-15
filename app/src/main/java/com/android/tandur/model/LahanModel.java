package com.android.tandur.model;

public class LahanModel {
    public String email;
    public String namaLahan;
    public String alamat;
    public String kelurahan;
    public String kecamatan;
    public String kabkot;
    public String provinsi;
    public String lokasi;
    public String pemilik;
    public String harga;
    public String noKTP;
    public String noSertan;
    public String latitude;
    public String longitude;
    public String panjang;
    public String lebar;
    public FasilitasModel fasilitas;
    public String peraturan;

    public LahanModel(String email, String namaLahan, String alamat, String kelurahan, String kecamatan, String kabkot, String provinsi, String lokasi, String pemilik, String harga, String noKTP, String noSertan, String latitude, String longitude, String panjang, String lebar, FasilitasModel fasilitas, String peraturan) {
        this.email = email;
        this.namaLahan = namaLahan;
        this.alamat = alamat;
        this.kelurahan = kelurahan;
        this.kecamatan = kecamatan;
        this.kabkot = kabkot;
        this.provinsi = provinsi;
        this.lokasi = lokasi;
        this.pemilik = pemilik;
        this.harga = harga;
        this.noKTP = noKTP;
        this.noSertan = noSertan;
        this.latitude = latitude;
        this.longitude = longitude;
        this.panjang = panjang;
        this.lebar = lebar;
        this.fasilitas = fasilitas;
        this.peraturan = peraturan;
    }

    public static class FasilitasModel {
        public String isIrigasi;
        public String isListrik;
        public String isPeralatan;
        public String isKanopi;

        public FasilitasModel(String isIrigasi, String isListrik, String isPeralatan, String isKanopi) {
            this.isIrigasi = isIrigasi;
            this.isListrik = isListrik;
            this.isPeralatan = isPeralatan;
            this.isKanopi = isKanopi;
        }
    }
}
