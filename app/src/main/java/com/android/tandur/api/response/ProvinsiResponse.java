package com.android.tandur.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProvinsiResponse  extends BaseResponse{
    @SerializedName("data")
    public List<ProvinsiModel> data;

    public static class ProvinsiModel {
        @SerializedName("ID_PROVINSI")
        public String idProvinsi;

        @SerializedName("NAMA_PROVINSI")
        public String namaProvinsi;

        @Override
        public String toString() {
            return namaProvinsi;
        }
    }
}
