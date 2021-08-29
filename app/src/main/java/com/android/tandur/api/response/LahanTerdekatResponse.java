package com.android.tandur.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LahanTerdekatResponse extends BaseResponse {
    @SerializedName("data")
    public List<LahanTerdekatModel> data;

    public static class LahanTerdekatModel {
        @SerializedName("ID_LAHAN")
        public String idLahan;

        @SerializedName("NAMA_LAHAN")
        public String namaLahan;

        @SerializedName("JARAK_LAHAN")
        public String jarakLahan;

        @SerializedName("NAMA_PROVINSI")
        public String namaProvinsi;

        @SerializedName("NAMA_KOTA")
        public String namaKota;

        @SerializedName("NAMA_KECAMATAN")
        public String namaKecamatan;

        @SerializedName("NAMA_KELURAHAN")
        public String namaKelurahan;

        @SerializedName("ALAMAT_LAHAN")
        public String alamatLahan;

        @SerializedName("HARGA_LAHAN")
        public String hargaLahan;

        @SerializedName("FOTO1_LAHAN")
        public String foto1Lahan;

        @SerializedName("PANJANG_LAHAN")
        public String panjangLahan;

        @SerializedName("LEBAR_LAHAN")
        public String lebarLahan;

        @SerializedName("BINTANG_LAHAN")
        public String bintangLahan;

        @SerializedName("LATITUDE_LAHAN")
        public double latitudeLahan;

        @SerializedName("LONGITUDE_LAHAN")
        public double longitudeLahan;
    }
}
