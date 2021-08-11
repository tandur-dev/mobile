package com.android.tandur.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KecamatanResponse extends BaseResponse {
    @SerializedName("data")
    public List<KecamatanModel> data;

    public static class KecamatanModel {
        @SerializedName("ID_KECAMATAN")
        public String idKecamatan;

        @SerializedName("ID_KOTA")
        public String idKota;

        @SerializedName("NAMA_KECAMATAN")
        public String namaKecamatan;

        @Override
        public String toString() {
            return namaKecamatan;
        }
    }
}
