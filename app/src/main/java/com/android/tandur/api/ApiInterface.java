package com.android.tandur.api;

import com.android.tandur.api.response.BaseResponse;
import com.android.tandur.api.response.DetailLahanResponse;
import com.android.tandur.api.response.KecamatanResponse;
import com.android.tandur.api.response.KelurahanRespone;
import com.android.tandur.api.response.KotaKabupatenResponse;
import com.android.tandur.api.response.LahanResponse;
import com.android.tandur.api.response.LahanTerdekatResponse;
import com.android.tandur.api.response.LoginResponse;
import com.android.tandur.api.response.ProvinsiResponse;
import com.android.tandur.api.response.RegisterResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("lahan/terdekat")
    Call<LahanTerdekatResponse> getLahanTerdekat(
            @Query("lat") double latitude,
            @Query("long") double longitude,
            @Query("limit") int limit
    );

    @Headers("Content-Type: application/json")
    @POST("lahan")
    Call<LahanResponse> postLahan(
            @Body String body
    );

    @Multipart
    @POST("lahan/foto")
    Call<BaseResponse> postFotoLahan(
            @Part("idLahan") RequestBody idLahan,
            @Part MultipartBody.Part foto1,
            @Part MultipartBody.Part foto2
    );

    @Multipart
    @POST("lahan/galeri")
    Call<BaseResponse> postGaleriLahan(
            @Part("idLahan") RequestBody idLahan,
            @Part MultipartBody.Part file
    );

    @GET("lahan/{idLahan}")
    Call<DetailLahanResponse> getDetailLahan(
            @Path("idLahan") String idLahan
    );

    @FormUrlEncoded
    @POST("urban-farming")
    Call<BaseResponse> postUrbanFarming(
            @Field("idLahan") String idLahan,
            @Field("email") String email,
            @Field("tgl") String tgl,
            @Field("tglSelesai") String tglSelesai,
            @Field("totBayar") String totBayar
    );
}
