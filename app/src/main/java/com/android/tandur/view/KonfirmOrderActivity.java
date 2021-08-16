package com.android.tandur.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.tandur.R;
import com.android.tandur.api.ApiClient;
import com.android.tandur.api.ApiInterface;
import com.google.android.material.button.MaterialButton;

public class KonfirmOrderActivity extends AppCompatActivity {
    private ApiInterface apiInterface;

    private TextView textViewNamaLahan, textViewHarga, textViewLuas, textViewFasilitas, textViewMulaiPada, textViewSelesaiPada, textViewPenyewa, textViewEmail, textViewNoTelepon, textViewDetailHarga, textViewItemLahan, textViewTotalBiayaSewa;
    private EditText editTextBulan;
    private MaterialButton materialButtonKurangBulan, materialButtonTambahBulan, materialButtonLanjutkan;

    private int bulan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirm_order);

        textViewNamaLahan = findViewById(R.id.textViewNamaLahan);
        textViewHarga = findViewById(R.id.textViewHarga);
        textViewLuas = findViewById(R.id.textViewLuas);
        textViewFasilitas = findViewById(R.id.textViewFasilitas);
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

        bulan = Integer.parseInt(editTextBulan.getText().toString());
        materialButtonKurangBulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bulan > 1) {
                    bulan--;
                    editTextBulan.setText(String.valueOf(bulan));
                }
            }
        });

        materialButtonTambahBulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bulan++;
                editTextBulan.setText(String.valueOf(bulan));
            }
        });

        materialButtonLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), MetodePembayaranActivity.class));
            }
        });
    }
}