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

        @SerializedName("LATITUDE_LAHAN")
        public double latitudeLahan;

        @SerializedName("LONGITUDE_LAHAN")
        public double longitudeLahan;
    }
}
