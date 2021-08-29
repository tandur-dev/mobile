package com.android.tandur.view.urban_farming;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.tandur.R;
import com.android.tandur.api.ApiClient;
import com.android.tandur.api.ApiInterface;
import com.android.tandur.api.response.KecamatanResponse;
import com.android.tandur.api.response.KelurahanRespone;
import com.android.tandur.api.response.KotaKabupatenResponse;
import com.android.tandur.api.response.ProvinsiResponse;
import com.android.tandur.preference.AppPreference;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SewakanLahanActivity extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback {
    private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";

    private ApiInterface apiInterface;

    private TextInputLayout textInputLayoutNomorSertifikat;
    private TextInputEditText textInputEditTextNamaLahan, textInputEditTextAlamat, textInputEditTextNomorSertifikat, textInputEditTextNamaPemilikSesuaiKTP, textInputEditTextNoKTP;
    private Spinner spinnerProvinsi, spinnerKotaKabupaten, spinnerKecamatan, spinnerKelurahan;
    private CheckBox checkBoxAlamatLahanSamaDenganAlamatRumah, checkBoxSamakanDenganKTP;
    private RadioGroup radioGroupLokasiLahan;
    private ImageView imageView1, imageView2;
    private TextView textView1, textView2;
    private LinearLayout linearLayoutTitikLokasi;
    private MapView mapView;
    private MaterialButton materialButtonPilihLokasi, materialButtonLanjutkan;

    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private ImageView hoveringMarker;
    private Layer droppedMarkerLayer;

    private String idProvinsi, idKotaKabupaten, idKecamatan, idKelurahan, latitude, longitude, lokasi;
    private boolean img;
    private Uri uri1, uri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_sewakan_lahan);

        apiInterface = ApiClient.getClient();

        textInputLayoutNomorSertifikat = findViewById(R.id.textInputLayoutNomorSertifikat);
        textInputEditTextNamaLahan = findViewById(R.id.textInputEditTextNamaLahan);
        textInputEditTextAlamat = findViewById(R.id.textInputEditTextAlamat);
        textInputEditTextNomorSertifikat = findViewById(R.id.textInputEditTextNomorSertifikat);
        textInputEditTextNamaPemilikSesuaiKTP = findViewById(R.id.textInputEditTextNamaPemilikSesuaiKTP);
        textInputEditTextNoKTP = findViewById(R.id.textInputEditTextNoKTP);
        spinnerProvinsi = findViewById(R.id.spinnerProvinsi);
        spinnerKotaKabupaten = findViewById(R.id.spinnerKotaKabupaten);
        spinnerKecamatan = findViewById(R.id.spinnerKecamatan);
        spinnerKelurahan = findViewById(R.id.spinnerKelurahan);
        checkBoxAlamatLahanSamaDenganAlamatRumah = findViewById(R.id.checkBoxAlamatLahanSamaDenganAlamatRumah);
        checkBoxSamakanDenganKTP = findViewById(R.id.checkBoxSamakanDenganKTP);
        radioGroupLokasiLahan = findViewById(R.id.radioGroupLokasiLahan);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        linearLayoutTitikLokasi = findViewById(R.id.linearLayoutTitikLokasi);
        mapView = findViewById(R.id.mapView);
        materialButtonPilihLokasi = findViewById(R.id.materialButtonPilihLokasi);
        materialButtonLanjutkan = findViewById(R.id.materialButtonLanjutkan);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        getProvinsi();

        checkBoxAlamatLahanSamaDenganAlamatRumah.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinnerProvinsi.setEnabled(false);
                    spinnerKotaKabupaten.setEnabled(false);
                    spinnerKecamatan.setEnabled(false);
                    spinnerKelurahan.setEnabled(false);
                } else {
                    spinnerProvinsi.setEnabled(true);
                    spinnerKotaKabupaten.setEnabled(true);
                    spinnerKecamatan.setEnabled(true);
                    spinnerKelurahan.setEnabled(true);
                }
            }
        });

        lokasi = "1";
        radioGroupLokasiLahan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonHalamanTerasRumah:
                        textInputLayoutNomorSertifikat.setVisibility(View.GONE);
                        textView1.setText("Foto Rumah Anda");
                        textView2.setText("Foto Halaman Rumah Anda");
                        linearLayoutTitikLokasi.setVisibility(View.GONE);
                        lokasi = "1";
                        break;
                    case R.id.radioButtonLahanBerbedaDariRumah:
                        textInputLayoutNomorSertifikat.setVisibility(View.VISIBLE);
                        textView1.setText("Foto Sertifikat Tanah");
                        textView2.setText("Foto Lahan Anda");
                        linearLayoutTitikLokasi.setVisibility(View.VISIBLE);
                        lokasi = "2";
                        break;
                }
            }
        });

        checkBoxSamakanDenganKTP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textInputEditTextNamaPemilikSesuaiKTP.setEnabled(false);
                    textInputEditTextNoKTP.setEnabled(false);
                } else {
                    textInputEditTextNamaPemilikSesuaiKTP.setEnabled(true);
                    textInputEditTextNoKTP.setEnabled(true);
                }
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img = true;
                ImagePicker.Companion.with(SewakanLahanActivity.this)
                        .crop()
                        .compress(1024)
                        .start();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img = false;
                ImagePicker.Companion.with(SewakanLahanActivity.this)
                        .crop()
                        .compress(1024)
                        .start();
            }
        });

        materialButtonLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailSewakanLahanActivity.class);
                intent.putExtra("NAMA_LAHAN", textInputEditTextNamaLahan.getText().toString());
                intent.putExtra("ALAMAT", textInputEditTextAlamat.getText().toString());
                if (checkBoxAlamatLahanSamaDenganAlamatRumah.isChecked()) {
                    intent.putExtra("ID_PROVINSI", "11");
                    intent.putExtra("ID_KOTA_KABUPATEN", "1101");
                    intent.putExtra("ID_KECAMATAN", "1101010");
                    intent.putExtra("ID_KELURAHAN", "1101010001");
                } else {
                    intent.putExtra("ID_PROVINSI", idProvinsi);
                    intent.putExtra("ID_KOTA_KABUPATEN", idKotaKabupaten);
                    intent.putExtra("ID_KECAMATAN", idKecamatan);
                    intent.putExtra("ID_KELURAHAN", idKelurahan);
                }
                intent.putExtra("LOKASI", lokasi);
                intent.putExtra("NO_SERTIFIKAT", textInputEditTextNomorSertifikat.getText().toString().isEmpty() ? "-" : textInputEditTextNomorSertifikat.getText().toString());
                if (checkBoxSamakanDenganKTP.isChecked()) {
                    intent.putExtra("NAMA_PEMILIK", AppPreference.getUser(SewakanLahanActivity.this).namaUser);
                    intent.putExtra("KTP_PEMILIK", "-");
                } else {
                    intent.putExtra("NAMA_PEMILIK", textInputEditTextNamaPemilikSesuaiKTP.getText().toString());
                    intent.putExtra("KTP_PEMILIK", textInputEditTextNoKTP.getText().toString());
                }
                if (longitude != null && latitude != null) {
                    intent.putExtra("LONGITUDE", longitude.isEmpty() ? "-" : longitude);
                    intent.putExtra("LATITUDE", latitude.isEmpty() ? "-" : latitude);
                } else {
                    intent.putExtra("LONGITUDE", "-");
                    intent.putExtra("LATITUDE", "-");
                }
                intent.putExtra("IMG1", uri1.toString());
                intent.putExtra("IMG2", uri2.toString());
                startActivity(intent);
            }
        });
    }

    public void getProvinsi() {
        apiInterface.getProvinsi().enqueue(new Callback<ProvinsiResponse>() {
            @Override
            public void onResponse(Call<ProvinsiResponse> call, Response<ProvinsiResponse> response) {
                if (response.isSuccessful()) {
                    ProvinsiResponse provinsiResponse = response.body();
                    if (provinsiResponse.status) {
                        ArrayAdapter<ProvinsiResponse.ProvinsiModel> adapter = new ArrayAdapter<ProvinsiResponse.ProvinsiModel>(SewakanLahanActivity.this, R.layout.item_dropdown_text, provinsiResponse.data);

                        spinnerProvinsi.setAdapter(adapter);
                        spinnerProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                getKotaKabupaten(provinsiResponse.data.get(position).idProvinsi);
                                idProvinsi = provinsiResponse.data.get(position).idProvinsi;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ProvinsiResponse> call, Throwable t) {
                Log.e("getProvinsi", t.getMessage());
            }
        });
    }

    public void getKotaKabupaten(String idProvinsi) {
        apiInterface.getKotaKabupaten(
                idProvinsi
        ).enqueue(new Callback<KotaKabupatenResponse>() {
            @Override
            public void onResponse(Call<KotaKabupatenResponse> call, Response<KotaKabupatenResponse> response) {
                if (response.isSuccessful()) {
                    KotaKabupatenResponse kotaKabupatenResponse = response.body();
                    if (kotaKabupatenResponse.status) {
                        ArrayAdapter<KotaKabupatenResponse.KotaKabupatenModel> adapter = new ArrayAdapter<KotaKabupatenResponse.KotaKabupatenModel>(SewakanLahanActivity.this, R.layout.item_dropdown_text, kotaKabupatenResponse.data);

                        spinnerKotaKabupaten.setAdapter(adapter);
                        spinnerKotaKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                getKecamatan(kotaKabupatenResponse.data.get(position).idKota);
                                idKotaKabupaten = kotaKabupatenResponse.data.get(position).idKota;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<KotaKabupatenResponse> call, Throwable t) {
                Log.e("getKotaKabupaten", t.getMessage());
            }
        });
    }

    public void getKecamatan(String idKota) {
        apiInterface.getKecamatan(
                idKota
        ).enqueue(new Callback<KecamatanResponse>() {
            @Override
            public void onResponse(Call<KecamatanResponse> call, Response<KecamatanResponse> response) {
                if (response.isSuccessful()) {
                    KecamatanResponse kecamatanResponse = response.body();
                    if (kecamatanResponse.status) {
                        ArrayAdapter<KecamatanResponse.KecamatanModel> adapter = new ArrayAdapter<KecamatanResponse.KecamatanModel>(SewakanLahanActivity.this, R.layout.item_dropdown_text, kecamatanResponse.data);

                        spinnerKecamatan.setAdapter(adapter);
                        spinnerKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                getKelurahan(kecamatanResponse.data.get(position).idKecamatan);
                                idKecamatan = kecamatanResponse.data.get(position).idKecamatan;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<KecamatanResponse> call, Throwable t) {
                Log.e("getKecamatan", t.getMessage());
            }
        });
    }

    public void getKelurahan(String idKecamatan) {
        apiInterface.getKelurahan(
                idKecamatan
        ).enqueue(new Callback<KelurahanRespone>() {
            @Override
            public void onResponse(Call<KelurahanRespone> call, Response<KelurahanRespone> response) {
                if (response.isSuccessful()) {
                    KelurahanRespone kelurahanRespone = response.body();
                    if (kelurahanRespone.status) {
                        ArrayAdapter<KelurahanRespone.KelurahanModel> adapter = new ArrayAdapter<KelurahanRespone.KelurahanModel>(SewakanLahanActivity.this, R.layout.item_dropdown_text, kelurahanRespone.data);

                        spinnerKelurahan.setAdapter(adapter);
                        spinnerKelurahan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                idKelurahan = kelurahanRespone.data.get(position).idKelurahan;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<KelurahanRespone> call, Throwable t) {
                Log.e("getKelurahan", t.getMessage());
            }
        });
    }

    private void initDroppedMarker(@NonNull Style loadedMapStyle) {
        // Add the marker image to map
        loadedMapStyle.addImage("dropped-icon-image", BitmapFactory.decodeResource(
                getResources(), R.drawable.mapbox_marker_icon_default));
        loadedMapStyle.addSource(new GeoJsonSource("dropped-marker-source-id"));
        loadedMapStyle.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
                "dropped-marker-source-id").withProperties(
                iconImage("dropped-icon-image"),
                visibility(NONE),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        ));
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component. Adding in LocationComponentOptions is also an optional parameter
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(
                    this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.NORMAL);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

            // Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

//            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
//            shapeableImageViewProfil.setImageURI(data.getData());
            if (img) {
                Picasso.get()
                        .load(data.getData())
                        .into(imageView1);
                uri1 = data.getData();
            } else {
                Picasso.get()
                        .load(data.getData())
                        .into(imageView2);
                uri2 = data.getData();
            }
        }
    }

    @Override
    public void onExplanationNeeded(List<String> list) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull final Style style) {
                enableLocationPlugin(style);
                // Toast instructing user to tap on the mapboxMap
//                Toast.makeText(
//                        LocationPickerActivity.this,
//                        getString(R.string.move_map_instruction), Toast.LENGTH_SHORT).show();

                // When user is still picking a location, we hover a marker above the mapboxMap in the center.
                // This is done by using an image view with the default marker found in the SDK. You can
                // swap out for your own marker image, just make sure it matches up with the dropped marker.
                hoveringMarker = new ImageView(SewakanLahanActivity.this);
                hoveringMarker.setImageResource(R.drawable.mapbox_marker_icon_default);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                hoveringMarker.setLayoutParams(params);
                mapView.addView(hoveringMarker);

                // Initialize, but don't show, a SymbolLayer for the marker icon which will represent a selected location.
                initDroppedMarker(style);

                // Button for user to drop marker or to pick marker back up.
                materialButtonPilihLokasi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (hoveringMarker.getVisibility() == View.VISIBLE) {

                            // Use the map target's coordinates to make a reverse geocoding search
                            final LatLng mapTargetLatLng = mapboxMap.getCameraPosition().target;

                            // Hide the hovering red hovering ImageView marker
                            hoveringMarker.setVisibility(View.INVISIBLE);

                            // Transform the appearance of the button to become the cancel button
                            materialButtonPilihLokasi.setBackgroundColor(
                                    ContextCompat.getColor(SewakanLahanActivity.this, R.color.colorAccent));
                            materialButtonPilihLokasi.setText("Batal");

                            // Show the SymbolLayer icon to represent the selected map location
                            if (style.getLayer(DROPPED_MARKER_LAYER_ID) != null) {
                                GeoJsonSource source = style.getSourceAs("dropped-marker-source-id");
                                if (source != null) {
                                    source.setGeoJson(Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));
                                }
                                droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                                if (droppedMarkerLayer != null) {
                                    droppedMarkerLayer.setProperties(visibility(VISIBLE));
                                }
                            }
                            // Use the map camera target's coordinates to make a reverse geocoding search
//                            reverseGeocode(Point.fromLngLat(mapTargetLatLng.getLongitude(), mapTargetLatLng.getLatitude()));
                            longitude = String.valueOf(mapTargetLatLng.getLongitude());
                            latitude = String.valueOf(mapTargetLatLng.getLatitude());

                        } else {
                            // Switch the button appearance back to select a location.
//                            materialButtonPilihLokasi.setBackgroundColor(
//                                    ContextCompat.getColor(SewakanLahanActivity.this, R.color.colorPrimary));
                            materialButtonPilihLokasi.setText("Pilih Lokasi");

                            // Show the red hovering ImageView marker
                            hoveringMarker.setVisibility(View.VISIBLE);

                            // Hide the selected location SymbolLayer
                            droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID);
                            if (droppedMarkerLayer != null) {
                                droppedMarkerLayer.setProperties(visibility(NONE));
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(boolean b) {
        if (b) {
            if (mapboxMap.getStyle() != null) {
                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });
            }
        } else {
            Toast.makeText(this, "Not Granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}