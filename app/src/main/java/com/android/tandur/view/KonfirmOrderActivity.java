package com.android.tandur.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.tandur.R;
import com.android.tandur.api.ApiClient;
import com.android.tandur.api.ApiInterface;
import com.android.tandur.api.response.BaseResponse;
import com.android.tandur.api.response.DetailLahanResponse;
import com.android.tandur.preference.AppPreference;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KonfirmOrderActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private Calendar calendar;

    private ImageView imageView;
    private TextView textViewNamaLahan, textViewHargaLahan, textViewLuasLahan, textViewFasilitas, textViewMulaiPada, textViewSelesaiPada, textViewPenyewa, textViewEmail, textViewNoTelepon, textViewDetailHarga, textViewItemLahan, textViewTotalBiayaSewa, textViewKelurahan;
    private EditText editTextBulan;
    private RatingBar ratingBar;
    private MaterialButton materialButtonKurangBulan, materialButtonTambahBulan, materialButtonLanjutkan;

    private int bulan;
    private String idLahan, luasLahan, tglMulai, tglSelesai;
    private double harga, total;

    private SimpleDateFormat simpleDateFormatView = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
    private SimpleDateFormat simpleDateFormatServer = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirm_order);

        idLahan = getIntent().getStringExtra("ID_LAHAN");
        luasLahan = getIntent().getStringExtra("LUAS_LAHAN");
        harga = Double.parseDouble(getIntent().getStringExtra("HARGA_LAHAN"));

        Log.e("harga", String.valueOf(harga));

        textViewNamaLahan = findViewById(R.id.textViewNamaLahan);
        textViewHargaLahan = findViewById(R.id.textViewHargaLahan);
        textViewLuasLahan = findViewById(R.id.textViewLuasLahan);
        ratingBar = findViewById(R.id.ratingBar);
        imageView = findViewById(R.id.imageView);
        textViewKelurahan = findViewById(R.id.textViewKelurahan);
        textViewMulaiPada = findViewById(R.id.textViewMulaiPada);
        textViewSelesaiPada = findViewById(R.id.textViewSelesaiPada);
        textViewPenyewa = findViewById(R.id.textViewPenyewa);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewNoTelepon = findViewById(R.id.textViewNoTelepon);
        textViewDetailHarga = findViewById(R.id.textViewDetailHarga);
        textViewItemLahan = findViewById(R.id.textViewItemLahan);
        textViewTotalBiayaSewa = findViewById(R.id.textViewTotalBiayaSewa);
        editTextBulan = findViewById(R.id.editTextBulan);
        materialButtonKurangBulan = findViewById(R.id.materialButtonKurangBulan);
        materialButtonTambahBulan = findViewById(R.id.materialButtonTambahBulan);
        materialButtonLanjutkan = findViewById(R.id.materialButtonLanjutkan);

        apiInterface = ApiClient.getClient();
        calendar = Calendar.getInstance();

        textViewMulaiPada.setText(simpleDateFormatView.format(calendar.getTime()));
        tglMulai = simpleDateFormatView.format(calendar.getTime());
        Calendar newCal = Calendar.getInstance();
        newCal.add(Calendar.MONTH, 1);
        textViewSelesaiPada.setText(simpleDateFormatView.format(newCal.getTime()));

        textViewPenyewa.setText(AppPreference.getUser(this).namaUser);
        textViewEmail.setText(AppPreference.getUser(this).emailUser);
        textViewNoTelepon.setText(AppPreference.getUser(this).telpUser);

        textViewItemLahan.setText("1 Bulan - Lahan " + luasLahan);
        textViewDetailHarga.setText("Rp. " + harga);
        textViewTotalBiayaSewa.setText("Rp. " + harga);

        bulan = Integer.parseInt(editTextBulan.getText().toString());
        materialButtonKurangBulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bulan > 1) {
                    bulan--;
                    editTextBulan.setText(String.valueOf(bulan));

                    Calendar newCal = Calendar.getInstance();
                    newCal.add(Calendar.MONTH, bulan);
                    textViewSelesaiPada.setText(simpleDateFormatView.format(newCal.getTime()));

                    total = harga*bulan;
                    textViewItemLahan.setText(String.valueOf(bulan) + " Bulan - Lahan " + luasLahan);
                    textViewDetailHarga.setText("Rp. " + String.valueOf(total));
                    textViewTotalBiayaSewa.setText("Rp. " + String.valueOf(total));

                    tglSelesai = simpleDateFormatServer.format(newCal.getTime());
                }
            }
        });

        materialButtonTambahBulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bulan++;
                editTextBulan.setText(String.valueOf(bulan));

                Calendar newCal = Calendar.getInstance();
                newCal.add(Calendar.MONTH, bulan);
                textViewSelesaiPada.setText(simpleDateFormatView.format(newCal.getTime()));

                total = harga*bulan;
                textViewItemLahan.setText(String.valueOf(bulan) + " Bulan - Lahan " + luasLahan);
                textViewDetailHarga.setText("Rp. " + String.valueOf(total));
                textViewTotalBiayaSewa.setText("Rp. " + String.valueOf(total));

                tglSelesai = simpleDateFormatServer.format(newCal.getTime());
            }
        });

        materialButtonLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPembayaran();
                startActivity(new Intent(v.getContext(), MetodePembayaranActivity.class));
            }
        });

        getDetail();
    }

    public void postPembayaran() {
        apiInterface.postUrbanFarming(
                idLahan,
                AppPreference.getUser(this).emailUser,
                tglMulai,
                tglSelesai,
                String.valueOf(total)
        ).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        Log.e("postUrbanFarming", "Sukses");
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("postUrbanFarming", t.getMessage());
            }
        });
    }

    public void getDetail() {
        apiInterface.getDetailLahan(
                idLahan
        ).enqueue(new Callback<DetailLahanResponse>() {
            @Override
            public void onResponse(Call<DetailLahanResponse> call, Response<DetailLahanResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        Picasso.get()
                                .load(response.body().data.foto1Lahan)
                                .into(imageView);
                        textViewNamaLahan.setText(response.body().data.namaLahan);
                        textViewHargaLahan.setText("Rp. " + response.body().data.hargaLahan + "/Bln");
                        textViewLuasLahan.setText("Luas: " + response.body().data.panjangLahan + " X " + response.body().data.lebarLahan + " Meter");
                        textViewKelurahan.setText(response.body().data.namaKelurahan);
                        if (response.body().data.bintangLahan != null) {
                            ratingBar.setRating(Float.parseFloat(response.body().data.bintangLahan));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailLahanResponse> call, Throwable t) {

            }
        });
    }
}