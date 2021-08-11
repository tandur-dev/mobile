package com.android.tandur.view.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.tandur.MainActivity;
import com.android.tandur.R;
import com.android.tandur.api.ApiClient;
import com.android.tandur.api.ApiInterface;
import com.android.tandur.api.response.LoginResponse;
import com.android.tandur.api.response.RegisterResponse;
import com.android.tandur.view.AppIntroActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifikasiFotoActivity extends AppCompatActivity {
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private ImageView imageViewKTP, imageViewSelfie;
    private MaterialButton materialButtonVerifikasi;

    private String email, namaLengkap, noTelepon, alamat, idProvinsi, idKotaKabupaten, idKecamatan, idKelurahan;
    private Uri fotoProfil, fotoKtp, fotoSelfie;
    private boolean select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_foto);

        email = getIntent().getStringExtra("EMAIL");
        namaLengkap = getIntent().getStringExtra("NAMA_LENGKAP");
        noTelepon = getIntent().getStringExtra("NO_TELEPON");
        alamat = getIntent().getStringExtra("ALAMAT");
        idProvinsi = getIntent().getStringExtra("ID_PROVINSI");
        idKotaKabupaten = getIntent().getStringExtra("ID_KOTA_KABUPATEN");
        idKecamatan = getIntent().getStringExtra("ID_KECAMATAN");
        idKelurahan = getIntent().getStringExtra("ID_KELURAHAN");
        fotoProfil = Uri.parse(getIntent().getStringExtra("FOTO_PROFILE"));

        apiInterface = ApiClient.getClient();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Tunggu Sebentar...");
        progressDialog.setCancelable(false);

        imageViewKTP = findViewById(R.id.imageViewKTP);
        imageViewSelfie = findViewById(R.id.imageViewSelfie);
        materialButtonVerifikasi = findViewById(R.id.materialButtonVerifikasi);

        imageViewKTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = true;
                ImagePicker.Companion.with(VerifikasiFotoActivity.this)
                        .crop()
                        .compress(1024)
                        .start();
            }
        });

        imageViewSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = false;
                ImagePicker.Companion.with(VerifikasiFotoActivity.this)
                        .crop()
                        .compress(1024)
                        .start();
            }
        });

        materialButtonVerifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fotoKtp != null && fotoSelfie != null) {
                    register();
                } else {
                    Toast.makeText(VerifikasiFotoActivity.this, "Mohon upload foto KTP dan foto selfie anda", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (select) {
                Picasso.get()
                        .load(data.getData())
                        .into(imageViewKTP);
                fotoKtp = data.getData();
            } else {
                Picasso.get()
                        .load(data.getData())
                        .into(imageViewSelfie);
                fotoSelfie = data.getData();
            }
        }
    }

    public void register() {
        progressDialog.show();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("updateToken", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                String token = task.getResult();
                Log.d("token", token);

                apiInterface.register(
                        RequestBody.create(MediaType.parse("text/plain"), email),
                        RequestBody.create(MediaType.parse("text/plain"), namaLengkap),
                        RequestBody.create(MediaType.parse("text/plain"), noTelepon),
                        RequestBody.create(MediaType.parse("text/plain"), alamat),
                        RequestBody.create(MediaType.parse("text/plain"), idKelurahan),
                        RequestBody.create(MediaType.parse("text/plain"), idKecamatan),
                        RequestBody.create(MediaType.parse("text/plain"), idKotaKabupaten),
                        RequestBody.create(MediaType.parse("text/plain"), idProvinsi),
                        RequestBody.create(MediaType.parse("text/plain"), token),
                        compressFile(fotoKtp, "fotoKTP"),
                        compressFile(fotoSelfie, "fotoSelfie"),
                        compressFile(fotoProfil, "fotoProfil")
                ).enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (response.isSuccessful()) {
                            RegisterResponse registerResponse = response.body();
                            if (registerResponse.status) {
                                startActivity(new Intent(VerifikasiFotoActivity.this, KonfirmasiActivity.class));
                                finish();
                            } else {
                                new AlertDialog.Builder(VerifikasiFotoActivity.this)
                                        .setTitle("Pesan")
                                        .setMessage(registerResponse.message)
                                        .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Log.e("register", t.getMessage());
                    }
                });
            }
        });
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