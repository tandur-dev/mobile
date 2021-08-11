package com.android.tandur.api.response;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse extends BaseResponse{
    @SerializedName("data")
    public RegisterModel data;

    public static class RegisterModel {
        @SerializedName("EMAIL_USER")
        public String emailUser;

        @SerializedName("NAMA_USER")
        public String namaUser;

        @SerializedName("TELP_USER")
        public String telpUser;

        @SerializedName("ALAMAT_USER")
        public String alamatUser;

        @SerializedName("ID_KECAMATAN")
        public String idKecamatan;

        @SerializedName("ID_KELURAHAN")
        public String idKelurahan;

        @SerializedName("ID_KOTA")
        public String idKota;

        @SerializedName("ID_PROVINSI")
        public String idProvinsi;

        @SerializedName("TOKEN_USER")
        public String tokenUser;

        @SerializedName("FOTOKTP_USER")
        public String fotoKtpUser;

        @SerializedName("SELFIE_USER")
        public String selfieUser;

        @SerializedName("FOTO_USER")
        public String fotoUser;

        @SerializedName("ISVERIF_USER")
        public String isVerifUser;
    }
}
