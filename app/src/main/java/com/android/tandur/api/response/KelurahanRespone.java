package com.android.tandur.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KelurahanRespone extends BaseResponse {
    @SerializedName("data")
    public List<KelurahanModel> data;

    public static class KelurahanModel {
        @SerializedName("ID_KELURAHAN")
        public String idKelurahan;

        @SerializedName("ID_KECAMATAN")
        public String idKecamatan;

        @SerializedName("NAMA_KELURAHAN")
        public String namaKelurahan;

        @Override
        public String toString() {
            return namaKelurahan;
        }
    }
}
