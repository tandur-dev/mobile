package com.android.tandur.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tandur.R;
import com.android.tandur.api.ApiClient;
import com.android.tandur.api.ApiInterface;
import com.android.tandur.api.response.DetailLahanResponse;
import com.google.android.material.button.MaterialButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailLahanActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private MapView mapView;
    private ImageView imageView;
    private TextView textViewNamaLahan, textViewAlamatLahan, textViewNamaPemilikLahan, textViewUkuran, textViewIdLahan, textViewTerdaftarPada, textViewNamaPeraturanLahan, textViewBiayaSewa;
    private LinearLayout linearLayoutIrigasi, linearLayoutPeralatan, linearLayoutListrik;
    private MaterialButton materialButtonSewaSekarag;

    private String idLahan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_detail_lahan);

        idLahan = getIntent().getStringExtra("ID_LAHAN");

        apiInterface = ApiClient.getClient();

        imageView = findViewById(R.id.imageView);
        textViewNamaLahan = findViewById(R.id.textViewNamaLahan);
        textViewAlamatLahan = findViewById(R.id.textViewAlamatLahan);
        textViewNamaPemilikLahan = findViewById(R.id.textViewNamaPemilikLahan);
        textViewUkuran = findViewById(R.id.textViewUkuran);
        textViewIdLahan = findViewById(R.id.textViewIdLahan);
        textViewTerdaftarPada = findViewById(R.id.textViewTerdaftarPada);
        textViewNamaPeraturanLahan = findViewById(R.id.textViewNamaPeraturanLahan);
        textViewBiayaSewa = findViewById(R.id.textViewBiayaSewa);
        linearLayoutIrigasi = findViewById(R.id.linearLayoutIrigasi);
        linearLayoutPeralatan = findViewById(R.id.linearLayoutPeralatan);
        linearLayoutListrik = findViewById(R.id.linearLayoutListrik);
        materialButtonSewaSekarag = findViewById(R.id.materialButtonSewaSekarag);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        getDetailLahan();

    }

    public void getDetailLahan() {
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
                        textViewAlamatLahan.setText(response.body().data.alamatLahan);
                        textViewNamaPemilikLahan.setText(response.body().data.pemilikLahan);
                        textViewUkuran.setText(response.body().data.panjangLahan + " X " + response.body().data.lebarLahan + " Meter");
                        textViewIdLahan.setText(response.body().data.idLahan);
                        textViewTerdaftarPada.setText(response.body().data.tglVerifLahan);
                        textViewNamaPeraturanLahan.setText(response.body().data.peraturanLahan);
                        textViewBiayaSewa.setText("Rp. " + response.body().data.hargaLahan);
                        if (response.body().data.fasilitasLahan.isIrigasiLahan.equalsIgnoreCase("0")) {
                            linearLayoutIrigasi.setVisibility(View.GONE);
                        }
                        if (response.body().data.fasilitasLahan.isPeralatanLahan.equalsIgnoreCase("0")) {
                            linearLayoutPeralatan.setVisibility(View.GONE);
                        }
                        if (response.body().data.fasilitasLahan.isListrikLahan.equalsIgnoreCase("0")) {
                            linearLayoutListrik.setVisibility(View.GONE);
                        }
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {
                                        mapboxMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.parseDouble(response.body().data.latitudeLahan), Double.parseDouble(response.body().data.longitudeLahan)))
                                                .title(response.body().data.namaLahan));
                                    }
                                });
                                CameraPosition position = new CameraPosition.Builder()
                                        .target(new LatLng(Double.parseDouble(response.body().data.latitudeLahan), Double.parseDouble(response.body().data.longitudeLahan)))
                                        .zoom(15)
                                        .build();
                                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);
                            }
                        });

                        materialButtonSewaSekarag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), KonfirmOrderActivity.class);
                                intent.putExtra("ID_LAHAN", idLahan);
                                intent.putExtra("HARGA_LAHAN", response.body().data.hargaLahan);
                                intent.putExtra("LUAS_LAHAN", response.body().data.panjangLahan + " X " + response.body().data.lebarLahan + " Meter");
                                intent.putExtra("LOKASI_LAHAN", response.body().data.lokasiLahan);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailLahanResponse> call, Throwable t) {
                Log.e("getDetailLahan", t.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}