package com.android.tandur.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KotaKabupatenResponse extends BaseResponse {
    @SerializedName("data")
    public List<KotaKabupatenModel> data;

    public static class KotaKabupatenModel {
        @SerializedName("ID_KOTA")
        public String idKota;

        @SerializedName("ID_PROVINSI")
        public String idProvinsi;

        @SerializedName("NAMA_KOTA")
        public String namaKota;

        @Override
        public String toString() {
            return namaKota;
        }
    }
}
