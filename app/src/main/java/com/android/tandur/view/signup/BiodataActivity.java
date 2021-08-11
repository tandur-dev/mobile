package com.android.tandur.view.signup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tandur.R;
import com.android.tandur.api.ApiClient;
import com.android.tandur.api.ApiInterface;
import com.android.tandur.api.response.KecamatanResponse;
import com.android.tandur.api.response.KelurahanRespone;
import com.android.tandur.api.response.KotaKabupatenResponse;
import com.android.tandur.api.response.ProvinsiResponse;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BiodataActivity extends AppCompatActivity {
    private ApiInterface apiInterface;

    private ShapeableImageView shapeableImageViewProfil;
    private TextView textViewEmail;
    private TextInputEditText textInputEditTextNamaLengkap, textInputEditTextNoTelepon, textInputEditTextAlamat;
//    private AutoCompleteTextView autoCompleteTextViewProvinsi, autoCompleteTextViewKotaKabupaten, autoCompleteTextViewKecamatan;
    private Spinner spinnerProvinsi, spinnerKotaKabupaten, spinnerKecamatan, spinnerKelurahan;
    private MaterialButton materialButtonSelanjutnya;

    private String email, idProvinsi, idKotaKabupaten, idKecamatan, idKelurahan;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata);

        email = getIntent().getStringExtra("EMAIL");

        apiInterface = ApiClient.getClient();

        shapeableImageViewProfil = findViewById(R.id.shapeableImageViewProfil);
        textViewEmail = findViewById(R.id.textViewEmail);
        textInputEditTextNamaLengkap = findViewById(R.id.textInputEditTextNamaLengkap);
        textInputEditTextNoTelepon = findViewById(R.id.textInputEditTextNoTelepon);
        textInputEditTextAlamat = findViewById(R.id.textInputEditTextAlamat);
//        autoCompleteTextViewProvinsi = findViewById(R.id.autoCompleteTextViewProvinsi);
//        autoCompleteTextViewKotaKabupaten = findViewById(R.id.autoCompleteTextViewKotaKabupaten);
//        autoCompleteTextViewKecamatan = findViewById(R.id.autoCompleteTextViewKecamatan);
        materialButtonSelanjutnya = findViewById(R.id.materialButtonSelanjutnya);

        spinnerProvinsi = findViewById(R.id.spinnerProvinsi);
        spinnerKotaKabupaten = findViewById(R.id.spinnerKotaKabupaten);
        spinnerKecamatan = findViewById(R.id.spinnerKecamatan);
        spinnerKelurahan = findViewById(R.id.spinnerKelurahan);

        textViewEmail.setText(email);
        getProvinsi();

        shapeableImageViewProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(BiodataActivity.this)
                        .crop()
                        .compress(1024)
                        .start();
            }
        });

        materialButtonSelanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkData()) {
                    Intent intent = new Intent(BiodataActivity.this, VerifikasiFotoActivity.class);
                    intent.putExtra("EMAIL", email);
                    intent.putExtra("FOTO_PROFILE", uri.toString());
                    intent.putExtra("NAMA_LENGKAP", textInputEditTextNamaLengkap.getText().toString());
                    intent.putExtra("NO_TELEPON", textInputEditTextNoTelepon.getText().toString());
                    intent.putExtra("ALAMAT", textInputEditTextAlamat.getText().toString());
                    intent.putExtra("ID_PROVINSI", idProvinsi);
                    intent.putExtra("ID_KOTA_KABUPATEN", idKotaKabupaten);
                    intent.putExtra("ID_KECAMATAN", idKecamatan);
                    intent.putExtra("ID_KELURAHAN", idKelurahan);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
//            shapeableImageViewProfil.setImageURI(data.getData());
            Picasso.get()
                    .load(data.getData())
                    .into(shapeableImageViewProfil);
            uri = data.getData();
        }
    }

    public void getProvinsi() {
        apiInterface.getProvinsi().enqueue(new Callback<ProvinsiResponse>() {
            @Override
            public void onResponse(Call<ProvinsiResponse> call, Response<ProvinsiResponse> response) {
                if (response.isSuccessful()) {
                    ProvinsiResponse provinsiResponse = response.body();
                    if (provinsiResponse.status) {
                        ArrayAdapter<ProvinsiResponse.ProvinsiModel> adapter = new ArrayAdapter<ProvinsiResponse.ProvinsiModel>(BiodataActivity.this, R.layout.item_dropdown_text, provinsiResponse.data);
//                        autoCompleteTextViewProvinsi.setAdapter(adapter);
//
//                        autoCompleteTextViewProvinsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                getKotaKabupaten(provinsiResponse.data.get(position).idProvinsi);
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//
//                            }
//                        });

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
                        ArrayAdapter<KotaKabupatenResponse.KotaKabupatenModel> adapter = new ArrayAdapter<KotaKabupatenResponse.KotaKabupatenModel>(BiodataActivity.this, R.layout.item_dropdown_text, kotaKabupatenResponse.data);
//                        autoCompleteTextViewKotaKabupaten.setAdapter(adapter);
//
//                        autoCompleteTextViewKotaKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                getKecamatan(kotaKabupatenResponse.data.get(position).idKota);
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//
//                            }
//                        });

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
                        ArrayAdapter<KecamatanResponse.KecamatanModel> adapter = new ArrayAdapter<KecamatanResponse.KecamatanModel>(BiodataActivity.this, R.layout.item_dropdown_text, kecamatanResponse.data);
//                        autoCompleteTextViewKecamatan.setAdapter(adapter);
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
                        ArrayAdapter<KelurahanRespone.KelurahanModel> adapter = new ArrayAdapter<KelurahanRespone.KelurahanModel>(BiodataActivity.this, R.layout.item_dropdown_text, kelurahanRespone.data);
//                        autoCompleteTextViewKecamatan.setAdapter(adapter);
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

    public boolean checkData() {
        boolean check1 = true;
        boolean check2 = true;
        boolean check3 = true;
        boolean check4 = true;

        if (uri == null) {
            Toast.makeText(this, "Mohon upload foto profil", Toast.LENGTH_SHORT).show();
            check1 = false;
        }

        if (textInputEditTextNamaLengkap.getText().toString().isEmpty()) {
            textInputEditTextNamaLengkap.setError("Mohon isi data berikut");
            check2 = false;
        }

        if (textInputEditTextNoTelepon.getText().toString().isEmpty()) {
            textInputEditTextNoTelepon.setError("Mohon isi data berikut");
            check3 = false;
        }

        if (textInputEditTextAlamat.getText().toString().isEmpty()) {
            textInputEditTextAlamat.setError("Mohon isi data berikut");
            check4 = false;
        }

        return check1 && check2 && check3 && check4;
    }
}