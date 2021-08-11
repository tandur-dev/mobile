package com.android.tandur.api.response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse extends BaseResponse{
    @SerializedName("data")
    public LoginModel data;

    public static class LoginModel {
        @SerializedName("EMAIL_USER")
        public String emailUser;

        @SerializedName("NOKTP_USER")
        public String noKtpUser;

        @SerializedName("NAMA_USER")
        public String namaUser;

        @SerializedName("TOKEN_USER")
        public String tokenUser;

        @SerializedName("TELP_USER")
        public String telpUser;

        @SerializedName("ALAMAT_USER")
        public String alamatUser;

        @SerializedName("ID_KECAMATAN")
        public String idKecamatan;

        @SerializedName("ID_KOTA")
        public String idKota;

        @SerializedName("ID_PROVINSI")
        public String idProvinsi;

        @SerializedName("FOTOKTP_USER")
        public String fotoKtpUser;

        @SerializedName("SELFIE_USER")
        public String selfieUser;

        @SerializedName("ISVERIF_USER")
        public String isVerifUser;

        @SerializedName("created_at")
        public String createdAt;

        @SerializedName("updated_at")
        public String updatedAt;

        @SerializedName("deleted_at")
        public String deletedAt;

        @SerializedName("ID_KELURAHAN")
        public String idKelurahan;

        @SerializedName("FOTO_USER")
        public String fotoUser;
    }
}
