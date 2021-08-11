package com.android.tandur.api;

import com.android.tandur.api.response.KecamatanResponse;
import com.android.tandur.api.response.KelurahanRespone;
import com.android.tandur.api.response.KotaKabupatenResponse;
import com.android.tandur.api.response.LoginResponse;
import com.android.tandur.api.response.ProvinsiResponse;
import com.android.tandur.api.response.RegisterResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("token") String token
    );

    @Multipart
    @POST("user/register")
    Call<RegisterResponse> register(
            @Part("email") RequestBody email,
            @Part("namaLengkap") RequestBody namaLengkap,
            @Part("telepon") RequestBody telepon,
            @Part("alamat") RequestBody alamat,
            @Part("kelurahan") RequestBody kelurahan,
            @Part("kecamatan") RequestBody kecamatan,
            @Part("kabkot") RequestBody kabkot,
            @Part("provinsi") RequestBody provinsi,
            @Part("token") RequestBody token,
            @Part MultipartBody.Part fotoKTP,
            @Part MultipartBody.Part fotoSelfie,
            @Part MultipartBody.Part fotoProfil
    );

    @GET("master/provinsi")
    Call<ProvinsiResponse> getProvinsi();

    @GET("master/kota/{idProvinsi}")
    Call<KotaKabupatenResponse> getKotaKabupaten(
            @Path("idProvinsi") String idProvinsi
    );

    @GET("master/kecamatan/{idKota}")
    Call<KecamatanResponse> getKecamatan(
            @Path("idKota") String idKota
    );

    @GET("master/kelurahan/{idKecamatan}")
    Call<KelurahanRespone> getKelurahan(
            @Path("idKecamatan") String getKelurahan
    );
}
