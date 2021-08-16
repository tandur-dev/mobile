package com.android.tandur.view.urban_farming;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.android.tandur.R;
import com.android.tandur.adapter.FotoLahanAdapter;
import com.android.tandur.api.ApiClient;
import com.android.tandur.api.ApiInterface;
import com.android.tandur.api.response.BaseResponse;
import com.android.tandur.api.response.LahanResponse;
import com.android.tandur.model.LahanModel;
import com.android.tandur.preference.AppPreference;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailSewakanLahanActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private FotoLahanAdapter fotoLahanAdapter;

    private ProgressDialog progressDialog;
    private TextInputEditText textInputEditTextPanjang, textInputEditTextLebar, textInputEditTextPeraturanLahan, textInputEditTextHarga;
    private CheckBox checkBoxIrigasi, checkBoxListrik, checkBoxPeralatan, checkBoxKanopi;
    private RecyclerView recyclerViewFotoLahan;
    private MaterialButton materialButtonTambahFoto, materialButtonSubmit;

    private String namaLahan, alamat, idProvinsi, idKotaKabupaten, idKecamatan, idKelurahan, lokasi, noSertifikat, namaPemilik, ktpPemilik, longitude, latitude;
    private List<Uri> uriList = new ArrayList<>();
    private Uri img1, img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sewakan_lahan);

        namaLahan = getIntent().getStringExtra("NAMA_LAHAN");
        alamat = getIntent().getStringExtra("ALAMAT");
        idProvinsi = getIntent().getStringExtra("ID_PROVINSI");
        idKotaKabupaten = getIntent().getStringExtra("ID_KOTA_KABUPATEN");
        idKecamatan = getIntent().getStringExtra("ID_KECAMATAN");
        idKelurahan = getIntent().getStringExtra("ID_KELURAHAN");
        lokasi = getIntent().getStringExtra("LOKASI");
        noSertifikat = getIntent().getStringExtra("NO_SERTIFIKAT");
        namaPemilik = getIntent().getStringExtra("NAMA_PEMILIK");
        ktpPemilik = getIntent().getStringExtra("KTP_PEMILIK");
        longitude = getIntent().getStringExtra("LONGITUDE");
        latitude = getIntent().getStringExtra("LATITUDE");
        img1 = Uri.parse(getIntent().getStringExtra("IMG1"));
        img2 = Uri.parse(getIntent().getStringExtra("IMG2"));

        apiInterface = ApiClient.getClient();
        fotoLahanAdapter = new FotoLahanAdapter(uriList);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon tunggu sebentar...");
        progressDialog.setCancelable(false);

        textInputEditTextPanjang = findViewById(R.id.textInputEditTextPanjang);
        textInputEditTextLebar = findViewById(R.id.textInputEditTextLebar);
        textInputEditTextPeraturanLahan = findViewById(R.id.textInputEditTextPeraturanLahan);
        textInputEditTextHarga = findViewById(R.id.textInputEditTextHarga);
        checkBoxIrigasi = findViewById(R.id.checkBoxIrigasi);
        checkBoxListrik = findViewById(R.id.checkBoxListrik);
        checkBoxPeralatan = findViewById(R.id.checkBoxPeralatan);
        checkBoxKanopi = findViewById(R.id.checkBoxKanopi);
        recyclerViewFotoLahan = findViewById(R.id.recyclerViewFotoLahan);
        materialButtonTambahFoto = findViewById(R.id.materialButtonTambahFoto);
        materialButtonSubmit = findViewById(R.id.materialButtonSubmit);

        recyclerViewFotoLahan.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFotoLahan.setHasFixedSize(true);
        recyclerViewFotoLahan.setAdapter(fotoLahanAdapter);

        materialButtonTambahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(DetailSewakanLahanActivity.this)
                        .crop()
                        .compress(1024)
                        .start();
            }
        });

        materialButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                LahanModel.FasilitasModel fasilitasModel = new LahanModel.FasilitasModel(
                        checkBoxIrigasi.isChecked() ? "1" : "0",
                        checkBoxListrik.isChecked() ? "1" : "0",
                        checkBoxPeralatan.isChecked() ? "1" : "0",
                        checkBoxKanopi.isChecked() ? "1" : "0"
                );

                LahanModel lahanModel = new LahanModel(
                        AppPreference.getUser(v.getContext()).emailUser,
                        namaLahan,
                        alamat,
                        idKelurahan,
                        idKecamatan,
                        idKotaKabupaten,
                        idProvinsi,
                        lokasi,
                        namaPemilik,
                        textInputEditTextHarga.getText().toString(),
                        ktpPemilik,
                        noSertifikat,
                        latitude,
                        longitude,
                        textInputEditTextPanjang.getText().toString(),
                        textInputEditTextLebar.getText().toString(),
                        fasilitasModel,
                        textInputEditTextPeraturanLahan.getText().toString()
                );

                postLahan(lahanModel);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
//            shapeableImageViewProfil.setImageURI(data.getData());
//            Picasso.get()
//                    .load(data.getData())
//                    .into(shapeableImageViewProfil);
//            uri = data.getData();
            uriList.add(data.getData());
            fotoLahanAdapter.notifyDataSetChanged();
        }
    }

    public void postLahan(LahanModel model) {
        apiInterface.postLahan(
                jsonObjectLahan(model).toString()
        ).enqueue(new Callback<LahanResponse>() {
            @Override
            public void onResponse(Call<LahanResponse> call, Response<LahanResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        postFotoLahan(response.body().idLahan);
                    }
                }
            }

            @Override
            public void onFailure(Call<LahanResponse> call, Throwable t) {
                Log.e("postLahan", t.getMessage());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                new AlertDialog.Builder(DetailSewakanLahanActivity.this)
                        .setTitle("Pesan")
                        .setMessage("Terjadi kesalahan pada server, silahkan coba beberapa saat lagi")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    public void postFotoLahan(String idLahan) {
        apiInterface.postFotoLahan(
                RequestBody.create(MediaType.parse("text/plain"), idLahan),
                compressFile(img1, "foto1"),
                compressFile(img2, "foto2")
        ).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        for (int i = 0; i < FotoLahanAdapter.getList().size(); i++) {
                            postGaleriLahan(idLahan, FotoLahanAdapter.getList().get(i));
                            if ((i + 1) == FotoLahanAdapter.getList().size()) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                startActivity(new Intent(DetailSewakanLahanActivity.this, KonfirmasiSewakanLahanActivity.class));
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("postFotoLahan", t.getMessage());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                new AlertDialog.Builder(DetailSewakanLahanActivity.this)
                        .setTitle("Pesan")
                        .setMessage("Terjadi kesalahan pada server, silahkan coba beberapa saat lagi")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    public void postGaleriLahan(String idLahan, Uri uri) {
        apiInterface.postGaleriLahan(
                RequestBody.create(MediaType.parse("text/plain"), idLahan),
                compressFile(uri, "file")
        ).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        Log.e("postGaleriLahan", "Berhasil");
                    } else {
                        Log.e("postGaleriLahan", "Gagal");
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e("postGaleriLahan", t.getMessage());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                new AlertDialog.Builder(DetailSewakanLahanActivity.this)
                        .setTitle("Pesan")
                        .setMessage("Terjadi kesalahan pada server, silahkan coba beberapa saat lagi")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    public JSONObject jsonObjectLahan(LahanModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", model.email);
            jsonObject.put("namaLahan", model.namaLahan);
            jsonObject.put("alamat", model.alamat);
            jsonObject.put("kelurahan", model.kelurahan);
            jsonObject.put("kecamatan", model.kecamatan);
            jsonObject.put("kabkot", model.kabkot);
            jsonObject.put("provinsi", model.provinsi);
            jsonObject.put("lokasi", model.lokasi);
            jsonObject.put("pemilik", model.pemilik);
            jsonObject.put("harga", model.harga);
            jsonObject.put("noKTP", model.noKTP);
            jsonObject.put("noSertan", model.noSertan);
            jsonObject.put("latitude", model.latitude);
            jsonObject.put("longitude", model.longitude);
            jsonObject.put("panjang", model.panjang);
            jsonObject.put("lebar", model.lebar);
            jsonObject.put("fasilitas", jsonObjectFasilitas(model.fasilitas));
            jsonObject.put("peraturan", model.peraturan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject jsonObjectFasilitas(LahanModel.FasilitasModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isIrigasi", model.isIrigasi);
            jsonObject.put("isListrik", model.isListrik);
            jsonObject.put("isPeralatan", model.isPeralatan);
            jsonObject.put("isKanopi", model.isKanopi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private File createTempFile(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() +".JPEG");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        //write the bytes in file
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArray);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private MultipartBody.Part compressFile(Uri uri, String path) {
        File file = new File(uri.getPath());
        try {
            File fileCompress = new Compressor(this)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFile(file);
            File file1 = createTempFile(Uri.fromFile(fileCompress.getAbsoluteFile()));
            Log.e("path", file1.getAbsolutePath());
            return MultipartBody.Part.createFormData(path, file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}