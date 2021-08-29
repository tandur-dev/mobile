package com.android.tandur.api.response;

import com.google.gson.annotations.SerializedName;

public class DetailLahanResponse extends BaseResponse {
    @SerializedName("data")
    public DetailLahanModel data;

    public static class DetailLahanModel {
        @SerializedName("ID_LAHAN")
        public String idLahan;

        @SerializedName("EMAIL_USER")
        public String emailUser;

        @SerializedName("NAMA_LAHAN")
        public String namaLahan;

        @SerializedName("BINTANG_LAHAN")
        public String bintangLahan;

        @SerializedName("ALAMAT_LAHAN")
        public String alamatLahan;

        @SerializedName("NAMA_PROVINSI")
        public String namaProvinsi;

        @SerializedName("ID_KOTA")
        public String idKota;

        @SerializedName("NAMA_KECAMATAN")
        public String namaKecamatan;

        @SerializedName("NAMA_KELURAHAN")
        public String namaKelurahan;

        @SerializedName("LATITUDE_LAHAN")
        public String latitudeLahan;

        @SerializedName("LONGITUDE_LAHAN")
        public String longitudeLahan;

        @SerializedName("NAMA_USER")
        public String namaUser;

        @SerializedName("FASILITAS_LAHAN")
        public FasilitasLahanModel fasilitasLahan;

        @SerializedName("PANJANG_LAHAN")
        public String panjangLahan;

        @SerializedName("LEBAR_LAHAN")
        public String lebarLahan;

        @SerializedName("TGLVERIF_LAHAN")
        public String tglVerifLahan;

        @SerializedName("PERATURAN_LAHAN")
        public String peraturanLahan;

        @SerializedName("PEMILIK_LAHAN")
        public String pemilikLahan;

        @SerializedName("HARGA_LAHAN")
        public String hargaLahan;

        @SerializedName("FOTO1_LAHAN")
        public String foto1Lahan;

        @SerializedName("LOKASI_LAHAN")
        public String lokasiLahan;

        public static class FasilitasLahanModel {
            @SerializedName("ISIRIGASI_LAHAN")
            public String isIrigasiLahan;

            @SerializedName("ISLISTRIK_LAHAN")
            public String isListrikLahan;

            @SerializedName("ISPERALATAN_LAHAN")
            public String isPeralatanLahan;

            @SerializedName("ISKANOPI_LAHAN")
            public String isKanopiLahan;
        }
    }
}
